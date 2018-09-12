package cn.gg.mybatis.configuration;

import cn.gg.mybatis.datasource.DataSource;
import cn.gg.mybatis.mapper.MapperRegister;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

import static java.io.File.separatorChar;

/**
 * @author gg
 * @date 2018-09-12 10:08:23
 * @return 读取配置文件
 */
@Data
public class Configuration {

    //配置文件
    private Properties properties = new Properties();
    //mapper文件
    private Properties mapperProperties = new Properties();
    //配置文件路径
    private String propertyUrl;
    //数据源
    private DataSource dataSource;
    //对应的配置文件
    private Class<?> clazzMapper;

    private MapperRegister mapperRegister = new MapperRegister();


    public Configuration( String property ,Class<?> clazz){
        this.propertyUrl = property;
        this.clazzMapper = clazz;
        init();
    }

    private void init(){
        //读取配置文件
        readProperties();
        //读取数据库配置
        readDataSource();
        //注册mapper文件
        registerMapper();
    }

    private void readProperties(){
        if ( propertyUrl == null){
            throw new RuntimeException("加载配置文件失败");
        }
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream( propertyUrl );
            properties.load( in );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataSource(){
        String driver = properties.getProperty( "jdbc.driver" );
        String url = properties.getProperty( "jdbc.url" );
        String userName = properties.getProperty( "jdbc.userName" );
        String passWord = properties.getProperty( "jdbc.passWord" );
        if ( isEmpty( driver ) || isEmpty( url ) || isEmpty( userName ) || isEmpty( passWord )){
            throw new RuntimeException("加载数据库配置文件异常");
        }
        dataSource = new DataSource( driver ,url , userName ,passWord );
    }

    private void registerMapper(){
        String basePackage = properties.getProperty( "mapperPackage" );
        if ( basePackage.isEmpty() ){
            throw new RuntimeException("没有对应的mapper");
        }
        //获取项目绝对路径
        String filePath = null;
        try {
            filePath = new File(getClass().getResource(String.valueOf(separatorChar)).toURI().getPath()).getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //将包由.替换成 /
        String packagePath = basePackage.replace('.', separatorChar);
        //获取mapper文件的绝对路径
        filePath = filePath + separatorChar + packagePath;

        File file = new File(filePath);
        File[] files = file.listFiles();

        HashMap mapperMap = new HashMap();
        HashMap clazzMap = new HashMap();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String fileStr = files[i].toString();
                String fileName = fileStr.substring( fileStr.lastIndexOf(separatorChar)+1,fileStr.length());
                if ( fileStr.substring(fileStr.indexOf(".") + 1, fileStr.length()).equals("properties")) {
                    mapperMap.put( fileName , files[i] );
                }else {
                    clazzMap.put( fileName , files[i] );
                }
            }
        }

        for (Object properties : mapperMap.keySet()) {
            String property = (String) properties;
            String simpleProperty = property.substring(0,property.indexOf("."));
            if ( simpleProperty.equals( clazzMapper.getSimpleName() )){
                try {
                    mapperProperties.load( new FileInputStream( mapperMap.get(property).toString() ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if( mapperProperties.size() == 0 ){
            throw new RuntimeException("加载mapper文件失败");
        }
        mapperRegister.doRegister( mapperProperties );

    }

    private boolean isEmpty(String str) {
        if ("".equals(str)) return true;
        else if ( null == str )return true;
        else if ( str.length() == 0 ) return true;
        else return false;
    }


}
