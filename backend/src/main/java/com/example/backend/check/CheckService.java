package com.example.backend.check;

import com.example.backend.check.common.exception.CheckInputDTOListNullException;
import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.CheckInputDTO;
import com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckService {

    private final CheckLoader checkLoader;
    private final CheckRunner checkRunner;

    @Autowired
    public CheckService(CheckLoader checkLoader, CheckRunner checkRunner) {
        this.checkLoader = checkLoader;
        this.checkRunner = checkRunner;
    }

    public List<CheckDTO> getCheckDTOs() {
        return checkLoader.getCheckNames().stream()
                .map(checkRunner::getCheckDTO)
                .toList();
    }

    public List<CheckExecutionDTO> getCheckExecutionDTOs(String checkName) {
        return checkRunner.getCheckExecutions(checkName).stream()
                .map(CheckExecutionDTOFactory::createCheckExecutionDTO)
                .toList();
    }

    public List<CheckResult> runCheckDTOs(List<CheckInputDTO> checkInputDTOs) {
        if (checkInputDTOs == null) {
            throw new CheckInputDTOListNullException();
        }

        return checkInputDTOs
                .stream()
                .map(checkLoader::convertIntoCheck)
                .map(checkRunner::runCheck)
                .toList();
    }

}
