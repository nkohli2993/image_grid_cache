package com.image_grid.app

import android.content.Context
import android.os.Environment
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File? = null

    init {
        //Find the dir to save cached images
        cacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!cacheDir!!.exists()) cacheDir!!.mkdirs()
    }

    fun getFile(url: String): File {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        val filename = url.hashCode().toString()
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        return File(cacheDir, filename)
    }

    fun clear() {
        val files = cacheDir!!.listFiles() ?: return
        for (f in files) f.delete()
    }
}