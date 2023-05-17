package com.stark.customhorizontalcalender.yearandmonthpicker.adapter


import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.stark.customhorizontalcalender.R
import com.stark.customhorizontalcalender.databinding.CellMonthBinding
import com.stark.customhorizontalcalender.utlis.invisible
import com.stark.customhorizontalcalender.utlis.show


class MonthAdapter(val onMonthSelected:(item:Month)->Unit): Adapter<MonthAdapter.MonthViewHolder>() {

   private val monthList = arrayListOf<Month>()
   private var previousSelectedPosition = 0
    init {
        monthList.add(Month(id = 1, month = "Jan", isSelected = true))
        monthList.add(Month(id = 2, month = "Feb"))
        monthList.add(Month(id = 3, month = "Mar"))
        monthList.add(Month(id = 4, month = "Apr"))
        monthList.add(Month(id = 5, month = "May"))
        monthList.add(Month(id = 6, month = "Jun"))
        monthList.add(Month(id = 7, month = "Jul"))
        monthList.add(Month(id = 8, month = "Aug"))
        monthList.add(Month(id = 9, month = "Sep"))
        monthList.add(Month(id = 10, month = "Oct"))
        monthList.add(Month(id = 11, month = "Nov"))
        monthList.add(Month(id = 12, month = "Dec"))

        previousSelectedPosition = 0
    }

    class MonthViewHolder(val binding: CellMonthBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder(CellMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
       return monthList.size
    }

    override fun onBindViewHolder(holder: MonthViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = monthList[position]
        holder.binding.textMonth.text = item.month
        holder.binding.root.setOnClickListener {
            onMonthSelected(item)
        }
        if(item.isSelected){
            holder.binding.backgroundCircle.show()
            holder.binding.textMonth.setTextColor(ContextCompat.getColor(holder.binding.textMonth.context,
                R.color.white))
            animateChangeBoundTransition(holder.binding.root)
        }else{
            holder.binding.backgroundCircle.invisible()
            holder.binding.textMonth.setTextColor(ContextCompat.getColor(holder.binding.textMonth.context,
                R.color.black))
            animateChangeBoundTransition(holder.binding.root)
        }

        holder.binding.root.setOnClickListener {
            if(previousSelectedPosition!=-1){
                monthList[previousSelectedPosition].isSelected = false
                notifyItemChanged(previousSelectedPosition)
                onMonthSelected.invoke(item)
            }
            previousSelectedPosition = position
            item.isSelected = true
            notifyItemChanged(position)
            onMonthSelected(item)
        }
    }

    private fun animateChangeBoundTransition(view:ViewGroup){
        TransitionManager.beginDelayedTransition(view)
    }
}

data class Month(
    val id:Int,
    val month:String,
    var isSelected:Boolean = false
)