plugins {
    id("refinedarchitect.common")
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/refinedmods/refinedstorage2")
        credentials {
            username = "anything"
            password = "\u0067hp_oGjcDFCn8jeTzIj4Ke9pLoEVtpnZMP4VQgaX"
        }
    }
}

refinedarchitect {
    common()
    publishing {
        maven = true
    }
}

base {
    archivesName.set("common")
}

val refinedstorageVersion: String by project
val refinedstorageQuartzArsenalVersion: String by project

dependencies {
    api("com.refinedmods.refinedstorage:refinedstorage-common:${refinedstorageVersion}")
    api("com.refinedmods.refinedstorage:refinedstorage-quartz-arsenal-common:${refinedstorageQuartzArsenalVersion}")
}
