package com.imanancin.storyapp1.ui.stories


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.imanancin.storyapp1.data.remote.response.StoryItem
import com.imanancin.storyapp1.databinding.ItemStoriesBinding

class StoriesAdapter : ListAdapter<StoryItem, StoriesAdapter.ViewHolder>(DIFF){
    lateinit var itemClickListener:OnClickListener

    inner class ViewHolder(private val view: ItemStoriesBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(item: StoryItem) {
            val progressBar = view.progressBar
            progressBar.visibility = View.VISIBLE

            view.tvItemName.text = item.name
            itemView.apply {
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(view.ivItemPhoto)

                setOnClickListener {
                    itemClickListener.onClick(item, view)
                }

            }
        }
    }

    fun setOnClickListener(clickListener:OnClickListener){
        this.itemClickListener = clickListener
    }

    interface OnClickListener{
        fun onClick(data: StoryItem?,view: ItemStoriesBinding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object {
        val DIFF: DiffUtil.ItemCallback<StoryItem> = object : DiffUtil.ItemCallback<StoryItem> () {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}