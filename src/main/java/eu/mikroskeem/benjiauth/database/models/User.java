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
    @DatabaseField(id = true, columnName = "username", canBeNull = false)
    private String username;

    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;

    @DatabaseField(columnName = "registerTimestamp", canBeNull = false)
    private Long registerTimestamp;

    @DatabaseField(columnName = "registeredIPAddress", canBeNull = false)
    private String registeredIPAddress;

    @DatabaseField(columnName = "loggedIn", canBeNull = false)
    private Boolean loggedIn;

    @DatabaseField(columnName = "lastLogin")
    private Long lastLogin;

    @DatabaseField(columnName = "lastIPAddress")
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
