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

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<CopyLibToResources>("copyLibToResources") {
    val projectDir = layout.projectDirectory

    source = projectDir.dir("lib")
    destination = projectDir.dir("src/main/resources/lib")

    onlyIf {
        // Only copy if the destination directory does not already contain files
        val destDir = destination.get().asFile
        !destDir.exists() || destDir.listFiles()?.isEmpty() == true
    }
}

tasks.named("processResources") {
    dependsOn("copyLibToResources")
}

abstract class CopyLibToResources : DefaultTask() {
    @get:InputDirectory
    abstract val source: DirectoryProperty

    @get:OutputDirectory
    abstract val destination: DirectoryProperty

    @get:Inject
    abstract val fs: FileSystemOperations

    @TaskAction
    fun action() {
        fs.copy {
            from(source)
            into(destination)
        }
    }
}
