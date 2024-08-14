plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
//    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24" //Entity의 기본생성자를 자동으로 만들어줌
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

    //querydsl
    //참고 URL : https://velog.io/@yangwon-park/Kotlin-Querydsl-%EC%84%B8%ED%8C%85
//    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
//    implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
//    implementation("jakarta.persistence:jakarta.persistence-api")
//    implementation("jakarta.annotation:jakarta.annotation-api")
//    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
//    kapt("org.springframework.boot:spring-boot-configuration-processor")

    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    //etc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0") //swagger
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5") //암호화
    implementation("org.springframework.boot:spring-boot-starter-mail") //mail 전송
    implementation("org.springframework.boot:spring-boot-starter-data-redis") //redis
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

/**
 * Query DSL 설정 참고
 * URL : https://velog.io/@hana0627/Kotlin-SpringBoot3-QueryDsl-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
 */

// Querydsl 설정부 추가 - start
//val generated = file("src/main/generated")
//
//// querydsl QClass 파일 생성 위치를 지정
//tasks.withType<JavaCompile> {
//    options.generatedSourceOutputDirectory.set(generated)
//}
//
//// kotlin source set 에 querydsl QClass 위치 추가
//sourceSets {
//    main {
//        kotlin.srcDirs += generated
//    }
//}
//
//// gradle clean 시에 QClass 디렉토리 삭제
//tasks.named("clean") {
//    doLast {
//        generated.deleteRecursively()
//    }
//}
//
//
//kapt {
//    generateStubs = true
//}
//
//// Querydsl 설정부 추가 - end
//
//kotlin {
//    compilerOptions {
//        freeCompilerArgs.addAll("-Xjsr305=strict")
//    }
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
