package com.base_ktor_project.security.password_hashing

import com.base_ktor_project.model.hash.SaltedHash
import java.security.SecureRandom
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

class SHA256HashingService : HashingService {

    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(
            hash = hash,
            salt = saltAsHex
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 16): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}