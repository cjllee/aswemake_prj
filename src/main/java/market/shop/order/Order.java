package market.shop.order;

import jakarta.validation.constraints.NotNull;
import market.shop.coupon.Coupon;
import market.shop.delivery.Delivery;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.item.Item;
import market.shop.member.Member;
import market.shop.orderitem.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Coupon> coupons = new ArrayList<>();

    private Long count;

    private Long itemId;

    private int totalPrice;

    private int deliveryFee;
    private int discount;


    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    //==생성 메서드==//
    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }
    //==조회 로직==//
    /** 전체 주문 가격 조회 */
    public double getTotalPrice() {
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice + delivery.getPrice();
    }


    public void applyDeliveryFee(int fee) {
        this.deliveryFee = fee;
        this.totalPrice += fee;
    }

    public void applyDiscount(int discountAmount) {
        if (this.totalPrice < discountAmount)
            throw new IllegalArgumentException("Discount cannot be larger than total price");

        this.discount = discountAmount;
        this.totalPrice -= discountAmount;
    }


}
