package com.eugene.commonsdk.module.network

import com.eugene.commonsdk.module.network.SSLSocketClient.trustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLSocketClient {

    //获取这个SSLSocketFactory
    val sslSocketFactory: SSLSocketFactory
        get() {
            try {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustManagers, SecureRandom())
                return sslContext.socketFactory
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }

    //获取TrustManager
    private val trustManagers: Array<TrustManager>
        get() = arrayOf(trustManager)

    //获取HostnameVerifier
    val hostnameVerifier: HostnameVerifier
        get() = HostnameVerifier { s, sslSession -> true }

    val trustManager: X509TrustManager
        get() = MyTrustManager()

    private class MyTrustManager : X509TrustManager {

        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }
}
