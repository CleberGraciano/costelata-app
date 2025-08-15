package com.integracaoOmie.integracaoOmie.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("./") // ou "src/main/resources" se você colocar o .env lá
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }
}
