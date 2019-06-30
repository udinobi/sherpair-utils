/*
 * Copyright 2019 Sherpair Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sherpair.typeclass

import java.security.SecureRandom

import com.sherpair.typeclass.AES._
import org.scalatest._

// scalastyle:off magic.number
class AesSpec extends FlatSpec with Matchers {
  "The AES typeclass" should "decrypt successfully an encrypted text, as long as secret and salt do not change" in {
    val secureRandom: SecureRandom = new SecureRandom()

    val bytesOfSecret: Array[Byte] = Array.fill(32)(0.byteValue)
    secureRandom.nextBytes(bytesOfSecret)

    val secret: Array[Char] = new String(bytesOfSecret).toCharArray

    val salt: Array[Byte] = Array.fill(8)(0.byteValue)
    secureRandom.nextBytes(salt)

    val data: String = 1234.toString

    val encrypted: EncryptedWithAES = data.encryptWithAES(secret, salt)
    val decrypted: Array[Char] = encrypted.decrypt(secret, salt)

    decrypted shouldEqual data.toCharArray
  }

  "The AES typeclass" should "throw BadPaddingException if decrypting the secret does not match" in {
    val salt: Array[Byte] = Array.fill(8)(0.byteValue)
    new SecureRandom().nextBytes(salt)

    val data: String = 1234.toString

    val encrypted: EncryptedWithAES = data.encryptWithAES("encryptionSecret".toCharArray, salt)

    intercept[javax.crypto.BadPaddingException] {
      encrypted.decrypt("decryptionSecret".toCharArray, salt)
    }
  }

  "The AES typeclass" should "throw BadPaddingException if decrypting the salt does not match" in {
    val bytesOfSecret: Array[Byte] = Array.fill(32)(0.byteValue)
    new SecureRandom().nextBytes(bytesOfSecret)

    val secret: Array[Char] = new String(bytesOfSecret).toCharArray

    val data: String = 1234.toString

    val encrypted: EncryptedWithAES = data.encryptWithAES(secret, "encryptionSalt".getBytes("UTF-8"))

    intercept[javax.crypto.BadPaddingException] {
      encrypted.decrypt(secret, "decryptionSalt".getBytes("UTF-8"))
    }
  }
}
// scalastyle:on magic.number
