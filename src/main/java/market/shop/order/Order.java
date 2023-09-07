package market.shop.order;


import market.shop.coupon.Coupon;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import market.shop.coupon.DiscountType;
import market.shop.item.Item;
import market.shop.member.Member;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    private int count;


    private int totalPrice;


    private static final int DELIVERY_FEE= 3000;

    @Enumerated(EnumType.STRING)
    private DiscountType couponType;

    public void setMember(Member member) {
        this.member=member;
        member.getOrders().add(this);
    }

    public void setItem(Item item){
        this.item=item;
    }

    public void setCount(int count){
        this.count=count;
        calculateTotalPrice();
    }


    public void calculateTotalPrice() {
        if(item != null && count > 0){
            totalPrice=(item.getPrice()*count)+DELIVERY_FEE;
        }
    }
}