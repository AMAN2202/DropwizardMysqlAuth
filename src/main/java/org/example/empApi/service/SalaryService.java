package org.example.empApi.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.example.empApi.resources.EmployeeResource;

public class SalaryService extends HystrixCommand<String> {
    private final Long salary;
    EmployeeResource employeeResource[];


    public SalaryService(Long salary) {
        super(HystrixCommandGroupKey.Factory.asKey("SalService"));
        this.salary = salary;
    }

    @Override
    protected String run() throws Exception {

//        change to allow or block service dummy nullpointerexception
        boolean allowService = true;
//        TimeUnit.SECONDS.sleep(1);

//        can use json for serialijing and deserializing for objects
        Long finalSalary = 0L;

        if (allowService) {
            return Long.toString(finalSalary);
        } else
            throw new NullPointerException();

    }

    @Override
    protected String getFallback() {

        //when service is not reachable this will be returned
        return String.valueOf(-100);
    }
}
