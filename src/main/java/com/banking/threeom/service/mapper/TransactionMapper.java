package com.banking.threeom.service.mapper;

import com.banking.threeom.domain.BankAccount;
import com.banking.threeom.domain.Transaction;
import com.banking.threeom.service.dto.BankAccountDTO;
import com.banking.threeom.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "sourceBankAccount", source = "sourceBankAccount", qualifiedByName = "bankAccountId")
    @Mapping(target = "destinationBankAccount", source = "destinationBankAccount", qualifiedByName = "bankAccountId")
    TransactionDTO toDto(Transaction s);

    @Named("bankAccountId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankAccountDTO toDtoBankAccountId(BankAccount bankAccount);
}
