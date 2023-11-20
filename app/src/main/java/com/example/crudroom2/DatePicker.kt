package com.example.crudroom2

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker : DialogFragment() {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mCalendar = Calendar.getInstance()
        val year = mCalendar.get(Calendar.YEAR)
        val month = mCalendar.get(Calendar.MONTH)
        val dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireActivity(), activity as DatePickerDialog.OnDateSetListener?, year, month, dayOfMonth)

        val todayInMillis = Calendar.getInstance().timeInMillis
        datePickerDialog.datePicker.minDate = todayInMillis

        return datePickerDialog
    }

}