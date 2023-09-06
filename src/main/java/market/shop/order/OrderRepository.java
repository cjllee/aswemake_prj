package market.shop.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;


import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    private final EntityManager em;

    public OrderRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Order order) {
        em.persist(order);
    }

    public market.shop.order.Order findOne(Long id) {
        return em.find(market.shop.order.Order.class, id);
    }


}

