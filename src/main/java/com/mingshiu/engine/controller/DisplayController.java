package com.mingshiu.engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DisplayController {

    @GetMapping("/")
    public String dashboard() {
        return "contents/dashboard";
    }

    @GetMapping("/reports")
    public String reports() {
        return "contents/reports";
    }

    @GetMapping("/mail/list")
    public String mailList() {
        return "contents/mail-list";
    }

    @GetMapping("/mail/send")
    public String mailSend() {
        return "contents/mail-send";
    }

    @GetMapping("/profile")
    public String profile() {
        return "contents/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "contents/settings";
    }
}
