package cn.gg.mybatis.executor;

import cn.gg.mybatis.mapper.MapperData;

import java.util.List;

/**
 * @author gg
 * @date 2018年09月12日14:18:03
 * @return 执行器
 */
public interface Executor {

    /**
     * 查询
     * @param mapperData 封装的sql和返回值类型
     * @param args 参数
     * @param <E>
     * @return
     */
    <E> List<E> query(MapperData mapperData , Object args);

    /**
     * 增删改
     * @param mapperData 封装的sql和返回值类型
     * @param args 参数
     * @return
     */
    Integer update(MapperData mapperData ,Object args);
}
