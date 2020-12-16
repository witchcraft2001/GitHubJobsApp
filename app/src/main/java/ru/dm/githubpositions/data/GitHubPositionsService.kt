package ru.dm.githubpositions.data

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dm.githubpositions.data.models.Position

interface GitHubPositionsService {
    companion object{
        fun getService() : GitHubPositionsService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://jobs.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GitHubPositionsService::class.java)
        }
    }

    @GET("positions.json")
    fun getPositions(
        @Query("page") page: Int
    ): Single<List<Position>>
}