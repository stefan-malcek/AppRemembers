package cz.muni.fi.pv256.hw8.ui.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.muni.fi.pv256.hw8.db.TodoDatabase
import cz.muni.fi.pv256.hw8.model.TodoItem
import cz.muni.fi.pv256.hw8.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private var todoDatabase: TodoDatabase = TodoDatabase.getInstance(app)
    private val subscriptions = CompositeDisposable()

    val showSnackBarMessage = SingleLiveEvent<TodoItem>()
    val todoList = MutableLiveData<MutableList<TodoItem>>().apply { value = mutableListOf() }

    fun loadData() {
        subscriptions.add(todoDatabase.todoDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result -> todoList.value = result })
    }

    fun addItem(item: TodoItem, position: Int? = null) {
        todoList.value?.let { if (position == null) it.add(item) else it.add(position, item) }

        subscriptions.add(todoDatabase.todoDao().insert(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

        forceLiveDataChange(todoList)
    }

    fun editItem(old: TodoItem, new: TodoItem) {
        new.todoId = old.todoId
        todoList.value?.let { it[it.indexOf(old)] = new }

        subscriptions.add(todoDatabase.todoDao().update(new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

        forceLiveDataChange(todoList)
    }

    fun dismissItem(item: TodoItem) {
        todoList.value?.remove(item)

        subscriptions.add(todoDatabase.todoDao().delete(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())

        forceLiveDataChange(todoList)
        showSnackBarMessage.value = item
    }

    fun clear() {
        subscriptions.clear()
    }

    private fun <E> forceLiveDataChange(liveData: MutableLiveData<E>) {
        liveData.value = liveData.value
    }
}
