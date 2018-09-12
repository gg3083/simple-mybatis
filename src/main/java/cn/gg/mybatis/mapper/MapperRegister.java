package cn.gg.mybatis.mapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author gg
 * @date 2018年09月12日11:57:00
 * @return 解析sql
 */
public class MapperRegister {

    private Map<String,MapperData> data = new HashMap<>();


    public void doRegister( Properties properties ) {
        Enumeration enumeration = properties.propertyNames();
        while ( enumeration.hasMoreElements() ){
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty( key );
            String sql = value.substring(0, value.indexOf("$"));
            String reg="(#[^}]*})";
            sql = sql.replaceAll(reg,"?");
            String type = value.substring( value.indexOf("$")+1,value.length() );
            System.err.println( "sql:"+ sql +" ---- type:" + type );
            try {
                Class typeClazz = this.getClass().getClassLoader().loadClass(type);
                data.put(key ,new MapperData( sql , typeClazz));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, MapperData> getData() {
        return data;
    }

}

