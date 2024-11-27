import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask

plugins {
    id("org.jetbrains.intellij.platform") version "2.1.0"
    java
    kotlin("jvm") version "2.1.0"
}

group = "com.asyncapi.plugin.idea"
version = "2.6.0+jre17"

repositories {
    mavenCentral()
    intellijPlatform {
        jetbrainsRuntime()
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        /*
            Our developers believe that you likely built the plugin against version 2024.2, which includes
            a companion object in this class. As a result, the generated bytecode references it. To ensure compatibility,
            the plugin should be built against the lowest supported version, which in this case is 2022.3.

            Please adjust the IntelliJ version to 2022.3 in the Gradle build script and try building the plugin again.
         */
        intellijIdeaCommunity("2022.3", useInstaller = false) // MUST NOT be changed

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(listOf(
            "org.jetbrains.plugins.yaml"
        ))

        pluginVerifier()
        jetbrainsRuntime()
        instrumentationTools()
        testFramework(TestFrameworkType.Platform)
    }

    implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.11.3")
}

// See https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    instrumentCode = true

    pluginConfiguration {
        description = providers.fileContents(
            layout.projectDirectory.file("src/main/resources/META-INF/description.html")
        ).asText

        changeNotes = """
            <h3>Added</h3>
            <ul>
                <li>IDEA 2024.2</li>
                <li>Yaml single quoted references handling - '#/components/messages/welcomeMessage', '../common/messages/welcomeMessage.yml'</li>
                <li><code>.yml</code> file recognition</li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        failureLevel = listOf(
            VerifyPluginTask.FailureLevel.INVALID_PLUGIN,
            VerifyPluginTask.FailureLevel.COMPATIBILITY_PROBLEMS,
            VerifyPluginTask.FailureLevel.NOT_DYNAMIC
        )

        ides {
            ides(listOf(
                "2022.3",
                "2022.3.1",
                "2022.3.2",
                "2022.3.3",
                "2023.1",
                "2023.1.1",
                "2023.1.2",
                "2023.1.3",
                "2023.1.4",
                "2023.1.5",
                "2023.2",
                "2023.2.1",
                "2023.2.2",
                "2023.2.3",
                "2023.2.4",
                "2023.2.5",
                "2023.3",
                "2023.3.1",
                "2023.3.2",
                "2023.3.3",
                "2023.3.4",
                "2023.3.5",
                "2023.3.6",
                "2023.3.7",
                "2024.1",
                "2024.1.1",
                "2024.1.2",
                "2024.1.3",
                "2024.1.4",
                "2024.1.5",
                "2024.1.6",
                "2024.2",
                "2024.2.0.1",
                "2024.2.0.2",
                "2024.2.1"
            ))
        }
    }
}

tasks {
    patchPluginXml {
        sinceBuild = "223"
        untilBuild = "242.*"
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(17))
    }
}
