package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.viewmodel.HomeListViewModel
import kotlinx.android.synthetic.main.task_item_recycler_view_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeListFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val data: MutableList<Task> = mutableListOf()

    companion object {
        fun newInstance() = HomeListFragment()
    }

    private val viewModel: HomeListViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_list_fragment, container, false)

        viewManager = LinearLayoutManager(context)
        viewAdapter = CustomAdapter(data)

        view.findViewById<RecyclerView>(R.id.task_list).apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(viewLifecycleOwner, Observer {
            data.clear()
            data.addAll(it)
            viewAdapter.notifyDataSetChanged()
        })
    }

    class CustomAdapter(private val data: List<Task>) :
        RecyclerView.Adapter<CustomAdapter.TaskViewHolder>() {

        class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.task_item_recycler_view_home, null)
            )

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.itemView.apply {
                titleText.text = data[position].title
                descText.text = data[position].desc
            }
        }

        override fun getItemCount() = data.size
    }


}
