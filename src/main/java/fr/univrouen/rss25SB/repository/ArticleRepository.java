package fr.univrouen.rss25SB.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import fr.univrouen.rss25SB.model.ItemType;

@Repository
public class ArticleRepository {
    private final Map<Integer, ItemType> articles = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public synchronized int addArticle(ItemType article) {
        for (ItemType a : articles.values()) {
            if (a.getTitle().equals(article.getTitle()) &&
                a.getPublished().equals(article.getPublished())) {
                return -1;
            }
        }
        int id = idCounter.getAndIncrement();
        articles.put(id, article);
        return id;
    }

    public List<ItemType> getAll() {
        return new ArrayList<>(articles.values());
    }

    public ItemType getById(int id) {
    return articles.get(id);
}

}
