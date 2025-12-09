plugins {
    id("com.refinedmods.refinedarchitect.common")
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
    version = modVersion
    common()
    publishing {
        maven = true
    }
}

base {
    archivesName.set("universalgrid-common")
}

val refinedstorageVersion: String by project
val refinedstorageQuartzArsenalVersion: String by project

dependencies {
    api("com.refinedmods.refinedstorage:refinedstorage-common:${refinedstorageVersion}")
    api("com.refinedmods.refinedstorage:refinedstorage-quartz-arsenal-common:${refinedstorageQuartzArsenalVersion}")
    compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
}
