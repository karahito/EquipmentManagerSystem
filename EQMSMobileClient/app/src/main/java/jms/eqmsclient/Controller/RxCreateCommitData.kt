package jms.eqmsclient.Controller

import com.google.gson.Gson
import io.realm.Realm
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.android.commons.network.Protocol.StockTakingStatus

/**
 * Created by jmsbusinesssoftmac on 2017/12/04.
 */
class RxCreateCommitData {
    companion object {
        fun newInstance() =RxCreateCommitData()
        private val STATUS = "status"
    }

    fun openRealm():Realm{
        return Realm.getDefaultInstance()
    }

    fun closeRealm(realm:Realm):Boolean {
        if (!realm.isClosed) {
            realm.close()
        }
        return realm.isClosed
    }

    fun CreateJson():String{
        val realm = openRealm()
        val result = realm.where(EquipmentEntity::class.java).equalTo(STATUS,StockTakingStatus.WAIT.value).findAll()
        return result.toString()
    }
}