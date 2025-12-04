package com.mingshiu.engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DisplayController {

    private String render(String pageTitle, String contentTemplate, String activePage, Model model) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("contentTemplate", contentTemplate);
        model.addAttribute("activePage", activePage);
        return "layout";
    }

    @GetMapping("/")
    public String news(Model model) {
        return render("最新情報", "contents/news", "news", model);
    }

    @GetMapping("/status/input")
    public String statusInput(Model model) {
        return render("ステータス入力", "contents/status-input", "status-input", model);
    }

    @GetMapping("/status/list")
    public String statusList(Model model) {
        return render("ステータス一覧", "contents/status-list", "status-list", model);
    }

    @GetMapping("/weapon")
    public String baseStatusWeapon(Model model) {
        return render("武器", "contents/weapon", "weapon", model);
    }

    @GetMapping("/echo")
    public String baseStatusEcho(Model model) {
        return render("音骸", "contents/echo", "echo", model);
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return render("設定", "contents/settings", "settings", model);
    }
}
