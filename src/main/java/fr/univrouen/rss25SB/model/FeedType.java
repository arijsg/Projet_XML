package fr.univrouen.rss25SB.model;

import java.util.ArrayList;
import java.util.List;

import fr.univrouen.rss25SB.generated.LinkType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "feed", namespace = "http://univ.fr/rss25")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeedType", propOrder = {
    "title",
    "pubDate",
    "copyright",
    "link",
    "item"
})
public class FeedType {

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected String title;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected String pubDate;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected String copyright;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected List<LinkType> link;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    protected List<ItemType> item;

    @XmlAttribute(name = "lang")
    protected String lang;

    @XmlAttribute(name = "version")
    protected String version;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPubDate() { return pubDate; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }

    public List<LinkType> getLink() { return link; }
    public void setLink(List<LinkType> link) { this.link = link; }

    public List<ItemType> getItem() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return item;
    }

    public void setItem(List<ItemType> item) { this.item = item; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getVersion() { return version == null ? "25" : version; }
    public void setVersion(String version) { this.version = version; }
}
