(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';
	
	with (window[namespace]) {
		
			loadStyle(base + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.css');
			
			loadScript(base + '/Library/uuid-0.2/uuid-0.2.js');
			loadScript(base + '/Library/rdfa/rdfa.js');
		
			loadScript(base + '/Frameworks/jQuery/jquery-1.2.6/jquery-1.2.6-patched.js', function(){
				jQuery.extend(window[namespace], {
						$:  	jQuery//.noConflict()
					});
				
				//loadScript(base + '/Frameworks/flensed/flXHR.js', function(){
				loadScript('http://flxhr.flensed.com/code/build/flXHR.js', function(){
					$.getAjaxTransport = function() {
						return new flensed.flXHR({
								instancePooling: 	true,
								autoUpdatePlayer: 	true,
								xmlResponseText: 	false,
								onerror: 			console.log,
								loadPolicyURL: 		base + '/crossdomain.xml'
							});
					}
					
				});
				
				loadScript(base + '/Frameworks/jQuery/jquery-dimensions-1.2.0/jquery.dimensions.js', function(){
					loadScript(base + '/Frameworks/jQuery/jquery-easing-1.3/jquery.easing.1.3.js', function(){
						loadScript(base + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.js', function(){
							
							humanMsg.setup('body', 'PowerMagpie');
							humanMsg.displayMsg('PowerMagpie magick loaded! v' + version, false);
							
						});
						
						/*loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.uri.js');
						loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.xmlns.js');
						loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.curie.js');
						loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.datatype.js');
						loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.rdf.js');
						loadScript(base + '/Frameworks/jQuery/rdfquery/jquery.rdfa.js');*/
						
					});
				});
				
			});
	}
	
})();

/*<script src="../jquery.uri.js" type="text/javascript"></script>
<script src="../jquery.xmlns.js" type="text/javascript"></script>
<script src="../jquery.curie.js" type="text/javascript"></script>
<script src="../jquery.datatype.js" type="text/javascript"></script>
<script src="../jquery.rdf.js" type="text/javascript"></script>
<script src="../jquery.rdfa.js" type="text/javascript"></script>*/

