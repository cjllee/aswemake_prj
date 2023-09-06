package market.shop.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import market.shop.member.Role;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import market.shop.pricehistory.PriceHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    @PersistenceContext // Add this line.
    private EntityManager em;

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Item findByName(String name) {
        return itemRepository.findByName(name);
    }

    @Transactional
    public void saveItem(Item item, Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        itemRepository.save(item);
    }// 저장


    @Transactional
    public void updateItemName(Long memberId, Long itemId, String newName) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        Item item = findOne(itemId);
        item.setName(newName);
    }// 아이템 이름 변경


    @Transactional
    public void deleteItem(Long itemId, Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        Item item = findOne(itemId);
        itemRepository.delete(item);
    } // 삭제

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public String findPriceAtTimeFormatted(Long itemId, LocalDateTime dateTime) {
        Item item = findOne(itemId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.format("%s 시점의 %s 상품 가격 = %d원",
                dateTime.format(formatter), item.getName(), item.getPrice());
    }

    public void updatePrice(Long memberId, Long itemId, int price) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        Item item = findOne(itemId);

        // Create and save a PriceHistory entity when the price is updated.
        PriceHistory history = new PriceHistory();
        history.setItem(item);
        history.setPrice(price);

        // Set the change time to the current time.
        history.setChangeTime(LocalDateTime.now());

        em.persist(history);

        item.setPrice(price);
    }


}