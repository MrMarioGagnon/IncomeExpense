package com.mg.incomeexpense.core;

import android.content.ContentResolver;
import android.test.AndroidTestCase;

import com.mg.incomeexpense.data.IncomeExpenseContract;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by mario on 2016-07-24.
 */
public class ToolsTest extends AndroidTestCase {

    @Test
    public void testConvertIdsToItems_happyPath() throws Exception {

        // Preparation
        ContentResolver cr = mContext.getContentResolver();

        // Execution
        List<ObjectBase> items = Tools.ConvertIdsToItems(cr, IncomeExpenseContract.ContributorEntry.CONTENT_URI, "1;2", ";");

        //Verification
        assertEquals(2, items.size());

    }
}