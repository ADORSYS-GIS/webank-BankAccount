package de.adorsys.ledgers.bank.api.service.mappers;

import de.adorsys.ledgers.bank.api.domain.AccountReferenceBO;
import de.adorsys.ledgers.bank.api.domain.AccountTypeBO;
import de.adorsys.ledgers.bank.api.domain.AccountUsageBO;
import de.adorsys.ledgers.bank.api.domain.BankAccountBO;
import de.adorsys.ledgers.bank.db.domain.AccountReference;
import de.adorsys.ledgers.bank.db.domain.AccountType;
import de.adorsys.ledgers.bank.db.domain.AccountUsage;
import de.adorsys.ledgers.bank.db.domain.BankAccount;
import java.util.ArrayList;
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
public class BankAccountMapperImpl implements BankAccountMapper {

    @Override
    public BankAccountBO toBankAccountBO(BankAccount BankAccount) {
        if ( BankAccount == null ) {
            return null;
        }

        BankAccountBO.BankAccountBOBuilder bankAccountBO = BankAccountBO.builder();

        bankAccountBO.id( BankAccount.getId() );
        bankAccountBO.iban( BankAccount.getIban() );
        bankAccountBO.msisdn( BankAccount.getMsisdn() );
        if ( BankAccount.getCurrency() != null ) {
            bankAccountBO.currency( Currency.getInstance( BankAccount.getCurrency() ) );
        }
        bankAccountBO.name( BankAccount.getName() );
        bankAccountBO.displayName( BankAccount.getDisplayName() );
        bankAccountBO.product( BankAccount.getProduct() );
        bankAccountBO.accountType( accountTypeToAccountTypeBO( BankAccount.getAccountType() ) );
        bankAccountBO.bic( BankAccount.getBic() );
        bankAccountBO.linkedAccounts( BankAccount.getLinkedAccounts() );
        bankAccountBO.usageType( accountUsageToAccountUsageBO( BankAccount.getUsageType() ) );
        bankAccountBO.details( BankAccount.getDetails() );
        bankAccountBO.blocked( BankAccount.isBlocked() );
        bankAccountBO.systemBlocked( BankAccount.isSystemBlocked() );
        bankAccountBO.branch( BankAccount.getBranch() );
        bankAccountBO.created( BankAccount.getCreated() );
        bankAccountBO.creditLimit( BankAccount.getCreditLimit() );

        return bankAccountBO.build();
    }

    @Override
    public List<BankAccountBO> toBankAccountListBO(List<BankAccount> list) {
        if ( list == null ) {
            return null;
        }

        List<BankAccountBO> list1 = new ArrayList<BankAccountBO>( list.size() );
        for ( BankAccount bankAccount : list ) {
            list1.add( toBankAccountBO( bankAccount ) );
        }

        return list1;
    }

    @Override
    public BankAccount toBankAccount(BankAccountBO BankAccount) {
        if ( BankAccount == null ) {
            return null;
        }

        BankAccount bankAccount = new BankAccount();

        bankAccount.setId( BankAccount.getId() );
        bankAccount.setIban( BankAccount.getIban() );
        bankAccount.setMsisdn( BankAccount.getMsisdn() );
        if ( BankAccount.getCurrency() != null ) {
            bankAccount.setCurrency( BankAccount.getCurrency().getCurrencyCode() );
        }
        bankAccount.setName( BankAccount.getName() );
        bankAccount.setDisplayName( BankAccount.getDisplayName() );
        bankAccount.setProduct( BankAccount.getProduct() );
        bankAccount.setBranch( BankAccount.getBranch() );
        bankAccount.setAccountType( accountTypeBOToAccountType( BankAccount.getAccountType() ) );
        bankAccount.setBic( BankAccount.getBic() );
        bankAccount.setLinkedAccounts( BankAccount.getLinkedAccounts() );
        bankAccount.setUsageType( accountUsageBOToAccountUsage( BankAccount.getUsageType() ) );
        bankAccount.setDetails( BankAccount.getDetails() );
        bankAccount.setBlocked( BankAccount.isBlocked() );
        bankAccount.setSystemBlocked( BankAccount.isSystemBlocked() );
        bankAccount.setCreated( BankAccount.getCreated() );
        bankAccount.setCreditLimit( BankAccount.getCreditLimit() );

        return bankAccount;
    }

    @Override
    public AccountReferenceBO toAccountReferenceBO(BankAccount BankAccount) {
        if ( BankAccount == null ) {
            return null;
        }

        AccountReferenceBO accountReferenceBO = new AccountReferenceBO();

        accountReferenceBO.setIban( BankAccount.getIban() );
        accountReferenceBO.setMsisdn( BankAccount.getMsisdn() );
        if ( BankAccount.getCurrency() != null ) {
            accountReferenceBO.setCurrency( Currency.getInstance( BankAccount.getCurrency() ) );
        }

        return accountReferenceBO;
    }

