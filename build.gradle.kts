plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.1"
}
dependencies{

    //导入kotlinx-datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    //导入okhttputils
    implementation("com.github.kittinunf.result:result:5.2.1")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okio:okio:3.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("com.google.code.gson:gson:2.9.0")

    //导入jsoup
    implementation("org.jsoup:jsoup:1.14.3")

    //导入io.ktor.http
    implementation("io.ktor:ktor-client-core:1.6.8")
    implementation("io.ktor:ktor-client-okhttp:1.6.8")
    implementation("io.ktor:ktor-client-json:1.6.8")
    implementation("io.ktor:ktor-client-features:1.6.8")
    implementation("io.ktor:ktor-client-logging:1.6.8")
    implementation("io.ktor:ktor-client-json-jvm:1.6.8")

    //导入org.json
    implementation("org.json:json:20220320")

    //导入Moshi
    implementation("com.squareup.moshi:moshi:1.13.0")
    //导入Moshi-Kotlin
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")

    //导入netty
    implementation("io.netty:netty-all:4.1.76.Final")

    //导入分词
    //implementation("com.hankcs:HanLP:4.0.0")

    //导入libs目录下的所有jar
    files("libs")

}


group = "com.greenhandzdl"
version = "0.0.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
