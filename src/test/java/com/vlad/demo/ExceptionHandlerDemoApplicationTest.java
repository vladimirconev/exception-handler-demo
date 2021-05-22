package com.vlad.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionHandlerDemoApplicationTest {

    @LocalServerPort
    protected int port;

    @Test
    public void getTaskTest(){
        given().when().
                port(this.port).
                get("/v1/task/1243").
                then().assertThat().
                statusCode(is(409));
    }

}