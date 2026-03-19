package com.czetsuyatech.nerv.persistence.repository;

import com.czetsuyatech.nerv.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

/**
 * Test repository for {@link UserEntity}.
 */
@Repository
public interface UserRepository extends SliceJpaRepository<UserEntity, Long> {

}
