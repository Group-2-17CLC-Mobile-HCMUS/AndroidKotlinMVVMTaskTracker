package com.g2.taskstrackermvvm.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.viewmodel.TagsViewModel
import kotlinx.android.synthetic.main.dialog_create_new_tag.view.*
import kotlinx.android.synthetic.main.fragment_tags.*
import kotlinx.android.synthetic.main.fragment_tags.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TagsFragment : Fragment() {

    private val viewModel: TagsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val v = inflater.inflate(R.layout.fragment_tags, container, false)
        v.tag_searchview.apply {
            setIconifiedByDefault(false)
            findViewById<TextView>(R.id.search_src_text).apply {
                setTextColor(resources.getColor(R.color.colorText, context.theme))
            }
            findViewById<ImageView>(R.id.search_button).apply {
                setColorFilter(resources.getColor(R.color.colorText, context.theme))
            }
            findViewById<ImageView>(R.id.search_close_btn).apply {
                setColorFilter(resources.getColor(R.color.colorText, context.theme))
            }
            findViewById<ImageView>(R.id.search_mag_icon).apply {
                setColorFilter(resources.getColor(R.color.colorText, context.theme))
            }
            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.applySearch(
                        query
                    )
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.applySearch(
                        newText
                    )
                    return true
                }
            })
        }
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_tag, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle item clicks
        return when (item.itemId) {
            R.id.no_filter -> {
                viewModel.applyFilter(TagsViewModel.FilterMode.NONE)
                true
            }
            R.id.red_filter -> {
                viewModel.applyFilter(TagsViewModel.FilterMode.RED)
                true
            }
            R.id.blue_filter -> {
                viewModel.applyFilter(TagsViewModel.FilterMode.BLUE)
                true
            }
            R.id.green_filter -> {
                viewModel.applyFilter(TagsViewModel.FilterMode.GREEN)
                true
            }
            else -> true
        } || super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_tag_fab.setOnClickListener {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater
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
                            viewModel.addTag(
                                v.tag_name_edit_text.text.toString(),
                                v.select_color_spinner.selectedItem as Tag.Color
                            )
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

            for (i in it.indices) {
                tags_container.getTagView(i).setOnCreateContextMenuListener { menu, _, _ ->
                    menu.apply {
                        add("Find tagged Tasks").setOnMenuItemClickListener { _ ->
                            activity?.let { act ->
                                val builder = AlertDialog.Builder(act)
                                builder.apply {
                                    setTitle("Find all tagged Tasks")
                                    setMessage("Find all Tasks tagged with this tag?")
                                    setPositiveButton(R.string.ok) { dialog, which ->
                                        dialog.dismiss()
                                        var directions =
                                            TagsFragmentDirections.actionTagsFragmentToHomeFragment(
                                                it[i].name
                                            )
                                        findNavController().navigate(directions)
                                    }
                                    setNegativeButton(R.string.cancel) { dialog, which ->
                                        dialog.cancel()
                                    }
                                    create()
                                }.show()
                            }
                            true
                        }
                        add("Edit").setOnMenuItemClickListener { _ ->
                            activity?.let { act ->
                                val builder = AlertDialog.Builder(act)
                                val inflater = requireActivity().layoutInflater
                                val v = inflater.inflate(
                                    R.layout.dialog_create_new_tag,
                                    null
                                )
                                val item = listOf(
                                    Tag.Color.RED,
                                    Tag.Color.BLUE,
                                    Tag.Color.GREEN
                                )
                                v.select_color_spinner.apply {
                                    adapter = ArrayAdapter(
                                        requireActivity(),
                                        android.R.layout.simple_spinner_dropdown_item,
                                        item
                                    )
                                    setSelection(item.indexOf(it[i].color))
                                }
                                v.tag_name_edit_text.setText(it[i].name)
                                builder.apply {
                                    setView(v)
                                    setTitle(R.string.update_tag_dialog_title)
                                    setPositiveButton(R.string.update_tag_dialog_pos_btn) { _, _ ->
                                        if (v.tag_name_edit_text.text.toString() == "") {
                                            Toast.makeText(
                                                context,
                                                "The name must not be empty",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            it[i].name =
                                                v.tag_name_edit_text.text.toString()
                                            it[i].color =
                                                v.select_color_spinner.selectedItem as Tag.Color
                                            viewModel.updateTag(it[i])
                                        }
                                    }
                                    setNegativeButton(R.string.cancel) { dialog, _ ->
                                        dialog.cancel()
                                    }
                                }
                                builder.create().show()
                            }
                            true
                        }
                        add("Remove").setOnMenuItemClickListener { _ ->
                            val builder: AlertDialog.Builder? = activity?.let { act ->
                                AlertDialog.Builder(act)
                            }
                            builder?.apply {
                                setMessage(R.string.delete_tags_dialog_content)
                                setTitle(R.string.delete_tags_dialog_title)
                                setPositiveButton(R.string.ok) { _, _ ->
                                    viewModel.removeTag(it[i])
                                }
                                setNegativeButton(R.string.cancel) { dialog, _ ->
                                    dialog.cancel()
                                }
                            }
                            builder?.create()?.show()
                            true
                        }
                    }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TagsFragment()
    }
}
