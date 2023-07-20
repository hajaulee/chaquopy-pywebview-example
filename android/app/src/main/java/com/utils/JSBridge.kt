package com.utils

import android.util.Log
import android.webkit.JavascriptInterface
import com.chaquo.python.PyObject

class JSBridge(private var pythonBridge: PyObject) {

    @JavascriptInterface
    fun call(funcName: String, param: String, valueId: String) {
        Log.i("JSBridge", "$funcName/$param/$valueId")
        pythonBridge.callAttr("call", funcName, param, valueId)
    }
}