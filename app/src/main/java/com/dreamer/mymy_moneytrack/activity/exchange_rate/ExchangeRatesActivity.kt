package com.dreamer.mymy_moneytrack.activity.exchange_rate

import android.content.Intent
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.BaseAdapter
import android.widget.ListView
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnItemClick
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity
import com.dreamer.mymy_moneytrack.adapter.ExchangeRateAdapter
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.entity.ExchangeRatePair
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy
import com.dreamer.mymy_moneytrack.util.ExchangeRatesSummarizer
import java.util.*
import javax.inject.Inject

class ExchangeRatesActivity : BaseBackActivity() {

    var rateController: ExchangeRateController? = null
        @Inject set
    private var exchangeRateList: List<ExchangeRatePair?>? = null

    @BindView(R.id.listView)
    var listView: ListView? = null
    override fun getContentViewId(): Int {
        return R.layout.activity_exchange_rates
    }

    override fun initData(): Boolean {
        val result = super.initData()
        appComponent.inject(this@ExchangeRatesActivity)
        return result
    }

    override fun initViews() {
        super.initViews()
        registerForContextMenu(listView)
        update()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_exchange_rate, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.delete -> {
                deleteExchangeRate(info.position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun deleteExchangeRate(position: Int) {
        CrashlyticsProxy.get()?.logButton("Delete Exchange Rate")
        rateController!!.deleteExchangeRatePair(exchangeRateList!![position])
        update()
        setResult(RESULT_OK)
    }

    @OnClick(R.id.btn_add_exchange_rate)
    fun addExchangeRate() {
        CrashlyticsProxy.get()?.logButton("Add Exchange Rate")
        val intent = Intent(this@ExchangeRatesActivity, AddExchangeRateActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE)
    }

    @OnItemClick(R.id.listView)
    fun addExchangeRateOnBaseOfExisted(position: Int) {
        CrashlyticsProxy.get()?.logButton("Edit Exchange Rate")
        if (position < 0 || position >= exchangeRateList!!.size) return
        val intent = Intent(this@ExchangeRatesActivity, AddExchangeRateActivity::class.java)
        intent.putExtra(AddExchangeRateActivity.KEY_EXCHANGE_RATE, exchangeRateList!![position])
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD_EXCHANGE_RATE -> {
                    update()
                    setResult(RESULT_OK)
                }
                else -> {}
            }
        }
    }

    private fun update() {
        exchangeRateList = ExchangeRatesSummarizer(rateController!!.readAll()).pairedSummaryList
        Collections.reverse(exchangeRateList)
        listView!!.adapter = ExchangeRateAdapter(this@ExchangeRatesActivity, exchangeRateList)
        (listView!!.adapter as BaseAdapter).notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ExchangeRatesActivity"
        private const val REQUEST_ADD_EXCHANGE_RATE = 1
    }
}