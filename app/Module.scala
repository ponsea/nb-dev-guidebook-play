import com.google.inject.AbstractModule
import com.google.inject.name.Names
import repositories.{TaskRepository, TaskRepositoryOnMemoryImpl}
import utils.{IdGenerator, SystemDateTime, IdGeneratorImpl, SystemDateTimeImpl}

class Module extends AbstractModule {
  def configure() = {
    bind(classOf[TaskRepository]).to(classOf[TaskRepositoryOnMemoryImpl])
    bind(classOf[IdGenerator]).to(classOf[IdGeneratorImpl])
    bind(classOf[SystemDateTime]).to(classOf[SystemDateTimeImpl])
  }
}
