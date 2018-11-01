import com.dsg.common.constants.DatabaseType;
import com.dsg.common.utils.JdbcFactory;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2018/7/24  Tue AM 11:34
 * mian-component
 *
 * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
 * @Version: Test V 0.0, Jul 24, 2018 DSG Exp$$
 * @Since 1.8
 * @Description :
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Pattern colr = Pattern.compile("smalldatetime|timestamp|datetime");
        Matcher mat = colr.matcher("smalldatetime");
        if (mat.find()) System.out.println("==============");
        String sql = "SELECT d.name as tab_name, a.name as col_name, b.name as coltype FROM   syscolumns   a left   join   systypes   b   on   a.xusertype=b.xusertype inner  join   sysobjects   d   on   a.id=d.id     and   d.xtype='U'   and     d.name<>'dtproperties' where d.name='?'";
        JdbcFactory.JDBC table_col = JdbcFactory.get(DatabaseType.SQL_SERVER, "joyu");
        ResultSet res = table_col.<String>executeQuery(sql,"t_discount_allot");
        while(res.next()){
            System.out.println(res.getString(1  ) + "======" + res.getString(2)+ "======" + res.getString(3));
        }
        res.close();
        table_col.close();
    }
}
