/**
@prefix : <http://purl.org/net/ns/doas#> .
<>	a	:JavaScript;
	:shortdesc "PowerMagpie loader";
 	:created "2008-06-12";
 	:release [
		:revision "0.3";
		:created "2008-06-12"
	];
 	:author [
		:name "Laurian Gridinoc";
		:homepage <http://purl.org/net/laur>
	];
 	:license <http://www.apache.org/licenses/LICENSE-2.0> .
*/

(function() {
	
	var namespace 	= 'http://purl.org/net/powermagpie';
	
	var object		= {
		
        //  Y = λf·(λx·f (x x)) (λx·f (x x))
		Y: 			function(le) {
		    			return function(f) {
		        			return f(f);
		    			} (function(f) {
		        			return le(function(x) {
		            			return f(f)(x);
		        			});
		    			});
					}, 
		
		bootstrap:  function() {
						with (window[namespace]) {
								loadStyle (base + '/boot/style.css');
								loadScript(base + '/boot/core.js', function(){
									loadScript(base + profile, function(){
										window[namespace].init();
									});
								});
						}
					},
					
		init: 		function() {
						alert('error: called init() in bootstrap');
					},

		loadScript: function(uri, callback) {
						var script 	= document.createElement('script');
						script.type = 'text/javascript';
						script.src 	= uri;
						if (callback !== null) { script.onload = callback; }
						document.getElementsByTagName('head')[0].appendChild(script);
					},
		
		loadStyle:  function(uri, callback) {
						var style 	= document.createElement('link');
						style.type 	= 'text/css';
						style.rel 	= 'stylesheet';
						style.href 	= uri;
						style.media = 'screen';
						if (callback !== null) { style.onload = callback; }
						document.getElementsByTagName('head')[0].appendChild(style);
					},
							 
		bookmarklet: function() {
						with (window[namespace]) {
							return "(function(){"
							+ "var s=document.createElementNS('http://www.w3.org/1999/xhtml','script');"
							+ "s.charset='UTF-8';"
							+ "s.type='text/javascript';"
							+ "s.src='" + base + "/bootstrap.js';"
							+ "window['" + namespace + "']={base: '" + base + "'};"
							+ "document.getElementsByTagName('head')[0].appendChild(s);"
							+ "}());";
						}
					},
						
		profile: 	'/Profiles/Classic.js',
		
		base: 		namespace, 

		version: 	'0.2.1'
	};
	
	if (!window[namespace]) { window[namespace] = {} };
	if ( window[namespace].$) {
		window[namespace].$.extend(window[namespace], object);
		window[namespace].toggle();
	} else {
		object.base = window[namespace].base;
		window[namespace] = object;
		window[namespace].bootstrap();
	}

}());
