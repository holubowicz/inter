package com.example.backend.check;

import com.example.backend.check.loader.CheckLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckServiceImpl implements CheckService {

    private final CheckLoader checkLoader;
    private final CheckConnector checkConnector;

    @Autowired
    public CheckServiceImpl(CheckLoader checkLoader, CheckConnector checkConnector) {
        this.checkLoader = checkLoader;
        this.checkConnector = checkConnector;
    }

    @Override
    public List<CheckDto> getCheckDtoList() {
        return checkLoader.getCheckDtoList();
    }

    @Override
    public List<CheckResult> runCheckDtoList(List<CheckDto> checkDtoList) {
        return checkDtoList
                .stream()
                .map(checkLoader::convertCheckDtoToCheck)
                .map(checkConnector::runCheck)
                .collect(Collectors.toList());
    }
}
