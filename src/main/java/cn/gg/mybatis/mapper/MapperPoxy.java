package cn.gg.mybatis.mapper;

import cn.gg.mybatis.session.Sqlseesion;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author GG
 * @date 2018年09月12日16:10:25
 * @return 代理
 */
public class MapperPoxy implements InvocationHandler{

    private Sqlseesion session;
    private Class mapperInterface;

    public MapperPoxy(Sqlseesion session, Class mapperInterface) {
        this.session = session;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperData mapperData = session.getConfiguration().getMapperRegister().getData()
                .get( mapperInterface.getName() + "." + method.getName() );
        if ( mapperData != null){
            return session.select( mapperData ,args );
        }
        return method.invoke(proxy,args);
    }
}
