package com.butlerschocolates.app.cardvalidation

import android.text.TextUtils
import com.butlerschocolates.app.R
import mostafa.ma.saleh.gmail.com.editcredit.EditCredit

import java.util.regex.Pattern

/**
 * Created   on 25-May-17.
 */

enum class CardType private constructor(val cardTypeName: String, regex: String,
                                        /**
                                         * @return The android resource id for the front card image, highlighting card number format.
                                         */
                                        val frontResource: Int,
                                        /**
                                         * @return minimum length of a card for this {}
                                         */
                                        val minCardLength: Int,
                                        /**
                                         * @return max length of a card for this {}
                                         */
                                        val maxCardLength: Int,
                                        /**
                                         * @return The length of the current card's security code.
                                         */
                                        val securityCodeLength: Int,
                                        /**
                                         * @return The android resource id for the security code name for this card type.
                                         */
                                        val securityCodeName: Int) {

    EMPTY("empty", "^$",
            R.drawable.ic_tick,
            12, 19,
            3, R.string.login);

    /**
     * @return The regex matching this card type.
     */
    val pattern: Pattern = Pattern.compile(regex)

    /**
     * @return the locations where spaces should be inserted when formatting the card in a user
     * * friendly way. Only for display purposes.
     */
    val spaceIndices: IntArray
        get() = if (this == EditCredit.Card.AMEX) AMEX_SPACE_INDICES else DEFAULT_SPACE_INDICES

    /**
     * @param cardNumber The card number to validate.
     * *
     * @return `true` if this card number is locally valid.
     */
    fun validate(cardNumber: String): Boolean {
        if (TextUtils.isEmpty(cardNumber)) {
            return false
        }

        val numberLength = cardNumber.length
        if (numberLength < minCardLength || numberLength > maxCardLength) {
            return false
        } else if (!pattern.matcher(cardNumber).matches()) {
            return false
        }
        return isLuhnValid(cardNumber)
    }

    companion object {

        private val AMEX_SPACE_INDICES = intArrayOf(4, 10)
        private val DEFAULT_SPACE_INDICES = intArrayOf(4, 8, 12)

        /**
         * Returns the card type matching this account, or []
         * for no match.
         *
         *
         * A partial account type may be given, with the caveat that it may not have enough digits to
         * match.
         */
        fun forCardNumber(cardNumber: String): CardType {
            for (cardType in values()) {
                if (cardType.pattern.matcher(cardNumber).matches()) {
                    return cardType
                }
            }
            return EMPTY
        }

        /**
         * Performs the Luhn check on the given card number.

         * @param cardNumber a String consisting of numeric digits (only).
         * *
         * @return `true` if the sequence passes the checksum
         * *
         * @throws IllegalArgumentException if `cardNumber` contained a non-digit (where [ ][Character.isDefined] is `false`).
         * *
         * @see [Luhn Algorithm
        ](http://en.wikipedia.org/wiki/Luhn_algorithm) */
        fun isLuhnValid(cardNumber: String): Boolean {
            val reversed = StringBuffer(cardNumber).reverse().toString()
            val len = reversed.length
            var oddSum = 0
            var evenSum = 0
            for (i in 0..len - 1) {
                val c = reversed[i]
                if (!Character.isDigit(c)) {
                    throw IllegalArgumentException(String.format("Not a digit: '%s'", c))
                }
                val digit = Character.digit(c, 10)
                if (i % 2 == 0) {
                    oddSum += digit
                } else {
                    evenSum += digit / 5 + 2 * digit % 10
                }
            }
            return (oddSum + evenSum) % 10 == 0
        }
    }
}