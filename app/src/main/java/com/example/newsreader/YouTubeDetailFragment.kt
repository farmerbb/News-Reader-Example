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

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_youtube_view.*

class YouTubeDetailFragment: DetailFragment() {

    override val vm: YouTubeViewModel by lazy {
        ViewModelProvider(requireActivity()).get(YouTubeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = View.inflate(activity, R.layout.fragment_youtube_view, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!vm.isInitialized()) {
            (activity as MainActivity).resetBackStack()
            return
        }

        thumbnail.loadFrom(Uri.parse(vm.selectedArticle.thumbnail))
        thumbnail.setOnClickListener { onThumbnailClick() }
        watch.setOnClickListener { onThumbnailClick() }
        video_description.text = vm.selectedArticle.description
    }

    private fun onThumbnailClick() = requireContext().openLink(getShareableLink())
}