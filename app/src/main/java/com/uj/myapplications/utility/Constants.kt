package com.uj.myapplications.utility

/**
 * Created by Umesh on 1/24/2018.
 */
class Constants {
    companion object {
        const val MIN_PASSWORD_LENGTH: Int = 6
        const val MIN_COUNTRY_CODE_LENGTH: Int = 2
        const val MESSAGE = "message"
        const val ERR_CANT_BE_EMPTY = "Cannot be empty."
        const val OOPS_STR = "Oops! Something went wrong, Try Again!"
        const val STATUS = "status"
        const val DATA = "data"
        const val PRICELIST = "pricelist"


        private const val ARABIC_LANG = "عربى"  // not in web service
        private const val BENGALI_LANG = "বাঙালি"
        private const val CHINESE_LANG = "中文" // not in web service
        private const val ENGLISH_LANG = "English"
        private const val GUJRATI_LANG = "ગુજરાતી"
        private const val PUNJABI_LANG = "ਪੰਜਾਬੀ"
        private const val TAMIL_LANG = "தமிழ்"
        private const val TELUGU_LANG = "తెలుగు"
        private const val URDU_LANG = "اردو"
        private const val HINDI_LANG = "हिन्दी"
        private const val BODO_LANG = "बोरो"
        private const val DOGRI_LANG = "डोगरी"
        private const val KONKANI_LANG = "कोंकणी"
        private const val MAITHILI_LANG = "मैथिली"
        private const val MARATHI_LANG = "मराठी"
        private const val NEPALI_LANG = "नेपाली"
        private const val SINDHI_LANG = "सिन्धी"

        private val arabicLangRegex = Regex("[0-9\\u0600-\\u06FF\\+\\.\\_\\%\\-\\+]{1,256}")
        private val banglaLangRegex = Regex("[0-9\\u0980-\\u09FF\\+\\.\\_\\%\\-\\+]{1,256}")
        private val chineseLangRegex = Regex("[0-9\\u4E00-\\u9FFF\\+\\.\\_\\%\\-\\+]{1,256}")
        private val englishLangRegex = Regex("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}")
        private val gujratiLangRegex = Regex("[0-9\\u0A81-\\u0AD0\\+\\.\\_\\%\\-\\+]{1,256}")
        private val punjabiLangRegex = Regex("[0-9\\u0A00-\\u0A7F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val tamilLangRegex = Regex("[0-9\\u0B80-\\u0BFF\\+\\.\\_\\%\\-\\+]{1,256}")
        private val teluguLangRegex = Regex("[0-9\\u0C00-\\u0C7F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val urduLangRegex =
            Regex("[0-9\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70-\\uFEFF\\+\\.\\_\\%\\-\\+]{1,256}")
        private val hindiLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val bodoLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val dogriLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val konkaniLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val maithiliLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val marathiLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val nepaliLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        private val sindhiLangRegex = Regex("[0-9\\u0900-\\u097F\\+\\.\\_\\%\\-\\+]{1,256}")
        //public val whoIsRegex = Regex("^[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,7}\$")

        val langRegexMap: HashMap<String, Regex> = HashMap()
        val SKIP: String = "Skip"
        init {
            langRegexMap[ARABIC_LANG] = arabicLangRegex
            langRegexMap[BENGALI_LANG] = banglaLangRegex
            langRegexMap[CHINESE_LANG] = chineseLangRegex
            langRegexMap[ENGLISH_LANG] = englishLangRegex
            langRegexMap[GUJRATI_LANG] = gujratiLangRegex
            langRegexMap[PUNJABI_LANG] = punjabiLangRegex
            langRegexMap[TAMIL_LANG] = tamilLangRegex
            langRegexMap[TELUGU_LANG] = teluguLangRegex
            langRegexMap[URDU_LANG] = urduLangRegex
            langRegexMap[HINDI_LANG] = hindiLangRegex

            langRegexMap[BODO_LANG] = bodoLangRegex
            langRegexMap[DOGRI_LANG] = dogriLangRegex
            langRegexMap[KONKANI_LANG] = konkaniLangRegex
            langRegexMap[MAITHILI_LANG] = maithiliLangRegex
            langRegexMap[MARATHI_LANG] = marathiLangRegex
            langRegexMap[NEPALI_LANG] = nepaliLangRegex
            langRegexMap[SINDHI_LANG] = sindhiLangRegex
        }
    }
}

enum class PaymentType {
    DOMAIN_RENEW,
    DOMAIN_PURCHASE
}

