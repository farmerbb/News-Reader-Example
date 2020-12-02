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
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.master_detail.*

class MainActivity: AppCompatActivity() {

    private var currentMasterFragment: Fragment? = null
    private var currentSocialFragment: Fragment? = null

    private var lastItemSelected = R.id.navigation_home
    private var layoutType = ""
    fun isMasterDetail() = layoutType == "master-detail"

    private val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if(!isMasterDetail() || item.itemId != lastItemSelected) {
            resetBackStack()

            when(item.itemId) {
                R.id.navigation_home -> home()
                R.id.navigation_social -> social()
                R.id.navigation_youtube -> youtube()
            }

            lastItemSelected = item.itemId
        }

        true
    }

    private val homeFragment: Fragment by lazy { HomeFragment() }
    private val youTubeFragment: Fragment by lazy { YouTubeListFragment() }

    private fun home() = fragmentTransaction(homeFragment, "Home", null)
    fun youtube() = fragmentTransaction(youTubeFragment, "YouTube", R.string.videos)

    private fun fragmentTransaction(fragment: Fragment, tag: String, titleRes: Int?) {
        title = getString(R.string.title, if(titleRes == null) "" else getString(titleRes))
        currentMasterFragment = fragment

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(resources.getInteger(R.integer.fragment_transition))
        transaction.replace(R.id.master, fragment, tag)

        if(currentSocialFragment?.isAdded == true)
            transaction.hide(currentSocialFragment!!)

        transaction.commit()
    }

    private fun social() {
        title = getString(R.string.social)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(resources.getInteger(R.integer.fragment_transition))

        currentMasterFragment?.let { transaction.remove(it) }

        if(currentSocialFragment == null)
            currentSocialFragment = SocialFragment()

        if(currentSocialFragment?.isAdded == true)
            transaction.show(currentSocialFragment!!)
        else
            transaction.add(R.id.social, currentSocialFragment!!, "Social")

        transaction.commit()
    }

    fun viewArticle(fragment: DetailFragment) {
        resetBackStack()

        val transaction = supportFragmentManager.beginTransaction()
        if(!isMasterDetail()) {
            transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            )

            transaction.remove(currentMasterFragment!!)
        }

        transaction.replace(R.id.detail, fragment, "ArticleView")
                .addToBackStack(null)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WebViewInitState.initialize(this)

        savedInstanceState?.let {
            title = it.getCharSequence("title")
            lastItemSelected = it.getInt("last_item_selected")
            layoutType = it.getString("layout_type")?: ""
        }

        if(layoutType.isNotEmpty() && layoutType != container.tag)
            resetBackStack()

        layoutType = container.tag as String

        toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_more_vert_black_24dp)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(listener)

        if(savedInstanceState == null) {
            if(intent.data != null)
                handleUri(intent.data!!)
            else
                home()
        } else {
            currentMasterFragment = supportFragmentManager.findFragmentById(R.id.master)
            currentSocialFragment = supportFragmentManager.findFragmentById(R.id.social)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.data?.let { handleUri(it) }
    }

    private fun handleUri(uri: Uri) {
        uri.host?.let {
            if(it.contains("example.com")) {
                navigation.selectedItemId = R.id.navigation_home
                home()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence("title", title)
        outState.putInt("last_item_selected", lastItemSelected)
        outState.putString("layout_type", layoutType)

        super.onSaveInstanceState(outState)
    }

    fun showProgress(show: Boolean) {
        progress.isVisible = show
    }

    fun resetBackStack() = supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    fun isRefreshing() = progress.isVisible

    override fun onBackPressed() {
        if(isMasterDetail())
            resetBackStack()

        super.onBackPressed()
    }
}