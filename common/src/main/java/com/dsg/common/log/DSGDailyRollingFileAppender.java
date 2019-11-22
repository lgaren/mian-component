package com.dsg.common.log;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;

/**
 * Created on 2018/2/12018 二月 星期四下午 19:24
 * coreTest
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  DSGLogAppender, V 0.0  2018/2/1 下午 19:24 DSG Exp$$
 *
 * （每天产生一个日志文件）
 */
public class DSGDailyRollingFileAppender extends DailyRollingFileAppender {

    // 实现Log分级别输出
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {

        return this.getThreshold().equals(priority);
    }
}
