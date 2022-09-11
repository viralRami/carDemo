package com.example.carlistdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(val carList: ArrayList<Car>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.car_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(carList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return carList.size
    }

    //the class is hodling the list view
    class ViewHolder( item: View) : RecyclerView.ViewHolder(item) {
        val txtCarId=item.findViewById<TextView>(R.id.txtCarIdValue)
        val txtCarYear=item.findViewById<TextView>(R.id.txtCarYearValue)
        val txtCarName=item.findViewById<TextView>(R.id.txtCarName)
        val txtCarColor=item.findViewById<TextView>(R.id.txtCarColorName)
        val txtCarModel=item.findViewById<TextView>(R.id.txtCarModelName)
        val txtCarPrice=item.findViewById<TextView>(R.id.txtCarPriceValue)
        fun bindItems(car: Car) {
            txtCarId.text= car.id.toString()
            txtCarYear.text = car.car_model_year.toString()
            txtCarName.text= car.car
            txtCarColor.text= car.car_color
            txtCarModel.text= car.car_model
            txtCarPrice.text= car.price
        }
    }

}