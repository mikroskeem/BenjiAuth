/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth;

import eu.mikroskeem.benjiauth.config.Benji;
import eu.mikroskeem.benjiauth.config.BenjiMessages;
import eu.mikroskeem.benjiauth.logger.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * @author Mark Vainomaa
 */
public interface BenjiAuthPlugin {
    /**
     * Gets BenjiAuth plugin API
     *
     * @return Instance of {@link BenjiAuthAPI}
     */
    @NotNull
    BenjiAuthAPI getApi();

    /**
     * Gets plugin configuration
     *
     * @return Instance of {@link Benji}
     */
    @NotNull
    Benji getConfig();

    /**
     * Gets plugin messages
     *
     * @return Instance of {@link BenjiMessages}
     */
    @NotNull
    BenjiMessages getMessages();

    /**
     * Reloads plugin configuration
     */
    void reloadConfig();

    /**
     * Gets plugin data folder
     *
     * @return Plugin data folder
     */
    @NotNull
    Path getPluginFolder();

    /**
     * Gets plugin logger
     *
     * @return Plugin logger
     */
    @NotNull
    PluginLogger getPluginLogger();
}
