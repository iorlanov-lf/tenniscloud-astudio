package com.logiforge.tenniscloud;

import com.logiforge.lavolta.android.api.AuthenticationPlugin;
import com.logiforge.lavolta.android.app.DefaultAuthPlugin;
import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.app.SyncUrlFactory;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.model.api.LogContext;
import com.logiforge.lavolta.android.model.api.sync.DataDump;
import com.logiforge.lavolta.android.model.api.sync.DataSlice;
import com.logiforge.lavolta.android.model.api.sync.LocalChanges;
import com.logiforge.lavolta.android.model.api.sync.LocalChangesSyncResult;
import com.logiforge.lavolta.android.model.api.sync.RemoteChangesDigest;
import com.logiforge.lavolta.android.model.config.SyncSetupSpecs;
import com.logiforge.lavolta.android.sync.jobs.ReconcileDynamicDataJob;
import com.logiforge.lavolta.android.sync.jobs.bo.CommitRequest;
import com.logiforge.lavolta.android.sync.jobs.bo.JournalTransaction;
import com.logiforge.lavolta.android.sync.jobs.bo.SyncBO;
import com.logiforge.tenniscloud.db.AppDb;
import com.logiforge.tenniscloud.db.TCLavoltaDb;

/**
 * Created by iorlanov on 2/26/2017.
 */
public class TCLavolta extends Lavolta {
    @Override
    protected LavoltaDb instantiateDatabase() {
        return new TCLavoltaDb(appContext);
    }

    @Override
    protected SyncUrlFactory instantiateUrlFactory() {
        return null;
    }

    @Override
    protected SyncSetupSpecs getSyncSetupSpecs() {
        return null;
    }

    @Override
    protected int getLavoltaConfigFileResourceId() {
        return 0;
    }

    @Override
    public AuthenticationPlugin getAuthPlugin() {
        DefaultAuthPlugin plugin = DefaultAuthPlugin.instance();
        if(plugin == null) {
            plugin = new TCAuthPlugin(appContext);
        }

        return plugin;
    }

    @Override
    protected SyncBO getSyncBO() {
        return new SyncBO() {

            @Override
            public void processCommitResults(CommitRequest commitRequest, LocalChangesSyncResult localChangesSyncResult, LogContext logContext) throws Exception {

            }

            @Override
            public void applyUpdates(RemoteChangesDigest remoteChangesDigest, LocalChanges localChanges, LogContext logContext) throws Exception {

            }

            @Override
            public void applyReplaceDataSlice(RemoteChangesDigest remoteChangesDigest, DataSlice dataSlice, LogContext logContext) throws Exception {

            }

            @Override
            public void applyAddDataSlice(RemoteChangesDigest remoteChangesDigest, DataSlice dataSlice, LogContext logContext) throws Exception {

            }

            @Override
            public ReconcileDynamicDataJob.ReconcileResult reconcile(LogContext logContext) throws Exception {
                return null;
            }
        };
    }

    @Override
    public void onRemoteUpdate(RemoteChangesDigest remoteChangesDigest, LocalChanges localChanges) {

    }

    @Override
    public void onReplaceDataSlice(DataSlice dataSlice) {

    }

    @Override
    public void onAddDataSlice(DataSlice dataSlice) {

    }

    @Override
    public void onDeleteUserData() {

    }

    @Override
    public void onCheckoutDynamicData(DataDump dataDump) {

    }

    @Override
    public void onExecuteServerWins(String s, String s1) {

    }

    @Override
    public boolean okToCommit(JournalTransaction journalTransaction) {
        return false;
    }
}
