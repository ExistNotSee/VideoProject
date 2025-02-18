package com.voidcom.v_base.utils

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.ArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.voidcom.v_base.R

object PermissionsUtils {
    private val TAG = PermissionsUtils::class.java.simpleName

    fun getStringFormRequestType(mContext: Context, type: Int): String {
        return when (type) {
            AppCode.requestReadStorage,
            AppCode.requestWriteStorage -> mContext.getString(R.string.requestPermission_readStorageMessage)

            AppCode.requestReadPhoneState -> mContext.getString(R.string.requestPermission_readPhoneStateMessage)
            AppCode.requestRecordAudio -> mContext.getString(R.string.requestPermission_recordAudioMessage)
            AppCode.requestAudioSettings -> mContext.getString(R.string.requestPermission_audioSettingsMessage)
            AppCode.requestCamera -> mContext.getString(R.string.requestPermission_cameraMessage)
            AppCode.requestLocation -> mContext.getString(R.string.requestPermission_locationMessage)
            else -> mContext.getString(R.string.requestPermission_notFoundMessage)
        }
    }

    fun getPermissionsFormRequestType(vararg type: Int): Array<String> {
        val array = Array(type.size, init = { "" })
        for (i in type.indices) {
            array[i] = when (type[i]) {
                AppCode.requestReadStorage -> Manifest.permission.READ_EXTERNAL_STORAGE
                AppCode.requestWriteStorage -> Manifest.permission.WRITE_EXTERNAL_STORAGE
                AppCode.requestReadPhoneState -> Manifest.permission.READ_PHONE_STATE
                AppCode.requestRecordAudio -> Manifest.permission.RECORD_AUDIO
                AppCode.requestAudioSettings -> Manifest.permission.MODIFY_AUDIO_SETTINGS
                AppCode.requestCamera -> Manifest.permission.CAMERA
                AppCode.requestLocation -> Manifest.permission.ACCESS_COARSE_LOCATION
                else -> continue
            }
        }
        return array
    }

    fun gotoPermissionSetting(context: Context, applicationID: String) {
        val brand: String = Build.BRAND //手机厂商
        when (brand.lowercase()) {
            "redmi", "xiaomi" -> gotoMiuiPermission(context) //小米
            "meizu" -> gotoMeizuPermission(context, applicationID)
            "huawei", "honor" -> gotoHuaweiPermission(context)
            else -> context.startActivity(getAppDetailSettingIntent(context))
        }
    }

    fun checkPermission(context: Context, vararg type: Int): Map<String, Boolean> {
        return checkPermission(context, getPermissionsFormRequestType(*type))
    }

    /**
     * @return 如果返回的是空map，表示有权限。其他情况都是没有权限或部分没有权限
     */
    fun checkPermission(context: Context, array: Array<String>): Map<String, Boolean> {
        array.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_DENIED
            ) {
                return ArrayMap<String, Boolean>().apply {
                    set(it, false)
                }
            }
        }
        return emptyMap()
    }

    /**
     * @param
     * @return true 不再显示请求权限信息
     */
    fun doNotShowAgain(context: Activity, s: String): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(context, s)

    /**
     * 跳转到miui的权限管理页面
     */
    private fun gotoMiuiPermission(context: Context) {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            putExtra("extra_pkgname", context.packageName)
        }
        try { // MIUI 8
            context.startActivity(intent.apply {
                setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity"
                )
            })
        } catch (e: Exception) {
            try { // MIUI 5/6/7
                context.startActivity(intent.apply {
                    setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                    )
                })
            } catch (e1: Exception) { // 否则跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context))
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private fun gotoMeizuPermission(context: Context, applicationID: String) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", applicationID)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            context.startActivity(getAppDetailSettingIntent(context))
        }
    }

    /**
     * 华为的权限管理页面
     */
    private fun gotoHuaweiPermission(context: Context) {
        try {
            context.startActivity(Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                component = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity"
                )//华为权限管理
            })
        } catch (e: Exception) {
            e.printStackTrace()
            context.startActivity(getAppDetailSettingIntent(context))
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     */
    private fun getAppDetailSettingIntent(context: Context): Intent = Intent().apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        data = Uri.fromParts("package", context.packageName, null)
    }
}