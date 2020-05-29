package org.example.empApi.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SalaryService {

    @HystrixCommand(fallbackMethod = "defaultSalary")
    public long updateSalary(long salary) {


        if (true)
            throw new RuntimeException("dummy exception");
        return salary * 2;
    }

    private long defaultSalary(long salary, Throwable throwable) {
        return 100L;
    }

}
