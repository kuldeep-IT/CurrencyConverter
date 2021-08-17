package com.peerbitskuldeep.currencyconvtrial.retrofit

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.peerbitskuldeep.currencyconvtrial.constants.Constants.Companion.BASE_URL
import com.peerbitskuldeep.currencyconvtrial.constants.Constants.Companion.END_POINT
import com.peerbitskuldeep.currencyconvtrial.listeners.DefaultActionPerformer
import com.peerbitskuldeep.currencyconvtrial.listeners.RetrofitResponseListener
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class NetworkCall constructor(context: Context){

    private var requestParams: HashMap<Any, Any>? = HashMap()
    private var endPoint = ""
    private var retrofitResponseListner: RetrofitResponseListener? = null

    private var headers = HashMap<Any, Any>()

    private var requestObject: Any? = null
    private var call: Call<ResponseBody>? = null

    companion object {

        private var actionPerformer: DefaultActionPerformer? = null

        private val interceptor = HttpLoggingInterceptor()

        private val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        val instance: ApiInterface
            get() {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build()

                return retrofit.create(ApiInterface::class.java)
            }

        fun setActionPerformer(actionPerformer: DefaultActionPerformer) {
            Companion.actionPerformer = actionPerformer
        }

        fun with(context: Context): NetworkCall {
            return NetworkCall(context)
        }
    }

    fun setRequestParams(params: HashMap<Any, Any>): NetworkCall
    {
        this.requestParams = params
        return this
    }

    fun setEndPoint(endPoint: String): NetworkCall
    {
        this.endPoint = endPoint
        return this
    }

    fun setResponseListener(retrofitResponseListener: RetrofitResponseListener): NetworkCall
    {
        this.retrofitResponseListner = retrofitResponseListener
        return this
    }

    //handleResponse -> 453 -> networkCall

    fun makeCall(): NetworkCall
    {
        if (headers.size > 0)
        {
            for((key, value) in headers)
            {
                Log.d("TAG", "makeCall: $key => $value")
            }
        }

        if (retrofitResponseListner != null) {
            retrofitResponseListner!!.onPreExecute()
        }

       /* if(requestObject != null)
        {*/
            makeRequestWithObject()
//        }

        return this
    }

    private fun makeRequestWithObject()
    {
//        Log.d("TAG", "makeRequestWithObject: ${reuestClass.toString()}")

        call = instance.getData(END_POINT/*, headers*/)

        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                handleResponse(response)

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                Log.d("TAG", "onFailure: ${t.message}")

            }
        })
    }

    private fun handleResponse(response: Response<ResponseBody?>)
    {
        try {

            if (response.body()!=null)
            {
                val body = response.body()!!.string() //not a toString

                var jsonObject: JSONObject = JSONObject(body)

                if(retrofitResponseListner != null)
                {
                    retrofitResponseListner!!.onSuccess(

                        response.code(),
                        jsonObject.optJSONObject("rates"),
                        body

                    )
                }
                else
                {
                    Log.d("TAG", "handleResponse: Error!...")
                }

            }

        }catch (e:Exception)
        {
            Log.d("TAG", "handleResponse: ${e.message}")
        }


    }


}


