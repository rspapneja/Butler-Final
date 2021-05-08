package com.butlerschocolates.app.model.home

import com.butlerschocolates.app.model.common.Customer

data class Data(
    val error: String,
    val platinum_card_terms: String,
    val register_email_confirmation: String,
    val about_us: String,
    val app_address: String,
    val app_email: String,
    val app_mobile: String,
    val app_title: String,
    val privacy_policy: String,
    val terms_conditions: String,
    val splash_image: String,
    val splash_title: String,
    val splash_desc: String,
    val storelist_banner: String,
    val profile_banner: String,
    val topup_banner: String,
    val register_loyalty_message: String,
    val ask_loyalty_signup: String,
    val topup_seek_value: String,
    val customer:Customer
)