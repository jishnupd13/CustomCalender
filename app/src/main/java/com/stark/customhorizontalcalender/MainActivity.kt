package com.stark.customhorizontalcalender

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.stark.customhorizontalcalender.adapter.NumberViewPagerAdapter
import com.stark.customhorizontalcalender.databinding.ActivityMainBinding
import com.stark.customhorizontalcalender.model.CurrentDateInstance
import com.stark.customhorizontalcalender.model.DayModel
import com.stark.customhorizontalcalender.model.DayViewType
import com.stark.customhorizontalcalender.model.NumberModel
import com.stark.customhorizontalcalender.utlis.DELAY
import com.stark.customhorizontalcalender.utlis.DELAY_CLICK_ACTION
import com.stark.customhorizontalcalender.utlis.click
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentItemPosition = 0
    private var realPosition = 0
    private var currentSelectedDate : Date? = Calendar.getInstance().time
    private val list = arrayListOf<NumberModel>()
    private lateinit var calenderViewPagerAdapter: NumberViewPagerAdapter
    private var rightClickStatus = false
    private var leftClickStatus = false

    private var calenderJob:Job? =null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfiniteViewPager()
        currentSelectedDate = CurrentDateInstance.currentDateInstance
    }


    private fun setInfiniteViewPager() {

        setInitialSetup()
        calenderViewPagerAdapter = NumberViewPagerAdapter(list, onDateChangeListener = { year, month ->
        },currentSelectedData = {day, selectionStatus ->

            if(selectionStatus){
                if(CurrentDateInstance.currentDateInstance != null && CurrentDateInstance.rangeMaxDate != null){
                    CurrentDateInstance.currentDateInstance = day
                    CurrentDateInstance.rangeMaxDate = null
                    calenderViewPagerAdapter.notifyItemChanged(realPosition)
                }else if(CurrentDateInstance.currentDateInstance ==null && CurrentDateInstance.rangeMaxDate == null){
                    CurrentDateInstance.currentDateInstance = day
                    CurrentDateInstance.rangeMaxDate = null
                    calenderViewPagerAdapter.notifyItemChanged(realPosition)
                }else if(CurrentDateInstance.currentDateInstance != null && CurrentDateInstance.rangeMaxDate==null){
                    val compareStatus = checkAndCompareDates(date1 = CurrentDateInstance.currentDateInstance!!, date2 = day!!)
                    if (compareStatus == -1){
                        val tmp = CurrentDateInstance.currentDateInstance
                        CurrentDateInstance.currentDateInstance = day
                        CurrentDateInstance.rangeMaxDate = tmp
                    }else{
                        CurrentDateInstance.rangeMaxDate = day
                    }
                    calenderViewPagerAdapter.notifyItemChanged(realPosition)
                }
            }else{
                /**
                 * User unselect the date
                 * */
                if(CurrentDateInstance.currentDateInstance != null && CurrentDateInstance.rangeMaxDate != null){
                    CurrentDateInstance.currentDateInstance = day
                    CurrentDateInstance.rangeMaxDate = null
                    calenderViewPagerAdapter.notifyItemChanged(realPosition)
                }else if(CurrentDateInstance.currentDateInstance!=null && CurrentDateInstance.rangeMaxDate==null){
                    CurrentDateInstance.currentDateInstance = null
                    CurrentDateInstance.rangeMaxDate = null
                    calenderViewPagerAdapter.notifyItemChanged(realPosition)
                }
            }

            calenderViewPagerAdapter.notifyItemChanged(realPosition+1)
            calenderViewPagerAdapter.notifyItemChanged(realPosition-1)
        })

        binding.apply {

            calenderRecyclerview.adapter = calenderViewPagerAdapter

            val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper = PagerSnapHelper()
            calenderRecyclerview.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(calenderRecyclerview)

            currentItemPosition = Int.MAX_VALUE / 2 - Math.ceil(list.size.toDouble() / 2).toInt()
            calenderRecyclerview.layoutManager?.scrollToPosition(currentItemPosition)



            imageArrowRight.click {
                lifecycleScope.launch(Dispatchers.Main){
                    if(!rightClickStatus){
                        realPosition += 1
                        rightClickStatus = true
                        delay(DELAY_CLICK_ACTION)
                        calenderRecyclerview.scrollToPosition(realPosition)
                        rightClickStatus = false
                    }
                }
            }


            imgArrowLeft.click {
                lifecycleScope.launch(Dispatchers.Main){
                    if(!leftClickStatus) {
                        realPosition -= 1
                        leftClickStatus = true
                        delay(DELAY_CLICK_ACTION)
                        calenderRecyclerview.scrollToPosition(realPosition)
                        leftClickStatus = false
                    }
                }
            }


        }


        binding.calenderRecyclerview.onScrollDoneGetPosition() { position ->
            setCalender(position)
        }

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

    private   fun setCalender(position: Int){

       if(calenderJob?.isActive == true)
           calenderJob?.cancel()

       calenderJob =  lifecycleScope.launch(Dispatchers.Main){
            currentItemPosition = position % list.size
            realPosition = position
            val format = SimpleDateFormat("MMM")


            val currentMonth = list[currentItemPosition].monthInInteger
            val currentYear = list[currentItemPosition].year

            val c = Calendar.getInstance()
            c.set(Calendar.MONTH,fetchCalenderMonth(currentMonth))
            c.set(Calendar.YEAR,currentYear)
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]+1

            val pC = Calendar.getInstance()
            pC.set(Calendar.MONTH,fetchCalenderMonth(currentMonth))
            pC.set(Calendar.YEAR,currentYear)
            pC.add(Calendar.MONTH, -1)
            val pYear = pC[Calendar.YEAR]
            val pMonth = pC[Calendar.MONTH]+1

            val nC = Calendar.getInstance()
            nC.set(Calendar.MONTH,fetchCalenderMonth(currentMonth))
            nC.set(Calendar.YEAR,currentYear)
            nC.add(Calendar.MONTH,1)
            val nYear = nC[Calendar.YEAR]
            val nMonth = nC[Calendar.MONTH]+1

            when (currentItemPosition) {

                0 -> {

                    list[2].dateList = printDatesInMonth(pYear,pMonth)
                    list[2].year = pYear
                    list[2].month = format.format(pC.time)
                    list[2].monthInInteger = pMonth

                    list[0].dateList =  printDatesInMonth(year,month)
                    list[0].year = year
                    list[0].month = format.format(c.time)
                    list[0].monthInInteger = month

                    list[1].dateList =  printDatesInMonth(nYear,nMonth)
                    list[1].year = nYear
                    list[1].month = format.format(nC.time)
                    list[1].monthInInteger = nMonth

                    lifecycleScope.launch {
                        delay(DELAY)
                        calenderViewPagerAdapter.notifyItemChanged(position-1)
                        calenderViewPagerAdapter.notifyItemChanged(position+1)
                    }

                }


                1 -> {

                    list[0].dateList =  printDatesInMonth(pYear,pMonth)
                    list[0].year = pYear
                    list[0].month = format.format(pC.time)
                    list[0].monthInInteger = pMonth

                    list[1].dateList =  printDatesInMonth(year,month)
                    list[1].year = year
                    list[1].month = format.format(c.time)
                    list[1].monthInInteger = month

                    list[2].dateList = printDatesInMonth(nYear,nMonth)
                    list[2].year = nYear
                    list[2].month = format.format(nC.time)
                    list[2].monthInInteger = nMonth

                    lifecycleScope.launch {
                        delay(DELAY)
                        calenderViewPagerAdapter.notifyItemChanged(position-1)
                        calenderViewPagerAdapter.notifyItemChanged(position+1)
                    }
                }

                2 -> {
                    list[1].dateList =  printDatesInMonth(pYear,pMonth)
                    list[1].year = pYear
                    list[1].month = format.format(pC.time)
                    list[1].monthInInteger = pMonth

                    list[2].dateList = printDatesInMonth(year,month)
                    list[2].year = year
                    list[2].month = format.format(c.time)
                    list[2].monthInInteger = month

                    list[0].dateList =  printDatesInMonth(nYear,nMonth)
                    list[0].year = nYear
                    list[0].month = format.format(nC.time)
                    list[0].monthInInteger = nMonth

                    lifecycleScope.launch {
                        delay(DELAY)
                        calenderViewPagerAdapter.notifyItemChanged(position-1)
                        calenderViewPagerAdapter.notifyItemChanged(position+1)
                    }
                }
            }
        }

       calenderJob?.start()

    }


    @SuppressLint("SimpleDateFormat")
    private fun setInitialSetup(){
        val format = SimpleDateFormat("MMM")

        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]+1

        val pC = Calendar.getInstance()
        pC.add(Calendar.MONTH, -1)
        val pYear = pC[Calendar.YEAR]
        val pMonth = pC[Calendar.MONTH]+1

        val nC = Calendar.getInstance()
        nC.add(Calendar.MONTH,1)
        val nYear = nC[Calendar.YEAR]
        val nMonth = nC[Calendar.MONTH]+1

        list.add(NumberModel(id = 1,printDatesInMonth(pYear,pMonth), year = pYear, month = format.format(pC.time),pMonth))
        list.add(NumberModel(id = 2,printDatesInMonth(year,month), year = year, month = format.format(c.time),month))
        list.add(NumberModel(id = 3,printDatesInMonth(nYear,nMonth), year = nYear, month = format.format(nC.time),nMonth))
    }


    @SuppressLint("SimpleDateFormat")
    private  fun printDatesInMonth(year: Int, month: Int):ArrayList<DayModel> {
        val dayList = arrayListOf<DayModel>()

        //Add the title
        dayList.add(DayModel(headerTitle = "M", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "T", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "W", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "T", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "F", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "S", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "S", dayViewType = DayViewType.CALENDER_HEADER))

        val fmt = SimpleDateFormat("dd/MM/yyyy EEE")
        val cal = Calendar.getInstance()
        cal.clear()
        cal.set(year, month - 1, 1)

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val offset = fetchFirstDayName(cal.time)
        //  Log.e("offset","<<< $offset")
        for (i in 0 until offset){
            dayList.add(DayModel(dayViewType = DayViewType.BUFFER))
        }

        for (i in 0 until daysInMonth) {
            //  Log.e("time","${fmt.format(cal.time)}")
          //  Log.e("dateCompare","${fmt.format(cal.time)}  ${currentSelectedDate?.compareTo(cal.time)}")
            dayList.add(DayModel(day = cal.time, dayViewType = DayViewType.DATE, isDaySelected = compareDates(cal.time)))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dayList
    }


    @SuppressLint("SimpleDateFormat")
    private fun fetchFirstDayName(date: Date):Int{
        val fmt = SimpleDateFormat("EEE")
        return when(fmt.format(date)){
            "Mon" -> 0
            "Tue" -> 1
            "Wed" -> 2
            "Thu" -> 3
            "Fri" -> 4
            "Sat" -> 5
            "Sun" -> 6
            else -> 0
        }
    }

    private fun fetchCalenderMonth(n:Int):Int{
        return when(n){
            1 ->  Calendar.JANUARY
            2 -> Calendar.FEBRUARY
            3 ->  Calendar.MARCH
            4 ->  Calendar.APRIL
            5 -> Calendar.MAY
            6 -> Calendar.JUNE
            7 -> Calendar.JULY
            8 -> Calendar.AUGUST
            9 -> Calendar.SEPTEMBER
            10 -> Calendar.OCTOBER
            11 -> Calendar.NOVEMBER
            12 -> Calendar.DECEMBER
            else -> 0
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun compareDates(date: Date):Boolean{
        return try {
            val fmt = SimpleDateFormat("yyyyMMdd")
            fmt.format(date) == currentSelectedDate?.let { fmt.format(it) }
        }catch (e: Exception){
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CurrentDateInstance.currentDateInstance = null
        CurrentDateInstance.rangeMaxDate = null
    }


    private fun checkAndCompareDates(date1: Date,date2: Date):Int{
        return if(date1 == date2)
            0
        else if(date1.after(date2))
            -1
        else
            1
    }


}