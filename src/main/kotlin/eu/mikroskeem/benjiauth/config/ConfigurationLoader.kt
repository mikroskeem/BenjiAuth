/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.benjiauth.config

import com.google.common.reflect.TypeToken
import eu.mikroskeem.benjiauth.Title
import eu.mikroskeem.benjiauth.TitleSerializer
import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.loader.HeaderMode
import ninja.leaping.configurate.objectmapping.ObjectMapper
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers
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

        // Add custom (de)serializers
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Title::class.java), TitleSerializer())

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