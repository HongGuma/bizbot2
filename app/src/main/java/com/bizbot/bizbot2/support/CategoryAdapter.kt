package com.bizbot.bizbot2.support

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bizbot.bizbot2.R

class CategoryAdapter(var context:Context,type:Int): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val area = arrayOf("전체","서울","부산","대구","인천","광주","대전","울산","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주")
    private val field = arrayOf("전체","금융","기술","인력","수출","내수","창업","경영","제도","동반성장")
    var mCAHandler: Handler = CategoryActivity.CAHandler(Looper.myLooper()!!)
    private val categoryType = type
    private var index = 0
    lateinit var arr: Array<String>

    init{
        when(type){
            1-> arr = area
            2-> arr = field
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arr[position]
        holder.apply {
            bind(item)
        }
        holder.itemName.setOnClickListener {
            index = position
            val message:Message = mCAHandler.obtainMessage()
            message.what = categoryType
            message.obj = item
            mCAHandler.sendMessage(message)
            notifyDataSetChanged()
        }
        holder.itemName.isSelected = index == position

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val itemName: TextView = v.findViewById(R.id.item_btn)

        fun bind(item: String){
            itemName.text = item
        }
    }




}