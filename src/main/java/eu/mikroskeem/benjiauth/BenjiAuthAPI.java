/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth;

import org.jetbrains.annotations.NotNull;

/**
 * BenjiAuth plugin API
 *
 * @author Mark Vainomaa
 */
public interface BenjiAuthAPI {
    /**
     * Gets instance of {@link LoginManager}
     *
     * @return Instance of {@link LoginManager}
     */
    @NotNull
    LoginManager getLoginManager();

    /**
     * Gets instance of {@link GeoIPAPI}.
     * Default implementation uses MaxMind's GeoLite2 Country database
     *
     * @return Instance of {@link GeoIPAPI}
     */
    @NotNull
    GeoIPAPI getGeoIPAPI();
}
