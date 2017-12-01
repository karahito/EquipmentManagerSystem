package jms.android.commons.network.Protocol

import io.reactivex.Observable
import jms.android.commons.network.Protocol.Entity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author D.Noguchi
 * @since 29,Nov.2017
 */
interface WeatherApi{
    @GET("/data/2.5{name}")
    fun get(@Path("name")name:String,@Query("q")q:String):Observable<Entity>
}