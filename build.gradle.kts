plugins {
    id("org.jetbrains.intellij") version "1.12.0"
    java
    kotlin("jvm") version "1.8.0"
}

group = "com.asyncapi.plugin.idea"
version = "2.0.0+jre17"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.9.1")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.3.1")
    plugins.set(listOf("yaml"))
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    sinceBuild.set("223")
    untilBuild.set("223.*")
    changeNotes.set("""
        <p>Update to support IntelliJ IDEA 2022.3, 2022.3.*</p>
    """.trimIndent())
}

tasks.getByName<org.jetbrains.intellij.tasks.RunPluginVerifierTask>("runPluginVerifier") {
    ideVersions.set(listOf(
        "2022.3",
        "2022.3.1"
    ))
    verifierVersion.set("1.289")
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
