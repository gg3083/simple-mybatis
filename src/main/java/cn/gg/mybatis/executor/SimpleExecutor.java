package cn.gg.mybatis.executor;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.mapper.MapperData;
import cn.gg.mybatis.statement.StatementHandler;

public class SimpleExecutor implements Executor {

    private Configuration configuration;

    public SimpleExecutor( Configuration configuration ){
        this.configuration = configuration;
    }

    @Override
    public <E> E query(MapperData mapperData, Object args) {
        StatementHandler handler = new StatementHandler( configuration );
        return handler.query( mapperData , args );
    }

    @Override
    public Integer update(MapperData mapperData, Object args) {
        return null;
    }
}
