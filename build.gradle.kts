import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.21"
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("maven-publish")
}

val targetJavaVersion = 25
group = project.property("maven_group") !!
version = project.property("mod_version") !!

base {
    archivesName.set(project.property("archives_base_name") as String)
}

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version") !!}")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version") !!}")
    implementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version") !!}")

    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version") !!}")

    val skijaVersion = "0.93.1"

    listOf("windows", "linux", "macos-x64", "macos-arm64").forEach { os ->
        implementation("org.jetbrains.skija:skija-$os:$skijaVersion")
        include("org.jetbrains.skija:skija-$os:$skijaVersion")
    }

    testImplementation(kotlin("test"))
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version") !!)
    inputs.property("loader_version", project.property("loader_version") !!)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version") !!,
            "loader_version" to project.property("loader_version") !!,
            "kotlin_loader_version" to project.property("kotlin_loader_version") !!
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.jar {
    from("LICENSE.txt") {
        rename { "LICENSE_${project.base.archivesName.get()}.txt" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }
}
