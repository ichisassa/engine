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

    @GetMapping("/reports")
    public String reports(Model model) {
        return render("レポート", "contents/reports", "reports", model);
    }

    @GetMapping("/mail/list")
    public String mailList(Model model) {
        return render("メール一覧", "contents/mail-list", "mail-list", model);
    }

    @GetMapping("/mail/send")
    public String mailSend(Model model) {
        return render("メール送信", "contents/mail-send", "mail-send", model);
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return render("プロファイル", "contents/profile", "profile", model);
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        return render("設定", "contents/settings", "settings", model);
    }

    @GetMapping("/base-status")
    public String showBaseStatus(Model model) {
        return render("基礎ステータス", "contents/base-status", "base-status", model);
    }
}
