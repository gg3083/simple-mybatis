package cn.gg.run.dao;

import cn.gg.run.domain.User;

import java.util.List;

public interface UserMapper {

    User getById(Integer id);

    List<User> getByName(String userName);

    Integer deleteById(Integer id);
}
