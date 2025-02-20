plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.alert.open"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springShellVersion"] = "3.4.0"

object Versions {
	const val mysqlConnector = "8.0.33"      // Phiên bản MySQL Connector
	const val hikariCP = "5.0.1"
	const val kafkaVersion = "3.6.1"
}


dependencies {
	// Lombok để sử dụng annotation @Slf4j
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// Chỉ định phiên bản cụ thể của PostgreSQL driver
	implementation("org.postgresql:postgresql:42.6.0")

	// Chỉ định phiên bản cụ thể của HikariCP
	implementation("com.zaxxer:HikariCP:5.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.shell:spring-shell-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// https://mvnrepository.com/artifact/org.redisson/redisson
	implementation("org.redisson:redisson:3.44.0")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Resilience4j dependencies
	implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
	implementation("io.github.resilience4j:resilience4j-all:2.2.0") // Optional: includes all modules

	// Monitoring
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.springframework.boot:spring-boot-starter-aop")

	implementation("io.micrometer:micrometer-core")

	// Micrometer Registry for Prometheus - enables exporting metrics to Prometheus
	implementation("io.micrometer:micrometer-registry-prometheus")

	// Redis support - if you're using Redis monitoring
//	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// Optional: Additional Micrometer features
	implementation("io.micrometer:micrometer-observation")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.2")

	compileOnly("org.projectlombok:lombok:1.18.36")

	implementation("com.google.guava:guava:33.4.0-jre")

	// Kafka Dependencies
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-clients:${Versions.kafkaVersion}")
	testImplementation("org.springframework.kafka:spring-kafka-test")

	// https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
