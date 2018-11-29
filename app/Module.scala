import com.google.inject.AbstractModule
import com.google.inject.name.Names
import repositories.{TaskRepository, TaskRepositoryOnMemoryImpl}
import utils.{IdGenerator, SystemDateTime}

class Module extends AbstractModule {
  def configure() = {
    bind(classOf[TaskRepository]).to(classOf[TaskRepositoryOnMemoryImpl])
    bind(classOf[IdGenerator]).toInstance(IdGenerator.defaultImpl)
    bind(classOf[SystemDateTime]).toInstance(SystemDateTime.defaultImpl)
  }
}
