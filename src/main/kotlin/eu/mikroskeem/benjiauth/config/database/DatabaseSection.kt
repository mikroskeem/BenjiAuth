/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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


    val asHikariConfig: HikariConfig get() = HikariConfig().apply hikari@ {
        jdbcUrl = database
        driverParams.forEach(this::addDataSourceProperty)
    }
}