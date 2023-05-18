package com.stark.customhorizontalcalender.utlis

import android.content.res.Resources
import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun RecyclerView.onScrollDoneGetPosition(onScrollUpdate: (Int) -> Unit) {
    this.addOnScrollListener(object :
        RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                }
                AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                    print("When User Done it's Scroll")
                    val currentPosition =
                        (this@onScrollDoneGetPosition.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    onScrollUpdate.invoke(currentPosition)
                }
                AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                }
            }
        }
    })
}

