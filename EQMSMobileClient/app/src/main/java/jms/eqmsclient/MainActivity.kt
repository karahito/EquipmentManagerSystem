package jms.eqmsclient

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.OrderedRealmCollection
import io.realm.Realm
import jms.android.commons.network.LogUtil
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.android.commons.network.Protocol.RxNetwork
import jms.eqmsclient.Controller.EquipmentRealmAdapter
import jms.eqmsclient.UI.EquipmentFragment
import jms.eqmsclient.UI.InputInterfaceFragment


class MainActivity : RxAppCompatActivity() {

    companion object {
        private val url =  "http://192.168.1.208:8080/findAll"
        private val subscription = RxNetwork().getDatabase(url)
    }
    @BindView(R.id.equipment_list) lateinit var mList: ListView

    private var realm:Realm? = null


    private lateinit var adapter:EquipmentRealmAdapter

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        val progress = ProgressDialog(this)

        progress.show()
            subscription.subscribeOn(Schedulers.computation())
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry()
                    .subscribe(
                            {
                            },
                            {},
                            {
                                LogUtil.d("complete")
                                progress.dismiss()
                                /** add Fragment */
                                supportFragmentManager.beginTransaction()
                                        /**
                                         * top_container
                                         */
                                        .add(R.id.top_container, InputInterfaceFragment.newInstance())
                                        /**
                                         * center_container
                                         */
                                        .add(R.id.center_container,EquipmentFragment.newInstance())
                                        .commit()
                            })


        realm = Realm.getDefaultInstance()
        val liveResult = realm?.where(EquipmentEntity::class.java)?.findAllSorted("code")
        adapter = EquipmentRealmAdapter(this,liveResult as OrderedRealmCollection<EquipmentEntity>)
        mList.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
        realm = null
        LogUtil.d("$realm")
    }

}
