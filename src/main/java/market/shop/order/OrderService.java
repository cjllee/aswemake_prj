package market.shop.order;

import market.shop.item.Item;
import market.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import market.shop.orderitem.OrderItem;

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
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        if (item == null) {
            throw new IllegalArgumentException("No such item: " + itemId);
        }

        // 배달비 계산 및 주문 상품 생성 및 설정 순서 변경
        int deliveryFee = 3000;

        List<OrderItem> orderItems= new ArrayList<>();

        // Here we create an OrderItem and set the count value.
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice()+deliveryFee,count);

        // Add the created order item to the list.
        orderItems.add(orderItem);

        // Create the order and save it.
        Order createdOrders = Order.createOrder(member ,orderItems);

        this.orderRepository.save(createdOrders);

        return createdOrders.getId();
    }
    public Order findOrder(Long id) {
        return this.orderRepository.findOne(id);
    }
}