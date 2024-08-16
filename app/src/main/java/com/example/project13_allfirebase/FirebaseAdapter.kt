package com.example.project13_allfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project13_allfirebase.databinding.ItemViewBinding

class FirebaseAdapter(private val mList: MutableList<FireData>): RecyclerView.Adapter<FirebaseAdapter.firebaseViewHolder>() {
    inner class firebaseViewHolder(val binding: ItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        var name: TextView =binding.txtName
        var stt: TextView =binding.txtStt
        var click: LinearLayout = binding.ClickRv
        var imView: ImageView = binding.imView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): firebaseViewHolder {
        val view = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return firebaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: firebaseViewHolder, position: Int) {
        var mList = mList[position]
        Glide.with(holder.imView.context)
            .load(mList.img)
            .placeholder(R.drawable.img_1)
            .into(holder.imView)

        holder.click.setOnClickListener {
            val intent = Intent(holder.imView.context, UpdateActivity::class.java)
            intent.putExtra("id", mList.id)
            intent.putExtra("name", mList.name)
            intent.putExtra("stt", mList.stt)
            intent.putExtra("img", mList.img)
            holder.imView.context.startActivity(intent)
        }

        holder.name.text = mList.name
        holder.stt.text = mList.stt
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}