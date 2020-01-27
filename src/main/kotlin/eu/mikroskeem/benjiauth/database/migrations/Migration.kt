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

package eu.mikroskeem.benjiauth.database.migrations

import com.j256.ormlite.dao.Dao
import eu.mikroskeem.benjiauth.plugin
import java.util.logging.Level

/**
 * A database migration
 *
 * @author Mark Vainomaa
 */
abstract class Migration {
    abstract val oldVersion: Int
    abstract val newVersion: Int
    abstract val description: String

    fun update(currentVersion: Int, allowFailure: Boolean,dao: Dao<*, *>) {
        try {
            doUpdate(currentVersion, dao)
        } catch (e: Exception) {
            if (allowFailure) {
                plugin.pluginLogger.log(Level.WARNING, "Migration ${this.javaClass.name} ($description) failed", e)
            } else {
                throw e
            }
        }
    }

    abstract fun doUpdate(currentVersion: Int, dao: Dao<*, *>)
}