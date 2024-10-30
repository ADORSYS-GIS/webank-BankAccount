package de.adorsys.ledgers.bank.api.service.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.ledgers.bank.api.domain.AccountReferenceBO;
import de.adorsys.ledgers.bank.api.domain.AddressBO;
import de.adorsys.ledgers.bank.api.domain.AmountBO;
import de.adorsys.ledgers.bank.api.domain.BalanceBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.api.domain.ChargeBearerBO;
import de.adorsys.ledgers.bank.api.domain.ExchangeRateBO;
import de.adorsys.ledgers.bank.api.domain.FrequencyCodeBO;
import de.adorsys.ledgers.bank.api.domain.PaymentBO;
import de.adorsys.ledgers.bank.api.domain.PaymentOrderDetailsBO;
import de.adorsys.ledgers.bank.api.domain.PaymentTargetBO;
import de.adorsys.ledgers.bank.api.domain.PaymentTargetDetailsBO;
import de.adorsys.ledgers.bank.api.domain.PaymentTypeBO;
import de.adorsys.ledgers.bank.api.domain.PurposeCodeBO;
import de.adorsys.ledgers.bank.api.domain.TransactionDetailsBO;
import de.adorsys.ledgers.bank.api.domain.TransactionStatusBO;
import de.adorsys.ledgers.bank.db.domain.AccountReference;
import de.adorsys.ledgers.bank.db.domain.Address;
import de.adorsys.ledgers.bank.db.domain.Amount;
import de.adorsys.ledgers.bank.db.domain.ChargeBearer;
import de.adorsys.ledgers.bank.db.domain.FrequencyCode;
import de.adorsys.ledgers.bank.db.domain.Payment;
import de.adorsys.ledgers.bank.db.domain.PaymentTarget;
import de.adorsys.ledgers.bank.db.domain.PaymentType;
import de.adorsys.ledgers.bank.db.domain.PurposeCode;
import de.adorsys.ledgers.bank.db.domain.TransactionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-30T08:25:26+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Ubuntu)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toPayment(PaymentBO payment) {
        if ( payment == null ) {
            return null;
        }

        Payment payment1 = new Payment();

        payment1.setPaymentId( payment.getPaymentId() );
        payment1.setBatchBookingPreferred( payment.getBatchBookingPreferred() );
        payment1.setRequestedExecutionDate( payment.getRequestedExecutionDate() );
        payment1.setRequestedExecutionTime( payment.getRequestedExecutionTime() );
        payment1.setPaymentType( paymentTypeBOToPaymentType( payment.getPaymentType() ) );
        payment1.setPaymentProduct( payment.getPaymentProduct() );
        payment1.setStartDate( payment.getStartDate() );
        payment1.setEndDate( payment.getEndDate() );
        payment1.setExecutionRule( payment.getExecutionRule() );
        payment1.setFrequency( frequencyCodeBOToFrequencyCode( payment.getFrequency() ) );
        payment1.setDayOfExecution( payment.getDayOfExecution() );
        payment1.setDebtorAccount( accountReferenceBOToAccountReference( payment.getDebtorAccount() ) );
        payment1.setDebtorName( payment.getDebtorName() );
        payment1.setDebtorAgent( payment.getDebtorAgent() );
        payment1.setTransactionStatus( toTransactionStatus( payment.getTransactionStatus() ) );
        payment1.setTargets( paymentTargetBOListToPaymentTargetList( payment.getTargets() ) );
        payment1.setAccountId( payment.getAccountId() );

        return payment1;
    }

    @Override
    public PaymentBO toPaymentBO(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentBO paymentBO = new PaymentBO();

        paymentBO.setPaymentId( payment.getPaymentId() );
        paymentBO.setBatchBookingPreferred( payment.getBatchBookingPreferred() );
        paymentBO.setRequestedExecutionDate( payment.getRequestedExecutionDate() );
        paymentBO.setRequestedExecutionTime( payment.getRequestedExecutionTime() );
        paymentBO.setPaymentType( paymentTypeToPaymentTypeBO( payment.getPaymentType() ) );
        paymentBO.setPaymentProduct( payment.getPaymentProduct() );
        paymentBO.setStartDate( payment.getStartDate() );
        paymentBO.setEndDate( payment.getEndDate() );
        paymentBO.setExecutionRule( payment.getExecutionRule() );
        paymentBO.setFrequency( frequencyCodeToFrequencyCodeBO( payment.getFrequency() ) );
        paymentBO.setDayOfExecution( payment.getDayOfExecution() );
        paymentBO.setDebtorAccount( accountReferenceToAccountReferenceBO( payment.getDebtorAccount() ) );
        paymentBO.setDebtorName( payment.getDebtorName() );
        paymentBO.setDebtorAgent( payment.getDebtorAgent() );
        paymentBO.setTransactionStatus( transactionStatusToTransactionStatusBO( payment.getTransactionStatus() ) );
        paymentBO.setTargets( paymentTargetListToPaymentTargetBOList( payment.getTargets() ) );
        paymentBO.setAccountId( payment.getAccountId() );

        return paymentBO;
    }

    @Override
    public TransactionStatus toTransactionStatus(TransactionStatusBO status) {
        if ( status == null ) {
            return null;
        }

        TransactionStatus transactionStatus;

        switch ( status ) {
            case ACCC: transactionStatus = TransactionStatus.ACCC;
            break;
            case ACCP: transactionStatus = TransactionStatus.ACCP;
            break;
            case ACSC: transactionStatus = TransactionStatus.ACSC;
            break;
            case ACSP: transactionStatus = TransactionStatus.ACSP;
            break;
            case ACTC: transactionStatus = TransactionStatus.ACTC;
            break;
            case ACWC: transactionStatus = TransactionStatus.ACWC;
            break;
            case ACWP: transactionStatus = TransactionStatus.ACWP;
            break;
            case RCVD: transactionStatus = TransactionStatus.RCVD;
            break;
            case PDNG: transactionStatus = TransactionStatus.PDNG;
            break;
            case RJCT: transactionStatus = TransactionStatus.RJCT;
            break;
            case CANC: transactionStatus = TransactionStatus.CANC;
            break;
            case ACFC: transactionStatus = TransactionStatus.ACFC;
            break;
            case PATC: transactionStatus = TransactionStatus.PATC;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + status );
        }

        return transactionStatus;
    }

    @Override
    public PaymentTargetBO toPaymentTargetBO(PaymentTarget target) {
        if ( target == null ) {
            return null;
        }

        PaymentTargetBO paymentTargetBO = new PaymentTargetBO();

        paymentTargetBO.setPaymentId( target.getPaymentId() );
        paymentTargetBO.setEndToEndIdentification( target.getEndToEndIdentification() );
        paymentTargetBO.setInstructedAmount( amountToAmountBO( target.getInstructedAmount() ) );
        paymentTargetBO.setCreditorAccount( accountReferenceToAccountReferenceBO( target.getCreditorAccount() ) );
        paymentTargetBO.setCreditorAgent( target.getCreditorAgent() );
        paymentTargetBO.setCreditorName( target.getCreditorName() );
        paymentTargetBO.setCreditorAddress( addressToAddressBO( target.getCreditorAddress() ) );
        paymentTargetBO.setPurposeCode( purposeCodeToPurposeCodeBO( target.getPurposeCode() ) );
        byte[] remittanceInformationUnstructuredArray = target.getRemittanceInformationUnstructuredArray();
        if ( remittanceInformationUnstructuredArray != null ) {
            paymentTargetBO.setRemittanceInformationUnstructuredArray( Arrays.copyOf( remittanceInformationUnstructuredArray, remittanceInformationUnstructuredArray.length ) );
        }
        byte[] remittanceInformationStructuredArray = target.getRemittanceInformationStructuredArray();
        if ( remittanceInformationStructuredArray != null ) {
            paymentTargetBO.setRemittanceInformationStructuredArray( Arrays.copyOf( remittanceInformationStructuredArray, remittanceInformationStructuredArray.length ) );
        }
        paymentTargetBO.setChargeBearer( chargeBearerToChargeBearerBO( target.getChargeBearer() ) );

        return paymentTargetBO;
    }

    @Override
    public PaymentTarget toPaymentTarget(PaymentTargetBO targetBO) {
        if ( targetBO == null ) {
            return null;
        }

        PaymentTarget paymentTarget = new PaymentTarget();

        paymentTarget.setPaymentId( targetBO.getPaymentId() );
        paymentTarget.setEndToEndIdentification( targetBO.getEndToEndIdentification() );
        paymentTarget.setInstructedAmount( amountBOToAmount( targetBO.getInstructedAmount() ) );
        paymentTarget.setCreditorAccount( accountReferenceBOToAccountReference( targetBO.getCreditorAccount() ) );
        paymentTarget.setCreditorAgent( targetBO.getCreditorAgent() );
        paymentTarget.setCreditorName( targetBO.getCreditorName() );
        paymentTarget.setCreditorAddress( addressBOToAddress( targetBO.getCreditorAddress() ) );
        paymentTarget.setPurposeCode( purposeCodeBOToPurposeCode( targetBO.getPurposeCode() ) );
        paymentTarget.setChargeBearer( chargeBearerBOToChargeBearer( targetBO.getChargeBearer() ) );
        byte[] remittanceInformationUnstructuredArray = targetBO.getRemittanceInformationUnstructuredArray();
        if ( remittanceInformationUnstructuredArray != null ) {
            paymentTarget.setRemittanceInformationUnstructuredArray( Arrays.copyOf( remittanceInformationUnstructuredArray, remittanceInformationUnstructuredArray.length ) );
        }
        byte[] remittanceInformationStructuredArray = targetBO.getRemittanceInformationStructuredArray();
        if ( remittanceInformationStructuredArray != null ) {
            paymentTarget.setRemittanceInformationStructuredArray( Arrays.copyOf( remittanceInformationStructuredArray, remittanceInformationStructuredArray.length ) );
        }
        paymentTarget.setPayment( toPayment( targetBO.getPayment() ) );

        return paymentTarget;
    }

    @Override
    public PaymentOrderDetailsBO toPaymentOrder(PaymentBO payment) {
        if ( payment == null ) {
            return null;
        }

        PaymentOrderDetailsBO paymentOrderDetailsBO = new PaymentOrderDetailsBO();

        paymentOrderDetailsBO.setPaymentId( payment.getPaymentId() );
        paymentOrderDetailsBO.setBatchBookingPreferred( payment.getBatchBookingPreferred() );
        paymentOrderDetailsBO.setRequestedExecutionDate( payment.getRequestedExecutionDate() );
        paymentOrderDetailsBO.setRequestedExecutionTime( payment.getRequestedExecutionTime() );
        paymentOrderDetailsBO.setPaymentType( payment.getPaymentType() );
        paymentOrderDetailsBO.setPaymentProduct( payment.getPaymentProduct() );
        paymentOrderDetailsBO.setStartDate( payment.getStartDate() );
        paymentOrderDetailsBO.setEndDate( payment.getEndDate() );
        paymentOrderDetailsBO.setExecutionRule( payment.getExecutionRule() );
        paymentOrderDetailsBO.setFrequency( payment.getFrequency() );
        paymentOrderDetailsBO.setDayOfExecution( payment.getDayOfExecution() );
        paymentOrderDetailsBO.setDebtorAccount( payment.getDebtorAccount() );
        paymentOrderDetailsBO.setTransactionStatus( payment.getTransactionStatus() );

        return paymentOrderDetailsBO;
    }

    @Override
    public PaymentTargetDetailsBO toPaymentTargetDetails(String id, PaymentTargetBO paymentTarget, LocalDate postingTime, List<ExchangeRateBO> rate, BalanceBO balanceAfterTransaction) {
        if ( id == null && paymentTarget == null && postingTime == null && rate == null && balanceAfterTransaction == null ) {
            return null;
        }

        PaymentTargetDetailsBO paymentTargetDetailsBO = new PaymentTargetDetailsBO();

        if ( paymentTarget != null ) {
            paymentTargetDetailsBO.setTransactionStatus( paymentTargetPaymentTransactionStatus( paymentTarget ) );
            byte[] remittanceInformationUnstructuredArray = paymentTarget.getRemittanceInformationUnstructuredArray();
            if ( remittanceInformationUnstructuredArray != null ) {
                paymentTargetDetailsBO.setRemittanceInformationUnstructuredArray( Arrays.copyOf( remittanceInformationUnstructuredArray, remittanceInformationUnstructuredArray.length ) );
            }
            byte[] remittanceInformationStructuredArray = paymentTarget.getRemittanceInformationStructuredArray();
            if ( remittanceInformationStructuredArray != null ) {
                paymentTargetDetailsBO.setRemittanceInformationStructuredArray( Arrays.copyOf( remittanceInformationStructuredArray, remittanceInformationStructuredArray.length ) );
            }
            paymentTargetDetailsBO.setDebtorName( paymentTargetPaymentDebtorName( paymentTarget ) );
            paymentTargetDetailsBO.setDebtorAgent( paymentTargetPaymentDebtorAgent( paymentTarget ) );
            paymentTargetDetailsBO.setCreditorName( paymentTarget.getCreditorName() );
            paymentTargetDetailsBO.setCreditorAgent( paymentTarget.getCreditorAgent() );
            paymentTargetDetailsBO.setCreditorAddress( paymentTarget.getCreditorAddress() );
            paymentTargetDetailsBO.setCreditorAccount( paymentTarget.getCreditorAccount() );
            paymentTargetDetailsBO.setEndToEndId( paymentTarget.getEndToEndIdentification() );
            paymentTargetDetailsBO.setTransactionAmount( paymentTarget.getInstructedAmount() );
            paymentTargetDetailsBO.setDebtorAccount( paymentTargetPaymentDebtorAccount( paymentTarget ) );
            paymentTargetDetailsBO.setPaymentOrderId( paymentTargetPaymentPaymentId( paymentTarget ) );
            paymentTargetDetailsBO.setPaymentType( paymentTargetPaymentPaymentType( paymentTarget ) );
            paymentTargetDetailsBO.setPaymentProduct( paymentTargetPaymentPaymentProduct( paymentTarget ) );
            paymentTargetDetailsBO.setPurposeCode( paymentTarget.getPurposeCode() );
        }
        if ( postingTime != null ) {
            paymentTargetDetailsBO.setBookingDate( postingTime );
            paymentTargetDetailsBO.setValueDate( postingTime );
        }
        paymentTargetDetailsBO.setTransactionId( id );
        List<ExchangeRateBO> list = rate;
        if ( list != null ) {
            paymentTargetDetailsBO.setExchangeRate( new ArrayList<ExchangeRateBO>( list ) );
        }
        paymentTargetDetailsBO.setBalanceAfterTransaction( balanceAfterTransaction );
        paymentTargetDetailsBO.setBankTransactionCode( de.adorsys.ledgers.bank.api.domain.BankTransactionCode.getByPaymentProduct(paymentTarget.getPayment().getPaymentProduct()) );
        paymentTargetDetailsBO.setProprietaryBankTransactionCode( de.adorsys.ledgers.bank.api.domain.BankTransactionCode.getByPaymentProduct(paymentTarget.getPayment().getPaymentProduct()) );

        return paymentTargetDetailsBO;
    }

    @Override
    public PaymentTargetDetailsBO toPaymentTargetDetailsBatch(String id, PaymentBO payment, AmountBO amount, LocalDate postingTime, List<ExchangeRateBO> rate, BalanceBO balanceAfterTransaction, ObjectMapper objectMapper) {
        if ( id == null && payment == null && amount == null && postingTime == null && rate == null && balanceAfterTransaction == null && objectMapper == null ) {
            return null;
        }

        PaymentTargetDetailsBO paymentTargetDetailsBO = new PaymentTargetDetailsBO();

        if ( payment != null ) {
            paymentTargetDetailsBO.setTransactionStatus( payment.getTransactionStatus() );
            paymentTargetDetailsBO.setDebtorName( payment.getDebtorName() );
            paymentTargetDetailsBO.setDebtorAgent( payment.getDebtorAgent() );
            paymentTargetDetailsBO.setDebtorAccount( payment.getDebtorAccount() );
            paymentTargetDetailsBO.setPaymentType( payment.getPaymentType() );
            paymentTargetDetailsBO.setPaymentOrderId( payment.getPaymentId() );
            paymentTargetDetailsBO.setPaymentProduct( payment.getPaymentProduct() );
        }
        if ( postingTime != null ) {
            paymentTargetDetailsBO.setValueDate( postingTime );
            paymentTargetDetailsBO.setBookingDate( postingTime );
        }
        paymentTargetDetailsBO.setTransactionId( id );
        paymentTargetDetailsBO.setTransactionAmount( amount );
        List<ExchangeRateBO> list = rate;
        if ( list != null ) {
            paymentTargetDetailsBO.setExchangeRate( new ArrayList<ExchangeRateBO>( list ) );
        }
        paymentTargetDetailsBO.setBalanceAfterTransaction( balanceAfterTransaction );
        paymentTargetDetailsBO.setRemittanceInformationUnstructuredArray( convertStringToRemittanceInformationUnstructuredByteArray("Batch booking, no remittance information available", objectMapper) );
        paymentTargetDetailsBO.setEndToEndId( payment.getTargets().stream().map(PaymentTargetBO::getEndToEndIdentification).reduce("", (accum, s) -> accum + ", " + s).replaceFirst(", ","" ) );
        paymentTargetDetailsBO.setProprietaryBankTransactionCode( de.adorsys.ledgers.bank.api.domain.BankTransactionCode.getByPaymentProduct(payment.getPaymentProduct()) );
        paymentTargetDetailsBO.setBankTransactionCode( de.adorsys.ledgers.bank.api.domain.BankTransactionCode.getByPaymentProduct(payment.getPaymentProduct()) );
        paymentTargetDetailsBO.setCreditorAgent( "multiple" );
        paymentTargetDetailsBO.setCreditorName( "multiple" );

        return paymentTargetDetailsBO;
    }

    @Override
    public TransactionDetailsBO toDepositTransactionDetails(AmountBO amount, BankAccountBO BankAccount, AccountReferenceBO creditorAccount, LocalDate postingDate, String postingLineId, BalanceBO balanceAfterTransaction, ObjectMapper objectMapper) {
        if ( amount == null && BankAccount == null && creditorAccount == null && postingDate == null && postingLineId == null && balanceAfterTransaction == null && objectMapper == null ) {
            return null;
        }

        TransactionDetailsBO transactionDetailsBO = new TransactionDetailsBO();

        if ( BankAccount != null ) {
            transactionDetailsBO.setDebtorName( BankAccount.getName() );
            transactionDetailsBO.setDebtorAccount( BankAccount.getReference() );
            transactionDetailsBO.setCreditorName( BankAccount.getName() );
        }
        if ( postingDate != null ) {
            transactionDetailsBO.setBookingDate( postingDate );
            transactionDetailsBO.setValueDate( postingDate );
        }
        if ( postingLineId != null ) {
            transactionDetailsBO.setTransactionId( postingLineId );
            transactionDetailsBO.setEndToEndId( postingLineId );
        }
        transactionDetailsBO.setTransactionAmount( amount );
        transactionDetailsBO.setCreditorAccount( creditorAccount );
        transactionDetailsBO.setBalanceAfterTransaction( balanceAfterTransaction );
        transactionDetailsBO.setRemittanceInformationUnstructuredArray( convertStringToRemittanceInformationUnstructuredByteArray("Cash deposit through Bank ATM", objectMapper) );
        transactionDetailsBO.setProprietaryBankTransactionCode( "PMNT-MCOP-OTHR" );
        transactionDetailsBO.setBankTransactionCode( "PMNT-MCOP-OTHR" );

        return transactionDetailsBO;
    }

    @Override
    public List<PaymentBO> toPaymentBOList(List<Payment> payments) {
        if ( payments == null ) {
            return null;
        }

        List<PaymentBO> list = new ArrayList<PaymentBO>( payments.size() );
        for ( Payment payment : payments ) {
            list.add( toPaymentBO( payment ) );
        }

        return list;
    }

    protected PaymentType paymentTypeBOToPaymentType(PaymentTypeBO paymentTypeBO) {
        if ( paymentTypeBO == null ) {
            return null;
        }

        PaymentType paymentType;

        switch ( paymentTypeBO ) {
            case SINGLE: paymentType = PaymentType.SINGLE;
            break;
            case BULK: paymentType = PaymentType.BULK;
            break;
            case PERIODIC: paymentType = PaymentType.PERIODIC;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + paymentTypeBO );
        }

        return paymentType;
    }

    protected FrequencyCode frequencyCodeBOToFrequencyCode(FrequencyCodeBO frequencyCodeBO) {
        if ( frequencyCodeBO == null ) {
            return null;
        }

        FrequencyCode frequencyCode;

        switch ( frequencyCodeBO ) {
            case DAILY: frequencyCode = FrequencyCode.DAILY;
            break;
            case WEEKLY: frequencyCode = FrequencyCode.WEEKLY;
            break;
            case EVERYTWOWEEKS: frequencyCode = FrequencyCode.EVERYTWOWEEKS;
            break;
            case MONTHLY: frequencyCode = FrequencyCode.MONTHLY;
            break;
            case EVERYTWOMONTHS: frequencyCode = FrequencyCode.EVERYTWOMONTHS;
            break;
            case QUARTERLY: frequencyCode = FrequencyCode.QUARTERLY;
            break;
            case SEMIANNUAL: frequencyCode = FrequencyCode.SEMIANNUAL;
            break;
            case ANNUAL: frequencyCode = FrequencyCode.ANNUAL;
            break;
            case MONTHLYVARIABLE: frequencyCode = FrequencyCode.MONTHLYVARIABLE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + frequencyCodeBO );
        }

        return frequencyCode;
    }

    protected AccountReference accountReferenceBOToAccountReference(AccountReferenceBO accountReferenceBO) {
        if ( accountReferenceBO == null ) {
            return null;
        }

        AccountReference accountReference = new AccountReference();

        accountReference.setIban( accountReferenceBO.getIban() );
        accountReference.setBban( accountReferenceBO.getBban() );
        accountReference.setPan( accountReferenceBO.getPan() );
        accountReference.setMaskedPan( accountReferenceBO.getMaskedPan() );
        accountReference.setMsisdn( accountReferenceBO.getMsisdn() );
        if ( accountReferenceBO.getCurrency() != null ) {
            accountReference.setCurrency( accountReferenceBO.getCurrency().getCurrencyCode() );
        }

        return accountReference;
    }

    protected List<PaymentTarget> paymentTargetBOListToPaymentTargetList(List<PaymentTargetBO> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentTarget> list1 = new ArrayList<PaymentTarget>( list.size() );
        for ( PaymentTargetBO paymentTargetBO : list ) {
            list1.add( toPaymentTarget( paymentTargetBO ) );
        }

        return list1;
    }

    protected PaymentTypeBO paymentTypeToPaymentTypeBO(PaymentType paymentType) {
        if ( paymentType == null ) {
            return null;
        }

        PaymentTypeBO paymentTypeBO;

        switch ( paymentType ) {
            case SINGLE: paymentTypeBO = PaymentTypeBO.SINGLE;
            break;
            case BULK: paymentTypeBO = PaymentTypeBO.BULK;
            break;
            case PERIODIC: paymentTypeBO = PaymentTypeBO.PERIODIC;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + paymentType );
        }

        return paymentTypeBO;
    }

    protected FrequencyCodeBO frequencyCodeToFrequencyCodeBO(FrequencyCode frequencyCode) {
        if ( frequencyCode == null ) {
            return null;
        }

        FrequencyCodeBO frequencyCodeBO;

        switch ( frequencyCode ) {
            case DAILY: frequencyCodeBO = FrequencyCodeBO.DAILY;
            break;
            case WEEKLY: frequencyCodeBO = FrequencyCodeBO.WEEKLY;
            break;
            case EVERYTWOWEEKS: frequencyCodeBO = FrequencyCodeBO.EVERYTWOWEEKS;
            break;
            case MONTHLY: frequencyCodeBO = FrequencyCodeBO.MONTHLY;
            break;
            case EVERYTWOMONTHS: frequencyCodeBO = FrequencyCodeBO.EVERYTWOMONTHS;
            break;
            case QUARTERLY: frequencyCodeBO = FrequencyCodeBO.QUARTERLY;
            break;
            case SEMIANNUAL: frequencyCodeBO = FrequencyCodeBO.SEMIANNUAL;
            break;
            case ANNUAL: frequencyCodeBO = FrequencyCodeBO.ANNUAL;
            break;
            case MONTHLYVARIABLE: frequencyCodeBO = FrequencyCodeBO.MONTHLYVARIABLE;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + frequencyCode );
        }

        return frequencyCodeBO;
    }

    protected AccountReferenceBO accountReferenceToAccountReferenceBO(AccountReference accountReference) {
        if ( accountReference == null ) {
            return null;
        }

        AccountReferenceBO accountReferenceBO = new AccountReferenceBO();

        accountReferenceBO.setIban( accountReference.getIban() );
        accountReferenceBO.setBban( accountReference.getBban() );
        accountReferenceBO.setPan( accountReference.getPan() );
        accountReferenceBO.setMaskedPan( accountReference.getMaskedPan() );
        accountReferenceBO.setMsisdn( accountReference.getMsisdn() );
        if ( accountReference.getCurrency() != null ) {
            accountReferenceBO.setCurrency( Currency.getInstance( accountReference.getCurrency() ) );
        }

        return accountReferenceBO;
    }

    protected TransactionStatusBO transactionStatusToTransactionStatusBO(TransactionStatus transactionStatus) {
        if ( transactionStatus == null ) {
            return null;
        }

        TransactionStatusBO transactionStatusBO;

        switch ( transactionStatus ) {
            case ACCC: transactionStatusBO = TransactionStatusBO.ACCC;
            break;
            case ACCP: transactionStatusBO = TransactionStatusBO.ACCP;
            break;
            case ACSC: transactionStatusBO = TransactionStatusBO.ACSC;
            break;
            case ACSP: transactionStatusBO = TransactionStatusBO.ACSP;
            break;
            case ACTC: transactionStatusBO = TransactionStatusBO.ACTC;
            break;
            case ACWC: transactionStatusBO = TransactionStatusBO.ACWC;
            break;
            case ACWP: transactionStatusBO = TransactionStatusBO.ACWP;
            break;
            case RCVD: transactionStatusBO = TransactionStatusBO.RCVD;
            break;
            case PDNG: transactionStatusBO = TransactionStatusBO.PDNG;
            break;
            case RJCT: transactionStatusBO = TransactionStatusBO.RJCT;
            break;
            case CANC: transactionStatusBO = TransactionStatusBO.CANC;
            break;
            case ACFC: transactionStatusBO = TransactionStatusBO.ACFC;
            break;
            case PATC: transactionStatusBO = TransactionStatusBO.PATC;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionStatus );
        }

        return transactionStatusBO;
    }

    protected List<PaymentTargetBO> paymentTargetListToPaymentTargetBOList(List<PaymentTarget> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentTargetBO> list1 = new ArrayList<PaymentTargetBO>( list.size() );
        for ( PaymentTarget paymentTarget : list ) {
            list1.add( toPaymentTargetBO( paymentTarget ) );
        }

        return list1;
    }

    protected AmountBO amountToAmountBO(Amount amount) {
        if ( amount == null ) {
            return null;
        }

        AmountBO amountBO = new AmountBO();

        if ( amount.getCurrency() != null ) {
            amountBO.setCurrency( Currency.getInstance( amount.getCurrency() ) );
        }
        amountBO.setAmount( amount.getAmount() );

        return amountBO;
    }

    protected AddressBO addressToAddressBO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressBO addressBO = new AddressBO();

        addressBO.setStreet( address.getStreet() );
        addressBO.setBuildingNumber( address.getBuildingNumber() );
        addressBO.setCity( address.getCity() );
        addressBO.setPostalCode( address.getPostalCode() );
        addressBO.setCountry( address.getCountry() );
        addressBO.setLine1( address.getLine1() );
        addressBO.setLine2( address.getLine2() );

        return addressBO;
    }

    protected PurposeCodeBO purposeCodeToPurposeCodeBO(PurposeCode purposeCode) {
        if ( purposeCode == null ) {
            return null;
        }

        PurposeCodeBO purposeCodeBO;

        switch ( purposeCode ) {
            case BKDF: purposeCodeBO = PurposeCodeBO.BKDF;
            break;
            case BKFE: purposeCodeBO = PurposeCodeBO.BKFE;
            break;
            case BKFM: purposeCodeBO = PurposeCodeBO.BKFM;
            break;
            case BKIP: purposeCodeBO = PurposeCodeBO.BKIP;
            break;
            case BKPP: purposeCodeBO = PurposeCodeBO.BKPP;
            break;
            case CBLK: purposeCodeBO = PurposeCodeBO.CBLK;
            break;
            case CDCB: purposeCodeBO = PurposeCodeBO.CDCB;
            break;
            case CDCD: purposeCodeBO = PurposeCodeBO.CDCD;
            break;
            case CDCS: purposeCodeBO = PurposeCodeBO.CDCS;
            break;
            case CDDP: purposeCodeBO = PurposeCodeBO.CDDP;
            break;
            case CDOC: purposeCodeBO = PurposeCodeBO.CDOC;
            break;
            case CDQC: purposeCodeBO = PurposeCodeBO.CDQC;
            break;
            case ETUP: purposeCodeBO = PurposeCodeBO.ETUP;
            break;
            case FCOL: purposeCodeBO = PurposeCodeBO.FCOL;
            break;
            case MTUP: purposeCodeBO = PurposeCodeBO.MTUP;
            break;
            case ACCT: purposeCodeBO = PurposeCodeBO.ACCT;
            break;
            case CASH: purposeCodeBO = PurposeCodeBO.CASH;
            break;
            case COLL: purposeCodeBO = PurposeCodeBO.COLL;
            break;
            case CSDB: purposeCodeBO = PurposeCodeBO.CSDB;
            break;
            case DEPT: purposeCodeBO = PurposeCodeBO.DEPT;
            break;
            case INTC: purposeCodeBO = PurposeCodeBO.INTC;
            break;
            case LIMA: purposeCodeBO = PurposeCodeBO.LIMA;
            break;
            case NETT: purposeCodeBO = PurposeCodeBO.NETT;
            break;
            case BFWD: purposeCodeBO = PurposeCodeBO.BFWD;
            break;
            case CCIR: purposeCodeBO = PurposeCodeBO.CCIR;
            break;
            case CCPC: purposeCodeBO = PurposeCodeBO.CCPC;
            break;
            case CCPM: purposeCodeBO = PurposeCodeBO.CCPM;
            break;
            case CCSM: purposeCodeBO = PurposeCodeBO.CCSM;
            break;
            case CRDS: purposeCodeBO = PurposeCodeBO.CRDS;
            break;
            case CRPR: purposeCodeBO = PurposeCodeBO.CRPR;
            break;
            case CRSP: purposeCodeBO = PurposeCodeBO.CRSP;
            break;
            case CRTL: purposeCodeBO = PurposeCodeBO.CRTL;
            break;
            case EQPT: purposeCodeBO = PurposeCodeBO.EQPT;
            break;
            case EQUS: purposeCodeBO = PurposeCodeBO.EQUS;
            break;
            case EXPT: purposeCodeBO = PurposeCodeBO.EXPT;
            break;
            case EXTD: purposeCodeBO = PurposeCodeBO.EXTD;
            break;
            case FIXI: purposeCodeBO = PurposeCodeBO.FIXI;
            break;
            case FWBC: purposeCodeBO = PurposeCodeBO.FWBC;
            break;
            case FWCC: purposeCodeBO = PurposeCodeBO.FWCC;
            break;
            case FWSB: purposeCodeBO = PurposeCodeBO.FWSB;
            break;
            case FWSC: purposeCodeBO = PurposeCodeBO.FWSC;
            break;
            case MARG: purposeCodeBO = PurposeCodeBO.MARG;
            break;
            case MBSB: purposeCodeBO = PurposeCodeBO.MBSB;
            break;
            case MBSC: purposeCodeBO = PurposeCodeBO.MBSC;
            break;
            case MGCC: purposeCodeBO = PurposeCodeBO.MGCC;
            break;
            case MGSC: purposeCodeBO = PurposeCodeBO.MGSC;
            break;
            case OCCC: purposeCodeBO = PurposeCodeBO.OCCC;
            break;
            case OPBC: purposeCodeBO = PurposeCodeBO.OPBC;
            break;
            case OPCC: purposeCodeBO = PurposeCodeBO.OPCC;
            break;
            case OPSB: purposeCodeBO = PurposeCodeBO.OPSB;
            break;
            case OPSC: purposeCodeBO = PurposeCodeBO.OPSC;
            break;
            case OPTN: purposeCodeBO = PurposeCodeBO.OPTN;
            break;
            case OTCD: purposeCodeBO = PurposeCodeBO.OTCD;
            break;
            case REPO: purposeCodeBO = PurposeCodeBO.REPO;
            break;
            case RPBC: purposeCodeBO = PurposeCodeBO.RPBC;
            break;
            case RPCC: purposeCodeBO = PurposeCodeBO.RPCC;
            break;
            case RPSB: purposeCodeBO = PurposeCodeBO.RPSB;
            break;
            case RPSC: purposeCodeBO = PurposeCodeBO.RPSC;
            break;
            case RVPO: purposeCodeBO = PurposeCodeBO.RVPO;
            break;
            case SBSC: purposeCodeBO = PurposeCodeBO.SBSC;
            break;
            case SCIE: purposeCodeBO = PurposeCodeBO.SCIE;
            break;
            case SCIR: purposeCodeBO = PurposeCodeBO.SCIR;
            break;
            case SCRP: purposeCodeBO = PurposeCodeBO.SCRP;
            break;
            case SHBC: purposeCodeBO = PurposeCodeBO.SHBC;
            break;
            case SHCC: purposeCodeBO = PurposeCodeBO.SHCC;
            break;
            case SHSL: purposeCodeBO = PurposeCodeBO.SHSL;
            break;
            case SLEB: purposeCodeBO = PurposeCodeBO.SLEB;
            break;
            case SLOA: purposeCodeBO = PurposeCodeBO.SLOA;
            break;
            case SWBC: purposeCodeBO = PurposeCodeBO.SWBC;
            break;
            case SWCC: purposeCodeBO = PurposeCodeBO.SWCC;
            break;
            case SWPT: purposeCodeBO = PurposeCodeBO.SWPT;
            break;
            case SWSB: purposeCodeBO = PurposeCodeBO.SWSB;
            break;
            case SWSC: purposeCodeBO = PurposeCodeBO.SWSC;
            break;
            case TBAS: purposeCodeBO = PurposeCodeBO.TBAS;
            break;
            case TBBC: purposeCodeBO = PurposeCodeBO.TBBC;
            break;
            case TBCC: purposeCodeBO = PurposeCodeBO.TBCC;
            break;
            case TRCP: purposeCodeBO = PurposeCodeBO.TRCP;
            break;
            case AGRT: purposeCodeBO = PurposeCodeBO.AGRT;
            break;
            case AREN: purposeCodeBO = PurposeCodeBO.AREN;
            break;
            case BEXP: purposeCodeBO = PurposeCodeBO.BEXP;
            break;
            case BOCE: purposeCodeBO = PurposeCodeBO.BOCE;
            break;
            case COMC: purposeCodeBO = PurposeCodeBO.COMC;
            break;
            case CPYR: purposeCodeBO = PurposeCodeBO.CPYR;
            break;
            case GDDS: purposeCodeBO = PurposeCodeBO.GDDS;
            break;
            case GDSV: purposeCodeBO = PurposeCodeBO.GDSV;
            break;
            case GSCB: purposeCodeBO = PurposeCodeBO.GSCB;
            break;
            case LICF: purposeCodeBO = PurposeCodeBO.LICF;
            break;
            case MP2B: purposeCodeBO = PurposeCodeBO.MP2B;
            break;
            case POPE: purposeCodeBO = PurposeCodeBO.POPE;
            break;
            case ROYA: purposeCodeBO = PurposeCodeBO.ROYA;
            break;
            case SCVE: purposeCodeBO = PurposeCodeBO.SCVE;
            break;
            case SERV: purposeCodeBO = PurposeCodeBO.SERV;
            break;
            case SUBS: purposeCodeBO = PurposeCodeBO.SUBS;
            break;
            case SUPP: purposeCodeBO = PurposeCodeBO.SUPP;
            break;
            case TRAD: purposeCodeBO = PurposeCodeBO.TRAD;
            break;
            case CHAR: purposeCodeBO = PurposeCodeBO.CHAR;
            break;
            case COMT: purposeCodeBO = PurposeCodeBO.COMT;
            break;
            case MP2P: purposeCodeBO = PurposeCodeBO.MP2P;
            break;
            case ECPG: purposeCodeBO = PurposeCodeBO.ECPG;
            break;
            case ECPR: purposeCodeBO = PurposeCodeBO.ECPR;
            break;
            case ECPU: purposeCodeBO = PurposeCodeBO.ECPU;
            break;
            case EPAY: purposeCodeBO = PurposeCodeBO.EPAY;
            break;
            case CLPR: purposeCodeBO = PurposeCodeBO.CLPR;
            break;
            case COMP: purposeCodeBO = PurposeCodeBO.COMP;
            break;
            case DBTC: purposeCodeBO = PurposeCodeBO.DBTC;
            break;
            case GOVI: purposeCodeBO = PurposeCodeBO.GOVI;
            break;
            case HLRP: purposeCodeBO = PurposeCodeBO.HLRP;
            break;
            case HLST: purposeCodeBO = PurposeCodeBO.HLST;
            break;
            case INPC: purposeCodeBO = PurposeCodeBO.INPC;
            break;
            case INPR: purposeCodeBO = PurposeCodeBO.INPR;
            break;
            case INSC: purposeCodeBO = PurposeCodeBO.INSC;
            break;
            case INSU: purposeCodeBO = PurposeCodeBO.INSU;
            break;
            case INTE: purposeCodeBO = PurposeCodeBO.INTE;
            break;
            case LBRI: purposeCodeBO = PurposeCodeBO.LBRI;
            break;
            case LIFI: purposeCodeBO = PurposeCodeBO.LIFI;
            break;
            case LOAN: purposeCodeBO = PurposeCodeBO.LOAN;
            break;
            case LOAR: purposeCodeBO = PurposeCodeBO.LOAR;
            break;
            case PENO: purposeCodeBO = PurposeCodeBO.PENO;
            break;
            case PPTI: purposeCodeBO = PurposeCodeBO.PPTI;
            break;
            case RELG: purposeCodeBO = PurposeCodeBO.RELG;
            break;
            case RINP: purposeCodeBO = PurposeCodeBO.RINP;
            break;
            case TRFD: purposeCodeBO = PurposeCodeBO.TRFD;
            break;
            case FORW: purposeCodeBO = PurposeCodeBO.FORW;
            break;
            case FXNT: purposeCodeBO = PurposeCodeBO.FXNT;
            break;
            case ADMG: purposeCodeBO = PurposeCodeBO.ADMG;
            break;
            case ADVA: purposeCodeBO = PurposeCodeBO.ADVA;
            break;
            case BCDM: purposeCodeBO = PurposeCodeBO.BCDM;
            break;
            case BCFG: purposeCodeBO = PurposeCodeBO.BCFG;
            break;
            case BLDM: purposeCodeBO = PurposeCodeBO.BLDM;
            break;
            case BNET: purposeCodeBO = PurposeCodeBO.BNET;
            break;
            case CBFF: purposeCodeBO = PurposeCodeBO.CBFF;
            break;
            case CBFR: purposeCodeBO = PurposeCodeBO.CBFR;
            break;
            case CCRD: purposeCodeBO = PurposeCodeBO.CCRD;
            break;
            case CDBL: purposeCodeBO = PurposeCodeBO.CDBL;
            break;
            case CFEE: purposeCodeBO = PurposeCodeBO.CFEE;
            break;
            case CGDD: purposeCodeBO = PurposeCodeBO.CGDD;
            break;
            case CORT: purposeCodeBO = PurposeCodeBO.CORT;
            break;
            case COST: purposeCodeBO = PurposeCodeBO.COST;
            break;
            case CPKC: purposeCodeBO = PurposeCodeBO.CPKC;
            break;
            case DCRD: purposeCodeBO = PurposeCodeBO.DCRD;
            break;
            case DSMT: purposeCodeBO = PurposeCodeBO.DSMT;
            break;
            case DVPM: purposeCodeBO = PurposeCodeBO.DVPM;
            break;
            case EDUC: purposeCodeBO = PurposeCodeBO.EDUC;
            break;
            case FACT: purposeCodeBO = PurposeCodeBO.FACT;
            break;
            case FAND: purposeCodeBO = PurposeCodeBO.FAND;
            break;
            case FCPM: purposeCodeBO = PurposeCodeBO.FCPM;
            break;
            case FEES: purposeCodeBO = PurposeCodeBO.FEES;
            break;
            case GOVT: purposeCodeBO = PurposeCodeBO.GOVT;
            break;
            case ICCP: purposeCodeBO = PurposeCodeBO.ICCP;
            break;
            case IDCP: purposeCodeBO = PurposeCodeBO.IDCP;
            break;
            case IHRP: purposeCodeBO = PurposeCodeBO.IHRP;
            break;
            case INSM: purposeCodeBO = PurposeCodeBO.INSM;
            break;
            case IVPT: purposeCodeBO = PurposeCodeBO.IVPT;
            break;
            case MCDM: purposeCodeBO = PurposeCodeBO.MCDM;
            break;
            case MCFG: purposeCodeBO = PurposeCodeBO.MCFG;
            break;
            case MSVC: purposeCodeBO = PurposeCodeBO.MSVC;
            break;
            case NOWS: purposeCodeBO = PurposeCodeBO.NOWS;
            break;
            case OCDM: purposeCodeBO = PurposeCodeBO.OCDM;
            break;
            case OCFG: purposeCodeBO = PurposeCodeBO.OCFG;
            break;
            case OFEE: purposeCodeBO = PurposeCodeBO.OFEE;
            break;
            case OTHR: purposeCodeBO = PurposeCodeBO.OTHR;
            break;
            case PADD: purposeCodeBO = PurposeCodeBO.PADD;
            break;
            case PTSP: purposeCodeBO = PurposeCodeBO.PTSP;
            break;
            case RCKE: purposeCodeBO = PurposeCodeBO.RCKE;
            break;
            case RCPT: purposeCodeBO = PurposeCodeBO.RCPT;
            break;
            case REBT: purposeCodeBO = PurposeCodeBO.REBT;
            break;
            case REFU: purposeCodeBO = PurposeCodeBO.REFU;
            break;
            case RENT: purposeCodeBO = PurposeCodeBO.RENT;
            break;
            case REOD: purposeCodeBO = PurposeCodeBO.REOD;
            break;
            case RIMB: purposeCodeBO = PurposeCodeBO.RIMB;
            break;
            case RPNT: purposeCodeBO = PurposeCodeBO.RPNT;
            break;
            case RRBN: purposeCodeBO = PurposeCodeBO.RRBN;
            break;
            case RVPM: purposeCodeBO = PurposeCodeBO.RVPM;
            break;
            case SLPI: purposeCodeBO = PurposeCodeBO.SLPI;
            break;
            case SPLT: purposeCodeBO = PurposeCodeBO.SPLT;
            break;
            case STDY: purposeCodeBO = PurposeCodeBO.STDY;
            break;
            case TBAN: purposeCodeBO = PurposeCodeBO.TBAN;
            break;
            case TBIL: purposeCodeBO = PurposeCodeBO.TBIL;
            break;
            case TCSC: purposeCodeBO = PurposeCodeBO.TCSC;
            break;
            case TELI: purposeCodeBO = PurposeCodeBO.TELI;
            break;
            case TMPG: purposeCodeBO = PurposeCodeBO.TMPG;
            break;
            case TPRI: purposeCodeBO = PurposeCodeBO.TPRI;
            break;
            case TPRP: purposeCodeBO = PurposeCodeBO.TPRP;
            break;
            case TRNC: purposeCodeBO = PurposeCodeBO.TRNC;
            break;
            case TRVC: purposeCodeBO = PurposeCodeBO.TRVC;
            break;
            case WEBI: purposeCodeBO = PurposeCodeBO.WEBI;
            break;
            case ANNI: purposeCodeBO = PurposeCodeBO.ANNI;
            break;
            case CAFI: purposeCodeBO = PurposeCodeBO.CAFI;
            break;
            case CFDI: purposeCodeBO = PurposeCodeBO.CFDI;
            break;
            case CMDT: purposeCodeBO = PurposeCodeBO.CMDT;
            break;
            case DERI: purposeCodeBO = PurposeCodeBO.DERI;
            break;
            case DIVD: purposeCodeBO = PurposeCodeBO.DIVD;
            break;
            case FREX: purposeCodeBO = PurposeCodeBO.FREX;
            break;
            case HEDG: purposeCodeBO = PurposeCodeBO.HEDG;
            break;
            case INVS: purposeCodeBO = PurposeCodeBO.INVS;
            break;
            case PRME: purposeCodeBO = PurposeCodeBO.PRME;
            break;
            case SAVG: purposeCodeBO = PurposeCodeBO.SAVG;
            break;
            case SECU: purposeCodeBO = PurposeCodeBO.SECU;
            break;
            case SEPI: purposeCodeBO = PurposeCodeBO.SEPI;
            break;
            case TREA: purposeCodeBO = PurposeCodeBO.TREA;
            break;
            case UNIT: purposeCodeBO = PurposeCodeBO.UNIT;
            break;
            case FNET: purposeCodeBO = PurposeCodeBO.FNET;
            break;
            case FUTR: purposeCodeBO = PurposeCodeBO.FUTR;
            break;
            case ANTS: purposeCodeBO = PurposeCodeBO.ANTS;
            break;
            case CVCF: purposeCodeBO = PurposeCodeBO.CVCF;
            break;
            case DMEQ: purposeCodeBO = PurposeCodeBO.DMEQ;
            break;
            case DNTS: purposeCodeBO = PurposeCodeBO.DNTS;
            break;
            case HLTC: purposeCodeBO = PurposeCodeBO.HLTC;
            break;
            case HLTI: purposeCodeBO = PurposeCodeBO.HLTI;
            break;
            case HSPC: purposeCodeBO = PurposeCodeBO.HSPC;
            break;
            case ICRF: purposeCodeBO = PurposeCodeBO.ICRF;
            break;
            case LTCF: purposeCodeBO = PurposeCodeBO.LTCF;
            break;
            case MAFC: purposeCodeBO = PurposeCodeBO.MAFC;
            break;
            case MARF: purposeCodeBO = PurposeCodeBO.MARF;
            break;
            case MDCS: purposeCodeBO = PurposeCodeBO.MDCS;
            break;
            case VIEW: purposeCodeBO = PurposeCodeBO.VIEW;
            break;
            case CDEP: purposeCodeBO = PurposeCodeBO.CDEP;
            break;
            case SWFP: purposeCodeBO = PurposeCodeBO.SWFP;
            break;
            case SWPP: purposeCodeBO = PurposeCodeBO.SWPP;
            break;
            case SWRS: purposeCodeBO = PurposeCodeBO.SWRS;
            break;
            case SWUF: purposeCodeBO = PurposeCodeBO.SWUF;
            break;
            case ADCS: purposeCodeBO = PurposeCodeBO.ADCS;
            break;
            case AEMP: purposeCodeBO = PurposeCodeBO.AEMP;
            break;
            case ALLW: purposeCodeBO = PurposeCodeBO.ALLW;
            break;
            case ALMY: purposeCodeBO = PurposeCodeBO.ALMY;
            break;
            case BBSC: purposeCodeBO = PurposeCodeBO.BBSC;
            break;
            case BECH: purposeCodeBO = PurposeCodeBO.BECH;
            break;
            case BENE: purposeCodeBO = PurposeCodeBO.BENE;
            break;
            case BONU: purposeCodeBO = PurposeCodeBO.BONU;
            break;
            case CCHD: purposeCodeBO = PurposeCodeBO.CCHD;
            break;
            case COMM: purposeCodeBO = PurposeCodeBO.COMM;
            break;
            case CSLP: purposeCodeBO = PurposeCodeBO.CSLP;
            break;
            case GFRP: purposeCodeBO = PurposeCodeBO.GFRP;
            break;
            case GVEA: purposeCodeBO = PurposeCodeBO.GVEA;
            break;
            case GVEB: purposeCodeBO = PurposeCodeBO.GVEB;
            break;
            case GVEC: purposeCodeBO = PurposeCodeBO.GVEC;
            break;
            case GVED: purposeCodeBO = PurposeCodeBO.GVED;
            break;
            case GWLT: purposeCodeBO = PurposeCodeBO.GWLT;
            break;
            case HREC: purposeCodeBO = PurposeCodeBO.HREC;
            break;
            case PAYR: purposeCodeBO = PurposeCodeBO.PAYR;
            break;
            case PEFC: purposeCodeBO = PurposeCodeBO.PEFC;
            break;
            case PENS: purposeCodeBO = PurposeCodeBO.PENS;
            break;
            case PRCP: purposeCodeBO = PurposeCodeBO.PRCP;
            break;
            case RHBS: purposeCodeBO = PurposeCodeBO.RHBS;
            break;
            case SALA: purposeCodeBO = PurposeCodeBO.SALA;
            break;
            case SSBE: purposeCodeBO = PurposeCodeBO.SSBE;
            break;
            case LBIN: purposeCodeBO = PurposeCodeBO.LBIN;
            break;
            case LCOL: purposeCodeBO = PurposeCodeBO.LCOL;
            break;
            case LFEE: purposeCodeBO = PurposeCodeBO.LFEE;
            break;
            case LMEQ: purposeCodeBO = PurposeCodeBO.LMEQ;
            break;
            case LMFI: purposeCodeBO = PurposeCodeBO.LMFI;
            break;
            case LMRK: purposeCodeBO = PurposeCodeBO.LMRK;
            break;
            case LREB: purposeCodeBO = PurposeCodeBO.LREB;
            break;
            case LREV: purposeCodeBO = PurposeCodeBO.LREV;
            break;
            case LSFL: purposeCodeBO = PurposeCodeBO.LSFL;
            break;
            case ESTX: purposeCodeBO = PurposeCodeBO.ESTX;
            break;
            case FWLV: purposeCodeBO = PurposeCodeBO.FWLV;
            break;
            case GSTX: purposeCodeBO = PurposeCodeBO.GSTX;
            break;
            case HSTX: purposeCodeBO = PurposeCodeBO.HSTX;
            break;
            case INTX: purposeCodeBO = PurposeCodeBO.INTX;
            break;
            case NITX: purposeCodeBO = PurposeCodeBO.NITX;
            break;
            case PTXP: purposeCodeBO = PurposeCodeBO.PTXP;
            break;
            case RDTX: purposeCodeBO = PurposeCodeBO.RDTX;
            break;
            case TAXS: purposeCodeBO = PurposeCodeBO.TAXS;
            break;
            case VATX: purposeCodeBO = PurposeCodeBO.VATX;
            break;
            case WHLD: purposeCodeBO = PurposeCodeBO.WHLD;
            break;
            case TAXR: purposeCodeBO = PurposeCodeBO.TAXR;
            break;
            case B112: purposeCodeBO = PurposeCodeBO.B112;
            break;
            case BR12: purposeCodeBO = PurposeCodeBO.BR12;
            break;
            case TLRF: purposeCodeBO = PurposeCodeBO.TLRF;
            break;
            case TLRR: purposeCodeBO = PurposeCodeBO.TLRR;
            break;
            case AIRB: purposeCodeBO = PurposeCodeBO.AIRB;
            break;
            case BUSB: purposeCodeBO = PurposeCodeBO.BUSB;
            break;
            case FERB: purposeCodeBO = PurposeCodeBO.FERB;
            break;
            case RLWY: purposeCodeBO = PurposeCodeBO.RLWY;
            break;
            case TRPT: purposeCodeBO = PurposeCodeBO.TRPT;
            break;
            case CBTV: purposeCodeBO = PurposeCodeBO.CBTV;
            break;
            case ELEC: purposeCodeBO = PurposeCodeBO.ELEC;
            break;
            case ENRG: purposeCodeBO = PurposeCodeBO.ENRG;
            break;
            case GASB: purposeCodeBO = PurposeCodeBO.GASB;
            break;
            case NWCH: purposeCodeBO = PurposeCodeBO.NWCH;
            break;
            case NWCM: purposeCodeBO = PurposeCodeBO.NWCM;
            break;
            case OTLC: purposeCodeBO = PurposeCodeBO.OTLC;
            break;
            case PHON: purposeCodeBO = PurposeCodeBO.PHON;
            break;
            case UBIL: purposeCodeBO = PurposeCodeBO.UBIL;
            break;
            case WTER: purposeCodeBO = PurposeCodeBO.WTER;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + purposeCode );
        }

        return purposeCodeBO;
    }

    protected ChargeBearerBO chargeBearerToChargeBearerBO(ChargeBearer chargeBearer) {
        if ( chargeBearer == null ) {
            return null;
        }

        ChargeBearerBO chargeBearerBO;

        switch ( chargeBearer ) {
            case CRED: chargeBearerBO = ChargeBearerBO.CRED;
            break;
            case DEBT: chargeBearerBO = ChargeBearerBO.DEBT;
            break;
            case SHAR: chargeBearerBO = ChargeBearerBO.SHAR;
            break;
            case SLEV: chargeBearerBO = ChargeBearerBO.SLEV;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + chargeBearer );
        }

        return chargeBearerBO;
    }

    protected Amount amountBOToAmount(AmountBO amountBO) {
        if ( amountBO == null ) {
            return null;
        }

        Amount amount = new Amount();

        if ( amountBO.getCurrency() != null ) {
            amount.setCurrency( amountBO.getCurrency().getCurrencyCode() );
        }
        amount.setAmount( amountBO.getAmount() );

        return amount;
    }

    protected Address addressBOToAddress(AddressBO addressBO) {
        if ( addressBO == null ) {
            return null;
        }

        Address address = new Address();

        address.setStreet( addressBO.getStreet() );
        address.setBuildingNumber( addressBO.getBuildingNumber() );
        address.setCity( addressBO.getCity() );
        address.setPostalCode( addressBO.getPostalCode() );
        address.setCountry( addressBO.getCountry() );
        address.setLine1( addressBO.getLine1() );
        address.setLine2( addressBO.getLine2() );

        return address;
    }

    protected PurposeCode purposeCodeBOToPurposeCode(PurposeCodeBO purposeCodeBO) {
        if ( purposeCodeBO == null ) {
            return null;
        }

        PurposeCode purposeCode;

        switch ( purposeCodeBO ) {
            case BKDF: purposeCode = PurposeCode.BKDF;
            break;
            case BKFE: purposeCode = PurposeCode.BKFE;
            break;
            case BKFM: purposeCode = PurposeCode.BKFM;
            break;
            case BKIP: purposeCode = PurposeCode.BKIP;
            break;
            case BKPP: purposeCode = PurposeCode.BKPP;
            break;
            case CBLK: purposeCode = PurposeCode.CBLK;
            break;
            case CDCB: purposeCode = PurposeCode.CDCB;
            break;
            case CDCD: purposeCode = PurposeCode.CDCD;
            break;
            case CDCS: purposeCode = PurposeCode.CDCS;
            break;
            case CDDP: purposeCode = PurposeCode.CDDP;
            break;
            case CDOC: purposeCode = PurposeCode.CDOC;
            break;
            case CDQC: purposeCode = PurposeCode.CDQC;
            break;
            case ETUP: purposeCode = PurposeCode.ETUP;
            break;
            case FCOL: purposeCode = PurposeCode.FCOL;
            break;
            case MTUP: purposeCode = PurposeCode.MTUP;
            break;
            case ACCT: purposeCode = PurposeCode.ACCT;
            break;
            case CASH: purposeCode = PurposeCode.CASH;
            break;
            case COLL: purposeCode = PurposeCode.COLL;
            break;
            case CSDB: purposeCode = PurposeCode.CSDB;
            break;
            case DEPT: purposeCode = PurposeCode.DEPT;
            break;
            case INTC: purposeCode = PurposeCode.INTC;
            break;
            case LIMA: purposeCode = PurposeCode.LIMA;
            break;
            case NETT: purposeCode = PurposeCode.NETT;
            break;
            case BFWD: purposeCode = PurposeCode.BFWD;
            break;
            case CCIR: purposeCode = PurposeCode.CCIR;
            break;
            case CCPC: purposeCode = PurposeCode.CCPC;
            break;
            case CCPM: purposeCode = PurposeCode.CCPM;
            break;
            case CCSM: purposeCode = PurposeCode.CCSM;
            break;
            case CRDS: purposeCode = PurposeCode.CRDS;
            break;
            case CRPR: purposeCode = PurposeCode.CRPR;
            break;
            case CRSP: purposeCode = PurposeCode.CRSP;
            break;
            case CRTL: purposeCode = PurposeCode.CRTL;
            break;
            case EQPT: purposeCode = PurposeCode.EQPT;
            break;
            case EQUS: purposeCode = PurposeCode.EQUS;
            break;
            case EXPT: purposeCode = PurposeCode.EXPT;
            break;
            case EXTD: purposeCode = PurposeCode.EXTD;
            break;
            case FIXI: purposeCode = PurposeCode.FIXI;
            break;
            case FWBC: purposeCode = PurposeCode.FWBC;
            break;
            case FWCC: purposeCode = PurposeCode.FWCC;
            break;
            case FWSB: purposeCode = PurposeCode.FWSB;
            break;
            case FWSC: purposeCode = PurposeCode.FWSC;
            break;
            case MARG: purposeCode = PurposeCode.MARG;
            break;
            case MBSB: purposeCode = PurposeCode.MBSB;
            break;
            case MBSC: purposeCode = PurposeCode.MBSC;
            break;
            case MGCC: purposeCode = PurposeCode.MGCC;
            break;
            case MGSC: purposeCode = PurposeCode.MGSC;
            break;
            case OCCC: purposeCode = PurposeCode.OCCC;
            break;
            case OPBC: purposeCode = PurposeCode.OPBC;
            break;
            case OPCC: purposeCode = PurposeCode.OPCC;
            break;
            case OPSB: purposeCode = PurposeCode.OPSB;
            break;
            case OPSC: purposeCode = PurposeCode.OPSC;
            break;
            case OPTN: purposeCode = PurposeCode.OPTN;
            break;
            case OTCD: purposeCode = PurposeCode.OTCD;
            break;
            case REPO: purposeCode = PurposeCode.REPO;
            break;
            case RPBC: purposeCode = PurposeCode.RPBC;
            break;
            case RPCC: purposeCode = PurposeCode.RPCC;
            break;
            case RPSB: purposeCode = PurposeCode.RPSB;
            break;
            case RPSC: purposeCode = PurposeCode.RPSC;
            break;
            case RVPO: purposeCode = PurposeCode.RVPO;
            break;
            case SBSC: purposeCode = PurposeCode.SBSC;
            break;
            case SCIE: purposeCode = PurposeCode.SCIE;
            break;
            case SCIR: purposeCode = PurposeCode.SCIR;
            break;
            case SCRP: purposeCode = PurposeCode.SCRP;
            break;
            case SHBC: purposeCode = PurposeCode.SHBC;
            break;
            case SHCC: purposeCode = PurposeCode.SHCC;
            break;
            case SHSL: purposeCode = PurposeCode.SHSL;
            break;
            case SLEB: purposeCode = PurposeCode.SLEB;
            break;
            case SLOA: purposeCode = PurposeCode.SLOA;
            break;
            case SWBC: purposeCode = PurposeCode.SWBC;
            break;
            case SWCC: purposeCode = PurposeCode.SWCC;
            break;
            case SWPT: purposeCode = PurposeCode.SWPT;
            break;
            case SWSB: purposeCode = PurposeCode.SWSB;
            break;
            case SWSC: purposeCode = PurposeCode.SWSC;
            break;
            case TBAS: purposeCode = PurposeCode.TBAS;
            break;
            case TBBC: purposeCode = PurposeCode.TBBC;
            break;
            case TBCC: purposeCode = PurposeCode.TBCC;
            break;
            case TRCP: purposeCode = PurposeCode.TRCP;
            break;
            case AGRT: purposeCode = PurposeCode.AGRT;
            break;
            case AREN: purposeCode = PurposeCode.AREN;
            break;
            case BEXP: purposeCode = PurposeCode.BEXP;
            break;
            case BOCE: purposeCode = PurposeCode.BOCE;
            break;
            case COMC: purposeCode = PurposeCode.COMC;
            break;
            case CPYR: purposeCode = PurposeCode.CPYR;
            break;
            case GDDS: purposeCode = PurposeCode.GDDS;
            break;
            case GDSV: purposeCode = PurposeCode.GDSV;
            break;
            case GSCB: purposeCode = PurposeCode.GSCB;
            break;
            case LICF: purposeCode = PurposeCode.LICF;
            break;
            case MP2B: purposeCode = PurposeCode.MP2B;
            break;
            case POPE: purposeCode = PurposeCode.POPE;
            break;
            case ROYA: purposeCode = PurposeCode.ROYA;
            break;
            case SCVE: purposeCode = PurposeCode.SCVE;
            break;
            case SERV: purposeCode = PurposeCode.SERV;
            break;
            case SUBS: purposeCode = PurposeCode.SUBS;
            break;
            case SUPP: purposeCode = PurposeCode.SUPP;
            break;
            case TRAD: purposeCode = PurposeCode.TRAD;
            break;
            case CHAR: purposeCode = PurposeCode.CHAR;
            break;
            case COMT: purposeCode = PurposeCode.COMT;
            break;
            case MP2P: purposeCode = PurposeCode.MP2P;
            break;
            case ECPG: purposeCode = PurposeCode.ECPG;
            break;
            case ECPR: purposeCode = PurposeCode.ECPR;
            break;
            case ECPU: purposeCode = PurposeCode.ECPU;
            break;
            case EPAY: purposeCode = PurposeCode.EPAY;
            break;
            case CLPR: purposeCode = PurposeCode.CLPR;
            break;
            case COMP: purposeCode = PurposeCode.COMP;
            break;
            case DBTC: purposeCode = PurposeCode.DBTC;
            break;
            case GOVI: purposeCode = PurposeCode.GOVI;
            break;
            case HLRP: purposeCode = PurposeCode.HLRP;
            break;
            case HLST: purposeCode = PurposeCode.HLST;
            break;
            case INPC: purposeCode = PurposeCode.INPC;
            break;
            case INPR: purposeCode = PurposeCode.INPR;
            break;
            case INSC: purposeCode = PurposeCode.INSC;
            break;
            case INSU: purposeCode = PurposeCode.INSU;
            break;
            case INTE: purposeCode = PurposeCode.INTE;
            break;
            case LBRI: purposeCode = PurposeCode.LBRI;
            break;
            case LIFI: purposeCode = PurposeCode.LIFI;
            break;
            case LOAN: purposeCode = PurposeCode.LOAN;
            break;
            case LOAR: purposeCode = PurposeCode.LOAR;
            break;
            case PENO: purposeCode = PurposeCode.PENO;
            break;
            case PPTI: purposeCode = PurposeCode.PPTI;
            break;
            case RELG: purposeCode = PurposeCode.RELG;
            break;
            case RINP: purposeCode = PurposeCode.RINP;
            break;
            case TRFD: purposeCode = PurposeCode.TRFD;
            break;
            case FORW: purposeCode = PurposeCode.FORW;
            break;
            case FXNT: purposeCode = PurposeCode.FXNT;
            break;
            case ADMG: purposeCode = PurposeCode.ADMG;
            break;
            case ADVA: purposeCode = PurposeCode.ADVA;
            break;
            case BCDM: purposeCode = PurposeCode.BCDM;
            break;
            case BCFG: purposeCode = PurposeCode.BCFG;
            break;
            case BLDM: purposeCode = PurposeCode.BLDM;
            break;
            case BNET: purposeCode = PurposeCode.BNET;
            break;
            case CBFF: purposeCode = PurposeCode.CBFF;
            break;
            case CBFR: purposeCode = PurposeCode.CBFR;
            break;
            case CCRD: purposeCode = PurposeCode.CCRD;
            break;
            case CDBL: purposeCode = PurposeCode.CDBL;
            break;
            case CFEE: purposeCode = PurposeCode.CFEE;
            break;
            case CGDD: purposeCode = PurposeCode.CGDD;
            break;
            case CORT: purposeCode = PurposeCode.CORT;
            break;
            case COST: purposeCode = PurposeCode.COST;
            break;
            case CPKC: purposeCode = PurposeCode.CPKC;
            break;
            case DCRD: purposeCode = PurposeCode.DCRD;
            break;
            case DSMT: purposeCode = PurposeCode.DSMT;
            break;
            case DVPM: purposeCode = PurposeCode.DVPM;
            break;
            case EDUC: purposeCode = PurposeCode.EDUC;
            break;
            case FACT: purposeCode = PurposeCode.FACT;
            break;
            case FAND: purposeCode = PurposeCode.FAND;
            break;
            case FCPM: purposeCode = PurposeCode.FCPM;
            break;
            case FEES: purposeCode = PurposeCode.FEES;
            break;
            case GOVT: purposeCode = PurposeCode.GOVT;
            break;
            case ICCP: purposeCode = PurposeCode.ICCP;
            break;
            case IDCP: purposeCode = PurposeCode.IDCP;
            break;
            case IHRP: purposeCode = PurposeCode.IHRP;
            break;
            case INSM: purposeCode = PurposeCode.INSM;
            break;
            case IVPT: purposeCode = PurposeCode.IVPT;
            break;
            case MCDM: purposeCode = PurposeCode.MCDM;
            break;
            case MCFG: purposeCode = PurposeCode.MCFG;
            break;
            case MSVC: purposeCode = PurposeCode.MSVC;
            break;
            case NOWS: purposeCode = PurposeCode.NOWS;
            break;
            case OCDM: purposeCode = PurposeCode.OCDM;
            break;
            case OCFG: purposeCode = PurposeCode.OCFG;
            break;
            case OFEE: purposeCode = PurposeCode.OFEE;
            break;
            case OTHR: purposeCode = PurposeCode.OTHR;
            break;
            case PADD: purposeCode = PurposeCode.PADD;
            break;
            case PTSP: purposeCode = PurposeCode.PTSP;
            break;
            case RCKE: purposeCode = PurposeCode.RCKE;
            break;
            case RCPT: purposeCode = PurposeCode.RCPT;
            break;
            case REBT: purposeCode = PurposeCode.REBT;
            break;
            case REFU: purposeCode = PurposeCode.REFU;
            break;
            case RENT: purposeCode = PurposeCode.RENT;
            break;
            case REOD: purposeCode = PurposeCode.REOD;
            break;
            case RIMB: purposeCode = PurposeCode.RIMB;
            break;
            case RPNT: purposeCode = PurposeCode.RPNT;
            break;
            case RRBN: purposeCode = PurposeCode.RRBN;
            break;
            case RVPM: purposeCode = PurposeCode.RVPM;
            break;
            case SLPI: purposeCode = PurposeCode.SLPI;
            break;
            case SPLT: purposeCode = PurposeCode.SPLT;
            break;
            case STDY: purposeCode = PurposeCode.STDY;
            break;
            case TBAN: purposeCode = PurposeCode.TBAN;
            break;
            case TBIL: purposeCode = PurposeCode.TBIL;
            break;
            case TCSC: purposeCode = PurposeCode.TCSC;
            break;
            case TELI: purposeCode = PurposeCode.TELI;
            break;
            case TMPG: purposeCode = PurposeCode.TMPG;
            break;
            case TPRI: purposeCode = PurposeCode.TPRI;
            break;
            case TPRP: purposeCode = PurposeCode.TPRP;
            break;
            case TRNC: purposeCode = PurposeCode.TRNC;
            break;
            case TRVC: purposeCode = PurposeCode.TRVC;
            break;
            case WEBI: purposeCode = PurposeCode.WEBI;
            break;
            case ANNI: purposeCode = PurposeCode.ANNI;
            break;
            case CAFI: purposeCode = PurposeCode.CAFI;
            break;
            case CFDI: purposeCode = PurposeCode.CFDI;
            break;
            case CMDT: purposeCode = PurposeCode.CMDT;
            break;
            case DERI: purposeCode = PurposeCode.DERI;
            break;
            case DIVD: purposeCode = PurposeCode.DIVD;
            break;
            case FREX: purposeCode = PurposeCode.FREX;
            break;
            case HEDG: purposeCode = PurposeCode.HEDG;
            break;
            case INVS: purposeCode = PurposeCode.INVS;
            break;
            case PRME: purposeCode = PurposeCode.PRME;
            break;
            case SAVG: purposeCode = PurposeCode.SAVG;
            break;
            case SECU: purposeCode = PurposeCode.SECU;
            break;
            case SEPI: purposeCode = PurposeCode.SEPI;
            break;
            case TREA: purposeCode = PurposeCode.TREA;
            break;
            case UNIT: purposeCode = PurposeCode.UNIT;
            break;
            case FNET: purposeCode = PurposeCode.FNET;
            break;
            case FUTR: purposeCode = PurposeCode.FUTR;
            break;
            case ANTS: purposeCode = PurposeCode.ANTS;
            break;
            case CVCF: purposeCode = PurposeCode.CVCF;
            break;
            case DMEQ: purposeCode = PurposeCode.DMEQ;
            break;
            case DNTS: purposeCode = PurposeCode.DNTS;
            break;
            case HLTC: purposeCode = PurposeCode.HLTC;
            break;
            case HLTI: purposeCode = PurposeCode.HLTI;
            break;
            case HSPC: purposeCode = PurposeCode.HSPC;
            break;
            case ICRF: purposeCode = PurposeCode.ICRF;
            break;
            case LTCF: purposeCode = PurposeCode.LTCF;
            break;
            case MAFC: purposeCode = PurposeCode.MAFC;
            break;
            case MARF: purposeCode = PurposeCode.MARF;
            break;
            case MDCS: purposeCode = PurposeCode.MDCS;
            break;
            case VIEW: purposeCode = PurposeCode.VIEW;
            break;
            case CDEP: purposeCode = PurposeCode.CDEP;
            break;
            case SWFP: purposeCode = PurposeCode.SWFP;
            break;
            case SWPP: purposeCode = PurposeCode.SWPP;
            break;
            case SWRS: purposeCode = PurposeCode.SWRS;
            break;
            case SWUF: purposeCode = PurposeCode.SWUF;
            break;
            case ADCS: purposeCode = PurposeCode.ADCS;
            break;
            case AEMP: purposeCode = PurposeCode.AEMP;
            break;
            case ALLW: purposeCode = PurposeCode.ALLW;
            break;
            case ALMY: purposeCode = PurposeCode.ALMY;
            break;
            case BBSC: purposeCode = PurposeCode.BBSC;
            break;
            case BECH: purposeCode = PurposeCode.BECH;
            break;
            case BENE: purposeCode = PurposeCode.BENE;
            break;
            case BONU: purposeCode = PurposeCode.BONU;
            break;
            case CCHD: purposeCode = PurposeCode.CCHD;
            break;
            case COMM: purposeCode = PurposeCode.COMM;
            break;
            case CSLP: purposeCode = PurposeCode.CSLP;
            break;
            case GFRP: purposeCode = PurposeCode.GFRP;
            break;
            case GVEA: purposeCode = PurposeCode.GVEA;
            break;
            case GVEB: purposeCode = PurposeCode.GVEB;
            break;
            case GVEC: purposeCode = PurposeCode.GVEC;
            break;
            case GVED: purposeCode = PurposeCode.GVED;
            break;
            case GWLT: purposeCode = PurposeCode.GWLT;
            break;
            case HREC: purposeCode = PurposeCode.HREC;
            break;
            case PAYR: purposeCode = PurposeCode.PAYR;
            break;
            case PEFC: purposeCode = PurposeCode.PEFC;
            break;
            case PENS: purposeCode = PurposeCode.PENS;
            break;
            case PRCP: purposeCode = PurposeCode.PRCP;
            break;
            case RHBS: purposeCode = PurposeCode.RHBS;
            break;
            case SALA: purposeCode = PurposeCode.SALA;
            break;
            case SSBE: purposeCode = PurposeCode.SSBE;
            break;
            case LBIN: purposeCode = PurposeCode.LBIN;
            break;
            case LCOL: purposeCode = PurposeCode.LCOL;
            break;
            case LFEE: purposeCode = PurposeCode.LFEE;
            break;
            case LMEQ: purposeCode = PurposeCode.LMEQ;
            break;
            case LMFI: purposeCode = PurposeCode.LMFI;
            break;
            case LMRK: purposeCode = PurposeCode.LMRK;
            break;
            case LREB: purposeCode = PurposeCode.LREB;
            break;
            case LREV: purposeCode = PurposeCode.LREV;
            break;
            case LSFL: purposeCode = PurposeCode.LSFL;
            break;
            case ESTX: purposeCode = PurposeCode.ESTX;
            break;
            case FWLV: purposeCode = PurposeCode.FWLV;
            break;
            case GSTX: purposeCode = PurposeCode.GSTX;
            break;
            case HSTX: purposeCode = PurposeCode.HSTX;
            break;
            case INTX: purposeCode = PurposeCode.INTX;
            break;
            case NITX: purposeCode = PurposeCode.NITX;
            break;
            case PTXP: purposeCode = PurposeCode.PTXP;
            break;
            case RDTX: purposeCode = PurposeCode.RDTX;
            break;
            case TAXS: purposeCode = PurposeCode.TAXS;
            break;
            case VATX: purposeCode = PurposeCode.VATX;
            break;
            case WHLD: purposeCode = PurposeCode.WHLD;
            break;
            case TAXR: purposeCode = PurposeCode.TAXR;
            break;
            case B112: purposeCode = PurposeCode.B112;
            break;
            case BR12: purposeCode = PurposeCode.BR12;
            break;
            case TLRF: purposeCode = PurposeCode.TLRF;
            break;
            case TLRR: purposeCode = PurposeCode.TLRR;
            break;
            case AIRB: purposeCode = PurposeCode.AIRB;
            break;
            case BUSB: purposeCode = PurposeCode.BUSB;
            break;
            case FERB: purposeCode = PurposeCode.FERB;
            break;
            case RLWY: purposeCode = PurposeCode.RLWY;
            break;
            case TRPT: purposeCode = PurposeCode.TRPT;
            break;
            case CBTV: purposeCode = PurposeCode.CBTV;
            break;
            case ELEC: purposeCode = PurposeCode.ELEC;
            break;
            case ENRG: purposeCode = PurposeCode.ENRG;
            break;
            case GASB: purposeCode = PurposeCode.GASB;
            break;
            case NWCH: purposeCode = PurposeCode.NWCH;
            break;
            case NWCM: purposeCode = PurposeCode.NWCM;
            break;
            case OTLC: purposeCode = PurposeCode.OTLC;
            break;
            case PHON: purposeCode = PurposeCode.PHON;
            break;
            case UBIL: purposeCode = PurposeCode.UBIL;
            break;
            case WTER: purposeCode = PurposeCode.WTER;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + purposeCodeBO );
        }

        return purposeCode;
    }

    protected ChargeBearer chargeBearerBOToChargeBearer(ChargeBearerBO chargeBearerBO) {
        if ( chargeBearerBO == null ) {
            return null;
        }

        ChargeBearer chargeBearer;

        switch ( chargeBearerBO ) {
            case CRED: chargeBearer = ChargeBearer.CRED;
            break;
            case DEBT: chargeBearer = ChargeBearer.DEBT;
            break;
            case SHAR: chargeBearer = ChargeBearer.SHAR;
            break;
            case SLEV: chargeBearer = ChargeBearer.SLEV;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + chargeBearerBO );
        }

        return chargeBearer;
    }

    private TransactionStatusBO paymentTargetPaymentTransactionStatus(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        TransactionStatusBO transactionStatus = payment.getTransactionStatus();
        if ( transactionStatus == null ) {
            return null;
        }
        return transactionStatus;
    }

    private String paymentTargetPaymentDebtorName(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        String debtorName = payment.getDebtorName();
        if ( debtorName == null ) {
            return null;
        }
        return debtorName;
    }

    private String paymentTargetPaymentDebtorAgent(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        String debtorAgent = payment.getDebtorAgent();
        if ( debtorAgent == null ) {
            return null;
        }
        return debtorAgent;
    }

    private AccountReferenceBO paymentTargetPaymentDebtorAccount(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        AccountReferenceBO debtorAccount = payment.getDebtorAccount();
        if ( debtorAccount == null ) {
            return null;
        }
        return debtorAccount;
    }

    private String paymentTargetPaymentPaymentId(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        String paymentId = payment.getPaymentId();
        if ( paymentId == null ) {
            return null;
        }
        return paymentId;
    }

    private PaymentTypeBO paymentTargetPaymentPaymentType(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        PaymentTypeBO paymentType = payment.getPaymentType();
        if ( paymentType == null ) {
            return null;
        }
        return paymentType;
    }

    private String paymentTargetPaymentPaymentProduct(PaymentTargetBO paymentTargetBO) {
        if ( paymentTargetBO == null ) {
            return null;
        }
        PaymentBO payment = paymentTargetBO.getPayment();
        if ( payment == null ) {
            return null;
        }
        String paymentProduct = payment.getPaymentProduct();
        if ( paymentProduct == null ) {
            return null;
        }
        return paymentProduct;
    }
}
