package com.example.listatareas.helper

import android.os.AsyncTask


class doAsync(val handler:()->Unit): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}