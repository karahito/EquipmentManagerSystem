
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
import io.reactivex.Observable.*
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
import java.io.IOException

/**
 * @author D.Noguchi
 * @since 1,Sep.2017
 */
class RxCommit {
    companion object {
        @JvmStatic fun newInstance() = RxCommit()
    }
    private val client = OkHttpClient()

    fun getClient():OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private fun postApi(url:String,data:String):String{
        val mimeType = MediaType.parse(data)
        val requestBody = RequestBody.create(mimeType,"")
        val request = Request.Builder().url(url).post(requestBody).build()
        val response = client.newCall(request).execute()
        return response.body()?.string() ?: throw ClassCastException("return error")
    }

    fun postDatabase(url:String,data:String): Observable<String>{
        return create({
            var realm: Realm?=null
            try{
                val result = postApi(url,data)
                val resJson = JSONObject(result)
                val arrayJson = resJson.getJSONArray("equipMobileEntity")
                LogUtil.d("$arrayJson")
                realm = Realm.getDefaultInstance()
                realm?.run {
                    beginTransaction()
                    realm?.createOrUpdateAllFromJson(EquipmentEntity::class.java,arrayJson)
                    commitTransaction()
                }
                it.onNext(arrayJson.toString())
                it.onComplete()
            }catch (e:IOException) {
                it.onError(e)
            }catch (e:JSONException){
                it.onError(e)
            }catch (e:RealmException){
                it.onError(e)
            }finally {
                realm?.close()
            }
        })
    }
}