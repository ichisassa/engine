package com.mingshiu.engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DisplayController {

    private String render(String pageTitle, String contentTemplate, String activePage, String partial, Model model) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("activePage", activePage);
        model.addAttribute("contentFragment", contentTemplate);
        if ("content".equals(partial)) {
            return "fragments/empty";
        }
        return "layout";
    }

    @GetMapping("/")
    public String news(@RequestParam(required = false) String partial, Model model) {
        return render("最新情報", "contents/news", "news", partial, model);
    }

    @GetMapping("/status/input")
    public String statusInput(@RequestParam(required = false) String partial, Model model) {
        return render("ステータス入力", "contents/status-input", "status-input", partial, model);
    }

    @GetMapping("/status/list")
    public String statusList(@RequestParam(required = false) String partial, Model model) {
        return render("ステータス一覧", "contents/status-list", "status-list", partial, model);
    }

    @GetMapping("/weapon")
    public String baseStatusWeapon(@RequestParam(required = false) String partial, Model model) {
        return render("武器", "contents/weapon", "weapon", partial, model);
    }

    @GetMapping("/echo")
    public String baseStatusEcho(@RequestParam(required = false) String partial, Model model) {
        return render("音骸", "contents/echo", "echo", partial, model);
    }

    @GetMapping("/settings")
    public String settings(@RequestParam(required = false) String partial, Model model) {
        return render("設定", "contents/settings", "settings", partial, model);
    }
}
