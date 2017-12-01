package jms.eqmsclient

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import jms.android.commons.network.Protocol.RxNetwork

/**
 * ApplicationContextを参照するためのカスタムApplicationクラス
 *
 * @author D.Noguchi
 * @since 14,Nov.2017
 */
class MyApplication:Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic lateinit var instance:Context
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
    }

}