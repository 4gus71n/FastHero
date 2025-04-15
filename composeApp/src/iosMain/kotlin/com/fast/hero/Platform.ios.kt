package com.fast.hero

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import platform.UIKit.UIDevice

actual fun getSqlDriver(context: SqlDriverContext?): SqlDriver = NativeSqliteDriver(
    AppDatabase.Schema,
    "AppDatabase"
)