package io.heapdog.core.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JWTUtils {

    private final MACSigner signer;
    private final MACVerifier verifier;

    public JWTUtils(@Value("${jwt.secret}") String secret) throws JOSEException {
        signer = new MACSigner(secret);
        verifier = new MACVerifier(secret);
    }

    public String generateToken(JWTClaimsSet claimsSet) throws JOSEException {
        if (claimsSet == null) {
            throw new IllegalArgumentException("Claims set is required");
        }

        if (claimsSet.getExpirationTime() == null) {
            throw new IllegalArgumentException("Expiration time is required");
        }
        if (claimsSet.getSubject() == null) {
            throw new IllegalArgumentException("Subject is required");
        }

        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimsSet.toJSONObject()));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public JWTClaimsSet verify(String token) throws ParseException, JOSEException, JwtValidationFailedException {
        if (token == null) {
            throw new IllegalArgumentException("Token is required");
        }
        JWSObject jwsObject = JWSObject.parse(token);
        if (jwsObject.verify(verifier)) {
            return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
        }
        throw new JwtValidationFailedException("Invalid token");
    }

}
