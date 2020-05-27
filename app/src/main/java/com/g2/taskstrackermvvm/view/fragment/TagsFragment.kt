package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import co.lujun.androidtagview.TagView
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.viewmodel.TagsViewModel
import kotlinx.android.synthetic.main.dialog_create_new_tag.*
import kotlinx.android.synthetic.main.dialog_create_new_tag.view.*
import kotlinx.android.synthetic.main.fragment_tags.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TagsFragment : Fragment() {

    private val viewModel: TagsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_tag_fab.setOnClickListener {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater;
                val v = inflater.inflate(R.layout.dialog_create_new_tag, null)
                v.select_color_spinner.apply {
                    adapter = ArrayAdapter(
                        requireActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        listOf(Tag.Color.RED, Tag.Color.BLUE, Tag.Color.GREEN)
                    )
                    setSelection(0)
                }
                builder.apply {
                    setView(v)
                    setTitle(R.string.create_tag_dialog_title)
                    setPositiveButton(R.string.ok) { _, _ ->
                        if (v.tag_name_edit_text.text.toString() == "") {
                            Toast.makeText(
                                context,
                                "The name must not be empty",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.addTag(v.tag_name_edit_text.text.toString(), v.select_color_spinner.selectedItem as Tag.Color)
                        }
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                }
                builder.create().show()
            }
        }
        viewModel.tags.observe(viewLifecycleOwner, Observer {
            val tagsColor: List<IntArray> = it.map { tag ->
                when (tag.color) {
                    Tag.Color.RED -> intArrayOf(
                        0xffff5131.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                    Tag.Color.BLUE -> intArrayOf(
                        0xff768fff.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                    Tag.Color.GREEN -> intArrayOf(
                        0xff5efc82.toInt(),
                        Color.BLACK,
                        Color.BLACK,
                        Color.YELLOW
                    )
                }
            }
            tags_container.setTags(it.map { tag -> tag.toString() }, tagsColor)
            tags_container.setOnTagClickListener(object : TagView.OnTagClickListener {
                override fun onSelectedTagDrag(position: Int, text: String?) {
                }

                override fun onTagLongClick(position: Int, text: String?) {
                    Log.d("TEST!-2", position.toString())
                    val builder: AlertDialog.Builder? = activity?.let { act ->
                        AlertDialog.Builder(act)
                    }
                    builder?.apply {
                        setMessage(R.string.delete_tags_dialog_content)
                        setTitle(R.string.delete_tags_dialog_title)
                        setPositiveButton(R.string.ok) { _, _ ->
                            viewModel.removeTag(it[position])
                        }
                        setNegativeButton(R.string.cancel) { dialog, _ ->
                            dialog.cancel()
                        }
                    }
                    builder?.create()?.show()
                }

                override fun onTagClick(position: Int, text: String?) {
                }

                override fun onTagCrossClick(position: Int) {
                }
            })
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TagsFragment()
    }
}
