plugins {
    id("java")
}

group = "cn.wenhe"
version = "1.0-SNAPSHOT"
description = "增值税发票验真测试工程"

java {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
    }
    toolchain { languageVersion.set(JavaLanguageVersion.of(8)) }
}

buildscript {
    extra["springBootVersion"] = "2.3.12.RELEASE"
    extra["lombokVersion"] = "1.18.24"

    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://mvnrepository.com")
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${property("springBootVersion")}")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://mvnrepository.com")
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.projectlombok:lombok:${property("lombokVersion")}")
        compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
        annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")
        implementation("com.alibaba:easyexcel:3.3.2")
        implementation("com.alibaba:fastjson:2.0.42")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.tencentcloudapi:tencentcloud-sdk-java-ocr:3.1.952")
        implementation("org.apache.httpcomponents:httpcore:4.4.13")
        implementation("org.apache.httpcomponents:httpclient:4.5.14") {
            exclude(group = "commons-codec", module = "commons-codec")
            exclude(group = "org.apache.httpcomponents", module = "httpcore")
        }
        implementation("org.openeuler:bgmprovider:1.0.4") {
            exclude(group = "org.bouncycastle", module = "bcprov-jdk15on")
        }

        implementation("org.bouncycastle:bcprov-jdk15on:1.70")
        implementation("com.squareup.okhttp3:okhttp:4.11.0") {
            exclude(group = "com.squareup.okio", module = "okio")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}


