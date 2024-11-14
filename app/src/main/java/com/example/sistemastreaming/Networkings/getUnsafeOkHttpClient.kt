package com.example.sistemastreaming.Networkings

import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun getUnsafeOkHttpClient(): OkHttpClient {
    try {
        // Trust manager que ignora certificados
        val trustAllCertificates = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
            }
        )

        // Configuração de SSL que ignora certificados
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        // Construção do cliente OkHttp
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .build()

    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}
