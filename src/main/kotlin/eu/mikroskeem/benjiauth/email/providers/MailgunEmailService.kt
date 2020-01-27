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

package eu.mikroskeem.benjiauth.email.providers

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.email.EmailService
import okhttp3.Credentials
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


/**
 * @author Mark Vainomaa
 */
class MailgunEmailService: EmailService {
    private lateinit var httpClient: OkHttpClient

    private lateinit var mailgunDomain: String
    private lateinit var apiKey: String
    private lateinit var domain: String
    private lateinit var from: String

    override fun initialize() {
        mailgunDomain = config.email.mailgun.mailgunDomain
        apiKey = config.email.mailgun.apiKey
        domain = config.email.mailgun.domain
        from = config.email.content.fromUser

        httpClient = OkHttpClient.Builder()
                .authenticator { _, response ->
                    val credential = Credentials.basic("api", apiKey)
                    return@authenticator response.request().newBuilder().header("Authorization", credential).build()
                }
                .build()

        // Test
        httpClient.newCall("$mailgunUrl/domains/$domain/ips".getRequest().build()).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw EmailService.EmailServiceInitException(
                        "Failed to test Mailgun connection, got response code ${resp.code()} whilst sending a GET request to ${resp.request().url()}"
                )
            }
        }
    }

    override fun sendEmail(recipients: Collection<String>, subject: String, body: EmailService.EmailBody) {
        val form = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", from)
                .addFormDataPart("subject", subject)

        recipients.forEach {
            form.addFormDataPart("to", it)
        }

        when (body.type) {
            EmailService.EmailType.HTML -> form.addFormDataPart("html", body.content)
            EmailService.EmailType.PLAIN -> form.addFormDataPart("text", body.content)
        }

        httpClient.newCall("$mailgunUrl/$domain/messages".postRequest(form.build()).build()).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw Exception("Failed to send an email: ${ resp.body()?.string()}")
            }
        }
    }

    private val mailgunUrl get() = "https://$mailgunDomain/v3"
    private fun String.getRequest() = Request.Builder().get().url(this)
    private fun String.postRequest(body: RequestBody) = Request.Builder().post(body).url(this)
}