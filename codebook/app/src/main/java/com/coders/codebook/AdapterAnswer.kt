package com.coders.codebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class AdapterAnswer(var context: Context, items:ArrayList<answer>): BaseAdapter() {

    var items: ArrayList<answer>? = null

    init {
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder: ViewHolder? = null
        var view: View? = convertView

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.template_answer, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as? ViewHolder
        }
        val item = getItem(position) as answer

        holder?.owner?.text = item.owner
        holder?.answer?.text = item.answer

        return view!!

    }

    override fun getItem(position: Int): Any {
        return items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items?.count()!!
    }

    private class ViewHolder(view: View) {

        var owner: TextView? = null
        var answer: TextView? = null


        init {
            owner = view.findViewById(R.id.nickname_answer)
            answer = view.findViewById(R.id.answer_answer)
        }
    }
}