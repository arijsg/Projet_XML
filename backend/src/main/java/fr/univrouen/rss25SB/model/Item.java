package fr.univrouen.rss25SB.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.univrouen.rss25SB.adapters.OffsetDateTimeAdapter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String guid;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String title;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<Category> category = new ArrayList<>();

    @XmlElement(namespace = "http://univ.fr/rss25")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime published;

    @XmlElement(namespace = "http://univ.fr/rss25")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    private OffsetDateTime updated;

    @XmlTransient
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @XmlTransient
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "content_id")
    private Content content;

    @XmlTransient
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<PersonType> authorsAndContributors = new ArrayList<>();

    // === Constructeur sans argument requis par JPA et JAXB ===
    public Item() {
    }

    // === Constructeur utile pour tester ===
    public Item(String guid, String title) {
        this.guid = guid;
        this.title = title;
    }

    // === Getters / Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Category> getCategory() { return category; }
    public void setCategory(List<Category> category) { this.category = category; }

    public OffsetDateTime getPublished() { return published; }
    public void setPublished(OffsetDateTime published) { this.published = published; }

    public OffsetDateTime getUpdated() { return updated; }
    public void setUpdated(OffsetDateTime updated) { this.updated = updated; }

    public Image getImage() { return image; }
    public void setImage(Image image) { this.image = image; }

    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }

    public List<PersonType> getAuthorsAndContributors() { return authorsAndContributors; }
    public void setAuthorsAndContributors(List<PersonType> authorsAndContributors) {
        this.authorsAndContributors = authorsAndContributors;
    }
}
