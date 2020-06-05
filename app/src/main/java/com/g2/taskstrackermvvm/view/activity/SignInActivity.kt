package com.g2.taskstrackermvvm.view.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.view.fragment.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess


class SignInActivity : AppCompatActivity(), SignInFragment.IOnSignIn {

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
//        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    lateinit var dialog: AlertDialog

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            runOnUiThread {
                dialog.show()
            }
            Log.d("SignInActivity", "onLost")

        }

        override fun onUnavailable() {
            runOnUiThread {
                dialog.show()
            }
            Log.d("SignInActivity", "onUnavailable")

        }

        override fun onLosing(network: Network?, maxMsToLive: Int) {
            Log.d("SignInActivity", "onLosing")
        }

        override fun onAvailable(network: Network?) {
            Log.d("SignInActivity", "onAvailable")
            runOnUiThread {
                dialog.dismiss()
            }
        }
    }

    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        dialog = AlertDialog.Builder(this).apply {
            setTitle("Network Lost")
            setMessage("The Network is not available")
            setPositiveButton("Exit") { _, _ ->
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                exitProcess(0);
            }
            // window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            setCancelable(false)
        }.create()

        connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        try {
//            connectivityManager.unregisterNetworkCallback(networkCallback)
//        } catch (e: Exception) {
//            Log.w("Signin", "NetworkCallback for Wi-fi was not registered or already unregistered")
//        }
//        Firebase.database.setPersistenceEnabled(true)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            onSignIn()
        }
    }

    override fun onResume() {
        super.onResume()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        if (!isNetworkConnectionAvailable()) {
            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun isNetworkConnectionAvailable(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo ?: return false
        val network: NetworkInfo.State = info.state
        return network === NetworkInfo.State.CONNECTED || network === NetworkInfo.State.CONNECTING
    }

    override fun onSignIn() {
        MainActivity.startActivity(this)
    }
}
