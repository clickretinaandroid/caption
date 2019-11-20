package com.example.retrofitkt.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkt.modelClass.Data
import com.example.retrofitkt.R
import kotlinx.android.synthetic.main.caption_view.view.*


class CaptionViewAdapter(private val context: Context, private val dataList: List<Data>) :
    RecyclerView.Adapter<CaptionViewAdapter.MyViewHolder>() {
    var s = arrayOfNulls<String?>(dataList.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.caption_view, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.setData(data, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentUser: Data? = null
        private var currentPosition: Int = 0

        init {

            itemView.copy.setOnClickListener {

                currentUser?.let {
                    copyText(position)
                }
            }
            itemView.wa.setOnClickListener {

                currentUser?.let {
                    shareWhatsapp(position)

                }
            }
            itemView.share_text.setOnClickListener {

                currentUser?.let {
                    sharetext(position)

                }
            }

        }

        fun setData(data: Data?, pos: Int) {
            data?.let {
                itemView.tv_caption.text = data.title
                s[pos] = data.title
            }
            this.currentUser = data
            this.currentPosition = pos
        }
    }

    private fun shareWhatsapp(position: Int) {
        try {
            val sendIntent = Intent("android.intent.action.MAIN")
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.type = "text/plain"
            sendIntent.putExtra(Intent.EXTRA_TEXT, s[position])
            sendIntent.putExtra("jid", "$s[]@s.whatsapp.net")
            sendIntent.setPackage("com.whatsapp")
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error/n$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyText(position: Int) {
        if (s[position] == "") {
            Toast.makeText(context, "Text is Empty", Toast.LENGTH_SHORT).show()
        } else {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData =
                ClipData.newPlainText("your_text_to_be_copied", s[position])
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharetext(position: Int) {
        val sharingIntent =
            Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = s[position].toString()
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
}