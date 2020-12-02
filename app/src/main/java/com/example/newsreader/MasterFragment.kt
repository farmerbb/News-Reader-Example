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
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

abstract class MasterFragment: Fragment() {
    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = View.inflate(activity, layoutId, null)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.article_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.refresh -> {
                onOverflowMenuRefresh()
                true
            }

            R.id.about -> {
                val dialog = AlertDialog.Builder(requireContext())
                        .setTitle(R.string.about_dialog_title)
                        .setMessage(R.string.about_dialog_message)
                        .setNegativeButton(R.string.action_licenses) { _, _ ->
                            startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
                        }
                        .setPositiveButton(R.string.action_ok, null)
                        .create()

                dialog.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun onOverflowMenuRefresh()
}