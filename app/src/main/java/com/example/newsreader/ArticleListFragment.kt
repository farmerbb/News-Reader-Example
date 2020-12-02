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
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_article_list.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsreader.model.NewsReaderArticle
import com.example.newsreader.model.NewsReaderItem
import kotlinx.android.synthetic.main.row.view.*

abstract class ArticleListFragment: MasterFragment() {
    private val vm: NewsReaderViewModel by lazy { createViewModel() }
    private var firstRefresh = true
    var currentItem = 0

    override val layoutId = R.layout.fragment_article_list

    inner class NewsReaderItemAdapter(private val dataset: List<NewsReaderArticle>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = object: RecyclerView.ViewHolder(View.inflate(activity, R.layout.row, null)) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val article = dataset[position]
            val view = holder.itemView

            view.title.text = article.title
            view.description.text = article.description

            if(article.thumbnail.isNotEmpty()) {
                view.image.isVisible = true
                view.image.loadFrom(Uri.parse(article.thumbnail))
            } else {
                view.image.setImageDrawable(null)
                view.image.isVisible = false
            }

            view.divider.isVisible = position != 0

            view.setOnClickListener {
                currentItem = position
                viewArticle(dataset[position])
            }
        }

        override fun getItemCount() = dataset.size
    }

    abstract fun createViewModel(): NewsReaderViewModel
    abstract fun createDetailFragment(): DetailFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firstRefresh = true

        val observer = Observer<NewsReaderItem> { response ->
            response?.let {
                when(it.success) {
                    true -> recyclerView.adapter = NewsReaderItemAdapter(it.articles)
                    false -> Toast.makeText(activity, R.string.unable_to_load, Toast.LENGTH_SHORT).show()
                }

                initPullToRefresh(it.articles)
            }
        }

        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        vm.items.observe(viewLifecycleOwner, observer)

        val items = vm.items.value.articles
        if(items.isEmpty()) {
            (activity as MainActivity).showProgress(true)
            swipeRefreshLayout.isEnabled = false
            vm.refreshData()
        } else {
            recyclerView.adapter = NewsReaderItemAdapter(items)
            initPullToRefresh(items)
        }
    }

    private fun initPullToRefresh(items: List<NewsReaderArticle>) {
        if(items.isNotEmpty()) {
            if(firstRefresh) {
                firstRefresh = false

                (activity as MainActivity).showProgress(false)

                swipeRefreshLayout.isEnabled = true
                swipeRefreshLayout.setOnRefreshListener { vm.refreshData() }

                if((activity as MainActivity).isMasterDetail())
                    viewArticle(items[currentItem])
            } else
                swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onOverflowMenuRefresh() {
        if(!(activity as MainActivity).isRefreshing() || !swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = true
            vm.refreshData()
        }
    }

    private fun viewArticle(article: NewsReaderArticle) {
        vm.selectedArticle = article
        (activity as MainActivity).viewArticle(createDetailFragment())
    }
}