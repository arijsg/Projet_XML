package fr.univrouen.rss25SB.model;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import fr.univrouen.rss25SB.generated.AuthorType;
import fr.univrouen.rss25SB.generated.CategoryType;
import fr.univrouen.rss25SB.generated.ContentType;
import fr.univrouen.rss25SB.generated.ImageType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemType", propOrder = {
    "guid",
    "title",
    "category",
    "published",
    "updated",
    "image",
    "content",
    "author"
})
public class ItemType {

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected String guid;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected String title;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected List<CategoryType> category;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected XMLGregorianCalendar published;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected XMLGregorianCalendar updated;

    @XmlElement(namespace = "http://univ.fr/rss25")
    protected ImageType image;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected ContentType content;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected List<AuthorType> author;

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<CategoryType> getCategory() { return category; }
    public void setCategory(List<CategoryType> category) { this.category = category; }

    public XMLGregorianCalendar getPublished() { return published; }
    public void setPublished(XMLGregorianCalendar published) { this.published = published; }

    public XMLGregorianCalendar getUpdated() { return updated; }
    public void setUpdated(XMLGregorianCalendar updated) { this.updated = updated; }

    public ImageType getImage() { return image; }
    public void setImage(ImageType image) { this.image = image; }

    public ContentType getContent() { return content; }
    public void setContent(ContentType content) { this.content = content; }

    public List<AuthorType> getAuthor() { return author; }
    public void setAuthor(List<AuthorType> author) { this.author = author; }
}
