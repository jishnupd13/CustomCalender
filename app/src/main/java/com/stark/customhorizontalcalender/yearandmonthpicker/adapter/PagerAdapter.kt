package com.stark.customhorizontalcalender.yearandmonthpicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

import com.stark.customhorizontalcalender.databinding.CellMonthItemBinding
import com.stark.customhorizontalcalender.databinding.CellYearItemBinding
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.models.CellType
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.models.ViewPagerItem


class PagerAdapter(
    val list: List<ViewPagerItem>,
    val onYearSelected:(year:Int)->Unit,
    val onMonthSelected:(month: Month)->Unit
): Adapter<RecyclerView.ViewHolder>() {

    class MonthItemViewHolder(val binding: CellMonthItemBinding) : RecyclerView.ViewHolder(binding.root)
    class YearItemViewHolder(val binding: CellYearItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0-> MonthItemViewHolder(CellMonthItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            1-> YearItemViewHolder(CellYearItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0->{
                val binding = (holder as MonthItemViewHolder).binding
                val monthAdapter = MonthAdapter(onMonthSelected = {
                    onMonthSelected.invoke(it)
                })
                binding.recyclerviewMonth.adapter = monthAdapter
            }

            1->{
                val binding = (holder as YearItemViewHolder).binding
                val yearAdapter = YearAdapter(onYearSelected = {
                    onYearSelected(it)
                })
                binding.recyclerviewYear.adapter = yearAdapter
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].cellType){
            CellType.Month-> CellType.Month.ordinal
            CellType.Year-> CellType.Year.ordinal
        }
    }

}