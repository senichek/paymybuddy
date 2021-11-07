package com.open.paymybuddy.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Person extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 25)
    private String name;

    @Column(name = "email", nullable = false)
    @NotBlank
    @Size(min = 2, max = 25)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    @Column(name = "balance", nullable = false)
    @NotBlank
    @Size(min = 2, max = 25)
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "person")
    private List<PersonConnection> connections;

    /* @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "person")
    private List<MoneyTransaction> transactions; */
}
