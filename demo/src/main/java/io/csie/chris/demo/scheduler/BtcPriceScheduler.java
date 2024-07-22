package io.csie.chris.demo.scheduler;

import io.csie.chris.demo.entity.BtcPrice;
import io.csie.chris.demo.repository.BtcPriceRepository;
import io.csie.chris.demo.service.BtcPriceService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BtcPriceScheduler {

    private static final int INITIAL_PRICE = 100;
    private static final int PRICE_INCREMENT = 10;
    private static final int MAX_PRICE = 460;
    private static final int MIN_PRICE = 100;

    private final AtomicInteger currentPrice = new AtomicInteger(INITIAL_PRICE);
    private volatile boolean increasing = true;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BtcPriceRepository btcPriceRepository;

    @Getter
    private final Logger logger = LoggerFactory.getLogger(BtcPriceScheduler.class);

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void updatePrice() {
        if (increasing) {
            currentPrice.addAndGet(PRICE_INCREMENT);
            if (currentPrice.get() >= MAX_PRICE) {
                increasing = false;
            }
        } else {
            currentPrice.addAndGet(-PRICE_INCREMENT);
            if (currentPrice.get() <= MIN_PRICE) {
                increasing = true;
            }
        }

        // Cache the latest price in Redis
        BtcPrice btcPrice = new BtcPrice();
        btcPrice.setPrice(currentPrice.get());
        btcPrice.setTimestamp(System.currentTimeMillis());
        redisTemplate.opsForValue().set(BtcPriceService.LATEST_BTC_PRICE_KEY, btcPrice);

        logger.info("Current BTC Price: {} USD", currentPrice.get());
    }

    @Scheduled(fixedRate = 60000) // Persist the latest cached data every minute
    public void persistLatestPrice() {
        BtcPrice latestPrice = (BtcPrice) redisTemplate.opsForValue().get(BtcPriceService.LATEST_BTC_PRICE_KEY);
        if (latestPrice != null) {
            latestPrice.setId("btcPrice");
            btcPriceRepository.save(latestPrice);
            logger.info("Persisted BTC price: {} USD at timestamp {}", latestPrice.getPrice(), latestPrice.getTimestamp());
        }
    }

    // Method to force persist the latest price to database, e.g., during application shutdown
    public void persistPriceOnShutdown() {
        BtcPrice latestPrice = (BtcPrice) redisTemplate.opsForValue().get(BtcPriceService.LATEST_BTC_PRICE_KEY);
        if (latestPrice != null) {
            latestPrice.setId("btcPrice");
            btcPriceRepository.save(latestPrice);
            logger.info("Persisted BTC price: {} USD at timestamp {}", latestPrice.getPrice(), latestPrice.getTimestamp());
        }
    }

    public int getCurrentPrice() {
        return currentPrice.get();
    }
}