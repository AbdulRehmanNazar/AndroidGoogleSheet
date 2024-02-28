package com.ar.gsheet

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * @Author: Abdul Rehman
 * @Date: 28/02/2024.
 */

class APIHandling {

    suspend fun getFormData(apiCallBack: APICallBack) {
        try {
            val notesApi = getRetrofit().create(NotesApi::class.java)
            val result = notesApi.getNotes()
            apiCallBack.getCallBack(result, true)
        } catch (e: Exception) {
            apiCallBack.getCallBack(null, false)
            e.printStackTrace()
        }
    }

    suspend fun insertDataIntoForm(
        context: Context,
        title: String,
        description: String,
        apiCallBack: APICallBack
    ) {
        try {
            val notesApi = getRetrofit().create(NotesApi::class.java)
            val addNotes = notesApi.addNote(Notes(title, description))
            if (addNotes == null) {
                apiCallBack.getCallBack(null, false)
            } else {
                apiCallBack.getCallBack(addNotes, true)
            }
        } catch (e: Exception) {
            apiCallBack.getCallBack(null, false)
            e.printStackTrace()
        }
    }

    private fun getRetrofit(): Retrofit {
        val retrosheetInterceptor = RetrosheetInterceptor.Builder()
            .setLogging(false)
            // To Read
            .addSheet(
                "notes", // sheet name
                "created_at", "title", "description" // columns in same order
            )
            // To write
            .addForm(
                "formResponse",
                "https://docs.google.com/forms/d/e/1FAIpQLScSv7AOw0Ek67GOjOzbt_I6rSOXHC9BC4RIO0TCdzC0u3uuwA/viewform?usp=sf_link" // form link
            )
            .build()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        // Building OkHttpClient
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor) // and attaching interceptor
            .addInterceptor(loggingInterceptor)
            .build()

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            // with baseUrl as sheet's public URL
            .baseUrl("https://docs.google.com/spreadsheets/d/1ofyRo7znkb5D7uHRze_nGw8_MUaQQ3JJxrhdlL7IDKo/") // Sheet's public URL
            // and attach previously created OkHttpClient
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}