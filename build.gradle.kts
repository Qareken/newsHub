plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.testcontainers:postgresql:1.15.1")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	// JaCoCo
	testImplementation("org.jacoco:org.jacoco.core:0.8.5")
}




tasks.test {
	// Настройка сбора данных о покрытии кода тестами
	finalizedBy("jacocoTestReport") // генерация отчета после выполнения тестов
}
tasks.register<JacocoReport>("jacocoTestReport") {
	// Подключение результатов тестирования к отчету
	dependsOn("test")
	sourceDirectories.setFrom(files(sourceSets.main.get().allSource.srcDirs))
	classDirectories.setFrom(files(sourceSets.main.get().output))
	executionData.setFrom(files("${buildDir}/jacoco/test.exec"))
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}