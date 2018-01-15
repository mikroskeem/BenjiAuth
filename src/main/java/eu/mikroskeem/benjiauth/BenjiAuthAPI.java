/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth;

import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Vainomaa
 */
public interface BenjiAuthAPI {
    @NotNull
    LoginManager getLoginManager();
}
