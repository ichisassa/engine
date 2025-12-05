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

    @GetMapping("/resonators/input")
    public String resonatorsInput(@RequestParam(required = false) String partial, Model model) {
        return render("共鳴者入力", "contents/resonators-input", "resonators-input", partial, model);
    }

    @GetMapping("/resonators/list")
    public String resonatorsList(@RequestParam(required = false) String partial, Model model) {
        return render("共鳴者一覧", "contents/resonators-list", "resonators-list", partial, model);
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
