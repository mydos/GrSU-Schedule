package by.kirich1409.grsuschedule.network

import android.app.Notification
import by.kirich1409.grsuschedule.BuildConfig
import by.kirich1409.grsuschedule.ScheduleApp
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService
import retrofit.ErrorHandler
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.converter.Converter
import retrofit.converter.JacksonConverter

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
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        if (!BuildConfig.DEBUG) {
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        }
        return JacksonConverter(objectMapper)
    }



    override fun getCacheFolder() = cacheDir

    override fun <T : Any> getRetrofitService(serviceClass: Class<T>): T {
        val restAdapter = RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(createConverter())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build()
        return restAdapter.create(serviceClass);
    }

    override fun createDefaultNotification(): Notification? = null

    override fun getServerUrl() = "http://api.grsu.by/1.x/app1"
}
