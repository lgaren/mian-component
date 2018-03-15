package com.dsg.common.constants;

import com.dsg.common.conf.DSGConfig;
/**
 * Created on 2018/3/13 2018 三月 星期二上午 11:17
 * mian-component
 *
 * @author: <a href="mailto:(liuyuanyuan@lvmama.com)">Liu Yuanyuan</a>
 * Version:  dataTest, V 0.0  2018/3/13 上午 10:25 DSG Exp$$
 */
@Deprecated
public enum DatabaseConfig {

    @Deprecated
    HIVE_SERVER2{
        public String driverName(){return DSGConfig.getVar(DSGConfig.ConfVars.HIVE_SERVER2_DRIVWE);}
        public String urlPrefix(){return "jdbc:hive2://";}
        public String userName(){return DSGConfig.getVar(DSGConfig.ConfVars.HIVE_SERVER2_USER);}
        public String password(){return DSGConfig.getVar(DSGConfig.ConfVars.HIVE_SERVER2_PASSWORD);}
        public String host(){return DSGConfig.getVar(DSGConfig.ConfVars.HIVE_SERVER2_HOST);}
        public int port(){return DSGConfig.getIntVar(DSGConfig.ConfVars.HIVE_SERVER2_PORT);}
    },

    @Deprecated
    MYSQL{
        public String driverName(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_DRIVER);}
        public String urlPrefix(){return "jdbc:mysql://";}
        public String userName(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_USER);}
        public String password(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_PASSWORD);}
        public String host(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_HOST);}
        public int port(){return DSGConfig.getIntVar(DSGConfig.ConfVars.DATABASE_PORT);}
    },

    @Deprecated
    ORACLE{
        public String driverName(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_DRIVER);}
        public String urlPrefix(){return "jdbc:oracle:thin:@//";}
        public String userName(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_USER);}
        public String password(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_PASSWORD);}
        public String host(){return DSGConfig.getVar(DSGConfig.ConfVars.DATABASE_HOST);}
        public int port(){return DSGConfig.getIntVar(DSGConfig.ConfVars.DATABASE_PORT);}
    };

    @Deprecated
    public String driverName(){throw new AbstractMethodError();}

    @Deprecated
    public String urlPrefix(){throw new AbstractMethodError();}

    @Deprecated
    public String userName(){throw new AbstractMethodError();}

    @Deprecated
    public String password(){throw new AbstractMethodError();}

    @Deprecated
    public String host(){throw new AbstractMethodError();}

    @Deprecated
    public int port(){throw new AbstractMethodError();}


}
