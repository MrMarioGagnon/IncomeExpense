package com.mg.incomeexpense.utility;

/**
 * Created by mario on 2017-03-04.
 */

public interface DatabaseMaintenanceListener {
    void onConnected();

    void onDatabaseBackuped();

    void onDatabaseRestored();

    void onError();
}
