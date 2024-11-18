package com.banking.threeom.web.rest;

import static com.banking.threeom.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banking.threeom.IntegrationTest;
import com.banking.threeom.domain.Balance;
import com.banking.threeom.repository.BalanceRepository;
import com.banking.threeom.service.dto.BalanceDTO;
import com.banking.threeom.service.mapper.BalanceMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import com.banking.threeom.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

/**
 * Integration tests for the {@link BalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
class BalanceResourceIT {

    private static final BigDecimal DEFAULT_INITIAL_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_INITIAL_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_BALANCE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBalanceMockMvc;

    private Balance balance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createEntity(EntityManager em) {
        Balance balance = new Balance().initialBalance(DEFAULT_INITIAL_BALANCE).currentBalance(DEFAULT_CURRENT_BALANCE);
        return balance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createUpdatedEntity(EntityManager em) {
        Balance balance = new Balance().initialBalance(UPDATED_INITIAL_BALANCE).currentBalance(UPDATED_CURRENT_BALANCE);
        return balance;
    }

    @BeforeEach
    public void initTest() {
        balance = createEntity(em);
    }

    @Test
    @Transactional
    void createBalance() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();
        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);
        restBalanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate + 1);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getInitialBalance()).isEqualByComparingTo(DEFAULT_INITIAL_BALANCE);
        assertThat(testBalance.getCurrentBalance()).isEqualByComparingTo(DEFAULT_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    void createBalanceWithExistingId() throws Exception {
        // Create the Balance with an existing ID
        balance.setId(1L);
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        assertThrows(NestedServletException.class, () -> restBalanceMockMvc
                .perform(
                        post(ENTITY_API_URL)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
                )
                .andExpect(status().isBadRequest()));

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInitialBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setInitialBalance(null);

        // Create the Balance, which fails.
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        restBalanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setCurrentBalance(null);

        // Create the Balance, which fails.
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        restBalanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBalances() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialBalance").value(hasItem(sameNumber(DEFAULT_INITIAL_BALANCE))))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(sameNumber(DEFAULT_CURRENT_BALANCE))));
    }

    @Test
    @Transactional
    void getBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get the balance
        restBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, balance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(balance.getId().intValue()))
            .andExpect(jsonPath("$.initialBalance").value(sameNumber(DEFAULT_INITIAL_BALANCE)))
            .andExpect(jsonPath("$.currentBalance").value(sameNumber(DEFAULT_CURRENT_BALANCE)));
    }

    @Test
    @Transactional
    void getNonExistingBalance() throws Exception {
        // Get the balance
        restBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance
        Balance updatedBalance = balanceRepository.findById(balance.getId()).get();
        // Disconnect from session so that the updates on updatedBalance are not directly saved in db
        em.detach(updatedBalance);
        updatedBalance.initialBalance(UPDATED_INITIAL_BALANCE).currentBalance(UPDATED_CURRENT_BALANCE);
        BalanceDTO balanceDTO = balanceMapper.toDto(updatedBalance);

        restBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, balanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getInitialBalance()).isEqualByComparingTo(UPDATED_INITIAL_BALANCE);
        assertThat(testBalance.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional

    void putNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        assertThrows(NestedServletException.class, () -> restBalanceMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, balanceDTO.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
                )
                .andExpect(status().isBadRequest()));
        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        assertThrows(NestedServletException.class, () -> restBalanceMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
                )
                .andExpect(status().isBadRequest()));

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBalanceWithPatch() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance using partial update
        Balance partialUpdatedBalance = new Balance();
        partialUpdatedBalance.setId(balance.getId());

        partialUpdatedBalance.initialBalance(UPDATED_INITIAL_BALANCE).currentBalance(UPDATED_CURRENT_BALANCE);

        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBalance))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getInitialBalance()).isEqualByComparingTo(UPDATED_INITIAL_BALANCE);
        assertThat(testBalance.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateBalanceWithPatch() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance using partial update
        Balance partialUpdatedBalance = new Balance();
        partialUpdatedBalance.setId(balance.getId());

        partialUpdatedBalance.initialBalance(UPDATED_INITIAL_BALANCE).currentBalance(UPDATED_CURRENT_BALANCE);

        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBalance))
            )
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getInitialBalance()).isEqualByComparingTo(UPDATED_INITIAL_BALANCE);
        assertThat(testBalance.getCurrentBalance()).isEqualByComparingTo(UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        assertThrows(NestedServletException.class, () -> restBalanceMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, balanceDTO.getId())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
                )
                .andExpect(status().isBadRequest()));

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        assertThrows(NestedServletException.class, () -> restBalanceMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(balanceDTO))
                )
                .andExpect(status().isBadRequest()));

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();
        balance.setId(count.incrementAndGet());

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(balanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        int databaseSizeBeforeDelete = balanceRepository.findAll().size();

        // Delete the balance
        restBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, balance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
