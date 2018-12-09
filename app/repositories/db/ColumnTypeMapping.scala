package repositories.db

import java.sql.Timestamp
import java.time.LocalDateTime
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.{UserId, TaskId}

private[db] trait ColumnTypeMapping { self: HasDatabaseConfigProvider[JdbcProfile] =>
  import profile.api._

  implicit val localDateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](
    Timestamp.valueOf(_),
    _.toLocalDateTime
  )

  implicit val userIdMapper = MappedColumnType.base[UserId, String](_.value, UserId(_))

  implicit val taskIdMapper = MappedColumnType.base[TaskId, String](_.value, TaskId(_))
}
