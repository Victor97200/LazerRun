package com.example.lazerrun

import retrofit2.http.GET

interface LaserRunService {
    @GET("univ/laserrun.json")
    suspend fun getCategories(): List<Category>


}