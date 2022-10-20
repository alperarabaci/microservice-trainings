package com.training.food.ordering.payment.service.domain;

import com.training.food.ordering.payment.service.domain.entity.CreditEntity;
import com.training.food.ordering.payment.service.domain.entity.CreditHistory;
import com.training.food.ordering.payment.service.domain.entity.Payment;
import com.training.food.ordering.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntity creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntity creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages);
}
