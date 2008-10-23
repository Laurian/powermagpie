(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';
	
	with (window[namespace]) {
		
			loadStyle(base + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.css');
			loadScript(base + '/Library/uuid-0.2/uuid-0.2.js');
			loadScript(base + '/Library/rdfa/rdfa.js');
		
			loadScript(namespace + '/Frameworks/jQuery/jquery-1.2.6/jquery-1.2.6.js', function(){
				jQuery.extend(window[namespace], {
						$:  	jQuery.noConflict()
					});
				
				loadScript(base + '/Frameworks/jQuery/jquery-dimensions-1.2.0/jquery.dimensions.js', function(){
					loadScript(base + '/Frameworks/jQuery/jquery-easing-1.3/jquery.easing.1.3.js', function(){
						loadScript(base + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.js', function(){
							
							humanMsg.setup('body', 'PowerMagpie');
							humanMsg.displayMsg('PowerMagpie magick loaded! v' + version, false);
							
						});
					});
				});
				
			});
	}
	
})();

