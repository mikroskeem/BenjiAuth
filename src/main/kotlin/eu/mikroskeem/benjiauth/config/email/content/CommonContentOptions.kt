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

package eu.mikroskeem.benjiauth.config.email.content

import eu.mikroskeem.benjiauth.email.EmailService
import eu.mikroskeem.benjiauth.plugin
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Locale

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
open class CommonContentOptions {
    @Setting(value = "subject", comment = "E-mail subject. Supported placeholders:\n" +
            "- {player_name}")
    var subject = ""
        private set

    @Setting(value = "source", comment = "E-mail body source. Must be a file name inside plugins/BenjiAuth/email path.\n" +
            "Supported placeholders:\n" +
            "- {player_name}\n" +
            "- {player_uuid}\n" +
            "- {verification_code}")
    var source = ""
        private set

    @Setting(value = "source-type", comment = "E-mail body source type. Supported types:\n" +
            "- html\n" +
            "- text\n" +
            "\n" +
            "Defaults to html, if invalid value is passed")
    var sourceType = "html"
        private set

    val isValid: Boolean get() {
        return subject.isNotEmpty() && source.isNotEmpty() && Files.exists(plugin.pluginFolder.resolve("email").resolve(source))
    }

    val sourceTypeValue: EmailService.EmailType get() {
        return try {
            EmailService.EmailType.valueOf(sourceType.toUpperCase(Locale.ROOT))
        } catch (e: Exception) {
            EmailService.EmailType.HTML
        }
    }
}