package com.stark.customhorizontalcalender.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stark.customhorizontalcalender.databinding.CellViewPagerBinding
import com.stark.customhorizontalcalender.model.NumberModel
import com.stark.customhorizontalcalender.utlis.hide
import com.stark.customhorizontalcalender.utlis.show
import java.util.Date

class NumberViewPagerAdapter(
    val list: ArrayList<NumberModel>,
    val currentSelectedData:(day:Date?)->Unit,
    val onDateChangeListener:(year:String,month:String)->Unit
) : Adapter<NumberViewPagerAdapter.NumberViewHolder>() {

    private var previousExpandPosition = -1

   inner class NumberViewHolder(val binding: CellViewPagerBinding):ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun  onBind(item:NumberModel,position: Int) = binding.apply {
            val dayAdapter = DayAdapter(item.dateList, currentSelectedData = currentSelectedData)
            recyclerviewCalender.adapter = dayAdapter
            textDayInfo.text = "${item.year} ${item.month}"
            onDateChangeListener(item.year.toString(),item.month)

            if(item.isExpanded){
                recyclerviewCalender.show()
            }else{
                recyclerviewCalender.hide()
            }

            rootLayout.setOnClickListener {
                if(item.isExpanded){
                    item.isExpanded = false
                    previousExpandPosition = -1
                    notifyItemChanged(position)
                }else{

                    if (previousExpandPosition != -1){
                        list[previousExpandPosition].isExpanded = false
                        notifyItemChanged(previousExpandPosition)
                    }

                    item.isExpanded = true
                    previousExpandPosition = position
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberViewHolder(
            CellViewPagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
       holder.onBind(list[position],position)
    }
}