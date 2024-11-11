package com.kiwe.manager.ui.home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.kiwe.domain.exception.APIException
import com.kiwe.domain.model.LogoutParam
import com.kiwe.domain.usecase.manager.login.ClearTokenUseCase
import com.kiwe.domain.usecase.manager.login.LogoutUseCase
import com.kiwe.domain.usecase.manager.token.GetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

private const val TAG = "HomeViewModel 싸피"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getTokenUseCase: GetTokenUseCase,
        private val logoutUseCase: LogoutUseCase,
        private val clearTokenUseCase: ClearTokenUseCase,
    ) : ViewModel(),
        ContainerHost<HomeState, HomeSideEffect> {
        override val container: Container<HomeState, HomeSideEffect> =
            container(
                initialState = HomeState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { _, throwable ->
                            intent {
                                if (throwable is APIException) {
                                    postSideEffect(
                                        HomeSideEffect.Toast(
                                            "${throwable.code} : " +
                                                (throwable.message ?: "알수 없는 에러"),
                                        ),
                                    )
                                } else {
                                    postSideEffect(
                                        HomeSideEffect.Toast(
                                            throwable.message ?: "알수 없는 에러",
                                        ),
                                    )
                                }
                            }
                        }
                },
            )

        fun onLogout() =
            intent {
                val token = getTokenUseCase()

                if (token != null) {
                    logoutUseCase(
                        LogoutParam(
                            token.refreshToken,
                        ),
                    ).getOrThrow()
                    Log.d(TAG, "onLogout: 여기까지는 되나?")
                    clearTokenUseCase()
                    Log.d(TAG, "onLogout: clearToken도 되나?")
                    postSideEffect(HomeSideEffect.Toast(message = "로그아웃 되었습니다!"))
                    postSideEffect(HomeSideEffect.NavigateToLoginScreen)
                } else {
                    postSideEffect(HomeSideEffect.Toast(message = "정상적인 로그아웃에 실패했습니다!"))
                }
            }

        fun onNameChange(name: String) =
            blockingIntent {
                reduce {
                    state.copy(name = name)
                }
            }

        fun onIdChange(id: String) =
            blockingIntent {
                reduce {
                    state.copy(id = id)
                }
            }

        fun onPasswordChange(password: String) =
            blockingIntent {
                reduce {
                    state.copy(password = password)
                }
            }

        fun onPasswordRepeatChange(repeatPassword: String) =
            blockingIntent {
                reduce {
                    state.copy(passwordRepeat = repeatPassword)
                }
            }

        fun onShowPasswordChange() =
            intent {
                reduce {
                    if (!state.showPassword) {
                        state.copy(
                            showPassword = true,
                            passwordImageVector = Icons.Filled.Visibility,
                            passwordVisualTransformation = VisualTransformation.None,
                        )
                    } else {
                        state.copy(
                            showPassword = false,
                            passwordImageVector = Icons.Filled.VisibilityOff,
                            passwordVisualTransformation = PasswordVisualTransformation(),
                        )
                    }
                }
            }

        fun onShowPasswordRepeatChange() =
            intent {
                reduce {
                    if (!state.showPasswordRepeat) {
                        state.copy(
                            showPasswordRepeat = true,
                            passwordRepeatImageVector = Icons.Filled.Visibility,
                            passwordRepeatVisualTransformation = VisualTransformation.None,
                        )
                    } else {
                        state.copy(
                            showPasswordRepeat = false,
                            passwordRepeatImageVector = Icons.Filled.VisibilityOff,
                            passwordRepeatVisualTransformation = PasswordVisualTransformation(),
                        )
                    }
                }
            }
    }

@Immutable
data class HomeState(
    val name: String = "",
    val id: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val showPassword: Boolean = false,
    val passwordImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordRepeatImageVector: ImageVector = Icons.Filled.VisibilityOff,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val passwordRepeatVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val showPasswordRepeat: Boolean = false,
)

sealed interface HomeSideEffect {
    object NavigateToLoginScreen : HomeSideEffect

    class Toast(
        val message: String,
    ) : HomeSideEffect
}
