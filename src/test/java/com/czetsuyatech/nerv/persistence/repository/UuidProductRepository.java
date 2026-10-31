package com.czetsuyatech.nerv.persistence.repository;

import com.czetsuyatech.nerv.persistence.entity.UuidProductEntity;
import java.util.UUID;

public interface UuidProductRepository extends SliceJpaRepository<UuidProductEntity, UUID> {
}
