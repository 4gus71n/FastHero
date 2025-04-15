package com.fast.hero

import app.cash.sqldelight.db.SqlDriver

interface SqlDriverContext {
    fun getContext(): Any
}

expect fun getSqlDriver(context: SqlDriverContext?): SqlDriver