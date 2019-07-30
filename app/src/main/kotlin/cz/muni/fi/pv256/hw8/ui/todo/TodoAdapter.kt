package cz.muni.fi.pv256.hw8.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv256.hw8.R
import cz.muni.fi.pv256.hw8.model.TodoItem
import cz.muni.fi.pv256.hw8.ui.helper.ItemTouchHelperAdapter
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(private val todoView: TodoView, private val onEditDialogListener: OnEditDialogListener)
    : RecyclerView.Adapter<TodoAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    var lastPositionRemoved = 0
        private set
    private var items: MutableList<TodoItem> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onItemDismiss(position: Int) {
        lastPositionRemoved = position
        items[position].also { todoView.onDismissItem(it) }
    }

    fun submitList(items: MutableList<TodoItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var name = itemView.item_name
        private var priority = itemView.item_priority

        fun bind(item: TodoItem) = with(itemView) {
            name.text = item.text
            priority.text = item.priority.toString()
            setOnLongClickListener {
                onEditDialogListener.onEditTodoItem(item)
                true
            }
        }
    }
}
