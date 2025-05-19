package fr.univrouen.rss25SB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelpController {

    @GetMapping("/help")
    @ResponseBody
    public String help() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Aide - Projet RSS25SB</title></head>" +
               "<body>" +
               "<h1>Aide - Service REST RSS25SB</h1>" +
               "<h2>Liste des opérations :</h2>" +
               "<ul>" +
               "<li><strong>/</strong> (GET) - Affiche la page d'accueil (HTML)</li>" +
               "<li><strong>/help</strong> (GET) - Affiche cette page d'aide (HTML)</li>" +
               "<li><strong>/rss25SB/resume/xml</strong> (GET) - Liste synthétique des articles (XML)</li>" +
               "<li><strong>/rss25SB/resume/html</strong> (GET) - Liste synthétique des articles (HTML)</li>" +
               "<li><strong>/rss25SB/resume/xml/{id}</strong> (GET) - Détail d'un article (XML)</li>" +
               "<li><strong>/rss25SB/html/{id}</strong> (GET) - Détail d'un article (HTML)</li>" +
               "<li><strong>/rss25SB/insert</strong> (POST) - Ajoute un article (XML en entrée, XML en sortie)</li>" +
               "<li><strong>/rss25SB/delete/{id}</strong> (DELETE) - Supprime un article (XML en sortie)</li>" +
               "</ul>" +
               "</body>" +
               "</html>";
    }
}