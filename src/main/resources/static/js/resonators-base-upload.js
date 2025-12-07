"use strict";
var UPLOAD_ENDPOINT = "/api/resonators/base/temp-file/upload";
var MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
var VISUAL_IMAGE_TYPE = "1";
var FACE_IMAGE_TYPE = "2";
function getUploadElements(config) {
    var dropZone = document.getElementById(config.dropZoneId);
    if (!dropZone) {
        return null;
    }
    return {
        dropZone: dropZone,
        resultBox: document.getElementById(config.resultBoxId),
        uniqueIdField: document.getElementById(config.uniqueIdFieldId),
        previewContainer: document.getElementById(config.previewContainerId),
        previewImage: document.getElementById(config.previewImageId),
    };
}
function renderResult(element, message, isError) {
    if (isError === void 0) { isError = false; }
    if (!element) {
        return;
    }
    element.textContent = message;
    element.classList.toggle("text-danger", isError);
    element.classList.toggle("text-success", !isError);
}
function isImageFile(file) {
    return !!file.type && file.type.toLowerCase().startsWith("image/");
}
function updatePreview(file, previewContainer, previewImage) {
    if (!previewContainer || !previewImage) {
        return;
    }
    var objectUrl = URL.createObjectURL(file);
    previewImage.src = objectUrl;
    previewImage.hidden = false;
    previewContainer.classList.add("has-image");
    previewImage.onload = function () {
        URL.revokeObjectURL(objectUrl);
    };
}
function uploadImage(file, elements, imageType) {
    renderResult(elements.resultBox, "アップロード中...", false);
    var formData = new FormData();
    formData.append("file", file);
    formData.append("imageType", imageType);
    fetch(UPLOAD_ENDPOINT, {
        method: "POST",
        body: formData,
    })
        .then(function (response) {
        if (!response.ok) {
            return response.json()
                .catch(function () { return {}; })
                .then(function (body) {
                var message = typeof body.message === "string" ? body.message : "アップロードに失敗しました。";
                throw new Error(message);
            });
        }
        return response.json();
    })
        .then(function (data) {
        if (elements.uniqueIdField) {
            elements.uniqueIdField.value = data.uniqueId || "";
        }
        updatePreview(file, elements.previewContainer, elements.previewImage);
        renderResult(elements.resultBox, "アップロード完了: ".concat(data.fileName || file.name), false);
    })
        .catch(function (error) {
        var message = (error && error.message) || "ネットワークエラーが発生しました。";
        renderResult(elements.resultBox, message, true);
    });
}
function handleFile(file, elements, imageType) {
    if (!isImageFile(file)) {
        renderResult(elements.resultBox, "画像ファイルのみアップロードできます。", true);
        return;
    }
    if (file.size > MAX_FILE_SIZE) {
        renderResult(elements.resultBox, "ファイルサイズは10MB以下にしてください。", true);
        return;
    }
    uploadImage(file, elements, imageType);
}
function handleDrop(event, elements, imageType) {
    var _a;
    var files = (_a = event.dataTransfer) === null || _a === void 0 ? void 0 : _a.files;
    if (!files || !files.length) {
        renderResult(elements.resultBox, "ファイルが選択されていません。", true);
        return;
    }
    handleFile(files[0], elements, imageType);
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
        handleDrop(event, elements, config.imageType);
    });
    var fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = "image/*";
    fileInput.hidden = true;
    dropZone.appendChild(fileInput);
    dropZone.addEventListener("click", function () { return fileInput.click(); });
    dropZone.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
            event.preventDefault();
            fileInput.click();
        }
    });
    fileInput.addEventListener("change", function () {
        var _a;
        var file = (_a = fileInput.files) === null || _a === void 0 ? void 0 : _a[0];
        if (file) {
            handleFile(file, elements, config.imageType);
        }
        fileInput.value = "";
    });
}
document.addEventListener("DOMContentLoaded", function () {
    initImageUpload({
        imageType: VISUAL_IMAGE_TYPE,
        dropZoneId: "visualDropZone",
        resultBoxId: "visualUploadResult",
        uniqueIdFieldId: "visualUniqueId",
        previewContainerId: "visualPreviewContainer",
        previewImageId: "visualPreview",
    });
    initImageUpload({
        imageType: FACE_IMAGE_TYPE,
        dropZoneId: "faceDropZone",
        resultBoxId: "faceUploadResult",
        uniqueIdFieldId: "faceUniqueId",
        previewContainerId: "facePreviewContainer",
        previewImageId: "facePreview",
    });
});
