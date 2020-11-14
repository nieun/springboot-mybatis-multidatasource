package com.example.mybatis;

import com.example.mybatis.datasource.master.mapper.MasterMapper;
import com.example.mybatis.datasource.slave.mapper.SlaveMapper;
import com.example.mybatis.model.TestModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultiDataSourceApplicationTests {

	@Autowired
	MasterMapper masterMapper;

	@Autowired
	SlaveMapper slaveMapper;

	@Test
	void test() {
		masterMapper.createTable();
		slaveMapper.createTable();

		TestModel testModel;

		testModel = new TestModel(1, "Both");
		masterMapper.insert(testModel);
		slaveMapper.insert(testModel);
		Assertions.assertEquals(1, masterMapper.selectAll().size());
		Assertions.assertEquals(1, slaveMapper.selectAll().size());

		testModel = new TestModel(2, "Exclusive");
		masterMapper.insert(testModel);

		System.out.println("Master" + masterMapper.selectAll());
		System.out.println("Slave" + slaveMapper.selectAll());

	}

}
