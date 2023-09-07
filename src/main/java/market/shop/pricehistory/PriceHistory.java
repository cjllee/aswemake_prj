package market.shop.pricehistory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PriceHistory {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime changedAt;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public LocalDateTime getChangedAt() {return changedAt;}

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }



}
