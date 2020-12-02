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

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_social.*

class SocialFragment: MasterFragment(), NewsReaderWebViewClient.Interface {
    override val layoutId = R.layout.fragment_social

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!isHidden) (activity as MainActivity).showProgress(true)

        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.setOnRefreshListener {
            socialWebView.reload()
            swipeRefreshLayout.isRefreshing = false
        }

        socialWebView.applyScrollViewFix(socialScrollView)
        socialWebView.setBackgroundColor(Color.TRANSPARENT)
        socialWebView.settings.javaScriptEnabled = true
     // socialWebView.settings.allowUniversalAccessFromFileURLs = true
        socialWebView.webViewClient = NewsReaderWebViewClient(this)
        socialWebView.loadUrl("file:///android_asset/social.html")
    }

    override fun onPageFinished() {
        (activity as MainActivity?)?.showProgress(false)
        swipeRefreshLayout?.isEnabled = true
    }

    override fun context() = requireContext()
    override fun onOverflowMenuRefresh() = socialWebView.reload()
}