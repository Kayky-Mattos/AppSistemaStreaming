package com.example.sistemastreaming.Networkings

import com.example.sistemastreaming.AppConstants
import com.example.sistemastreaming.Models.Files
import com.example.sistemastreaming.Models.Playlist
import com.example.sistemastreaming.Models.PostPlaylist
import com.example.sistemastreaming.Models.PostUser
import com.example.sistemastreaming.Models.Token
import com.example.sistemastreaming.Models.User
import com.example.sistemastreaming.Models.contentReqPlaylist
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceInterface {

    @GET(AppConstants.urlgetToken)
    suspend fun getToken(): Token

    @GET(AppConstants.urlgetUser)
    suspend fun getUser(@Path("emailUser") email: String): User

    @GET(AppConstants.urlgetFiles)
    suspend fun getFiles(): List<Files>

    @GET(AppConstants.urlgetPlaylist)
    suspend fun getPlaylist( @Path("userId") userId: String ): List<Playlist>

    @DELETE(AppConstants.urlDeletePlaylist)
    suspend fun deletePlaylist( @Path("PlayListId") playListId: String ): Response<Unit>
    @DELETE(AppConstants.urlDeleteConteudo)
    suspend fun deleteConteudo( @Path("ConteudoId") conteudoId: String ): Response<Unit>

    @POST(AppConstants.urlpostContentPlaylist)
    suspend fun postContentPlaylist( @Path("PlayListId") playListId: String,
                                     @Body contentRequest: contentReqPlaylist
    ): Response<Unit>

    @POST(AppConstants.urlpostPlaylist)
    suspend fun postPlaylist( @Path("UserId") userId: String,
                                     @Body contentRequest: PostPlaylist
    ): Response<Unit>

    @POST(AppConstants.urlpostUser)
    suspend fun postUser( @Body contentRequest: PostUser ): Response<Unit>

}