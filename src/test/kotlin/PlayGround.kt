

import org.testng.ITestContext
import org.testng.annotations.BeforeTest
import org.testng.annotations.DataProvider
import java.lang.reflect.Method


class PlayGround : BaseTest(){



    @DataProvider(name = "TestProvider")
    fun TestProviderFun(): Array<Array<String>> {
        return arrayOf<Array<String>>(
            arrayOf("Русский", "Английский")
        )

    }


//    @Test (retryAnalyzer = Retry::class, dataProvider = "TestProvider", groups = ["LOCAL"])
//    @Parameters("testENV")
    //@org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL", "LOCAL"])
    fun `не_то`(tt : Any): String = tt.javaClass.enclosingMethod.name

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Черновик2`() {
        val test = object{}
        val name = object{}.javaClass.enclosingMethod.name
//        val test3: ITestContext = ITestContext
//        println("The current function is ${startTest(test3)}")

    }

    fun `Черновик3`() {
        val name = object{}.javaClass.enclosingMethod.name

        println("The current function is $name")
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ALL", "LOAL"])
    fun startTest(testContext: ITestContext){
        println(testContext.name)
    }

    fun startTest2(testContext: ITestContext) {
        println(testContext.name)
        return
    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ALL", "LOCAL"])
//    fun testCheck(method: Method) {
//        println(method.name)
//    }


//    fun startTest(testContext: ITestContext) {
//        println(testContext.getName()) // it prints "Check name test"
//    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ALL", "LOCAL"])
//    fun testCheck2(method: Method) {
//        val method: Method
//        println(method.name)
//    }
}