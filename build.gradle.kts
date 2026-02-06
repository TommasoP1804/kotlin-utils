
import io.freefair.gradle.plugins.aspectj.AspectjCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    kotlin("plugin.noarg") version "2.3.0"
    id("maven-publish")
    id("io.freefair.aspectj.post-compile-weaving") version "9.1.0" // AspectJ plugin
    id("com.vanniktech.maven.publish") version "0.30.0"
    signing
}

noArg {
    annotation("dev.tommasop1804.kutils.annotations.NoArgs")
}

group = "dev.tommasop1804"
version = "1.0.0"
// Kotlin-Utils
// Tommaso Pastorelli
// Last update: Tommaso Pastorelli | 20260206T184119Z

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.osgeo.org/repository/release/")
    }
    /*maven {
        url = uri("https://download.osgeo.org/webdav/geotools/")
    }*/

}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

    implementation("tools.jackson.core:jackson-databind:3.0.2")
    implementation("tools.jackson.core:jackson-core:3.0.2")
    implementation("tools.jackson.module:jackson-module-kotlin:2.20.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")

    implementation("io.hypersistence:hypersistence-tsid:2.1.4")
    implementation("org.locationtech.jts:jts-core:1.19.0")
    implementation("org.locationtech.jts.io:jts-io-common:1.19.0")
    implementation("org.hibernate:hibernate-spatial:6.6.0.Final")
    implementation("org.geolatte:geolatte-geom:1.9.1")
    implementation("org.locationtech.proj4j:proj4j:1.3.0")
    api("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.lalyos:jfiglet:0.0.8")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation(kotlin("scripting-jsr223"))
    implementation("org.openjdk.nashorn:nashorn-core:15.3")
    implementation("org.aspectj:aspectjrt:1.9.24")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.slf4j:jul-to-slf4j:2.0.13")
    /*implementation("org.geotools:gt-referencing:26.1") {
        exclude(group = "javax.media", module = "jai_core")
    }
    implementation("org.geotools:gt-main:26.1") {
        exclude(group = "javax.media", module = "jai_core")
    }
    implementation("org.geotools:gt-epsg-hsql:26.1") {
        exclude(group = "javax.media", module = "jai_core")
    }*/
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    implementation("com.github.jsqlparser:jsqlparser:5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.yaml:snakeyaml:2.5")
    implementation("jakarta.mail:jakarta.mail-api:2.0.1")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
}


/*configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.geotools") {
                useTarget("org.geotools:${requested.name}:26.1")
                because("GeoTools dependencies are only in OSGeo repository")
            }
        }
    }
}*/

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-Xallow-contracts-on-more-functions")
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xallow-holdsin-contract")
        freeCompilerArgs.add("-Xallow-condition-implies-returns-contracts")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType(AspectjCompile::class.java).configureEach {
    options.compilerArgs.add("-Xlint:ignore")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll(listOf(
            "-Xno-param-assertions",
            "-Xno-call-assertions"
        ))
    }
}

mavenPublishing {
    coordinates("dev.tommasop1804", "kotlin-utils", "1.0.0")

    pom {
        name.set("Kotlin Utils")
        description.set("Utility functions")
        inceptionYear.set("2024")
        url.set("https://github.com/TommasoP1804")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("tommasop1804")
                name.set("Tommaso")
                url.set("https://tommasop1804.dev")
            }
        }
        scm {
            url.set("https://github.com/tommasop1804/kotlin-utils")
            connection.set("scm:git:git://github.com/tommasop1804/kotlin-utils.git")
            developerConnection.set("scm:git:ssh://git@github.com/tommasop1804/kotlin-utils.git")
        }
    }

    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}