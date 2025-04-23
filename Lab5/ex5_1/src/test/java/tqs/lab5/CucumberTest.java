package tqs.lab5;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("calculator.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tqs.lab5")
public class CucumberTest {
}