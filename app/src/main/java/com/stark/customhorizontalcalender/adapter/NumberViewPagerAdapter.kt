package com.stark.customhorizontalcalender.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stark.customhorizontalcalender.databinding.CellViewPagerBinding
import com.stark.customhorizontalcalender.model.NumberModel
import java.util.Date

class NumberViewPagerAdapter(
    val list: ArrayList<NumberModel>,
    val currentSelectedData:(day:Date?)->Unit,
    val onDateChangeListener:(year:String,month:String)->Unit
) : Adapter<NumberViewPagerAdapter.NumberViewHolder>() {

   inner class NumberViewHolder(val binding: CellViewPagerBinding):ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun  onBind(item:NumberModel) = binding.apply {
            val dayAdapter = DayAdapter(item.dateList, currentSelectedData = currentSelectedData)
            recyclerviewCalender.adapter = dayAdapter
            textDayInfo.text = "${item.year} ${item.month}"
            onDateChangeListener(item.year.toString(),item.month)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberViewHolder(
            CellViewPagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
       holder.onBind(list[position % list.size])
    }
}