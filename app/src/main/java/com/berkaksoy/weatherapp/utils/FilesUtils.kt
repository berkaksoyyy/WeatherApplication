package com.berkaksoy.weatherapp.utils

import timber.log.Timber
import java.io.File

object FilesUtils {

    fun deleteDirectoryTree(fileOrDirectory: File) {
        try {
            if (fileOrDirectory.isDirectory) {
                fileOrDirectory.listFiles()?.let { listFiles ->
                    for (child in listFiles) {
                        deleteDirectoryTree(child)
                    }
                }
            }
            fileOrDirectory.delete()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }
}