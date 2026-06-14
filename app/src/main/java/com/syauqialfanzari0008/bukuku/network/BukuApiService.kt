package com.syauqialfanzari0008.bukuku.network

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.syauqialfanzari0008.bukuku.model.Buku
import com.syauqialfanzari0008.bukuku.model.OpStatus
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val BASE_URL = "http://bukuku-syauqi.infinityfreeapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun hexToBytes(hex: String): ByteArray {
    val data = ByteArray(hex.length / 2)
    for (i in hex.indices step 2) {
        data[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
    }
    return data
}

private fun bytesToHex(bytes: ByteArray): String {
    return bytes.joinToString("") { "%02x".format(it) }
}

private fun solveAntiBot(html: String): String? {
    val matches = Regex("""toNumbers\("([0-9a-fA-F]+)"\)""").findAll(html)
        .map { it.groupValues[1] }.toList()
    if (matches.size < 3) return null
    return try {
        val a = hexToBytes(matches[0])
        val b = hexToBytes(matches[1])
        val c = hexToBytes(matches[2])
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(a, "AES"), IvParameterSpec(b))
        bytesToHex(cipher.doFinal(c))
    } catch (e: Exception) {
        Log.e("ANTI_BOT", "Gagal solve: ${e.message}")
        null
    }
}

private val antiBotInterceptor = Interceptor { chain ->
    val request = chain.request()
    var response = chain.proceed(request)
    val body = response.peekBody(4096).string()

    if (body.contains("toNumbers")) {
        val cookieValue = solveAntiBot(body)
        if (cookieValue != null) {
            response.close()
            val newRequest = request.newBuilder()
                .addHeader("Cookie", "__test=$cookieValue")
                .build()
            response = chain.proceed(newRequest)
        }
    }
    response
}

private val client = OkHttpClient.Builder()
    .addInterceptor(antiBotInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BukuApiService {
    @GET("buku.php")
    suspend fun getBuku(
        @Header("Authorization") userId: String
    ): List<Buku>

    @Multipart
    @POST("buku.php")
    suspend fun postBuku(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("penulis") penulis: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("buku.php")
    suspend fun deleteBuku(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus
}

object BukuApi {
    val service: BukuApiService by lazy {
        retrofit.create(BukuApiService::class.java)
    }

    fun getBukuUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }