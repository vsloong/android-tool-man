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


}