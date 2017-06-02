package com.hyeonmin.kface.tenonine.asynctasks

import android.os.AsyncTask
import com.hyeonmin.kface.tenonine.R
import com.hyeonmin.kface.tenonine.activities.IntroActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Hyeonmin on 2017-05-31.
 */
open class TenONineLoginAsyncTask(activity: IntroActivity) : AsyncTask<String, R.integer, String>() {

    var activity = activity

    override fun doInBackground(vararg url: String?): String {
        val jsonHtml = StringBuilder()
        var return_str = ""

        System.out.println(url[0])

        while (return_str.equals("", ignoreCase = true)) {
            try {

                val data_url = URL(url[0])
                val conn = data_url.openConnection() as HttpURLConnection
                if (conn != null) {
                    conn.connectTimeout = 10000
                    conn.useCaches = false
                    if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                        val br = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                        while (true) {
                            val line = br.readLine() ?: break
                            jsonHtml.append(line + "\n")
                        }
                        br.close()
                    }
                    conn.disconnect()
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return_str = jsonHtml.toString()
        }
        return jsonHtml.toString()
    }

    override fun onPostExecute(str: String) {
        activity.getTenONineAccesstoken(str)
    }
}