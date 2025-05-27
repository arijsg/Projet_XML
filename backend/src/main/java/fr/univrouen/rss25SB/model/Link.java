package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@Table(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlAttribute(name = "rel", required = true)
    private String rel;

    @XmlAttribute(name = "type", required = true)
    private String type;

    @XmlAttribute(name = "href", required = true)
    private String href;

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRel() { return rel; }
    public void setRel(String rel) { this.rel = rel; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }
}
