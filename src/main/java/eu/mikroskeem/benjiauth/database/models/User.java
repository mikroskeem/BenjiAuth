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

package eu.mikroskeem.benjiauth.database.models;

import com.google.common.base.Preconditions;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Mark Vainomaa
 */
@DatabaseTable(tableName = "users")
public class User {
    /** Username column name */
    public final static String USERNAME_FIELD = "username";

    /** Password column name */
    public final static String PASSWORD_FIELD = "password";

    /** Registration timestamp column name */
    public final static String REGISTER_TIMESTAMP_FIELD = "register_timestamp";

    /** Registration IP address column name */
    public final static String REGISTERED_IP_ADDRESS_FIELD = "registered_ip_address";

    /** Login status column name */
    public final static String LOGGED_IN_FIELD = "logged_in";

    /** Last login Unix timestamp column name */
    public final static String LAST_LOGIN_FIELD = "last_login";

    /** Last seen Unix timestamp column name */
    public final static String LAST_SEEN_FIELD = "last_seen";

    /** Last login IP address column name */
    public final static String LAST_IP_ADDRESS_FIELD = "last_ip_address";

    /** Force kill session column name */
    public final static String FORCE_KILL_SESSION_FIELD = "force_kill_session";

    @DatabaseField(id = true, columnName = USERNAME_FIELD, canBeNull = false, width = 16)
    private String username;

    @DatabaseField(columnName = PASSWORD_FIELD, canBeNull = false)
    private String password;

    @DatabaseField(columnName = REGISTER_TIMESTAMP_FIELD, canBeNull = false)
    private Long registerTimestamp;

    @DatabaseField(columnName = REGISTERED_IP_ADDRESS_FIELD, canBeNull = false)
    private String registeredIPAddress;

    @DatabaseField(columnName = LOGGED_IN_FIELD, canBeNull = false)
    private Boolean loggedIn;

    @DatabaseField(columnName = LAST_LOGIN_FIELD)
    private Long lastLogin;

    @DatabaseField(columnName = LAST_SEEN_FIELD)
    private Long lastSeen;

    @DatabaseField(columnName = LAST_IP_ADDRESS_FIELD)
    private String lastIPAddress;

    @DatabaseField(columnName = FORCE_KILL_SESSION_FIELD, canBeNull = false)
    private Boolean forceKillSession;

    /**
     * No-args constructor for ORMLite usage
     */
    User() {}

    /**
     * Constructs new {@link User} object
     *
     * @param username Player username
     * @param password Player BCrypt hashed password
     * @param registerTimestamp Registration Unix timestamp
     * @param registeredIPAddress IP address which was used on registration
     * @param loggedIn Whether player is logged in or not
     * @param lastLogin Last login Unix timestamp
     * @param lastSeen Last seen Unix timestamp
     * @param lastIPAddress Last login IP address
     * @param forceKillSession  Whether player session should killed forcefully or not
     */
    public User(@NotNull String username, @NotNull String password, @NotNull Long registerTimestamp,
                @NotNull String registeredIPAddress, @NotNull Boolean loggedIn,
                @Nullable Long lastLogin, @Nullable Long lastSeen,
                @Nullable String lastIPAddress, @NotNull Boolean forceKillSession) {
        this.username = username;
        this.password = password;
        this.registerTimestamp = registerTimestamp;
        this.registeredIPAddress = registeredIPAddress;
        this.loggedIn = loggedIn;
        this.lastLogin = lastLogin;
        this.lastSeen = lastSeen;
        this.lastIPAddress = lastIPAddress;
        this.forceKillSession = forceKillSession;
    }

    /**
     * Gets player username
     *
     * @return Player username
     */
    @NotNull
    public String getUsername() {
        return username;
    }

    /**
     * Gets password BCrypt hash
     *
     * @return Password BCrypt hash
     */
    @NotNull
    public String getPassword() {
        return password;
    }

    /**
     * Sets password BCrypt hash
     *
     * @param password Password BCrypt hash
     */
    public void setPassword(@NotNull String password) {
        Preconditions.checkArgument(!password.isEmpty(), "Password cannot be set empty!");
        this.password = password;
    }

    /**
     * Gets player registration timestamp
     *
     * @return Player registration timestamp
     */
    @NotNull
    public Long getRegisterTimestamp() {
        return registerTimestamp;
    }

    /**
     * Gets player registration IP address
     *
     * Note: string *may* be empty only if user is force registered by server or administrator. This gets updated
     *       next time when player logs in
     *
     * @return Player registration IP address
     */
    @NotNull
    public String getRegisteredIPAddress() {
        return registeredIPAddress;
    }

    /**
     * Sets player registration IP address.
     *
     * Note: use only if registration IP address is not set (empty string).
     *
     * @param registeredIPAddress Player registration IP address
     */
    public void setRegisteredIPAddress(@NotNull String registeredIPAddress) {
        Preconditions.checkArgument(this.registeredIPAddress.isEmpty(), "Registration IP address is already set!");
        Preconditions.checkArgument(!registeredIPAddress.isEmpty(), "Registration IP address cannot be set empty!");
        this.registeredIPAddress = registeredIPAddress;
    }

    /**
     * Returns whether player is logged in or not
     *
     * @return Whether player is logged in or not
     */
    @NotNull
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets player logged in or not
     *
     * @param loggedIn Whether player is logged in or not
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets player last login timestamp. Might be null if player haven't logged in yet.
     *
     * @return Player last login timestamp
     */
    @Nullable
    public Long getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets player last login timestamp.
     *
     * @param lastLogin Player last login timestamp
     */
    public void setLastLogin(@Nullable Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Gets player last seen timestamp. Might be null if player haven't logged in yet.
     *
     * @return Player last seen timestamp
     */
    @Nullable
    public Long getLastSeen() {
        return lastSeen;
    }

    /**
     * Sets player last login timestamp.
     *
     * @param lastSeen Player last seen timestamp
     */
    public void setLastSeen(@Nullable Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     * Gets player last login IP address. Might be null if player has cleared its session
     *
     * @return Player last login IP address
     */
    @Nullable
    public String getLastIPAddress() {
        return lastIPAddress;
    }

    /**
     * Sets player last login IP address. Set to null with {@link #setLastLogin(Long)} to clear session
     *
     * @param lastIPAddress Player last login IP address
     */
    public void setLastIPAddress(@Nullable String lastIPAddress) {
        this.lastIPAddress = lastIPAddress;
    }

    /**
     * Gets whether player session should killed forcefully (e.g by <code>/logout</code> command) or not
     *
     * @return Whether player session should killed forcefully or not
     */
    public boolean getForceKillSession() {
        return forceKillSession;
    }

    /**
     * Sets whether player session should killed forcefully (e.g by <code>/logout</code> command) or not
     *
     * @param forceKillSession Whether player session should killed forcefully or not
     */
    public void setForceKillSession(boolean forceKillSession) {
        this.forceKillSession = forceKillSession;
    }
}
