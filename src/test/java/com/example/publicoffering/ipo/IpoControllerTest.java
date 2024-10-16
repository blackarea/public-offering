package com.example.publicoffering.ipo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class IpoControllerTest {

    @Autowired
    IpoController ipoController;

    @Test
    void test1() throws IOException {
        ipoController.getAll();
    }
}