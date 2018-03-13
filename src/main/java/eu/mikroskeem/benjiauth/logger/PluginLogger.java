/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is so self explanatory that I can't be arsed to documentate it...
 *
 * @author Mark Vainomaa
 */
public interface PluginLogger {
    void info(@NotNull String message);

    void info(@NotNull String message, @Nullable Throwable throwable);

    void warn(@NotNull String message);

    void warn(@NotNull String message, @Nullable Throwable throwable);

    void error(@NotNull String message);

    void error(@NotNull String message, @Nullable Throwable throwable);
}
