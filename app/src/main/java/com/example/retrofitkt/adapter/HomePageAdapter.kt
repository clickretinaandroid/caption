package com.example.retrofitkt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkt.activity.CaptionView
import com.example.retrofitkt.R
import com.example.retrofitkt.modelClass.Captions
import kotlinx.android.synthetic.main.view_item.view.*

class HomePageAdapter(private val context: Context, private val captionsList: List<Captions>) :
    RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    var s = arrayOfNulls<String?>(captionsList.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_item, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return captionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val User = captionsList[position]
        holder.setData(User, position)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CaptionView::class.java)
            intent.putExtra("URL", s[position])
            context.startActivity(intent)
        }
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(Captions: Captions?, pos: Int) {
            Captions?.let {
                itemView.tvtext.text = Captions.title
                s[pos] = Captions.url
            }
        }
    }
}