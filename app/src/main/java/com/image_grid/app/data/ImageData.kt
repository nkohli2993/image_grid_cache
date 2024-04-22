package com.image_grid.app.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    val id: String,
    val title: String,
    val language: String,
    val thumbnail: ThumbnailData,
    val mediaType: String,
    val coverageURL: String,
    val publishedAt: String,
    val publishedBy: String,
    val backupDetails: BackupDetailsData,

    ) : Parcelable

@Parcelize
data class ThumbnailData(
    val id: String,
    val version: String,
    val domain: String,
    val basePath: String,
    val key: String,
    val qualities: Array<String>,
    val aspectRatio: String,
) : Parcelable

@Parcelize
data class BackupDetailsData(
    val pdfLink: String,
    val screenshotURL: String,

    ) : Parcelable
