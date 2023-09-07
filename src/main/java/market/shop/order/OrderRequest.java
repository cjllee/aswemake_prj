package market.shop.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private Long memberId;
    private Long itemId;
    private int count;
    private String coupons;
}