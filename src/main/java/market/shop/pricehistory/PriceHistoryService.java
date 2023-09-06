package market.shop.pricehistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceHistoryService {
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public List<PriceHistory> findByChangedAt(LocalDateTime start, LocalDateTime end) {
        return priceHistoryRepository.findByChangedAt(start, end);
    }
}