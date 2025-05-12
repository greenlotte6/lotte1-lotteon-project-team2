package kr.co.lotteon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LotteOnApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteOnApplication.class, args);
    }

}
