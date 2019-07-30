package cz.muni.fi.pv256.hw8.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import cz.muni.fi.pv256.hw8.R
import cz.muni.fi.pv256.hw8.model.Priority
import cz.muni.fi.pv256.hw8.model.TodoItem
import cz.muni.fi.pv256.hw8.model.priorityValues
import kotlinx.android.synthetic.main.fragment_todo_item_dialog.*

/**
 * @author Marek Sabo
 */
class TodoItemDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "TodoItemDialogFragment"
        private const val EDIT_ITEM = "edit_item"

        fun newInstance(todoItem: TodoItem?) = TodoItemDialogFragment().apply {
            todoItem?.let { arguments = bundleOf(EDIT_ITEM to it) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_todo_item_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priority_options.adapter = ArrayAdapter<Priority>(requireContext(), android.R.layout.simple_list_item_1, priorityValues)
        todo_text.requestFocus()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val editableTodoItem = arguments?.getParcelable<TodoItem>(EDIT_ITEM)

        if (editableTodoItem == null) {
            dialog_title.text = resources.getText(R.string.create_new_todo)
            create_dialog.text = resources.getText(R.string.create)
        } else {
            dialog_title.text = resources.getText(R.string.edit_todo)
            create_dialog.text = resources.getText(R.string.edit)

            editableTodoItem.let {
                todo_text.setText(it.text)
                priority_options.setSelection(priorityValues.indexOf(it.priority))
            }
        }

        create_dialog.setOnClickListener { createOrEditTodoItem(editableTodoItem) }
        cancel_dialog.setOnClickListener { dismiss() }
    }

    private fun createOrEditTodoItem(editableItem: TodoItem?) {
        val todoText = todo_text.text?.toString()
        if (todoText.isNullOrBlank()) {
            val action = if (editableItem == null) "created" else "edited"
            Toast.makeText(context, "Todo text is blank, item not $action", Toast.LENGTH_LONG).show()
        } else {
            val priority = priority_options.selectedItem as Priority
            val newItem = TodoItem(todoText!!, priority)
            if (editableItem != null) {
                (targetFragment as OnEditDialogListener).onFinishEditing(editableItem, newItem)
            } else {
                (targetFragment as OnFinishDialogListener).onCreateTodoItem(newItem)
            }
        }
        dismiss()
    }
}
