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

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment

abstract class DetailFragment: Fragment() {

    abstract val vm: NewsReaderViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_view, menu)

        activity?.let {
            if(!(it as MainActivity).isMasterDetail()) {
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
            }
        }
    }

    override fun onDestroyOptionsMenu() {
        activity?.let {
            if(!(it as MainActivity).isMasterDetail())
                it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                // Override default Android "up" behavior to instead mimic the back button
                activity?.onBackPressed()
                true
            }

            // Share button
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_SUBJECT, vm.selectedArticle.title)
                    putExtra(Intent.EXTRA_TEXT, getShareableLink())
                    type = "text/plain"
                }

                intent.resolveActivity(requireContext().packageManager)?.let {
                    startActivity(Intent.createChooser(intent, getString(R.string.share_dialog)))
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getShareableLink() = vm.selectedArticle.link
}