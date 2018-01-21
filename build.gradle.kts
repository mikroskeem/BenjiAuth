plugins {
    kotlin("jvm") version "1.2.10"
    id("net.minecrell.licenser") version "0.3"
    id("net.minecrell.plugin-yml.bungee") version "0.2.1"
    id("com.github.johnrengelman.shadow") version "2.0.2"
    id("org.zeroturnaround.gradle.jrebel") version "1.1.8"
}

val gradleWrapperVersion: String by extra
val kotlinVersion: String by extra
val waterfallApiVersion: String by extra
val configurateVersion: String by extra
val hikariVersion: String by extra
val ormliteVersion: String by extra
val bcryptVersion: String by extra
val geoipVersion: String by extra
val commonsCompressVersion: String by extra

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "destroystokyo-repo"
        setUrl("https://repo.destroystokyo.com/repository/maven-public/")
    }

    maven {
        name = "bstats-repo"
        setUrl("http://repo.bstats.org/content/repositories/releases/")
    }
}

dependencies {
    compileOnly("io.github.waterfallmc:waterfall-api:$waterfallApiVersion")
    compileOnly(rootProject.files("lib/FastLogin.jar"))

    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation("ninja.leaping.configurate:configurate-hocon:$configurateVersion") {
        exclude(module = "guava")
    }
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("com.j256.ormlite:ormlite-jdbc:$ormliteVersion")
    implementation("org.mindrot:jbcrypt:$bcryptVersion")
    implementation("com.maxmind.geoip2:geoip2:$geoipVersion")
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
    softDepends = setOf("FastLogin")
}

val shadowJar by tasks.getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    val relocations = listOf(
            "kotlin",
            "com.typesafe.config",
            "ninja.leaping.configurate",
            "com.zaxxer.hikari",
            "com.j256.ormlite",
            "org.mindrot.jbcrypt",
            "com.maxmind.geoip2"
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
    }
}

val wrapper by tasks.creating(Wrapper::class) {
    gradleVersion = gradleWrapperVersion
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
}

if(rootProject.findProperty("useJRebel") == "true") {
    tasks.getByName("jar").dependsOn(tasks.getByName("generateRebel"))
}
tasks.getByName("jar").dependsOn(tasks.getByName("shadowJar"))
defaultTasks("licenseFormat", "build")