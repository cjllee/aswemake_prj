package market.shop;


import market.shop.item.Item;
import market.shop.item.ItemService;
import market.shop.member.Member;
import market.shop.member.MemberForm;
import market.shop.member.MemberService;
import market.shop.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    // 상품 조회 API
    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemService.findOne(itemId);
    }

    // 상품 가격 수정 API
    @PutMapping("/items/{itemId}/price")
    public void updateItemPrice(@PathVariable Long itemId, @RequestParam int price) {
        itemService.updatePrice(itemId, price);
    }

    // 회원 가입 API
    @PostMapping("/members")
    public void createMember(@RequestBody MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);
    }

    // 주문 생성 API
    @PostMapping("/orders")
    public Long createOrder(@RequestParam Long memberId,
                            @RequestParam Long itemId,
                            @RequestParam int count) {
        return orderService.order(memberId, itemId, count);
    }
}

