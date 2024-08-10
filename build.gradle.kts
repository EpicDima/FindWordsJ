plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jmh)
}

group = "com.epicdima.findwords"
version = "1.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines)

    jmh(libs.jmh.core)
    jmh(libs.jmh.processor)
    jmhAnnotationProcessor(libs.jmh.processor)

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.epicdima.findwords.Runner"
    }
}
