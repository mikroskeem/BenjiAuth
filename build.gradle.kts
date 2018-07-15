import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.2.51"
    id("net.minecrell.licenser") version "0.3"
    id("net.minecrell.plugin-yml.bungee") version "0.2.1"
    id("com.github.johnrengelman.shadow") version "2.0.4"
    id("org.zeroturnaround.gradle.jrebel") version "1.1.8"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val waterfallApiVersion = "1.12-SNAPSHOT"
val slf4jApiVersion = "1.7.25"
val configurateVersion = "3.3"
val hikariVersion = "3.2.0"
val ormliteVersion = "5.1"
val bcryptVersion = "0.4"
val geoipVersion = "2.12.0"
val commonsCompressVersion = "1.17"
val luckpermsApiVersion = "4.2"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("http://repo.bstats.org/content/repositories/releases/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:$waterfallApiVersion")
    compileOnly(rootProject.files("lib/FastLogin.jar"))
    compileOnly("me.lucko.luckperms:luckperms-api:$luckpermsApiVersion")

    implementation(kotlin("stdlib-jdk8"))
    implementation("ninja.leaping.configurate:configurate-hocon:$configurateVersion") {
        exclude(module = "guava")
    }
    implementation("com.zaxxer:HikariCP:$hikariVersion") {
        exclude(module = "slf4j-api")
    }
    implementation("com.j256.ormlite:ormlite-jdbc:$ormliteVersion")
    implementation("org.mindrot:jbcrypt:$bcryptVersion")
    implementation("com.maxmind.geoip2:geoip2:$geoipVersion") {
        exclude(module = "httpcore")
    }
    implementation("org.apache.commons:commons-compress:$commonsCompressVersion")
}

license {
    header = rootProject.file("etc/HEADER")
    filter.include("**/*.java")
    filter.include("**/*.kt")
}

bungee {
    name = "BenjiAuth"
    main = "eu.mikroskeem.benjiauth.BenjiAuth"
    description = "Finally, a decent authentication plugin for BungeeCord"
    author = "${listOf("mikroskeem")}"
    softDepends = setOf("LuckPerms", "FastLogin")
}

val shadowJar by tasks.getting(ShadowJar::class) {
    val relocations = listOf(
            "kotlin",
            "com.typesafe.config",
            "ninja.leaping.configurate",
            "com.zaxxer.hikari",
            "com.j256.ormlite",
            "org.mindrot.jbcrypt",
            "com.maxmind.db",
            "com.maxmind.geoip2",
            "org.apache.commons.compress",
            "org.apache.commons.codec",
            "org.apache.commons.logging",
            "com.fasterxml.jackson",
            "org.objenesis"
    )
    val targetPackage = "eu.mikroskeem.benjiauth.lib"

    if(rootProject.findProperty("useJRebel") != "true") {
        relocations.forEach {
            relocate(it, "$targetPackage.$it")
        }
    }

    dependencies {
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/lang/annotations/**")
        exclude("META-INF/maven/**")

        // TODO: very likely unsafe exclusions
        exclude("org/apache/http/**") // Since GeoIP's web client isn't used, this can be excluded
        exclude("org/apache/commons/logging/**") // I guess logging is used when web client is used

        exclude("org/apache/commons/codec/digest/**") // Nothing uses digest there
        exclude("org/apache/commons/codec/language/**") // Nothing uses language text files

        //exclude("org/apache/commons/compress/archivers/zip/**") // Note: Apparently this is needed for tar.gz unpacking
        exclude("org/apache/commons/compress/compressors/**") // No compressing needed
        // Exclude unneeded archivers
        exclude("org/apache/commons/compress/archivers/jar/**")
        exclude("org/apache/commons/compress/archivers/sevenz/**")
        exclude("org/apache/commons/compress/archivers/dump/**")
        exclude("org/apache/commons/compress/archivers/cpio/**")
        exclude("org/apache/commons/compress/archivers/ar/**")
        exclude("org/apache/commons/compress/archivers/arj/**")

        // Exclude unneeded list
        exclude("mozilla/public-suffix-list.txt")
    }
}

if(rootProject.findProperty("useJRebel") == "true") {
    tasks.getByName("jar").dependsOn(tasks.getByName("generateRebel"))
}
tasks.getByName("jar").dependsOn(tasks.getByName("shadowJar"))
defaultTasks("licenseFormat", "build")