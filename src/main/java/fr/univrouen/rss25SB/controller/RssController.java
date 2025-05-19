package fr.univrouen.rss25SB.controller;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.rss25SB.model.FeedType;
import fr.univrouen.rss25SB.model.ItemType;
import fr.univrouen.rss25SB.repository.ArticleRepository;
import fr.univrouen.rss25SB.utils.ValidationUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

@RestController
@RequestMapping("/rss25SB")
public class RssController {

    @Autowired
    private ArticleRepository repository;

    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public String insertArticle(@RequestBody String xmlContent) {
        try {
            // 1. Validation XML via XSD
            StreamSource xmlSource = new StreamSource(new StringReader(xmlContent));
            InputStream xsdStream = getClass().getClassLoader().getResourceAsStream("rss25SB.xsd");

            if (xsdStream == null) {
                return "<response><status>ERROR</status><description>Le fichier XSD rss25SB.xsd est introuvable</description></response>";
            }

            StreamSource xsdSource = new StreamSource(xsdStream);
            boolean valid = ValidationUtils.validateXML(xmlSource, xsdSource);

            if (!valid) {
                return "<response><status>ERROR</status><description>XML invalide (non conforme XSD)</description></response>";
            }

            // 2. Unmarshalling JAXB → FeedType
            JAXBContext context = JAXBContext.newInstance(FeedType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FeedType feed = (FeedType) unmarshaller.unmarshal(new StringReader(xmlContent));

            List<ItemType> items = feed.getItem();
            int lastId = -1;

            for (ItemType item : items) {
                int id = repository.addArticle(item);
                if (id == -1) {
                    return "<response><status>ERROR</status><description>Doublon détecté</description></response>";
                }
                lastId = id;
            }

            return "<response><status>INSERTED</status><id>" + lastId + "</id></response>";

        } catch (Exception e) {
            e.printStackTrace();
            return "<response><status>ERROR</status><description>" + e.getMessage() + "</description></response>";
        }
    }

    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String getAllArticlesXML() {
        List<ItemType> articles = repository.getAll();

        StringBuilder xml = new StringBuilder();
        xml.append("<resume xmlns=\"http://univ.fr/rss25\">\n");

        for (ItemType item : articles) {
            xml.append("  <item>\n");
            xml.append("    <guid>").append(item.getGuid()).append("</guid>\n");
            xml.append("    <title>").append(item.getTitle()).append("</title>\n");
            xml.append("    <published>").append(item.getPublished()).append("</published>\n");
            xml.append("  </item>\n");
        }

        xml.append("</resume>");
        return xml.toString();
    }

    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getAllArticlesHTML() {
        try {
            // 1. Récupérer tous les articles
            List<ItemType> articles = repository.getAll();

            // 2. Créer un objet FeedType
            FeedType feed = new FeedType();
            feed.getItem().addAll(articles);


            // 3. Marshalling en XML
            JAXBContext context = JAXBContext.newInstance(FeedType.class);
            java.io.StringWriter xmlWriter = new java.io.StringWriter();
            jakarta.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(feed, xmlWriter);

            // 4. Charger la XSLT
            InputStream xsltStream = getClass().getClassLoader().getResourceAsStream("rss25.tp4.xslt");
            if (xsltStream == null) {
                return "<html><body><h1>Erreur</h1><p>Feuille XSLT introuvable</p></body></html>";
            }

            // 5. Appliquer la transformation
            javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new StringReader(xmlWriter.toString()));
            javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
            java.io.StringWriter htmlWriter = new java.io.StringWriter();

            javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);
            transformer.transform(xmlSource, new javax.xml.transform.stream.StreamResult(htmlWriter));

            return htmlWriter.toString();

        } catch (Exception e) {
            return "<html><body><h1>Erreur</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }

    @GetMapping(value = "/resume/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public String getArticleByIdAsXML(@PathVariable int id) {
        ItemType item = repository.getById(id);

        if (item == null) {
            return "<response><status>ERROR</status><id>" + id + "</id></response>";
        }

        try {
            // Créer un flux avec un seul article
            FeedType feed = new FeedType();
            feed.getItem().add(item);

            JAXBContext context = JAXBContext.newInstance(FeedType.class);
            java.io.StringWriter xmlWriter = new java.io.StringWriter();
            jakarta.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(feed, xmlWriter);

            return xmlWriter.toString();

        } catch (Exception e) {
            return "<response><status>ERROR</status><id>" + id + "</id></response>";
        }
    }



    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getArticleByIdAsHTML(@PathVariable int id) {
        try {
            // Vérifier si l'article existe
            ItemType item = repository.getById(id);
            if (item == null) {
                return "<html><body><h1>Erreur</h1><p>status : ERROR</p><p>id : " + id + "</p></body></html>";
            }

            // Construire un flux RSS avec un seul item
            FeedType feed = new FeedType();
            feed.getItem().add(item);

            JAXBContext context = JAXBContext.newInstance(FeedType.class);
            java.io.StringWriter xmlWriter = new java.io.StringWriter();
            jakarta.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(feed, xmlWriter);

            InputStream xsltStream = getClass().getClassLoader().getResourceAsStream("rss25.tp4.xslt");
            if (xsltStream == null) {
                return "<html><body><h1>Erreur</h1><p>Feuille XSLT introuvable</p></body></html>";
            }

            javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(new StringReader(xmlWriter.toString()));
            javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
            java.io.StringWriter htmlWriter = new java.io.StringWriter();

            javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = factory.newTransformer(xsltSource);

            transformer.transform(xmlSource, new javax.xml.transform.stream.StreamResult(htmlWriter));
            return htmlWriter.toString();

        } catch (Exception e) {
            return "<html><body><h1>Erreur</h1><p>status : ERROR</p><p>id : " + id + "</p></body></html>";
        }
    }


}
