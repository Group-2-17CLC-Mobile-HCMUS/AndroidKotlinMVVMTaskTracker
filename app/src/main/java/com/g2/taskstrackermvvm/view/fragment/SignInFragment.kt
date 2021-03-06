package com.g2.taskstrackermvvm.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    interface IOnSignIn {
        fun onSignIn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInBtn.setOnClickListener {
            showSignInOptions()
        }
    }

    private val MY_REQUEST_CODE: Int = 1002
    lateinit var providers: List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Init
        providers = listOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.GoogleBuilder().build()
            //AuthUI.IdpConfig.PhoneBuilder().build()
        )
    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), MY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (data == null) {
                return Toast.makeText(context, "Login canceled", Toast.LENGTH_SHORT).show()
            }
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(context, "" + user!!.email, Toast.LENGTH_SHORT).show()

                viewModel.addCurrentUser()

                (activity as IOnSignIn).onSignIn()
            } else {
                Toast.makeText(context, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SignInFragment()
    }
}
