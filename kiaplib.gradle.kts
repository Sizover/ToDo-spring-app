// Подключение локального Maven репозитория Gitlab
class KiaplibPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.repositories {
            maven(mavenKiap())
        }
    }

    companion object {
        const val gitlabMavenUrl: String = "https://git.kiap.dev/api/v4/groups/43/-/packages/maven"

        // Локальный Maven репозиторию с аутентификацией по токену, см. getMavenToken()
        fun mavenKiap(): Action<in MavenArtifactRepository> {
            return Action {
                url = java.net.URI(gitlabMavenUrl)
                credentials(HttpHeaderCredentials::class) {
                    getMavenToken().apply {
                        name = this.first
                        value = this.second
                    }
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }

        // Возвращает токен подключения к Maven repository Gitlab из env или файла
        fun getMavenToken(): Pair<String, String> {

            // 1. Если задана env переменная `GITLAB_MAVEN_TOKEN` - считаем этот как Deploy Token.
            System.getenv("GITLAB_MAVEN_TOKEN")?.let {
                if (it.isNotBlank())
                    return Pair("Deploy-Token", it)
            }

            // 2. Если задана env переменная `CI_JOB_TOKEN` - считаем этот как Job Token.
            System.getenv("CI_JOB_TOKEN")?.let {
                if (it.isNotBlank())
                    return Pair("Job-Token", it)
            }

            // 3. Если в env подходящего нет, читаем из файла `gradle.local.properties`
            //    переменную `gitlabMavenToken` - считаем его как Private Token.
            val token: String = java.util.Properties().apply {
                load(java.io.FileInputStream(File("gradle.local.properties")))
            }.getProperty("gitlabMavenToken")

            return Pair("Private-Token", token)
        }
    }
}

apply<KiaplibPlugin>()
