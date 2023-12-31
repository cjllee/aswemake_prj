package market.shop;

import lombok.RequiredArgsConstructor;
import market.shop.coupon.Coupon;
import market.shop.coupon.DiscountType;
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
import market.shop.pricehistory.PriceHistory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    } // 성공

    //상품 등록
    @PostMapping("/items")
    public void createItem(@RequestBody ItemForm form, @RequestParam Long memberId) {
        Item item = new Item();
        item.setName(form.getName());
        item.setPrice(form.getPrice());

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setItem(item);
        priceHistory.setPrice(form.getPrice());
        priceHistory.setChangedAt(LocalDateTime.now());

        itemService.saveItem(item , memberId, priceHistory);
    }


    // 상품 조회 API
    @GetMapping("/items/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return itemService.findOne(itemId);
    }

    //삭제
    @DeleteMapping("/items/{memberId}/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long memberId, @PathVariable Long itemId) {
        itemService.deleteItem(memberId, itemId);
        return ResponseEntity.noContent().build();
    }


    // 상품 가격 수정 API
    @PutMapping("/items/price")
    public void updateItemPrice(@RequestBody UpdatePriceRequest request) {
        Item item = itemService.findByName(request.getItemName());

        if (item == null) {
            throw new IllegalArgumentException("해당 이름의 상품이 없습니다.");
        }
        itemService.updatePrice(request.getMemberId(), item.getId(), request.getPrice());
    }


    // 상품 가격 조회
    @GetMapping("/items/{itemId}/price-at-time")
    public String getItemPriceAndNameAtTime(@PathVariable Long itemId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime dateTime) {
        return itemService.findPriceAndNameAtTime(itemId, dateTime);
    }


    // 주문 생성 API
    @PostMapping("/orders")
    public ResponseEntity<Long> placeOrder(@RequestBody @Validated OrderRequest request){

        Coupon coupon = new Coupon();

        if(request.getCoupons().equals("PERCENT")) {
            coupon.setDiscountType(DiscountType.PERCENT);
            coupon.setDiscountAmount(10);
        } else if(request.getCoupons().equals("AMOUNT")) {
            coupon.setDiscountType(DiscountType.AMOUNT);
            coupon.setDiscountAmount(1000);
        }

        Long orderId = orderService.order(request.getMemberId(), request.getItemId(), request.getCount(), coupon);

        return ResponseEntity.ok(orderId);
    }

    //주문 조회
    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable("id") Long orderId) {
        return orderService.findOrder(orderId);
    }

}


