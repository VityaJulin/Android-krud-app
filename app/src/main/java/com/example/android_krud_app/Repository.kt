package com.example.android_krud_app

import com.example.android_krud_app.api.API
import com.example.android_krud_app.api.AuthRequestParams
import com.example.android_krud_app.api.CreatePostRequest
import com.example.android_krud_app.api.RegistrationRequestParams
import com.example.android_krud_app.api.interceptor.InjectAuthTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("https://android-krud-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    // Добавление interceptor-ов в retrofit клиент. Во все последующие запросы будет добавляться токен
    // и они будут логироваться
    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://android-krud-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        API = retrofit.create(com.example.android_krud_app.api.API::class.java)
    }

    // Ленивое создание API
    private var API: API =
        retrofit.create(com.example.android_krud_app.api.API::class.java)


    suspend fun authenticate(login: String, password: String) = API.authenticate(
      AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        API.register(
          RegistrationRequestParams(
            login,
            password
          )
        )

    suspend fun createPost(content: String) = API.createPost(CreatePostRequest(content = content))

    suspend fun getPosts() = API.getPosts()

    suspend fun likedByMe(id: Long) = API.likedByMe(id)

    suspend fun cancelMyLike(id: Long) = API.cancelMyLike(id)

    suspend fun repostedByMe(id: Long) = API.repostedByMe(id)

    suspend fun getPostsAfter(id: Long) = API.getPostsAfter(id)
}
