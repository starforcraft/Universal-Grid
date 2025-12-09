plugins {
    id("com.refinedmods.refinedarchitect.neoforge")
}

repositories {
    maven {
        name = "Refined Storage"
        url = uri("https://maven.creeperhost.net")
        content {
            includeGroup("com.refinedmods.refinedstorage")
        }
    }
}

val modVersion: String by project

refinedarchitect {
    modId = "universalgrid"
    version = modVersion
    neoForge()
    publishing {
        maven = true
    }
}

base {
    archivesName.set("universalgrid-neoforge")
}

val refinedstorageVersion: String by project
val refinedstorageQuartzArsenalVersion: String by project

val commonJava by configurations.existing
val commonResources by configurations.existing

dependencies {
    compileOnly(project(":common"))
    commonJava(project(path = ":common", configuration = "commonJava"))
    commonResources(project(path = ":common", configuration = "commonResources"))
    api("com.refinedmods.refinedstorage:refinedstorage-neoforge:${refinedstorageVersion}")
    api("com.refinedmods.refinedstorage:refinedstorage-quartz-arsenal-neoforge:${refinedstorageQuartzArsenalVersion}")
}
