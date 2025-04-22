package com.example.backend.check;

import java.util.List;

public interface CheckService {

    List<CheckDto> getCheckDtoList();

    List<CheckResult> runCheckDtoList(List<CheckDto> checkDtoList);

}
