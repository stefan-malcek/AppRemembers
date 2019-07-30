package cz.muni.fi.pv256.hw8.ui.todo

import cz.muni.fi.pv256.hw8.model.TodoItem

interface OnFinishDialogListener {
    fun onCreateTodoItem(todoItem: TodoItem)
}
