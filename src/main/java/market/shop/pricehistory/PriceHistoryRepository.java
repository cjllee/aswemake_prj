package market.shop.pricehistory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import market.shop.item.Item;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class PriceHistoryRepository {

    private final EntityManager em;

    public PriceHistoryRepository(EntityManager em) {
        this.em = em;
    }

    public void save(PriceHistory priceHistory) {
        em.persist(priceHistory);
    }

    public PriceHistory findByItemAndChangedAt(Item item, LocalDateTime changedAt) {
        try {
            return em.createQuery("SELECT p FROM PriceHistory p WHERE p.item = :item AND p.changedAt = :changedAt",
                            PriceHistory.class)
                    .setParameter("item", item)
                    .setParameter("changedAt", changedAt)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}