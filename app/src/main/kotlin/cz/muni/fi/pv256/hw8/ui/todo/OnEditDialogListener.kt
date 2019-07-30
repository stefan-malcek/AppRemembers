package cz.muni.fi.pv256.hw8.ui.todo

import cz.muni.fi.pv256.hw8.model.TodoItem

interface OnEditDialogListener {
    fun onEditTodoItem(old: TodoItem)
    fun onFinishEditing(old: TodoItem, new: TodoItem)
}
