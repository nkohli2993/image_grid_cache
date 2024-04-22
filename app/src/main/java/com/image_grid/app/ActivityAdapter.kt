package com.image_grid.app
import com.image_grid.app.base.BaseAdapter
import com.image_grid.app.base.BaseViewHolder
import com.image_grid.app.data.ImageData
import com.image_grid.app.databinding.AdapterImageBinding

class ActivityAdapter(
    val imageUrls: ArrayList<ImageData>,
    val imageLoader: ImageLoader
) : BaseAdapter<AdapterImageBinding>() {
    override fun getLayoutRes()= R.layout.adapter_image
    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val binding = holder.binding as AdapterImageBinding
        imageLoader.DisplayImage(imageUrls[position].thumbnail.domain+"/"+imageUrls[position].thumbnail.basePath+"/0/"+imageUrls[position].thumbnail.key,binding.imageIV)
    }

    override fun getItemCount(): Int {
        return  imageUrls.size
    }


}