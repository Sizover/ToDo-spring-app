package otus_api_tests

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

open class BaseHttpClient {
    val client = OkHttpClient()
    val url = "https://api.agify.io/"

    fun testGetRequest(queryParameter: Name): Response {
        val urlBuilder: HttpUrl.Builder = (url).toHttpUrlOrNull()!!.newBuilder()
        if (queryParameter.name?.trim()?.isNotEmpty() == true){
            urlBuilder.addQueryParameter("name", queryParameter.name)
        }
        if (queryParameter.country_id?.isNotEmpty() == true){
            urlBuilder.addQueryParameter("country_id", queryParameter.country_id)
        }
        val _url: String = urlBuilder.build().toString()
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url(_url)
            .build()
        return client.newCall(request).execute()
    }
}