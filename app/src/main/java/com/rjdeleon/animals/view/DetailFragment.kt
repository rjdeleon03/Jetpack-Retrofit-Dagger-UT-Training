package com.rjdeleon.animals.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.rjdeleon.animals.R
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.util.getProgressDrawable
import com.rjdeleon.animals.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var mAnimal: Animal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mAnimal = DetailFragmentArgs.fromBundle(it).animal
        }

        context?.let {
            animalImage.loadImage(mAnimal?.imageUrl, getProgressDrawable(it))
        }

        animalName.text = mAnimal?.name
        animalLocation.text = mAnimal?.location
        animalLifespan.text = mAnimal?.lifeSpan
        animalDiet.text = mAnimal?.diet

    }

}
