package com.kiwe.kiosk.ui.screen.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.ShoppingCartItem
import com.kiwe.kiosk.ui.screen.order.component.OrderDialog
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.ui.theme.letterSpacing
import com.kiwe.kiosk.utils.dropShadow
import org.orbitmvi.orbit.compose.collectAsState
import java.util.Locale

@Composable
fun ShoppingCartDialog(
    viewModel: ShoppingCartViewModel,
    goOrderList: () -> Unit,
    onClose: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    OrderDialog {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            textAlign = TextAlign.Center,
            text = "장바구니",
            style = Typography.titleLarge,
        )
        if (state.shoppingCartItem.isEmpty()) {
            Box(
                modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.6F),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "상품을 추가해주세요",
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.KIWE_gray1),
                )
            }
        } else {
            ShoppingCartDialog(
                state.shoppingCartItem,
                viewModel::onDeleteItem,
                viewModel::onAddItem,
                viewModel::onMinusItem,
            )
        }
        Column {
            var cost = 0
            for (shoppingCartItem in state.shoppingCartItem) {
                cost += shoppingCartItem.count * shoppingCartItem.totalPrice
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "총 ${state.shoppingCartItem.size}건",
                )
                Text(
                    text = String.format(Locale.getDefault(), "%,d원", cost),
                )
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Button(
                    modifier = Modifier.weight(1F),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        onClose()
                    },
                    colors =
                        ButtonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(R.color.KIWE_gray1),
                            disabledContainerColor = colorResource(R.color.KIWE_gray1),
                            disabledContentColor = Color.White,
                        ),
                ) {
                    Text(
                        text = "닫기",
                        style = Typography.bodyLarge,
                    )
                }
                Spacer(modifier = Modifier.weight(0.3F))
                Button(
                    modifier = Modifier.weight(1F),
                    enabled = state.shoppingCartItem.isNotEmpty(),
                    shape = RoundedCornerShape(10.dp),
                    onClick = goOrderList,
                    colors =
                        ButtonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(R.color.KIWE_green5),
                            disabledContainerColor = colorResource(R.color.KIWE_silver1),
                            disabledContentColor = Color.White,
                        ),
                ) {
                    Text(
                        text = "결제",
                        style = Typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoppingCartDialog(
    shoppingCartItemList: List<ShoppingCartItem>,
    onDeleteItem: (String) -> Unit,
    onAddItem: (String, Map<String, Pair<String, Int>>) -> Unit,
    onMinusItem: (String, Map<String, Pair<String, Int>>) -> Unit,
) {
    Column {
        var cost = 0
        shoppingCartItemList.forEach {
            cost += it.totalPrice * it.count
        }
        LazyColumn(
            modifier =
                Modifier.height(
                    LocalConfiguration.current.screenHeightDp.dp * 0.6F,
                ),
        ) {
            items(shoppingCartItemList.size) {
                Card(
                    modifier =
                        Modifier
                            .background(Color.Transparent)
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                            .dropShadow(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Black.copy(alpha = 0.25F),
                                offsetX = 2.dp,
                                offsetY = (2).dp,
                                spread = 0.dp,
                            ).clip(RoundedCornerShape(10.dp)),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .background(Color.White)
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AsyncImage(
                            model = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                            contentDescription = "메뉴 이미지 주소",
                            modifier = Modifier.weight(1F),
                        )
                        ShoppingCartDataInfo(
                            shoppingCartItemList[it],
                            modifier = Modifier.weight(2F),
                            onDeleteItem = onDeleteItem,
                            onAddItem = onAddItem,
                            onMinusItem = onMinusItem,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShoppingCartDataInfo(
    item: ShoppingCartItem,
    modifier: Modifier = Modifier,
    onDeleteItem: (String) -> Unit,
    onAddItem: (String, Map<String, Pair<String, Int>>) -> Unit,
    onMinusItem: (String, Map<String, Pair<String, Int>>) -> Unit,
) {
    Column(
        modifier = modifier.padding(start = 15.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.menuTitle,
                style = Typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
            )
            Image(
                painter = painterResource(R.drawable.close),
                modifier =
                    Modifier
                        .height(20.dp)
                        .clickable {
                            onDeleteItem(item.menuTitle)
                        },
                contentDescription = "삭제 버튼",
            )
        }
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
            text =
                buildAnnotatedString {
                    item.menuRadioOption.onEachIndexed { index, entry ->
                        withStyle(
                            style =
                                SpanStyle(
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 16.sp,
                                    letterSpacing = letterSpacing,
                                ),
                        ) {
                            append(entry.key + ":")
                        }

                        withStyle(
                            style =
                                SpanStyle(
                                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                                    fontSize = 16.sp,
                                    letterSpacing = letterSpacing,
                                ),
                        ) {
                            append(entry.value.first)
                        }
                        if (index != item.menuRadioOption.size - 1) {
                            withStyle(
                                style =
                                    SpanStyle(
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 16.sp,
                                        letterSpacing = letterSpacing,
                                    ),
                            ) {
                                append(" | ")
                            }
                        }
                    }
                },
        )

        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = String.format(Locale.getDefault(), "%,d원", item.totalPrice * item.count),
                style = Typography.bodyMedium,
                color = Color.Black,
            )
            Row(
                modifier = Modifier.weight(1F),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.count != 1) {
                    Image(
                        modifier =
                            Modifier
                                .height(20.dp)
                                .clickable {
                                    onMinusItem(item.menuTitle, item.menuRadioOption)
                                },
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "감소 버튼",
                    )
                } else {
                    Image(
                        modifier =
                            Modifier
                                .height(20.dp)
                                .alpha(0F),
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "감소 버튼",
                    )
                }
                Text(
                    text = item.count.toString(),
                    style = Typography.bodySmall,
                    color = Color.Black,
                )
                Image(
                    modifier =
                        Modifier
                            .height(20.dp)
                            .clickable {
                                onAddItem(item.menuTitle, item.menuRadioOption)
                            },
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "추가 버튼",
                )
            }
        }
    }
}

@Preview
@Composable
fun ShoppingCartDialogPreview() {
    ShoppingCartDialog(
        listOf(
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페",
//                menuPrice = 5000,
//                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페",
//                menuPrice = 5000,
//                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
            ),
            ShoppingCartItem(
                menuTitle = "딸111기쿠키프라페",
//                menuPrice = 5000,
//                menuImgUrl = "https://img.freepik.com/free-photo/black-coffee-cup_74190-7411.jpg",
                count = 99,
            ),
        ),
        {},
        { _, _ -> },
        { _, _ -> },
    )
}
