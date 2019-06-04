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

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Registers a new player
     *
     * @param username Player username
     * @param password Password
     */
    void registerUser(@NotNull String username, @NotNull String password);

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
    boolean isEligibleForSessionLogin(@NotNull ProxiedPlayer player);

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
     * Gets player e-mail
     *
     * @param player Player
     * @return E-mail address. Null if not set
     */
    @Nullable
    String getEmail(@NotNull ProxiedPlayer player);

    /**
     * Sets new e-mail address for player
     *
     * @param player Player
     * @param email New e-mail address. Use null to unset
     * @param verificationCode E-mail address verification code. Use null if you don't need e-mail address verification
     */
    void setEmail(@NotNull ProxiedPlayer player, @Nullable String email, @Nullable String verificationCode);

    /**
     *
     * @param player
     * @param verificationCode
     * @return
     */
    @NotNull
    EmailVerifyResult verifyEmail(@NotNull ProxiedPlayer player, @Nullable String verificationCode);

    /**
     * Returns whether player's email address is verified or not
     *
     * @param player Player
     * @return whether player's email address is verified or not
     */
    boolean isEmailVerified(@NotNull ProxiedPlayer player);

    /**
     * Gets player's password reset code
     *
     * @param player Player
     * @return Password reset code, or null if none pending
     */
    @Nullable
    String getPasswordResetCode(@NotNull ProxiedPlayer player);

    /**
     * Gets player's password reset code. Note that password reset code gets unset on player login.
     *
     * @param player Player
     * @param code Password reset code. Use null to unset
     */
    void setPasswordResetCode(@NotNull ProxiedPlayer player, @Nullable String code);

    /**
     *
     * @param player
     * @param code
     * @param newPassword
     * @return
     */
    @NotNull
    PasswordResetVerifyResult verifyPasswordReset(@NotNull ProxiedPlayer player, @Nullable String code, @NotNull String newPassword);

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

    enum EmailVerifyResult {
        SUCCESS(true),
        ALREADY_VERIFIED(false),
        EXPIRED(false),
        FAILED(false),
        ;

        private final boolean successful;

        EmailVerifyResult(boolean successful) {
            this.successful = successful;
        }

        public boolean isSuccessful() {
            return successful;
        }
    }

    enum PasswordResetVerifyResult {
        SUCCESS(true),
        EXPIRED(false),
        FAILED(false),
        ;

        private final boolean successful;

        PasswordResetVerifyResult(boolean successful) {
            this.successful = successful;
        }

        public boolean isSuccessful() {
            return successful;
        }
    }
}