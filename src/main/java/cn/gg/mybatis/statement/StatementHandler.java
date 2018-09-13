package cn.gg.mybatis.statement;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.datasource.DataSource;
import cn.gg.mybatis.mapper.MapperData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author GG
 * @date 2018年09月12日14:20:53
 * @return 封装jdbc
 */
public class StatementHandler {

    private Configuration configuration;

    public StatementHandler( Configuration configuration){
        this.configuration = configuration;
    }

    public <E> List<E> query(MapperData mapperData, Object args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement( mapperData.getSql() );
            setParameter( ps ,args );
            ResultSet rs = ps.executeQuery();
            return (List<E>) setResult( rs , mapperData.getType());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer update(MapperData mapperData, Object args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement( mapperData.getSql() );
            setParameter( ps ,args );
            return ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取jdbc连接
     * @return
     */
    private Connection getConnection(){
        DataSource dataSource = configuration.getDataSource();
        Connection conn = null;
        try {
            Class.forName(dataSource.getDriver());
            conn = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUserName(), dataSource.getPassWord());
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 设置参数
     * @param ps
     * @param parameter
     */
    private void setParameter( PreparedStatement ps ,Object parameter ){
        Object[] args = (Object[])parameter;
        try {
            for (int i = 0; i < args.length; i++) {

                if( args[i] instanceof Integer ) {
                    ps.setInt(i + 1, (Integer)args[i]);
                }else if( args[i] instanceof Double ){
                    ps.setDouble( i+1 ,(Double)args[i]);
                }else if( args[i] instanceof java.sql.Date){
                    ps.setDate( i+1 , (java.sql.Date) args[i]);
                }else if( args[i] instanceof String){
                    ps.setString( i+1 , (String)args[i]);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将查询值封装到对象中
     * @param rs
     * @param type
     * @param <E>
     * @return
     */
    private <E> List<E> setResult(ResultSet rs , Class type){
        Object obj = null;
        List<Object> list = new LinkedList<>();
        try {
            //获取结果集的元素个数
            int count = rs.getMetaData().getColumnCount();

            //业务对象的属性数组
            Field[] fields = type.getDeclaredFields();

            while (rs.next()) {
                obj = type.newInstance();//构造业务对象实体
                for(int i = 1;i<=count;i++){
                    for(int j=0;j<fields.length;j++){
                        Field f = fields[j];
                        String cloumn = rs.getMetaData().getColumnName(i);
                        if(f.getName().equalsIgnoreCase(String.valueOf(camel(cloumn)))){
                            String methodName = "set" + bigFirst( f.getName() ); // set方法的名字
                            Method m = type.getMethod( methodName ,f.getType());
                            Class<?> argsType = f.getType();//m.getParameterTypes()[0].getName() ;//
                            if ( Integer.class == argsType) {
                                m.invoke(obj, rs.getInt( cloumn ));
                            } else if ( String.class == argsType) {
                                m.invoke(obj, rs.getString( cloumn ));
                            } else if ( Double.class == argsType ) {
                                m.invoke(obj, rs.getDouble( cloumn ));
                            } else if ( Date.class == argsType ) {
                                m.invoke(obj, rs.getDate( cloumn ));
                            } else {
                                m.invoke(obj, rs.getObject( cloumn ));
                            }
                        }
                    }
                }
                list.add( obj );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (List<E>) list;
    }

    /**
     *
     * @param str
     * @return 下划线转驼峰
     */
    public StringBuffer camel(String str) {
        //利用正则删除下划线，把下划线后一位改成大写
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            matcher.appendTail(sb);
        }else {
            return sb;
        }
        return camel(sb.toString());
    }

    /**
     *
     * @param str
     * @return 首字母大写
     */
    public String bigFirst(String str){
        String value = camel( str ).toString();
        String first = value.substring(0, 1);
        String tail = value.substring(1);
        return first.toUpperCase() + tail;
    }

}
