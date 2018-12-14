package utils

import javax.inject.Singleton
import java.security.MessageDigest

trait Crypto {
  def digest(str: String): String
}

@Singleton()
class CryptoImpl extends Crypto {
  def digest(str: String) = {
    MessageDigest
      .getInstance("SHA-256")
      .digest(str.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString
  }
}
