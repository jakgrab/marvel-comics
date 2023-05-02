package com.example.feature_main.ui.screens.favourite.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.core.data.firestore_data.ComicsData
import com.example.feature_main.databinding.ComicItemBinding
import com.example.feature_main.ui.navigation.ComicScreens

class ComicAdapter(
    private val favouritesList: List<ComicsData>,
    private val navController: NavController
) : RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(val binding: ComicItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val comicsItemBinding =
            ComicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicViewHolder(comicsItemBinding)
    }

    override fun getItemCount(): Int {
        return favouritesList.count()
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.binding.cvComicItem.setOnClickListener {
            navController.navigate(ComicScreens.DetailsScreen.name)
        }
    }
}