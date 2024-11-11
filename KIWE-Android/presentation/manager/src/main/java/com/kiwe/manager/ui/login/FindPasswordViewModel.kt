package com.kiwe.manager.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.EditMemberParam
import com.kiwe.domain.usecase.manager.edit.EditMemberInfoUseCase
import com.kiwe.domain.usecase.manager.menu.GetMenuByIdUseCase
import com.kiwe.domain.usecase.manager.search.SearchAllMemberUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByEmailUseCase
import com.kiwe.domain.usecase.manager.search.SearchMemberByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel
    @Inject
    constructor(
        private val searchMemberByEmailUseCase: SearchMemberByEmailUseCase,
        private val searchMemberByIdUseCase: SearchMemberByIdUseCase,
        private val searchAllMemberUseCase: SearchAllMemberUseCase,
        private val editMemberInfoUseCase: EditMemberInfoUseCase,
        private val getMenuByIdUseCase: GetMenuByIdUseCase
    ) : ViewModel(),
        ContainerHost<FindPasswordState, FindPasswordSideEffect> {
        override val container: Container<FindPasswordState, FindPasswordSideEffect> =
            container(
                initialState = FindPasswordState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        FindPasswordSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        FindPasswordSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onSearchMemberByEmail() =
            intent {
                val response = searchMemberByEmailUseCase(state.searchMemberByEmail).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onSearchMemberById() =
            intent {
                val response = searchMemberByIdUseCase(state.id).getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onSearchAllMember() =
            intent {
                val response = searchAllMemberUseCase().getOrThrow()
                postSideEffect(FindPasswordSideEffect.Toast(response.toString()))
            }

        fun onEditMemberInfo() =
            intent {
                val response =
                    editMemberInfoUseCase(
                        memberId = state.id,
                        newMemberInfo =
                            EditMemberParam(
                                name = state.name,
                                email = state.email,
                                kioskIds = listOf(),
                            ),
                    )
                postSideEffect(FindPasswordSideEffect.Toast(response.getOrThrow().toString()))
            }

        fun onGetMenuById() = intent {
            val response = getMenuByIdUseCase(state.id)
            postSideEffect(FindPasswordSideEffect.Toast(response.getOrThrow().toString()))
        }
    }

@Immutable
data class FindPasswordState(
    val searchMemberByEmail: String = "2",
    val id: Int = 3,
    val name: String = "2",
    val email: String = "2",
    val password: String = "2",
    val kioskIds: List<Int> = listOf(),
    val passwordRepeat: String = "",
    val showPassword: Boolean = false,
    val passwordImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordRepeatImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val passwordRepeatVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val showPasswordRepeat: Boolean = false,
)

sealed interface FindPasswordSideEffect {
    object NavigateToLoginScreen : FindPasswordSideEffect

    class Toast(
        val message: String,
    ) : FindPasswordSideEffect
}
