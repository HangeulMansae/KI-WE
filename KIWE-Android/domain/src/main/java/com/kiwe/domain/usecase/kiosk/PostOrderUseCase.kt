package com.kiwe.domain.usecase.kiosk

import com.kiwe.domain.model.Order

interface PostOrderUseCase {
    suspend operator fun invoke(
        kioskId: Int,
        order: Order,
    ): Result<String>
}
