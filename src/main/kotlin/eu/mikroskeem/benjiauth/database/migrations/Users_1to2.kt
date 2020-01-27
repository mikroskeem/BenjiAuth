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
import com.j256.ormlite.db.H2DatabaseType
import com.j256.ormlite.db.MysqlDatabaseType
import com.j256.ormlite.db.SqliteDatabaseType
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.database.models.User

/**
 * @author Mark Vainomaa
 */
class Users_1to2: Migration() {
    override val oldVersion: Int = 1
    override val newVersion: Int = 2
    override val description: String = "Adds original username column and convets usernames to lower case for proper username comparing"

    override fun doUpdate(currentVersion: Int, dao: Dao<*, *>) {
        if (dao.tableName != config.database.tableName || currentVersion >= newVersion)
            // Nothing to do here
            return

        when (dao.connectionSource.databaseType) {
            is H2DatabaseType,
            is MysqlDatabaseType -> {
                dao.executeRaw("alter table `${dao.tableName}` add column `${User.ORIGINAL_USERNAME_FIELD}` varchar(16) after `${User.USERNAME_FIELD}`;")
                dao.executeRaw("update `${dao.tableName}` set `${User.ORIGINAL_USERNAME_FIELD}` = `${User.USERNAME_FIELD}`;")
                dao.executeRaw("update `${dao.tableName}` set `${User.USERNAME_FIELD}` = lower(`${User.USERNAME_FIELD}`);")
            }
            is SqliteDatabaseType -> {
                // Does not support ordering :(
                dao.executeRaw("alter table `${dao.tableName}` add column `${User.ORIGINAL_USERNAME_FIELD}` varchar(16);")
                dao.executeRaw("update `${dao.tableName}` set `${User.ORIGINAL_USERNAME_FIELD}` = `${User.USERNAME_FIELD}`;")
                dao.executeRaw("update `${dao.tableName}` set `${User.USERNAME_FIELD}` = lower(`${User.USERNAME_FIELD}`);")
            }
        }
    }
}