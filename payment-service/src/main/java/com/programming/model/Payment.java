package com.programming.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    Long amount;
    Long amountCapturable;
    Long amountReceived;
    Long applicationFeeAmount;
    Long canceledAt;
    String cancellationReason;
    String captureMethod;
    String clientSecret;
    String confirmationMethod;
    Long created;
    String currency;
    String description;
    String receiptEmail;
    String status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}