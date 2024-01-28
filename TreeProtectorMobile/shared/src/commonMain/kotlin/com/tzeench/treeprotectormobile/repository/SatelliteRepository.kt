package com.tzeench.treeprotectormobile.repository

import com.tzeench.treeprotectormobile.dto.SatelliteDto
import com.tzeench.treeprotectormobile.utils.NetworkResultState
import com.tzeench.treeprotectormobile.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface SatelliteRepository {
    suspend fun makeTreeSatellitePhoto(
        x: Float,
        y: Float,
        zoom: Int,
        deviceId: String
    ): Flow<NetworkResultState<SatelliteDto>>
}

class SatelliteRepositoryImpl constructor(private val httpClient: HttpClient) :
    SatelliteRepository {

    override suspend fun makeTreeSatellitePhoto(
        x: Float,
        y: Float,
        zoom: Int,
        deviceId: String
    ): Flow<NetworkResultState<SatelliteDto>> {
        return flowOf(
            safeApiCall {
                httpClient.get(urlString = "http://16.171.182.158:8000/trees/make_tree_satellite_photo?x=$x&y=$y&zoom=$zoom&machine_id=$deviceId") {}
                    .body()
            }
        )
    }
}

