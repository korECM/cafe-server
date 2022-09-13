package zip.cafe.service.auth.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*

data class AppleIdentityTokenHeader(
    /**
     *  A 10-character identifier key, obtained from your developer account.
     */
    val kid: String,
    /**
     * The encryption algorithm used to encrypt the token.
     */
    val alg: String,
)

data class AppleAuthKeys(
    val keys: List<Key>
) {
    fun getMatchedKey(kid: String, alg: String): Key? {
        return keys.find { it.kid == kid && it.alg == alg }
    }
}

data class Key(
    /**
     * The encryption algorithm used to encrypt the token.
     */
    val alg: String,
    /**
     *  The exponent value for the RSA public key.
     */
    val e: String,
    /**
     *  A 10-character identifier key, obtained from your developer account.
     */
    val kid: String,
    /**
     *  The key type parameter setting. You must set to "RSA".
     */
    val kty: String,
    /**
     *  The modulus value for the RSA public key.
     */
    val n: String,
    /**
     *  The intended use for the public key.
     */
    val use: String
) {
    fun getPublicKey(): PublicKey {
        val urlDecoder = Base64.getUrlDecoder()
        val modules = BigInteger(1, urlDecoder.decode(n))
        val publicExponent = BigInteger(1, urlDecoder.decode(e))

        val publicKeySpec = RSAPublicKeySpec(modules, publicExponent)
        return KeyFactory.getInstance(kty).generatePublic(publicKeySpec)
    }
}

data class AppleIdentityTokenBody(
    val aud: String,
    @JsonProperty("auth_time")
    val authTime: Int,
    @JsonProperty("c_hash")
    val cHash: String,
    val email: String,
    @JsonProperty("email_verified")
    val emailVerified: String,
    val exp: Int,
    val iat: Int,
    @JsonProperty("is_private_email")
    val isPrivateEmail: String,
    val iss: String,
    val nonce: String,
    @JsonProperty("nonce_supported")
    val nonceSupported: Boolean,
    val sub: String
)
