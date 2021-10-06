package com.example.demoquizwithandroid

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Database
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class QuizViewModel : ViewModel() {
    private val collectionId = "606d5bd9626ae"
    lateinit var client : Client
    fun create(context: Context) {
        client = Client(context)
            .setEndpoint("https://demo.appwrite.io/v1")
            .setProject("606d5bc9de604")
        getQuestions()
    }
    private val db by lazy {
        Database(client)
    }

    private val _questions = MutableLiveData<JSONArray>().apply { value = null }


    val questions: LiveData<JSONArray> = _questions;
    val selectedQuestion = MutableLiveData<Int>().apply { value = 0}
    val correct = MutableLiveData<Int>().apply { value = 0 }

    private fun getQuestions() {
        viewModelScope.launch {
            try {
                var response = db.listDocuments(collectionId)
                val json = response.body?.string() ?: ""
                var que = JSONObject(json)
                _questions.postValue( que["documents"] as JSONArray)
            } catch(e : AppwriteException) {
                Log.e("Get questions",e.message.toString())
            }
        }
    }
}