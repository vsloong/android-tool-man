plugins {
    kotlin("jvm")
}

dependencies {

    // 解压缩
    implementation("org.apache.commons:commons-compress:1.21")

    // Json解析
    implementation("com.google.code.gson:gson:2.10.1")

    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    // jackson
    api("com.fasterxml.jackson.core:jackson-core:2.16.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.16.0")
}