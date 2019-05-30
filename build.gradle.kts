import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.31"
    id("net.minecrell.licenser") version "0.4.1"
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val waterfallApiVersion = "1.14-SNAPSHOT"
val slf4jApiVersion = "1.7.25"
val configurateVersion = "3.7-SNAPSHOT"
val hikariVersion = "3.3.0"
val ormliteVersion = "5.1"
val bcryptVersion = "0.6.0"
val geoipVersion = "2.12.0"
val commonsCompressVersion = "1.18"
val luckpermsApiVersion = "4.3"
val bstatsVersion = "1.4"
val okHttpVersion = "3.14.2"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:$waterfallApiVersion")
    compileOnly(rootProject.files("lib/FastLogin.jar"))
    compileOnly("me.lucko.luckperms:luckperms-api:$luckpermsApiVersion")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.spongepowered:configurate-hocon:$configurateVersion") {
        exclude(module = "guava")
    }
    implementation("com.zaxxer:HikariCP:$hikariVersion") {
        exclude(module = "slf4j-api")
    }
    implementation("com.j256.ormlite:ormlite-jdbc:$ormliteVersion")
    implementation("at.favre.lib:bcrypt:$bcryptVersion")
    implementation("com.maxmind.geoip2:geoip2:$geoipVersion") {
        exclude(module = "httpcore")
        exclude(module = "httpclient")
    }
    implementation("org.apache.commons:commons-compress:$commonsCompressVersion")
    implementation("org.bstats:bstats-bungeecord-lite:$bstatsVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
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
            "at.favre.lib",
            "com.maxmind.db",
            "com.maxmind.geoip2",
            "org.apache.commons.compress",
            "com.fasterxml.jackson",
            "org.objenesis",
            "org.bstats",
            "okio",
            "okhttp3"
    )
    val targetPackage = "eu.mikroskeem.benjiauth.lib"

    relocations.forEach {
        relocate(it, "$targetPackage.$it")
    }

    dependencies {
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/lang/annotations/**")
        exclude("org/checkerframework/**")
        exclude("META-INF/maven/**")

        // TODO: very likely unsafe exclusions
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

tasks["build"].dependsOn(tasks["shadowJar"])
defaultTasks("licenseFormat", "build")
