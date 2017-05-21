package bj.discogsbrowser.model.search

import bj.discogsbrowser.model.common.Pagination
import com.google.gson.annotations.SerializedName

/**
 * Created by Josh Laird on 19/05/2017.
 */
data class RootSearchResponse(val pagination: Pagination, @SerializedName("results") val searchResults: List<SearchResult>)