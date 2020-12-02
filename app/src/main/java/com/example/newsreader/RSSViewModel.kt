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
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.OnTaskCompleted
import com.prof.rssparser.Parser
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class RSSViewModel(val app: Application): NewsReaderViewModel(app) {

    lateinit var baseUrl: String
    lateinit var rssFeedPath: String

    override fun refreshData() {
        disposables.add(Single.create(SingleOnSubscribe<List<Article>> {
            val parser = Parser.Builder().build()
            parser.onFinish(object: OnTaskCompleted {
                override fun onTaskCompleted(channel: Channel) = it.onSuccess(channel.articles)
                override fun onError(e: Exception) {
                    it.tryOnError(e)
                }
            })

            parser.execute("$baseUrl$rssFeedPath")
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> parseArticles(list) }, { parseArticles(listOf()) })
        )
    }

    private fun parseArticles(articles: List<Article>) {
        disposables.add(Single.create(SingleOnSubscribe<List<NewsReaderArticle>> { e ->
            val parsedList = ArrayList<NewsReaderArticle>()

            for(article in articles) {
                val document = Jsoup.parse(article.description).apply { setBaseUri(baseUrl) }

                val maxLength = 500
                val description = if(document.text().length > maxLength)
                    "${document.text().substring(0..maxLength)}\u2026"
                else
                    document.text()

             // val img = document.selectFirst("p")?.selectFirst("img")
                val img = Jsoup.parse(article.content).selectFirst("img")
                var thumbnail = ""

                img?.let {
                    val src = it.absUrl("src")
                    if(src.isNotEmpty())
                        thumbnail = src
                }

                val link = if(article.link?.startsWith("/") == true)
                    "$baseUrl${article.link}"
                else
                    article.link

                val date = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US)
                        .parse(article.pubDate ?: "")

                parsedList.add(NewsReaderArticle(
                        title = article.title?.sanitize() ?: "",
                        description = description.sanitize(),
                        thumbnail = thumbnail,
                        date = SimpleDateFormat("MMM d, yyyy, hh:mm a", Locale.US)
                                .format(date ?: Date()),
                        content = parseContent(article.content ?: "").sanitize(),
                        link = link ?: ""
                ))
            }

            if(parsedList.isNotEmpty())
                e.onSuccess(parsedList)
            else
                e.tryOnError(Throwable())
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> items.value = NewsReaderItem(list, true) },
                        { items.value = NewsReaderItem(items.value.articles, false) })
        )
    }

    private fun parseContent(content: String): String {
        val document = Jsoup.parse(content).apply { setBaseUri(baseUrl) }

        document.head()
                .append("<link href=\"https://fonts.googleapis.com/css?family=Libre+Franklin\" rel=\"stylesheet\">")

        document.select("img")
                .attr("style", "max-width: 100%; height: auto")

        document.select("iframe")
                .attr("style", "max-width: 100%")

        document.select("a")
                .attr("style", "color: ${app.hexColor(R.color.colorPrimary)}")

        document.select("body")
                .attr("style", "font-family: 'Libre Franklin', sans-serif; color: ${app.hexColor(R.color.article_text)}")

        return document.html()
    }
}
