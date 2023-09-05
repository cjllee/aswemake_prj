package market.shop.coupon;

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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}