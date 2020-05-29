# DropwizardAuth

How to start the DropwizardAuth application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/DropwizardAuth-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Sql schema for user and roles   

    user
      email varcahar(30) primary key
      password varchar(30)
      
    roles 
      email varchar(30) forign key 
      role varchar(30)  
        

How to work with SampleTestModule
---

1. Write the test cases in the src/main/java of the module(SampleTestModule in our case).

2. Create and install JAR file for Module in local Maven repository.
```
    > cd ./target/classes/
    > jar cvf sample-rules-1.0.x.jar ./com/sahajjain/rules/ 
    > mvn install:install-file -Dfile=./sample-rules-1.0.x.jar -DgroupId=com.sahajjain -DartifactId=sample-rules -Dversion=1.0.x -Dpackaging=jar -DgeneratePom=true
```

3. Add the jar file as dependency in the arch-unit-maven-plugin in pom.xml of service module.

4. Add the checks we want to apply through Configurable rules as follows:
```
  <configurableRules>
      <configurableRule>
          <rule>com.sahajjain.rules.sampleCheck</rule>
          <applyOn>
              <packageName>com.sahajjain</packageName>
              <scope>test</scope>
          </applyOn>
          <checks>
              <!-- otherwise you can specify either field or method names here. If no checks block is defined, all are executed -->
              <check>check1</check>
          </checks>
      </configurableRule>
  </configurableRules>
```
here <scope> in the <applyOn> section defines the scope of the checks in the package means if it is ‘main’ then checks will be applied to the src/main/java/ ‘packageName’, otherwise if it is ‘test’ then checks will be applied to the src/test/java/ ‘packageName’.

5. After that, simply run `mvn test` in the terminal in the project directory to check for archrules.

