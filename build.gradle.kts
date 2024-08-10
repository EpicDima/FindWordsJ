plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.benchmark)
    alias(libs.plugins.kotlin.all.open)
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

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)

    add("benchmarkImplementation", sourceSets.main.get().output + sourceSets.main.get().runtimeClasspath)
}

benchmark {
    targets {
        register("benchmark")
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.epicdima.findwords.Runner"
    }
}
