package com.dreamer.mymy_moneytrack.activity.account.edit.fragment

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.R.layout
import com.dreamer.mymy_moneytrack.activity.base.BaseFragment
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.controller.data.AccountController
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy
import com.dreamer.mymy_moneytrack.util.validator.EditAccountValidator
import com.dreamer.mymy_moneytrack.util.validator.IValidator
import kotlinx.android.synthetic.main.fragment_edit_account.*
import javax.inject.Inject

class EditAccountFragment : BaseFragment() {

    @Inject
    internal lateinit var accountController: AccountController
    @Inject
    internal lateinit var formatController: FormatController

    private lateinit var accountValidator: IValidator<Account>

    private lateinit var account: Account

    override val contentViewId: Int = layout.fragment_edit_account

    override fun initData() {
        appComponent.inject(this@EditAccountFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT) }
    }

    override fun initViews(view: View) {
        etTitle.setText(account.title)
        etGoal.setText(formatController.formatPrecisionNone(account.goal))
        viewColor.setBackgroundColor(account.color)

        val fabDone = view.rootView.findViewById<FloatingActionButton>(R.id.fabDone)
        fabDone.setOnClickListener { done() }

        accountValidator = EditAccountValidator(context!!, view)
    }

    private fun done() {
        CrashlyticsProxy.get().logButton("Edit Account")
        if (accountValidator.validate()) {
            val title = etTitle.text.toString().trim { it <= ' ' }
            val goal = etGoal.text.toString().toDouble()

            val newAccount = Account(
                account.id, title, account.curSum.toDouble(),
                account.currency, goal, account.isArchived, account.color
            )
            val updated = accountController.update(newAccount) != null
            if (updated) {
                CrashlyticsProxy.get().logEvent("Edit Account")
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        }
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): EditAccountFragment {
            val fragment = EditAccountFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }

}
