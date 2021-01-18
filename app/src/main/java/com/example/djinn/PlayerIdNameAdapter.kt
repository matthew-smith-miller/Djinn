package com.example.djinn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PlayerIdNameAdapter(context: Context) : ArrayAdapter<DataClasses.IdNameTuple>(context, 0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listEntry: View? = convertView
        if (listEntry == null) {
            listEntry = LayoutInflater.from(context).inflate(
                android.R.layout.simple_spinner_item,
                parent,
                false
            )
        }

        val current = getItem(position)
        listEntry?.findViewById<TextView>(android.R.id.text1)?.text = current?.name
        listEntry?.tag = current?.id

        return listEntry!!
    }
}