package com.base.provider;

import com.base.configuration.property.JwtProperties;
import com.base.shared.util.PemUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties props;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtTokenProvider(JwtProperties props) {
        this.props = props;
        try {
            this.privateKey = PemUtils.readPrivateKey(Path.of(props.getPrivateKeyPath()));
            this.publicKey  = PemUtils.readPublicKey(Path.of(props.getPublicKeyPath()));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load RSA keys for JWT", e);
        }
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + props.getExpirationMs());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(privateKey, SignatureAlgorithm.RS256)  // ký bằng PRIVATE
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)                        // verify bằng PUBLIC
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | SecurityException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
