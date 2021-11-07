package com.open.paymybuddy.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class MoneyTransaction {

    @Column(name = "senderID", nullable = false)
    @NotBlank
    //@Size(min = 2, max = 6)
    private Integer senderID;

    @Column(name = "receiverID", nullable = false)
    @NotBlank
    private Integer receiverID;

    @Column(name = "amount", nullable = false)
    @NotBlank
    private BigDecimal amount;
}