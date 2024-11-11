package com.kiwe.data.network.service

import com.kiwe.data.network.util.getResult
import com.kiwe.data.network.util.postResult
import com.kiwe.domain.model.LogoutParam
import com.kiwe.domain.model.MemberInfoResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import javax.inject.Inject

class MemberService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        suspend fun searchMemberByEmail(email: String): Result<MemberInfoResponse> =
            client.getResult("api/members/email") {
                parameter("email", email)
            }

        suspend fun searchMemberById(id: Int): Result<MemberInfoResponse> = client.getResult("api/members/$id")

        suspend fun searchAllMember(): Result<List<MemberInfoResponse>> = client.getResult("api/members/all")

        suspend fun logout(logoutParam: LogoutParam): Result<Unit> =
            client.postResult<Unit>("api/members/log-out") {
                setBody(logoutParam)
            }
    }
