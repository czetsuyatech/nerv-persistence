package com.czetsuyatech.nerv.persistence.config;

import com.czetsuyatech.nerv.persistence.entity.NervEntityConfig;
import com.czetsuyatech.nerv.persistence.repository.NervRepositoryConfig;
import com.czetsuyatech.nerv.persistence.repository.SimpleSliceJpaRepositoryImpl;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Test persistence configuration enabling entity scanning and custom repository base support.
 */
@EnableJpaRepositories(
    repositoryBaseClass = SimpleSliceJpaRepositoryImpl.class,
    basePackageClasses = {NervRepositoryConfig.class})
@EntityScan(basePackageClasses = NervEntityConfig.class)
@EnableTransactionManagement
public class TestPersistenceConfig {

}
