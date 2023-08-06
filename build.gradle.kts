plugins {
    java
    kotlin("jvm") version "1.9.0"
    id("me.champeau.jmh") version "0.7.1"
}

group = "com.epicdima.findwords"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
    options.compilerArgs = listOf("--enable-preview", "--add-modules", "jdk.incubator.concurrent")
}

tasks.withType(JavaExec::class.java) {
    jvmArgs = listOf("--enable-preview")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    jmh("org.openjdk.jmh:jmh-core:1.36")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.epicdima.findwords.Runner"
    }
}
