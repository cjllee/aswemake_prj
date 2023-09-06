package market.shop;

import lombok.RequiredArgsConstructor;
import market.shop.item.Item;
import market.shop.item.ItemForm;
import market.shop.item.ItemService;
import market.shop.member.Member;
import market.shop.member.MemberForm;
import market.shop.member.MemberService;
import market.shop.order.Order;
import market.shop.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final ItemService itemService;

    private final MemberService memberService;

    private final OrderService orderService;

    // 회원 가입 API
    @PostMapping("/members")
    public void createMember(@RequestBody MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        member.setRole(form.getRole()); // role setting
        memberService.join(member);
    }

    //상품 등록
    @PostMapping("/items")
    public void createItem(@RequestBody ItemForm form, @RequestParam Long memberId) {
        Item item = new Item();
        item.setName(form.getName());
        item.setPrice(form.getPrice());
        itemService.saveItem(item , memberId);
    }

    // 상품 조회 API
    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemService.findOne(itemId);
    }

    // 상품 가격 수정 API
    @PutMapping("/items/{itemId}/price")
    public void updateItemPrice(@PathVariable Long itemId, @RequestParam int price, @RequestParam Long memberId) {
        itemService.updatePrice(memberId , itemId, price);
    }

    // 주문 생성 API
    @PostMapping("/orders")
    public Long createOrder(@RequestParam Long memberId,
                            @RequestParam Long itemId,
                            @RequestParam int count) {
        return orderService.order(memberId, itemId, count);
    }

    //주문 조회
    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable("id") Long orderId) {
        return orderService.findOrder(orderId);
    }




}


