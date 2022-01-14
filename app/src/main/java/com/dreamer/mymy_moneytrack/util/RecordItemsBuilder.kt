package com.dreamer.mymy_moneytrack.util

import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.entity.RecordItem
import com.dreamer.mymy_moneytrack.entity.data.Record
import javax.inject.Inject

class RecordItemsBuilder {

    @Inject
    lateinit var formatController: FormatController

    constructor() {
        MtApp.get().appComponent.inject(this)
    }

    fun getRecordItems(recordList: List<Record>): List<RecordItem> {
        val recordItems: MutableList<RecordItem> = mutableListOf()

        var lastDate: String? = null
        for (record in recordList) {
            if (formatController.formatDateToString(record.time) != lastDate) {
                lastDate = formatController.formatDateToString(record.time)
                recordItems.add(RecordItem.Header(lastDate))
            }

            recordItems.add(RecordItem.Record(record))
        }

        return recordItems
    }

}