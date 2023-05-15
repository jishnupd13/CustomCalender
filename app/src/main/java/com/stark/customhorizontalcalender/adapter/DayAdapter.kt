package com.stark.customhorizontalcalender.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.stark.customhorizontalcalender.R
import com.stark.customhorizontalcalender.databinding.CellBufferBinding
import com.stark.customhorizontalcalender.databinding.CellDayTitleBinding
import com.stark.customhorizontalcalender.databinding.CellDaysBinding
import com.stark.customhorizontalcalender.model.CurrentDateInstance
import com.stark.customhorizontalcalender.model.DayModel
import com.stark.customhorizontalcalender.model.DayViewType
import com.stark.customhorizontalcalender.utlis.click
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.IllegalArgumentException

class DayAdapter(
    val list:ArrayList<DayModel>,
    val currentSelectedData:(day:Date?,selectionStatus:Boolean)->Unit
) : Adapter<ViewHolder>() {

    val todayDate = Calendar.getInstance().time

    init {
        list.map {
            if (compareDates(it.day)){
                it.isDaySelected = true
            } else if(compareRangeDates(it.day)){
                it.isDaySelected = true
            }else
                it.isDaySelected = compareWithInRange(it.day)
        }
    }

    inner class DayViewHolder(private val binding: CellDaysBinding):ViewHolder(binding.root){

        fun onBind(item:DayModel,position: Int) = binding.apply {
            textDay.text = toSimpleString(item.day)

            if(compareDateWithToday(item.day) || item.day.after(todayDate)){
                textDay.setTextColor(ContextCompat.getColor(textDay.context,R.color.black))
            }else{
                textDay.setTextColor(ContextCompat.getColor(textDay.context,R.color.colorCalenderUnSelected))
            }


            if(item.isDaySelected)
                root.setBackgroundResource(R.drawable.bg_gray_circle)
            else
                root.setBackgroundResource(0)


            root.click {
                if(item.isDaySelected) {
                    item.isDaySelected = false
                    notifyItemChanged(position)
                    currentSelectedData.invoke(item.day,false)
                }else{
                    item.isDaySelected = true
                    notifyItemChanged(position)
                    currentSelectedData.invoke(item.day,true)
                }
            }
        }

       @SuppressLint("SimpleDateFormat")
       private fun toSimpleString(date: Date?) = with(date ?: Date()) {
            SimpleDateFormat("dd").format(this)
        }
    }

    inner class DayTitleViewHolder(val binding: CellDayTitleBinding):ViewHolder(binding.root){
        fun onBind(item:DayModel) = binding.apply {
            textDay.text = item.headerTitle
        }
    }

    inner class DayBufferViewHolder(val binding: CellBufferBinding):ViewHolder(binding.root){
        fun onBind(item:DayModel) = binding.apply {
            Log.e("status","${item.dayViewType}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when(viewType){

            1 -> DayTitleViewHolder(
                CellDayTitleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )

            2 -> DayBufferViewHolder(
                CellBufferBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )

            3 -> DayViewHolder(
                CellDaysBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            )

            else -> throw IllegalArgumentException("Illegal argument exception")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
           1-> (holder as DayTitleViewHolder).onBind(list[position])
           2 -> (holder as DayBufferViewHolder).onBind(list[position])
           3-> (holder as DayViewHolder).onBind(list[position],position)
       }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].dayViewType){
            DayViewType.CALENDER_HEADER-> 1
            DayViewType.BUFFER-> 2
            DayViewType.DATE-> 3
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun compareDates(date: Date):Boolean{
        return try {
            val fmt = SimpleDateFormat("yyyyMMdd")
            fmt.format(date) == CurrentDateInstance.currentDateInstance?.let { fmt.format(it) }
        }catch (e: Exception){
            false
        }
    }

     /**
      *  To Compare the dates
      *  Status 0 is equal
      *  1 is greater
      *  -1 is lesser
      * */
    private fun checkAndCompareDates(date1: Date,date2: Date):Int{
        return if(date1 == date2)
            0
        else if(date1.after(date2))
            -1
        else
            1
    }

    @SuppressLint("SimpleDateFormat")
    private fun compareRangeDates(date: Date):Boolean{
        return try {
            val fmt = SimpleDateFormat("yyyyMMdd")
            fmt.format(date) == CurrentDateInstance.rangeMaxDate?.let { fmt.format(it) }
        }catch (e: Exception){
            false
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun compareDateWithToday(date: Date):Boolean{
        return try {
            val fmt = SimpleDateFormat("yyyyMMdd")
            fmt.format(date) == todayDate?.let { fmt.format(it) }
        }catch (e: Exception){
            false
        }
    }

    private fun compareWithInRange(date: Date):Boolean{
        return if(CurrentDateInstance.currentDateInstance!=null && CurrentDateInstance.rangeMaxDate != null){
            date.after(CurrentDateInstance.currentDateInstance) && date.before(CurrentDateInstance.rangeMaxDate)
        }else false
    }

}