package org.jenkinsci.plugins.workflow.job

class WorkflowJob {

	def name

	def WorkflowJob(name) {
		this.name = name
	}
		
	def getFullName() {
		return this.name
	}
}