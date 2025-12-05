"use strict";
var CONTENT_ROOT_ID = "content-root";
var NAV_LINK_SELECTOR = "a.js-nav";
var PARTIAL_PARAM = "partial=content";
var PAGE_TITLE_ID = "page-title";
var DOCUMENT_TITLE_ID = "document-title";
var STATUS_TREE_SELECTOR = ".status-nav";
function setActiveNav(activePage) {
    var navLinks = document.querySelectorAll("".concat(NAV_LINK_SELECTOR, "[data-nav-key]"));
    navLinks.forEach(function (link) {
        var navKey = link.getAttribute("data-nav-key");
        link.classList.toggle("active", navKey === activePage);
    });
    var statusTree = document.querySelector(STATUS_TREE_SELECTOR);
    if (!statusTree) {
        return;
    }
    var isStatusPage = activePage.startsWith("status-");
    statusTree.classList.toggle("menu-open", isStatusPage);
    var toggleLink = statusTree.querySelector(":scope > .nav-link");
    if (toggleLink) {
        toggleLink.classList.toggle("active", isStatusPage);
        toggleLink.setAttribute("aria-expanded", String(isStatusPage));
    }
}
function applyFragmentMetadata(container) {
    var _a;
    var fragmentRoot = container.querySelector("[data-page-title]");
    if (!fragmentRoot) {
        return;
    }
    var newTitle = fragmentRoot.getAttribute("data-page-title");
    if (newTitle) {
        var pageHeading = document.getElementById(PAGE_TITLE_ID);
        if (pageHeading) {
            pageHeading.textContent = newTitle;
        }
        var docTitle = document.getElementById(DOCUMENT_TITLE_ID);
        if (docTitle) {
            var suffix = (_a = docTitle.getAttribute("data-title-suffix")) !== null && _a !== void 0 ? _a : "";
            docTitle.textContent = "".concat(newTitle).concat(suffix);
        }
        else {
            document.title = newTitle;
        }
    }
    var activePage = fragmentRoot.getAttribute("data-active-page");
    if (activePage) {
        setActiveNav(activePage);
    }
}
/**
 * Fetch the requested page but only return the fragment used in the main content area.
 */
function loadPartialContent(url, pushHistory) {
    if (pushHistory === void 0) { pushHistory = true; }
    var separator = url.includes("?") ? "&" : "?";
    var partialUrl = "".concat(url).concat(separator).concat(PARTIAL_PARAM);
    fetch(partialUrl, { method: "GET", credentials: "same-origin" })
        .then(function (res) {
        if (!res.ok) {
            throw new Error("HTTP error: ".concat(res.status));
        }
        return res.text();
    })
        .then(function (html) {
        var root = document.getElementById(CONTENT_ROOT_ID);
        if (!root) {
            console.warn("#".concat(CONTENT_ROOT_ID, " \u304C\u898B\u3064\u304B\u308A\u307E\u305B\u3093"));
            return;
        }
        root.innerHTML = html;
        applyFragmentMetadata(root);
        if (pushHistory) {
            window.history.pushState({}, "", url);
        }
    })
        .catch(function (err) {
        console.error("partial load failed:", err);
    });
}
/**
 * Intercept navigation clicks on .js-nav elements and swap to partial rendering.
 */
function setupPartialNav() {
    document.addEventListener("click", function (event) {
        var target = event.target;
        if (!target) {
            return;
        }
        var link = target.closest(NAV_LINK_SELECTOR);
        if (!link) {
            return;
        }
        var href = link.getAttribute("href");
        if (!href || href.startsWith("#")) {
            return;
        }
        event.preventDefault();
        loadPartialContent(href, true);
    });
    window.addEventListener("popstate", function () {
        var url = "".concat(window.location.pathname).concat(window.location.search);
        loadPartialContent(url, false);
    });
}
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", setupPartialNav);
}
else {
    setupPartialNav();
}
//# sourceMappingURL=partial-nav.js.map