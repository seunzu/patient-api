package com.inspline.patient_api.global.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("prod")
public class ProdDataInitializer implements ApplicationRunner {

    private final DummyDataLoader dummyDataLoader;

    @Override
    public void run(ApplicationArguments args) {
        dummyDataLoader.load();
    }
}