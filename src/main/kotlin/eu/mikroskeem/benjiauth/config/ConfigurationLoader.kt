/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config

import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.loader.HeaderMode
import ninja.leaping.configurate.objectmapping.ObjectMapper
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import java.nio.file.Files
import java.nio.file.Path

/**
 * Configuration loader using Configurate library
 *
 * @author Mark Vainomaa
 */
class ConfigurationLoader<T: Any>(
        configurationFile: Path,
        private val configClass: Class<T>,
        private val baseNodeName: String = configClass.name.run { substring(lastIndexOf('.') + 1) },
        private val header: String? = null
) {
    /** This configuration loader's default options */
    private val defaultOptions: ConfigurationOptions get() = ConfigurationOptions.defaults().let { opts ->
        this@ConfigurationLoader.header?.let { hdr -> opts.setHeader(hdr) } ?: opts
    }

    /** Configuration file absolute location */
    private val configurationPath = configurationFile.toAbsolutePath()
    /** Configuration loader instance */
    private val loader: ConfigurationLoader<CommentedConfigurationNode>
    /** Object mapper instance for [T] */
    private val mapper: ObjectMapper<T>.BoundInstance
    /** Configuration lodaer base node */
    private lateinit var baseNode: CommentedConfigurationNode

    /** Configuration instance */
    lateinit var configuration: T
        private set

    init {
        // Validate configuration file
        if(Files.isDirectory(configurationPath))
            throw IllegalStateException("Path $configurationPath is a directory!")

        if(Files.notExists(configurationPath))
            Files.createDirectories(configurationPath.parent)

        // Validate configuration class
        configClass.annotations.find { it is ConfigSerializable } ?:
                throw IllegalStateException("configClass must have @ConfigSerializable annotation!")

        // Validate base node name
        if(baseNodeName.isEmpty())
            throw IllegalStateException("baseNodeName must not be empty!")

        // Build configuration loader
        loader = HoconConfigurationLoader.builder().apply {
            defaultOptions = this@ConfigurationLoader.defaultOptions
            headerMode = HeaderMode.PRESERVE
            setPath(configurationPath)
        }.build()

        // Build object mapper
        mapper = ObjectMapper.forClass(configClass).bindToNew()

        // Populate initial configuration
        load()
        save()
    }

    /**
     * Loads configuration
     */
    fun load() {
        baseNode = loader.load()
        configuration = mapper.populate(baseNode.getNode(baseNodeName))
    }

    /**
     * Saves configuration
     */
    fun save() {
        mapper.serialize(baseNode.getNode(baseNodeName))
        loader.save(baseNode)
    }
}