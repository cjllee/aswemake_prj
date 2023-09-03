package item;

import lombok.RequiredArgsConstructor;
import member.Grade;
import member.Member;
import member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public void saveItem(Item item, Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member.getGrade() != Grade.MAN) {
            throw new IllegalStateException("매니저만 생성및 업데이트가 가능합니다.");
        }
        itemRepository.save(item);
    }


    @Transactional
    public void deleteItem(Long itemId, Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member.getGrade() != Grade.MAN) {
            throw new IllegalStateException("매니저만이 삭제가 가능합니다.");
        }

        Item item = findOne(itemId);
        itemRepository.delete(item);
    }

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

}