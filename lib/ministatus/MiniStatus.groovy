package ministatus

class MiniStatus {
	
	def style
	def kuerzelMap
	def imgSource
	
	MiniStatus(imgDim, overlayDim, kuerzelMap, imgSource) {
		this.style = styleTemplate(imgDim, overlayDim)
		this.kuerzelMap = kuerzelMap
		this.imgSource = imgSource
	}
	
	def createContent(subFolders) {
		def mergedStatusHtml = subFolders.collect{ createMiniStatus(it) }.join("\n")

		
		def pageContent = this.style + mergedStatusHtml
		return pageContent
	}

	def createMiniStatus(folder) {
		def cells = folder.items.collect{ cellTemplate(it) }.join('\n')
		containerTemplate(folder.name, cells)
	}

	def styleTemplate(imgDim, overlayDim) {
	"""\
	<style>
	.container {
		border: 1px solid #000;
		padding: 10px;
	}
	.cellWrap {
		display: inline-block;
		float:left;
		width: ${imgDim.w}px;
		height: ${imgDim.h}px;
	}
	.cell {
		pointer-events: none;
		position: relative;
	}
	.image {
		display: block;
		margin-left: -${imgDim.offs}px;
		margin-top: -${imgDim.offs}px;
		width: ${imgDim.dim}px;
		height: ${imgDim.dim}px;
	}
	.imgOverlay {
		display: block;
		margin-left: -${overlayDim.offs}px;
		margin-top: -${overlayDim.offs}px;
		width: ${overlayDim.dim}px;
		height: ${overlayDim.dim}px;
		background: rgba(255,255,255,0.5);
	}
	.imageWrap {
		position: absolute;
		z-index: 5;
		overflow: hidden;
		width: ${imgDim.w}px;
		height: ${imgDim.h}px;
	}
	.imageOverlayWrap {
		position: absolute;
		z-index: 20;
		overflow: hidden;
		width: ${overlayDim.w}px;
		height: ${overlayDim.h}px;
	}
	.textWrap {
		position: absolute;
		z-index: 10;
		overflow: hidden;
		width: ${imgDim.w}px;
		height: ${imgDim.h}px;
	}
	.text {
		font-family: arial;
		font-size: 9px;
		width: ${imgDim.w}px;
		height: ${imgDim.h}px;
		line-height: ${imgDim.w}px;
		text-align: center;
		vertical-align: middle;
		writing-mode: vertical-rl;
		text-orientation: mixed;
	}
	.statusName {
		display: inline-block;
		overflow: hidden;
		width: 200px;
		line-height: ${imgDim.h}px;
	}
	.statusContent {
		display: inline-block;
		overflow: hidden;
		width: 250px;
	}
	</style>
	"""
	}

	def containerTemplate(name, content) {
	"""\
	<div class="container">
		<div class="statusName">${name}</div>
		<div class="statusContent">
			${content}
			<div style="clear:both"></div>
		</div>
	</div>
	"""
	}

	def cellTemplate(item) {
		def fullText = item.name
		
		//def kuerzel = this.kuerzelMap.find { template, kuerzel -> fullText ==~ ".*${template}.*" }?.value ?: fullText
		def kuerzelFromMap = this.kuerzelMap.findResult { regex, template ->
			//println "${fullText} matches ${regex} -> ${fullText.replaceAll(regex, template)}"
			(fullText ==~ regex) ? fullText.replaceAll(regex, template) : null
		}
		def kuerzel = kuerzelFromMap ?: fullText
		def img, overlayImg,  titlePrefix
		
		(img, titlePrefix, overlayImg) = this.imgSource(item)

		def fullTitle = titlePrefix ? "${titlePrefix} ${fullText}" : fullText
		
		def overlayHtml = ''
		if (overlayImg) {
			overlayHtml = """\
					<div class="imageOverlayWrap">
						<img class="imgOverlay" src="${overlayImg}" />
					</div>
			"""
		}
		"""\
			<div class="cellWrap" title="${fullTitle}">
				<div class="cell">
					<div class="textWrap">
						<span class="text">${kuerzel}</span>
					</div>
					<div class="imageWrap">
						<img class="image" src="${img}" />
					</div>
					${overlayHtml}
				</div>
			</div>
		"""
	}
}