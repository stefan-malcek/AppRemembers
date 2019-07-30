package cz.muni.fi.pv256.hw8.ui.todo

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import cz.muni.fi.pv256.hw8.R
import cz.muni.fi.pv256.hw8.model.TodoItem
import cz.muni.fi.pv256.hw8.ui.helper.ItemTouchHelperCallback
import cz.muni.fi.pv256.hw8.util.GridDividerDecoration
import kotlinx.android.synthetic.main.fragment_todo_list.*

class TodoListFragment : Fragment(), TodoView, OnFinishDialogListener, OnEditDialogListener {

    companion object {
        fun newInstance() = TodoListFragment()
    }

    private val todoListAdapter by lazy { TodoAdapter(this, this) }
    private val viewModel by lazy { ViewModelProviders.of(this).get(TodoViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add.setOnClickListener { createDialog() }

        viewModel.loadData()

        viewModel.todoList.apply {
            observe(this@TodoListFragment, Observer { todoListAdapter.submitList(it) })
            value = viewModel.todoList.value
        }

        viewModel.showSnackBarMessage.observe(this, Observer { item ->
            Snackbar.make(view, item.text + " was deleted", Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { viewModel.addItem(item, todoListAdapter.lastPositionRemoved) }
                    .show()
        })

        todo_list.apply {
            val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

            if (isPortrait) {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            } else {
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(GridDividerDecoration(context))
            }

            emptyView = empty_view
            adapter = todoListAdapter

            ItemTouchHelper(ItemTouchHelperCallback(todoListAdapter)).attachToRecyclerView(this)
        }
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    override fun onDismissItem(item: TodoItem) = viewModel.dismissItem(item)

    override fun onCreateTodoItem(todoItem: TodoItem) = viewModel.addItem(todoItem)

    override fun onEditTodoItem(old: TodoItem) = createDialog(old)

    override fun onFinishEditing(old: TodoItem, new: TodoItem) = viewModel.editItem(old, new)

    private fun createDialog(todoItem: TodoItem? = null) = TodoItemDialogFragment
            .newInstance(todoItem)
            .apply { setTargetFragment(this@TodoListFragment, 0) }
            .show(fragmentManager, TodoItemDialogFragment.TAG)
}
