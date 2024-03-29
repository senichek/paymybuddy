package com.open.paymybuddy.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class MoneyTransaction extends BaseEntity {

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "amount", nullable = false)
    @NotNull
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "sender")
    @NotNull
    @ToString.Exclude
    /* This field results in error or exception when used in ToString method due to Lazy fetching.
    Removing it from ToString() method */
    private Person sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToString.Exclude
    private Person receiver;

    @Column(name = "sender_email", nullable = false)
    @NotBlank
    private String senderEmail;

    @Column(name = "receiver_email", nullable = false)
    @NotBlank
    private String receiverEmail;

    @Column(name = "tax", nullable = false)
    @NotNull
    private BigDecimal tax;

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;
}