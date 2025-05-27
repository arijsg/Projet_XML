package fr.univrouen.rss25SB.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "Person_type")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String name;

    @XmlElement(namespace = "http://univ.fr/rss25")
    private String email;

    @XmlElement(namespace = "http://univ.fr/rss25")
    private String uri;

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
}
