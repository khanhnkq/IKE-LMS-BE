plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.ike"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":platform"))
    implementation(project(":finance"))
    implementation(project(":academic"))
    implementation(project(":notification"))
    implementation(project(":audit"))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.modulith.starter.core)
    developmentOnly(libs.spring.boot.devtools)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.modulith.docs)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
