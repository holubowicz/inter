package com.example.backend.check;

import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckInputDto;
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
        return checkLoader.getCheckNameList().stream()
                .map(checkRunner::getCheckDto)
                .toList();
    }

    public List<CheckResult> runCheckDtoList(List<CheckInputDto> checkInputDtoList) {
        if (checkInputDtoList == null) {
            throw new IllegalArgumentException(CHECK_DTO_LIST_NULL_ERROR);
        }

        return checkInputDtoList
                .stream()
                .map(checkLoader::convertCheckInputDtoToCheck)
                .map(checkRunner::runCheck)
                .toList();
    }

}
