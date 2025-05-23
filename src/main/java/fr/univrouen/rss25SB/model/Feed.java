package fr.univrouen.rss25SB.model;

import java.time.OffsetDateTime;
import java.util.List;

import fr.univrouen.rss25SB.adapters.OffsetDateTimeAdapter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "feed")
@XmlRootElement(name = "feed", namespace = "http://univ.fr/rss25")
@XmlAccessorType(XmlAccessType.FIELD)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String title;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime pubDate;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String copyright;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "feed_id")
    @XmlElement(name = "link", namespace = "http://univ.fr/rss25")
    private List<Link> link;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "feed_id")
    @XmlElement(name = "item", namespace = "http://univ.fr/rss25")
    private List<Item> item;

    @XmlAttribute(name = "lang", required = true)
    private String lang;

    @XmlAttribute(name = "version", required = true)
    private String version = "25";

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public OffsetDateTime getPubDate() { return pubDate; }
    public void setPubDate(OffsetDateTime pubDate) { this.pubDate = pubDate; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }

    public List<Link> getLink() { return link; }
    public void setLink(List<Link> link) { this.link = link; }

    public List<Item> getItem() { return item; }
    public void setItem(List<Item> item) { this.item = item; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}
