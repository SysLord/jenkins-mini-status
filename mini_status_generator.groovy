package de.syslord.jenkins.ministatus

import ministatus.*

// == INPUTS ========================================================================================

/* Simulate jenkins API and generate random status */
FAKE_JENKINS = System.getenv()['FAKE_JENKINS']?.toBoolean()

def generateForSubfolders = System.getenv()['PARENT_FOLDER']

/* Convert job names to short name using regex. */
def shortNames = [
	'.*(\\d+)/JOB(\\d+).*' : '$1-$2',
	'(....).*' : '$1$2$3$4$5'
]

// == SETUP FAKE =======================================================================================
if (FAKE_JENKINS) {
	println "Using fake jenkins"
	
	def jobs = { folder -> (1..10).collect{ return new org.jenkinsci.plugins.workflow.job.WorkflowJob("${folder}/JOB${it}"); } }
	def folder = { folder, folderJobs -> return new com.cloudbees.hudson.plugins.folder.Folder(folder, folderJobs); }
	def folders = { parent, jobsClosure -> return (1..10).collect{ def folderName = "${parent}/folder${it}"; folder(folderName, jobsClosure(folderName)); } }
	
	def jobfolders = folders( generateForSubfolders, jobs )
	def rootFolder = folder ( generateForSubfolders, jobfolders )
	def root = folder(generateForSubfolders, rootFolder)
	
	hudson.model.Hudson.instance.setJobs(root)
}

// == Logic ==============================================================================================

def folderType = com.cloudbees.hudson.plugins.folder.Folder.class
def jobTypes = [org.jenkinsci.plugins.workflow.job.WorkflowJob.class]
def jobScanner = new JobScanner(folderType, jobTypes)

def jobTree = jobScanner.getJobTree()
println jobTree
def subFolders = jobScanner.directSubfolders(jobTree, generateForSubfolders)
println subFolders

// TODO disabled info from real jenkins API
// jobChecker.addJobDisabledInfo(subFolders)

disabledOverlayImage 	= 'resources/edit-delete.png'
redImage 			= 'resources/red.png'
blueImage 			= 'resources/blue.png'

def imgDim =     [w:12 , h:25, dim:800, offs:400]
def overlayDim = [w:12 , h:25, dim: 12, offs:  0]

def imgSource = { item -> 
	def overlayImg = ''
	def prefix = ''
	if (it.disabled) {
		prefix = 'disabled'
		overlayImg = disabledOverlayImage
	}
	return ["${it.name}/lastBuild/status", prefix, overlayImg]
}
if (FAKE_JENKINS) {
	imgSource = { item -> 
		def overlayImg = ''
		def prefix = ''
		if (Math.random() < 0.3) {
			prefix = '[disabled]'
			overlayImg = disabledOverlayImage
		}
		
		return (Math.random() > 0.2) ? [ blueImage, prefix, overlayImg] : [ redImage, prefix, overlayImg] 
	}
}

def htmlContent = new MiniStatus(imgDim, overlayDim, shortNames, imgSource).createContent(subFolders)

def html = HtmlUtil.instance.createPage(htmlContent)
println html

def mini = HtmlUtil.instance.minimizeHtml(html)
new File('jenkins_gen_output.html').write(mini)

