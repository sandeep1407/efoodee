package com.uj.myapplications.interfaces

import java.io.File

interface IImageCompressTaskListener {
    fun onComplete(compressed: List<File>)
    fun onError(error: Throwable)
}