var STORAGE_KEY = 'sidebarTreeState';
var MENU_SELECTOR = 'li.nav-item.has-treeview[data-menu-id]';
var PARENT_LINK_SELECTOR = ':scope > a.nav-link';
var SAVE_DELAY_MS = 50;
function loadTreeState() {
    try {
        var raw = window.localStorage.getItem(STORAGE_KEY);
        if (!raw) {
            return {};
        }
        var parsed = JSON.parse(raw);
        return typeof parsed === 'object' && parsed !== null ? parsed : {};
    }
    catch (error) {
        console.warn('Failed to load sidebar tree state', error);
        return {};
    }
}
function saveTreeState(state) {
    try {
        window.localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
    }
    catch (error) {
        console.warn('Failed to save sidebar tree state', error);
    }
}
function setMenuState(element, isOpen) {
    element.classList.toggle('menu-open', isOpen);
    var toggle = element.querySelector(PARENT_LINK_SELECTOR);
    if (toggle) {
        toggle.setAttribute('aria-expanded', String(isOpen));
    }
}
function captureState(items, state) {
    for (var _i = 0, items_1 = items; _i < items_1.length; _i++) {
        var item = items_1[_i];
        var menuId = item.dataset.menuId;
        if (!menuId) {
            continue;
        }
        state[menuId] = item.classList.contains('menu-open');
    }
    return state;
}
function attachToggleHandlers(items, state) {
    var _loop_1 = function (item) {
        var menuId = item.dataset.menuId;
        if (!menuId) {
            return "continue";
        }
        var toggle = item.querySelector(PARENT_LINK_SELECTOR);
        if (!toggle) {
            return "continue";
        }
        toggle.addEventListener('click', function () {
            window.setTimeout(function () {
                state[menuId] = item.classList.contains('menu-open');
                saveTreeState(state);
            }, SAVE_DELAY_MS);
        });
    };
    for (var _i = 0, items_2 = items; _i < items_2.length; _i++) {
        var item = items_2[_i];
        _loop_1(item);
    }
}
function initSidebarTreeState() {
    var items = Array.from(document.querySelectorAll(MENU_SELECTOR));
    if (!items.length) {
        return;
    }
    var state = loadTreeState();
    for (var _i = 0, items_3 = items; _i < items_3.length; _i++) {
        var item = items_3[_i];
        var menuId = item.dataset.menuId;
        if (!menuId) {
            continue;
        }
        if (Object.prototype.hasOwnProperty.call(state, menuId)) {
            setMenuState(item, !!state[menuId]);
        }
        else {
            state[menuId] = item.classList.contains('menu-open');
        }
    }
    attachToggleHandlers(items, state);
    window.addEventListener('beforeunload', function () {
        captureState(items, state);
        saveTreeState(state);
    });
}
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initSidebarTreeState);
}
else {
    initSidebarTreeState();
}
export {};
//# sourceMappingURL=sidebar-tree-state.js.map