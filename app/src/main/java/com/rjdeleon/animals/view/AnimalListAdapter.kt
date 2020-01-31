package com.rjdeleon.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rjdeleon.animals.R
import com.rjdeleon.animals.model.Animal
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val mAnimalList: ArrayList<Animal>)
    : RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
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

    class AnimalViewHolder(private val mItemView: View)
        : RecyclerView.ViewHolder(mItemView) {

        fun bindData(animal: Animal) {
            mItemView.animalName.text = animal.name
        }

    }
}