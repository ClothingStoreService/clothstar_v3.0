plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jetbrains.kotlin.kapt") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24" //Entity의 기본생성자를 자동으로 만들어줌

    jacoco
}

group = "org.store"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //basic
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")

    //web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.4")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    //DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("p6spy:p6spy:3.9.1")
    implementation("com.github.gavlyukovskiy:datasource-decorator-spring-boot-autoconfigure:1.9.0")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    //Querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    //test
    val mockkVersion = "1.13.12"
    val kotestVersion = "5.8.0"
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    //etc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0") //swagger
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5") //암호화
    implementation("org.springframework.boot:spring-boot-starter-mail") //mail 전송
    implementation("org.springframework.boot:spring-boot-starter-data-redis") //redis
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jacoco {
    // JaCoCo 버전
    toolVersion = "0.8.8"

//  테스트결과 리포트를 저장할 경로 변경
//  default는 "${project.reporting.baseDir}/jacoco"
//  reportsDir = file("$buildDir/customJacocoReportDir")
}

// kotlin DSL

tasks.jacocoTestReport {
    reports {
        // 원하는 리포트를 켜고 끌 수 있습니다.
//        html.isEnabled = true
//        xml.isEnabled = false
//        csv.isEnabled = false

        //.isEnabled 가 Deprecated 되었습니다 (저는 gradle 7.2 버전에 kotlin DSL 사용하고 있습니다)
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)

//  각 리포트 타입 마다 리포트 저장 경로를 설정할 수 있습니다.
//  html.destination = file("$buildDir/jacocoHtml")
//  xml.destination = file("$buildDir/jacoco.xml")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            // 룰을 간단히 켜고 끌 수 있다.
            enabled = true

            // 룰을 체크할 단위는 클래스 단위
            element = "CLASS"

            // 메서드 커버리지를 최소한 00% 만족시켜야 한다.
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }

            // 라인 커버리지를 최소한 00% 만족시켜야 한다.
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    finalizedBy("jacocoTestCoverageVerification")
}
