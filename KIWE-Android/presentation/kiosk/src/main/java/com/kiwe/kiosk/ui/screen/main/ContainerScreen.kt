package com.kiwe.kiosk.ui.screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kiwe.kiosk.R
import com.kiwe.kiosk.main.MainViewModel
import com.kiwe.kiosk.ui.theme.KIWEAndroidTheme
import com.kiwe.kiosk.ui.theme.KioskBackgroundBrush
import com.kiwe.kiosk.utils.MainEnum
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ContainerScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val state = viewModel.collectAsState().value
    ContainerScreen(
        page = state.page,
        mode = state.mode,
        onBackClick = onBackClick,
        content = content,
    )
}

@Composable
private fun ContainerScreen(
    page: Int,
    mode: MainEnum.KioskMode,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            mode // TODO
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                StepIndicator(page)
                Box(modifier = Modifier.clip(CircleShape).size(68.dp).padding(vertical = 8.dp)) {
                    Image(
                        modifier =
                            Modifier.fillMaxSize(),
                        painter =
                            rememberAsyncImagePainter(
                                model = R.drawable.ic_launcher_playstore,
                                contentScale = ContentScale.Crop,
                            ),
                        contentDescription = "logo",
                    )
                }
            }
        },
        content = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(KioskBackgroundBrush)
                        .padding(it),
                content = {
                    content()
                },
            )
        },
        bottomBar = {
            PreviousButton(onBackClick = onBackClick)
        },
    )
}

@Composable
fun PreviousButton(onBackClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 120.dp, vertical = 20.dp),
        onClick = onBackClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2e7d32),
                contentColor = Color.White,
            ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "이전으로",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
        )
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = listOf("메뉴", "주문", "결제", "확인")
    if (currentStep >= 0) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEachIndexed { index, step ->
                StepItem(
                    title = step,
                    isActive = index == currentStep,
                    isFirst = index == 0,
                    isLast = index == steps.size - 1,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun StepItem(
    title: String,
    isActive: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    isFirst and isLast // TODO
    // 색상조정 필요
    val backgroundColor = if (isActive) Color(0xFF7b4f3f) else Color(0xFFede0d4)
    val textColor = if (isActive) Color.White else Color.Gray

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier =
                Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
        ) {
            val path =
                Path().apply {
                    moveTo(0f, 0f) // 시작점 (왼쪽 상단)
                    lineTo(size.width - 20f, 0f) // 상단 직선
                    lineTo(size.width, size.height / 2) // 오른쪽 중간의 뾰족한 부분
                    lineTo(size.width - 20f, size.height) // 하단 직선
                    lineTo(0f, size.height) // 왼쪽 하단
                    lineTo(20f, size.height / 2) // 왼쪽 중간의 뾰족한 부분 (마지막 단계는 제외)
                    close()
                }

            drawPath(path = path, color = backgroundColor)
        }
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
        )
    }
}

@Composable
@Preview
fun ContainerScreenPreview() {
    KIWEAndroidTheme {
        ContainerScreen(
            page = 0,
            mode = MainEnum.KioskMode.MANUAL,
            onBackClick = {},
            content = {},
        )
    }
}
