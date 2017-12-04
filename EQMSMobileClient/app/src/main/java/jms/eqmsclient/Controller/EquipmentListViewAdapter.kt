package jms.eqmsclient.Controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import jms.android.commons.network.Protocol.EquipmentEntity
import jms.eqmsclient.R

/**
 * 備品管理アイテム表示ListView用アダプタ
 *
 * @param context
 * @param item
 *
 * @author D.Noguchi
 */
class EquipmentListViewAdapter(context: Context,item:List<EquipmentEntity>):ArrayAdapter<EquipmentEntity>(context,0,item){
    /** resのLayoutResourceを参照するためにLayoutInflaterを生成*/
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * ButterKnifeでViewFiledを定義
     * ＊この時点ではBindされていないので参照値はnull
     */
    @BindView (R.id.equipment_list_raw_code) lateinit var mCode:TextView
    @BindView (R.id.equipment_list_raw_number) lateinit var mNumber:TextView
    @BindView (R.id.equipment_list_raw_date) lateinit var mName:TextView

    /**
     * Itemの表示Layout用のViewを取得
     * @param position
     * @param convertView
     * @param parent
     *
     * @return View
     *
     * @author D.Noguchi
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /**
         * Viewを取得する
         * convertViewがNullの場合は参照するViewを定義する
         */
        val view = convertView ?: inflater.inflate(R.layout.equipment_list_raw,parent)

        /** ButterKnifeで定義したViewFieldをbindする*/
        ButterKnife.bind(this,view!!)

        // List行のViewの動的変更を行う

        /**
         *  現在行のItemを取得してViewにセットする
         */
        this.getItem(position).run {
            mCode.text = code
            mNumber.text = worker
            mName.text = name
        }
        return view
    }

}