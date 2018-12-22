package utils

import javax.inject.Singleton
import java.security.MessageDigest
import scala.util.Random

trait Crypto {
  def digest(str: String): String
  def randomString(length: Int): String
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

  def randomString(length: Int) = {
    Random.alphanumeric.take(length).mkString
  }
}
