package com.mingshiu.engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DisplayController {

    private static final String VIEW_LAYOUT = "layout";

    private static final String CONTENT_DASHBOARD = "contents/dashboard";
    private static final String CONTENT_SETTINGS = "contents/settings";
    private static final String CONTENT_STATUS_BASE = "contents/status-base-input";
    private static final String CONTENT_STATUS_WEAPON = "contents/status-weapon-input";
    private static final String CONTENT_STATUS_BONUS = "contents/status-bonus-input";
    private static final String CONTENT_STATUS_ECHO = "contents/status-echo-input";

    private static final String ACTIVE_DASHBOARD = "dashboard";
    private static final String ACTIVE_SETTINGS = "settings";
    private static final String ACTIVE_STATUS_BASE = "status-base-input";
    private static final String ACTIVE_STATUS_WEAPON = "status-weapon-input";
    private static final String ACTIVE_STATUS_BONUS = "status-bonus-input";
    private static final String ACTIVE_STATUS_ECHO = "status-echo-input";

    private static final String TITLE_DASHBOARD = "ダッシュボード";
    private static final String TITLE_SETTINGS = "設定";
    private static final String TITLE_STATUS_BASE = "基礎ステータス入力画面";
    private static final String TITLE_STATUS_WEAPON = "武器ステータス入力画面";
    private static final String TITLE_STATUS_BONUS = "ステータスボーナス入力画面";
    private static final String TITLE_STATUS_ECHO = "音骸ステータス入力画面";

    /**
     * Populates the common layout attributes before rendering the shared template.
     *
     * @param pageTitle       heading displayed in the page chrome
     * @param contentTemplate Thymeleaf template fragment containing the page body
     * @param activePage      sidebar key used for highlighting the current menu
     * @param model           current request model
     * @return the base layout view name
     */
    private String render(String pageTitle, String contentTemplate, String activePage, Model model) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("contentTemplate", contentTemplate);
        model.addAttribute("activePage", activePage);
        return VIEW_LAYOUT;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        return render(TITLE_DASHBOARD, CONTENT_DASHBOARD, ACTIVE_DASHBOARD, model);
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return render(TITLE_SETTINGS, CONTENT_SETTINGS, ACTIVE_SETTINGS, model);
    }

    @GetMapping("/status/base/input")
    public String baseStatusInput(Model model) {
        return render(TITLE_STATUS_BASE, CONTENT_STATUS_BASE, ACTIVE_STATUS_BASE, model);
    }

    @GetMapping("/status/weapon/input")
    public String baseStatusWeapon(Model model) {
        return render(TITLE_STATUS_WEAPON, CONTENT_STATUS_WEAPON, ACTIVE_STATUS_WEAPON, model);
    }

    @GetMapping("/status/bonus/input")
    public String baseStatusBonus(Model model) {
        return render(TITLE_STATUS_BONUS, CONTENT_STATUS_BONUS, ACTIVE_STATUS_BONUS, model);
    }

    @GetMapping("/status/echo/input")
    public String baseStatusEcho(Model model) {
        return render(TITLE_STATUS_ECHO, CONTENT_STATUS_ECHO, ACTIVE_STATUS_ECHO, model);
    }
}
