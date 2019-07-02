package com.irs.quartz;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.irs.pojo.TbLog;
import com.irs.service.LogService;
import com.irs.util.DateUtil;
import com.irs.util.GlobalUtil;

/**
 * 清除系统日志任务
 * @author Mr Du
 *
 */

@Component
public class MethodLogQuartz {
    private static Logger LOGGER = Logger.getLogger(MethodLogQuartz.class);
    private static final String LOGDAYS = "log.days";

    @Autowired
    private LogService logServiceImpl;
    /** 
     * cron表达式：* * * * * *（共6位，使用空格隔开，具体如下） 
     * cron表达式：*(秒0-59) *(分 钟0-59) *(小时0-23) *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT) 
     */ 
    @Scheduled(cron="0 0 0 * * ?")
    public void clearLog() {
        int logDays = Integer.valueOf(GlobalUtil.getValue(LOGDAYS));
        Date date = DateUtil.getDate(DateUtil.getDate(), -logDays);
        int count =logServiceImpl.delLogsByDate(date);
        String date1=DateUtil.getStringDate(date, DateUtil.DateFormat1);
		LOGGER.info("日志定时删除任务，删除日志数量:" + count + ", createDate < "
                + date1);
        TbLog log=new TbLog();
        log.setIp("");
        log.setOperation("定时删除日志："+count+"条");
        log.setCreateTime(date1);
        log.setUsername("系统自动任务");
        log.setMethod("");
        log.setParams("");
		logServiceImpl.insLog(log);
    }
}
