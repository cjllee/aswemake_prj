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

        List<OrderItem> orderItems= new ArrayList<>();

        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        orderItems.add(orderItem);

        Order createdOrders = Order.createOrder(member ,orderItems);

        // Apply delivery fee of 3000.
        createdOrders.applyDeliveryFee(3000);

        // Set the count and itemId fields in the created order.
        createdOrders.setCount((long) count);
        createdOrders.setItemId(itemId);

        this.orderRepository.save(createdOrders);

        return createdOrders.getId();
    }

    public Order findOrder(Long id) {
        return this.orderRepository.findOne(id);
    }
}