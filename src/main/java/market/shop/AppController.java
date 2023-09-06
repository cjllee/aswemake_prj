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
import market.shop.pricehistory.PriceHistory;
import market.shop.pricehistory.PriceHistoryService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final ItemService itemService;

    private final MemberService memberService;

    private final OrderService orderService;

    private final PriceHistoryService priceHistoryService;
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

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setItem(item);
        priceHistory.setPrice(form.getPrice());
        priceHistory.setChangedAt(LocalDateTime.now());  // 현재 시간 설정

        itemService.saveItem(item , memberId, priceHistory);  // saveItem() 메서드에서 PriceHistroy 객체도 함께 저장해야 합니다.
    }

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

    @GetMapping("/price-histories")
    public List<PriceHistory> getPriceHistories(@RequestParam String start, @RequestParam String end) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        return priceHistoryService.findByChangedAt(startTime, endTime);
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


