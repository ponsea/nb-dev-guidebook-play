package services

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
// import play.api.inject.bind
// import play.api.inject.guice.GuiceApplicationBuilder
import utils._
import persistence.TaskRepositoryOnMemoryImpl
import models.{Task, TaskId, UserId}
import common.UnitSpec

class TaskServiceSpec extends UnitSpec with GuiceOneAppPerSuite with Injecting {
  def fixture() =
    new {
      val task1 = Task(
        TaskId("1"),
        "Scalaの勉強",
        true,
        UserId("1"),
        Some(LocalDateTime.parse("2019-04-01T00:00:00")),
        LocalDateTime.parse("2018-04-01T00:00:00"),
        LocalDateTime.parse("2018-04-01T00:00:00")
      )
      val task2 = Task(
        TaskId("2"),
        "Playの勉強",
        false,
        UserId("1"),
        Some(LocalDateTime.parse("2019-04-01T00:00:00")),
        LocalDateTime.parse("2018-04-01T00:00:00"),
        LocalDateTime.parse("2018-04-01T00:00:00")
      )
      val mockTaskRepository = new TaskRepositoryOnMemoryImpl(Set(task1, task2))
      val nowDateTime = LocalDateTime.now()
      val mockSystemDateTime = new SystemDateTime {
        def now() = nowDateTime
      }
      val newId = "new ID"
      val mockIdGenerator = new IdGenerator {
        def generateId() = newId
      }
      val taskService = new TaskService(mockTaskRepository)(mockIdGenerator,
                                                            mockSystemDateTime,
                                                            inject[ExecutionContext])
    }

  // override def fakeApplication() = new GuiceApplicationBuilder().overrides(bind[TaskRepository].toInstance(mockTaskRepository)).build()

  "TaskService#getAll" must {
    "return all tasks" in {
      val f = fixture(); import f._
      await(taskService.getAll()) must contain theSameElementsAs Seq(task1, task2)
    }
  }

  "TaskService#get" when {
    "a task of passed ID exists" must {
      "return the task" in {
        val f = fixture(); import f._
        val actual = await(taskService.get(task1.id))
        actual.right.value mustBe task1
      }
    }

    "a task of passed ID does not exists" must {
      "return a TaskNotFound" in {
        val f = fixture(); import f._
        val actual = await(taskService.get(TaskId("an ID that does not exists")))
        actual.left.value mustBe a[TaskNotFound]
      }
    }
  }

  "TaskService#create" must {
    "create a Task" in {
      val f = fixture(); import f._
      val creatorId = UserId("creator ID")
      val taskName = "task name"
      val deadline = None
      val expectedlyCreatedTask =
        Task(TaskId(newId), taskName, false, creatorId, deadline, nowDateTime, nowDateTime)
      val actuallyCreatedTask = await(taskService.create(taskName, deadline, creatorId))
      actuallyCreatedTask mustBe expectedlyCreatedTask
      val actuallySavedTask = await(taskService.get(TaskId(newId)))
      actuallySavedTask.right.value mustBe expectedlyCreatedTask
    }
  }

  "TaskService#update" when {
    "the task exists and the user updating it is permitted" must {
      "update the task" in {
        val f = fixture(); import f._
        val updaterId = task1.userId
        val newTaskName = "new task name"
        val newIsFinished = true
        val newDeadline = None
        val expectedlyUpdatedTask = task1.copy(name = newTaskName,
                                               isFinished = newIsFinished,
                                               deadline = newDeadline,
                                               updatedAt = nowDateTime)
        val result = await(
          taskService
            .update(task1.id, Some(newTaskName), Some(newIsFinished), Some(newDeadline), updaterId))
        result.right.value mustBe expectedlyUpdatedTask
        val savedTask = await(taskService.get(task1.id))
        savedTask.right.value mustBe expectedlyUpdatedTask
      }
    }

    "the task does not exists" must {
      "return a TaskNotFound" in {
        val f = fixture(); import f._
        val updaterId = task1.userId
        val result = await(
          taskService.update(TaskId("an ID that does not exists"), None, None, None, updaterId))
        result.left.value mustBe a[TaskNotFound]
      }
    }

    "the task exists but the user updating it is not permitted" must {
      "return a TaskPermissionDenied" in {
        val f = fixture(); import f._
        val updaterId = UserId("2") // the user of this ID is not permitted for updating task1
        val result = await(taskService.update(task1.id, None, None, None, updaterId))
        result.left.value mustBe a[TaskPermissionDenied.type]
      }
    }
  }

  "TaskService#delete" when {
    "the task exists and the user deleting it is permitted" must {
      "delete the task" in {
        val f = fixture(); import f._
        val deleterId = task1.userId
        val result = await(taskService.delete(task1.id, deleterId))
        result.right.value must be(())
        await(taskService.get(task1.id)).left.value mustBe a[TaskNotFound]
      }
    }

    "the task does not exists" must {
      "return a TaskNotFound" in {
        val f = fixture(); import f._
        val deleterId = task1.userId
        val result = await(taskService.delete(TaskId("an ID that does not exists"), deleterId))
        result.left.value mustBe a[TaskNotFound]
      }
    }

    "the task exists but the user deleting it is not permitted" must {
      "return a TaskPermissionDenied" in {
        val f = fixture(); import f._
        val deleterId = UserId("2") // the user of this ID is not permitted for deleting task1
        val result = await(taskService.delete(task1.id, deleterId))
        result.left.value mustBe a[TaskPermissionDenied.type]
      }
    }
  }
}
