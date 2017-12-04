
//
// Copyright 2017 JapanMicroSystem Co.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package jms.android.commons.network.Protocol

import io.reactivex.Observable
import io.reactivex.Observable.create
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.Subject
import io.realm.Realm
import io.realm.exceptions.RealmException
import jms.android.commons.network.LogUtil
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.io.IOException

/**
 * @author D.Noguchi
 * @since 29,Nov.2017
 */
class RxNetwork {
    companion object {
        private val NEXT = "onNext"
        private val ERROR = "onError"
        private val COMPLETE = "onComplete"
    }
    private val client = OkHttpClient()

    fun getRestClient(url:String):Retrofit{
        return Retrofit.Builder().client(getClient()).baseUrl(url).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build()
    }

    private fun getClient():OkHttpClient{
        val interceptor = HttpLoggingInterceptor()

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    fun getRxHttpObservable(url: String):Observable<String>{
        return create{
            try{
                    val result = getApi(url)
                    val resJson = JSONObject(result)
                    val weathers = resJson.getJSONArray("weather")
                    val weather = weathers.getJSONObject(0)
                    val description = weather.getString("description")

                LogUtil.d("$NEXT:$description")

                    it.onNext(description)
//                it.onNext(result)
                it.onComplete()
            }catch (e:IOException){
                LogUtil.e(ERROR, e)
                it.onError(e)
            }catch (e:JSONException){
                LogUtil.e(ERROR, e)
                it.onError(e)
            }finally {
//                LogUtil.d(COMPLETE)
//                it.onComplete()
            }
        }
    }

    private fun getApi(url:String):String{
            val request = Request.Builder()
                    .url(url)
                    .build()
            val response = client.newCall(request).execute()
            return response.body()?.string() ?: throw ClassCastException("return error")
    }



    fun getSubject(url:String):Subject<String>{
        val subject = AsyncSubject.create<String>()
        create<String> {
            try{
                val result = getApi(url)
                val resJson = JSONObject(result)
                val weathers = resJson.getJSONArray("weather")
                val weather = weathers.getJSONObject(0)
                val description = weather.getString("description")

                it.onNext(description)
            }catch (e:IOException){
                LogUtil.e(e)
                it.onError(e)
            }catch (e:JSONException){
                LogUtil.e(e)
                it.onError(e)
            }finally {
//                it.onComplete()
            }

        }
        return subject
    }

    fun getDatabase(url:String):Observable<String>{
        return create {
            var realm:Realm? = null
            try{
                val result = getApi(url)
                val resJson = JSONObject(result)
                val arrayJson = resJson.getJSONArray("equipMobileEntity")
                LogUtil.d("$arrayJson")
                realm = Realm.getDefaultInstance()
                realm?.beginTransaction()
                realm?.createOrUpdateAllFromJson(EquipmentEntity::class.java,arrayJson)
                realm?.commitTransaction()
//                val res = resJson.toString()
                it.onNext(arrayJson.toString())
                it.onComplete()
            }catch (e:IOException){
                LogUtil.e(e)
                it.onError(e)
            }catch (e:JSONException){
                LogUtil.e(e)
                it.onError(e)
            }catch (e:RealmException){
                LogUtil.e(e)
                it.onError(e)
            }finally {
                realm?.close()
            }
        }
    }


}