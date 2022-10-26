package com.training.food.ordering.payment.service.dataaccess.credithistory.mapper;

import com.training.food.ordering.domain.valueobject.CustomerId;
import com.training.food.ordering.domain.valueobject.Money;
import com.training.food.ordering.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;
import com.training.food.ordering.payment.service.domain.entity.CreditHistoryId;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {

    public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return CreditHistory.builder()
                .id(new CreditHistoryId(creditHistoryEntity.getId()))
                .customerId(new CustomerId(creditHistoryEntity.getCustomerId()))
                .amount(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
        return CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerId(creditHistory.getCustomerId().getValue())
                .amount(creditHistory.getAmount().getAmount())
                .type(creditHistory.getTransactionType())
                .build();
    }

}
