type VisualUploadElements = {
  dropZone        : HTMLElement;
  resultBox       : HTMLElement      | null;
  uniqueIdField   : HTMLInputElement | null;
  previewContainer: HTMLElement      | null;
  previewImage    : HTMLImageElement | null;
};

const UPLOAD_ENDPOINT   = "/api/resonators/base/temp-file/upload";
const MAX_FILE_SIZE     = 10 * 1024 * 1024; // 5MB
const VISUAL_IMAGE_TYPE = "1";

function getVisualUploadElements(): VisualUploadElements | null {
  const dropZone = document.getElementById("visualDropZone");
  if (!dropZone) {
    return null;
  }

  return {
    dropZone,
    resultBox        : document.getElementById("visualUploadResult"),
    uniqueIdField    : document.getElementById("visualUniqueId") as HTMLInputElement | null,
    previewContainer : document.getElementById("visualPreviewContainer"),
    previewImage     : document.getElementById("visualPreview") as HTMLImageElement | null,
  };
}

function renderResult(element: HTMLElement | null, message: string, isError = false): void {
  if (!element) {
    return;
  }
  element.textContent = message;
  element.classList.toggle("text-danger", isError);
  element.classList.toggle("text-success", !isError);
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

async function uploadVisualImage(file: File, elements: VisualUploadElements): Promise<void> {
  renderResult(elements.resultBox, "アップロード中...", false);
  const formData = new FormData();
  formData.append("file", file);
  formData.append("imageType", VISUAL_IMAGE_TYPE);

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
    renderResult(elements.resultBox, `アップロード成功: ${data.fileName ?? file.name}`, false);
  } catch (error) {
    console.error("Upload failed", error);
    renderResult(elements.resultBox, "ネットワークエラーが発生しました。", true);
  }
}

function handleFile(file: File, elements: VisualUploadElements): void {
  if (!isImageFile(file)) {
    renderResult(elements.resultBox, "画像ファイルのみアップロードできます。", true);
    return;
  }

  if (file.size > MAX_FILE_SIZE) {
    renderResult(elements.resultBox, "ファイルサイズは5MB以内にしてください。", true);
    return;
  }

  void uploadVisualImage(file, elements);
}

function handleDrop(event: DragEvent, elements: VisualUploadElements): void {
  const files = event.dataTransfer?.files;
  if (!files || !files.length) {
    renderResult(elements.resultBox, "ファイルが見つかりません。", true);
    return;
  }

  handleFile(files[0], elements);
}

function initializeVisualDropZone(): void {
  const elements = getVisualUploadElements();
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
    handleDrop(event, elements);
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
      handleFile(file, elements);
    }
    fileInput.value = "";
  });
}

document.addEventListener("DOMContentLoaded", () => {
  initializeVisualDropZone();
});
