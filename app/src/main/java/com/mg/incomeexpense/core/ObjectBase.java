package com.mg.incomeexpense.core;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public abstract class ObjectBase implements Serializable {

    protected Boolean mDirty;
    protected Boolean mDead;
    protected Boolean mNew;
    protected Long mId;

    {
        mId = null;
        mDead = false;
    }

    public Boolean isDead() {
        return mDead;
    }

    public void setDead(boolean dead) {
        mDead = dead;
        mDirty = dead;
    }

    public Boolean isDirty() {
        return mDirty;
    }

    public Boolean isNew() {
        return mNew;
    }

    public void setNew(Boolean new_) {
        mNew = new_;
        mDirty = mNew;
    }

    public Long getId() {
        return mId;
    }

    public void setId(@NonNull Long id) {
        Objects.requireNonNull(id, "Parameter id of type Long is mandatory");
        mId = id;
    }
}