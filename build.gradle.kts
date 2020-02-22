import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.61"
    id("net.minecrell.licenser") version "0.4.1"
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "eu.mikroskeem"
version = "0.0.1-SNAPSHOT"

val waterfallApiVersion = "1.15-SNAPSHOT"
val geoIpApiVersion = "0.0.1-SNAPSHOT"
val slf4jApiVersion = "1.7.26"
val configurateVersion = "3.7-SNAPSHOT"
val hikariVersion = "3.4.2"
val ormliteVersion = "5.1"
val bcryptVersion = "0.9.0"
val luckpermsApiVersion = "5.0"
val bstatsVersion = "1.4"
val okHttpVersion = "4.3.1"
val expiringMapVersion = "0.5.9"

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
    compileOnly("net.luckperms:api:$luckpermsApiVersion")
    compileOnly("eu.mikroskeem.geoipapi:api:$geoIpApiVersion")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.spongepowered:configurate-hocon:$configurateVersion") {
        exclude(module = "guava")
    }
    implementation("com.zaxxer:HikariCP:$hikariVersion") {
        exclude(module = "slf4j-api")
    }
    implementation("com.j256.ormlite:ormlite-jdbc:$ormliteVersion")
    implementation("at.favre.lib:bcrypt:$bcryptVersion")
    implementation("org.bstats:bstats-bungeecord-lite:$bstatsVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("net.jodah:expiringmap:$expiringMapVersion")
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
    depends = setOf("GeoIPAPI")
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
            "org.objenesis",
            "org.bstats",
            "okio",
            "okhttp3",
            "net.jodah.expiringmap"
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

        // Exclude unneeded list
        exclude("mozilla/public-suffix-list.txt")
    }
}

tasks["build"].dependsOn(tasks["shadowJar"])
defaultTasks("licenseFormat", "build")
