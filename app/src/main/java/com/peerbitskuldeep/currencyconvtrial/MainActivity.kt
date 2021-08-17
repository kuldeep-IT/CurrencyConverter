package com.peerbitskuldeep.currencyconvtrial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.peerbitskuldeep.currencyconvtrial.apiresponse.Rates
import com.peerbitskuldeep.currencyconvtrial.constants.Constants.Companion.END_POINT
import com.peerbitskuldeep.currencyconvtrial.listeners.RetrofitResponseListener
import com.peerbitskuldeep.currencyconvtrial.retrofit.NetworkCall
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Math.round
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var fromSp:String? = null
    private var toSp:String? = null
    private var edtAmount:Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConvert.setOnClickListener {
            fromSp = spFromCurrency.selectedItem.toString()
            toSp = spToCurrency.selectedItem.toString()


            if(edtFrom.text.isNullOrEmpty())
            {
                Toast.makeText(this,"Please Enter Amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                edtAmount =edtFrom.text.toString().toDouble()
                apiCallResponseData()
            }
        }

    }

    fun createEndPoint(endPoint:String, fromCurrency:String, toCurrency:String): String
    {
        return endPoint + "&base=" + fromCurrency + "&symbols="  + toCurrency
    }

    fun apiCallResponseData()
    {
//        val params = HashMap<Any, Any>()

        NetworkCall.with(this)
            .setEndPoint(createEndPoint(END_POINT,fromSp!!,toSp!!))
            .setResponseListener(object : RetrofitResponseListener {
                override fun onPreExecute() {
                }

                override fun onSuccess(statusCode: Int, jsonObject: JSONObject, response: String) {
                    val rateTo = jsonObject.optString(toSp).toDouble()

                    var finalRate = round(edtAmount!! * rateTo * 100) / 100
                    tvResult.setText("$edtAmount $fromSp = ${finalRate.toDouble()} $toSp")

//                    Toast.makeText(this@MainActivity,"Success : " + (edtAmount?.times(rateTo)), Toast.LENGTH_SHORT).show()
                }

                override fun onError(statusCode: Int, messages: ArrayList<String>) {
                    Toast.makeText(this@MainActivity,"Failure", Toast.LENGTH_SHORT).show()

                }
            }).makeCall()
    }

    /*fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.CAD
        "HKD" -> rates.HKD
        "ISK" -> rates.ISK
        "EUR" -> rates.EUR
        "PHP" -> rates.PHP
        "DKK" -> rates.DKK
        "HUF" -> rates.HUF
        "CZK" -> rates.CZK
        "AUD" -> rates.AUD
        "RON" -> rates.RON
        "SEK" -> rates.SEK
        "IDR" -> rates.IDR
        "INR" -> rates.INR
        "BRL" -> rates.BRL
        "RUB" -> rates.RUB
        "HRK" -> rates.HRK
        "JPY" -> rates.JPY
        "THB" -> rates.THB
        "CHF" -> rates.CHF
        "SGD" -> rates.SGD
        "PLN" -> rates.PLN
        "BGN" -> rates.BGN
        "CNY" -> rates.CNY
        "NOK" -> rates.NOK
        "NZD" -> rates.NZD
        "ZAR" -> rates.ZAR
        "USD" -> rates.USD
        "MXN" -> rates.MXN
        "ILS" -> rates.ILS
        "GBP" -> rates.GBP
        "KRW" -> rates.KRW
        "MYR" -> rates.MYR
        else -> null
    }*/

}


