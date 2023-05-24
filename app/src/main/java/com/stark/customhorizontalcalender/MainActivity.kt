package com.stark.customhorizontalcalender

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stark.customhorizontalcalender.adapter.NumberViewPagerAdapter
import com.stark.customhorizontalcalender.databinding.ActivityMainBinding
import com.stark.customhorizontalcalender.model.CalenderType
import com.stark.customhorizontalcalender.model.CurrentDateInstance
import com.stark.customhorizontalcalender.model.DayModel
import com.stark.customhorizontalcalender.model.DayViewType
import com.stark.customhorizontalcalender.model.NumberModel
import com.stark.customhorizontalcalender.yearandmonthpicker.MonthAndYearPickerDialogFragment
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

    private val selectedDates = arrayListOf(
        "01-01-2023",
        "02-01-2023",
        "03-02-2023",
        "04-02-2023",
        "05-03-2023",
        "06-03-2023",
        "07-03-2023"
    )


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

                    endMonth = -1
                    endYear = -1

                    /*if(startYear!=-1 && startMonth!= -1 && endMonth != -1 && endYear!= -1) {
                        setInitialSetup()
                    }*/
                    textEndMonth.text = ""
                    calenderViewPagerAdapter.setUpList(arrayListOf())

                }.show((binding.root.context as AppCompatActivity).supportFragmentManager,"Tag")
            }

            binding.textEndMonth.setOnClickListener {
                MonthAndYearPickerDialogFragment{

                    endYear = it.second
                    endMonth = it.first.id


                    if(startYear!=-1 && startMonth!= -1 && endMonth != -1 && endYear!= -1){
                        val validationStatus = validate()
                        Log.e("status","<<< $validationStatus")
                        if(validationStatus){
                            binding.textEndMonth.text = "${it.first.month} ${it.second}"
                            setInitialSetup()
                        }else{
                            Toast.makeText(this@MainActivity,"Impossible",Toast.LENGTH_LONG).show()
                        }
                    }

                }.show((binding.root.context as AppCompatActivity).supportFragmentManager,"Tag")
            }
        }
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

        selectDateCalenderInstance.set(startYear, startMonth-1,1)
        endDateCalenderInstance.set( endYear,endMonth,1)

        while (!selectDateCalenderInstance.equals(endDateCalenderInstance)){
            currentMonth = selectDateCalenderInstance.get(Calendar.MONTH)
            currentYear = selectDateCalenderInstance.get(Calendar.YEAR)
            Log.e("selectedYear","$currentMonth $currentYear")
            list.add(NumberModel(id = 1,printDatesInMonth(currentYear,currentMonth), year = currentYear, month = format.format(selectDateCalenderInstance.time),currentMonth, type = CalenderType.Calender))
            if(currentMonth == 11){
                list.add(NumberModel(id = 1,
                    arrayListOf(), year = currentYear+1, month = format.format(selectDateCalenderInstance.time),currentMonth, type = CalenderType.Heading))
            }
            selectDateCalenderInstance.add(Calendar.MONTH, 1)
        }

        if(list.size>0){
            val lastIndex = list.size -1
            if(list[lastIndex].type==CalenderType.Heading)
                list.removeAt(lastIndex)
        }

        calenderViewPagerAdapter.setUpList(list)
    }


    @SuppressLint("SimpleDateFormat")
    private  fun printDatesInMonth(year: Int, month: Int):ArrayList<DayModel> {
        val dayList = arrayListOf<DayModel>()

        //Add the title
        dayList.add(DayModel(headerTitle = "S", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "M", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "T", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "W", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "T", dayViewType = DayViewType.CALENDER_HEADER))
        dayList.add(DayModel(headerTitle = "F", dayViewType = DayViewType.CALENDER_HEADER))
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
             // Log.e("time","${fmt.format(cal.time)}")
          //  Log.e("dateCompare","${fmt.format(cal.time)}  ${currentSelectedDate?.compareTo(cal.time)}")
            dayList.add(DayModel(day = cal.time, dayViewType = DayViewType.DATE, isDaySelected = compareDates(cal.time), isDateEnabled =checkDateEnabled(cal.time)))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dayList
    }


    @SuppressLint("SimpleDateFormat")
    private fun fetchFirstDayName(date: Date):Int{
        val fmt = SimpleDateFormat("EEE")
        return when(fmt.format(date)){
            "Mon" -> 1
            "Tue" -> 2
            "Wed" -> 3
            "Thu" -> 4
            "Fri" -> 5
            "Sat" -> 6
            "Sun" -> 0
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

    private fun validate():Boolean {
        val startDateInstance = Calendar.getInstance()
        startDateInstance.set(startYear, startMonth-1 , 1)
        val startDate = startDateInstance.time

        val endDateInstance = Calendar.getInstance()
        endDateInstance.set(endYear,endMonth,1)
        val endDate = endDateInstance.time

        return endDate.after(startDate)
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkDateEnabled(date: Date):Boolean{
        return try {
            val format = SimpleDateFormat("dd-MM-yyyy")
            val currentDate =  format.format(date.time)
            selectedDates.contains(currentDate)
        }catch (e:Exception){
            Log.e("exception","${e.message}")
            false
        }

    }
}