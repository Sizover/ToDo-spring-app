package otus_api_tests

//import io.restassured.module.kotlin.extensions.*
import com.google.gson.Gson
import io.restassured.RestAssured.*
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.IOException

class TestClass: BaseHttpClient() {
    @DataProvider(name = "Имена")
    open fun differentNames(): Any {
        return arrayOf<Array<Any>>(
            arrayOf(Name(name = "ivan"), 200),
            arrayOf(Name(name = "igor", country_id = "US"), 200),
            arrayOf(Name(name = ""), 422),
            arrayOf(Name(), 422),
        )
    }

    @Test(dataProvider = "Имена")
    fun testOfName(name: Name, respCode: Int){
        val client = BaseHttpClient()
        val resp = client.testGetRequest(name)
        val respJson = Gson().fromJson(resp.body!!.string(), Name::class.java)
        Assertions.assertEquals(respCode, resp.code)
        if (respCode == 200){
            Assertions.assertEquals(name.name?.lowercase(), respJson.name)
            Assertions.assertEquals(name.country_id, respJson.country_id)
            Assertions.assertTrue(respJson.country_id == null)
            Assertions.assertTrue(respJson.age?.isNotEmpty() ?: false)
            Assertions.assertTrue(respJson.count?.isNotEmpty() ?: false)
        }
    }

    @Test
    fun testOfBadRealisationEmptyString() {
        val urlBuilder: HttpUrl.Builder = (url).toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("name", "")
        val url: String = urlBuilder.build().toString()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        val response = call.execute()
        Assertions.assertEquals(response.code, 200)
    }

    @Test
    fun testOfBadRealisationEmptyString2() {
        val urlBuilder: HttpUrl.Builder = (url).toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter("name", null)
        val url: String = urlBuilder.build().toString()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        val response = call.execute()
        Assertions.assertEquals(response.code, 200)
    }
}