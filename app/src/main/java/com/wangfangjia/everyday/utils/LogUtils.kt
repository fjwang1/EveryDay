package com.wangfangjia.everyday.utils

import android.util.Log

object LogUtils {
    const val TAG = "EveryDay"
    fun d(tag: String, msg: String) {
        Log.d("${TAG}_$tag", msg)
    }

    fun e(tag: String, msg: String) {
        Log.e("${TAG}_$tag", msg)
    }

    fun w(tag: String, msg: String) {
        Log.w("${TAG}_$tag", msg)
    }
}