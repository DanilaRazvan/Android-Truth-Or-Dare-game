package com.example.adevarsiprovocare

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class RecyclerViewAdapterPlayers(
    context: Context,
    private var names: ArrayList<String>,
    private var imageNames: ArrayList<String>
) : RecyclerView.Adapter<RecyclerViewAdapterPlayers.ViewHolder>() {

    interface RecyclerViewListener {
        fun update()
    }

    private var listener = context as RecyclerViewListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = names[position]
        holder.image.setImageResource(getImageID(imageNames[position]))

        holder.parentLayout.setOnLongClickListener {
            PrepareActivity.run {
                names.removeAt(position)
                images.removeAt(position)
            }
            listener.update()
            return@setOnLongClickListener true
        }
    }

    private fun getImageID(imageName: String): Int {
        val fields = R.drawable::class.java.fields

        for (i in fields.indices) {
            try {
                val name = fields[i].name
                if (name.matches("^a[0-9]*\$".toRegex()) && fields[i].name == "a${imageName}") {
                    return fields[i].getInt(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return -1
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.nameTextView)
        var image: ImageView = itemView.findViewById(R.id.playerImageList)
        var parentLayout: RelativeLayout = itemView.findViewById(R.id.parentLayout)
    }
}