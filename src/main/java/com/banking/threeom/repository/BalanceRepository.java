package com.banking.threeom.repository;

import com.banking.threeom.domain.Balance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Balance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {}
