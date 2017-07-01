package com.example.imagesviewer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

/**
 * <pre>
 *     author : YangBin
 *     e-mail : yangbinyhbn@gmail.com
 *     time   : 2017/6/21
 *     desc   :
 * </pre>
 */
class MainActivity : AppCompatActivity() {

    /**
     * data
     */
    val images = ArrayList<String>()

    /**
     * default grid view decoration width
     */
    val SPACE_DECORATION = 16


    companion object {
        val DEFAULT_PATH_PICTURE: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listImages.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = SPACE_DECORATION
                outRect.right = SPACE_DECORATION
                outRect.bottom = SPACE_DECORATION
                outRect.top = SPACE_DECORATION
            }
        })

        File(DEFAULT_PATH_PICTURE).listFiles { file ->
            file.isFile && file.isImageFile()
        }.sortedBy {
            -it.lastModified()
        }.forEach {
            images.add(it.path)
        }
        log("images: \n $images")

        listImages.adapter = ImageListAdapter(images, { path, position ->
            log("position: $position path : $path ")
            startActivity(Intent(this, ImageDisplayActivity::class.java).apply {
                putExtra(ImageDisplayActivity.ARG_PATH, path)
            })
        })


    }


    class ImageListAdapter(val images: ArrayList<String>
                           , val itemReaction: ((String, Int) -> Unit)? = null) : RecyclerView.Adapter<ImageListAdapter.Holder>() {


        override fun onBindViewHolder(holder: Holder, position: Int) {

            val path = images[position]

            holder.itemView.itemImage.setImagePath(path)

            holder.itemView.textTitle.text = File(path).nameWithoutExtension
            //add click reaction
            holder.itemView.itemImage.setOnClickListener {
                itemReaction?.invoke(path, position)
            }
        }

        override fun getItemCount(): Int = images.size

        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, null, false))


        class Holder(view: View) : RecyclerView.ViewHolder(view)

    }


}


