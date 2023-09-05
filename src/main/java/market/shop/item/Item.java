package market.shop.item;

import market.shop.coupon.Coupon;
import market.shop.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;


    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Coupon coupon;




}
