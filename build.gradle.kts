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
}

ploceus {
    setIntermediaryGeneration(2)
}

dependencies {
    minecraft("com.mojang:minecraft:${Versions.minecraft}")
//    mappings(ploceus.featherMappings(Versions.feather))
    mappings(ploceus.mcpMappings("stable", "22"))

    modImplementation("net.fabricmc:fabric-loader:${Versions.fabric}")
    ploceus.dependOsl(Versions.osl)
}

tasks.assemble {
    dependsOn("remapJar")
}

tasks.processResources {
    val v = project.version
    inputs.property("version", v)

    filesMatching("fabric.mod.json") {
        expand("version" to v)
    }
}