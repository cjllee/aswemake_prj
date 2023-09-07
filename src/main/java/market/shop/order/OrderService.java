package market.shop.order;

import jakarta.persistence.EntityManager;
import market.shop.coupon.Coupon;
import market.shop.coupon.DiscountType;
import market.shop.item.Item;
import market.shop.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
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
    private final EntityManager em;

    /** 주문 */
    @Transactional
    public Long order(Long memberId,Long itemId,int count, List<Coupon> coupons){

        Member member=memberRepository.findOne(memberId);
        Item item=itemRepository.findOne(itemId);

        Order order=new Order();
        order.setMember(member);
        order.setItem(item);

        // Set the count field in the Order entity.
        order.setCount(count);

        int totalPrice = item.getPrice() * count;

        for (Coupon coupon : coupons) {
            if (coupon.getDiscountType() == DiscountType.PERCENT) {
                totalPrice -= totalPrice * (coupon.getDiscountAmount() / 100.0);
                order.setCouponType(DiscountType.PERCENT); // Set the discount type in the Order entity.
            } else if (coupon.getDiscountType() == DiscountType.AMOUNT) {
                totalPrice -= coupon.getDiscountAmount();
                order.setCouponType(DiscountType.AMOUNT); // Set the discount type in the Order entity.
            }

            coupon.setItem(item);
            coupon.setOrder(order);

            em.persist(coupon);

        }

        totalPrice += 3000;

        order.setTotalPrice(totalPrice);

        return  orderRepository.save(order);

    }

    public Order findOrder(Long id) {
        return this.orderRepository.findOne(id);
    }
}