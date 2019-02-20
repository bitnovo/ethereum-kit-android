package io.horizontalsystems.ethereumkit.network

import com.google.gson.GsonBuilder
import io.horizontalsystems.ethereumkit.models.etherscan.EtherscanResponse
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class EtherscanService(baseUrl: String, private val apiKey: String) {

    private val service: EtherscanServiceAPI

    init {

        val logger = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(logger)

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

        service = retrofit.create(EtherscanServiceAPI::class.java)
    }

    fun getTransactionList(address: String, startBlock: Int): Flowable<EtherscanResponse> {
        return service.getTransactionList("account", "txList", address, startBlock, 99_999_999, "desc", apiKey)
    }

    fun getTokenTransactions(address: String, startBlock: Int): Flowable<EtherscanResponse> {
        return service.getTokenTransactions("account", "tokentx", address, startBlock, 99_999_999, "desc", apiKey)
    }

    interface EtherscanServiceAPI {

        @GET("/api")
        fun getTransactionList(
                @Query("module") module: String,
                @Query("action") action: String,
                @Query("address") address: String,
                @Query("startblock") startblock: Int,
                @Query("endblock") endblock: Int,
                @Query("sort") sort: String,
                @Query("apiKey") apiKey: String): Flowable<EtherscanResponse>

        @GET("/api")
        fun getTokenTransactions(
                @Query("module") module: String,
                @Query("action") action: String,
                @Query("address") address: String,
                @Query("startblock") startblock: Int,
                @Query("endblock") endblock: Int,
                @Query("sort") sort: String,
                @Query("apiKey") apiKey: String): Flowable<EtherscanResponse>
    }

}
