package com.kiwe.domain.usecase.kiosk

import com.kiwe.domain.model.Order

interface OrderUseCase {
    suspend operator fun invoke(order: Order): Result<String>
}
