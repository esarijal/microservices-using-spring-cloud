package com.mitrais.microservices.netflixzuulapigatewayserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class AbstractModel<T, ID> extends Auditable<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID id;
}
