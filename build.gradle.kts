import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("nu.studer.jooq") version "8.0" // これを追加
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.17.4"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // testcontainers
//    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    // flyway
    runtimeOnly("org.flywaydb:flyway-mysql")

    // kotest
    val kotest_version = "5.5.4"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotest_version")

    // jooq
    jooqGenerator("com.mysql:mysql-connector-j")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    url = "jdbc:mysql://localhost:3306/libraly?enabledTLSProtocols=TLSv1.2"
                    user = "root"
                    password = "root"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = System.getenv("MYSQL_DB_NAME")
                        excludes = "flyway_schema_history"
                    }
                    generate.apply {
                        isDeprecated = false
                        isTables = true
                    }
                    target.apply {
                        packageName = "com.example.ktknowledgeTodo.infra.jooq"
                        directory = "$buildDir/generated/source/jooq/main"
                    }
                }
            }
        }
    }
}
