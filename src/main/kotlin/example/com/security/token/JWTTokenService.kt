package example.com.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JWTTokenService:TokenService {
    override fun generate(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim): String {
        var token = JWT.create()
            .withAudience(tokenConfig.audience)
            .withExpiresAt(Date(System.currentTimeMillis()+tokenConfig.expiry))
            .withIssuer(tokenConfig.issuer)
        tokenClaim.forEach { claim ->
            token = token.withClaim(claim.name,claim.value)
        }
        return token.sign(Algorithm.HMAC256(tokenConfig.secret))
    }
}