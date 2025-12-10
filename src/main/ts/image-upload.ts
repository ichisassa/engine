type UploadElements = {
  dropZone: HTMLElement;
  resultBox: HTMLElement | null;
  uniqueIdField: HTMLInputElement | null;
  previewContainer: HTMLElement | null;
  previewImage: HTMLImageElement | null;
};

type UploadConfig = {
  fileType: string;
  dropZoneId: string;
  resultBoxId?: string;
  uniqueIdFieldId?: string;
  previewContainerId?: string;
  previewImageId?: string;
};

type UploadApiResponse = {
  isError?: boolean;
  messages?: Record<string, string>;
  uniqueId?: string;
  fileName?: string;
};

const UPLOAD_ENDPOINT = "/api/upload/file/image";
const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
const FILE_TYPE_VISUAL = "1";
const FILE_TYPE_FACE = "2";
const LOADING_OVERLAY_ID = "uploadLoadingOverlay";
const BODY_LOADING_CLASS = "upload-loading";
type ImageUploadApi = {
  register: (config: UploadConfig) => void;
  registerMany: (configs: UploadConfig[]) => void;
  initFromDom: (root?: ParentNode) => void;
};

let activeUploadCount = 0;

function getWindowWithApi(): Window & { MingshiuImageUpload?: ImageUploadApi } {
  return window as Window & { MingshiuImageUpload?: ImageUploadApi };
}

