package com.btcpiyush.ads.provider.api

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object AdsRetrofit {

    private interface AdsApi {
        @GET("{url}")
        fun getAdsSettings(@Path(value = "url") jsonPage: String) : Observable<ResponseBody?>
    }

    fun getInstance(baseUrl: String): Retrofit {
        var mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        var mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

    fun  <T> getAdsSettings(baseUrl: String, jsonPage: String, classOfT: Class<T> ,callback: AdsResponse<T>){
        var retrofit = getInstance(baseUrl)
        var apiInterface = retrofit.create(AdsApi::class.java)
            apiInterface.getAdsSettings(jsonPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                try {
                    val data : T = Gson().fromJson(it?.string(), classOfT)
                    callback.onResponse(data)
                } catch (exp: Exception) {
                    callback.onFailure(exp)
                }
            }

    }

}

