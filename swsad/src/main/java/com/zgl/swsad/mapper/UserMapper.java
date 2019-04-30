package com.zgl.swsad.mapper;

import com.zgl.swsad.model.Person;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface UserMapper {
    @Select("SELECT * FROM Person WHERE id = #{id}")
    Person selectUser(int id);

    @Select("select * from person order by id")
    ArrayList<Person> selectAllUser();//返回一个person对象

    /**
     * 添加新用户
     * @param Person 实例
     * @return 成功操作的记录数目
     */
    @Insert("INSERT INTO Person (name, age) Values(#{name}, #{age})")
    int insertUser(Person person);

    @Update("UPDATE Person set name=#{name}, age=#{age} where id=#{id}")
    int updateUser(Person person);

    @Delete("DELETE FROM Person where id=#{id}")
    int deleteUser(int id);
}
