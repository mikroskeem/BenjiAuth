/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.database.models;

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
    public final static String USERNAME_FIELD = "username";
    public final static String PASSWORD_FIELD = "password";
    public final static String REGISTER_TIMESTAMP_FIELD = "register_timestamp";
    public final static String REGISTERED_IP_ADDRESS_FIELD = "registered_ip_address";
    public final static String LOGGED_IN_FIELD = "logged_in";
    public final static String LAST_LOGIN_FIELD = "last_login";
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

    User() {}

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

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public Long getRegisterTimestamp() {
        return registerTimestamp;
    }

    @NotNull
    public String getRegisteredIPAddress() {
        return registeredIPAddress;
    }

    @NotNull
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(@NotNull Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Nullable
    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(@Nullable Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Nullable
    public String getLastIPAddress() {
        return lastIPAddress;
    }

    public void setLastIPAddress(@Nullable String lastIPAddress) {
        this.lastIPAddress = lastIPAddress;
    }
}
