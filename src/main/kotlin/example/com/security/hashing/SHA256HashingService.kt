package example.com.security.hashing

import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom

class SHA256HashingService:HashingService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltHex = Hex.encodeHexString(salt)
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        TODO("Not yet implemented")
    }
}