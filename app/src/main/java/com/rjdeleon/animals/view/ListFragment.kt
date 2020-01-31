package com.rjdeleon.animals.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.rjdeleon.animals.R
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var mViewModel: ListViewModel
    private val mListAdapter = AnimalListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        mViewModel.animals.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                animalRecyclerView.visibility = View.VISIBLE
                mListAdapter.updateAnimalList(list)
            }
        })

        mViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            if (loading) {
                errorTextView.visibility = View.GONE
                animalRecyclerView.visibility = View.GONE
            }
        })

        mViewModel.loadError.observe(viewLifecycleOwner, Observer { loadError ->
            errorTextView.visibility = if (loadError) View.VISIBLE else View.GONE

        })
        mViewModel.refresh()

        animalRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mListAdapter
        }

        refreshLayout.setOnRefreshListener {
            mViewModel.refresh()
            refreshLayout.isRefreshing = false
        }

    }
}
