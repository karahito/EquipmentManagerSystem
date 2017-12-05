
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

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.android.commons.network.Protocol.StockTakingStatus
import jms.eqmsclient.R


/**
 * @author D.Noguchi
 */

internal class EquipmentRealmAdapter(context: Context,realmResults: OrderedRealmCollection<EquipmentEntity>):RealmBaseAdapter<EquipmentEntity>(realmResults),ListAdapter {

    /** resのLayoutResourceを参照するためにLayoutInflaterを生成*/
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * ButterKnifeでViewFiledを定義
     * ＊この時点ではBindされていないので参照値はnull
     */
    @BindView (R.id.equipment_list_raw_code) lateinit var mCode:TextView
    @BindView (R.id.equipment_list_raw_number) lateinit var mNumber:TextView
    @BindView (R.id.equipment_list_raw_date) lateinit var mDate:TextView
    @BindView (R.id.equipment_list_raw_checkBox) lateinit var mCheck:AppCompatCheckBox
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /**
         * Viewを取得する
         * convertViewがNullの場合は参照するViewを定義する
         */
        val view = convertView ?: inflater.inflate(R.layout.equipment_list_raw,null)

        /** ButterKnifeで定義したViewFieldをbindする*/
        ButterKnife.bind(this,view!!)

        mCheck.isClickable=false
        if (adapterData != null) {
            adapterData?.get(position)?.run {
                mCode.text = code
                mNumber.text = worker
                val year = set_date.substring(0..3)
                val month = set_date.substring(4..5)
                val day = set_date.substring(6..7)
                val date = "$year/$month/$day"
                mDate.text=date
                when(status){
                    StockTakingStatus.YET.value ->{
                        mCheck.visibility = View.VISIBLE
                        mCheck.isChecked = false
                    }
                    StockTakingStatus.DONE.value ->{
//                        mCheck.isChecked = true
                        mCheck.visibility = View.INVISIBLE
                    }
                    StockTakingStatus.WAIT.value->{
                        mCheck.visibility = View.VISIBLE
                        mCheck.isChecked = true
                    }
                }
            }
        }


//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return view
    }
}

//internal class MyListAdapter(realmResults: OrderedRealmCollection<EquipmentEntity) : RealmBaseAdapter<EquipmentEntity>(realmResults), ListAdapter {
//
//    private var inDeletionMode = false
//    private val countersToDelete = HashSet<Int>()
//
//    private class ViewHolder {
//        internal var countText: TextView? = null
//        internal var deleteCheckBox: CheckBox? = null
//    }
//
//    fun enableDeletionMode(enabled: Boolean) {
//        inDeletionMode = enabled
//        if (!enabled) {
//            countersToDelete.clear()
//        }
//        notifyDataSetChanged()
//    }
//
//    fun getCountersToDelete(): Set<Int> {
//        return countersToDelete
//    }
//
//    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var convertView = convertView
//        val viewHolder: ViewHolder
//        if (convertView == null) {
//            convertView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.row, parent, false)
//            viewHolder = ViewHolder()
//            viewHolder.countText = convertView!!.findViewById(R.id.textview)
//            viewHolder.deleteCheckBox = convertView!!.findViewById(R.id.checkBox)
//            convertView!!.setTag(viewHolder)
//        } else {
//            viewHolder = convertView!!.getTag()
//        }
//
//        if (adapterData != null) {
//            val item = adapterData!![position]
//            viewHolder.countText!!.setText(item.getCountString())
//            if (inDeletionMode) {
//                viewHolder.deleteCheckBox!!.setOnCheckedChangeListener { buttonView, isChecked -> countersToDelete.add(item.getId()) }
//            } else {
//                viewHolder.deleteCheckBox!!.setOnCheckedChangeListener(null)
//            }
//            viewHolder.deleteCheckBox!!.isChecked = countersToDelete.contains(item.getId())
//            viewHolder.deleteCheckBox!!.visibility = if (inDeletionMode) View.VISIBLE else View.GONE
//        }
//        return convertView
//    }
//}