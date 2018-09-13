package cn.gg.mybatis.session;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.executor.Executor;
import cn.gg.mybatis.mapper.MapperData;
import cn.gg.mybatis.mapper.MapperPoxy;
import lombok.Data;

import java.lang.reflect.Proxy;

/**
 * @author GG
 * @date 2018年09月12日14:10:25
 * @return
 */
@Data
public class Sqlseesion {

    private Configuration configuration;
    private Executor executor;

    public Sqlseesion( Configuration configuration ,Executor executor){
        this.configuration = configuration;
        this.executor = executor;
    }

    public <E> E getMapper(Class<?> clazz){
        return (E) Proxy.newProxyInstance(this.getClass().getClassLoader() , new Class[]{clazz},new MapperPoxy( this ,clazz));
    }

    public <E> E select(MapperData data ,Object args){
        return (E)executor.query( data , args );
    }


}
