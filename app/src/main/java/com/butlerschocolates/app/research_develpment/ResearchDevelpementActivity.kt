package com.butlerschocolates.app.research_develpment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.butlerschocolates.app.R
import com.butlerschocolates.app.api.Status
import com.butlerschocolates.app.model.home.HomeRequestBody
import com.butlerschocolates.app.util.AppConstants
import com.butlerschocolates.app.util.Console
import com.butlerschocolates.app.viewmodels.orderInfo.OrderInfoViewModel


class ResearchDevelpementActivity : AppCompatActivity() {

    var TAG= "Tag RD "
    val orderInfoViewModel: OrderInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_research_develpement)

        Console.Log(TAG,"Before fetching data......")

        orderInfoViewModel.onHomeRequest(creatHomeRequestBody())
        getHomeApi()

        Console.Log(TAG,"After fetching data......")
    }

    fun creatHomeRequestBody(): HomeRequestBody
    {
        var homeRequestBody = HomeRequestBody()
        homeRequestBody.version = AppConstants.API_VERSION
        return homeRequestBody
    }

    fun getHomeApi()
    {
        ViewModelProviders.of(this).get(OrderInfoViewModel::class.java).loginResponse.observe(this, Observer { homeData->
            when (homeData!!.status) {
                Status.LOADING->{
                    Console.Log(TAG,"fetching data......")
                }
                Status.SUCCESS->{
                    Console.Log(TAG,homeData.data!!.data!!.terms_conditions)
                }
                Status.ERROR->{
                    Console.Log(TAG,homeData!!.throwable.toString())
                }
            }
        })
    }
}