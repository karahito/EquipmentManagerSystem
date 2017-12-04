
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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxFragment
import jms.eqmsclient.R

/**
 * 入力インターフェース用フラグメント
 *
 * @author D.Noguchi
 * @since 1,Sep.2017
 */
class InputInterfaceFragment:RxFragment() {
    companion object {
        @JvmStatic fun newInstance():InputInterfaceFragment = InputInterfaceFragment()
    }

    interface onSearchEvent{
        fun onSearchClick(worker:String?,code:String?,omit:Boolean)
    }

    private lateinit var target:Fragment

    @BindView(R.id.input_code) lateinit var mCode: AppCompatEditText
    @BindView(R.id.input_worker) lateinit var  mWorker: AppCompatEditText
    @BindView(R.id.check_omit) lateinit var  mOmit: AppCompatCheckBox
    @BindView(R.id.search) lateinit var mSearch:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.input_interfase_field,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this,view)

        mWorker.nextFocusDownId = mCode.id
        mCode.nextFocusDownId = mOmit.id


            mSearch.setOnClickListener {
                target = fragmentManager?.findFragmentByTag(R.string.tag_EquipmentFragment.toString()) ?: throw ClassCastException("Target is not found")
                if (target !is onSearchEvent)
                    throw ClassCastException("target is not implemented listener")
                val listener = target as onSearchEvent
                var worker:String? = null
                var code:String? = null
                if (!mWorker.text.isNullOrBlank()) {
                     worker = mWorker.text.toString()
                }
                if (!mCode.text.isNullOrBlank()) {
                     code = mCode.text.toString()
                }
                listener.onSearchClick(worker,code,mOmit.isChecked)
            }


    }
}