/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;

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

    User() {}

    public User(@NotNull String username, @NotNull String password, @NotNull Long registerTimestamp) {
        this.username = username;
        this.password = password;
        this.registerTimestamp = registerTimestamp;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public Long getRegisterTimestamp() {
        return registerTimestamp;
    }
}
