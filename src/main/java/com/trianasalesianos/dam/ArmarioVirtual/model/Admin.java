package com.trianasalesianos.dam.ArmarioVirtual.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
public class Admin extends Usuario{
}
