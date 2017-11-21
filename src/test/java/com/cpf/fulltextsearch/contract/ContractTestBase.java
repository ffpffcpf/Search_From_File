package com.cpf.fulltextsearch.contract;

import com.github.macdao.moscow.ContractAssertion;
import com.github.macdao.moscow.ContractContainer;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractTestBase {
    private static final ContractContainer CONTAINER = new ContractContainer(Paths.get("contracts/api"));

    private final TestName name = new TestName();

    @Value("${local.server.port}")
    private int port;

    public int getPort() {
        return port;
    }

    @Rule
    public TestName getName() {
        return name;
    }

    protected Map<String, String> assertContract() {
        return assertContract(getName().getMethodName());
    }

    protected Map<String, String> assertContract(String description) {
        return new ContractAssertion(CONTAINER.findContracts(description))
                .setRestExecutor(new RestTemplateExecutor())
                .setPort(getPort())
                .setNecessity(true)
                .assertContract();
    }
}
