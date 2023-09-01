package item;

import coupon.Coupon;
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

    private LocalDateTime orderDate;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private Coupon coupon;
}
