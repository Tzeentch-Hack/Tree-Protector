package com.tzeench.treeprotectormobile.repository

import com.tzeench.treeprotectormobile.dto.TreeModelDto
import com.tzeench.treeprotectormobile.utils.NetworkResultState
import com.tzeench.treeprotectormobile.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface AddNewTreeRepository {

    suspend fun addNewTree(
    deviceId: String,
    coordinates: String,
    file: ByteArray
    ): Flow<NetworkResultState<TreeModelDto>>
}

class AddNewTreeRepositoryImpl constructor(private val httpClient: HttpClient): AddNewTreeRepository {
    override suspend fun addNewTree(
        deviceId: String,
        coordinates: String,
        file: ByteArray
    ): Flow<NetworkResultState<TreeModelDto>> {
        return flowOf(
            safeApiCall {
                httpClient.post(urlString = "http://16.171.182.158:8000/trees/add_new_tree?machine_id=$deviceId&coordinates=$coordinates") {
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append("file", file, Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=image$file.jpg"
                                    )
                                })
                            }
                        )
                    )
                }.body()
            })
    }

}
