package com.example.carlistdemo

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {
    lateinit var  rvCarList:RecyclerView
    lateinit var  customeAdapter: CustomAdapter
     var  carListTemp=ArrayList<Car>()
     var  finalCarList=ArrayList<Car>()
    var minYear=1985
    var maxYear=2022

    var minprice=1000
    var maxPrice=4000

    var even=false
    var odd=false
    var shortAlpha =false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvCarList=findViewById(R.id.rvCarList)
        var filterBtn=findViewById<ImageView>(R.id.imgFilter)
        filterBtn.setOnClickListener { showDialog() }
        if (isOnline(this)) {
            getCarList()

        }else{
            carListTemp=getArrayList()
            finalCarList=getArrayList()
            if(carListTemp.isNotEmpty())
                setAdapter()
        }
    }

    private fun getCarList() {
        val call: Call<Cars> = RetrofitClient.instance!!.getMyApi().getCar()
        call.enqueue(object : Callback<Cars> {
            override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                Log.e("sucesses:::",response.body()!!.cars.toString())
                val myCarList: List<Car> = response.body()!!.cars
                val carList=ArrayList<Car>()
                for (i in 0..49)
                {
                    carList.add(i,myCarList[i])
                }
                saveArrayList(carList)
                carListTemp=carList
                finalCarList=carList
                setAdapter()
            }

            override fun onFailure(call: Call<Cars>, t: Throwable?) {
                Log.e("error:::",t.toString())
                Toast.makeText(applicationContext, "An error has occured", Toast.LENGTH_LONG).show()
            }

          
        })
    }
    fun saveArrayList(listArray: ArrayList<Car>) {
        val prefs = getDefaultSharedPreferences(this@MainActivity)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(listArray)
        editor.putString("TAG_LIST", json) ///"TAG_LIST" is a key must same for getting data
        editor.apply()
    }
    fun getArrayList(): ArrayList<Car> {
        val prefs = getDefaultSharedPreferences(this@MainActivity)
        val gson = Gson()
        val json = prefs.getString("TAG_LIST", null)
        val listType: Type = object : TypeToken<ArrayList<Car>>() {}.type

        return gson.fromJson(json, listType)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
   private fun setAdapter()
    {
        customeAdapter=CustomAdapter(carListTemp)
        rvCarList.adapter=customeAdapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDialog() {
        val dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bckgroud_dailog)
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.filter_dialog)
        val rsPrice = dialog.findViewById(R.id.rsPrice) as RangeSeekBar
        val rsYear = dialog.findViewById(R.id.rsYear) as RangeSeekBar
rsPrice.setIndicatorTextDecimalFormat("0")
        rsYear.setIndicatorTextDecimalFormat("0")
        val btnOdd = dialog.findViewById(R.id.btnOdd) as RadioButton
        val btnEven = dialog.findViewById(R.id.btnEven) as RadioButton
        val shortAlphabetically = dialog.findViewById(R.id.shortAlphabetically) as CheckBox
        val yesBtn = dialog.findViewById(R.id.btnApply) as Button
        val noBtn = dialog.findViewById(R.id.btnCancel) as Button
        rsPrice.setProgress(minprice.toFloat(),maxPrice.toFloat())
        rsYear.setProgress(minYear.toFloat(),maxYear.toFloat())
        btnOdd.isChecked=odd
        btnEven.isChecked=even
        shortAlphabetically.isChecked=shortAlpha
        shortAlphabetically.setOnCheckedChangeListener { buttonView, isChecked ->
            shortAlpha=isChecked
        }
        btnOdd.setOnCheckedChangeListener { buttonView, isChecked ->
            odd=isChecked
        }
        btnEven.setOnCheckedChangeListener { buttonView, isChecked ->
            even=isChecked
        }

        rsPrice.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(rangeSeekBar: RangeSeekBar, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
            minprice=leftValue.toInt()
                maxPrice=rightValue.toInt()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

        })
        rsYear.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(rangeSeekBar: RangeSeekBar, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                minYear=leftValue.toInt()
                maxYear=rightValue.toInt()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

        })

        yesBtn.setOnClickListener {
shortList()
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            carListTemp=finalCarList
            setAdapter()
            dialog.dismiss() }
        dialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun shortList() {
        carListTemp.clear()
        finalCarList.forEach {
            if (it.car_model_year.toInt() in (minYear + 1) until maxYear) {
                carListTemp.add(it)

            }
        }
        carListTemp.removeIf { it -> it.price.removePrefix("$").toFloat().toInt() !in (minprice + 1) until maxPrice }
        if(even)
        {
            carListTemp.removeIf { it-> (it.id.toInt())%2 != 0 }
        }
        if(odd)
        {
            carListTemp.removeIf { it-> (it.id.toInt())%2 == 0 }
        }
        if(shortAlpha)
        {

                carListTemp.sortBy { it.car }  /* = java.util.ArrayList<com.example.carlistdemo.Car> */
        }
        setAdapter()
    }

}
