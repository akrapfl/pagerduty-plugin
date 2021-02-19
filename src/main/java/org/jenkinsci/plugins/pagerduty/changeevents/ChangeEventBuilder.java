package org.jenkinsci.plugins.pagerduty.changeevents;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.Run;
import hudson.tasks.Builder;
import hudson.model.TaskListener;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * A build step for recording a PagerDuty Change Event.
 *
 * This is intended to be used as a post-build action. It takes details from the
 * build and makes an API call to PagerDuty's Change Events API.
 *
 * See https://developer.pagerduty.com/docs/events-api-v2/send-change-events/
 */
// public class NessusScanBuilder extends Builder {
public class ChangeEventBuilder extends Builder  {
    /**
     * The integration key that identifies the service the change occurred on.
     */
    private final String integrationKey;

    /**
     * Should a change event be created on successful builds.
     */
    private boolean createOnSuccess;

    /**
     * Should a change event be created on failed builds.
     */
    private boolean createOnFailure;

    /**
     * Should a change event be created on unstable builds.
     */
    private boolean createOnUnstable;

    /**
     * Should a change event be created on aborted builds.
     */
    private boolean createOnAborted;

    /**
     * Should a change event be created on not built builds.
     */
    private boolean createOnNotBuilt;
    /**
     * custom event data that can be passed on to set as summary
     */
    private   String summaryText ;

    @DataBoundConstructor
    public ChangeEventBuilder(String integrationKey) {
        this.integrationKey = integrationKey;
    }


    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
	// public void    perform(Run<?, ?> build, @Nonnull FilePath workspace, @Nonnull Launcher launcher,
	// @Nonnull TaskListener listener) {
        Result result = build.getResult();

        if ((result == Result.SUCCESS && createOnSuccess) || (result == Result.FAILURE && createOnFailure)
                || (result == Result.UNSTABLE && createOnUnstable) || (result == Result.ABORTED && createOnAborted)
                || (result == Result.NOT_BUILT && createOnNotBuilt)) {
        	
        	
        	new ChangeEventSender().send(integrationKey, summaryText, build, listener);
        	
        }
	return true;
    }
// nessus
    // @Override
    // public DescriptorImpl getDescriptor() {
    //     return (DescriptorImpl)super.getDescriptor();
    // }

 // original
    // public static DescriptorImpl descriptor() {
    //     return Jenkins.get().getDescriptorByType(ChangeEventBuilder.DescriptorImpl.class);
    // }

    public String getIntegrationKey() {
        return integrationKey;
    }
    
    public String getSummaryText() {
	    return summaryText;
	  }

    public boolean getCreateOnSuccess() {
        return createOnSuccess;
    }

    @DataBoundSetter
    public void setCreateOnSuccess(boolean createOnSuccess) {
        this.createOnSuccess = createOnSuccess;
    }

    public boolean getCreateOnFailure() {
        return createOnFailure;
    }

    @DataBoundSetter
    public void setCreateOnFailure(boolean createOnFailure) {
        this.createOnFailure = createOnFailure;
    }

    public boolean getCreateOnUnstable() {
        return createOnUnstable;
    }

    @DataBoundSetter
    public void setCreateOnUnstable(boolean createOnUnstable) {
        this.createOnUnstable = createOnUnstable;
    }

    public boolean getCreateOnAborted() {
        return createOnAborted;
    }

    @DataBoundSetter
    public void setCreateOnAborted(boolean createOnAborted) {
        this.createOnAborted = createOnAborted;
    }

    public boolean getCreateOnNotBuilt() {
        return createOnNotBuilt;
    }

    @DataBoundSetter
    public void setCreateOnNotBuilt(boolean createOnNotBuilt) {
        this.createOnNotBuilt = createOnNotBuilt;
    }
    
    @DataBoundSetter
    public void setSummaryText(String summaryText) {
  	  this.summaryText = summaryText;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        @Nonnull
        public String getDisplayName() {
            return "PagerDuty Change Event Builder";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        /**
         * Provides basic validation of integration keys.
         *
         * Just ensures they are the correct length and only include allowed characters.
         *
         * @param value The integration key
         * @return Whether or not the integration key is valid
         */
        public FormValidation doCheckIntegrationKey(@QueryParameter String value) {
            Pattern pattern = Pattern.compile("^[0-9a-z]{32}$");
            Matcher matcher = pattern.matcher(value);

            if (matcher.matches()) {
                return FormValidation.ok();
            }

            if (value.length() != 32) {
                return FormValidation.error("Must be 32 characters long");
            }

            return FormValidation.error("Must only be letters and digits");
        }
    }
}
