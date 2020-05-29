package com.g2.taskstrackermvvm.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.g2.taskstrackermvvm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_update_user_profile.*
import kotlinx.android.synthetic.main.fragment_update_user_profile.view.*

class UpdateUserProfileFragment : Fragment() {

    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_user_profile, container, false)
        if (user != null) {
            view.usernameEditText.setText(user.displayName)
            view.emailTextView.text = user.email
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateProfileBtn.setOnClickListener {
            if (user != null) {
                if (usernameEditText.text == null) {
                    Toast.makeText(context, "Unable to update", Toast.LENGTH_SHORT).show()
                } else {
                    val newUsername = usernameEditText.text.toString()
                    val profile = UserProfileChangeRequest.Builder()
                    profile.displayName = newUsername
                    user.updateProfile(profile.build()).addOnSuccessListener {
                        AlertDialog.Builder(activity).apply {
                            setTitle("Update Success")
                            setMessage("Display name has been successfully updated!")
                            setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                        }.show()
                    }.addOnFailureListener {
                        AlertDialog.Builder(activity).apply {
                            setTitle("Update Failure")
                            setMessage("Update failed with exception ${it.message}")
                            setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                        }.show()
                    }
                }

            }
        }
    }
}
