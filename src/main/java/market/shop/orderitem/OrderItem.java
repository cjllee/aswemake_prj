package market.shop.orderitem;

import market.shop.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.order.Order;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private int TotalPrice;

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count); // 여기에 추가
        orderItem.setTotalPrice(orderPrice * count); // 여기에 추가

        return orderItem;
    }

    public int getTotalPrice(){
        return getOrderPrice() *getCount();
    }
}
