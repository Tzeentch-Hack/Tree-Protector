package com.tzeench.treeprotectormobile.di

import com.tzeench.treeprotectormobile.presenters.CameraPresenter
import com.tzeench.treeprotectormobile.presenters.RegistryPresenter
import com.tzeench.treeprotectormobile.presenters.SatellitePresenter
import com.tzeench.treeprotectormobile.repository.AddNewTreeRepository
import com.tzeench.treeprotectormobile.repository.AddNewTreeRepositoryImpl
import com.tzeench.treeprotectormobile.repository.RegistryRepository
import com.tzeench.treeprotectormobile.repository.RegistryRepositoryImpl
import com.tzeench.treeprotectormobile.repository.SatelliteRepository
import com.tzeench.treeprotectormobile.repository.SatelliteRepositoryImpl
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun injectionModule(enableNetworkLogs: Boolean = false) = module {
    single {
        HttpClient(engineFactory = CIO) {
            expectSuccess = true
            install(HttpTimeout) {
                requestTimeoutMillis = 1000000
            }
            addDefaultResponseValidation()

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                }
                contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
            }

            if (enableNetworkLogs) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.i(tag = "Http Client", message = message)
                        }
                    }
                }.also {
                    Napier.base(DebugAntilog())
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }

    single<RegistryRepository> { RegistryRepositoryImpl(httpClient = get()) }
    factoryOf(::RegistryPresenter)

    single<AddNewTreeRepository> { AddNewTreeRepositoryImpl(httpClient = get()) }
    factoryOf(::CameraPresenter)

    single<SatelliteRepository> { SatelliteRepositoryImpl(httpClient = get()) }
    factoryOf(::SatellitePresenter)
}