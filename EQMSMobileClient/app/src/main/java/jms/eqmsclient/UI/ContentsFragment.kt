
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
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jms.android.commons.network.LogUtil
import jms.android.commons.network.Protocol.RxCommit
import jms.eqmsclient.Controller.RxCreateCommitData
import jms.eqmsclient.R

/**
 * @author D.Noguchi
 * @since 1,Sep.2017
 */
class ContentsFragment:RxFragment() {
    companion object {
        @JvmStatic fun newInstance() = ContentsFragment()
        @JvmStatic val subscription = RxCreateCommitData().rxGetCommitData()
        @JvmStatic private val url = "http://192.168.1.208:8080/transaction"
    }

    @BindView(R.id.commit) lateinit var mCommit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.beginTransaction()
                .add(R.id.top_container,InputInterfaceFragment.newInstance(),R.string.tag_InputInterfaceFragment.toString())
                .add(R.id.center_container,EquipmentFragment.newInstance(),R.string.tag_EquipmentFragment.toString())
                .commit()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_contents_canvas,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButterKnife.bind(this,view)
        mCommit.setOnClickListener {
            val progress = ProgressDialog(this.context)
            progress.show()
            subscription.subscribeOn(Schedulers.computation())
                    .bindToLifecycle(this)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            /** onNext */
                            {
                                RxCommit().postDatabase(url,it)
                                        .subscribeOn(Schedulers.computation())
                                        .bindToLifecycle(this)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                {},
                                                {},
                                                {})
                            },
                            /** onError */
                            {},
                            /** onComplete */
                            {
                                progress.dismiss()
                            }
                    )
//            LogUtil.d(RxCreateCommitData().CreateJson())
//            val progress = ProgressDialog(this.context)
//            val json = RxCreateCommitData().CreateJson()
//            if (json != null) {
//                subscription.postDatabase(url,json)
//                        .subscribeOn(Schedulers.computation())
//                        .retry(2)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                /** onNext*/
//                                {
//                                    LogUtil.d("onNext"+it.toString())
//                                },
//                                /** onError */
//                                {
//                                    LogUtil.e(it)
//                                    progress.dismiss()
//                                },
//                                /** onComplete */
//                                {
//                                    LogUtil.d("onComplete")
//                                    progress.dismiss()
//                                    Toast.makeText(this.context, "Commit Successful", Toast.LENGTH_SHORT).show()
//                                }
//                        )
//            }else{
//                progress.dismiss()
////                throw ClassCastException("json type error")
//            }
        }
    }
}