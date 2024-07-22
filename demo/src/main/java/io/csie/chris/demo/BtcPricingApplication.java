package io.csie.chris.demo;

import io.csie.chris.demo.scheduler.BtcPriceScheduler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class BtcPricingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BtcPricingApplication.class, args);
    }

    @Bean
    public DisposableBean shutdownHook(@Autowired BtcPriceScheduler btcPriceScheduler) {
        return () -> {
            btcPriceScheduler.persistPriceOnShutdown();
            btcPriceScheduler.getLogger().info("Persisted BTC price on shutdown.");
        };
    }
}