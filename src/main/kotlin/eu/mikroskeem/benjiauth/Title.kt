/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import com.google.common.reflect.TypeToken
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer

/**
 * @author Mark Vainomaa
 */
data class Title(
        val title: String,
        val subTitle: String,
        val fadeIn: Long = 20,
        val stay: Long = 20,
        val fadeOut: Long = 20
)

fun Title.shouldBeSent(): Boolean = title.isNotEmpty() || subTitle.isNotEmpty()

fun ProxiedPlayer.sendTitle(titleObject: Title) {
    val player = this
    titleObject.run {
        // Don't send if both title and subtitle are empty
        if(!shouldBeSent())
            return

        proxy.createTitle().also {
            it.title(*TextComponent.fromLegacyText(title.color()))
            it.subTitle(*TextComponent.fromLegacyText(subTitle.color()))
            it.fadeIn(fadeIn.toInt())
            it.stay(stay.toInt())
            it.fadeOut(fadeOut.toInt())
        }.run(player::sendTitle)
    }
}

fun ProxiedPlayer.resetTitle() = proxy.createTitle().reset().send(this)

class TitleSerializer: TypeSerializer<Title> {
    override fun deserialize(type: TypeToken<*>, node: ConfigurationNode): Title {
        val title = node.getNode("title").string
        val subTitle = node.getNode("subtitle").string
        val fadeIn = node.getNode("fade-in").long
        val stay = node.getNode("stay").long
        val fadeOut = node.getNode("fade-out").long
        return Title(title, subTitle, fadeIn, stay, fadeOut)
    }

    override fun serialize(type: TypeToken<*>, titleObj: Title, node: ConfigurationNode) {
        titleObj.run {
            node.getNode("title").value = title
            node.getNode("subtitle").value = subTitle
            node.getNode("fade-in").value = fadeIn
            node.getNode("stay").value = stay
            node.getNode("fade-out").value = fadeOut
        }
    }
}