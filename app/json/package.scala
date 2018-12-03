import play.api.libs.json._

package object json {
  implicit class JsPathOps(self: JsPath) {
    // JsPath#readNullableがundefinedとnullを区別しないので、区別するメソッドを独自で定義
    // 読み込むJsPathの末尾より前のパスがなかったらJsError("error.path.missing")
    // 読み込むJsPathの末尾がundefinedならNone
    // 読み込むJsPathの値がnullならSome(None)
    // 読み込むJsPathに値が存在するならSome(Some(A))
    def readOptional[A](implicit reads: Reads[A]): Reads[Option[Option[A]]] = {
      Reads[Option[Option[A]]] { json =>
        self
          .applyTillLast(json)
          .fold(identity,
                _.fold(
                  _ => JsSuccess(None),
                  _ match {
                    case JsNull => JsSuccess(Some(None))
                    case js     => reads.reads(js).repath(self).map(v => Some(Some(v)))
                  }
                ))
      }
    }
    // ※インデントが奇抜ですが、scalafmtの結果です。
  }
}
