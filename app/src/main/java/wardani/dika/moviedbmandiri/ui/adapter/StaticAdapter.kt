package wardani.dika.moviedbmandiri.ui.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class StaticAdapter<V: RecyclerView.ViewHolder, T> : RecyclerView.Adapter<V>() {
    protected var items: List<T> = emptyList()

    final override fun getItemCount(): Int {
        return items.size
    }

    fun changeItems(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }
}