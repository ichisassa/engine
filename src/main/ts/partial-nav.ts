const CONTENT_ROOT_ID = "content-root";
const NAV_LINK_SELECTOR = "a.js-nav";
const PARTIAL_PARAM = "partial=content";
const PAGE_TITLE_ID = "page-title";
const DOCUMENT_TITLE_ID = "document-title";
const STATUS_TREE_SELECTOR = ".status-nav";

function setActiveNav(activePage: string): void {
  const navLinks = document.querySelectorAll<HTMLAnchorElement>(
    `${NAV_LINK_SELECTOR}[data-nav-key]`
  );
  navLinks.forEach((link) => {
    const navKey = link.getAttribute("data-nav-key");
    link.classList.toggle("active", navKey === activePage);
  });

  const statusTree = document.querySelector<HTMLElement>(STATUS_TREE_SELECTOR);
  if (!statusTree) {
    return;
  }

  const isStatusPage = activePage.startsWith("status-");
  statusTree.classList.toggle("menu-open", isStatusPage);

  const toggleLink = statusTree.querySelector<HTMLAnchorElement>(":scope > .nav-link");
  if (toggleLink) {
    toggleLink.classList.toggle("active", isStatusPage);
    toggleLink.setAttribute("aria-expanded", String(isStatusPage));
  }
}

function applyFragmentMetadata(container: HTMLElement): void {
  const fragmentRoot = container.querySelector<HTMLElement>("[data-page-title]");
  if (!fragmentRoot) {
    return;
  }

  const newTitle = fragmentRoot.getAttribute("data-page-title");
  if (newTitle) {
    const pageHeading = document.getElementById(PAGE_TITLE_ID);
    if (pageHeading) {
      pageHeading.textContent = newTitle;
    }

    const docTitle = document.getElementById(DOCUMENT_TITLE_ID) as HTMLTitleElement | null;
    if (docTitle) {
      const suffix = docTitle.getAttribute("data-title-suffix") ?? "";
      docTitle.textContent = `${newTitle}${suffix}`;
    } else {
      document.title = newTitle;
    }
  }

  const activePage = fragmentRoot.getAttribute("data-active-page");
  if (activePage) {
    setActiveNav(activePage);
  }
}

/**
 * Fetch the requested page but only return the fragment used in the main content area.
 */
function loadPartialContent(url: string, pushHistory: boolean = true): void {
  const separator = url.includes("?") ? "&" : "?";
  const partialUrl = `${url}${separator}${PARTIAL_PARAM}`;

  fetch(partialUrl, { method: "GET", credentials: "same-origin" })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
      }
      return res.text();
    })
    .then((html) => {
      const root = document.getElementById(CONTENT_ROOT_ID);
      if (!root) {
        console.warn(`#${CONTENT_ROOT_ID} が見つかりません`);
        return;
      }

      root.innerHTML = html;
      applyFragmentMetadata(root);
      if (pushHistory) {
        window.history.pushState({}, "", url);
      }
    })
    .catch((err) => {
      console.error("partial load failed:", err);
    });
}

/**
 * Intercept navigation clicks on .js-nav elements and swap to partial rendering.
 */
function setupPartialNav(): void {
  document.addEventListener("click", (event: MouseEvent) => {
    const target = event.target as HTMLElement | null;
    if (!target) {
      return;
    }

    const link = target.closest<HTMLAnchorElement>(NAV_LINK_SELECTOR);
    if (!link) {
      return;
    }

    const href = link.getAttribute("href");
    if (!href || href.startsWith("#")) {
      return;
    }

    event.preventDefault();
    loadPartialContent(href, true);
  });

  window.addEventListener("popstate", () => {
    const url = `${window.location.pathname}${window.location.search}`;
    loadPartialContent(url, false);
  });
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", setupPartialNav);
} else {
  setupPartialNav();
}
