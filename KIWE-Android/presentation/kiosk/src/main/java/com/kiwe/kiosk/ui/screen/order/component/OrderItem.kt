package com.kiwe.kiosk.ui.screen.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kiwe.kiosk.R
import com.kiwe.kiosk.model.OrderItem
import com.kiwe.kiosk.ui.theme.Typography
import com.kiwe.kiosk.utils.dropShadow
import java.util.Locale

@Composable
fun OrderItem(
    orderItem: OrderItem,
    modifier: Modifier = Modifier,
    onClick: (String, Int) -> Unit,
) {
    Box(
        modifier =
            modifier
                .padding(end = 4.dp)
                .dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.25F),
                    offsetY = 4.dp,
                    offsetX = 4.dp,
                    spread = 0.dp,
                ).clip(RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .clickable { onClick(orderItem.menuTitle, orderItem.menuPrice) },
    ) {
        Column(
            modifier =
                Modifier
                    .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier =
                    Modifier
                        .weight(1F)
                        .padding(bottom = 10.dp),
                model = orderItem.menuImgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = "Translated description of what the image contains",
            )
            Text(
                text = orderItem.menuTitle,
                style = Typography.bodySmall,
                textAlign = TextAlign.Center,
            )
            Text(
                text = String.format(Locale.getDefault(), "%,d원", orderItem.menuPrice),
                color = colorResource(R.color.KIWE_orange1),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun OrderItemPreview() {
    OrderItem(
        OrderItem(
            menuTitle = "디카페인 카페모카",
            menuPrice = 4500,
            menuImgUrl = "https://example.com/image.jpg",
        ),
        onClick = { _, _ -> },
    )
}
