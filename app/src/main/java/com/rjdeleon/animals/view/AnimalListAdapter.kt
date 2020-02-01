package com.rjdeleon.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rjdeleon.animals.R
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.util.getProgressDrawable
import com.rjdeleon.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val mAnimalList: ArrayList<Animal>)
    : RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        val viewHolder = AnimalViewHolder(view)
        viewHolder.setOnClickListener {
            val action = ListFragmentDirections.actionGoToDetail(mAnimalList[it])
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

    class AnimalViewHolder(private val mItemView: View)
        : RecyclerView.ViewHolder(mItemView) {

        fun bindData(animal: Animal) {
            mItemView.animalName.text = animal.name
            mItemView.animalImage
                .loadImage(animal.imageUrl, getProgressDrawable(mItemView.context))
        }

        fun setOnClickListener(listener: (Int) -> Unit) {
            mItemView.setOnClickListener { listener(adapterPosition) }
        }

    }
}