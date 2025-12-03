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
    public String dashboard(Model model) {
        return render("ダッシュボード", "contents/dashboard", "dashboard", model);
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return render("設定", "contents/settings", "settings", model);
    }

    @GetMapping("/status/base/input")
    public String baseStatusInput(Model model) {
        return render("基礎ステータス入力画面", "contents/status-base-input", "status-base-input", model);
    }

    @GetMapping("/status/weapon/input")
    public String baseStatusWeapon(Model model) {
        return render("武器ステータス入力画面", "contents/status-weapon-input", "status-weapon-input", model);
    }

    @GetMapping("/status/bonus/input")
    public String baseStatusBonus(Model model) {
        return render("ステータスボーナス入力画面", "contents/status-bonus-input", "status-bonus-input", model);
    }

    @GetMapping("/status/echo/input")
    public String baseStatusEcho(Model model) {
        return render("音骸ステータス入力画面", "contents/status-echo-input", "status-echo-input", model);
    }

}
