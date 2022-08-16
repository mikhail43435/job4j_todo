package ru.job4j.todo.service;

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
                toCharArray()).
                get().getMessage()).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        assertThat(validationService.validateUserPassword(" ".toCharArray()).
                get().getMessage()).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        assertThat(validationService.validateUserPassword("dfge56 w5ertrsg".toCharArray()).
                get().getMessage()).
                isEqualTo("Invalid password. Password must contain only digits and letters.");

        char[] arr100 = new char[100];
        Arrays.fill(arr100, 'q');
        assertThat(validationService.validateUserPassword(arr100)).isEmpty();

        char[] arr0 = new char[0];
        assertThat(validationService.
                validateUserPassword(arr0).
                get().getMessage()).
                isEqualTo("Invalid password length.The password length must be in 1-100 range.");

        char[] arr101 = new char[101];
        Arrays.fill(arr100, 'q');
        assertThat(validationService.
                validateUserPassword(arr101).
                get().getMessage()).
                isEqualTo("Invalid password length.The password length must be in 1-100 range.");
    }

    @Test
    void whenValidateTaskName() {
        ValidationService validationService = new ValidationService();
        assertThat(validationService.validateTaskName("1")).isEmpty();
        assertThat(validationService.validateTaskName("q")).isEmpty();
        assertThat(validationService.validateTaskName("Q")).isEmpty();

        assertThat(validationService.validateTaskName(" ").
                get().getMessage()).
                isEqualTo("Invalid task name: < > The task's name cannot be blank.");

        assertThat(validationService.validateTaskName("").
                get().getMessage()).
                isEqualTo("Invalid task name: <> The task's name cannot be blank.");

        char[] arr256 = new char[256];
        Arrays.fill(arr256, 'a');
        assertThat(validationService.validateTaskName(Arrays.toString(arr256)).
                get().getMessage()).
                isEqualTo("Invalid task name: <[a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, "
                        + "a, a, a]> The length of the task's name must be in the range 1 - 255 .");
    }
}