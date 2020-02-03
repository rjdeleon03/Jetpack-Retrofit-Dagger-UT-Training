package com.rjdeleon.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rjdeleon.animals.R
import com.rjdeleon.animals.databinding.ItemAnimalBinding
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.util.getProgressDrawable
import com.rjdeleon.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val mAnimalList: ArrayList<Animal>)
    : RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val dataBinding = ItemAnimalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = AnimalViewHolder(dataBinding)
        viewHolder.setOnClickListener { index, view ->
            val action = ListFragmentDirections.actionGoToDetail(mAnimalList[index])
            Navigation.findNavController(view).navigate(action)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bindData(mAnimalList[position])
    }

    override fun getItemCount() = mAnimalList.size

    fun updateAnimalList(newAnimalList: List<Animal>) {
        mAnimalList.clear()
        mAnimalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    class AnimalViewHolder(private val mDataBinding: ItemAnimalBinding)
        : RecyclerView.ViewHolder(mDataBinding.root) {

        fun bindData(animal: Animal) {
            mDataBinding.animal = animal
        }

        fun setOnClickListener(listener: (Int, View) -> Unit) {
            mDataBinding.listener = object: AnimalClickListener {
                override fun onClick(v: View) {
                    listener(adapterPosition, v)
                }
            }
        }

    }
}