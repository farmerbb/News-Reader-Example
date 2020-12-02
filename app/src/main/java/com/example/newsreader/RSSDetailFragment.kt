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
import android.view.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_article_view.*

class RSSDetailFragment: DetailFragment(), NewsReaderWebViewClient.Interface {

    override val vm: RSSViewModel by lazy {
        ViewModelProvider(requireActivity()).get(RSSViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = View.inflate(activity, R.layout.fragment_article_view, null)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!vm.isInitialized()) {
            (activity as MainActivity).resetBackStack()
            return
        }

        article_title.text = vm.selectedArticle.title
        article_date.text = getString(R.string.date, vm.selectedArticle.date)

        article_content.applyScrollViewFix(scrollView)
        article_content.setBackgroundColor(Color.TRANSPARENT)
        article_content.settings.javaScriptEnabled = true
        article_content.webViewClient = NewsReaderWebViewClient(this)
        article_content.loadDataWithBaseURL(
                vm.baseUrl,
                vm.selectedArticle.content,
                "text/html",
                "UTF-8",
                null
        )
    }

    override fun context() = requireContext()
    override fun onPageFinished() {}
}