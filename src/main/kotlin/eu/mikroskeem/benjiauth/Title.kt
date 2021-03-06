/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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
        if (!shouldBeSent())
            return

        proxy.createTitle()
                .title(*TextComponent.fromLegacyText(title.color()))
                .subTitle(*TextComponent.fromLegacyText(subTitle.color()))
                .fadeIn(fadeIn.toInt())
                .stay(stay.toInt())
                .fadeOut(fadeOut.toInt())
                .run(player::sendTitle)
    }
}

fun ProxiedPlayer.resetTitle() {
    proxy.createTitle().clear().send(this)
    proxy.createTitle().reset().send(this)
}

class TitleSerializer: TypeSerializer<Title> {
    override fun deserialize(type: TypeToken<*>, node: ConfigurationNode): Title {
        val title = node.getNode("title").string ?: ""
        val subTitle = node.getNode("subtitle").string ?: ""
         val fadeIn = node.getNode("fade-in").long
        val stay = node.getNode("stay").long
        val fadeOut = node.getNode("fade-out").long
        return Title(title, subTitle, fadeIn, stay, fadeOut)
    }

    override fun serialize(type: TypeToken<*>, titleObj: Title?, node: ConfigurationNode) {
        titleObj!!.run {
            node.getNode("title").value = title
            node.getNode("subtitle").value = subTitle
            node.getNode("fade-in").value = fadeIn
            node.getNode("stay").value = stay
            node.getNode("fade-out").value = fadeOut
        }
    }
}