plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jetbrains.kotlin.kapt") version "1.9.24"
    kotlin("jvm") version "1.9.24"
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
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    //thymeleaf
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
    testImplementation("com.squareup.okhttp3:okhttp:4.9.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")

    //oauth2
    implementation ("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation ("org.springframework.security:spring-security-oauth2-jose")

    //etc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0") //swagger
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5") //암호화
    implementation("org.springframework.boot:spring-boot-starter-mail") //mail 전송
    implementation("org.springframework.boot:spring-boot-starter-data-redis") //redis
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //vault
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.1.4")
    implementation("org.springframework.cloud:spring-cloud-config-server:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config:4.1.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
