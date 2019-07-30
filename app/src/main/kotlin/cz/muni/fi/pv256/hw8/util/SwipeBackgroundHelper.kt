package cz.muni.fi.pv256.hw8.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import cz.muni.fi.pv256.hw8.R

class SwipeBackgroundHelper {

    companion object {

        private const val THRESHOLD = 2.5
        private const val OFFSET_PX = 20

        fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
            val drawCommand = createDrawCommand(viewItem, dX, iconResId)
            paintDrawCommand(drawCommand, canvas, dX, viewItem)
        }

        private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand {
            val context = viewItem.context
            var icon = ContextCompat.getDrawable(context, iconResId)!!
            icon = DrawableCompat.wrap(icon).mutate()
            icon.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white),
                    PorterDuff.Mode.SRC_IN)
            val backgroundColor = getBackgroundColor(R.color.red, R.color.grey, dX, viewItem)
            return DrawCommand(icon, backgroundColor)
        }

        private fun getBackgroundColor(firstColor: Int, secondColor: Int, dX: Float, viewItem: View) =
                when (willActionBeTriggered(dX, viewItem.width)) {
                    true -> ContextCompat.getColor(viewItem.context, firstColor)
                    false -> ContextCompat.getColor(viewItem.context, secondColor)
                }

        private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
            drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
            drawIcon(canvas, viewItem, dX, drawCommand.icon)
        }

        private fun drawIcon(canvas: Canvas, viewItem: View, dX: Float, icon: Drawable) {
            val topMargin = calculateTopMargin(icon, viewItem)
            icon.bounds = getStartContainerRectangle(viewItem, icon.intrinsicWidth, topMargin, OFFSET_PX, dX)
            icon.draw(canvas)
        }

        private fun getStartContainerRectangle(viewItem: View, iconWidth: Int, topMargin: Int, sideOffset: Int, dx: Float) = with(viewItem) {
            Rect(
                    right + dx.toInt() + sideOffset,
                    top + topMargin,
                    right + dx.toInt() + iconWidth + sideOffset,
                    bottom - topMargin
            )
        }

        private fun calculateTopMargin(icon: Drawable, viewItem: View) = (viewItem.height - icon.intrinsicHeight) / 2

        private fun drawBackground(canvas: Canvas, viewItem: View, dX: Float, backgroundColor: Int) {
            val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }
            val backgroundRectangle = getBackgroundRectangle(viewItem, dX)
            canvas.drawRect(backgroundRectangle, backgroundPaint)
        }

        private fun getBackgroundRectangle(viewItem: View, dX: Float) = with(viewItem) {
            RectF(right.toFloat() + dX, top.toFloat(), right.toFloat(), bottom.toFloat())
        }

        private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean = Math.abs(dX) >= viewWidth / THRESHOLD
    }

    private class DrawCommand constructor(val icon: Drawable, val backgroundColor: Int)
}
