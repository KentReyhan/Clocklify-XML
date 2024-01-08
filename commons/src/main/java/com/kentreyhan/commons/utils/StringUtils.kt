package com.kentreyhan.commons.utils

class StringUtils {
    fun checkIfStringValueExist(string: String?, emptyReplacement: String): String {
        return if(string.isNullOrEmpty()||string.isBlank()){
            emptyReplacement
        } else if(string.isNotEmpty()||string.isNotBlank()){
            string
        } else{
            emptyReplacement
        }
    }

}