package market.shop.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import market.shop.member.Role;
import market.shop.member.Member;
import market.shop.member.MemberRepository;
import market.shop.pricehistory.PriceHistory;
import market.shop.pricehistory.PriceHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    @PersistenceContext
    private EntityManager em;

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PriceHistoryRepository priceHistoryRepository;


    public Item findByName(String name) {
        return itemRepository.findByName(name);
    }

    @Transactional
    public void saveItem(Item item, Long memberId, PriceHistory priceHistory) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        itemRepository.save(item);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        String formatDateTime = LocalDateTime.now().format(formatter);


        priceHistory.setChangedAt(LocalDateTime.parse(formatDateTime, formatter));

        priceHistoryRepository.save(priceHistory);
    }


    @Transactional
    public void updateItemName(Long memberId, Long itemId, String newName) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        Item item = findOne(itemId);
        item.setName(newName);
    }


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

    @Transactional
    public void updatePrice(Long memberId, Long itemId, int price) {
        Member member = memberRepository.findOne(memberId);
        if (member.getRole() != Role.Mart) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        Item item = findOne(itemId);


        PriceHistory history = new PriceHistory();
        history.setItem(item);
        history.setPrice(price);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        String formatDateTime = LocalDateTime.now().format(formatter);


        history.setChangedAt(LocalDateTime.parse(formatDateTime, formatter));

        em.persist(history);

        item.setPrice(price);
    }


    public String findPriceAndNameAtTime(Long itemId, LocalDateTime dateTime) {
        Item item = findOne(itemId);
        PriceHistory priceHistory = priceHistoryRepository.findByItemAndChangedAt(item, dateTime);
        if (priceHistory == null) {
            throw new NoSuchElementException("해당 상품 및 시간에 대한 가격 내역이 없습니다.");
        }
        return String.format("%s 시점의 %s 상품 가격 = %d원",
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                item.getName(),
                priceHistory.getPrice());
    }
}