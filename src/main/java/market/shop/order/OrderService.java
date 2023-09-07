package market.shop.order;

import market.shop.item.Item;
import market.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    /** 주문 */
    @Transactional
    public Long order(Long memberId,Long itemId,int count){

        Member member=memberRepository.findOne(memberId);
        Item item=itemRepository.findOne(itemId);

        Order order=new Order();
        order.setMember(member);
        order.setItem(item);
        order.setCount(count);

        // 주문을 저장합니다.
        return  orderRepository.save(order);

    }


    public Order findOrder(Long id) {
        return this.orderRepository.findOne(id);
    }
}