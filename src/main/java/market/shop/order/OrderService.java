package market.shop.order;

import market.shop.item.Item;
import market.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import market.shop.orderitem.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;



    /** 주문 */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        if (item == null) {
            throw new IllegalArgumentException("No such item: " + itemId);
        }

        //상품 원래 가격 계산
        int originalPrice=item.getPrice()*count;

        //주문 가격 : 원래 가격에서 할인된 금액 빼기 (일정 수준 이상일 때 10% 할인)
        int orderPrice;

        if(originalPrice >= 10000){
            orderPrice=originalPrice-(int)(originalPrice*0.1);
        }else{
            orderPrice=originalPrice;
        }

        if(item.getCoupon() != null){
            orderPrice -= item.getCoupon().getDiscountAmount();
            if(orderPrice < 0){
                throw new IllegalStateException("Discounted price cannot be less than zero.");
            }
        }

        // 배달비 계산 및 주문 상품 생성 및 설정 순서 변경
        int deliveryFee = 3000;

        OrderItem orderItem = OrderItem.createOrderItem(item, orderPrice + deliveryFee,count);


        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(orderItem);

        // 주문 생성 및 저장
        Order createdOrder = Order.createOrder(member,orderItems);

        this.orderRepository.save(createdOrder);

        return createdOrder.getId();
    }

    public Order findOrder(Long id) {
        return this.orderRepository.findOne(id);
    }
}