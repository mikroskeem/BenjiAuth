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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mark Vainomaa
 */
@DatabaseTable(tableName = "benjiauth_metadata")
public class DatabaseMetadata {
    /** Key column name */
    public final static String KEY_FIELD = "key";

    /** Value type name */
    public final static String VALUE_TYPE_FIELD = "type";

    /** Key value name */
    public final static String VALUE_FIELD = "value";

    @DatabaseField(id = true, columnName = KEY_FIELD, canBeNull = false, width = 16)
    private String key;

    @DatabaseField(columnName = VALUE_TYPE_FIELD, canBeNull = false)
    private String type;

    @DatabaseField(columnName = VALUE_FIELD)
    private String value;

    DatabaseMetadata() {}

    public DatabaseMetadata(String key, String type, String value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @Nullable
    public String getValue() {
        return value;
    }
}
