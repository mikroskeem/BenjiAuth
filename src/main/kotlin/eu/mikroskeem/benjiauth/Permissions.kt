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

package eu.mikroskeem.benjiauth

/**
 * @author Mark Vainomaa
 */

/*
 * Big list of permissions used in this plugin
 *
 * TL;DR quick start guide:
 * - Grant benjiauth.player.* to players
 * - Grant benjiauth.admin.* and benjiauth.command.benjiauth to network operators
 *
 */

/**
 * Allows player to use /login command
 */
const val COMMAND_LOGIN = "benjiauth.player.login"
/**
 * Allows player to use /register command
 */
const val COMMAND_REGISTER = "benjiauth.player.register"
/**
 * Allows player to use /login command
 */
const val COMMAND_LOGOUT = "benjiauth.player.logout"
/**
 * Allows player to use /changepassword
 */
const val COMMAND_CPW = "benjiauth.player.changepassword"

/**
 * Allows access to /benjiauth command
 */
const val COMMAND_BENJIAUTH = "benjiauth.command.benjiauth"
/**
 * Allows server operator to use /benjiauth forcelogin
 */
const val ADMIN_ACTION_FORCELOGIN = "benjiauth.admin.forcelogin"
/**
 * Allows server operator to use /benjiauth forcelogout
 */
const val ADMIN_ACTION_LOGOUT = "benjiauth.admin.forcelogout"
/**
 * Allows server operator to use /benjiauth register
 */
const val ADMIN_ACTION_REGISTER = "benjiauth.admin.register"
/**
 * Allows server operator to use /benjiauth reload
 */
const val ADMIN_ACTION_RELOAD = "benjiauth.admin.reload"
/**
 * Allows server operator to use /benjiauth unregister
 */
const val ADMIN_ACTION_UNREGISTER = "benjiauth.admin.unregister"