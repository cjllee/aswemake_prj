package market.shop.order;

import market.shop.delivery.Delivery;
import market.shop.item.Item;
import market.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import market.shop.orderitem.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        //상품 원래 가격
        int originalPrice=item.getPrice()*count;

        //주문 가격 : 원래 가격에서 할인된 금액 빼기 (일정 수준 이상일 때 10% 할인)
        int orderPrice;
        if(originalPrice >= 10000){
            orderPrice=originalPrice-(int)(originalPrice*0.1);  // Apply a 10% discount.
        }else{
            orderPrice=originalPrice;
        }

        if(item.getCoupon() != null){
            orderPrice -= item.getCoupon().getDiscountAmount();
            if(orderPrice < 0){
                throw new IllegalStateException("Discounted price cannot be less than zero.");
            }
        }

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderPrice, count);

        // 주문 생성
        Order order = Order.createOrder(member,orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /** 주문 취소 */

    //public List<Order> findOrders(OrderSearch orderSearch) {
     //   return orderRepository.findAllByString(orderSearch);
  //  }


}