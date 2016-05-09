package chrislovecnm.k8s.gpmr.cucumber;

import org.junit.runner.RunWith;


import chrislovecnm.k8s.gpmr.AbstractCassandraTest;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/features")
public class CucumberTest extends AbstractCassandraTest {

}
