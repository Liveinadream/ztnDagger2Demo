package com.ztn.app.ui.album

import android.util.SparseArray
import java.util.*

/**
 * Created by 冒险者ztn on 2020/3/12.
 * 相册 entry 类
 */

class AudioEntry {
    var id: Long = 0
    var author: String? = null
    var title: String? = null
    var genre: String? = null
    var duration = 0
    var path: String? = null
}

class AlbumEntry(var bucketId: Int, var bucketName: String, var coverPhoto: PhotoEntry) {
    var videoOnly = false
    var photos = ArrayList<PhotoEntry>()
    var photosByIds = SparseArray<PhotoEntry>()
    fun addPhoto(photoEntry: PhotoEntry) {
        photos.add(photoEntry)
        photosByIds.put(photoEntry.imageId, photoEntry)
    }

}

class PhotoEntry(
    var bucketId: Int,
    var imageId: Int,
    var dateTaken: Long,
    var path: String,
    orientation: Int,
    isVideo: Boolean,
    var width: Int,
    var height: Int,
    var size: Long) {

    var duration = 0
    var orientation = 0
    var thumbPath: String? = null
    var imagePath: String? = null
    var isVideo: Boolean
    var caption: CharSequence? = null
    var isFiltered = false
    var isPainted = false
    var isCropped = false
    var isMuted = false
    var ttl = 0
    var canDeleteAfter = false

    fun reset() {
        isFiltered = false
        isPainted = false
        isCropped = false
        ttl = 0
        imagePath = null
        if (!isVideo) {
            thumbPath = null
        }
        caption = null
    }

    init {
        if (isVideo) {
            duration = orientation
        } else {
            this.orientation = orientation
        }
        this.isVideo = isVideo
    }
}
