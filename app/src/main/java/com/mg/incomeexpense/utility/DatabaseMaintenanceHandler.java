package com.mg.incomeexpense.utility;

import android.support.annotation.NonNull;

/**
 * Created by mario on 2017-03-11.
 */

public interface DatabaseMaintenanceHandler {
    void addListener(@NonNull DatabaseMaintenanceListener listener);
    void connected();
    void restoreDone();
    void backupDone();
    void error();
}
