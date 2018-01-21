/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;

/**
 * Geo IP API
 *
 * @author Mark Vainomaa
 */
public interface GeoIPAPI {
    /**
     * Gets country by IP
     *
     * @param ipAddress IP address to query
     * @return Country ISO code, or null if no country was found for given address
     */
    @Nullable
    String getCountryByIP(@NotNull InetAddress ipAddress);

    /**
     * Gets country by IP
     *
     * @param ipAddress IP address in a string form to query
     * @return Country ISO code, or null if no country was found for given address
     */
    @Nullable
    String getCountryByIP(@NotNull String ipAddress);
}
