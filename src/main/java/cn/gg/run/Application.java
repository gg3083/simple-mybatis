package cn.gg.run;

import cn.gg.mybatis.configuration.Configuration;
import cn.gg.mybatis.session.Sqlseesion;
import cn.gg.mybatis.session.SqlseesionFactory;
import cn.gg.mybatis.session.SqlseesionFactoryBuilder;
import cn.gg.run.dao.UserMapper;

public class Application {


    public static void main(String[] args) {
        Configuration configuration = new Configuration( "mybatis-config.properties" , UserMapper.class);
        SqlseesionFactory factory = new SqlseesionFactoryBuilder( configuration ).build();
        Sqlseesion session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Integer user = userMapper.deleteById( 95 );
        System.err.println( user );
    }
}
