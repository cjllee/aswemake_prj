package market.shop.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePriceRequest {
    private String itemName;
    private int price;
    private Long memberId;
}