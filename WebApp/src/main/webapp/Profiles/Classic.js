(function(){

	var namespace 	  = 'http://purl.org/net/powermagpie';

	window[namespace].$.extend(
		window[namespace],
		{

			init: 	function() {
						with(window[namespace]){
    var pmui = document.createElement('div');
    pmui.id = "c3f22685-79cc-4ed6-b833-2ff9f61a5a33";
    pmui.innerHTML = "<div id='c3f22685-79cc-4ed6-b833-2ff9f61a5a33-logo'><img src='"+base+"/title.png' /></div>"
        +"<iframe id='pmframe' name='pmframe' frameborder='0' scrolling='no' src='"+base+"/UI/?"+Math.random()+"#"+session+"'></iframe>";
    
    document.body.appendChild(pmui);

    setTimeout(function(){
        $("#c3f22685-79cc-4ed6-b833-2ff9f61a5a33").draggable();
        $("#c3f22685-79cc-4ed6-b833-2ff9f61a5a33").resizable({ autohide: true, minHeight: 400, minWidth: 200 });
    }, 2000);
                        }
					},

            toggle:		function() {
							aler('magick will be here!');
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


