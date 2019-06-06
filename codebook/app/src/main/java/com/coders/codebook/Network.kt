package com.coders.codebook

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject


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
            Log.d("volley get error", error.message)
        })
        queue.add(request)
    }


    fun getJson(activityContext: Context, url: String, dataGet: JSONObject, listener: Response.Listener<JSONObject>){

        val queue = Volley.newRequestQueue(activityContext)

        val request = JsonObjectRequest(Request.Method.POST, url, dataGet, listener, Response.ErrorListener {
                error ->
            Log.d("volley getJson error", error.message)
        })
        queue.add(request)
    }

    fun postHTTP(activityContext: Context, url: String, dataPost: JSONObject, listener: Response.Listener<JSONObject>) {

        val queue = Volley.newRequestQueue(activityContext)

        val request = JsonObjectRequest(Request.Method.POST, url, dataPost, listener, Response.ErrorListener {
            error ->
            Log.d("volley post error", error.message)
        })
        queue.add(request)
    }
}