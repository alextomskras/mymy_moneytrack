package com.dreamer.mymy_moneytrack.util

import com.dreamer.mymy_moneytrack.controller.PreferenceController
import com.dreamer.mymy_moneytrack.controller.data.CategoryController
import java.util.*

class CategoryAutoCompleter(
    private val categoryController: CategoryController,
    private val preferenceController: PreferenceController
) {
    private val categoryList: MutableList<String>
    private val recordTitleCategoryMap: MutableMap<String, String>
    fun completeByPart(part: String?): List<String> {
        val resultList: MutableList<String> = ArrayList()
        for (category in categoryList) {
            if (category.startsWith(part!!)) resultList.add(category)
        }
        return resultList
    }

    fun removeFromAutoComplete(category: String) {
        categoryList.remove(category)
        categoryController.disableCategory(category)
    }

    fun completeByRecordTitle(title: String): String? {
        return recordTitleCategoryMap[title]
    }

    fun addRecordTitleCategoryPair(title: String, category: String) {
        if (title.isEmpty() || category.isEmpty()) {
            return
        }
        recordTitleCategoryMap[title] = category
        preferenceController.writeRecordTitleCategoryPairs(recordTitleCategoryMap)
    }

    init {
        categoryList = ArrayList()
        recordTitleCategoryMap = preferenceController.readRecordTitleCategoryPairs()
        for (category in categoryController.readFiltered()) {
            categoryList.add(category.name)
        }
    }
}