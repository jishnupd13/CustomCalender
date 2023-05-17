package com.stark.customhorizontalcalender

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.stark.customhorizontalcalender.adapter.NumberViewPagerAdapter
import com.stark.customhorizontalcalender.databinding.ActivityMainBinding
import com.stark.customhorizontalcalender.model.CurrentDateInstance
import com.stark.customhorizontalcalender.model.DayModel
import com.stark.customhorizontalcalender.model.DayViewType
import com.stark.customhorizontalcalender.model.NumberModel
import com.stark.customhorizontalcalender.utlis.DELAY_CLICK_ACTION
import com.stark.customhorizontalcalender.utlis.click
import com.stark.customhorizontalcalender.utlis.show
import com.stark.customhorizontalcalender.yearandmonthpicker.MonthAndYearPickerDialogFragment
import kotlinx.coroutines.Dispatchers
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
    private var list = arrayListOf<NumberModel>()
    private lateinit var calenderViewPagerAdapter: NumberViewPagerAdapter
    private var rightClickStatus = false
    private var leftClickStatus = false

    private var selectDateCalenderInstance = Calendar.getInstance()
    private var endDateCalenderInstance = Calendar.getInstance()

    private var startMonth = -1
    private var startYear = -1

    private var endMonth = -1
    private var endYear = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfiniteViewPager()
        currentSelectedDate = CurrentDateInstance.currentDateInstance
    }


    private fun setInfiniteViewPager() {

        calenderViewPagerAdapter = NumberViewPagerAdapter(list, onDateChangeListener = { year, month ->

        }, currentSelectedData = {
            if(it != null){
                currentSelectedDate = it
                CurrentDateInstance.currentDateInstance = it
                val date = convertDateToString(it)
                Toast.makeText(this,date,Toast.LENGTH_LONG).show()
            }
        })

        binding.calenderRecyclerview.adapter = calenderViewPagerAdapter

        binding.apply {

            binding.textStartMonth.setOnClickListener {
                MonthAndYearPickerDialogFragment{
                    binding.textStartMonth.text = "${it.first.month} ${it.second}"

                    startYear = it.second
                    startMonth = it.first.id

                    Log.e("startYear","$startMonth $startYear")

                    if(startYear!=-1 && startMonth!= -1 && endMonth != -1 && endYear!= -1) {
                        setInitialSetup()
                    }

                }.show((binding.root.context as AppCompatActivity).supportFragmentManager,"Tag")
            }

            binding.textEndMonth.setOnClickListener {
                MonthAndYearPickerDialogFragment{

                    endYear = it.second
                    endMonth = it.first.id

                    binding.textEndMonth.text = "${it.first.month} ${it.second}"

                    if(startYear!=-1 && startMonth!= -1 && endMonth != -1 && endYear!= -1){
                        setInitialSetup()
                    }

                }.show((binding.root.context as AppCompatActivity).supportFragmentManager,"Tag")
            }
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

    private fun transitionManagerForRecyclerview(){
        val changeBounds = ChangeBounds()
        changeBounds.excludeChildren(binding.calenderRecyclerview, true)
        TransitionManager.beginDelayedTransition(binding.rootLayout, changeBounds)
    }


    @SuppressLint("SimpleDateFormat")
    private fun setInitialSetup(){
        val format = SimpleDateFormat("MMM")
        list = arrayListOf()

        /*val c = Calendar.getInstance()
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

        Log.e("month","$month $pMonth $nMonth")

        list.add(NumberModel(id = 1,printDatesInMonth(pYear,pMonth), year = pYear, month = format.format(pC.time),pMonth))
        list.add(NumberModel(id = 2,printDatesInMonth(year,month), year = year, month = format.format(c.time),month))
        list.add(NumberModel(id = 3,printDatesInMonth(nYear,nMonth), year = nYear, month = format.format(nC.time),nMonth))*/

      //  val endDateMonth = endDateCalenderInstance.get(Calendar.MONTH)
        //val endDateYear = endDateCalenderInstance.get(Calendar.YEAR)

        var currentMonth: Int
        var currentYear: Int

        selectDateCalenderInstance.set(startYear, startMonth,1)
        endDateCalenderInstance.set( endYear,endMonth,1)

        list = arrayListOf()
        while (!selectDateCalenderInstance.equals(endDateCalenderInstance)){
            currentMonth = selectDateCalenderInstance.get(Calendar.MONTH)
            currentYear = selectDateCalenderInstance.get(Calendar.YEAR)
            Log.e("selectedYear","$currentMonth $currentYear")
            list.add(NumberModel(id = 1,printDatesInMonth(currentYear,currentMonth), year = currentYear, month = format.format(selectDateCalenderInstance.time),currentMonth))
            selectDateCalenderInstance.add(Calendar.MONTH, 1)
        }

        calenderViewPagerAdapter.setUpList(list)
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
        //cal.set(year, month - 1, 1)
        cal.set(year, month , 1)

        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val offset = fetchFirstDayName(cal.time)
        //  Log.e("offset","<<< $offset")
        for (i in 0 until offset){
            dayList.add(DayModel(dayViewType = DayViewType.BUFFER))
        }

        for (i in 0 until daysInMonth) {
              Log.e("time","${fmt.format(cal.time)}")
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
    }

    private fun convertDateToString(date: Date):String{
       return try {
            val format = SimpleDateFormat("EEEE MMM dd, yyyy")
            format.format(date.time)
        }catch (e:Exception){
            ""
        }
    }
}