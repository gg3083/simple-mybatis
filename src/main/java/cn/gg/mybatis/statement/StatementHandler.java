package cn.gg.mybatis.statement;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.datasource.DataSource;
import cn.gg.mybatis.mapper.MapperData;
import cn.gg.run.domain.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
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

    public <E> E query(MapperData mapperData, Object args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement( mapperData.getSql() );
            setParameter( ps ,args );
            ResultSet rs = ps.executeQuery();
            return (E) setResult( rs , mapperData.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

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

    private <E> E setResult(ResultSet rs ,Class type){
        Object obj = null;
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
                            String methodName = "set" + test( f.getName() ); // set方法的名字
                            Method m =type.getMethod( methodName ,f.getType());
                            Object argsType = f.getType().getName();//m.getParameterTypes()[0].getName() ;//
                            if (argsType.equals("java.lang.Integer")) {
                                m.invoke(obj, rs.getInt( cloumn ));
                            } else if (argsType.equals("java.lang.String")) {
                                m.invoke(obj, rs.getString( cloumn ));
                            } else if (argsType.equals("java.lang.Double")) {
                                m.invoke(obj, rs.getDouble( cloumn ));
                            } else if (argsType.equals("java.sql.Date")) {
                                m.invoke(obj, rs.getDate( cloumn ));
                            } else {
                                m.invoke(obj, rs.getObject( cloumn ));
                            }
                        }
                    }
                }
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
        return (E)obj;
    }

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static StringBuffer camel(String str) {
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

    public static String test(String str){
        String value = camel( str ).toString();
        String first = value.substring(0, 1);
        String tail = value.substring(1);
        return first.toUpperCase() + tail;
    }


    public static void main(String[] args) {

        String a =  new String("a");
        System.err.println(a.getClass().getTypeName());
        if ( a instanceof String){
            System.err.println("INt");
        }

    }
}
