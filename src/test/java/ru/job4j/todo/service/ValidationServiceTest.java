package ru.job4j.todo.service;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ValidationServiceTest {

    @Test
    void whenValidatePassword() {
        ValidationService validationService = new ValidationService();
        assertThat(validationService.validateUserPassword("1".toCharArray())).isEmpty();
        assertThat(validationService.validateUserPassword("z".toCharArray())).isEmpty();
        assertThat(validationService.validateUserPassword("Q".toCharArray())).isEmpty();
        assertThat(validationService.validateUserPassword("q@".
                toCharArray())).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        assertThat(validationService.validateUserPassword(" ".toCharArray())).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        assertThat(validationService.validateUserPassword("dfge56 w5ertrsg".toCharArray())).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        char[] arr100 = new char[100];
        Arrays.fill(arr100, 'q');
        assertThat(validationService.validateUserPassword(arr100)).isEmpty();

        char[] arr0 = new char[0];
        assertThat(validationService.
                validateUserPassword(arr0)).
                isEqualTo("Invalid password length.The password length must be in 1-100 range.");

        char[] arr101 = new char[101];
        Arrays.fill(arr100, 'q');
        assertThat(validationService.
                validateUserPassword(arr101)).
                isEqualTo("Invalid password length.The password length must be in 1-100 range.");
    }
}