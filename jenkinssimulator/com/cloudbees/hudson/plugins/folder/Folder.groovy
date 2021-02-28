package com.cloudbees.hudson.plugins.folder

class Folder {

	def items = []
	def name

	def Folder(name, items) {
		this.name = name
		this.items = items
	}
	
	def getItems() {
		return this.items
	}
	
	def getFullName() {
		return this.name
	}
}