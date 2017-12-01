package jms.eqmsclient.Controller

import android.content.Context
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
    @BindView (R.id.equipment_list_raw_name) lateinit var mName:TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /**
         * Viewを取得する
         * convertViewがNullの場合は参照するViewを定義する
         */
        val view = convertView ?: inflater.inflate(R.layout.equipment_list_raw,null)

        /** ButterKnifeで定義したViewFieldをbindする*/
        ButterKnife.bind(this,view!!)

        if (adapterData != null) {
            adapterData?.get(position)?.run {
                mCode.text = code
                mNumber.text = worker
                mName.text = name
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