package com.stark.customhorizontalcalender.utlis

import android.content.res.Resources
import android.view.View

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback


val Int.toPx get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun View.click(action:()->Unit){
    this.setOnClickListener {
        action.invoke()
    }
}

fun ViewPager2.stateChangeListener(action: (state:Int) -> Unit){
    this.registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            action.invoke(state)
        }
    })
}