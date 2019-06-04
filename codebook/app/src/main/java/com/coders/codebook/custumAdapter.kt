package com.coders.codebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class custumAdapter(var context: Context, items:ArrayList<technology>):BaseAdapter() {

    var items: ArrayList<technology>? = null

    init {
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var holder: ViewHolder? = null
        var view: View? = convertView

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.template_challenge, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as? ViewHolder
        }
        val item = getItem(position) as technology

        holder?.title?.text = item.title
        holder?.description?.text = item.description
        holder?.image?.setImageResource(item.image)

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

        var title: TextView? = null
        var description: TextView? = null
        var image: ImageView? = null


        init {
            title = view.findViewById(R.id.titleChallenge)
            description = view.findViewById(R.id.descriptionChallenge)
            image = view.findViewById(R.id.imageTec)
        }
    }
}