/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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

    /** Last login IP address column name */
    public final static String LAST_IP_ADDRESS_FIELD = "last_ip_address";

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

    @DatabaseField(columnName = LAST_IP_ADDRESS_FIELD)
    private String lastIPAddress;

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
     * @param lastIPAddress Last login IP address
     */
    public User(@NotNull String username, @NotNull String password, @NotNull Long registerTimestamp,
                @NotNull String registeredIPAddress, @NotNull Boolean loggedIn,
                @Nullable Long lastLogin, @Nullable String lastIPAddress) {
        this.username = username;
        this.password = password;
        this.registerTimestamp = registerTimestamp;
        this.registeredIPAddress = registeredIPAddress;
        this.loggedIn = loggedIn;
        this.lastLogin = lastLogin;
        this.lastIPAddress = lastIPAddress;
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
     * @return Player registration IP address
     */
    @NotNull
    public String getRegisteredIPAddress() {
        return registeredIPAddress;
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
     * Gets player last login timestamp. Might be null if player has cleared its session
     *
     * @return Player last login timestamp
     */
    @Nullable
    public Long getLastLogin() {
        return lastLogin;
    }

    /**
     * Sets player last login timestamp. Set to null with {@link #setLastIPAddress(String)} to clear session
     *
     * @param lastLogin Player last login timestamp
     */
    public void setLastLogin(@Nullable Long lastLogin) {
        this.lastLogin = lastLogin;
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
}
