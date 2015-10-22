package com.gt.bmf.cronjob;

import com.gt.bmf.service.GfQueryLogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Administrator on 15-7-23.
 */
public class AutoBuildTerminal extends TransactionalQuartzTask  {


    private GfQueryLogService gfQueryLogService;

    public void setGfQueryLogService(GfQueryLogService gfQueryLogService) {
        this.gfQueryLogService = gfQueryLogService;
    }

    @Override
    protected void executeTransactional(JobExecutionContext ctx) throws JobExecutionException {
        try {
            gfQueryLogService.checkData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
