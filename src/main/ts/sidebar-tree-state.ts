interface TreeState {
  [menuId: string]: boolean;
}

const STORAGE_KEY = 'sidebarTreeState';
const MENU_SELECTOR = 'li.nav-item.has-treeview[data-menu-id]';
const PARENT_LINK_SELECTOR = ':scope > a.nav-link';
const SAVE_DELAY_MS = 50;

function loadTreeState(): TreeState {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return {};
    }
    const parsed = JSON.parse(raw);
    return typeof parsed === 'object' && parsed !== null ? (parsed as TreeState) : {};
  } catch (error) {
    console.warn('Failed to load sidebar tree state', error);
    return {};
  }
}

function saveTreeState(state: TreeState): void {
  try {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  } catch (error) {
    console.warn('Failed to save sidebar tree state', error);
  }
}

function setMenuState(element: HTMLLIElement, isOpen: boolean): void {
  element.classList.toggle('menu-open', isOpen);
  const toggle = element.querySelector<HTMLAnchorElement>(PARENT_LINK_SELECTOR);
  if (toggle) {
    toggle.setAttribute('aria-expanded', String(isOpen));
  }
}

function captureState(
  items: HTMLLIElement[],
  state: TreeState
): TreeState {
  for (const item of items) {
    const menuId = item.dataset.menuId;
    if (!menuId) {
      continue;
    }
    state[menuId] = item.classList.contains('menu-open');
  }
  return state;
}

function attachToggleHandlers(
  items: HTMLLIElement[],
  state: TreeState
): void {
  for (const item of items) {
    const menuId = item.dataset.menuId;
    if (!menuId) {
      continue;
    }
    const toggle = item.querySelector<HTMLAnchorElement>(PARENT_LINK_SELECTOR);
    if (!toggle) {
      continue;
    }

    toggle.addEventListener('click', () => {
      window.setTimeout(() => {
        state[menuId] = item.classList.contains('menu-open');
        saveTreeState(state);
      }, SAVE_DELAY_MS);
    });
  }
}

function initSidebarTreeState(): void {
  const items = Array.from(
    document.querySelectorAll<HTMLLIElement>(MENU_SELECTOR)
  );
  if (!items.length) {
    return;
  }

  const state = loadTreeState();

  for (const item of items) {
    const menuId = item.dataset.menuId;
    if (!menuId) {
      continue;
    }
    if (Object.prototype.hasOwnProperty.call(state, menuId)) {
      setMenuState(item, !!state[menuId]);
    } else {
      state[menuId] = item.classList.contains('menu-open');
    }
  }

  attachToggleHandlers(items, state);

  window.addEventListener('beforeunload', () => {
    captureState(items, state);
    saveTreeState(state);
  });
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initSidebarTreeState);
} else {
  initSidebarTreeState();
}

export {};
