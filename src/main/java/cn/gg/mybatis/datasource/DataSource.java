package cn.gg.mybatis.datasource;

import lombok.Data;

/**
 * @author gg
 * @date 2018-09-12 10:08:23
 * @return 数据库工具类
 */
@Data
public class DataSource {

    private String driver;
    private String url;
    private String userName;
    private String passWord;

    public DataSource( String driver , String url , String userName , String passWord ){
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.passWord = passWord;
    }
}
