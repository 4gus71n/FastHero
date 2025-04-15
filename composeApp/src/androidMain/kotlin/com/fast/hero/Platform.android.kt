package com.fast.hero

import android.os.Build
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.datlag.tooling.Platform

actual fun getSqlDriver(context: SqlDriverContext?): SqlDriver = AndroidSqliteDriver(
    AppDatabase.Schema,
    context?.getContext() as FastHeroApplication,
    "AppDatabase"
)