package com.coders.codebook

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

object Network {


    fun vNetwork(activity:AppCompatActivity):Boolean{

        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
    }


    fun getHTTP(activityContext: Context, url: String, listener: Response.Listener<String>){

        val queue = Volley.newRequestQueue(activityContext)

        val request = StringRequest(Request.Method.GET, url, listener, Response.ErrorListener {
            error ->
            Log.d("volley error", error.message)
        })

        queue.add(request)
    }
}