import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    id("jacoco")
    id("com.google.cloud.tools.jib") version "3.3.0"
}

group = "zip"
version = "0.0.15-SNAPSHOT"
val qeurydslVersion = "5.0.0"
val kotestVersion = "5.5.3"

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

val asciidoctorExt: Configuration by configurations.creating

tasks.jar {
    enabled = false
}

tasks.build {
    finalizedBy("copyDocument")
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    finalizedBy("copyDocument")
}

val snippetsDir by extra { file("build/generated-snippets") }

kotlin.sourceSets.main {
    setBuildDir("$buildDir")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.2")

    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    implementation("com.querydsl:querydsl-jpa:$qeurydslVersion")
    kapt("com.querydsl:querydsl-apt:$qeurydslVersion:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
        exclude(group = "org.assertj")
        exclude(group = "org.hamcrest")
    }

    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.10.1")

    implementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    implementation("io.github.serpro69:kotlin-faker:1.12.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        tasks.test,
        tasks.jacocoTestReport,
        tasks.jacocoTestCoverageVerification
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"], tasks["copyDocument"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}

tasks.test {
    outputs.dir(snippetsDir)
    systemProperty("org.springframework.restdocs.outputDir", snippetsDir)
    finalizedBy(testCoverage)

    systemProperty("kotest.framework.classpath.scanning.config.disable", true)

    doLast {
        println("View code coverage at:")
        println("file://$buildDir/reports/coverage/index.html")
    }
}

tasks.jacocoTestReport {
    val QDomains = ('A'..'Z')
        .toMutableList()
        .map { "**/Q$it*" }

    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/coverage"))
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(QDomains)
                }
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    val QDomains = ('A'..'Z')
        .toMutableList()
        .map { "*Q$it*" }

    violationRules {
        rule {
            // 커버리지 체크를 제외할 클래스들
            excludes = listOf(
                "*.Kotlin*"
            ) + QDomains
        }
    }
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    configurations("asciidoctorExt")
    dependsOn(tasks.test)

    baseDirFollowsSourceDir()

    doFirst {
        delete("src/main/resources/static/docs")
    }
}

tasks.register<Copy>("copyDocument") {
    dependsOn(tasks.asciidoctor)

    destinationDir = file(".")
    from(tasks.asciidoctor.get().outputDir) {
        into("src/main/resources/static/docs")
    }
}

tasks.named("jib") {
    dependsOn(tasks.asciidoctor)
}

jib {
    from {
        image = "gcr.io/distroless/java17-debian11"
        platforms {
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "786604280410.dkr.ecr.ap-northeast-2.amazonaws.com/cafe"
        credHelper.helper = "ecr-login"
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
    }

    extraDirectories {
        paths {
            path {
                setFrom(tasks.asciidoctor.get().outputDir.toString())
                into = "/app/resources/static/docs"
            }
        }
    }
}
