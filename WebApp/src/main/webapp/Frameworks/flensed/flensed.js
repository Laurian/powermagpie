/*	flensedCore 0.2 alpha2 <http://www.flensed.com/>
	Copyright (c) 2008 Kyle Simpson
	This software is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>

	====================================================================================================
	Portions of this code were extracted and/or derived from:

	SWFObject v2.1 <http://code.google.com/p/swfobject/>
	Copyright (c) 2007-2008 Geoff Stearns, Michael Williams, and Bobby van der Sluis
	This software is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>
*/

if (typeof flensed === "undefined") { var flensed = {}; }

setTimeout(function() {
	var this_script = "flensed.js",		// DO NOT rename this file or change this line.
		base_path_known = false,
		doc = document,
		win = window,
		scriptArry = doc.getElementsByTagName("script"),
		scrlen = scriptArry.length;
	try { flensed.base_path.toLowerCase(); base_path_known = true; } catch(err) { flensed.base_path = ""; }
	
	if ((typeof scriptArry !== "undefined") && (scriptArry !== null)) {
		if (!base_path_known) {
			var idx=0;
			for (var k=0; k<scrlen; k++) {
				if (typeof scriptArry[k].src !== "undefined") {
					if ((idx=scriptArry[k].src.indexOf(this_script)) >= 0) {
						flensed.base_path = scriptArry[k].src.substr(0,idx);
						break;
					}
				}
			}
		}
	}
},0);

flensed.parseXMLString = function(xmlStr) {
	var xmlDoc = null;
	if (window.ActiveXObject) {
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM"); 
		xmlDoc.async=false;
		xmlDoc.loadXML(xmlStr);
	}
	else {
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(xmlStr,"text/xml");
	}
	return xmlDoc;
};
flensed.getObjectById = function(idStr) {
	try {
		if (document.layers) { return document.layers[idStr]; }
		else if (document.all) { return document.all[idStr]; }
		else if (document.getElementById) { return document.getElementById(idStr); }
	}
	catch (err) { }
	return null;
};
flensed.bindEvent = function(obj,eventName,handlerFunc) {
	eventName = eventName.toLowerCase();
	if (typeof obj.addEventListener !== "undefined") { obj.addEventListener(eventName.replace(/^on/,""),handlerFunc,false); }
	else if (typeof obj.attachEvent !== "undefined") { obj.attachEvent(eventName,handlerFunc); }
	else if (typeof obj[eventName] === "function") {
		var oldHandler = obj[eventName];
		obj[eventName] = function() {
			oldHandler();
			handlerFunc();
		};
	}
	else { obj[eventName] = handlerFunc; }
};
flensed.throwUnhandledError = function(errDescription) {
	throw new Error(errDescription);
};
flensed.error = function(code,name,description,srcElement) {
	return {
		number:code,
		name:name,
		description:description,
		message:description,
		srcElement:srcElement,
		toString:function() { return code+", "+name+", "+description; }
	};
};
flensed.ua = function() {
	var	UNDEF = "undefined",
		OBJECT = "object",
		SHOCKWAVE_FLASH = "Shockwave Flash",
		SHOCKWAVE_FLASH_AX = "ShockwaveFlash.ShockwaveFlash",
		FLASH_MIME_TYPE = "application/x-shockwave-flash",
		JSTRUE = true,
		JSFALSE = false,
		win = window,
		doc = document,
		nav = navigator;
	
	var w3cdom = typeof doc.getElementById !== UNDEF && typeof doc.getElementsByTagName !== UNDEF && typeof doc.createElement !== UNDEF,
		playerVersion = [0,0,0],
		d = null;
	if (typeof nav.plugins !== UNDEF && typeof nav.plugins[SHOCKWAVE_FLASH] === OBJECT) {
		d = nav.plugins[SHOCKWAVE_FLASH].description;
		if (d && !(typeof nav.mimeTypes !== UNDEF && nav.mimeTypes[FLASH_MIME_TYPE] && !nav.mimeTypes[FLASH_MIME_TYPE].enabledPlugin)) { // navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin indicates whether plug-ins are enabled or disabled in Safari 3+
			d = d.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
			playerVersion[0] = parseInt(d.replace(/^(.*)\..*$/, "$1"), 10);
			playerVersion[1] = parseInt(d.replace(/^.*\.(.*)\s.*$/, "$1"), 10);
			playerVersion[2] = /r/.test(d) ? parseInt(d.replace(/^.*r(.*)$/, "$1"), 10) : 0;
		}
	}
	else if (typeof win.ActiveXObject !== UNDEF) {
		var a = null, fp6Crash = JSFALSE;
		try {
			a = new ActiveXObject(SHOCKWAVE_FLASH_AX + ".7");
		}
		catch(e) {
			try { 
				a = new ActiveXObject(SHOCKWAVE_FLASH_AX + ".6");
				playerVersion = [6,0,21];
				a.AllowScriptAccess = "always";  // Introduced in fp6.0.47
			}
			catch(e2) {
				if (playerVersion[0] === 6) {
					fp6Crash = JSTRUE;
				}
			}
			if (!fp6Crash) {
				try {
					a = new ActiveXObject(SHOCKWAVE_FLASH_AX);
				}
				catch(e3) {}
			}
		}
		if (!fp6Crash && a) { // a will return null when ActiveX is disabled
			try {
				d = a.GetVariable("$version");  // Will crash fp6.0.21/23/29
				if (d) {
					d = d.split(" ")[1].split(",");
					playerVersion = [parseInt(d[0], 10), parseInt(d[1], 10), parseInt(d[2], 10)];
				}
			}
			catch(e4) {}
		}
	}
	var u = nav.userAgent.toLowerCase(),
		p = nav.platform.toLowerCase(),
		webkit = /webkit/.test(u) ? parseFloat(u.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : JSFALSE, // returns either the webkit version or false if not webkit
		ie = JSFALSE,
		ieVer = 0,
		windows = p ? /win/.test(p) : /win/.test(u),
		mac = p ? /mac/.test(p) : /mac/.test(u);
	/*@cc_on
		ie = true;
		ieVer = parseInt(u.match(/msie (\d+)/)[1],10);
		@if (@_win32)
			windows = true;
		@elif (@_mac)
			mac = true;
		@end
	@*/
	return { w3cdom:w3cdom, pv:playerVersion, webkit:webkit, ie:ie, ieVer:ieVer, win:windows, mac:mac };
}();
