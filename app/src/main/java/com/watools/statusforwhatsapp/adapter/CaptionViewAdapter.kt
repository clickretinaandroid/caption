package com.watools.statusforwhatsapp.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.watools.statusforwhatsapp.modelClass.Data
import com.watools.statusforwhatsapp.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.caption_view.view.*


class CaptionViewAdapter(private val context: Context, private val dataList: List<Data>) :
    RecyclerView.Adapter<CaptionViewAdapter.MyViewHolder>() {
    private lateinit var mInterstitialAd: InterstitialAd
    var msg = arrayOfNulls<String?>(dataList.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.caption_view, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        createInterstitial()
        val data = dataList[position]
        holder.setData(data, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentUser: Data? = null
        private var currentPosition: Int = 0

        init {

            itemView.copy.setOnClickListener {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                }else{
                    Log.e("TAG","Ad is not loaded")
                    mInterstitialAd.loadAd(AdRequest.Builder().build())
                }
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
                    shareText(position)

                }
            }

        }

        fun setData(data: Data?, pos: Int) {
            data?.let {
                itemView.tv_caption.text = data.caption
                msg[pos] = data.caption
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg[position])
            sendIntent.putExtra("jid", "$msg[]@s.whatsapp.net")
            sendIntent.setPackage("com.whatsapp")
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Error/n$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyText(position: Int) {
        if (msg[position] == "") {
            Toast.makeText(context, "Text is Empty", Toast.LENGTH_SHORT).show()
        } else {

            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData =
                ClipData.newPlainText("your_text_to_be_copied", msg[position])
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareText(position: Int) {
        val sharingIntent =
            Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = msg[position].toString()
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
    private fun createInterstitial() {
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

    }
}