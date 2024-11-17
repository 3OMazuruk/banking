package com.banking.threeom.service;

import com.banking.threeom.domain.Balance;
import com.banking.threeom.repository.BalanceRepository;
import com.banking.threeom.service.dto.BalanceDTO;
import com.banking.threeom.service.mapper.BalanceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Balance}.
 */
@Service
@Transactional
public class BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceService.class);

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    public BalanceService(BalanceRepository balanceRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save.
     * @return the persisted entity.
     */
    public BalanceDTO save(BalanceDTO balanceDTO) {
        log.debug("Request to save Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     * Update a balance.
     *
     * @param balanceDTO the entity to save.
     * @return the persisted entity.
     */
    public BalanceDTO update(BalanceDTO balanceDTO) {
        log.debug("Request to update Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     * Partially update a balance.
     *
     * @param balanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BalanceDTO> partialUpdate(BalanceDTO balanceDTO) {
        log.debug("Request to partially update Balance : {}", balanceDTO);

        return balanceRepository
            .findById(balanceDTO.getId())
            .map(existingBalance -> {
                balanceMapper.partialUpdate(existingBalance, balanceDTO);

                return existingBalance;
            })
            .map(balanceRepository::save)
            .map(balanceMapper::toDto);
    }

    /**
     * Get all the balances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll(pageable).map(balanceMapper::toDto);
    }

    /**
     *  Get all the balances where BankAccount is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BalanceDTO> findAllWhereBankAccountIsNull() {
        log.debug("Request to get all balances where BankAccount is null");
        return StreamSupport
            .stream(balanceRepository.findAll().spliterator(), false)
            .filter(balance -> balance.getBankAccount() == null)
            .map(balanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one balance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BalanceDTO> findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        return balanceRepository.findById(id).map(balanceMapper::toDto);
    }

    /**
     * Delete the balance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
    }
}
