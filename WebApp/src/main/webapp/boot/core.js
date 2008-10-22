(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';
	
	with (window[namespace]) {
		
			loadStyle(namespace + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.css');
			loadScript(namespace + '/Library/uuid-0.2/uuid-0.2.js');
			loadScript(namespace + '/Library/rdfa/rdfa.js');
		
			loadScript(namespace + '/Frameworks/jQuery/jquery-1.2.6/jquery-1.2.6.js', function(){
				jQuery.extend(window[namespace], {
						$:  	jQuery.noConflict()
					});
				
				//alert(0);
				
				loadScript(namespace + '/Frameworks/jQuery/jquery-dimensions-1.2.0/jquery.dimensions.js', function(){
					loadScript(namespace + '/Frameworks/jQuery/jquery-easing-1.3/jquery.easing.1.3.js', function(){
						loadScript(namespace + '/Frameworks/jQuery/humanmsg-1.0/humanmsg-1.0.js', function(){
							//loadScript(namespace + '/Library/uuid-0.2/uuid-0.2.js');
							//loadScript(namespace + '/Library/rdfa/rdfa.js');
							//alert(1);
							humanMsg.setup('body', 'PowerMagpie');
							humanMsg.displayMsg('PowerMagpie magick loaded! v' + version, false);
						});
					});
				});
				
			});
	}
	
})();

