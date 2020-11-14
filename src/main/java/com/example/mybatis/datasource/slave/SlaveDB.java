package com.example.mybatis.datasource.slave;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Transactional(transactionManager = SlaveDBConfig.TX_MANAGER, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public @interface SlaveDB {
}
