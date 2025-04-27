package com.example.backend.check;

import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckService {

    public static final String CHECK_DTO_LIST_NULL_ERROR = "Check DTO list is null";

    private final CheckLoader checkLoader;
    private final CheckRunner checkRunner;

    @Autowired
    public CheckService(CheckLoader checkLoader, CheckRunner checkRunner) {
        this.checkLoader = checkLoader;
        this.checkRunner = checkRunner;
    }

    public List<CheckDto> getCheckDtoList() {
        return checkLoader.getCheckDtoList();
    }

    public List<CheckResult> runCheckDtoList(List<CheckDto> checkDtoList) {
        if (checkDtoList == null) {
            throw new IllegalArgumentException(CHECK_DTO_LIST_NULL_ERROR);
        }

        return checkDtoList
                .stream()
                .map(checkLoader::convertCheckDtoToCheck)
                .map(checkRunner::runCheck)
                .toList();
    }

}
