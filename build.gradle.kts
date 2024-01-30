
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version : String by project
val hikaricp_version : String by project
val postgres_version : String by project
val dagger_version : String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("kapt") version "1.9.22"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation ("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation ("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation( "org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation ("org.postgresql:postgresql:$postgres_version")
    implementation ("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.google.dagger:dagger:$dagger_version")
    kapt("com.google.dagger:dagger-compiler:$dagger_version")
    annotationProcessor("com.google.dagger:dagger-compiler:$dagger_version")
}
