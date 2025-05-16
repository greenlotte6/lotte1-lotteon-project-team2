package kr.co.lotteon.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* Application 암호화를 위한 환경설정
* */

@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword("lotteOn"); // encrypt key
        config.setAlgorithm("PBEWITHMD5ANDDES");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String encryptedText = encryptor.encrypt("553cbe3c55b08148e1bcad2930c886dc");
        System.out.println("카카오-id: (" + encryptedText + ")");

        encryptedText = encryptor.encrypt("Xw6xWUo43GltM6eqm6bG4prABeBmOIM7");
        System.out.println("카카오-secret: (" + encryptedText + ")");


        return encryptor;
    }
}
