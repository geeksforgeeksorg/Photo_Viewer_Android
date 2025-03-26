package org.geeksforgeeks.demo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class Adapter (
    private val context: Context,
    private val imagePathArrayList: ArrayList<String>
) : RecyclerView.Adapter<Adapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val imageIV: ImageView = itemView.findViewById<ImageView>(R.id.idIVImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = imagePathArrayList.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val imgFile = File(imagePathArrayList[position])
        if (imgFile.exists()) {
            Glide.with(context).load(imgFile).into(holder.imageIV)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ImageDetailActivity::class.java)
                intent.putExtra("imgPath", imagePathArrayList[position])
                context.startActivity(intent)
            }
        }
    }
}