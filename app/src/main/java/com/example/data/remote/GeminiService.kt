package com.example.data.remote

import com.example.BuildConfig
import com.example.data.model.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiPart(
    val text: String
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiCandidate(
    val content: GeminiContent
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)


interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}

object GeminiRepository {

    suspend fun askAiTutor(
        prompt: String,
        codeContext: String = "",
        language: AppLanguage = AppLanguage.ENGLISH
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineFallbackResponse(prompt, codeContext, language)
        }

        val systemPrompt = if (language == AppLanguage.TELUGU) {
            "మీరు 'AI C Lab' కి అంకితమైన తెలివైన C ప్రోగ్రామింగ్ ట్యూటర్. విద్యార్థుల ప్రశ్నకు సరళమైన మరియు స్పష్టమైన తెలుగు సమాధానం ఇవ్వండి. C భావనలను వివరించండి."
        } else {
            "You are 'AI C Lab' expert C Programming Tutor. Explain concepts clearly with code examples and beginner friendly tone."
        }

        val fullUserText = if (codeContext.isNotBlank()) {
            "Current C Code:\n```c\n$codeContext\n```\n\nStudent Question: $prompt"
        } else {
            prompt
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = fullUserText)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        try {
            val response = RetrofitClient.api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: getOfflineFallbackResponse(prompt, codeContext, language)
        } catch (e: Exception) {
            getOfflineFallbackResponse(prompt, codeContext, language)
        }
    }

    suspend fun explainCodeLineByLine(
        code: String,
        language: AppLanguage = AppLanguage.ENGLISH
    ): String = withContext(Dispatchers.IO) {
        val prompt = if (language == AppLanguage.TELUGU) {
            "ఈ C ప్రోగ్రామ్‌ను లైన్-బై-లైన్ (లైన్ వారీగా) సులభమైన తెలుగులో వివరించండి:"
        } else {
            "Provide a line-by-line detailed explanation for this C code:"
        }
        askAiTutor(prompt, code, language)
    }

    suspend fun analyzeAndFixError(
        code: String,
        errorMessage: String,
        language: AppLanguage = AppLanguage.ENGLISH
    ): String = withContext(Dispatchers.IO) {
        val prompt = if (language == AppLanguage.TELUGU) {
            "ఈ C కోడ్‌లో ఉన్న పొరపాటును గుర్తించి, ఎర్రర్, కారణం, సరిచేసిన కోడ్ మరియు తెలుగు వివరణ ఇవ్వండి. ఎర్రర్ మెసేజ్: $errorMessage"
        } else {
            "Analyze this compilation/runtime error: '$errorMessage'. Provide Error Cause, Correction, Corrected Code, and Explanation."
        }
        askAiTutor(prompt, code, language)
    }

    private fun getOfflineFallbackResponse(
        prompt: String,
        codeContext: String,
        language: AppLanguage
    ): String {
        return if (language == AppLanguage.TELUGU) {
            """
            🤖 **AI C Lab ట్యూటర్ (తెలుగు సమాధానం)**
            
            **విశ్లేషణ:**
            1. C భాషలో ప్రతీ statement సాధారణంగా `;` (semicolon) తో ముగుస్తుంది.
            2. `printf()` మరియు `scanf()` సక్రమంగా ఉపయోగించబడ్డాయో చూసుకోండి.
            3. `int main()` ముగింపులో `return 0;` ఉపయోగించండి.
            
            **సరిచేసిన కోడ్ చిట్కా:**
            ```c
            #include <stdio.h>
            int main() {
                printf("Hello AI C Lab!\n");
                return 0;
            }
            ```
            *సూచన: తదుపరి ఉదాహరణల కోసం Virtual Lab ప్రయోగాలు ప్రయత్నించండి.*
            """.trimIndent()
        } else {
            """
            🤖 **AI C Lab Tutor Response**
            
            **Explanation & Guidance:**
            1. In C programming, every statement ends with a semicolon `;`.
            2. Ensure format specifiers like `%d` match integer variables, and `%f` match float variables.
            3. Always verify function brackets `{ }` and header files like `#include <stdio.h>`.
            
            **Corrected Snippet:**
            ```c
            #include <stdio.h>
            int main() {
                printf("Welcome to AI C Lab!\n");
                return 0;
            }
            ```
            *Tip: Use the Virtual C Lab modules to practice test cases!*
            """.trimIndent()
        }
    }
}
