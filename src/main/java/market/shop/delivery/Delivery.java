package market.shop.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.order.Order;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery" , fetch = FetchType.LAZY)
    private Order order;

    private int price;

}
