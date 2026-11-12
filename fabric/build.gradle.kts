plugins {
    id("com.refinedmods.refinedarchitect.fabric")
}

repositories {
    maven {
        name = "JEI"
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }
    maven {
        name = "Refined Storage"
        url = uri("https://maven.creeperhost.net")
        content {
            includeGroup("com.refinedmods.refinedstorage")
        }
    }
    maven {
        name = "ModMenu"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        name = "Cloth Config"
        url = uri("https://maven.shedaniel.me/")
    }
}

val modVersion: String by project

refinedarchitect {
    modId = "universalgrid"
    version = modVersion
    fabric()
    publishing {
        maven = true
    }
}

base {
    archivesName.set("universalgrid-fabric")
}

val minecraftVersion: String by project
val refinedstorageVersion: String by project
val refinedstorageQuartzArsenalVersion: String by project
val jeiVersion: String by project

val commonJava by configurations.existing
val commonResources by configurations.existing

dependencies {
    compileOnly(project(":common"))
    commonJava(project(path = ":common", configuration = "commonJava"))
    commonResources(project(path = ":common", configuration = "commonResources"))

    modApi("com.refinedmods.refinedstorage:refinedstorage-fabric:${refinedstorageVersion}")
    modApi("com.refinedmods.refinedstorage:refinedstorage-quartz-arsenal-fabric:${refinedstorageQuartzArsenalVersion}")
    modApi("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
}
