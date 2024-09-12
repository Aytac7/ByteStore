
plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.example"
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
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-validation")


    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    runtimeOnly("org.postgresql:postgresql")
    implementation ("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.h2database:h2")

    implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.liquibase:liquibase-core:4.24.0")

    implementation("org.springframework.boot:spring-boot-starter-security")


    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-core")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("com.amazonaws:aws-java-sdk:1.12.669")
//    implementation("com.amazonaws:aws-java-sdk-s3:1.12.767")


   // implementation("io.swagger:swagger-annotations:1.6.4")
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")


//    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")




}

tasks.withType<Test> {
    useJUnitPlatform()
}