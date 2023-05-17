package com.stark.customhorizontalcalender.yearandmonthpicker.adapter

import android.annotation.SuppressLint
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.stark.customhorizontalcalender.R
import com.stark.customhorizontalcalender.databinding.CellYearBinding

class YearAdapter(val onYearSelected:(year:Int)->Unit) : Adapter<YearAdapter.YearViewHolder>() {

    private  val yearList = arrayListOf<YearModel>()
    private var previousSelectedItem = 0

    init {
        for (i in 2021..2038){ yearList.add(YearModel(year = i)) }
        yearList[0].isSelected = true
    }

    class YearViewHolder(val binding: CellYearBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
       return YearViewHolder(CellYearBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
      return yearList.size
    }

    override fun onBindViewHolder(holder: YearViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = yearList[position]
        holder.binding.textYear.text = item.year.toString()
        if(item.isSelected) {
            holder.binding.textYear.setTextColor(ContextCompat.getColor(holder.binding.textYear.context,
                R.color.primary))
            holder.binding.textYear.textSize = 28f
            animateChangeBoundTransition(holder.binding.root)
        }else{
            holder.binding.textYear.setTextColor(ContextCompat.getColor(holder.binding.textYear.context,
                R.color.colorDarkGrey))
            holder.binding.textYear.textSize = 24f
            animateChangeBoundTransition(holder.binding.root)
        }
        holder.binding.textYear.setOnClickListener {
            if(previousSelectedItem!=-1){
                yearList[previousSelectedItem].isSelected = false
                notifyItemChanged(previousSelectedItem)
            }
            previousSelectedItem = position
            item.isSelected = true
            notifyItemChanged(position)
            onYearSelected(item.year)
        }
    }

   private fun animateChangeBoundTransition(view:ViewGroup){
        val transition = ChangeBounds()
        TransitionManager.beginDelayedTransition(view,transition)
    }

}



data class YearModel(
    val year:Int,
    var isSelected:Boolean=false
)