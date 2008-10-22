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

(function(){
	
	var namespace 	  = 'http://purl.org/net/powermagpie';
	
	if (! window[namespace]) window[namespace] = {};
	if (window[namespace].toggle) return window[namespace].toggle();
	
	var object		 = {
		
		Y: 			function(le) {
		    			return function(f) {
		        			return f(f);
		    			}( function(f) {
		        			return le( function(x) {
		            			return f(f)(x);
		        			});
		    			});
					}, 
		
		bootstrap:  function() {
						with (window[namespace]) {
								loadStyle(namespace + '/boot/style.css');
								loadScript(namespace + '/boot/core.js', function(){
									loadScript(profile, function(){
										window[namespace].init();
									});
								});
						}
					},
					
		init: 		function(){
						alert('init() bootstrap');
					},

		toggle:		function(){
						alert('toggle() bootstrap');
					},

		loadScript: function(uri, callback) {
						var script 	= document.createElement('script');
						script.type = 'text/javascript';
						script.src 	= uri;
						if (callback !== null) { script.onload = callback; }
						document.getElementsByTagName("head")[0].appendChild(script);
					},
		
		loadStyle:  function(uri, callback){
						var style 	= document.createElement('link');
						style.type 	= 'text/css';
						style.rel 	= 'stylesheet';
						style.href 	= uri;
						style.media = 'screen';
						if (callback !== null) { style.onload = callback; }
						document.getElementsByTagName("head")[0].appendChild(style);
					},
							 
		bookmarklet: "(function(){"
						+ "var s=document.createElementNS('http://www.w3.org/1999/xhtml','script');"
						+ "s.charset='UTF-8';"
						+ "s.type='text/javascript';"
						+ "s.src='" + namespace + "/bootstrap.js';"
						+ "document.getElementsByTagName('head')[0].appendChild(s);"
						+ "}());",
						
		profile: 	namespace + '/profiles/default.js', 
		
		version: 	'0.1.4'
	};
	
	if (window[namespace].$) {
		window[namespace].$.extend(window[namespace], object);
	} else window[namespace] = object;
	
	if (window[namespace].preboot) {
		window[namespace].preboot();
	} else window[namespace].bootstrap();

}());
