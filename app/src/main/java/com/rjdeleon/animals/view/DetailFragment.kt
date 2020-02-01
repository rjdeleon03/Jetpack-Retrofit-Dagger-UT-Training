package com.rjdeleon.animals.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.rjdeleon.animals.databinding.FragmentDetailBinding
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.util.getProgressDrawable
import com.rjdeleon.animals.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private var mAnimal: Animal? = null
    private lateinit var mDataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mDataBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mAnimal = DetailFragmentArgs.fromBundle(it).animal
        }

        mAnimal?.imageUrl?.let {
            setupBackgroundColor(it)
        }

        mDataBinding.animal = mAnimal

    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightVibrantSwatch?.rgb ?: 0
                            mDataBinding.backgroundColor = intColor
                        }
                }
            })
    }

}
