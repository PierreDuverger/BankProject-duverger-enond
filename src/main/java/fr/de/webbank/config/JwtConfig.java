package fr.de.webbank.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JwtConfig {
    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{60*1000}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

}
