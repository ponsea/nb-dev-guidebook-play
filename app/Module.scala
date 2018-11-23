import com.google.inject.AbstractModule
import com.google.inject.name.Names
import repositories.{TaskRepository, TaskRepositoryOnMemoryImpl}

class Module extends AbstractModule {
  def configure() = {
    bind(classOf[TaskRepository]).to(classOf[TaskRepositoryOnMemoryImpl])
  }
}
