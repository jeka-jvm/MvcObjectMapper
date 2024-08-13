package org.example.mvcobjectmapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank(message = "Имя должно быть заполнено")
    private String firstName;

    @NotBlank(message = "Фамилия должна быть заполнена")
    private String lastName;

    @Email(message = "Email не корректен")
    private String email;

    @NotBlank(message = "Контактный номер должен быть заполнен")
    private String contactNumber;

}
