package market.shop.pricehistory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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

    public PriceHistory findByItemNameAndRegisteredTime(String itemName, LocalDateTime registeredTime) {
        String query = "SELECT p FROM PriceHistory p WHERE p.itemName = :itemName AND p.registeredTime = :registeredTime";
        try {
            return em.createQuery(query, PriceHistory.class)
                    .setParameter("itemName", itemName)
                    .setParameter("registeredTime", registeredTime)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoSuchElementException("No price history for the given item and time");
        }
    }





}