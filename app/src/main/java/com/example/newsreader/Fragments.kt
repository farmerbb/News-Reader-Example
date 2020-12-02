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

import androidx.lifecycle.ViewModelProvider

abstract class RSSFragment: ArticleListFragment() {
    abstract val baseUrl: String
    abstract val rssFeedPath: String

    override fun createViewModel(): NewsReaderViewModel {
        val vm = ViewModelProvider(requireActivity()).get(RSSViewModel::class.java)
        vm.baseUrl = baseUrl
        vm.rssFeedPath = rssFeedPath

        return vm
    }

    override fun createDetailFragment() = RSSDetailFragment()
}

class HomeFragment: RSSFragment() {
    override val baseUrl = "https://www.xda-developers.com"
    override val rssFeedPath = "/feed"
}

class YouTubeListFragment: ArticleListFragment() {
    override fun createViewModel() = ViewModelProvider(requireActivity()).get(YouTubeViewModel::class.java)
    override fun createDetailFragment() = YouTubeDetailFragment()
}