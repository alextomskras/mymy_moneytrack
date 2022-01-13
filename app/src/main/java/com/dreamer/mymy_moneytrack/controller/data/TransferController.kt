package com.dreamer.mymy_moneytrack.controller.data

import com.dreamer.mymy_moneytrack.controller.base.BaseController
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.dreamer.mymy_moneytrack.entity.data.Transfer
import com.dreamer.mymy_moneytrack.repo.DbHelper
import com.dreamer.mymy_moneytrack.repo.base.IRepo

/**
 * Controller class to encapsulate transfer handling logic.
 * Created on 2/17/16.
 *
 * @author Evgenii Kanivets
 */
class TransferController(
    transferRepo: IRepo<Transfer>,
    private val accountController: AccountController
) : BaseController<Transfer>(transferRepo) {

    override fun create(transfer: Transfer?): Transfer? {
        val createdTransfer = repo.create(transfer)

        return if (createdTransfer == null) null else {
            accountController.transferDone(createdTransfer)
            createdTransfer
        }
    }

    fun getTransfersForAccount(account: Account): List<Transfer> {
        val condition = "${DbHelper.FROM_ACCOUNT_ID_COLUMN}=? OR ${DbHelper.TO_ACCOUNT_ID_COLUMN}=?"
        val args = arrayOf(account.id.toString(), account.id.toString())

        return readWithCondition(condition, args)
    }

}
