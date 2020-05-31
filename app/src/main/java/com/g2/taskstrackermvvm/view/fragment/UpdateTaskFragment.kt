package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.utils.toEditable
import com.g2.taskstrackermvvm.viewmodel.UpdateTaskViewModel
import kotlinx.android.synthetic.main.fragment_update_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateTaskFragment : Fragment() {

    val args: UpdateTaskFragmentArgs by navArgs()
    private lateinit var task: LiveData<Task>
    private val status = listOf<Task.Status>(Task.Status.Todo, Task.Status.Doing, Task.Status.Done)

    private val viewModel: UpdateTaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = viewModel.getTask(args.taskId)
        statusSpinner.adapter =
            context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, status) }
        task.observe(viewLifecycleOwner, Observer {
            tasknameEditText.text = it.title.toEditable()
            descEditText.text = it.desc.toEditable()
            statusSpinner.setSelection(status.indexOf(it.status))
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UpdateTaskFragment()
    }
}

