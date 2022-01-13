package com.dreamer.mymy_moneytrack.entity

sealed class RecordItem {

    data class Header(val date: String) : RecordItem()

    data class Record(val title: String, val categoryName: String, val fullPrice: Double, val currency: String, val isIncome: Boolean) : RecordItem() {
        constructor(record: com.dreamer.mymy_moneytrack.entity.data.Record) : this(record.title, record.category?.name?.toString().orEmpty(), record.fullPrice, record.currency, record.isIncome)
    }
}