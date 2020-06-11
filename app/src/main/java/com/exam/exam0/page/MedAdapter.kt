package com.exam.exam0.page

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exam.exam0.R
import com.exam.exam0.data.local.LocalModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_main_adapter.view.*
import java.lang.Exception


class MedAdapter(
    private var items: List<LocalModel>,
    private val callback: IdRecyclerViewCallback<LocalModel>
) :
    RecyclerView.Adapter<MedAdapter.ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_main_adapter,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: List<LocalModel>) {
        items = newItems
    }

    inner class ListItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: LocalModel) {
            containerView.apply {

                try {
                    val bitmap = BitmapFactory.decodeByteArray(item.picture, 0, item.picture.size)

                    image_list.setImageBitmap(bitmap)
                    name_textview_list.text = item.name
                    best_before_textview_list.text = item.best_before.toString()
                } catch (ex : Exception) {
                    image_list.setBackgroundResource(R.drawable.coldrex)
                    name_textview_list.text = item.name
                    best_before_textview_list.text = item.best_before.toString()
                }

                details_button.setOnClickListener {
                    callback.onItemSelected(item)
                }
            }
        }
    }
}
