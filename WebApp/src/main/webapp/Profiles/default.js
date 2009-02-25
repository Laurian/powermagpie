(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';

	window[namespace].$.extend(
		window[namespace],
		{
			
			init: 	function() {
						with(window[namespace]){
/////////

$("*").hover(
	function (event) {
		event.stopPropagation();
		if (this.tagName == "HTML" || this.tagName == "BODY") return;
	
		Y(function (clean) {
			return function (el) {
				$(el).css({ outline: "none" });
				return ($(el).parent().length == 0)?el:clean($(el).parent());
			};
		})($(this).parent());
	
		$(this).css({ outline: "1px solid DeepPink" });
	
		$(this).bind('click', function(event) {
			event.stopPropagation();
			
			var base = document.location.href.substring(0, document.location.href.length - document.location.hash.length);
			var id = this.id;
			if (this.id == "") id = ("xpointer("+ getXPath(this).toLowerCase() +")").replace(/\[/g, '%5B').replace(/\]/g, '%5D');
			var uri = base + "#" + id;

			var seeAlso = 'http://purl.org/net/powermagpie/store/' + (new UUID() + '').toLowerCase();
			
			$(this).attr({
				'id':		id,
				'about': 	uri,
				'rel': 'rdfs:seeAlso',
				'resource':	seeAlso,
				'property': 'content:item',
				//'xmlns:atom': 'http://djpowell.net/schemas/atomrdf/0.3/',
				'xmlns:content': 'http://purl.org/rss/1.0/modules/content/',
				'xmlns:rdfs': 'http://www.w3.org/2000/01/rdf-schema#'
			});
			
			RDFA.CALLBACK_DONE_PARSING = function() {
				humanMsg.displayMsg('Annotated <a href="javascript:alert(RDFA.triplestore.subjectIndex[\'&lt;' + escape(uri) + '&gt;\'])">' + uri + '</a>', true);	
				//TODO @@@
				// add session triples + post to server -> talis metastore
				var nt = '';
				var st = RDFA.triplestore.subjectIndex['<' + uri + '>'];
				for(t in st) {
					if ((st[t] + '').indexOf('function') != 0) nt += st[t] + '\n';
				}
				nt += '<'+seeAlso+'> <http://www.w3.org/2000/10/annotation-ns#context> <'+uri+'> .' + '\n';
				nt += '<'+seeAlso+'> <http://www.w3.org/2000/10/annotation-ns#annotates> <'+base+'> .' + '\n';
				if (console) console.log(nt);
				// ajax post to http://localhost:8080/PowerMagpieLight/store
				//$.post('http://localhost:8080/PowerMagpieLight/store', {'nt': nt});
				$('body').append('<div id="temp" style="xdisplay: none"><form id="fnt" method="POST" action="http://localhost:8080/PowerMagpie/store" target="iii"><textarea id="nt" name="nt" cols="100" rows="10"></textarea><input type="submit"></form><iframe src="about:blank" name="iii" id="iii"></iframe></div>');
				$('#nt').text(nt);
				//$('#fnt').submit();
				//$('#temp').remove();
				
				insert(['link', {
					'type': 'application/rdf+xml',
					'href': 'http://api.talis.com/stores/lgridinoc-dev1/meta?about=' + seeAlso
				}]);
			};
			
			RDFA.parse();
			
			return false;
		}); // click

  	}, 
  	function (event) {
		if (this.tagName == "HTML" || this.tagName == "BODY") return;
		$(this).css({ outline: "none" });
		$(this).unbind('click');
		$(this).parent().trigger("mouseover");
  	}
); // hover

//humanMsg.setup('body', 'PowerMagpie');
//humanMsg.displayMsg('PowerMagpie magick loaded! v' + version, false);

/////////							
						}
					},
					
			getXPath: 	function(element){
							var path = "";
					     	for (; element && element.nodeType == 1; element = element.parentNode) {
							       idx = (function(element) {
											var count = 1;
										    for (var sib = element.previousSibling; sib ; sib = sib.previousSibling) {
										        if(sib.nodeType == 1 && sib.tagName == element.tagName) count++;
										    }
										    return count;
									  }(element));
							       xname = element.tagName;
							       if (idx > 1) xname += "[" + idx + "]";
							       path = "/" + xname + path;
							    }
							    return path;
						},
						
			insert: 	function(data, callback){
							console.log(data);
							var node = document.createElement(data[0]);
							for (attr in data[1]) {
								node.setAttribute(attr, data[1][attr]);
							}
							if (callback !== null) { node.onload = callback; }
							document.getElementsByTagName("head")[0].appendChild(node);
						},
					
			session: 	'http://purl.org/net/powermagpie/store/' 
						+ (new UUID() 
						+ '').toLowerCase()  
		});
				
}());


