package market.shop.pricehistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.item.Item;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PriceHistory {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime changeTime; // 가격 변경 시간

    private int price; // 변경된 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


}
