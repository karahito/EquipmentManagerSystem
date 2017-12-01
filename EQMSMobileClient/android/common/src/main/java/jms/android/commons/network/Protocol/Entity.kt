package jms.android.commons.network.Protocol

/**
 * Created by jmsbusinesssoftmac on 2017/11/29.
 */
class Entity {
    lateinit var bass: String
    lateinit var wether: List<Weather>

    data class Weather(
            var id: Int,
            var main: String,
            var description: String,
            var icon: String
    )
}