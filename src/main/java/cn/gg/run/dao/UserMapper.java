package cn.gg.run.dao;

import cn.gg.run.domain.User;

public interface UserMapper {

    User getById(Integer id);

    User getByName(String userName);
}
