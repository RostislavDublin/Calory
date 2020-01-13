package rdublin.portal.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SecurityConstants {
    public static final SecretKey SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}