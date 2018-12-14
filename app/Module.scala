import com.google.inject.AbstractModule
import com.google.inject.name.Names
import persistence._
import utils._

class Module extends AbstractModule {
  def configure() = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryOnMemoryImpl])
    bind(classOf[TaskRepository]).to(classOf[TaskRepositorySlickImpl])
    bind(classOf[IdGenerator]).to(classOf[IdGeneratorImpl])
    bind(classOf[SystemDateTime]).to(classOf[SystemDateTimeImpl])
  }
}
