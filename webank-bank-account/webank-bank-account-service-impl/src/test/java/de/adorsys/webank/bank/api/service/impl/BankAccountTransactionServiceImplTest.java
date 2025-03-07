/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import de.adorsys.webank.bank.api.domain.*;//NOPMD
import de.adorsys.webank.bank.api.service.CurrencyExchangeRatesService;
import de.adorsys.webank.bank.api.service.BankAccountConfigService;
import de.adorsys.webank.bank.api.service.BankAccountService;
import de.adorsys.webank.bank.api.service.mappers.BankAccountMapper;
import de.adorsys.webank.bank.api.service.mappers.PaymentMapper;
import de.adorsys.webank.bank.api.service.mappers.PostingMapper;
import de.adorsys.webank.bank.api.service.mappers.SerializeService;
import de.adorsys.webank.bank.db.domain.AccountType;
import de.adorsys.webank.bank.db.domain.AccountUsage;
import de.adorsys.webank.bank.db.domain.BankAccount;
import de.adorsys.ledgers.postings.api.domain.*;//NOPMD
import de.adorsys.ledgers.postings.api.service.LedgerService;
import de.adorsys.ledgers.postings.api.service.PostingService;
import de.adorsys.ledgers.util.exception.DepositModuleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;//NOPMD
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.adorsys.webank.bank.api.domain.PaymentTypeBO.BULK;
import static de.adorsys.webank.bank.api.domain.PaymentTypeBO.SINGLE;
import static de.adorsys.webank.bank.api.domain.TransactionStatusBO.ACSP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;//NOPMD

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD.TooManyMethods", "CPD-START"})
class BankAccountTransactionServiceImplTest {
    private static final String ACCOUNT_ID = "ACCOUNT_ID";
    private static final LocalDateTime REQUEST_TIME = LocalDateTime.now();
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency CHF = Currency.getInstance("CHF");
    private static final Currency GBP = Currency.getInstance("GBP");
    private static final String IBAN = "DE1234567890";
    private static int PMT_ID = 0; //NOPMD

    @InjectMocks
    private BankAccountTransactionServiceImpl transactionService;

    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private BankAccountConfigService bankAccountConfigService;
    @Mock
    private LedgerService ledgerService;
    @Mock
    private PostingService postingService;
    @Mock
    private SerializeService serializeService;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private ObjectMapper objectMapper;

    private final PaymentMapper localPaymentMapper = Mappers.getMapper(PaymentMapper.class);
    @Mock
    private CurrencyExchangeRatesService exchangeRatesService;
    @Mock
    private PostingMapper postingMapper = Mappers.getMapper(PostingMapper.class);
    private final PostingMapper localPostingMapper = Mappers.getMapper(PostingMapper.class);

    private static final ObjectMapper STATIC_MAPPER;

    private final BankAccountMapper bankAccountMapper = Mappers.getMapper(BankAccountMapper.class);

    static {
        STATIC_MAPPER = new ObjectMapper()
                                .findAndRegisterModules()
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                                .registerModule(new Jdk8Module())
                                .registerModule(new JavaTimeModule())
                                .registerModule(new ParameterNamesModule());
    }

