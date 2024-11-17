package com.banking.threeom.service.mapper;

import com.banking.threeom.domain.Balance;
import com.banking.threeom.service.dto.BalanceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Balance} and its DTO {@link BalanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface BalanceMapper extends EntityMapper<BalanceDTO, Balance> {}
