package otus_api_tests

import Retry
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.junit.jupiter.api.Assertions
import org.testng.annotations.Test
import java.time.LocalDateTime

class TestClass: BaseHttpClient() {


    fun createTask(task: Task): Task{
        val testUrl = "$url/api/v1/tasks"
//        val testTask = Task(name= "apiTestTask${LocalDateTime.now()}", priority = Priority.values().random())
        val rsPost = postTaskRequest(body = task)
        val respTask = Gson().fromJson(rsPost.body!!.string(), Task::class.java)
        Assertions.assertEquals(200, rsPost.code)
        Assertions.assertEquals(task.name, respTask.name)
        Assertions.assertEquals(task.priority, respTask.priority)
        Assertions.assertFalse(respTask.completed)
        Assertions.assertTrue(respTask.id != null)

        return respTask
    }

    @Test(retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun createTaskAndCheckTask(){
        val testTask = Task(name= "apiTestTask${LocalDateTime.now()}", priority = Priority.values().random())
        val respTask = createTask(testTask)
        val respTaskList = mutableListOf<Task>()
        val getrs = getTasksRequest()
        val getRsArray = JsonParser.parseString(getrs.body!!.string()).getAsJsonArray()
        getRsArray.forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        testTask.id = respTask.id
        Assertions.assertTrue(respTaskList.contains(testTask))
    }

    @Test(retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun completeRNDTask(){
        val testTask = Task(name= "apiTestTask${LocalDateTime.now()}", priority = Priority.values().random())
        createTask(testTask)
        val respTaskList = mutableListOf<Task>()
        var getrs = getTasksRequest()
        val getRsArray = JsonParser.parseString(getrs.body!!.string()).getAsJsonArray()
        getRsArray.forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        val rndTestTask = respTaskList.random()
        putTaskStatusRequest(id = rndTestTask.id!!, completed = true)
        rndTestTask.completed = true
        respTaskList.clear()
        getrs = getTasksRequest(done = true)
        JsonParser.parseString(getrs.body!!.string()).getAsJsonArray().forEach {
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        Assertions.assertTrue(respTaskList.contains(rndTestTask))
    }

    @Test(retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun deleteRNDCompletedTask(){
        val testTask = Task(name= "apiTestTask${LocalDateTime.now()}", priority = Priority.values().random())
        val rsTestTask = createTask(testTask)
        putTaskStatusRequest(id = rsTestTask.id!!, completed = true)
        val respTaskList = mutableListOf<Task>()
        var getrs = getTasksRequest(done = true)
        val getRsArray = JsonParser.parseString(getrs.body!!.string()).getAsJsonArray()
        getRsArray.forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        val rndTestTask = respTaskList.random()
        val deleteRS = deleteTaskStatusRequest(id = rndTestTask.id!!)
        val deleteRSError = Gson().fromJson(deleteRS.body!!.string(), ErrorMessage::class.java)
        Assertions.assertEquals(500, deleteRS.code)
        Assertions.assertEquals("Internal Server Error", deleteRSError.error)
        Assertions.assertEquals("Deleting completed task not allowed!", deleteRSError.message)
        getrs = getTasksRequest(done = true)
        respTaskList.clear()
        JsonParser.parseString(getrs.body!!.string()).getAsJsonArray().forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        Assertions.assertTrue(respTaskList.contains(rndTestTask))
    }

    @Test(retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun deleteRNDNotCompletedTask(){
        val testTask = Task(name= "apiTestTask${LocalDateTime.now()}", priority = Priority.values().random())
        val rsTestTask = createTask(testTask)
        val respTaskList = mutableListOf<Task>()
        var getrs = getTasksRequest()
        val getRsArray = JsonParser.parseString(getrs.body!!.string()).getAsJsonArray()
        getRsArray.forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        val rndTestTask = respTaskList.random()
        val deleteRS = deleteTaskStatusRequest(id = rndTestTask.id!!)
        Assertions.assertEquals(200, deleteRS.code)
        getrs = getTasksRequest()
        respTaskList.clear()
        JsonParser.parseString(getrs.body!!.string()).getAsJsonArray().forEach{
            val itTask = Gson().fromJson(it, Task::class.java)
            respTaskList.add(itTask)
        }
        Assertions.assertTrue(!respTaskList.contains(rndTestTask))
    }
}