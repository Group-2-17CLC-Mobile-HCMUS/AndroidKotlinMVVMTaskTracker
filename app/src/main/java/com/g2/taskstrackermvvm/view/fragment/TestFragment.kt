package com.g2.taskstrackermvvm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI

import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.test_fragment2.*
import kotlinx.android.synthetic.main.test_fragment2.signOutBtn
import org.koin.androidx.viewmodel.ext.android.viewModel


class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    private val testViewModel: TestViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.test_fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOutBtn.setOnClickListener {
            context?.let { it1 ->
                AuthUI.getInstance().signOut(it1)
                    .addOnCompleteListener {
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e -> Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }

        test_add_task_btn.setOnClickListener {
            context?.let {
//                testViewModel.testAddTag()
//                    testViewModel.testDeleteTag()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        testViewModel.testAddTag()
//        testViewModel.listTaskData.observe(viewLifecycleOwner, Observer {
//            showText.text = null
//            var textViewer = ""
//            for (item in it) {
//                textViewer += item.toString() + "\n"
//                testViewModel.setTag(item.id, "-M7_yqv2ve6bb_ixvZVV")
//            }
//            showText.text = textViewer
//        })
        testViewModel.addUserTest()

//        testViewModel.listTagData.observe(viewLifecycleOwner, Observer {
//            var text = ""
//            for (item in it) {
//                text += "${item.id}\n"
//            }
//            showText.text = text
//        })

//        testViewModel.updateUserTest()
    }

}
