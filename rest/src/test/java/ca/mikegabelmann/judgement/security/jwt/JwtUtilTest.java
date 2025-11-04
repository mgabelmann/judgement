package ca.mikegabelmann.judgement.security.jwt;

import ca.mikegabelmann.judgement.config.JudgementConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtUtil.class, JudgementConfiguration.class})
@TestPropertySource(properties = {
        "jwt.secret=a9c9cca61f97e3d9bf484cdbc870b7ca7e37d77bfe4dfd464bff5d505f2de3d7",
        "jwt.refreshtoken.salt=a9c9cca61f97e3d9bf484cdbc870b7ca7e37d77bfe4dfd464bff5d505f2de3d7",
        "jwt.access.expiration=3600000",
        "jwt.refresh.expiration=84000000"}
)
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;


    @BeforeEach
    void beforeEach() {
        Assertions.assertNotNull(jwtUtil);
    }

    @Test
    void generateAccessToken() {
        String token = jwtUtil.generateAccessToken("username", Collections.EMPTY_LIST);
        Assertions.assertNotNull(token);
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtUtil.generateAccessToken("username", Collections.EMPTY_LIST);
        String username = jwtUtil.getUsernameFromToken(token);

        Assertions.assertEquals("username", username);
    }

    @Test
    void test1_validateToken() {
        String token = jwtUtil.generateAccessToken("username", Collections.EMPTY_LIST);
        Assertions.assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void test2_validateToken() {
        String token = jwtUtil.generateAccessToken("username", Collections.EMPTY_LIST);
        token += "x";
        Assertions.assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void test2_generateAccessToken() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        String token = jwtUtil.generateAccessToken("username", authorities);

        Assertions.assertNotNull(token);

        Collection<? extends GrantedAuthority> roles = jwtUtil.getRolesFromToken(token);
        Assertions.assertNotNull(roles);
        Assertions.assertEquals(2, roles.size());
        Assertions.assertEquals(authorities, roles);
    }

    @Test
    void test3_generateRefreshToken() {
        String token = jwtUtil.generateRefreshToken();
        Assertions.assertNotNull(token);
        Assertions.assertDoesNotThrow(() -> UUID.fromString(token));
    }

    @Test
    void test1_getRefreshTokenExpiration() {
        Assertions.assertTrue(Instant.now().isBefore(jwtUtil.getRefreshTokenExpiration()));
    }

    @Test
    void test1_hashRefreshToken() {
        String token = jwtUtil.generateRefreshToken();
        Assertions.assertNotNull(token);

        String hashedToken = Assertions.assertDoesNotThrow(() -> jwtUtil.hashRefreshToken(token));
        Assertions.assertNotNull(hashedToken);
    }

    @Test
    void test1_ensureVariableSet() {
        Assertions.assertThrows(NullPointerException.class, () -> jwtUtil.ensureVariableSet("x", null, ""));
        Assertions.assertThrows(NullPointerException.class, () -> jwtUtil.ensureVariableSet("x", "", ""));
    }

    @Test
    void test2_ensureVariableSet() {
        Assertions.assertDoesNotThrow(() -> jwtUtil.ensureVariableSet("x", "aaa", ""));
    }

}