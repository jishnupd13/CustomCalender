package com.stark.customhorizontalcalender

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.stark.customhorizontalcalender.adapter.NumberViewPagerAdapter
import com.stark.customhorizontalcalender.databinding.ActivityMainBinding
import com.stark.customhorizontalcalender.model.CurrentDateInstance
import com.stark.customhorizontalcalender.model.DayModel
import com.stark.customhorizontalcalender.model.DayViewType
import com.stark.customhorizontalcalender.model.NumberModel
import com.stark.customhorizontalcalender.utlis.DELAY
import com.stark.customhorizontalcalender.utlis.DELAY_CLICK_ACTION
import com.stark.customhorizontalcalender.utlis.click
import com.stark.customhorizontalcalender.utlis.hide
import com.stark.customhorizontalcalender.utlis.invisible
import com.stark.customhorizontalcalender.utlis.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentItemPosition = 0
    private var currentSelectedDate : Date? = Calendar.getInstance().time
    private val list = arrayListOf<NumberModel>()
    private lateinit var calenderViewPagerAdapter: NumberViewPagerAdapter
    private var rightClickStatus = false
    private var leftClickStatus = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInfiniteViewPager()
        currentSelectedDate = CurrentDateInstance.currentDateInstance
        binding.imageArrowRight.invisible()
        binding.imgArrowLeft.show()
    }


    private fun setInfiniteViewPager() {
        setInitialSetup()
        calenderViewPagerAdapter = NumberViewPagerAdapter(list, onDateChangeListener = { year, month ->
            lifecycleScope.launch(Dispatchers.Main) {

            }
        }, currentSelectedData = {
            currentSelectedDate = it
            CurrentDateInstance.currentDateInstance = it
        })

        binding.apply {

            //calenderViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            calenderViewPager.adapter = calenderViewPagerAdapter



           /* currentItemPosition = Int.MAX_VALUE / 2 - Math.ceil(list.size.toDouble() / 2).toInt()
            calenderViewPager.setCurrentItem(currentItemPosition, false)*/

          /*  calenderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentItemPosition = position % list.size
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
                                //calenderViewPagerAdapter.notifyDataSetChanged()
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
                                //calenderViewPagerAdapter.notifyDataSetChanged()
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
                                //calenderViewPagerAdapter.notifyDataSetChanged()
                                calenderViewPagerAdapter.notifyItemChanged(position-1)
                                calenderViewPagerAdapter.notifyItemChanged(position+1)
                            }
                        }
                    }
                }
            })*/

            imageArrowRight.click {
                val currentViewPagerPosition = calenderViewPager.currentItem
                lifecycleScope.launch(Dispatchers.Main){
                    if(!rightClickStatus){
                        rightClickStatus = true
                        delay(DELAY_CLICK_ACTION)
                        calenderViewPager.setCurrentItem(currentViewPagerPosition + 1,false)
                        rightClickStatus = false
                    }
                }
            }

            imgArrowLeft.click {
                val currentViewPagerPosition = calenderViewPager.currentItem
                lifecycleScope.launch(Dispatchers.Main){

                    if(!leftClickStatus) {
                        leftClickStatus = true
                        delay(DELAY_CLICK_ACTION)
                        calenderViewPager.setCurrentItem( currentViewPagerPosition - 1,false)
                        leftClickStatus = false
                    }
                }
            }
        }


        binding.calenderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)




                binding.apply {
                    if(position ==0){
                        imgArrowLeft.invisible()
                        imageArrowRight.show()
                        lifecycleScope.launch {
                            delay(DELAY)
                            calenderViewPagerAdapter.notifyItemChanged(position+1)
                        }
                    }else if(position == list.size-1){
                        imgArrowLeft.show()
                        imageArrowRight.invisible()
                        lifecycleScope.launch {
                            delay(DELAY)
                            calenderViewPagerAdapter.notifyItemChanged(position-1)
                        }
                    }else{
                        imgArrowLeft.show()
                        imageArrowRight.show()

                        lifecycleScope.launch {
                            delay(DELAY)
                            calenderViewPagerAdapter.notifyItemChanged(position+1)
                            calenderViewPagerAdapter.notifyItemChanged(position-1)
                        }

                    }
                }
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun setInitialSetup(){
        val format = SimpleDateFormat("MMM")

       /* val c = Calendar.getInstance()
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
        list.add(NumberModel(id = 3,printDatesInMonth(nYear,nMonth), year = nYear, month = format.format(nC.time),nMonth))*/


         val c = Calendar.getInstance()
         val year = c[Calendar.YEAR]
          val month = c[Calendar.MONTH]+1
          list.add(NumberModel(id = 0,printDatesInMonth(year,month), year = year, month = format.format(c.time),month))

        for (i in 0..2) {
            c.add(Calendar.MONTH,1)
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]+1
            list.add(NumberModel(id = 0,printDatesInMonth(year,month), year = year, month = format.format(c.time),month))

        }
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
            Log.e("dateCompare","${fmt.format(cal.time)}  ${currentSelectedDate?.compareTo(cal.time)}")
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
}