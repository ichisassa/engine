"use strict";
var BATTLE_STYLE_MODAL_ID = "battle-style-modal";
var BATTLE_STYLE_SELECTORS = {
    trigger: "#battle-style-trigger",
    modal: "#" + BATTLE_STYLE_MODAL_ID,
    list: "#battle-style-list",
    placeholder: "#battle-style-empty",
    apply: "[data-battle-style-apply]",
    cancel: "[data-battle-style-cancel]",
    checkbox: ".battle-style-option-input",
};
function queryElement(selector) {
    return document.querySelector(selector);
}
function toggleBattleStyleModal(shouldOpen) {
    var modal = queryElement(BATTLE_STYLE_SELECTORS.modal);
    if (!modal) {
        return;
    }
    if (typeof $ === "function") {
        $(modal).modal(shouldOpen ? "show" : "hide");
        return;
    }
    modal.classList.toggle("show", shouldOpen);
    modal.setAttribute("aria-hidden", shouldOpen ? "false" : "true");
    modal.style.display = shouldOpen ? "block" : "none";
    if (shouldOpen) {
        document.body.classList.add("modal-open");
    }
    else {
        document.body.classList.remove("modal-open");
    }
}
function gatherSelectedStyles() {
    var modal = queryElement(BATTLE_STYLE_SELECTORS.modal);
    if (!modal) {
        return [];
    }
    var checked = modal.querySelectorAll(BATTLE_STYLE_SELECTORS.checkbox + ":checked");
    var selected = [];
    Array.prototype.forEach.call(checked, function (checkbox) {
        var value = checkbox.value.trim();
        if (value) {
            selected.push(value);
        }
    });
    return selected;
}
function renderBattleStyleList(values) {
    var list = queryElement(BATTLE_STYLE_SELECTORS.list);
    var placeholder = queryElement(BATTLE_STYLE_SELECTORS.placeholder);
    if (!list || !placeholder) {
        return;
    }
    list.innerHTML = "";
    if (!values.length) {
        placeholder.classList.remove("d-none");
        return;
    }
    placeholder.classList.add("d-none");
    var fragment = document.createDocumentFragment();
    values.forEach(function (text) {
        var item = document.createElement("div");
        item.className = "style-item";
        item.setAttribute("role", "listitem");
        item.textContent = text;
        fragment.appendChild(item);
    });
    list.appendChild(fragment);
}
function focusBattleStyleTrigger() {
    var trigger = queryElement(BATTLE_STYLE_SELECTORS.trigger);
    if (trigger) {
        trigger.focus();
    }
}
document.addEventListener("click", function (event) {
    var target = event.target;
    if (!target) {
        return;
    }
    if (target.closest(BATTLE_STYLE_SELECTORS.trigger)) {
        event.preventDefault();
        toggleBattleStyleModal(true);
        return;
    }
    if (target.closest(BATTLE_STYLE_SELECTORS.apply)) {
        event.preventDefault();
        var selections = gatherSelectedStyles();
        renderBattleStyleList(selections);
        toggleBattleStyleModal(false);
        return;
    }
    if (target.closest(BATTLE_STYLE_SELECTORS.cancel)) {
        event.preventDefault();
        toggleBattleStyleModal(false);
    }
});
document.addEventListener("hidden.bs.modal", function (event) {
    var modal = event.target;
    if (modal && modal.id === BATTLE_STYLE_MODAL_ID) {
        focusBattleStyleTrigger();
    }
});
