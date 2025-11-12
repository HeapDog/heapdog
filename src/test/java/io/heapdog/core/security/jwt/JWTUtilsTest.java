package io.heapdog.core.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

class JWTUtilsTest {

    private JWTUtils jwtUtils;

    @BeforeEach
    void setUp() throws JOSEException {
        String testSecret = "01234567890123456789012345678912"; // 32 characters for HS256
        jwtUtils = new JWTUtils(testSecret);
    }

    @Test
    void generateToken_withSubject_shouldReturnToken() throws JOSEException {
        var token = jwtUtils.generateToken(new JWTClaimsSet.Builder()
                .subject("test")
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 1000))
                .build());
        Assertions.assertNotNull(token);
    }

    @Test
    void generateToken_withNoSubject_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(new JWTClaimsSet.Builder()
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 1000))
                .build()));
    }

    @Test
    void generateToken_withNoExpirationTime_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(new JWTClaimsSet.Builder()
                .subject("test")
                .build()));
    }

    @Test
    void generateToken_withNoExpirationTimeAndSubject_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(new JWTClaimsSet.Builder()
                .build()));
    }

    @Test
    void generateToken_withNullClaimsSet_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtUtils.generateToken(null));
    }

    @Test
    void verify_withNullToken_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtUtils.verify(null));
    }

    @Test
    void verify_withInvalidToken_shouldThrowParseException() {
        String invalidToken = "invalid.token.value";
        Assertions.assertThrows(ParseException.class, () -> jwtUtils.verify(invalidToken));
    }

//    @Test
//    void verify_withInvalidSignature_shouldThrowJOSEException() throws JOSEException {
//        String validToken = jwtUtils.generateToken(new JWTClaimsSet.Builder()
//                .subject("test")
//                .expirationTime(new Date(System.currentTimeMillis() +  100)).build());
//    }

    @Test
    void verify_withValidToken_shouldReturnClaimsSet() throws JOSEException, ParseException, JwtValidationFailedException {
        String validToken = jwtUtils.generateToken(new JWTClaimsSet.Builder()
                .subject("test")
                .expirationTime(new Date(System.currentTimeMillis() + 1000)).build());
        JWTClaimsSet claimsSet = jwtUtils.verify(validToken);
        Assertions.assertNotNull(claimsSet);
        Assertions.assertEquals("test", claimsSet.getSubject());
    }

    @Test
    void verify() {
    }
}