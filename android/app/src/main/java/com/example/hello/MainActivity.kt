package com.example.hello

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pythonStart(MainPresenter(this))
    }

    private fun pythonStart(mainPresenter: MainPresenter){
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        val py = Python.getInstance()
        setupPythonWebView(py, mainPresenter)
        startMain(py)
    }

    private fun startMain(py: Python) {
        val runpy = py.getModule("runpy")
        runpy.callAttr("run_module", "main", null,  "__main__")
    }

    private fun setupPythonWebView(
        py: Python,
        mainPresenter: MainPresenter
    ) {
        try {
            val webViewAndroidPlatform = py.getModule("webview.platforms.android")
            webViewAndroidPlatform["mainPresenter"] = PyObject.fromJava(mainPresenter)
        } catch (e: Exception) {
            Toast.makeText(
                this, "Python webview library not found.", Toast.LENGTH_LONG
            ).show()
        }
    }

}