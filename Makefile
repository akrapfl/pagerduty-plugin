.PHONY: testjob

testjob:
	mkdir -p work/jobs/test
	cp pagerduty_test_job.xml work/jobs/test/config.xml
