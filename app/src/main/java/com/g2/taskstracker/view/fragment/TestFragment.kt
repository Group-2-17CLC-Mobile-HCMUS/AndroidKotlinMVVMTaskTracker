package com.g2.taskstracker.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI

import com.g2.taskstracker.R
import com.g2.taskstracker.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.test_fragment2.*
import kotlinx.android.synthetic.main.test_fragment2.signOutBtn

class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    private val viewModel = viewModels<TestViewModel>()

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.value.dataText.observe(viewLifecycleOwner, Observer {
            showText.text = it
        })
    }

}
