package com.fast.hero

import android.app.Application
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.fast.hero.sdk.FastCore

class FastHeroApplication : Application(), SqlDriverContext {

    override fun getContext(): Any {
        return this
    }

    val fastCore = FastCore(
        driver = getSqlDriver(this),
        now = { System.currentTimeMillis() }
    )
}