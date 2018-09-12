package cn.gg.mybatis.session;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.executor.Executor;
import cn.gg.mybatis.executor.SimpleExecutor;

/**
 * @author GG
 * @date 2018年09月12日14:10:25
 * @return
 */
public class SqlseesionFactory {

    private Configuration configuration;

    public SqlseesionFactory( Configuration configuration){
        this.configuration = configuration;
    }

    public Sqlseesion openSession(){
        return openSessionFromDataSource();
    }

    private Sqlseesion openSessionFromDataSource() {
        Executor executor = new SimpleExecutor( configuration );
        Sqlseesion sqlSession = new Sqlseesion(configuration, executor);
        return sqlSession;
    }
}
