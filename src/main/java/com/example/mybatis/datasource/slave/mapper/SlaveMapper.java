package com.example.mybatis.datasource.slave.mapper;

import com.example.mybatis.datasource.master.MasterDB;
import com.example.mybatis.datasource.slave.SlaveDB;
import com.example.mybatis.model.TestModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@SlaveDB
public interface SlaveMapper {

    @Update("CREATE TABLE IF NOT EXISTS test(" +
            "  id INT NOT NULL PRIMARY KEY," +
            "  text VARCHAR NOT NULL" +
            ")")
    void createTable();

    @Insert("INSERT INTO test (id, text) VALUES (#{id}, #{text})")
    void insert(TestModel record);

    @Select("SELECT * FROM test")
    @ConstructorArgs({
            @Arg(name = "id", column = "id", id = true),
            @Arg(name = "text", column = "text")
    })
    List<TestModel> selectAll();
}
