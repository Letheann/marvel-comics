package com.example.comics.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object QualifierMarvelRetrofit : Qualifier {
    override val value: QualifierValue
        get() = "QualifierMarvelRetrofit"
}

object QualifierMarvelApi : Qualifier {
    override val value: QualifierValue
        get() = "QualifierMarvelApi"
}