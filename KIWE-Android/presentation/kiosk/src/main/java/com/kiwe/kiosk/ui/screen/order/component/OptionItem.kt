package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiwe.kiosk.R
import com.kiwe.kiosk.ui.theme.letterSpacing

@Composable
fun OptionItem(
    optionName: String,
    optionPrice: Int,
    optionImg: String,
    selected: Boolean,
    onRadioOptionClick: () -> Unit,
) {
    Button(
        modifier =
            if (selected) {
                Modifier
                    .size(80.dp)
                    .aspectRatio(1F)
                    .clip(RoundedCornerShape(5.dp))
                    .border(
                        border = BorderStroke(2.dp, color = Color.Red),
                        shape = RoundedCornerShape(5.dp),
                    )
            } else {
                Modifier
                    .size(80.dp)
                    .aspectRatio(1F)
                    .clip(RoundedCornerShape(5.dp))
            },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.White,
            ),
        shape = RoundedCornerShape(5.dp),
        onClick = onRadioOptionClick,
        contentPadding = PaddingValues(0.dp),
    ) {
        Column(
            modifier = Modifier.aspectRatio(ratio = 1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .aspectRatio(1F)
                        .weight(1F),
                model = optionImg,
                contentDescription = "옵션 이미지",
            )
            Box(
                modifier = Modifier.weight(1F),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = optionName,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                    )

                    Text(
                        text =
                            buildAnnotatedString {
                                withStyle(
                                    style =
                                        SpanStyle(
                                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                            fontSize = 16.sp,
                                            letterSpacing = letterSpacing,
                                            color = colorResource(R.color.KIWE_orange1),
                                        ),
                                ) {
                                    if (optionPrice >= 0) {
                                        append("+")
                                    }
                                }
                                withStyle(
                                    style =
                                        SpanStyle(
                                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                            fontSize = 16.sp,
                                            letterSpacing = letterSpacing,
                                            color = colorResource(R.color.KIWE_orange1),
                                        ),
                                ) {
                                    append(optionPrice.toString())
                                }
                                withStyle(
                                    style =
                                        SpanStyle(
                                            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                            fontSize = 16.sp,
                                            color = Color.Black,
                                            letterSpacing = letterSpacing,
                                        ),
                                ) {
                                    append("원")
                                }
                            },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OptionItemPreview() {
    OptionItem(
        "설탕 추가",
        300,
        "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
        false,
        {},
    )
}
