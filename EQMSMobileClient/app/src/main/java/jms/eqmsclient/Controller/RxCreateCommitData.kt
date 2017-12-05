
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


package jms.eqmsclient.Controller

import com.google.gson.Gson
import io.reactivex.Observable
import io.realm.Realm
import io.realm.exceptions.RealmException
import jms.android.commons.network.LogUtil
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.android.commons.network.Protocol.StockTakingStatus
import org.json.JSONException
import java.io.IOException
import java.util.*

/**
 * @author D.Noguchi
 * @since 4.Sep.2017
 */
class RxCreateCommitData {
    companion object {
        fun newInstance() =RxCreateCommitData()
        private val STATUS = "status"
    }

    private fun openRealm():Realm{
        return Realm.getDefaultInstance()
    }

    private fun closeRealm(realm:Realm):Boolean {
        if (!realm.isClosed) {
            realm.close()
        }
        return realm.isClosed
    }

    fun rxGetCommitData():Observable<String>{
        return Observable.create{
            emitter->
            val realm = openRealm()
            try{
                val result = realm.where(EquipmentEntity::class.java).equalTo(STATUS,StockTakingStatus.WAIT.value).findAll()
                result.forEach {
                    emitter.onNext(it.code)
                }
            }catch(e:RealmException){
                LogUtil.e(e)
                emitter.onError(e)
            }finally {
                closeRealm(realm)
                emitter.onComplete()
            }
        }
    }

    fun CreateJson():String?{
        val realm = openRealm()
        try {
            val entity = realm.where(EquipmentEntity::class.java).equalTo(STATUS, StockTakingStatus.WAIT.value).findAll()
            val result = ArrayList<String>()
            entity.forEach {
                result.add(it.code)
            }

            val commitList = ArrayList<CommitEntity>()
            entity.forEach {
                commitList.add(CommitEntity(it.code))
            }

            val gson = Gson()
            val commitJson = gson.toJson(commitList)
            println(commitJson)
//            val stringer = JSONStringer()
//            stringer.`object`()
//
//            result.forEach {
//                stringer.value(it)
//            }
//            stringer.endObject()
//            return stringer.toString()
            return commitJson.toString()

        }catch (e:IOException){
            LogUtil.e(e)
            return null
        }catch (e:RealmException) {
            LogUtil.e(e)
            return null
        }catch(e:JSONException) {
            LogUtil.e(e)
            return null
        }finally {
            closeRealm(realm = realm)
        }
    }
}

data class CommitEntity(
    var code:String
)