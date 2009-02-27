(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';
	
	with (window[namespace]) {
					
			loadScript(base + '/Library/uuid-0.2/uuid-0.2.js');
			loadScript(base + '/Library/rdfa/rdfa.js');

            //DWR
            /*
            var dwr = {};
            var PowerMagpie = {};
            PowerMagpie._path =  base + '/dwr';
            
            loadScript(base + '/dwr/interface/PowerMagpie.js', function(){

                PowerMagpie._path =  base + '/dwr';

                loadScript(base + '/dwr/engine.js', function(){
                    
                    dwr.engine._defaultPath = base + '/dwr';
                    dwr.engine.setRpcType(dwr.engine.ScriptTag);                    
                    
                });
            });
            */

		
		    loadScript(base + '/Frameworks/jQuery/jquery-ui-personalized-1.6rc6/jquery-1.3.1.js', function(){
				jQuery.extend(window[namespace], {
						$:  	jQuery//.noConflict()
					});
				
				//loadScript(base + '/Frameworks/flensed/flXHR.js', function(){
				/*loadScript('http://flxhr.flensed.com/code/build/flXHR.js', function(){
					$.getAjaxTransport = function() {
						return new flensed.flXHR({
								instancePooling: 	true,
								autoUpdatePlayer: 	true,
								xmlResponseText: 	false,
								onerror: 			console.log,
								loadPolicyURL: 		base + '/crossdomain.xml'
							});
					}
					
				});*/
				
				loadScript(base + '/Frameworks/jQuery/jquery-ui-personalized-1.6rc6/jquery-ui-personalized-1.6rc6.js', function(){
				});
				
			});
	}
	
})();

