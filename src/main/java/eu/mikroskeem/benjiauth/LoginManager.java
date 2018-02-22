/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Vainomaa
 */
public interface LoginManager {
    /**
     * Returns whether username is registered or not
     *
     * @param username Username to check
     * @return Registered or not
     */
    boolean isRegistered(@NotNull String username);

    /**
     * Returns whether player is registered or not
     *
     * @param player Player to check
     * @return Registered or not
     */
    boolean isRegistered(@NotNull ProxiedPlayer player);

    /**
     * Registers a new player
     *
     * @param player Player
     * @param password Password
     */
    void registerUser(@NotNull ProxiedPlayer player, @NotNull String password);

    /**
     * Unregisters player from database
     *
     * @param player Player to unregister
     */
    void unregisterUser(@NotNull ProxiedPlayer player);

    /**
     * Unregisters player from database
     *
     * @param username Player username to unregister
     */
    void unregisterUser(@NotNull String username);

    /**
     * Returns whether player is egilible for password login or not
     *
     * @param player Player
     * @return Whether player is egilible for password login or not
     */
    boolean isEgilibleForSessionLogin(@NotNull ProxiedPlayer player);

    /**
     * Returns whether player is logged in or not
     *
     * @param player Player to check
     * @return Logged in or not
     */
    boolean isLoggedIn(@NotNull ProxiedPlayer player);

    /**
     * Returns whether player was logged in forcefully or not
     *
     * @param player Player to check
     * @return Logged in forcefully or not
     * @see #loginUser(ProxiedPlayer, boolean)
     */
    boolean isForcefullyLoggedIn(@NotNull ProxiedPlayer player);

    /**
     * Marks player logged in
     *
     * @param player Player to mark logged in
     */
    default void loginUser(@NotNull ProxiedPlayer player) {
        loginUser(player, false);
    }

    /**
     * Marks player logged in
     *
     * @param player Player to mark logged in
     * @param force Whether this was a forced login (by plugin or command) or not
     */
    void loginUser(@NotNull ProxiedPlayer player, boolean force);

    /**
     * Logs player out without clearing session and keeping one "ready"
     *
     * @param player Player to mark logged out
     * @see #isUserReady(ProxiedPlayer)
     * @see #markUserReady(ProxiedPlayer)
     */
    default void logoutUser(@NotNull ProxiedPlayer player) {
        logoutUser(player, false);
    }

    /**
     * Logs player out, keeping one "ready"
     *
     * @param player Player to mark logged out
     * @param clearSession Whether to clear session as well or not
     * @see #isUserReady(ProxiedPlayer)
     * @see #markUserReady(ProxiedPlayer)
     */
    default void logoutUser(@NotNull ProxiedPlayer player, boolean clearSession) {
        logoutUser(player, clearSession, true);
    }

    /**
     * Logs player out
     *
     * @param player Player to mark logged out
     * @param clearSession Whether to clear session as well or not
     * @param keepReady Whether to keep "ready" status or not, see {@link #isUserReady(ProxiedPlayer)}
     *                   Falsy value is only useful when player is disconnecting from server
     * @see #isUserReady(ProxiedPlayer)
     * @see #markUserReady(ProxiedPlayer)
     */
    void logoutUser(@NotNull ProxiedPlayer player, boolean clearSession, boolean keepReady);

    /**
     * Checks registered player password in database against {@code password}
     *
     * @param player Registered player
     * @param password Password
     * @return Whether password was correct or not
     */
    boolean checkPassword(@NotNull ProxiedPlayer player, @NotNull String password);

    /**
     * Sets new password for player
     *
     * @param player Player
     * @param newPassword New password
     */
    void changePassword(@NotNull ProxiedPlayer player, @NotNull String newPassword);

    /**
     * Returns whether user initial data processing is done or not and player is eligible for
     * events and such.
     *
     * @param player Player
     * @return
     */
    boolean isUserReady(@NotNull ProxiedPlayer player);

    /**
     * Marks user ready, in other words initial data processing is done and events about given user
     * can be fired.
     * Otherwise only data processing is done.
     * This method can be invoked only once per user session.
     *
     * @param player Player
     */
    void markUserReady(@NotNull ProxiedPlayer player);

    /**
     * Gets amount of registrations for given IP address
     *
     * @param ipAddress IP address to query
     * @return Count of registrations for given IP address
     */
    long getRegistrations(@NotNull String ipAddress);
}