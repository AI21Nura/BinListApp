package com.ainsln.core.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import jakarta.inject.Inject

interface IntentSender {
    fun showMap(latitude: Double, longitude: Double): Boolean
    fun call(phone: String): Boolean
    fun openWebPage(url: String): Boolean
}

class BaseIntentSender @Inject constructor(
    private val context: Context
) : IntentSender {

    override fun showMap(latitude: Double, longitude: Double): Boolean {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:$latitude,$longitude?z=11")
        )
        return startActivityIfCanResolve(intent)
    }

    override fun call(phone: String): Boolean {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$phone")
        )
        return startActivityIfCanResolve(intent)
    }

    override fun openWebPage(url: String): Boolean {
        var newUrl = url
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            newUrl = "http://"+ url

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(newUrl)
        )
        return startActivityIfCanResolve(intent)
    }

    private fun startActivityIfCanResolve(intent: Intent): Boolean {
        return with(context){
            if (intent.resolveActivity(packageManager) != null){
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            } else
                false
        }
    }
}
