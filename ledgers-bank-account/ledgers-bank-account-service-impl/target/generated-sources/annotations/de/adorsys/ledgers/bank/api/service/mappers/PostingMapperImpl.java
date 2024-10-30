package de.adorsys.ledgers.bank.api.service.mappers;

import de.adorsys.ledgers.postings.api.domain.LedgerAccountBO;
import de.adorsys.ledgers.postings.api.domain.LedgerBO;
import de.adorsys.ledgers.postings.api.domain.PostingBO;
import de.adorsys.ledgers.postings.api.domain.PostingLineBO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-30T08:25:26+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Ubuntu)"
)
@Component
public class PostingMapperImpl implements PostingMapper {

    @Override
    public PostingBO buildPosting(LocalDateTime pstTime, String paymentId, String oprDetails, LedgerBO ledger, String userName) {
        if ( pstTime == null && paymentId == null && oprDetails == null && ledger == null && userName == null ) {
            return null;
        }

        PostingBO postingBO = new PostingBO();

        if ( pstTime != null ) {
            postingBO.setOprTime( pstTime );
            postingBO.setPstTime( pstTime );
            postingBO.setValTime( pstTime );
        }
        postingBO.setOprSrc( paymentId );
        postingBO.setOprDetails( oprDetails );
        postingBO.setLedger( ledger );
        postingBO.setRecordUser( userName );
        postingBO.setOprId( id() );
        postingBO.setId( null );
        postingBO.setPstType( de.adorsys.ledgers.postings.api.domain.PostingTypeBO.BUSI_TX );
        postingBO.setPstStatus( de.adorsys.ledgers.postings.api.domain.PostingStatusBO.POSTED );

        return postingBO;
    }

    @Override
    public PostingLineBO buildPostingLine(String lineDetails, LedgerAccountBO ledgerAccount, BigDecimal debitAmount, BigDecimal creditAmount, String subOprSrcId, String lineId) {
        if ( lineDetails == null && ledgerAccount == null && debitAmount == null && creditAmount == null && subOprSrcId == null && lineId == null ) {
            return null;
        }

        PostingLineBO postingLineBO = new PostingLineBO();

        postingLineBO.setDetails( lineDetails );
        postingLineBO.setAccount( ledgerAccount );
        postingLineBO.setDebitAmount( debitAmount );
        postingLineBO.setCreditAmount( creditAmount );
        postingLineBO.setSubOprSrcId( subOprSrcId );
        postingLineBO.setId( lineId );

        return postingLineBO;
    }
}
