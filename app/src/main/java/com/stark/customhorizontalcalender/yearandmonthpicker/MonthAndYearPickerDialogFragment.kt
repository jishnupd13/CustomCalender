package com.stark.customhorizontalcalender.yearandmonthpicker

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.stark.customhorizontalcalender.R
import com.stark.customhorizontalcalender.databinding.DialogMonthYearPickerBinding
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.Month
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.PagerAdapter
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.models.CellType
import com.stark.customhorizontalcalender.yearandmonthpicker.adapter.models.ViewPagerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MonthAndYearPickerDialogFragment(val onSelectMonthAndYear:(item:Pair<Month,Int>)->Unit):DialogFragment(
    R.layout.dialog_month_year_picker) {

    private lateinit var binding: DialogMonthYearPickerBinding
    private var year = 2021
    private var month = Month(id = 1,"Jan", isSelected = true)

    private var viewPagerItemList = arrayListOf<ViewPagerItem>(
        ViewPagerItem(1, CellType.Month),
        ViewPagerItem(2,CellType.Year)
    )
    private lateinit var pagerAdapter: PagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogMonthYearPickerBinding.bind(view)
        binding.textYear.text = year.toString()
        binding.textMonth.text = month.month

        pagerAdapter = PagerAdapter(viewPagerItemList, onYearSelected = {
            binding.textYear.text = it.toString()
            year = it
        },{
            binding.textMonth.text = it.month.uppercase()
            month = it
            lifecycleScope.launch {
                delay(300)
                withContext(Dispatchers.Main){
                    binding.viewPager.currentItem = 1
                }
            }

        })
        binding.viewPager.adapter = pagerAdapter
        setOnClickListeners()
        pageChangeListener()

    }

    private fun pageChangeListener(){
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position==0){
                    selectMonth()
                }else{
                    selectYear()
                }
            }
        })
    }

    private fun setOnClickListeners(){
        binding.textCancel.setOnClickListener {
            dismiss()
        }

        binding.textOk.setOnClickListener {
            onSelectMonthAndYear(Pair(month,year))
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun selectMonth(){
        binding.textMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.textYear.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorLightGrey))
        binding.textDialogTitle.text = "Select month"
    }

    private fun selectYear(){
        binding.textMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorLightGrey))
        binding.textYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.textDialogTitle.text = "Select year"
    }
}