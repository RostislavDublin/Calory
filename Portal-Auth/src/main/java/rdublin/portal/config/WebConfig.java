package rdublin.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Stream;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    PortalProperties portalProperties;

    /**
     * Configure cross origin requests processing.
     *
     * @param r
     * @since 4.2
     */
    @Override
    public void addCorsMappings(CorsRegistry r) {
        Stream.of(r.addMapping("/**")).forEach(cr -> {
            cr.allowedOrigins(portalProperties.getAllowOrigin());
            cr.allowCredentials(true);
            cr.allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
            cr.maxAge(3210);
            cr.allowedHeaders("X-Requested-With", "Content-Type", "Authorization", "Origin", "Accept",
                    "Access-Control-Request-Method", "Access-Control-Request-Headers");
        });
    }
}
