package com.dsg.common.log;

import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;

/**
 * Created on 2018/2/12018 二月 星期四下午 19:31
 * coreTest
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  DSGRollingFileAppender, V 0.0  2018/2/1 下午 19:31 DSG Exp$$
 *
 *  （文件大小到达指定尺寸的时候产生一个新的文件）
 */
public class DSGRollingFileAppender extends RollingFileAppender {

    // 实现Log分级别输出
    @Override
    public boolean isAsSevereAsThreshold(Priority priority) {

        return this.getThreshold().equals(priority);
    }
}
