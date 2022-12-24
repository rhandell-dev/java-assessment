package com.example.takehome.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class CommonConfig {

    @Bean
    public ObjectMapper jsonMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToEnable(DeserializationFeature.UNWRAP_ROOT_VALUE)
                .build();
    }

    @Bean
    public WebClient webClient(ObjectMapper jsonMapper) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5000,
                                TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000,
                                TimeUnit.MILLISECONDS)));
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonDecoder(
                                new Jackson2JsonDecoder(jsonMapper)))
                .build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies).build();
    }

}