function getUploadElements(config: UploadConfig): UploadElements | null {
  const dropZone = document.getElementById(config.dropZoneId);
  if (!dropZone) {
    return null;
  }

  return {
    dropZone,
    resultBox: config.resultBoxId ? document.getElementById(config.resultBoxId) : null,
    uniqueIdField: config.uniqueIdFieldId
      ? (document.getElementById(config.uniqueIdFieldId) as HTMLInputElement | null)
      : null,
    previewContainer: config.previewContainerId ? document.getElementById(config.previewContainerId) : null,
    previewImage: config.previewImageId
      ? (document.getElementById(config.previewImageId) as HTMLImageElement | null)
      : null,
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

function setGlobalLoading(isLoading: boolean): void {
  const overlay = document.getElementById(LOADING_OVERLAY_ID);
  if (!overlay) {
    return;
  }
  activeUploadCount += isLoading ? 1 : -1;
  if (activeUploadCount < 0) {
    activeUploadCount = 0;
  }
  const shouldShow = activeUploadCount > 0;
  overlay.classList.toggle("is-active", shouldShow);
  overlay.setAttribute("aria-hidden", String(!shouldShow));
  document.body.classList.toggle(BODY_LOADING_CLASS, shouldShow);
  document.body.setAttribute("aria-busy", String(shouldShow));
}

function isGlobalLoading(): boolean {
  return activeUploadCount > 0;
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
  fileType: string,
): Promise<void> {
  renderResult(elements.resultBox, "アップロード中...", false);
  const formData = new FormData();
  formData.append("file", file);
  formData.append("FileType", fileType);

  setGlobalLoading(true);
  try {
    const response = await fetch(UPLOAD_ENDPOINT, {
      method: "POST",
      body: formData,
    });

    const data: UploadApiResponse | null = await response.json().catch(() => null);
    if (!data || data.isError) {
      const messages = data?.messages ?? {};
      const joined = Object.values(messages)
        .filter((value): value is string => typeof value === "string" && value.length > 0)
        .join("\n");
      const errorMessage = joined || "アップロードに失敗しました。";
      renderResult(elements.resultBox, errorMessage, true);
      return;
    }

    if (elements.uniqueIdField && typeof data.uniqueId === "string" && data.uniqueId.length > 0) {
      elements.uniqueIdField.value = data.uniqueId;
    }
    updatePreview(file, elements.previewContainer, elements.previewImage);
    const fileName = typeof data.fileName === "string" && data.fileName.length > 0 ? data.fileName : file.name;
    renderResult(elements.resultBox, `アップロード完了: ${fileName}`, false);
  } catch (error) {
    console.error("Upload failed", error);
    renderResult(elements.resultBox, "ネットワークエラーが発生しました。", true);
  } finally {
    setGlobalLoading(false);
  }
}

function handleFile(file: File, elements: UploadElements, fileType: string): void {
  if (isGlobalLoading()) {
    return;
  }
  if (!isImageFile(file)) {
    renderResult(elements.resultBox, "画像ファイルのみアップロードできます。", true);
    return;
  }

  if (file.size > MAX_FILE_SIZE) {
    renderResult(elements.resultBox, "ファイルサイズが10MBを超えています。", true);
    return;
  }

  void uploadImage(file, elements, fileType);
}

function handleDrop(event: DragEvent, elements: UploadElements, fileType: string): void {
  if (isGlobalLoading()) {
    return;
  }
  const files = event.dataTransfer?.files;
  if (!files || !files.length) {
    renderResult(elements.resultBox, "ファイルがドロップされていません。", true);
    return;
  }

  handleFile(files[0], elements, fileType);
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

  const setPressed = (pressed: boolean): void => {
    dropZone.classList.toggle("is-pressed", pressed);
  };

  const clearPressed = (): void => setPressed(false);

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
    clearPressed();
    handleDrop(event, elements, config.fileType);
  });

  const fileInput = document.createElement("input");
  fileInput.type = "file";
  fileInput.accept = "image/*";
  fileInput.hidden = true;
  dropZone.appendChild(fileInput);

  dropZone.addEventListener("mousedown", () => {
    if (isGlobalLoading()) {
      return;
    }
    setPressed(true);
  });

  dropZone.addEventListener("touchstart", () => {
    if (isGlobalLoading()) {
      return;
    }
    setPressed(true);
  });

  ["mouseup", "mouseleave", "touchend", "touchcancel", "blur"].forEach((eventName) => {
    dropZone.addEventListener(eventName, clearPressed);
  });

  dropZone.addEventListener("click", () => fileInput.click());
  dropZone.addEventListener("keydown", (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault();
      setPressed(true);
      fileInput.click();
    }
  });
  dropZone.addEventListener("keyup", (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault();
      clearPressed();
    }
  });

  fileInput.addEventListener("change", () => {
    const file = fileInput.files?.[0];
    if (file) {
      handleFile(file, elements, config.fileType);
    }
    fileInput.value = "";
    clearPressed();
  });
}

function initUploadContainers(root: ParentNode = document): void {
  const containers = root.querySelectorAll<HTMLElement>("[data-upload-file-type], [data-upload-image-type]");
  containers.forEach((container) => {
    if (container.dataset.uploadInitialized === "true") {
      return;
    }
    const fileType = container.dataset.uploadFileType ?? container.dataset.uploadImageType;
    const dropZoneId = container.dataset.uploadDropZoneId;
    if (!fileType || !dropZoneId) {
      return;
    }
    initImageUpload({
      fileType,
      dropZoneId,
      resultBoxId: container.dataset.uploadResultId,
      uniqueIdFieldId: container.dataset.uploadUniqueIdFieldId,
      previewContainerId: container.dataset.uploadPreviewContainerId,
      previewImageId: container.dataset.uploadPreviewImageId,
    });
    container.dataset.uploadInitialized = "true";
  });
}

function bootstrapImageUploads(): void {
  initUploadContainers();
  const api: ImageUploadApi = {
    register: (config: UploadConfig) => initImageUpload(config),
    registerMany: (configs: UploadConfig[]) => configs.forEach((config) => initImageUpload(config)),
    initFromDom: (root?: ParentNode) => initUploadContainers(root ?? document),
  };
  getWindowWithApi().MingshiuImageUpload = api;
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", bootstrapImageUploads);
} else {
  bootstrapImageUploads();
}
