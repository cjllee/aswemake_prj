package market.shop.coupon;

import jakarta.persistence.criteria.Order;
import market.shop.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Coupon {

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;


    private int discountAmount;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}