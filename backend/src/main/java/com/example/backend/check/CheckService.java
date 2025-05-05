package com.example.backend.check;

import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckHistoryDto;
import com.example.backend.check.model.CheckInputDto;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.factory.CheckHistoryDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.backend.check.common.ErrorMessages.CHECK_INPUT_DTO_LIST_NULL;

@Service
public class CheckService {

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

    public List<CheckHistoryDto> getCheckHistoryDtoList(String checkName) {
        return checkRunner.getCheckHistoryList(checkName).stream()
                .map(CheckHistoryDtoFactory::getCheckHistoryDto)
                .toList();
    }

    public List<CheckResult> runCheckDtoList(List<CheckInputDto> checkInputDtoList) {
        if (checkInputDtoList == null) {
            throw new IllegalArgumentException(CHECK_INPUT_DTO_LIST_NULL);
        }

        return checkInputDtoList
                .stream()
                .map(checkLoader::convertCheckInputDtoToCheck)
                .map(checkRunner::runCheck)
                .toList();
    }

}
