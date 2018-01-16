/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.database

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit.HOURS

/**
 * @author Mark Vainomaa
 */
class GeoIPDatabase {
    private val cache: LoadingCache<String, String> = CacheBuilder.newBuilder()
            .expireAfterAccess(12, HOURS)
            .build(CacheLoader.from { ip ->
                "ree"
            })
}