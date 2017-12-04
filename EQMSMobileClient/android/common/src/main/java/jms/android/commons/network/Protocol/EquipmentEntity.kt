package jms.android.commons.network.Protocol

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/**
 * @author D.Noguchi
 * @since 29,Nov.2017
 */
@RealmClass
data class EquipmentEntity(
        @PrimaryKey var code:String,
        var worker:String,
        var name:String,
        var set_date:String,
        var status:Int)
 :RealmModel{
    constructor() : this("","","","",StockTakingStatus.YET.value)
}