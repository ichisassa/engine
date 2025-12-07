type UploadElements = {
  dropZone        : HTMLElement;
  resultBox       : HTMLElement      | null;
  uniqueIdField   : HTMLInputElement | null;
  previewContainer: HTMLElement      | null;
  previewImage    : HTMLImageElement | null;
};

type UploadConfig = {
  imageType        : string;
  dropZoneId       : string;
  resultBoxId      : string;
  uniqueIdFieldId  : string;
  previewContainerId: string;
  previewImageId   : string;
};

const UPLOAD_ENDPOINT   = "/api/resonators/base/temp-file/upload";
const MAX_FILE_SIZE     = 10 * 1024 * 1024; // 10MB
const VISUAL_IMAGE_TYPE = "1";
const FACE_IMAGE_TYPE   = "2";

function getUploadElements(config: UploadConfig): UploadElements | null {
  const dropZone = document.getElementById(config.dropZoneId);
  if (!dropZone) {
    return null;
  }

  return {
    dropZone,
    resultBox        : document.getElementById(config.resultBoxId),
    uniqueIdField    : document.getElementById(config.uniqueIdFieldId) as HTMLInputElement | null,
    previewContainer : document.getElementById(config.previewContainerId),
    previewImage     : document.getElementById(config.previewImageId) as HTMLImageElement | null,
  };
}

function renderResult(element: HTMLElement | null, message: string, isError = false): void {
  if (!element) {
    return;
  }
  element.textContent = message;
  element.classList.remove("text-muted");
  element.classList.toggle("text-danger", isError);
  element.classList.toggle("text-white", !isError);
}

function isImageFile(file: File): boolean {
  return !!file.type && file.type.toLowerCase().startsWith("image/");
}

function updatePreview(
  file: File,
  previewContainer: HTMLElement | null,
  previewImage: HTMLImageElement | null,
): void {
  if (!previewContainer || !previewImage) {
    return;
  }
  const objectUrl = URL.createObjectURL(file);
  previewImage.src = objectUrl;
  previewImage.hidden = false;
  previewContainer.classList.add("has-image");
  previewImage.onload = () => {
    URL.revokeObjectURL(objectUrl);
  };
}

async function uploadImage(
  file: File,
  elements: UploadElements,
  imageType: string,
): Promise<void> {
  renderResult(elements.resultBox, "アップロード中...", false);
  const formData = new FormData();
  formData.append("file", file);
  formData.append("imageType", imageType);

  try {
    const response = await fetch(UPLOAD_ENDPOINT, {
      method: "POST",
      body: formData,
    });

    if (!response.ok) {
      const errorBody = await response.json().catch(() => ({}));
      const message = typeof errorBody.message === "string" ? errorBody.message : "アップロードに失敗しました。";
      renderResult(elements.resultBox, message, true);
      return;
    }

    const data = await response.json();
    if (elements.uniqueIdField) {
      elements.uniqueIdField.value = data.uniqueId ?? "";
    }
    updatePreview(file, elements.previewContainer, elements.previewImage);
    renderResult(elements.resultBox, `アップロード完了: ${data.fileName ?? file.name}`, false);
  } catch (error) {
    console.error("Upload failed", error);
    renderResult(elements.resultBox, "ネットワークエラーが発生しました。", true);
  }
}

function handleFile(file: File, elements: UploadElements, imageType: string): void {
  if (!isImageFile(file)) {
    renderResult(elements.resultBox, "画像ファイルのみアップロードできます。", true);
    return;
  }

  if (file.size > MAX_FILE_SIZE) {
    renderResult(elements.resultBox, "ファイルサイズは10MB以下にしてください。", true);
    return;
  }

  void uploadImage(file, elements, imageType);
}

function handleDrop(event: DragEvent, elements: UploadElements, imageType: string): void {
  const files = event.dataTransfer?.files;
  if (!files || !files.length) {
    renderResult(elements.resultBox, "ファイルが選択されていません。", true);
    return;
  }

  handleFile(files[0], elements, imageType);
}

function initImageUpload(config: UploadConfig): void {
  const elements = getUploadElements(config);
  if (!elements) {
    return;
  }

  const { dropZone } = elements;

  const preventDefault = (event: DragEvent): void => {
    event.preventDefault();
    event.stopPropagation();
  };

  const setHighlight = (shouldHighlight: boolean): void => {
    dropZone.classList.toggle("drag-over", shouldHighlight);
  };

  dropZone.addEventListener("dragenter", (event) => {
    preventDefault(event);
    setHighlight(true);
  });

  dropZone.addEventListener("dragover", (event) => {
    preventDefault(event);
    setHighlight(true);
  });

  dropZone.addEventListener("dragleave", (event) => {
    preventDefault(event);
    setHighlight(false);
  });

  dropZone.addEventListener("drop", (event) => {
    preventDefault(event);
    setHighlight(false);
    handleDrop(event, elements, config.imageType);
  });

  const fileInput = document.createElement("input");
  fileInput.type = "file";
  fileInput.accept = "image/*";
  fileInput.hidden = true;
  dropZone.appendChild(fileInput);

  dropZone.addEventListener("click", () => fileInput.click());
  dropZone.addEventListener("keydown", (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault();
      fileInput.click();
    }
  });

  fileInput.addEventListener("change", () => {
    const file = fileInput.files?.[0];
    if (file) {
      handleFile(file, elements, config.imageType);
    }
    fileInput.value = "";
  });
}

document.addEventListener("DOMContentLoaded", () => {
  initImageUpload({
    imageType        : VISUAL_IMAGE_TYPE,
    dropZoneId       : "visualDropZone",
    resultBoxId      : "visualUploadResult",
    uniqueIdFieldId  : "visualUniqueId",
    previewContainerId: "visualPreviewContainer",
    previewImageId   : "visualPreview",
  });

  initImageUpload({
    imageType        : FACE_IMAGE_TYPE,
    dropZoneId       : "faceDropZone",
    resultBoxId      : "faceUploadResult",
    uniqueIdFieldId  : "faceUniqueId",
    previewContainerId: "facePreviewContainer",
    previewImageId   : "facePreview",
  });
});
