package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.CreateTaskViewModel
import kotlinx.android.synthetic.main.create_new_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateTaskFragment : Fragment() {

    private val viewModel: CreateTaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTaskBtn.setOnClickListener {
            if (titleEditText.text.toString() == "") {
                Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addTask(
                    title = titleEditText.text.toString(),
                    desc = taskDescEditText.text.toString()
                )
                findNavController().navigate(R.id.action_createTaskFragment_to_homeFragment)
            }
        }
        cancelCreateTaskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_createTaskFragment_to_homeFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateTaskFragment()
    }
}
