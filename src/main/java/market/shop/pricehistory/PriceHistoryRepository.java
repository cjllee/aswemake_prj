package market.shop.pricehistory;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceHistoryRepository {

    private final EntityManager em;

    public PriceHistoryRepository(EntityManager em) {
        this.em = em;
    }

    public void save(PriceHistory priceHistory) {
        em.persist(priceHistory);
    }

    public PriceHistory findOne(Long id) {
        return em.find(PriceHistory.class, id);
    }
    public List<PriceHistory> findByChangedAt(LocalDateTime start, LocalDateTime end) {
        return em.createQuery("SELECT p FROM PriceHistory p WHERE p.changedAt >= :start AND p.changedAt < :end", PriceHistory.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }


}