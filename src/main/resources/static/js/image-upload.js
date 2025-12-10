"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var UPLOAD_ENDPOINT = "/api/upload/file/image";
var MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
var FILE_TYPE_VISUAL = "1";
var FILE_TYPE_FACE = "2";
var LOADING_OVERLAY_ID = "uploadLoadingOverlay";
var BODY_LOADING_CLASS = "upload-loading";
var activeUploadCount = 0;
function getWindowWithApi() {
    return window;
}
function getUploadElements(config) {
    var dropZone = document.getElementById(config.dropZoneId);
    if (!dropZone) {
        return null;
    }
    return {
        dropZone: dropZone,
        resultBox: config.resultBoxId ? document.getElementById(config.resultBoxId) : null,
        uniqueIdField: config.uniqueIdFieldId
            ? document.getElementById(config.uniqueIdFieldId)
            : null,
        previewContainer: config.previewContainerId ? document.getElementById(config.previewContainerId) : null,
        previewImage: config.previewImageId
            ? document.getElementById(config.previewImageId)
            : null,
    };
}
function renderResult(element, message, isError) {
    if (isError === void 0) { isError = false; }
    if (!element) {
        return;
    }
    element.textContent = message;
    element.classList.remove("text-muted");
    element.classList.toggle("text-danger", isError);
    element.classList.toggle("text-white", !isError);
}
function setGlobalLoading(isLoading) {
    var overlay = document.getElementById(LOADING_OVERLAY_ID);
    if (!overlay) {
        return;
    }
    activeUploadCount += isLoading ? 1 : -1;
    if (activeUploadCount < 0) {
        activeUploadCount = 0;
    }
    var shouldShow = activeUploadCount > 0;
    overlay.classList.toggle("is-active", shouldShow);
    overlay.setAttribute("aria-hidden", String(!shouldShow));
    document.body.classList.toggle(BODY_LOADING_CLASS, shouldShow);
    document.body.setAttribute("aria-busy", String(shouldShow));
}
function isGlobalLoading() {
    return activeUploadCount > 0;
}
function isImageFile(file) {
    return !!file.type && file.type.toLowerCase().startsWith("image/");
}
function updatePreview(base64Data, mimeType, previewContainer, previewImage) {
    if (!previewContainer || !previewImage || !base64Data) {
        return;
    }
    var resolvedMime = mimeType && mimeType.length > 0 ? mimeType : "image/png";
    previewImage.src = "data:".concat(resolvedMime, ";base64,").concat(base64Data);
    previewImage.hidden = false;
    previewContainer.classList.add("has-image");
}
function uploadImage(file, elements, fileType) {
    return __awaiter(this, void 0, void 0, function () {
        var formData, response, data, messages, joined, errorMessage, fileName, error_1;
        var _a;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0:
                    renderResult(elements.resultBox, "アップロード中...", false);
                    formData = new FormData();
                    formData.append("file", file);
                    formData.append("FileType", fileType);
                    setGlobalLoading(true);
                    _b.label = 1;
                case 1:
                    _b.trys.push([1, 4, 5, 6]);
                    return [4 /*yield*/, fetch(UPLOAD_ENDPOINT, {
                            method: "POST",
                            body: formData,
                        })];
                case 2:
                    response = _b.sent();
                    return [4 /*yield*/, response.json().catch(function () { return null; })];
                case 3:
                    data = _b.sent();
                    if (!data || data.isError) {
                        messages = (_a = data === null || data === void 0 ? void 0 : data.messages) !== null && _a !== void 0 ? _a : {};
                        joined = Object.values(messages)
                            .filter(function (value) { return typeof value === "string" && value.length > 0; })
                            .join("\n");
                        errorMessage = joined || "アップロードに失敗しました。";
                        renderResult(elements.resultBox, errorMessage, true);
                        return [2 /*return*/];
                    }
                    if (elements.uniqueIdField && typeof data.uniqueId === "string" && data.uniqueId.length > 0) {
                        elements.uniqueIdField.value = data.uniqueId;
                    }
                    updatePreview(data.base64Data, data.mimeType, elements.previewContainer, elements.previewImage);
                    fileName = typeof data.fileName === "string" && data.fileName.length > 0 ? data.fileName : file.name;
                    renderResult(elements.resultBox, "\u30A2\u30C3\u30D7\u30ED\u30FC\u30C9\u5B8C\u4E86: ".concat(fileName), false);
                    return [3 /*break*/, 6];
                case 4:
                    error_1 = _b.sent();
                    console.error("Upload failed", error_1);
                    renderResult(elements.resultBox, "ネットワークエラーが発生しました。", true);
                    return [3 /*break*/, 6];
                case 5:
                    setGlobalLoading(false);
                    return [7 /*endfinally*/];
                case 6: return [2 /*return*/];
            }
        });
    });
}
function handleFile(file, elements, fileType) {
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
function handleDrop(event, elements, fileType) {
    var _a;
    if (isGlobalLoading()) {
        return;
    }
    var files = (_a = event.dataTransfer) === null || _a === void 0 ? void 0 : _a.files;
    if (!files || !files.length) {
        renderResult(elements.resultBox, "ファイルがドロップされていません。", true);
        return;
    }
    handleFile(files[0], elements, fileType);
}
function initImageUpload(config) {
    var elements = getUploadElements(config);
    if (!elements) {
        return;
    }
    var dropZone = elements.dropZone;
    var preventDefault = function (event) {
        event.preventDefault();
        event.stopPropagation();
    };
    var setHighlight = function (shouldHighlight) {
        dropZone.classList.toggle("drag-over", shouldHighlight);
    };
    var setPressed = function (pressed) {
        dropZone.classList.toggle("is-pressed", pressed);
    };
    var clearPressed = function () { return setPressed(false); };
    dropZone.addEventListener("dragenter", function (event) {
        preventDefault(event);
        setHighlight(true);
    });
    dropZone.addEventListener("dragover", function (event) {
        preventDefault(event);
        setHighlight(true);
    });
    dropZone.addEventListener("dragleave", function (event) {
        preventDefault(event);
        setHighlight(false);
    });
    dropZone.addEventListener("drop", function (event) {
        preventDefault(event);
        setHighlight(false);
        clearPressed();
        handleDrop(event, elements, config.fileType);
    });
    var fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = "image/*";
    fileInput.hidden = true;
    dropZone.appendChild(fileInput);
    dropZone.addEventListener("mousedown", function () {
        if (isGlobalLoading()) {
            return;
        }
        setPressed(true);
    });
    dropZone.addEventListener("touchstart", function () {
        if (isGlobalLoading()) {
            return;
        }
        setPressed(true);
    });
    ["mouseup", "mouseleave", "touchend", "touchcancel", "blur"].forEach(function (eventName) {
        dropZone.addEventListener(eventName, clearPressed);
    });
    dropZone.addEventListener("click", function () { return fileInput.click(); });
    dropZone.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            setPressed(true);
            fileInput.click();
        }
    });
    dropZone.addEventListener("keyup", function (event) {
        if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            clearPressed();
        }
    });
    fileInput.addEventListener("change", function () {
        var _a;
        var file = (_a = fileInput.files) === null || _a === void 0 ? void 0 : _a[0];
        if (file) {
            handleFile(file, elements, config.fileType);
        }
        fileInput.value = "";
        clearPressed();
    });
}
function initUploadContainers(root) {
    if (root === void 0) { root = document; }
    var containers = root.querySelectorAll("[data-upload-file-type], [data-upload-image-type]");
    containers.forEach(function (container) {
        var _a;
        if (container.dataset.uploadInitialized === "true") {
            return;
        }
        var fileType = (_a = container.dataset.uploadFileType) !== null && _a !== void 0 ? _a : container.dataset.uploadImageType;
        var dropZoneId = container.dataset.uploadDropZoneId;
        if (!fileType || !dropZoneId) {
            return;
        }
        initImageUpload({
            fileType: fileType,
            dropZoneId: dropZoneId,
            resultBoxId: container.dataset.uploadResultId,
            uniqueIdFieldId: container.dataset.uploadUniqueIdFieldId,
            previewContainerId: container.dataset.uploadPreviewContainerId,
            previewImageId: container.dataset.uploadPreviewImageId,
        });
        container.dataset.uploadInitialized = "true";
    });
}
function bootstrapImageUploads() {
    initUploadContainers();
    var api = {
        register: function (config) { return initImageUpload(config); },
        registerMany: function (configs) { return configs.forEach(function (config) { return initImageUpload(config); }); },
        initFromDom: function (root) { return initUploadContainers(root !== null && root !== void 0 ? root : document); },
    };
    getWindowWithApi().MingshiuImageUpload = api;
}
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", bootstrapImageUploads);
}
else {
    bootstrapImageUploads();
}
