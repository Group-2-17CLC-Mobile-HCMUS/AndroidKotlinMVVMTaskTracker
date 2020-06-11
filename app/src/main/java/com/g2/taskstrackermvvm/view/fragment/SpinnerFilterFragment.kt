package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.ChartsFragmentViewModel
import com.g2.taskstrackermvvm.viewmodel.ListTaskViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpinnerFilterFragment : Fragment(){
    private val viewModel: ListTaskViewModel by viewModel()
    private val type = arrayOf("All", "To do", "Doing", "Done")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.home_fragment, container, false)
        val spinner = v.findViewById<Spinner>(R.id.spinner)
        spinner?.adapter = activity?.applicationContext?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                type
            )
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val choosed = parent?.getItemAtPosition(position).toString()
                if (choosed == "All") {

                }
                if (choosed == "To do") {
                    viewModel.data.value?.forEach {
                        if(it.status == Task.Status.Todo)
                        {

                        }
                    }
                }
                if (choosed == "Doing") {
                    viewModel.data.value?.forEach {
                        if(it.status == Task.Status.Doing)
                        {

                        }
                    }
                }
                if (choosed == "Done") {
                    viewModel.data.value?.forEach {
                        if(it.status == Task.Status.Done)
                        {

                        }
                    }
                }
            }
        }
        return v;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}