package org.jenkinsci.plugins.pagerduty.changeevents;

import hudson.model.*;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.HudsonTestCase;
import hudson.tasks.Shell;
import org.apache.commons.io.FileUtils;

public class ChangeEventBuilderTest extends HudsonTestCase {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    final String integrationKey = "Test integration key";

    @Test
    public void testConfigRoundtrip() throws Exception {

	FreeStyleProject project = createFreeStyleProject();
        ChangeEventBuilder before = new ChangeEventBuilder(integrationKey);
        project.getBuildersList().add(new Shell("echo hello"));
	project.getBuildersList().add(before);

	submit(createWebClient().getPage(project,"configure").getFormByName("config"));

	ChangeEventBuilder after = project.getBuildersList().get(ChangeEventBuilder.class);

	// assertEqualBeans(before,after);
    }
}
