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
import com.example.newsreader.model.NewsReaderArticle
import com.example.newsreader.model.NewsReaderItem
import com.example.newsreader.model.YouTubeAPIResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.HashMap

class YouTubeViewModel(app: Application): NewsReaderViewModel(app) {
    interface YouTubeAPI {
        @GET("v3/search")
        fun search(@QueryMap params: Map<String, String>): Single<YouTubeAPIResponse>
    }

    override fun refreshData() {
        val params = HashMap<String, String>()
        params["part"] = "snippet"
        params["channelId"] = "UCk1SpWNzOs4MYmr0uICEntg"
        params["maxResults"] = "20"
        params["order"] = "date"
        params["key"] = "insert YouTube API key here"

        disposables.add(Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(YouTubeAPI::class.java)
                .search(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val parsedList = response.items.map {
                        NewsReaderArticle(
                                title = it.snippet.title.fromHtml(),
                                description = it.snippet.description.fixUrl(),
                                thumbnail = it.snippet.thumbnails.high.url,
                                date = "",
                                content = "",
                                link = "https://www.youtube.com/watch?v=${it.id.videoId}")
                    }

                    items.value = NewsReaderItem(parsedList, true)
                }, { items.value = NewsReaderItem(items.value.articles, false) })
        )
    }
}