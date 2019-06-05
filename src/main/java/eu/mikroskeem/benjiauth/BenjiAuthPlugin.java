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

package eu.mikroskeem.benjiauth;

import eu.mikroskeem.benjiauth.config.Benji;
import eu.mikroskeem.benjiauth.config.BenjiMessages;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

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
    Logger getPluginLogger();
}
