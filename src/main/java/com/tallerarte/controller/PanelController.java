package com.tallerarte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelController {

    @GetMapping("/instructor/panel")
    public String instructor() {
        return "panel/instructor";
    }

    @GetMapping("/admin/panel")
    public String administrador() {
        return "panel/administrador";
    }
}