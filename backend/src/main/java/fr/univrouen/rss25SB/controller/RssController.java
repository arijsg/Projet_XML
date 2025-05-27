package fr.univrouen.rss25SB.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.rss25SB.model.Feed;
import fr.univrouen.rss25SB.model.Item;
import fr.univrouen.rss25SB.repository.ArticleRepository;
import fr.univrouen.rss25SB.utils.ValidationUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

@RestController
@RequestMapping("/rss25SB")
public class RssController {

    @Autowired
    private ArticleRepository articleRepository;

    // --- I.3.1 Liste synthétique XML ---
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> listArticlesSummaryXml() {
        try {
            List<Item> items = articleRepository.findAll();

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<resume xmlns=\"http://univ.fr/rss25\">\n");

            for (Item item : items) {
                sb.append("  <item>\n");
                sb.append("    <id>").append(item.getId()).append("</id>\n");

                // ✅ Inclure <title> si disponible
                if (item.getTitle() != null) {
                    sb.append("    <title>").append(item.getTitle()).append("</title>\n");
                }

                // ✅ Inclure <published> ou <updated>
                if (item.getPublished() != null) {
                    sb.append("    <published>").append(item.getPublished()).append("</published>\n");
                } else if (item.getUpdated() != null) {
                    sb.append("    <updated>").append(item.getUpdated()).append("</updated>\n");
                }

                // ✅ Inclure les catégories (sous forme <category term="..."/>)
                if (item.getCategory() != null) {
                    for (var cat : item.getCategory()) {
                        sb.append("    <category term=\"").append(cat.getTerm()).append("\" />\n");
                    }
                }

                // ✅ Toujours inclure <guid>
                sb.append("    <guid>").append(item.getGuid()).append("</guid>\n");
                sb.append("  </item>\n");
            }

            sb.append("</resume>");
            return ResponseEntity.ok(sb.toString());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("<response><status>ERROR</status></response>");
        }
    }



    // --- I.3.2 Liste synthétique HTML ---
   @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> listArticlesSummaryHtml() {
        try {
            List<Item> items = articleRepository.findAll();

            StringBuilder xmlContent = new StringBuilder();
            xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlContent.append("<resume xmlns=\"http://univ.fr/rss25\">\n");

            for (Item item : items) {
                xmlContent.append("  <item>\n");
                xmlContent.append("    <id>").append(item.getId()).append("</id>\n");

                // Ajouter <title>
                if (item.getTitle() != null) {
                    xmlContent.append("    <title>").append(item.getTitle()).append("</title>\n");
                }

                // Ajouter <published> ou <updated>
                if (item.getPublished() != null) {
                    xmlContent.append("    <published>").append(item.getPublished()).append("</published>\n");
                } else if (item.getUpdated() != null) {
                    xmlContent.append("    <updated>").append(item.getUpdated()).append("</updated>\n");
                }

                // Ajouter <category>
                if (item.getCategory() != null) {
                    for (var cat : item.getCategory()) {
                        xmlContent.append("    <category term=\"").append(cat.getTerm()).append("\" />\n");
                    }
                }

                // Ajouter <guid>
                xmlContent.append("    <guid>").append(item.getGuid()).append("</guid>\n");
                xmlContent.append("  </item>\n");
            }

            xmlContent.append("</resume>");

            // Appliquer XSLT
            Source xsltSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("rss25.tp4.xslt"));
            if (xsltSource == null) {
                return ResponseEntity.internalServerError()
                        .body("<html><body><h1>Erreur</h1><p>Feuille XSLT introuvable</p></body></html>");
            }

            Source xmlSource = new StreamSource(new StringReader(xmlContent.toString()));
            StringWriter htmlWriter = new StringWriter();

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSource);
            transformer.transform(xmlSource, new StreamResult(htmlWriter));

            return ResponseEntity.ok(htmlWriter.toString());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("<html><body><h1>Erreur</h1><p>Erreur de transformation XSLT</p></body></html>");
        }
    }



    // --- I.4.1 Détail article XML ---
    @GetMapping(value = "/resume/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getArticleDetailXml(@PathVariable Long id) {
        try {
            Optional<Item> itemOpt = articleRepository.findById(id);
            if (itemOpt.isEmpty()) {
                String error = "<response><id>" + id + "</id><status>ERROR</status></response>";
                return ResponseEntity.status(404).body(error);
            }

            Item item = itemOpt.get();

            // Création d'un Feed temporaire avec un seul Item pour respecter XSD
            Feed feed = new Feed();
            feed.setItem(List.of(item));
            // Remplir aussi d'autres champs minimaux (ex : title, pubDate) si nécessaire
            feed.setTitle("Détail Article");
            feed.setLang("fr");
            feed.setVersion("25");

            JAXBContext jaxbContext = JAXBContext.newInstance(Feed.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            marshaller.marshal(feed, sw);

            return ResponseEntity.ok(sw.toString());
        } catch (Exception e) {
            String error = "<response><id>" + id + "</id><status>ERROR</status></response>";
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // --- I.4.2 Détail article HTML ---
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
public ResponseEntity<String> getArticleDetailHtml(@PathVariable Long id) {
    try {
        // Récupérer l'article par son ID
        Optional<Item> itemOpt = articleRepository.findById(id);
        if (itemOpt.isEmpty()) {
            String error = "<html><body><h1>Erreur</h1><p>status : ERROR</p><p>id : " + id + "</p></body></html>";
            return ResponseEntity.status(404).body(error);
        }

        Item item = itemOpt.get();

        // Créer un objet Feed temporaire avec un seul article
        Feed feed = new Feed();
        feed.setItem(List.of(item));
        feed.setTitle("Détail de l'article");
        feed.setLang("fr");
        feed.setVersion("25");

        // Convertir l'objet Feed en XML
        JAXBContext jaxbContext = JAXBContext.newInstance(Feed.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter xmlWriter = new StringWriter();
        marshaller.marshal(feed, xmlWriter);

        // Charger le fichier XSLT
        Source xsltSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("rss25.tp4.xslt"));
        if (xsltSource == null) {
            return ResponseEntity.internalServerError()
                    .body("<html><body><h1>Erreur</h1><p>Feuille XSLT introuvable</p></body></html>");
        }

        Source xmlSource = new StreamSource(new StringReader(xmlWriter.toString()));
        StringWriter htmlWriter = new StringWriter();

        // Appliquer le XSLT sur le XML pour générer le HTML
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);
        transformer.transform(xmlSource, new StreamResult(htmlWriter));

        // Retourner le HTML généré
        return ResponseEntity.ok(htmlWriter.toString());
    } catch (Exception e) {
        String error = "<html><body><h1>Erreur</h1><p>status : ERROR</p><p>id : " + id + "</p></body></html>";
        return ResponseEntity.internalServerError().body(error);
    }
}



    // --- I.5 Ajout article POST ---
  @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> insertArticle(@RequestBody String xmlContent) {
        try {
            // Charger le XSD depuis le classpath
            Source xsdSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("rss25SB.xsd"));

            // Convertir la chaîne XML en Source
            Source xmlSource = new StreamSource(new StringReader(xmlContent));

            // === 1. VALIDER LE FLUX XML ===
            if (!ValidationUtils.validateXML(xmlSource, xsdSource)) {
                return ResponseEntity.badRequest().body(
                    "<response><status>ERROR</status><description>Validation XSD échouée</description></response>"
                );
            }

            // === 2. UNMARSHAL XML en objet Java ===
            JAXBContext jaxbContext = JAXBContext.newInstance(Feed.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Feed feed = (Feed) unmarshaller.unmarshal(new StringReader(xmlContent));

            // === 3. VÉRIFIER QUE DES ARTICLES SONT PRÉSENTS ===
            List<Item> items = feed.getItem();
            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    "<response><status>ERROR</status><description>Flux vide, aucun article trouvé</description></response>"
                );
            }

            // === 4. VÉRIFIER LES DOUBLONS ===
            for (Item item : items) {
                if (articleRepository.existsByTitleAndPublished(item.getTitle(), item.getPublished())) {
                    return ResponseEntity.status(409).body(
                        "<response><status>ERROR</status><description>Article déjà présent : " + item.getTitle() + "</description></response>"
                    );
                }
            }

            // === 5. SAUVEGARDER LES ARTICLES ===
            for (Item item : items) {
                articleRepository.save(item);
            }

            Long lastId = items.get(items.size() - 1).getId();
            return ResponseEntity.ok(
                "<response><status>INSERTED</status><id>" + lastId + "</id></response>"
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "<response><status>ERROR</status><description>Erreur inconnue : " + e.getMessage() + "</description></response>"
            );
        }
    }



    // --- I.5.1 Suppression article DELETE ---
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            if (!articleRepository.existsById(id)) {
                return ResponseEntity.status(404).body("<response><status>ERROR</status><description>Article non trouvé</description></response>");
            }

            articleRepository.deleteById(id);

            // TODO: Supprimer un flux vide si nécessaire (hors périmètre immédiat)

            return ResponseEntity.ok("<response><status>DELETED</status><id>" + id + "</id></response>");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("<response><status>ERROR</status></response>");
        }
    }


    @GetMapping("/search")
    public ResponseEntity<String> searchArticles(@RequestParam(required = false) String date,
                                                @RequestParam(required = false) String category) {
        try {
            List<Item> items = articleRepository.findAll();

            // Filtrer par date
            if (date != null) {
                OffsetDateTime searchDate = OffsetDateTime.parse(date);  // Validation de la date (format ISO)
                items = items.stream()
                        .filter(item -> item.getPublished().isAfter(searchDate) || item.getPublished().isEqual(searchDate))
                        .collect(Collectors.toList());
            }

            // Filtrer par catégorie
            if (category != null) {
                items = items.stream()
                        .filter(item -> item.getCategory().stream()
                                .anyMatch(cat -> cat.getTerm().equals(category)))
                        .collect(Collectors.toList());
            }

            // Si aucun résultat
            if (items.isEmpty()) {
                return ResponseEntity.ok("<response><status>NONE</status></response>");
            }

            // Construire le flux XML
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<resume xmlns=\"http://univ.fr/rss25\">\n");

            for (Item item : items) {
                sb.append("  <item>\n");
                sb.append("    <id>").append(item.getId()).append("</id>\n");
                sb.append("    <date>").append(item.getPublished().toString()).append("</date>\n");
                sb.append("    <guid>").append(item.getGuid()).append("</guid>\n");
                sb.append("  </item>\n");
            }

            sb.append("</resume>");

            return ResponseEntity.ok(sb.toString());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("<response><status>ERROR</status><description>Erreur de traitement : " + e.getMessage() + "</description></response>");
        }
    }

}
