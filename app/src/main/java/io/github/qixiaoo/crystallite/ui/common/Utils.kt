package io.github.qixiaoo.crystallite.ui.common

import android.content.Context
import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.common.PICTURES_SITE
import io.github.qixiaoo.crystallite.data.model.Comic
import io.github.qixiaoo.crystallite.data.model.MdCover
import io.github.qixiaoo.crystallite.data.model.ProgressStatus
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import kotlin.math.PI

fun Float.pxToDp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}

fun Float.dpToPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

fun Int.pxToDp(context: Context): Float {
    return this / context.resources.displayMetrics.density
}

fun Int.dpToPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter =
                (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
        }
        frameworkPaint.color = color.toArgb()

        val leftPixel = offsetX.toPx()
        val topPixel = offsetY.toPx()
        val rightPixel = size.width + leftPixel
        val bottomPixel = size.height + topPixel

        canvas.drawRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            paint = paint,
        )
    }
})

fun getCoverUrl(comic: Comic): String? {
    val firstCover = comic.mdCovers.getOrNull(0)?.b2key
    return if (firstCover == null) null else PICTURES_SITE.plus(firstCover)
}

fun getCoverUrl(mdCovers: List<MdCover>): String? {
    val firstCover = mdCovers.getOrNull(0)?.b2key
    return if (firstCover == null) null else PICTURES_SITE.plus(firstCover)
}

fun ProgressStatus.toResourceId(): Int {
    return when (this) {
        ProgressStatus.ONGOING -> R.string.ongoing
        ProgressStatus.COMPLETED -> R.string.completed
        ProgressStatus.CANCELLED -> R.string.cancelled
        ProgressStatus.HIATUS -> R.string.hiatus
    }
}

fun ReadingMode.toResourceId(): Int {
    return when (this) {
        ReadingMode.LeftToRight -> R.string.ltr
        ReadingMode.RightToLeft -> R.string.rtl
    }
}

fun radToDeg(rad: Double): Double {
    return rad / PI * 180
}
