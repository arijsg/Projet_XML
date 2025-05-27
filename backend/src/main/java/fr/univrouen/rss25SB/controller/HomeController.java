package fr.univrouen.rss25SB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>Projet RSS25SB</h1>" +
               "<p>Version 1.0</p>" +
               "<p>Développé par Sghiri Arij et Oudjane Aldjia</p>" +
               "<img src='/images/logo.png' alt='Logo' style='width:200px; height:auto;'/>" ;
    }
}