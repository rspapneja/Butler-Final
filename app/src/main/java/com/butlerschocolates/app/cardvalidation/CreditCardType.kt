package com.butlerschocolates.app.cardvalidation

import com.butlerschocolates.app.util.Console


/**
 * Created   on 25-May-17.
 */

class CreditCardType {
    internal var cardNumber: String = ""
    internal var TAG = "CreditCardType"

    fun validateCreditCardType(cardNumber: String, creditCardValidator: CreditCardValidator) {
        this.cardNumber = cardNumber
        Console.Log(TAG, CardType.forCardNumber(cardNumber).cardTypeName.toString())
        creditCardValidator.isValid(CardType.isLuhnValid(cardNumber))

    }


    interface CreditCardValidator {
        fun isValid(valid: Boolean)
    }
}
