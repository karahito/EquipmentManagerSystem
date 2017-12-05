
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


package jms.eqmsclient

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jms.android.commons.network.LogUtil
import jms.android.commons.network.Protocol.RxNetwork
import jms.eqmsclient.Controller.EquipmentRealmAdapter
import jms.eqmsclient.UI.ContentsFragment


class MainActivity : RxAppCompatActivity() {

    companion object {
        private val url = "http://192.168.1.208:8080/findAll"
        private val subscription = RxNetwork().getDatabase(url)
    }

    private lateinit var adapter: EquipmentRealmAdapter

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
                .retry(1000)
                .bindToLifecycle(this)
                .subscribe(
                        {},
                        {
                            LogUtil.e("offline work",it)
                            progress.dismiss()
                            supportFragmentManager.beginTransaction()
                                    .add(R.id.contents, ContentsFragment.newInstance())
                                    .commit()
                        },
                        {
                            progress.dismiss()
                            LogUtil.d("complete")
                            /** add Fragment */
                            supportFragmentManager.beginTransaction()
                                    .add(R.id.contents, ContentsFragment.newInstance())
                                    .commit()
                        })

    }
}

