package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@Table(name = "image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlAttribute(name = "type", required = true)
    private String type;

    @XmlAttribute(name = "href", required = true)
    private String href;

    @XmlAttribute(name = "alt", required = true)
    private String alt;

    @XmlAttribute(name = "length")
    private Integer length;

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public String getAlt() { return alt; }
    public void setAlt(String alt) { this.alt = alt; }

    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
}
