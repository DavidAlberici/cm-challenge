plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    // jackson mapper
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")

    // mockito
    testImplementation("org.mockito:mockito-core:2.1.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")

    //slf4j
    implementation("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")
}

tasks.test {
    // this removes a mockito error message about deprecation, I need to investigate further
    doFirst {
        val mockitoCoreJar = configurations.testRuntimeClasspath.get()
                .find { it.name.contains("mockito-core") }
                ?.absolutePath
                ?: throw GradleException("mockito-core JAR not found in testRuntimeClasspath")
        jvmArgs("-javaagent:$mockitoCoreJar", "-Xshare:off")
    }
    useJUnitPlatform()
}

application {
    mainClass.set("com.davidalberici.cm_challenge.Main")
    // Name used for the launch scripts installDist generates
    applicationName = "cm-challenge"
}