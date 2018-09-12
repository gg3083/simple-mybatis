package cn.gg.mybatis.mapper;

import lombok.Data;

/**
 * @author GG
 * @date 2018年09月12日14:08:45
 * @return 封装方法对应的sql以及返回值
 */
@Data
public class MapperData {

    private String sql;
    private Class type;

    public MapperData(String sql, Class type) {
        this.sql = sql;
        this.type = type;
    }
}
