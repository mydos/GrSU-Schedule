package by.kirich1409.grsuschedule.network

import android.app.Notification
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService
import com.squareup.okhttp.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient
import retrofit.converter.Converter
import retrofit.converter.JacksonConverter
import java.io.File

/**
 * Created by kirillrozov on 9/13/15.
 */
public class ScheduleSpiceService : RetrofitJackson2SpiceService() {

    override fun onCreate() {
        super.onCreate()
        addRetrofitInterface(ScheduleService::class.java)
    }

    override fun createConverter(): Converter {
        val objectMapper = ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return JacksonConverter(objectMapper)
    }

    override fun getCacheFolder(): File {
        return cacheDir
    }

    override fun <T : Any> getRetrofitService(serviceClass: Class<T>): T {
        val client: OkHttpClient = OkHttpClient()
        client.setCache(null)

        val restAdapter = RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(createConverter())
                .setClient(OkClient())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build()
        return restAdapter.create(serviceClass);
    }

    override fun createDefaultNotification(): Notification? = null

    override fun getServerUrl(): String = "http://api.grsu.by/1.x/app1"
}
