package ministatus

@Singleton
class HtmlUtil {
	
	def minimizeHtml(html) {
		def styles = html.findAll( /[.]\w+\s[{]/ ).collect{ it[1..-3] }
		def styleNum = -1
		def styleMap = styles.collectEntries{ styleNum++; return [(it): 's' + styleNum]; }
		println "Minimizing styles: ${styleMap}"
		
		styleMap.each{ name, replace -> html = html.replaceAll("[.]${name}\s[{]", ".${replace} {") }
		styleMap.each{ name, replace -> html = html.replaceAll("class=['\"]${name}['\"]", "class='${replace}'") }
		
		return html.replaceAll('\t*', '').replaceAll('\n','')
	}
	
	def createPage(content) {
		return "<html><body>\n${content}</html></body>"
	}
}