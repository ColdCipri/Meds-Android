package com.exam.exam0.page

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.exam.exam0.R
import com.exam.exam0.base.BaseActivity
import com.exam.exam0.data.local.LocalModel
import com.exam.exam0.utils.observeNonNull
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedActivity : BaseActivity<ViewModel>(), IdRecyclerViewCallback<LocalModel> {

    override val viewModel: AppViewModel by viewModel()
    private lateinit var ordersAdapter: MedAdapter
    private var hasInternet = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        observeOrdersLiveData()
        observeConnectivity()
        setupListeners()
    }

    private fun initRecyclerView() {
        ordersAdapter =
            MedAdapter(listOf(),this)
            with(rv_first_section) {
            adapter = ordersAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMedsRemote(this)
    }

    private fun observeConnectivity() {
        viewModel.getConnectivityStatus().observeNonNull(this) {
            hasInternet = it
            if (!it) {
                bt_add.isEnabled = false
                bt_refresh.isEnabled = false
                Toast.makeText(this, "No Internet!", Toast.LENGTH_SHORT).show()
            } else {
                bt_add.isEnabled = true
                bt_refresh.isEnabled = true
                Toast.makeText(this, "Internet!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeOrdersLiveData() {
        viewModel.getMedsLiveData().observeNonNull(this) {
            ordersAdapter.updateItems(it)
            ordersAdapter.notifyDataSetChanged()
        }
        viewModel.getLoadingLiveData().observeNonNull(this) {
            if (!it)
                progress_bar.visibility = View.VISIBLE
            else
                progress_bar.visibility = View.GONE
        }
    }

    private fun setupListeners() {
        bt_refresh.setOnClickListener {
            viewModel.getMedsRemote(this)
        }
        bt_add.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragment_container,
                AddMedFragment()
            )
            transaction.commit()
        }
    }

    override fun onItemSelected(item: LocalModel) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_container,
            DetailsFragment(item)
        )
        transaction.commit()
    }


}
