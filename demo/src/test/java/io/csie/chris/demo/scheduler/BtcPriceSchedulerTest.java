package io.csie.chris.demo.scheduler;

import io.csie.chris.demo.entity.BtcPrice;
import io.csie.chris.demo.repository.BtcPriceRepository;
import io.csie.chris.demo.service.BtcPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BtcPriceSchedulerTest {

    private static final int CYCLE_DURATION = 360; // in seconds (6 minutes)

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private BtcPriceRepository btcPriceRepository;

    @InjectMocks
    private BtcPriceScheduler btcPriceScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void updatePrice_Increasing() {
        btcPriceScheduler.getCurrentPrice();
        long currentTimeMillis = System.currentTimeMillis();
        long seconds = currentTimeMillis / 1000;
        long elapsedTime = seconds % CYCLE_DURATION;
        long periodIndex = elapsedTime / 5;

        for (int i = 0; i < 36; i++) {
            if (periodIndex < 36) { // first 3 minutes, price increases
                btcPriceScheduler.updatePrice();
                verify(valueOperations, atLeastOnce()).set(eq(BtcPriceService.LATEST_BTC_PRICE_KEY), any(BtcPrice.class));
                verifyNoMoreInteractions(valueOperations);
            }
        }
    }

    @Test
    void updatePrice_Decreasing() {
        btcPriceScheduler.getCurrentPrice();
        long currentTimeMillis = System.currentTimeMillis();
        long seconds = currentTimeMillis / 1000;
        long elapsedTime = seconds % CYCLE_DURATION;
        long periodIndex = elapsedTime / 5;

        for (int i = 36; i < 72; i++) {
            if (periodIndex >= 36) { // next 3 minutes, price decreases
                btcPriceScheduler.updatePrice();
                verify(valueOperations, atLeastOnce()).set(eq(BtcPriceService.LATEST_BTC_PRICE_KEY), any(BtcPrice.class));
                verifyNoMoreInteractions(valueOperations);
            }
        }
    }

    @Test
    void persistLatestPrice() {
        BtcPrice btcPrice = new BtcPrice();
        btcPrice.setPrice(150);
        btcPrice.setTimestamp(System.currentTimeMillis());

        when(redisTemplate.opsForValue().get(BtcPriceService.LATEST_BTC_PRICE_KEY)).thenReturn(btcPrice);

        btcPriceScheduler.persistLatestPrice();

        verify(btcPriceRepository, times(1)).save(btcPrice);
        verifyNoMoreInteractions(btcPriceRepository);
    }

    @Test
    void persistPriceOnShutdown() {
        BtcPrice btcPrice = new BtcPrice();
        btcPrice.setPrice(150);
        btcPrice.setTimestamp(System.currentTimeMillis());

        when(redisTemplate.opsForValue().get(BtcPriceService.LATEST_BTC_PRICE_KEY)).thenReturn(btcPrice);

        btcPriceScheduler.persistPriceOnShutdown();

        verify(btcPriceRepository, times(1)).save(btcPrice);
        verifyNoMoreInteractions(btcPriceRepository);
    }
}