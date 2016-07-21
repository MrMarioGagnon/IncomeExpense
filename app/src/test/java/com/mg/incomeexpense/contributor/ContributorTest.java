package com.mg.incomeexpense.contributor;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorTest {

    @Test
    public void testCreate() throws Exception {

        // Preparation
        String contributorName = "Contributor1";

        //Execution
        Contributor c = Contributor.create(1l, contributorName);

        //Verification
        assertEquals(contributorName, c.getName());
    }

    @Test
    public void testCreateNullId() throws Exception{

        // Preparation
        Long id = null;
        String contributorName = "Contributor1";

        // Execution
        Contributor c = Contributor.create(id, contributorName);

    }

    @Test
    public void testCreateNew() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testSetName() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }

    @Test
    public void testCompareTo() throws Exception {

    }
}