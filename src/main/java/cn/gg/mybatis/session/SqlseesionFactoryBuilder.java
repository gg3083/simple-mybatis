package cn.gg.mybatis.session;

import cn.gg.mybatis.configuration.Configuration;

/**
 * @author GG
 * @date 2018年09月12日14:10:25
 * @return
 */
public class SqlseesionFactoryBuilder {

    private Configuration configuration;


    public SqlseesionFactoryBuilder( Configuration configuration){
        this.configuration = configuration;
    }

    public SqlseesionFactory build(){
        return new SqlseesionFactory( configuration );
    }
}
