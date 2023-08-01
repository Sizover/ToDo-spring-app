

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import org.testng.annotations.DataProvider


class PlayGround : BaseTest(){



    @DataProvider(name = "TestProvider")
    fun TestProviderFun(): Array<Array<String>> {
        return arrayOf<Array<String>>(
            arrayOf("Русский", "Английский")
        )

    }


//    @Test (retryAnalyzer = Retry::class, dataProvider = "TestProvider", groups = ["LOCAL"])
//    @Parameters("testENV")
    @org.testng.annotations.Test(retryAnalyzer = Retry::class, groups = ["LOCAL"])
    fun `Черновик`() {
        Selenide.open("https://test.kiap.local/")
        Thread.sleep(1000)
        val test1 = WebDriverRunner()
    }
}