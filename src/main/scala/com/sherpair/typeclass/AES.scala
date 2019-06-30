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

import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import javax.crypto.{Cipher, SecretKey, SecretKeyFactory}
import javax.crypto.spec.{IvParameterSpec, PBEKeySpec, SecretKeySpec}

object AES {

  case class EncryptedWithAES(encryptedData: Array[Byte], iv: Array[Byte]) {

    def decrypt(secret: Array[Char], salt: Array[Byte]): Array[Char] = {
      val (cipher: Cipher, secretKey: SecretKey) = genCipherAndSecretKey(secret, salt)
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv))
      new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8).toCharArray
    }
  }

  trait AES[T] {
    def encryptWithAES(secret: Array[Char], salt: Array[Byte], data: T): EncryptedWithAES
  }

  type Data = String

  implicit object Encryptor extends AES[Data] {
    override def encryptWithAES(secret: Array[Char], salt: Array[Byte], data: Data): EncryptedWithAES = {
      val (cipher: Cipher, secretKey: SecretKey) = genCipherAndSecretKey(secret, salt)
      cipher.init(Cipher.ENCRYPT_MODE, secretKey)
      EncryptedWithAES(
        cipher.doFinal(data.getBytes("UTF-8")),
        cipher.getParameters.getParameterSpec(classOf[IvParameterSpec]).getIV
      )
    }
  }

  implicit class Encrypting[Data](data: Data) {
    def encryptWithAES(secret: Array[Char], salt: Array[Byte])(implicit aesInstance: AES[Data]): EncryptedWithAES =
      aesInstance.encryptWithAES(secret, salt, data)
  }

  private def genCipherAndSecretKey(secret: Array[Char], salt: Array[Byte]): (Cipher, SecretKey) = {
    val keySpec: KeySpec = new PBEKeySpec(secret, salt, Short.MaxValue, (Byte.MinValue << 1).abs)
    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpec)
    (Cipher.getInstance("AES/CBC/PKCS5Padding"), new SecretKeySpec(secretKeyFactory.getEncoded, "AES"))
  }
}
