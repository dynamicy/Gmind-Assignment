package io.csie.chris.demo;

import io.csie.chris.demo.scheduler.BtcPriceScheduler;
import io.csie.chris.demo.repository.BtcPriceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
public class BtcPricingApplicationTest {

    @Autowired
    private BtcPriceScheduler btcPriceScheduler;

    @Test
    void contextLoads() {
    }

    @Test
    void testShutdownHook() throws Exception {
        DisposableBean shutdownHook = new BtcPricingApplication().shutdownHook(btcPriceScheduler);
        shutdownHook.destroy();

        verify(btcPriceScheduler).persistPriceOnShutdown();
    }

    @Configuration
    static class TestConfig {

        @Bean
        public BtcPriceScheduler btcPriceScheduler() {
            BtcPriceScheduler scheduler = Mockito.mock(BtcPriceScheduler.class);
            Logger mockLogger = Mockito.mock(Logger.class);
            Mockito.when(scheduler.getLogger()).thenReturn(mockLogger);
            return scheduler;
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            return Mockito.mock(RedisTemplate.class);
        }

        @Bean
        public BtcPriceRepository btcPriceRepository() {
            return Mockito.mock(BtcPriceRepository.class);
        }
    }
}