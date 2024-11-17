package com.banking.threeom.service.mapper;

import com.banking.threeom.domain.Balance;
import com.banking.threeom.domain.BankAccount;
import com.banking.threeom.service.dto.BalanceDTO;
import com.banking.threeom.service.dto.BankAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankAccount} and its DTO {@link BankAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {
    @Mapping(target = "balance", source = "balance", qualifiedByName = "balanceId")
    BankAccountDTO toDto(BankAccount s);

    @Named("balanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BalanceDTO toDtoBalanceId(Balance balance);
}
