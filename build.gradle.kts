plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.benchmark)
}

group = "com.epicdima.findwords"
version = "1.0"

repositories {
    google()
    mavenCentral()
}

sourceSets {
    create("benchmark") {
        dependencies {
            implementation(libs.kotlinx.benchmark)
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines)

    implementation(libs.androidx.annotation)

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    add("benchmarkImplementation", sourceSets.main.get().output + sourceSets.main.get().runtimeClasspath)
}

benchmark {
    targets {
        register("benchmark")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.epicdima.findwords.Runner"
    }
}
