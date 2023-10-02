package otus_api_tests

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

open class BaseHttpClient {
    val client = OkHttpClient()
    val url = "http://localhost:8080"


//    fun testGetRequest(queryParameter: Task): Response {
//        val urlBuilder: HttpUrl.Builder = (url).toHttpUrlOrNull()!!.newBuilder()
//        if (queryParameter.name?.trim()?.isNotEmpty() == true){
//            urlBuilder.addQueryParameter("name", queryParameter.name)
//        }
//        if (queryParameter.country_id?.isNotEmpty() == true){
//            urlBuilder.addQueryParameter("country_id", queryParameter.country_id)
//        }
//        val _url: String = urlBuilder.build().toString()
//        val request = Request.Builder()
//            .addHeader("Content-Type", "application/json")
//            .url(_url)
//            .build()
//        val test = client.newCall(request)
//        return client.newCall(request).execute()
//    }

    fun getTasksRequest(url: String = "http://localhost:8080/api/v1/tasks", done: Boolean = false): Response{
        val urlBuilder: HttpUrl.Builder = (url).toHttpUrlOrNull()!!.newBuilder()
            urlBuilder.addQueryParameter("done", done.toString())
        val _url: String = urlBuilder.build().toString()
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(_url)
            .build()
        return client.newCall(request).execute()
    }

    fun postTaskRequest(url: String = "http://localhost:8080/api/v1/tasks", body: Any): Response{
        val requestBody = Gson().toJson(body).toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()
        return client.newCall(request).execute()
    }

    fun putTaskStatusRequest(url: String = "http://localhost:8080/api/v1/tasks/status", id: Int, completed: Boolean): Response{
        val urlBuilder: HttpUrl.Builder = ("$url/$id").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("done", completed.toString())
        val requestBody = Gson().toJson(null).toRequestBody("application/json".toMediaType())
        val _url: String = urlBuilder.build().toString()
        val request = Request.Builder()
            .put(requestBody)
            .addHeader("Content-Type", "application/json")
            .url(_url)
            .build()
        return client.newCall(request).execute()
    }

    fun deleteTaskStatusRequest(url: String = "http://localhost:8080/api/v1/tasks", id: Int): Response{
        val urlBuilder: HttpUrl.Builder = ("$url/$id").toHttpUrlOrNull()!!.newBuilder()
//        urlBuilder.addQueryParameter("done", completed.toString())
//        val requestBody = Gson().toJson(null).toRequestBody("application/json".toMediaType())
        val _url: String = urlBuilder.build().toString()
        val request = Request.Builder()
            .delete()
            .addHeader("Content-Type", "application/json")
            .url(_url)
            .build()
        return client.newCall(request).execute()
    }
}