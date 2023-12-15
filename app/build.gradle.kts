import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
}

group = "com.vsloong"
version = "1.0-SNAPSHOT"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


// 如果做单元测试需要自行处理下资源
tasks.withType<Test> {
    useJUnitPlatform()
    dependsOn("prepareAppResources") // Needed when running tests with a clean build
    systemProperty(
        "compose.application.resources.dir",
        // Uses raw string to avoid error for paths containing special characters
        "${buildDir.toPath()}.compose.tmp.prepareAppResources"
    )
}


compose.desktop {
    application {
        mainClass = "com.vsloong.toolman.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "toolman"
            packageVersion = "1.0.0"

            // 配置资源目录
            appResourcesRootDir.set(project.rootProject.file("assets"))

        }
    }
}
