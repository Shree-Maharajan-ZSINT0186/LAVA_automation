package StepDef.Web;

import Helpers.ScreenShotCapture;
import Helpers.WebActions;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestStepStarted;
import io.cucumber.plugin.event.TestStep;
import io.cucumber.plugin.event.TestStepFinished;

public class StepAndScenarioNameListener implements ConcurrentEventListener {
	WebActions actions= new WebActions();
	String scenarioName = null;
	String stepName = null;

	private EventHandler<TestCaseStarted> testCaseStartedHandler;
	private EventHandler<TestStepStarted> testStepStartedHandler;
	private EventHandler<TestCaseFinished> testCaseFinishedHandler;
	private EventHandler<TestStepFinished> testStepFinishedHandler;

	public StepAndScenarioNameListener() { this.testCaseStartedHandler
		=this::handleTestCaseStarted; this.testStepStartedHandler =
		this::handleTestStepStarted; this.testCaseFinishedHandler =
		this::handleTestCaseFinished; this.testStepFinishedHandler =
		this::handleTestStepFinished; }

	@Override
	public void setEventPublisher(EventPublisher publisher) {
		publisher.registerHandlerFor(TestCaseStarted.class, testCaseStartedHandler);
		publisher.registerHandlerFor(TestStepStarted.class, testStepStartedHandler);
		publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedHandler);
		publisher.registerHandlerFor(TestStepFinished.class, testStepFinishedHandler);
	}

	public void handleTestStepStarted(TestStepStarted event) {
		TestStep testStep = event.getTestStep();
		if (testStep instanceof PickleStepTestStep) {
			PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
			String stepName = pickleStep.getStep().getText();

			WebActions.stepName = stepName;
		}
	}

	private void handleTestCaseFinished(TestCaseFinished event) {
		scenarioName = event.getTestCase().getName();
		
	}

	private void handleTestStepFinished(TestStepFinished event) {
		TestStep testStep = event.getTestStep();
		if (testStep instanceof PickleStepTestStep) {
			PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
			stepName = pickleStep.getStep().getText();
		}
	}
	

	private void handleTestCaseStarted(TestCaseStarted testcasestarted1) {
	}
}

