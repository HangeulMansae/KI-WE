package com.kiwe.manager.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiwe.manager.ui.theme.KIWEAndroidTheme
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUpScreen: () -> Unit,
    onNavigateToFindPassWordScreen: () -> Unit,
) {
//    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Toast ->
                Toast
                    .makeText(
                        context,
                        sideEffect.message,
                        Toast.LENGTH_SHORT,
                    ).show()

            LoginSideEffect.NavigateToMainActivity -> {
//                context.startActivity(
//                    Intent(
//                        context,
//                        MainActivity::class.java,
//                    ).apply {
//                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    },
//                )
            }
        }
    }

    LoginScreen(
        onNavigateToSignUpScreen = onNavigateToSignUpScreen,
        onNavigateToFindPassWordScreen = onNavigateToFindPassWordScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    onNavigateToSignUpScreen: () -> Unit,
    onNavigateToFindPassWordScreen: () -> Unit,
) {
    Surface(
        modifier = Modifier.systemBarsPadding(),
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Connected",
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(
                    text = "Your favorite social network",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Column(
                modifier =
                    Modifier
                        .padding(top = 24.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(top = 36.dp),
                    text = "Log in Screen",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Id",
                    style = MaterialTheme.typography.labelLarge,
                )
//                FCTextField(
//                    modifier =
//                        Modifier
//                            .padding(top = 8.dp)
//                            .fillMaxWidth(),
//                    value = id,
//                    onValueChange = onIdChange,
//                )
//
//                Text(
//                    modifier = Modifier.padding(top = 16.dp),
//                    text = "Password",
//                    style = MaterialTheme.typography.labelLarge,
//                )
//                FCTextField(
//                    modifier =
//                        Modifier
//                            .padding(top = 8.dp)
//                            .fillMaxWidth(),
//                    value = password,
//                    visualTransformation = PasswordVisualTransformation(),
//                    onValueChange = onPasswordChange,
//                )
//                FCButton(
//                    modifier =
//                        Modifier
//                            .padding(top = 24.dp)
//                            .fillMaxWidth(),
//                    text = "로그인",
//                    onClick = onLoginClick,
//                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier =
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 24.dp)
                            .clickable(onClick = onNavigateToSignUpScreen),
                ) {
                    Text(text = "Don't have an account?")
                    Text(text = "Sign up", color = MaterialTheme.colorScheme.primary)
                }
                Text(
                    modifier = Modifier.clickable(onClick = onNavigateToFindPassWordScreen),
                    text = "FindPassWord",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    KIWEAndroidTheme {
        LoginScreen(
            onNavigateToSignUpScreen = {},
            onNavigateToFindPassWordScreen = {},
        )
    }
}
