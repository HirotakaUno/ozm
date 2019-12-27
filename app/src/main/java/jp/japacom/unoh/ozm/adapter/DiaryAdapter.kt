package jp.japacom.unoh.ozm.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.japacom.unoh.ozm.R


class DiaryAdapter(private val myDataset: ArrayList<String>) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    protected var dataSet: List<String>? = null

    // [3]
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView

        init {
            textView = v.findViewById(R.id.textView)
        }
    }

    // [4]
    fun RecyclerViewAdapter(myDataSet: List<String>?) {
        dataSet = myDataSet
    }

    // [5]
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiaryAdapter.ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.diary_list_item, parent, false)
        return ViewHolder(v)
    }

    // [6]
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = dataSet!![position]
        holder.textView.text = text
    }

    // [7]
    override fun getItemCount(): Int {
        return dataSet!!.size
    }
}