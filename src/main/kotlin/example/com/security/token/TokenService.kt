package example.com.security.token

interface TokenService {
    fun generate(tokenConfig: TokenConfig, vararg tokenClaim: TokenClaim):String
}