/* Copyright 2020 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.newsreader

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ScrollView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropTransformation

fun WebView.applyScrollViewFix(scrollView: ScrollView) {
    setOnTouchListener { _, _ ->
        val oldPos = intArrayOf(scrollView.scrollX, scrollView.scrollY)
        scrollView.post { scrollView.scrollTo(oldPos[0], oldPos[1]) }

        false
    }
}

fun Context.openLink(url: String, baseUrl: String = "") {
    val customTabs = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.windowBackground))
            .setStartAnimations(this, R.anim.cct_enter_from_right, R.anim.cct_exit_to_left)
            .setExitAnimations(this, R.anim.cct_enter_from_left, R.anim.cct_exit_to_right)
            .setShowTitle(true)
            .addDefaultShareMenuItem()
            .build()

    try {
        customTabs.launchUrl(this, Uri.parse("$baseUrl$url"))
    } catch (e: ActivityNotFoundException) {}
}

fun ImageView.loadFrom(url: Uri) {
    val picasso = Picasso.get().load(url)

    if(url.host == "i.ytimg.com")
        picasso.transform(CropTransformation(480, 270))
    else
        picasso.fit().centerInside()

    picasso.into(this)
}

fun String.sanitize() = replace("$$", "$")

fun String.fixUrl(): String {
    return if(Patterns.WEB_URL.matcher(split(" ").last()).matches())
        trimEnd('.')
    else
        this
}

fun String.fromHtml() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

fun Context.hexColor(res: Int) = "#${String.format("%x", ContextCompat.getColor(this, res)).takeLast(6)}"

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if(value) View.VISIBLE else View.GONE
    }