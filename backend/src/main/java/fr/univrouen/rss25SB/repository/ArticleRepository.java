package fr.univrouen.rss25SB.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.rss25SB.model.Item;

@Repository
public interface ArticleRepository extends JpaRepository<Item, Long> {

    boolean existsByTitleAndPublished(String title, OffsetDateTime published);

    // Si votre modèle utilise pubDate ailleurs, adaptez ici
}
