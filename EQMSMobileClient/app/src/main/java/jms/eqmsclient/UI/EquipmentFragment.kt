
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

package jms.eqmsclient.UI

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.jakewharton.rxbinding.widget.dataChanges
import com.trello.rxlifecycle2.components.support.RxFragment
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmResults
import jms.android.commons.network.LogUtil
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.android.commons.network.Protocol.StockTakingStatus
import jms.eqmsclient.Controller.EquipmentRealmAdapter
import jms.eqmsclient.R

/**
 * 備品管理リスト表示用フラグメント
 *
 * @author D.Noguchi
 * @since 1,Sep.2017
 */
class EquipmentFragment:RxFragment(),InputInterfaceFragment.onSearchEvent {
    override fun onSearchClick(worker: String?, code: String?,omit:Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        progress.show()
        val query = realm.where(EquipmentEntity::class.java)
        if (omit)
            query.notEqualTo("status",StockTakingStatus.DONE.value)

        if (worker != null)
            query.equalTo("worker",worker)

        if (code != null)
            query.equalTo(CODE,code)

        liveData = query.findAllSorted(CODE)

        adapter.updateData(liveData)
        progress.dismiss()
    }


    companion object {
        @JvmStatic fun newInstance():EquipmentFragment = EquipmentFragment()
        @JvmStatic private val CODE = "code"
    }

    /** 備品管理リスト表示用DBインスタンス */
    private lateinit var realm:Realm
    /** 備品管理リストItemAdapter */
    private lateinit var adapter:EquipmentRealmAdapter
    /**
     * 備品管理リスト用アイテムLiveData
     * DBから取得したデータをLiveDataに変換して保持する
     */
    private lateinit var liveData:OrderedRealmCollection<EquipmentEntity>
    /** ProgressDialog */
   private lateinit var progress:ProgressDialog


    /**
     * ViewFiled
     */
    @BindView(R.id.equipment_list) lateinit var mList:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progress = ProgressDialog(this.context)
        /**
         * DBのOpen処理
         * realmインスタンスが生成されていないければインスタンスを生成する
         * */
//        if (!realm.isClosed) {
            realm = Realm.getDefaultInstance()
//        }

        /** DBからデータを取得してLiveDataに変換*/
        liveData = realm.where(EquipmentEntity::class.java).findAll()
        /** 取得したLiveDataをセットしてAdapterを生成する */
        adapter = EquipmentRealmAdapter(this.context!!,liveData)
    }


    /**
     * ViewInitialized
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_list,container,false)

    /**
     * Viewの生成
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)
        mList.adapter = adapter

        mList.onItemClickListener = AdapterView.OnItemClickListener{ _, _, i, _ ->
            LogUtil.d("${liveData[i]}")
//            Toast.makeText(this.context,"${liveData[i]}",Toast.LENGTH_SHORT).show()
            progress.show()
            realm.beginTransaction()
//            if (liveData[i].status == StockTakingStatus.YET.value) {
//                liveData[i].status = StockTakingStatus.WAIT.value
//            }
            when(liveData[i].status){
                StockTakingStatus.YET.value -> liveData[i].status = StockTakingStatus.WAIT.value
                StockTakingStatus.WAIT.value -> liveData[i].status = StockTakingStatus.YET.value
            }
            realm.insertOrUpdate(liveData[i])
            realm.commitTransaction()
            progress.dismiss()
        }



    }



    override fun onDestroy() {
        super.onDestroy()
        /**
         * DBのClose処理
         * realmインスタンスが破棄されていないければ破棄する
         **/
        if (!realm.isClosed) {
            realm.close()
        }
    }




}