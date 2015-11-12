package com.gt.bmf.service;

import com.gt.bmf.pojo.GfQueryLog;

import java.io.IOException;

public interface GfQueryLogService extends BmfBaseService<GfQueryLog> {
    public void checkData() throws IOException, InterruptedException;
    public void buy(double upPrice,double downPrice);
    public void setLockBuyAction(boolean lockBuyAction);
    public void setLockSaleAction(boolean lockSaleAction);
    public boolean isLockBuyAction();
    public boolean isLockSaleAction() ;

}
