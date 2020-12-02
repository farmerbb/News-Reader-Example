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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsreader.model.NewsReaderArticle
import com.example.newsreader.model.NewsReaderItem
import io.reactivex.disposables.CompositeDisposable

object NewsReaderViewModelCache: HashMap<String, NonNullLiveData<NewsReaderItem>>()

abstract class NewsReaderViewModel(app: Application): AndroidViewModel(app) {
    private val cache = NewsReaderViewModelCache
    protected val disposables = CompositeDisposable()

    val items: NonNullLiveData<NewsReaderItem> by lazy {
        cache.getOrPut(javaClass.name) { NonNullLiveData(NewsReaderItem(arrayListOf(), true)) }
    }

    lateinit var selectedArticle: NewsReaderArticle
    fun isInitialized() = ::selectedArticle.isInitialized

    abstract fun refreshData()

    override fun onCleared() = disposables.dispose()
}

class NonNullLiveData<T>(initValue: T): MutableLiveData<T>() {
    init {
        value = initValue
    }

    override fun getValue() = super.getValue()!!
}