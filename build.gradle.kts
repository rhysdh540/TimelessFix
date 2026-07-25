plugins {
    java
    id("net.fabricmc.fabric-loom-remap") version("1.17.+")
    id("ploceus") version("1.17.+")
}

group = "dev.rdh"
version = "0.1"

java.toolchain {
    languageVersion = JavaLanguageVersion.of(25)
}

@Suppress("MayBeConstant")
object Versions {
    val minecraft = "1.8.9"
    val feather = "1"
    val osl = "0.20.3"
    val fabric = "0.19.3"
    val celeritas = "2.4.0-dev.5"
}

ploceus {
    setIntermediaryGeneration(2)
}

val celery = sourceSets.create("celery") {
    compileClasspath += sourceSets.main.map { it.output + it.compileClasspath }.get()
}

repositories {
    maven("https://maven.taumc.org/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.minecraft}")
//    mappings(ploceus.featherMappings(Versions.feather))
    mappings(ploceus.mcpMappings("stable", "22"))

    modImplementation("net.fabricmc:fabric-loader:${Versions.fabric}")
    ploceus.dependOsl(Versions.osl)
    add(celery.compileOnlyConfigurationName, "org.embeddedt.celeritas:celeritas-common:${Versions.celeritas}")
}

tasks.assemble {
    dependsOn("remapJar")
}

tasks.jar {
    from(celery.output)
}

tasks.processResources {
    val v = project.version
    inputs.property("version", v)

    filesMatching("fabric.mod.json") {
        expand("version" to v)
    }
}
