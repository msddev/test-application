package com.mkdev.local.di

import com.mkdev.local.ArchAppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DATABASE = "DATABASE"

val localModule = module {
    single(qualifier = named(DATABASE)) { ArchAppDatabase.buildDatabase(androidContext()) }
    factory { (get(qualifier = named(DATABASE)) as ArchAppDatabase).photoDao() }
}