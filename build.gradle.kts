plugins {
    java
    id("me.champeau.jmh") version "0.6.5"
}

group = "com.epicdima.findwords"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}

dependencies {
    jmh("org.openjdk.jmh:jmh-core:0.9")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:0.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}