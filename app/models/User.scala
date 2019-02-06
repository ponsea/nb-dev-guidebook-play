package models

import java.time.LocalDateTime
import utils.{IdGenerator, Crypto}

case class User(id: UserId, // ユーザーID
                name: String, // ユーザー名
                email: String, // Emailアドレス
                hashedPassword: String, // ハッシュ化されたパスワード
                salt: String, // ハッシュ化するパスワードに添えるソルト(レインボー攻撃対策)
                createdAt: LocalDateTime, // 作成日時
                updatedAt: LocalDateTime) { // 最終更新日時
  def canEditBy(editorId: UserId): Boolean = this.id == editorId

  def passwordEquals(password: String)(implicit crypto: Crypto): Boolean = {
    this.hashedPassword == crypto.digest(password + this.salt)
  }
}

case class UserId(value: String) extends AnyVal

object UserId {
  def newId()(implicit idGen: IdGenerator) = UserId(idGen.generateId())
}
