package com.mg.incomeexpense.core;

/**
 * Created by mario on 2016-07-21.
 */
public class ItemStateChangeEvent {

    private ObjectBase mItem;
    private Boolean mIsCancelled = false;

    public Boolean isCancelled() {
        return mIsCancelled;
    }

    public ObjectBase getItem() {
        return mItem;
    }

    public ItemStateChangeEvent(ObjectBase item)
    {
        if(item == null){
            throw new NullPointerException("Parameter item of type ObjectBase is mandatory.");
        }

        if(item.getId() == null && !item.isNew()){
            throw new NullPointerException("Item id is null but item is not new.");
        }

        mItem = item;
        mIsCancelled = false;

    }

    public ItemStateChangeEvent(){
        mIsCancelled = true;
    }

}
