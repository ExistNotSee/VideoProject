package com.voidcom.videoproject

import android.util.DisplayMetrics
import com.voidcom.v_base.BaseApplication

/**
 * Created by voidcom on 2022/4/17 16:00
 * Description:
 */
class VideoApplication : BaseApplication() {
    companion object {
        lateinit var displayMetrics: DisplayMetrics
    }

    override fun onCreate() {
        super.onCreate()
        displayMetrics=resources.displayMetrics
    }
}