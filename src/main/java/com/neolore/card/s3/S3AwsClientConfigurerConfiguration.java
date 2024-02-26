package com.neolore.card.s3;

import io.awspring.cloud.autoconfigure.core.AwsClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.time.Duration;

@Configuration
class S3AwsClientConfigurerConfiguration {

    @Bean
    AwsClientCustomizer<S3ClientBuilder> s3ClientBuilderAwsClientConfigurer() {
        return new S3AwsClientClientConfigurer();
    }

    static class S3AwsClientClientConfigurer implements AwsClientCustomizer<S3ClientBuilder> {
        @Override
        public ClientOverrideConfiguration overrideConfiguration() {
            return ClientOverrideConfiguration.builder().apiCallTimeout(Duration.ofMillis(5000)).build();
        }

        @Override
        public SdkHttpClient httpClient() {
            return ApacheHttpClient.builder().connectionTimeout(Duration.ofMillis(10000)).build();
        }
    }
}