package rdublin.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "portal")
public class PortalProperties {
    private String[] allowOrigin;
}
