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

    @GetMapping("/settings")
    public String settings(@RequestParam(required = false) String partial, Model model) {
        return render("設定", "contents/settings", "settings", partial, model);
    }

    @GetMapping("/resonators/base/input")
    public String resonatorsBaseInput(@RequestParam(required = false) String partial, Model model) {
        return render("基本情報入力", "contents/resonators-base-input", "resonators-base-input", partial, model);
    }

    @GetMapping("/resonators/base/list")
    public String resonatorsBaseList(@RequestParam(required = false) String partial, Model model) {
        return render("基本情報一覧", "contents/resonators-base-list", "resonators-base-list", partial, model);
    }

    @GetMapping("/resonators/status/input")
    public String resonatorsStatusInput(@RequestParam(required = false) String partial, Model model) {
        return render("ステータス入力", "contents/resonators-status-input", "resonators-status-input", partial, model);
    }

    @GetMapping("/resonators/status/list")
    public String resonatorsStatusList(@RequestParam(required = false) String partial, Model model) {
        return render("ステータス一覧", "contents/resonators-status-list", "resonators-status-list", partial, model);
    }

    @GetMapping("/weapon/input")
    public String weaponInput(@RequestParam(required = false) String partial, Model model) {
        return render("武器入力", "contents/weapon-input", "weapon-input", partial, model);
    }

    @GetMapping("/weapon/list")
    public String weaponList(@RequestParam(required = false) String partial, Model model) {
        return render("武器一覧", "contents/weapon-list", "weapon-list", partial, model);
    }

    @GetMapping("/echo/input")
    public String echoInput(@RequestParam(required = false) String partial, Model model) {
        return render("音骸入力", "contents/echo-input", "echo-input", partial, model);
    }

    @GetMapping("/echo/list")
    public String echoList(@RequestParam(required = false) String partial, Model model) {
        return render("音骸一覧", "contents/echo-list", "echo-list", partial, model);
    }
}
