package rdublin.portal.auth.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    public DefaultTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.cors()
            .and()
            .csrf()
            .disable()
            .anonymous().and().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/users").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/oauth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
