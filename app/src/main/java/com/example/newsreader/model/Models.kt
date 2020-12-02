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

package com.example.newsreader.model

import com.squareup.moshi.JsonClass

class NewsReaderItem(val articles: List<NewsReaderArticle>, val success: Boolean)
data class NewsReaderArticle(val title: String,
                       val description: String,
                       val thumbnail: String,
                       val date: String,
                       val content: String,
                       val link: String)

@JsonClass(generateAdapter = true)
class Thumbnail(val url: String, val width: Int, val height: Int)

@JsonClass(generateAdapter = true)
class Id(val kind: String, val videoId: String)

@JsonClass(generateAdapter = true)
class Item(val kind: String, val etag: String, val id: Id, val snippet: Snippet)

@JsonClass(generateAdapter = true)
class PageInfo(val totalResults: Int, val resultsPerPage: Int)

@JsonClass(generateAdapter = true)
class ThumbnailSizes(val default: Thumbnail, val medium: Thumbnail, val high: Thumbnail)

@JsonClass(generateAdapter = true)
class YouTubeAPIResponse(val kind: String,
                         val etag: String,
                         val nextPageToken: String,
                         val regionCode: String,
                         val pageInfo: PageInfo,
                         val items: List<Item>)

@JsonClass(generateAdapter = true)
class Snippet(val publishedAt: String,
              val channelId: String,
              val title: String,
              val description: String,
              val thumbnails: ThumbnailSizes,
              val channelTitle: String,
              val liveBroadcastContent: String)