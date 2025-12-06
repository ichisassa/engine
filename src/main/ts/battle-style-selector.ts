declare const $: any;

const BATTLE_STYLE_MODAL_ID = "battle-style-modal";
const BATTLE_STYLE_SELECTORS = {
  trigger: "#battle-style-trigger",
  modal: `#${BATTLE_STYLE_MODAL_ID}`,
  list: "#battle-style-list",
  placeholder: "#battle-style-empty",
  apply: "[data-battle-style-apply]",
  cancel: "[data-battle-style-cancel]",
  checkbox: ".battle-style-option-input",
};

function queryElement<T extends Element>(selector: string): T | null {
  return document.querySelector<T>(selector);
}

function toggleBattleStyleModal(shouldOpen: boolean): void {
  const modal = queryElement<HTMLElement>(BATTLE_STYLE_SELECTORS.modal);
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
  } else {
    document.body.classList.remove("modal-open");
  }
}

function gatherSelectedStyles(): string[] {
  const modal = queryElement<HTMLElement>(BATTLE_STYLE_SELECTORS.modal);
  if (!modal) {
    return [];
  }

  const checked = modal.querySelectorAll<HTMLInputElement>(
    `${BATTLE_STYLE_SELECTORS.checkbox}:checked`
  );
  const selected: string[] = [];
  Array.prototype.forEach.call(checked, (checkbox: HTMLInputElement) => {
    const value = checkbox.value.trim();
    if (value) {
      selected.push(value);
    }
  });
  return selected;
}

function renderBattleStyleList(values: string[]): void {
  const list = queryElement<HTMLElement>(BATTLE_STYLE_SELECTORS.list);
  const placeholder = queryElement<HTMLElement>(BATTLE_STYLE_SELECTORS.placeholder);
  if (!list || !placeholder) {
    return;
  }

  list.innerHTML = "";
  if (!values.length) {
    placeholder.classList.remove("d-none");
    return;
  }

  placeholder.classList.add("d-none");
  const fragment = document.createDocumentFragment();
  values.forEach((text) => {
    const item = document.createElement("div");
    item.className = "style-item";
    item.setAttribute("role", "listitem");
    item.textContent = text;
    fragment.appendChild(item);
  });
  list.appendChild(fragment);
}

function focusBattleStyleTrigger(): void {
  const trigger = queryElement<HTMLButtonElement>(BATTLE_STYLE_SELECTORS.trigger);
  trigger?.focus();
}

document.addEventListener("click", (event) => {
  const target = event.target as HTMLElement | null;
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
    const selections = gatherSelectedStyles();
    renderBattleStyleList(selections);
    toggleBattleStyleModal(false);
    return;
  }

  if (target.closest(BATTLE_STYLE_SELECTORS.cancel)) {
    event.preventDefault();
    toggleBattleStyleModal(false);
  }
});

document.addEventListener("hidden.bs.modal", (event) => {
  const modal = event.target as HTMLElement | null;
  if (modal && modal.id === BATTLE_STYLE_MODAL_ID) {
    focusBattleStyleTrigger();
  }
});
