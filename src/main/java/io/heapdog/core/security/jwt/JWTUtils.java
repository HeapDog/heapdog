package io.heapdog.core.security.jwt;

import io.heapdog.core.exception.JwtValidationFailedException;
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
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(claimsSet.toJSONObject()));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public JWTClaimsSet verify(String token) throws ParseException, JOSEException, JwtValidationFailedException {
        JWSObject jwsObject = JWSObject.parse(token);
        if (jwsObject.verify(verifier)) {
            return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
        }
        throw new JwtValidationFailedException("Invalid token");
    }

}
