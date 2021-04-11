package leo.bachelorsthesis.backend.config;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import leo.bachelorsthesis.backend.builders.DtoBuilder;
import leo.bachelorsthesis.backend.clients.TokenClient;
import leo.bachelorsthesis.backend.clients.EmpathyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan({"leo.bachelorsthesis.backend.config"})
public class AppConfig {

    @Value("${tokenServerUrl}")
    private String tokenServerUrl;

    @Value("${staticFilePath}")
    private String staticFilePath;

    @Value("${empathyServerUrl}")
    private String empathyServerUrl;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DtoBuilder dtoBuilder() {
        return new DtoBuilder();
    }

    @Bean
    public TokenClient tokenClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(TokenClient.class))
                .logLevel(Logger.Level.FULL)
                .target(TokenClient.class, tokenServerUrl);
    }

    @Bean
    public EmpathyClient empathyClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(EmpathyClient.class))
                .logLevel(Logger.Level.FULL)
                .target(EmpathyClient.class, empathyServerUrl);
    }
}
