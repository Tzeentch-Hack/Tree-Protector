package com.tzeench.treeprotectormobile.repository

import com.tzeench.treeprotectormobile.dto.GetAllTreesModelDto
import com.tzeench.treeprotectormobile.utils.NetworkResultState
import com.tzeench.treeprotectormobile.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RegistryRepository {

    suspend fun getAllTrees(): Flow<NetworkResultState<GetAllTreesModelDto>>
}

class RegistryRepositoryImpl constructor(private val httpClient: HttpClient): RegistryRepository {
    override suspend fun getAllTrees(): Flow<NetworkResultState<GetAllTreesModelDto>> {
        return flowOf(
            safeApiCall {
                httpClient.get(urlString = "http://16.171.182.158:8000/trees/get_all_trees"){}.body()
            }
        )
    }

}