    @Test
    void depositCash_accountNotFound() {
        // Given
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean())).thenThrow(DepositModuleException.class);
        AmountBO amountBO = new AmountBO(EUR, BigDecimal.TEN);
        // Then
        assertThrows(DepositModuleException.class, () -> transactionService.depositCash(ACCOUNT_ID, amountBO, "recordUser"));
    }

    @Test
    void depositCash_amountLessThanZero() {
        AmountBO amountBO = new AmountBO(EUR, BigDecimal.valueOf(-123L));
        // Then
        assertThrows(DepositModuleException.class, () -> transactionService.depositCash(ACCOUNT_ID, amountBO, "recordUser"));
    }

    @Test
    void depositCash_blockedAccount() {
        // Given
        BankAccountDetailsBO bankAccountDetailsBO = getBankAccountBO();
        BankAccountBO blockedAccount = new BankAccountBO();
        blockedAccount.setBlocked(true);
        bankAccountDetailsBO.setAccount(blockedAccount);
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean())).thenReturn(bankAccountDetailsBO);
        AmountBO amountBO = new AmountBO(EUR, BigDecimal.valueOf(123L));
        // Then
        assertThrows(DepositModuleException.class, () -> transactionService.depositCash(ACCOUNT_ID, amountBO, "recordUser"));
    }

    @Test
    void depositCash_differentCurrencies() {
        // Given
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean())).thenReturn(getBankAccountBO());
        AmountBO amountBO = new AmountBO(USD, BigDecimal.TEN);
        // Then
        assertThrows(DepositModuleException.class, () -> transactionService.depositCash(ACCOUNT_ID, amountBO, "recordUser"));
    }

    @Test
    void depositCash_OK() throws IOException {
        // Given
        when(bankAccountService.getAccountDetailsById(anyString(), any(), anyBoolean())).thenReturn(getBankAccountBO());
        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(any())).thenReturn(Optional.of(getLedger()));
        when(bankAccountConfigService.getCashAccount()).thenReturn("id");
        when(ledgerService.findLedgerAccount(any(), anyString())).thenReturn(new LedgerAccountBO());
        when(ledgerService.findLedgerAccountById(any())).thenReturn(new LedgerAccountBO());
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));
        when(paymentMapper.toDepositTransactionDetails(any(), any(), any(), any(), anyString(), any(), any())).thenAnswer(i -> localPaymentMapper.toDepositTransactionDetails(i.getArgument(0), i.getArgument(1), i.getArgument(2), (LocalDate) i.getArgument(3), (String) i.getArgument(4), i.getArgument(5), i.getArgument(6)));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        AmountBO amount = new AmountBO(EUR, BigDecimal.TEN);

        // When
        transactionService.depositCash(ACCOUNT_ID, amount, "recordUser");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());
        PostingBO posting = postingCaptor.getValue();
        assertThat(posting.getLines()).hasSize(2);
        for (PostingLineBO line : posting.getLines()) {
            assertThat(line.getId()).isNotBlank();
            TransactionDetailsBO transactionDetails = STATIC_MAPPER.readValue(line.getDetails(), TransactionDetailsBO.class);
            assertThat(transactionDetails.getEndToEndId()).isEqualTo(line.getId());
            assertThat(transactionDetails.getTransactionId()).isNotBlank();
            assertThat(transactionDetails.getBookingDate()).isEqualTo(transactionDetails.getValueDate());
            assertThat(transactionDetails.getCreditorAccount()).usingRecursiveComparison().isEqualTo(bankAccountMapper.toAccountReferenceBO(getBankAccount()));
            assertThat(transactionDetails.getTransactionAmount()).usingRecursiveComparison().isEqualTo(amount);
        }
    }

    @Test
    void bookPayment_single_same_currency() throws IOException {
        // Given
        PaymentBO payment = getPayment(SINGLE, EUR, EUR, EUR, null, false);

        when(paymentMapper.toPaymentOrder(any())).thenAnswer(i -> localPaymentMapper.toPaymentOrder((PaymentBO) i.getArguments()[0]));
        when(paymentMapper.toPaymentTargetDetails(anyString(), any(), any(), any(), any())).thenAnswer(i -> localPaymentMapper.toPaymentTargetDetails((String) i.getArguments()[0], (PaymentTargetBO) i.getArguments()[1], (LocalDate) i.getArguments()[2], (List<ExchangeRateBO>) i.getArguments()[3], i.getArgument(4)));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, EUR, EUR));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(bankAccountConfigService.getClearingAccount(any())).thenReturn("clearing");
        when(ledgerService.findLedgerAccount(any(), anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(2);
        assertThat(dcAmountOk(lines)).isTrue();

        PostingLineBO line1 = lines.get(0);
        assertThat(line1).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.TEN, BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line1.getDetails()).isNotBlank();
        assertThat(STATIC_MAPPER.readValue(line1.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line1.getId(), null));

        PostingLineBO line2 = lines.get(1);
        assertThat(line2).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.ZERO, BigDecimal.TEN, null, null, null, payment.getPaymentId()), "id", "details");
        assertThat(line2.getDetails()).isNotBlank();
        assertThat(STATIC_MAPPER.readValue(line2.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line2.getId(), null));
    }

    @Test
    void bookPayment_single_two_different_currency() throws IOException {
        // Given
        PaymentBO payment = getPayment(SINGLE, EUR, USD, USD, null, false);

        when(paymentMapper.toPaymentOrder(any())).thenAnswer(i -> localPaymentMapper.toPaymentOrder((PaymentBO) i.getArguments()[0]));
        when(paymentMapper.toPaymentTargetDetails(anyString(), any(), any(), any(), any())).thenAnswer(i -> localPaymentMapper.toPaymentTargetDetails((String) i.getArguments()[0], (PaymentTargetBO) i.getArguments()[1], (LocalDate) i.getArguments()[2], (List<ExchangeRateBO>) i.getArguments()[3], i.getArgument(4)));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, USD, USD));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(ledgerService.findLedgerAccountById(anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(4);
        assertThat(dcAmountOk(lines)).isTrue();

        PostingLineBO line1 = lines.get(0);
        assertThat(line1).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.valueOf(8.3333), BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line1.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates1 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(0).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line1.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line1.getId(), exchangeRates1));

        PostingLineBO line2 = lines.get(1);
        assertThat(line2).isEqualToIgnoringGivenFields(expectedLine(null, null, BigDecimal.ZERO, BigDecimal.valueOf(8.3333), null, null, null, posting.getOprSrc()), "id", "details");
        assertThat(line2.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates2 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(1).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line2.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line2.getId(), exchangeRates2));

        PostingLineBO line3 = lines.get(2);
        assertThat(line3).isEqualToIgnoringGivenFields(expectedLine(null, null, BigDecimal.TEN, BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line3.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates3 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(2).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line3.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line3.getId(), exchangeRates3));

        PostingLineBO line4 = lines.get(3);
        assertThat(line4).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.ZERO, BigDecimal.TEN, null, null, null, posting.getOprSrc()), "id", "details");
        assertThat(line4.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates4 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(3).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line4.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line4.getId(), exchangeRates4));
    }

    @Test
    void bookPayment_single_two_different_currency_accountNotFound() {
        // Given
        PaymentBO payment = getPayment(SINGLE, EUR, USD, USD, null, false);

        when(paymentMapper.toPaymentOrder(any())).thenAnswer(i -> localPaymentMapper.toPaymentOrder((PaymentBO) i.getArguments()[0]));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, USD, USD));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));

        // Then
        assertThrows(DepositModuleException.class, () -> transactionService.bookPayment(payment, REQUEST_TIME, "TEST"));
    }

    @Test
    void bookPayment_single_three_different_currency() throws IOException {
        // Given
        PaymentBO payment = getPayment(SINGLE, EUR, USD, CHF, null, false);

        when(paymentMapper.toPaymentOrder(any())).thenAnswer(i -> localPaymentMapper.toPaymentOrder((PaymentBO) i.getArguments()[0]));
        when(paymentMapper.toPaymentTargetDetails(anyString(), any(), any(), any(), any())).thenAnswer(i -> localPaymentMapper.toPaymentTargetDetails((String) i.getArguments()[0], (PaymentTargetBO) i.getArguments()[1], (LocalDate) i.getArguments()[2], (List<ExchangeRateBO>) i.getArguments()[3], i.getArgument(4)));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, USD, CHF));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(ledgerService.findLedgerAccountById(anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(4);
        assertThat(dcAmountOk(lines)).isTrue();

        PostingLineBO line1 = lines.get(0);
        assertThat(line1).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.valueOf(8.3333), BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line1.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates1 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(0).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line1.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line1.getId(), exchangeRates1));

        PostingLineBO line2 = lines.get(1);
        assertThat(line2).isEqualToIgnoringGivenFields(expectedLine(null, null, BigDecimal.ZERO, BigDecimal.valueOf(8.3333), null, null, null, posting.getOprSrc()), "id", "details");
        assertThat(line2.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates2 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(1).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line2.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line2.getId(), exchangeRates2));

        PostingLineBO line3 = lines.get(2);
        assertThat(line3).isEqualToIgnoringGivenFields(expectedLine(null, null, BigDecimal.valueOf(4.16665), BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line3.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates3 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(2).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line3.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line3.getId(), exchangeRates3));

        PostingLineBO line4 = lines.get(3);
        assertThat(line4).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.ZERO, BigDecimal.valueOf(4.16665), null, null, null, posting.getOprSrc()), "id", "details");
        assertThat(line4.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates4 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(3).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line4.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line4.getId(), exchangeRates4));
    }

    @Test
    void bookPayment_bulk_same_currency_batchBookingPreferredTrue() throws IOException {
        // Given
        PaymentBO payment = getPayment(BULK, EUR, EUR, EUR, null, true);


        ReflectionTestUtils.setField(transactionService, "paymentMapper", Mappers.getMapper(PaymentMapper.class));


        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, EUR, EUR));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(ledgerService.findLedgerAccountById(anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(2);
        assertThat(dcAmountOk(lines)).isTrue();

        PostingLineBO line1 = lines.get(0);
        assertThat(line1).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.TEN, BigDecimal.ZERO, null, null, null, posting.getOprSrc()), "id", "details");
        assertThat(line1.getDetails()).isNotBlank();
        List<ExchangeRateBO> exchangeRates1 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(lines.get(0).getDetails(), TransactionDetailsBO.class).getExchangeRate();
        assertThat(STATIC_MAPPER.readValue(line1.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line1.getId(), exchangeRates1), "creditorAgent", "creditorName", "creditorAccount", "remittanceInformationUnstructured");

        PostingLineBO line2 = lines.get(1);
        assertThat(line2).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.ZERO, BigDecimal.TEN, null, null, null, payment.getPaymentId()), "id", "details");
        assertThat(line2.getDetails()).isNotBlank();
        assertThat(STATIC_MAPPER.readValue(line2.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line2.getId(), null));
    }

    @Test
    void bookPayment_bulk_same_currency_batchBookingPreferredFalse() throws IOException {
        // Given
        PaymentBO payment = getPayment(BULK, EUR, EUR, EUR, null, false);

        when(paymentMapper.toPaymentOrder(any())).thenAnswer(i -> localPaymentMapper.toPaymentOrder((PaymentBO) i.getArguments()[0]));
        when(paymentMapper.toPaymentTargetDetails(anyString(), any(), any(), any(), any())).thenAnswer(i -> localPaymentMapper.toPaymentTargetDetails((String) i.getArguments()[0], (PaymentTargetBO) i.getArguments()[1], (LocalDate) i.getArguments()[2], (List<ExchangeRateBO>) i.getArguments()[3], i.getArgument(4)));
        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, EUR, EUR));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(ledgerService.findLedgerAccountById(anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(2);
        assertThat(dcAmountOk(lines)).isTrue();

        PostingLineBO line1 = lines.get(0);
        assertThat(line1).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.TEN, BigDecimal.ZERO, null, null, null, posting.getOprId()), "id", "details");
        assertThat(line1.getDetails()).isNotBlank();
        assertThat(STATIC_MAPPER.readValue(line1.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line1.getId(), null));

        PostingLineBO line2 = lines.get(1);
        assertThat(line2).isEqualToIgnoringGivenFields(expectedLine(null, new LedgerAccountBO(null, new LedgerBO()), BigDecimal.ZERO, BigDecimal.TEN, null, null, null, payment.getPaymentId()), "id", "details");
        assertThat(line2.getDetails()).isNotBlank();
        assertThat(STATIC_MAPPER.readValue(line2.getDetails(), PaymentTargetDetailsBO.class)).isEqualToIgnoringGivenFields(getExpectedDetails(payment, line2.getId(), null));
    }

    @Test
    void bookPayment_bulk_two_different_currency() {
        // Given
        PaymentBO payment = getPayment(BULK, EUR, USD, USD, CHF, true);

        ReflectionTestUtils.setField(transactionService, "paymentMapper", Mappers.getMapper(PaymentMapper.class));

        when(postingMapper.buildPosting(any(), anyString(), anyString(), any(), anyString())).thenAnswer(i -> localPostingMapper.buildPosting((LocalDateTime) i.getArguments()[0], (String) i.getArguments()[1], (String) i.getArguments()[2], (LedgerBO) i.getArguments()[3], (String) i.getArguments()[4]));
        when(postingMapper.buildPostingLine(anyString(), any(), any(), any(), anyString(), anyString())).thenAnswer(i -> localPostingMapper.buildPostingLine((String) i.getArguments()[0], (LedgerAccountBO) i.getArguments()[1], (BigDecimal) i.getArguments()[2], (BigDecimal) i.getArguments()[3], (String) i.getArguments()[4], (String) i.getArguments()[5]));
        when(serializeService.serializeOprDetails(any())).thenAnswer(i -> STATIC_MAPPER.writeValueAsString(i.getArguments()[0]));

        when(bankAccountConfigService.getLedger()).thenReturn("mockbank");
        when(ledgerService.findLedgerByName(anyString())).thenReturn(Optional.of(new LedgerBO("mockbank", "id", null, null, null, null, null)));

        when(exchangeRatesService.getExchangeRates(any(), any(), any())).thenReturn(getRates(EUR, USD, USD));
        when(exchangeRatesService.applyRate(any(), any())).thenAnswer(i -> new CurrencyExchangeRatesServiceImpl(null, null).applyRate(i.getArgument(0), i.getArgument(1)));

        when(ledgerService.findLedgerAccountById(anyString())).thenReturn(new LedgerAccountBO("user account", new LedgerBO()));
        when(bankAccountService.getOptionalAccountByIbanAndCurrency(any(), any())).thenReturn(Optional.of(getBankAccountBO().getAccount()));
        when(bankAccountConfigService.getClearingAccount(anyString())).thenReturn("some");
        when(ledgerService.findLedgerAccount(any(), anyString())).thenReturn(new LedgerAccountBO("clearing", new LedgerBO()));

        // When
        transactionService.bookPayment(payment, REQUEST_TIME, "TEST");

        // Then
        ArgumentCaptor<PostingBO> postingCaptor = ArgumentCaptor.forClass(PostingBO.class);
        verify(postingService, times(1)).newPosting(postingCaptor.capture());

        PostingBO posting = postingCaptor.getValue();
        List<PostingLineBO> lines = posting.getLines();
        assertThat(lines).hasSize(7);
        List<PostingLineBO> creditLines = lines.stream().filter(l -> l.getCreditAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        List<PostingLineBO> debitLines = lines.stream().filter(l -> l.getDebitAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        assertThat(creditLines).hasSize(4);
        assertThat(debitLines).hasSize(3);
        assertEquals(debitLines.stream().map(PostingLineBO::getDebitAmount).reduce(BigDecimal::add),
                     creditLines.stream().map(PostingLineBO::getCreditAmount).reduce(BigDecimal::add));
    }

    private PaymentTargetDetailsBO getExpectedDetails(PaymentBO payment, String trId, List<ExchangeRateBO> rates) {
        PaymentTargetDetailsBO details = new PaymentTargetDetailsBO();
        details.setTransactionStatus(ACSP);
        details.setTransactionId(trId);
        details.setEndToEndId(payment.getTargets().get(0).getEndToEndIdentification());
        details.setPaymentProduct("sepa-credit-transfers");
        details.setBookingDate(REQUEST_TIME.toLocalDate());
        details.setValueDate(REQUEST_TIME.toLocalDate());
        details.setTransactionAmount(payment.getTargets().get(0).getInstructedAmount());
        details.setExchangeRate(rates);
        details.setCreditorName(payment.getTargets().get(0).getCreditorName());
        details.setCreditorAccount(payment.getTargets().get(0).getCreditorAccount());
        details.setCreditorAddress(payment.getTargets().get(0).getCreditorAddress());
        details.setDebtorName(null);
        details.setDebtorAccount(payment.getDebtorAccount());
        details.setPaymentOrderId(payment.getPaymentId());
        details.setPaymentType(payment.getPaymentType());
        details.setBankTransactionCode("PMNT-ICDT-STDO");
        details.setProprietaryBankTransactionCode("PMNT-ICDT-STDO");
        return details;
    }

    private PostingLineBO expectedLine(String id, LedgerAccountBO account, BigDecimal debit, BigDecimal credit, String details, String oprId, String oprSrcId, String subOprSrcId) {
        PostingLineBO l = new PostingLineBO();
        l.setId(id);
        l.setAccount(account);
        l.setDebitAmount(debit);
        l.setCreditAmount(credit);
        l.setDetails(details);
        l.setOprId(oprId);
        l.setOprSrc(oprSrcId);
        l.setPstStatus(PostingStatusBO.POSTED);
        l.setPstTime(null);
        l.setPstType(null);
        l.setSubOprSrcId(subOprSrcId);
        return l;
    }

    private boolean dcAmountOk(List<PostingLineBO> lines) {
        Optional<BigDecimal> credit = lines.stream().map(PostingLineBO::getCreditAmount).reduce(BigDecimal::add);
        Optional<BigDecimal> debit = lines.stream().map(PostingLineBO::getDebitAmount).reduce(BigDecimal::add);
        return credit.equals(debit);
    }

    private List<ExchangeRateBO> getRates(Currency debt, Currency amount, Currency cred) {
        if (debt.equals(amount) && amount.equals(cred)) {
            return Collections.emptyList();
        }
        List<ExchangeRateBO> ratesToReturn = new ArrayList<>();
        updateRatesList(amount, debt, ratesToReturn);
        updateRatesList(amount, cred, ratesToReturn);
        return ratesToReturn;
    }

    private void updateRatesList(Currency from, Currency to, List<ExchangeRateBO> ratesToReturn) {
        if (!from.equals(to)) {
            String rateFrom = resolveRate(from);
            String rateTo = resolveRate(to);
            if (!rateFrom.equals(rateTo)) {
                ratesToReturn.add(new ExchangeRateBO(from, rateFrom, to, rateTo, LocalDate.now(), "International Currency Exchange Market"));
            }
        }
    }

    private String resolveRate(Currency currency) {
        HashMap<Currency, String> map = new HashMap<>();
        map.put(EUR, "1");
        map.put(USD, "1.2");
        map.put(CHF, "0.5");
        map.put(GBP, "0.8");
        return map.get(currency);
    }

    private PaymentBO getPayment(PaymentTypeBO type, Currency debtor, Currency amount, Currency creditor, Currency creditor2, boolean batchBookingPreferred) {
        return new PaymentBO("pmt1", batchBookingPreferred, null,
                             null, type, "sepa-credit-transfers", null, null, null, null,
                             null, getReference(debtor), null, null, ACSP, getTargets(amount, creditor, creditor2), getBankAccount().getId());
    }

    private List<PaymentTargetBO> getTargets(Currency amount, Currency curr1, Currency curr2) {
        return Stream.of(curr1, curr2)
                       .filter(Objects::nonNull)
                       .map(this::getReference)
                       .map(r -> new PaymentTargetBO(nextTargetId(), "END-TO-END", getAmount(amount), r, null, "name", null, null, null, null, null, null))
                       .collect(Collectors.toList());
    }

    private AmountBO getAmount(Currency currency) {
        return new AmountBO(currency, BigDecimal.TEN);
    }

    private AccountReferenceBO getReference(Currency currency) {
        AccountReferenceBO reference = new AccountReferenceBO();
        reference.setIban(IBAN);
        reference.setCurrency(currency);
        return reference;
    }

    private String nextTargetId() {
        PMT_ID++;
        return "target" + PMT_ID;
    }

    private static LedgerBO getLedger() {
        LedgerBO ledgerBO = new LedgerBO();
        ledgerBO.setName("ledger");
        return ledgerBO;
    }

    private BankAccount getBankAccount() {
        return new BankAccount("id", IBAN, "msisdn", "EUR",
                                  "name", "displayName", "product", null, AccountType.CASH, "bic", null,
                                  AccountUsage.PRIV, "details", false, false, LocalDateTime.now(), BigDecimal.ZERO);
    }

    private BankAccountDetailsBO getBankAccountBO() {
        return new BankAccountDetailsBO(
                new BankAccountBO("id", IBAN, null, null, null, "msisdn", EUR, "name", "displayName", "product", AccountTypeBO.CASH, "bic", "linkedAccounts", AccountUsageBO.PRIV, "details", false, false, "branch", null, BigDecimal.ZERO),
                Collections.emptyList());
    }
}
