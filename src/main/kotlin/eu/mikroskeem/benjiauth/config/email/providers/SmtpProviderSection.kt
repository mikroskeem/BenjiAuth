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

package eu.mikroskeem.benjiauth.config.email.providers

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import java.util.Properties

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class SmtpProviderSection {
    @SMTPProperty("mail.smtp.host")
    var hostname = ""
        private set

    @SMTPProperty("mail.smtp.port")
    var port = 587
        private set

    @SMTPProperty("mail.smtp.user")
    var username = ""
        private set

    @SMTPProperty("mail.smtp.password")
    var password = ""
        private set

    @SMTPProperty("mail.smtp.from")
    var fromHeader = "BenjiAuth <noreply+benjiauth@example.com>"
        private set

    internal fun toProperties(): Properties {
        val properties = Properties()
        this::class.java.fields
                .filter { it.isAnnotationPresent(SMTPProperty::class.java) }
                .map { it.getAnnotation(SMTPProperty::class.java).name to it.get(this).toString() }
                .forEach { (key, value) -> properties[key] = value }
        return properties
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
internal annotation class SMTPProperty(val name: String)