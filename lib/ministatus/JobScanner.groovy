package ministatus

class JobScanner {
	
	def folderType
	def jobTypes
	
	JobScanner(folderType, jobTypes) {
		this.folderType = folderType
		this.jobTypes = jobTypes
	}
	
	def directSubfolders(tree, parentFolder) {
		def folderRegex = parentFolder + "/[^/]*"
		def newTree = []
		tree.each {
			directSubfoldersRecursive(it, newTree, folderRegex)
		}
		return newTree
	}
	
	def directSubfoldersRecursive(job, tree, folderRegex) {
		println job.name
		if (!job.isDir) {
			return
		}
		if (job.name ==~ folderRegex) {
			tree.add(job)
			return
		}
		job.items.each { subItem -> 
			directSubfoldersRecursive(subItem, tree, folderRegex)
		}
	}

	def prefixFilter(list, folderPrefix) {
		return list.findAll{ it.startsWith(folderPrefix) }
	}
	
	def flatten(tree) {
		def flat = []
		flatten(tree, flat)
		return flat
	}
	
	def flatten_internal(tree, flat) {
		tree.each{ key, value -> 
			flat.add(key)
			value.isDir && flatten_internal(value.items, flat)
		}
	}
	
	def getJobTree() {
		def allJobs = hudson.model.Hudson.getInstance().getItems()

		def jobTree = []
		allJobs.each {
			visitJobTree(it, jobTree)
		}
		return jobTree
	}

	def visitJobTree(job, tree) {
		if (jobTypes.any{ it.isAssignableFrom(job.getClass()) }) {
			tree.add([name: job.getFullName(), isDir: false])
		} else if (this.folderType.isAssignableFrom(job.getClass())) {
			def subTree = []
			job.getItems().each{ folderJob -> 
				visitJobTree(folderJob, subTree)
			}
			tree.add([name: job.getFullName(), isDir: true, items: subTree])
		}
	}
}