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

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Coupon> coupons = new ArrayList<>();

    // Item과의 연관 관계 설정 - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 주문 아이템

    // 주문 수량은 int로 변경합니다.
    private int count;

    // 총 가격은 계산을 통해 얻습니다.
    private int totalPrice;

    // 배송비는 상수로 설정합니다.
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

    // 총 가격 계산 메서드 추가
    public void calculateTotalPrice() {
        if(item != null && count > 0){
            totalPrice=(item.getPrice()*count)+DELIVERY_FEE;
        }
    }
}