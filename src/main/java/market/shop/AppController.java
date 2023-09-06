package market.shop;

import lombok.RequiredArgsConstructor;
import market.shop.item.Item;
import market.shop.item.ItemForm;
import market.shop.item.ItemService;
import market.shop.item.UpdatePriceRequest;
import market.shop.member.Member;
import market.shop.member.MemberForm;
import market.shop.member.MemberService;
import market.shop.order.Order;
import market.shop.order.OrderRequest;
import market.shop.order.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

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
        member.setRole(form.getRole());
        memberService.join(member);
    } // 완료

    //상품 등록
    @PostMapping("/items")
    public void createItem(@RequestBody ItemForm form, @RequestParam Long memberId) {
        Item item = new Item();
        item.setName(form.getName());
        item.setPrice(form.getPrice());
        itemService.saveItem(item , memberId);
    } //완료

    // 상품 조회 API
    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemService.findOne(itemId);
    }
    //완료


    // 상품 가격 수정 API
    @PutMapping("/items/price")
    public void updateItemPrice(@RequestBody UpdatePriceRequest request) {
        Item item = itemService.findByName(request.getItemName());

        if (item == null) {
            throw new IllegalArgumentException("해당 이름의 상품이 없습니다.");
        }
        itemService.updatePrice(request.getMemberId(), item.getId(), request.getPrice());
    }
    //완료

    //상품 가격 조회
    @GetMapping("/items/{itemId}/price")
    public String getItemPriceAt(@PathVariable Long itemId,
                                 @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return itemService.findPriceAtTimeFormatted(itemId, dateTime);
    }
// 여기부터 하기


    // 주문 생성 API
    @PostMapping("/orders")
    public Long createOrder(@RequestBody OrderRequest request) {
        return orderService.order(request.getMemberId(), request.getItemId(), request.getCount());
    }

    //주문 조회
    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable("id") Long orderId) {
        return orderService.findOrder(orderId);
    }
// 쿠폰 적용되는지 확인 하기
}


