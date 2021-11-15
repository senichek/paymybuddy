package com.open.paymybuddy.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "users")
public class Person extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 25)
    private String name;

    @Column(name = "email", nullable = false)
    @NotBlank
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 250)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "owner")
    @ToString.Exclude
    /* This field results in error or exception when used in ToString method due to Lazy fetching.
    Removing it from ToString() method */
    private List<PersonConnection> connections;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "sender")
    private List<MoneyTransaction> transactions;
}