    @Override
    public AccountReference toAccountReference(AccountReferenceBO reference) {
        if ( reference == null ) {
            return null;
        }

        AccountReference accountReference = new AccountReference();

        accountReference.setIban( reference.getIban() );
        accountReference.setBban( reference.getBban() );
        accountReference.setPan( reference.getPan() );
        accountReference.setMaskedPan( reference.getMaskedPan() );
        accountReference.setMsisdn( reference.getMsisdn() );
        if ( reference.getCurrency() != null ) {
            accountReference.setCurrency( reference.getCurrency().getCurrencyCode() );
        }

        return accountReference;
    }

    protected AccountTypeBO accountTypeToAccountTypeBO(AccountType accountType) {
        if ( accountType == null ) {
            return null;
        }

        AccountTypeBO accountTypeBO;

        switch ( accountType ) {
            case CACC: accountTypeBO = AccountTypeBO.CACC;
            break;
            case CASH: accountTypeBO = AccountTypeBO.CASH;
            break;
            case CHAR: accountTypeBO = AccountTypeBO.CHAR;
            break;
            case CISH: accountTypeBO = AccountTypeBO.CISH;
            break;
            case COMM: accountTypeBO = AccountTypeBO.COMM;
            break;
            case CPAC: accountTypeBO = AccountTypeBO.CPAC;
            break;
            case LLSV: accountTypeBO = AccountTypeBO.LLSV;
            break;
            case LOAN: accountTypeBO = AccountTypeBO.LOAN;
            break;
            case MGLD: accountTypeBO = AccountTypeBO.MGLD;
            break;
            case MOMA: accountTypeBO = AccountTypeBO.MOMA;
            break;
            case NREX: accountTypeBO = AccountTypeBO.NREX;
            break;
            case ODFT: accountTypeBO = AccountTypeBO.ODFT;
            break;
            case ONDP: accountTypeBO = AccountTypeBO.ONDP;
            break;
            case OTHR: accountTypeBO = AccountTypeBO.OTHR;
            break;
            case SACC: accountTypeBO = AccountTypeBO.SACC;
            break;
            case SLRY: accountTypeBO = AccountTypeBO.SLRY;
            break;
            case SVGS: accountTypeBO = AccountTypeBO.SVGS;
            break;
            case TAXE: accountTypeBO = AccountTypeBO.TAXE;
            break;
            case TRAN: accountTypeBO = AccountTypeBO.TRAN;
            break;
            case TRAS: accountTypeBO = AccountTypeBO.TRAS;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountType );
        }

        return accountTypeBO;
    }

    protected AccountUsageBO accountUsageToAccountUsageBO(AccountUsage accountUsage) {
        if ( accountUsage == null ) {
            return null;
        }

        AccountUsageBO accountUsageBO;

        switch ( accountUsage ) {
            case PRIV: accountUsageBO = AccountUsageBO.PRIV;
            break;
            case ORGA: accountUsageBO = AccountUsageBO.ORGA;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountUsage );
        }

        return accountUsageBO;
    }

    protected AccountType accountTypeBOToAccountType(AccountTypeBO accountTypeBO) {
        if ( accountTypeBO == null ) {
            return null;
        }

        AccountType accountType;

        switch ( accountTypeBO ) {
            case CACC: accountType = AccountType.CACC;
            break;
            case CASH: accountType = AccountType.CASH;
            break;
            case CHAR: accountType = AccountType.CHAR;
            break;
            case CISH: accountType = AccountType.CISH;
            break;
            case COMM: accountType = AccountType.COMM;
            break;
            case CPAC: accountType = AccountType.CPAC;
            break;
            case LLSV: accountType = AccountType.LLSV;
            break;
            case LOAN: accountType = AccountType.LOAN;
            break;
            case MGLD: accountType = AccountType.MGLD;
            break;
            case MOMA: accountType = AccountType.MOMA;
            break;
            case NREX: accountType = AccountType.NREX;
            break;
            case ODFT: accountType = AccountType.ODFT;
            break;
            case ONDP: accountType = AccountType.ONDP;
            break;
            case OTHR: accountType = AccountType.OTHR;
            break;
            case SACC: accountType = AccountType.SACC;
            break;
            case SLRY: accountType = AccountType.SLRY;
            break;
            case SVGS: accountType = AccountType.SVGS;
            break;
            case TAXE: accountType = AccountType.TAXE;
            break;
            case TRAN: accountType = AccountType.TRAN;
            break;
            case TRAS: accountType = AccountType.TRAS;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountTypeBO );
        }

        return accountType;
    }

    protected AccountUsage accountUsageBOToAccountUsage(AccountUsageBO accountUsageBO) {
        if ( accountUsageBO == null ) {
            return null;
        }

        AccountUsage accountUsage;

        switch ( accountUsageBO ) {
            case PRIV: accountUsage = AccountUsage.PRIV;
            break;
            case ORGA: accountUsage = AccountUsage.ORGA;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountUsageBO );
        }

        return accountUsage;
    }
}
