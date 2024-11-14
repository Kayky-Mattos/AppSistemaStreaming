package com.example.sistemastreaming
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val scaleFactor = Math.max(0.85f, 1 - Math.abs(position))
            val vertMargin = height * (1 - scaleFactor) / 2
            val horzMargin = width * (1 - scaleFactor) / 2
            translationX = if (position < 0) horzMargin - vertMargin / 2 else horzMargin + vertMargin / 2
            scaleX = scaleFactor
            scaleY = scaleFactor
            alpha = (0.5f + (scaleFactor - 0.85f) / 0.15f * (1 - 0.5f))
        }
    }
}