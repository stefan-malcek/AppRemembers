package cz.muni.fi.pv256.hw8.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.pv256.hw8.R
import cz.muni.fi.pv256.hw8.ui.todo.TodoListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val todoListFragment = TodoListFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, todoListFragment)
                    .commit()
        }
    }
}
