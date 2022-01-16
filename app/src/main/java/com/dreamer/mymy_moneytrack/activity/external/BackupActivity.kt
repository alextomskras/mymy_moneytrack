package com.dreamer.mymy_moneytrack.activity.external


import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnItemClick
import com.dreamer.mymy_moneytrack.MtApp
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity
import com.dreamer.mymy_moneytrack.adapter.BackupAdapter
import com.dreamer.mymy_moneytrack.controller.PreferenceController
import com.dreamer.mymy_moneytrack.controller.backup.BackupController
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.DbxClientV2
import timber.log.Timber
import javax.inject.Inject

class BackupActivity : BaseBackActivity(), BackupAdapter.OnBackupListener,
    BackupController.OnBackupListener {

    var preferenceController: PreferenceController? = null
    @Inject set


    var backupController: BackupController? = null
        @Inject set
    private var dbClient: DbxClientV2? = null

    @BindView(R.id.btn_backup_now)
    var btnBackupNow: View? = null

    @BindView(R.id.listView)
    var listView: ListView? = null
    override fun getContentViewId(): Int {
        return R.layout.activity_backup
    }

    override fun initData(): Boolean {
        appComponent.inject(this@BackupActivity)
        val accessToken = preferenceController!!.readDropboxAccessToken()
        if (accessToken == null) {
            Auth.startOAuth2Authentication(this@BackupActivity, APP_KEY)
        } else {
            val config = DbxRequestConfig("open_money_tracker")
            dbClient = DbxClientV2(config, accessToken)
            backupController!!.onBackupListener = this
            fetchBackups()
        }
        return super.initData()
    }

    override fun initViews() {
        super.initViews()
        btnBackupNow!!.isEnabled = preferenceController!!.readDropboxAccessToken() != null
    }

    override fun onResume() {
        super.onResume()
        if (Auth.getOAuth2Token() != null) {
            try {
                preferenceController!!.writeDropboxAccessToken(Auth.getOAuth2Token())
                btnBackupNow!!.isEnabled = true
                val config = DbxRequestConfig("open_money_tracker")
                dbClient = DbxClientV2(config, Auth.getOAuth2Token())
                fetchBackups()
            } catch (e: IllegalStateException) {
                Timber.e("Error authenticating: %s", e.message)
            }
        }
    }

    override fun onBackupDelete(backupName: String) {
        val builder = AlertDialog.Builder(this@BackupActivity)
        builder.setTitle(getString(R.string.delete_backup_title))
        builder.setMessage(getString(R.string.delete_backup_message, backupName))
        builder.setPositiveButton(android.R.string.ok) { dialog, which -> removeBackup(backupName) }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    override fun onBackupsFetched(backupList: List<String>) {
        if (isFinishing) return
        stopProgress()
        val backupAdapter = BackupAdapter(this@BackupActivity, backupList)
        backupAdapter.onBackupListener = this@BackupActivity
        listView!!.adapter = backupAdapter
    }

    override fun onBackupSuccess() {
        CrashlyticsProxy.get()?.logEvent("Backup success")
        Timber.d("Backup success.")
        if (isFinishing) return
        stopProgress()
        fetchBackups()
    }

    override fun onBackupFailure(reason: String?) {
        CrashlyticsProxy.get()?.logEvent("Backup failure")
        Timber.d("Backup failure.")
        if (isFinishing) return
        stopProgress()
        showToast(R.string.failed_create_backup)
        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION == reason) logout()
    }

    override fun onRestoreSuccess(backupName: String) {
        CrashlyticsProxy.get()?.logEvent("Restore Success")
        Timber.d("Restore success.")
        if (isFinishing) return
        stopProgress()
        val builder = AlertDialog.Builder(this@BackupActivity)
        builder.setTitle(getString(R.string.backup_is_restored))
        builder.setMessage(getString(R.string.backup_restored, backupName))
        builder.setOnDismissListener {
            MtApp.get()?.buildAppComponent()
            setResult(RESULT_OK)
            finish()
        }
        builder.setPositiveButton(android.R.string.ok, null)
        builder.show()
    }

    override fun onRestoreFailure(reason: String?) {
        CrashlyticsProxy.get()?.logEvent("Restore Failure")
        Timber.d("Restore failure.")
        if (isFinishing) return
        stopProgress()
        showToast(R.string.failed_restore_backup)
        if (BackupController.OnBackupListener.ERROR_AUTHENTICATION == reason) logout()
    }

    override fun onRemoveSuccess() {
        CrashlyticsProxy.get()?.logEvent("Remove Success")
        Timber.d("Remove success.")
        if (isFinishing) return
        stopProgress()
        fetchBackups()
    }

    override fun onRemoveFailure(reason: String?) {
        CrashlyticsProxy.get()?.logEvent("Remove Failure")
        Timber.d("Remove failure.")
        if (isFinishing) return
        stopProgress()
        showToast(reason)
    }

    @OnClick(R.id.btn_backup_now)
    fun backupNow() {
        CrashlyticsProxy.get()?.logButton("Make Backup")
        startProgress(getString(R.string.making_backup))
        backupController!!.makeBackup(dbClient!!)
    }

    @OnItemClick(R.id.listView)
    fun restoreBackupClicked(position: Int) {
        CrashlyticsProxy.get()?.logButton("Restore backup")
        val backupName = listView!!.adapter.getItem(position).toString()
        val builder = AlertDialog.Builder(this@BackupActivity)
        builder.setTitle(getString(R.string.warning))
        builder.setMessage(getString(R.string.want_erase_and_restore, backupName))
        builder.setPositiveButton(android.R.string.ok) { dialog, which -> restoreBackup(backupName) }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    private fun restoreBackup(backupName: String) {
        startProgress(getString(R.string.restoring_backup))
        backupController!!.restoreBackup(dbClient!!, backupName)
    }

    private fun fetchBackups() {
        startProgress(getString(R.string.fetching_backups))
        backupController!!.fetchBackups(dbClient!!)
    }

    private fun removeBackup(backupName: String) {
        startProgress(getString(R.string.removing_backup))
        backupController!!.removeBackup(dbClient!!, backupName)
    }

    private fun logout() {
        preferenceController!!.writeDropboxAccessToken(null)
        Auth.startOAuth2Authentication(this@BackupActivity, APP_KEY)
        btnBackupNow!!.isEnabled = false
    }

    companion object {
        private const val APP_KEY = "5lqugcckdy9y6lj"
    }
}