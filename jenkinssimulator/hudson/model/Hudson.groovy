package hudson.model

@Singleton
class Hudson {
	def jobs = []

	def setJobs(jobs) {
		this.jobs = jobs
	}

	def getItems() {
		return this.jobs
	}
}