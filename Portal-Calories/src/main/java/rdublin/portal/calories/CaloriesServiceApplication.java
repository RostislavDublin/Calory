package rdublin.portal.calories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import rdublin.portal.auth.oauth2.PortalAccessTokenConverter;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableResourceServer
@EnableDiscoveryClient
public class CaloriesServiceApplication {

    @Autowired
    private PortalAccessTokenConverter customAccessTokenConverter;

    public static void main(String[] args) {
        SpringApplication.run(CaloriesServiceApplication.class, args);
    }

    @Bean
    public PortalAccessTokenConverter customAccessTokenConverter() {
        return new PortalAccessTokenConverter();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");

        converter.setAccessTokenConverter(customAccessTokenConverter);

        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
