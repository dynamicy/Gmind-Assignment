package io.csie.chris.demo.service;

import io.csie.chris.demo.dto.BtcPriceModel;
import io.csie.chris.demo.entity.BtcPrice;
import io.csie.chris.demo.repository.BtcPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BtcPriceService {

    public static final String LATEST_BTC_PRICE_KEY = "latestBtcPrice";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BtcPriceRepository btcPriceRepository;

    public BtcPriceModel getCurrentBtcPrice() {
        // First try to get the price from Redis
        BtcPrice btcPrice = (BtcPrice) redisTemplate.opsForValue().get(LATEST_BTC_PRICE_KEY);

        // If not found in Redis, get it from the database
        if (btcPrice == null) {
            btcPrice = btcPriceRepository.findById("btcPrice").orElse(null);
            // Cache the result in Redis for future requests
            if (btcPrice != null) {
                redisTemplate.opsForValue().set(LATEST_BTC_PRICE_KEY, btcPrice);
            }
        }

        // Convert to BtcPriceModel before returning
        if (btcPrice != null) {
            return BtcPriceModel.fromEntity(btcPrice);
        }
        return null;
    }
}