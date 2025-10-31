package ca.mikegabelmann.judgement.security.jwt;

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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtUtil.class)
@TestPropertySource(properties = {
        "jwt.secret=a9c9cca61f97e3d9bf484cdbc870b7ca7e37d77bfe4dfd464bff5d505f2de3d7",
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
        String token = jwtUtil.generateAccessToken("username");
        Assertions.assertNotNull(token);
    }

//    @Test
//    void generateRefreshToken() {
//        String token = jwtUtil.generateRefreshToken("username");
//        Assertions.assertNotNull(token);
//    }

    @Test
    void getUsernameFromToken() {
        String token = jwtUtil.generateAccessToken("username");
        String username = jwtUtil.getUsernameFromToken(token);

        Assertions.assertEquals("username", username);
    }

    @Test
    void test1_validateToken() {
        String token = jwtUtil.generateAccessToken("username");
        Assertions.assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void test2_validateToken() {
        String token = jwtUtil.generateAccessToken("username");
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

        Collection<? extends GrantedAuthority> roles = jwtUtil.parseRolesFromToken(token);
        Assertions.assertNotNull(roles);
        Assertions.assertEquals(2, roles.size());
        Assertions.assertEquals(authorities, roles);
    }

//    @Test
//    void test2_generateRefreshToken() {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        String token = jwtUtil.generateRefreshToken().toString();//.generateRefreshToken("username", authorities);
//
//        Assertions.assertNotNull(token);
//
//        Collection<? extends GrantedAuthority> roles = jwtUtil.parseRolesFromToken(token);
//        Assertions.assertNotNull(roles);
//        Assertions.assertEquals(2, roles.size());
//    }

}