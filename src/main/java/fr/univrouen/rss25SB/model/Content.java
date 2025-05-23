package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

@Entity
@Table(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlValue
    private String value;

    @XmlAttribute(name = "type", required = true)
    private String type;

    @XmlAttribute(name = "src")
    private String src;

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSrc() { return src; }
    public void setSrc(String src) { this.src = src; }
}
