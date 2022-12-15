package com.example.diwithhilt.ui.activity

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diwithhilt.R
import com.example.diwithhilt.adapter.RecyclerAdapter
import com.example.diwithhilt.base.BaseCompactActivity
import com.example.diwithhilt.databinding.ActivityMainBinding
import com.example.diwithhilt.model.User
import com.example.diwithhilt.utils.Status
import com.example.diwithhilt.viewModel.ActivityMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseCompactActivity<ActivityMainBinding, ActivityMainViewModel>() {

    override val viewModel: ActivityMainViewModel by viewModels()
    private lateinit var adapter: RecyclerAdapter

    override fun initialize() {
        super.initialize()
        setup()
    }

    private fun setup() {
        adapter = RecyclerAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
        viewModel.getRecyclerListDataObserver().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    override fun getLayoutResID(): Int = R.layout.activity_main
}