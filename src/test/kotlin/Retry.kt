import org.testng.IRetryAnalyzer
import org.testng.ITestResult

public class Retry : IRetryAnalyzer {
    private var actualRetry: Int = 0
    private val maxRetry:Int = 0

    override fun retry(result: ITestResult?): Boolean {
        return if (actualRetry < maxRetry){
            actualRetry += 1
            true
        } else false
    }
}
