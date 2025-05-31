group = "swot"
version = "0.1"

plugins {
    alias(libs.plugins.kotlin.jvm) apply true

    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.13.2")
}

application {
    mainClass.set("swot.CompilerKt")
}

val copyLibToResources by tasks.registering(Copy::class) {
    from("lib")
    into("src/main/resources/lib")

    doFirst {
        val dest = file("src/main/resources/lib")
        if (dest.exists() && dest.listFiles()?.isNotEmpty() == true) {
            logger.lifecycle("[copyLibToResources] Destination already exists and is not empty, skipping copy.")
            enabled = false
        } else {
            logger.lifecycle("[copyLibToResources] Copying lib/ to resources/lib/...")
        }
    }
}

tasks.named("processResources") {
    dependsOn(copyLibToResources)
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
