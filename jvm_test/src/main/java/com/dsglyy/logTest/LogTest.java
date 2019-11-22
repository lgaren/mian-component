package com.dsglyy.logTest;

import com.dsg.common.conf.DSGConfig;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2018/1/292018 一月 星期一下午 15:17
 * coreTest
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  LogTest, V 0.0  2018/1/29 下午 15:17 DSG Exp$$
 */
public class LogTest {
    private final static Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args){
<<<<<<< HEAD
//
//
=======
//        -Dlog4j.configuration=file:${KYLIN_HOME}/conf/kylin-server-log4j.properties
>>>>>>> f515e2b1fd02cb3af610c9d249efc5732d1c37eb
//        -Dproject.configuration=../conf/dsg-site.xml
        logger.info(" info hahahahaASFh");
        logger.warn("warn hahahah");
        logger.debug("debug haasdhahaha");
        System.err.println("22222222222222");
        logger.error("error hahahaha");
          DSGConfig conf = DSGConfig.getConf();
          System.out.println(conf.getTimeVar(DSGConfig.ConfVars.RESOURCE_LIMIT_UNUSEDTIME, TimeUnit.SECONDS));

//        Configuration conf = new Configuration();
//        conf.addResource("dsg-site.xml");
//        System.out.println(conf.get("com.lvmama.seagull.resource.aliveTime"));
    }


}
