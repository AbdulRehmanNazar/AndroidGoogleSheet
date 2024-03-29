package com.ar.gsheet

import com.github.theapache64.retrosheet.annotations.Read
import com.github.theapache64.retrosheet.annotations.Write
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApi {
    @Read("SELECT *")
    @GET("notes")
    suspend fun getNotes(): List<Notes>

    @Write
    @POST("formResponse")
    suspend fun addNote(@Body addNoteRequest: Notes): Notes
}