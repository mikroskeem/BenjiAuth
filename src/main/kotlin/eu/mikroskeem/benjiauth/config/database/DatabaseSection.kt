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

package eu.mikroskeem.benjiauth.config.database

import com.zaxxer.hikari.HikariConfig
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class DatabaseSection {
    @Setting(value = "database-url", comment = "Database JDBC connection URL")
    var database = "jdbc:mysql://127.0.0.1:3306/th3databaze?user=r00t&password=p2sswurd"
        private set

    @Setting(value = "driver-class", comment = "Driver class to initialize")
    var driverClass = "com.mysql.jdbc.Driver"
        private set

    @Setting("driver-parameters", comment = "Database driver parameters. Advanced use only")
    var driverParams: Map<String, String> = mapOf(
            Pair("properties", "useUnicode=true;characterEncoding=utf8"),
            Pair("prepStmtCacheSize", "250"),
            Pair("prepStmtCacheSqlLimit", "2048"),
            Pair("cachePrepStmts", "true"),
            Pair("useServerPrepStmts", "true"))
        private set

    @Setting(value = "table-name", comment = "What table name to use?")
    var tableName: String = "users"
        private set

    val asHikariConfig: HikariConfig get() = HikariConfig().apply hikari@ {
        jdbcUrl = database
        driverParams.forEach(this::addDataSourceProperty)
    }
}