package org.example.empApi.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class SalaryService  {

    @HystrixCommand(fallbackMethod = "defaultSalary")
    public long updateSalary(long salary)
    {

        return salary*2;
    }

    public long defaultSalary(long salary,Throwable t)
    {
        return 100L;
    }

}
