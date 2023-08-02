package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.qixiaoo.crystallite.ui.common.shadow
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderedCover(
    modifier: Modifier = Modifier,
    title: String,
    seqNo: Int?,
    cover: String?,
    onClick: () -> Unit = {},
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val ratioWidth = 3f
    val ratioHeight = 4f
    val borderRadius = 6.dp

    val gradientColors = listOf(Color.Transparent, Color(0f, 0f, 0f, 0.6f))

    Box(
        modifier = Modifier
            .shadow(
                color = Color(0f, 0f, 0f, 0.4f),
                offsetX = 0.dp,
                offsetY = (6).dp,
                blurRadius = 10.dp
            )
            .clip(RoundedCornerShape(borderRadius))
            .clickable(onClick = onClick)
            .aspectRatio(ratioWidth / ratioHeight)
            .then(modifier)
    ) {
        // cover
        AsyncImage(
            model = cover,
            contentDescription = "the cover of \"$title\"",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // sequence number
        if (seqNo != null) {
            Badge(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            ) {
                Text(text = seqNo.toString(), modifier = Modifier.padding(start = 4.dp, end = 4.dp))
            }
        }

        // title
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.matchParentSize()
            ) {
                Box(
                    modifier = Modifier
                        .background(brush = Brush.verticalGradient(gradientColors))
                        .fillMaxWidth()
                ) {
                    Text(
                        title,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        style = typography.titleSmall,
                        modifier = Modifier
                            .padding(
                                start = 4.dp, end = 4.dp, top = 22.5.dp, bottom = 4.dp
                            )
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderedCoverPreview() {
    CrystalliteTheme {
        OrderedCover(
            title = "This is A Very Very Very Very Long Title",
            seqNo = 1,
            cover = "https://meo.comick.pictures/7XLwv0.jpg"
        )
    }
}