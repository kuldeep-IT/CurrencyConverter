package com.peerbitskuldeep.currencyconvtrial.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun getData(@Url endpoint: String/*, @HeaderMap hashMap: HashMap<Any, Any>*/): Call<ResponseBody>

}