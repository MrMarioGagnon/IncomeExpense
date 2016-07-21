package com.mg.incomeexpense.core;

import java.io.Serializable;

public abstract class ObjectBase implements Serializable {

    protected boolean mDirty;
    protected boolean mDead;
    protected boolean mNew;
    protected Long mId;

    {
        mId = null;
        mDead = false;
    }

    public boolean isDead() {
        return mDead;
    }

    public void setDead(boolean dead) {
        mDead = dead;
        mDirty = dead;
    }

    public boolean isDirty() {
        return mDirty;
    }

    public boolean isNew() {
        return mNew;
    }

    public void setNew(boolean new_) {
        mNew = new_;
        mDirty = mNew;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }
}