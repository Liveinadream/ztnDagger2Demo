package com.ztn.commom.utils

import android.graphics.*
import android.widget.ImageView
import com.ztn.common.utils.dip2px

/**
 * Created by 冒险者ztn on 2019-07-01.
 * 图片工具
 */
object FaceUtils {
    /**
     * 根据 ImageView 拿到一个对应的文字头像
     *
     * @param portrait 对应的 ImageView
     * @param name     名字
     * @param identity 身份
     * @return 对应的文字 bitmap
     */
    fun getDefaultPortrait(portrait: ImageView, name: String, identity: String): Bitmap {

        val width = portrait.width
        val height = portrait.height
        val face = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(face)


        //底色
        canvas.drawColor(Color.BLUE)

        val sub = if (name.length > 1) {
            2
        } else {
            name.length
        }
        val drawText = name.substring(0, sub)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = when (identity) {
            "Android 开发" -> Color.WHITE
            else -> Color.RED
        }

        paint.textSize = 12f.dip2px
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        val bounds = Rect(0, 0, face.width, face.height)
        val fontMetrics = paint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom
        val baseLineY = bounds.centerY() - top / 2 - bottom / 2
        canvas.drawText(drawText, bounds.centerX().toFloat(), baseLineY, paint)
        return createRoundCornerBitmap(face, (face.width / 2).toFloat())
    }

    /**
     * 根据原本的 bitmap 创建对应角度的 bitmap
     */
    fun createRoundCornerBitmap(bitmap: Bitmap, roundPx: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)

        val color = Color.BLUE
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }


}