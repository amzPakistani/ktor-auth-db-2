package example.com.security.token

data class TokenConfig(
    val issuer:String,
    val audience:String,
    val expiry:Long,
    val secret:String
)