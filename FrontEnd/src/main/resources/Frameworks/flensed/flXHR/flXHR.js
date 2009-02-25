/*	flXHR 0.6 alpha7 <http://www.flxhr.com/>
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
	var this_script = "flXHR.js",		// DO NOT rename this file or change this line.
		base_path_known = false,
		doc = document,
		win = window,
		scriptArry = doc.getElementsByTagName("script"),
		scrlen = scriptArry.length;
	try { flensed.base_path.toLowerCase(); base_path_known = true; } catch(err) { flensed.base_path = ""; }

	function load_script(src,type,language) {
		for (var k=0; k<scrlen; k++) {
			if (typeof scriptArry[k].src !== "undefined") {
				if (scriptArry[k].src.indexOf(src) >= 0) { break; }  // script already loaded/loading...
			}
		}
		var scriptElem = doc.createElement("script");
		scriptElem.setAttribute("src",flensed.base_path+src);
		if (typeof type !== "undefined") { scriptElem.setAttribute("type",type); }
		if (typeof language !== "undefined") { scriptElem.setAttribute("language",language); }
		doc.getElementsByTagName("head")[0].appendChild(scriptElem);
	}
	
	if ((typeof scriptArry !== "undefined") && (scriptArry !== null)) {
		if (!base_path_known) {
			var idx=0;
			for (var k=0; k<scrlen; k++) {
				if (typeof scriptArry[k].src !== "undefined") {
					if (((idx=scriptArry[k].src.indexOf("flensed.js")) >= 0) || ((idx=scriptArry[k].src.indexOf(this_script)) >= 0)) {
						flensed.base_path = scriptArry[k].src.substr(0,idx);
						break;
					}
				}
			}
		}
	}
	try { flensed.checkplayer.module_ready(); } catch (err2) { load_script("checkplayer.js","text/javascript"); }

	var coreInterval = null;
	(function waitForCore() {
		try { flensed.ua.pv.join("."); } catch (err) {
			coreInterval = setTimeout(waitForCore,25);
			return;
		}
		if (flensed.ua.ie) { load_script("flXHR.vbs","text/vbscript","vbscript"); }
		flensed.binaryToString = function(binobj,skipVB) {
			skipVB = ((flensed.ua.ie && typeof skipVB !== "undefined")?(!(!skipVB)):!flensed.ua.ie);
			if (!skipVB) {
				try { return flXHR_vb_BinaryToString(binobj); } catch (err) { }
			}
			var str = "", buf = [];
			try { 
				for (var i=0; i<binobj.length; i++) { buf[buf.length] = String.fromCharCode(binobj[i]); }
				str = buf.join("");
			} catch (err2) { }
			return str;
		};
		var cleanup_flXHR = function() {
			try { win.detachEvent("onunload",cleanup_flXHR); } catch (err) { }
			try {
				for (var k in flensed.flXHR) {
					if (flensed.flXHR[k] !== Object.prototype[k]) {
						try { flensed.flXHR[k] = null; } catch (err2) { }
					}
				}
				flensed.flXHR = null;
			}
			catch (err3) { }
		};
		flensed.bindEvent(win,"onunload",cleanup_flXHR);
	})();
	function clearCoreInterval() { clearTimeout(coreInterval); try { win.detachEvent("onunload",clearCoreInterval); } catch (err) { } }
	if (coreInterval !== null) { try { win.attachEvent("onunload",clearCoreInterval); } catch(err3) { } }	// only IEwin would leak memory this way
	var dependencyTimeout = setTimeout(function(){
		try { win.detachEvent("onunload",clearDependencyTimeout); } catch (err) { }
		try { flensed.checkplayer.module_ready(); } catch (err2) { throw new Error("flXHR dependencies failed to load."); }
	},10000);	// only wait 10 secs max for CheckPlayer to load
	function clearDependencyTimeout() { clearTimeout(dependencyTimeout); try { win.detachEvent("onunload",clearDependencyTimeout); } catch (err) { } }
	try { win.attachEvent("onunload",clearDependencyTimeout); } catch (err4) { }	// only IEwin would leak memory this way
},0);

flensed.flXHR = function(configObject) {
	var _flxhr = flensed.flXHR;	// for compression optimization
	var _chkplyr = null;	// for compression optimization
	if (typeof _flxhr._idc === "undefined") { _flxhr._idc = 0; }
	if (typeof _flxhr._inp === "undefined") { _flxhr._inp = []; }
	var instancePooling = false;
	if (typeof configObject !== "undefined" && configObject !== null && typeof configObject === "object") {
		if (typeof configObject.instancePooling !== "undefined") { 
			instancePooling = !(!configObject.instancePooling);
			if (instancePooling) {
				var ret = function(){
					for (var k=0; k<_flxhr._inp.length; k++) {
						var inst = _flxhr._inp[k];
						if (inst.readyState === 4) {	// must have already been used and be idle
							inst.Reset();
							inst.Configure(configObject);
							return inst;
						}
					}
					return null;
				}();
				if (ret !== null) { return ret; }
			}
		}
	}

	// Private Properties
	var	idNumber = ++_flxhr._idc,
		constructQueue = [],
		constructInterval = null,
		notReadyInterval = null,
		timeoutInterval = null,
		swfId = null,
		readyState = -1,
		responseBody = null,
		responseText = null,
		responseXML = null,
		status = null,
		statusText = null,
		swfObj = null,
		UNDEF = "undefined",
		EMPTY = "",
		JSFUNC = "function",
		JSSTR = "string",
		JSTRUE = true,
		JSFALSE = false,
		OBJECT = "object",
		win = window,
		doc = document,
		publicAPI = null,
		appendTo = null,
		rawresponse = null,
		queue_empty = true,

	// Configurable Properties (via instantiation constructor)
		instanceId = "flXHR_"+idNumber,
		binaryResponseBody = JSFALSE,
		xmlResponseText = JSTRUE,
		autoUpdatePlayer = JSFALSE,
		swfIdPrefix = "flXHR_swf",
		styleClass = "flXHRhideSwf",
		appendToId = null,
		sendTimeout = -1,
		loadPolicyURL = EMPTY,
		onreadystatechange = null,
		onerror  = null,
		ontimeout = null;

	// Private Methods
	var constructor = function() {
		if (typeof configObject === OBJECT && configObject !== null) {
			if ((typeof configObject.instanceId !== UNDEF) && (configObject.instanceId !== null) && (configObject.instanceId !== EMPTY)) { instanceId = configObject.instanceId; }
			if ((typeof configObject.swfIdPrefix !== UNDEF) && (configObject.swfIdPrefix !== null) && (configObject.swfIdPrefix !== EMPTY)) { swfIdPrefix = configObject.swfIdPrefix; }
			if ((typeof configObject.appendToId !== UNDEF) && (configObject.appendToId !== null) && (configObject.appendToId !== EMPTY)) { appendToId = configObject.appendToId; }
			if ((typeof configObject.loadPolicyURL !== UNDEF) && (configObject.loadPolicyURL !== null) && (configObject.loadPolicyURL !== EMPTY)) { loadPolicyURL = configObject.loadPolicyURL; }

			if (typeof configObject.binaryResponseBody !== UNDEF) { binaryResponseBody = !(!configObject.binaryResponseBody); }
			if (typeof configObject.xmlResponseText !== UNDEF) { xmlResponseText = !(!configObject.xmlResponseText); }
			if (typeof configObject.autoUpdatePlayer !== UNDEF) { autoUpdatePlayer = !(!configObject.autoUpdatePlayer); }
			if ((typeof configObject.sendTimeout !== UNDEF) && (parseInt(configObject.sendTimeout,10) > 0)) { sendTimeout = parseInt(configObject.sendTimeout,10); }

			if ((typeof configObject.onreadystatechange !== UNDEF) && (configObject.onreadystatechange !== null)) { onreadystatechange = configObject.onreadystatechange; }
			if ((typeof configObject.onerror !== UNDEF) && (configObject.onerror !== null)) { onerror = configObject.onerror; }
			if ((typeof configObject.ontimeout !== UNDEF) && (configObject.ontimeout !== null)) { ontimeout = configObject.ontimeout; }
		}

		swfId = swfIdPrefix+"_"+idNumber;

		function clearConstructInterval() { clearTimeout(constructInterval); try { win.detachEvent("onunload",clearConstructInterval); } catch (err) { } }
		try { win.attachEvent("onunload",clearConstructInterval); } catch (err) { }	// only IEwin would leak memory this way
		(function waitForCore() {
			try { flensed.bindEvent(win,"onunload",destructor); } catch (err) { constructInterval = setTimeout(waitForCore,25); return; }
			try { win.detachEvent("onunload",clearConstructInterval); } catch (err2) { }
			constructInterval = setTimeout(continueConstructor,25);
		})();
	}();

	function continueConstructor() {
		if (appendToId === null) { appendTo = doc.getElementsByTagName("body")[0]; }
		else { appendTo = flensed.getObjectById(appendToId); }
		
		try { appendTo.nodeName.toLowerCase(); swfobject.getObjectById("a"); flensed.checkplayer.module_ready(); } catch (err) {	// make sure DOM object, swfobject, and checkplayer are all ready
			// maybe set a timeout here in case the DOM obj (appendTo) never gets ready?
			constructInterval = setTimeout(continueConstructor,25);
			return;
		}
		_chkplyr = flensed.checkplayer;	// for optimization purposes

		if ((typeof _flxhr._cpobj === UNDEF) && (typeof _chkplyr._instance === UNDEF)) {
			try {
				_flxhr._cpobj = new _chkplyr(_flxhr.MIN_PLAYER_VERSION,checkCallback,JSFALSE,updateCallback);
			}
			catch (err2) { doError(_flxhr.DEPENDENCY_ERROR,"flXHR: checkplayer Init Failed","The initialization of the 'checkplayer' library failed to complete."); return; }
		}
		else {
			_flxhr._cpobj = _chkplyr._instance;
			stillContinueConstructor();
		}
	}

	function stillContinueConstructor() {
		if (typeof _flxhr._css === UNDEF && appendToId === null) {	// only if CSS hasn't been defined yet, and if flXHR's being added to the BODY of the page
			try {
				swfobject.createCSS("."+styleClass,"left:-1px;top:0px;width:1px;height:1px;position:absolute;");
				_flxhr._css = true;
			}
			catch (err) { doError(_flxhr.DEPENDENCY_ERROR,"flXHR: swfobject Call Failed","A call to the 'swfobject' library failed to complete."); return; }
		}

		var holder=doc.createElement("div");
		holder.id = swfId;
		holder.className = styleClass;
		appendTo.appendChild(holder);
		appendTo = null;

		var flashvars = {},
			params = { allowScriptAccess:"always" },
			attributes = { id:swfId, name:swfId, styleclass:styleClass },
			optionsObj = { swfCB:finishConstructor, swfEICheck:"reset" };

		try {
			_flxhr._cpobj.DoSWF(flensed.base_path+"flXHR.swf", swfId, "1", "1", flashvars, params, attributes, optionsObj);
		}
		catch (err2) { doError(_flxhr.DEPENDENCY_ERROR,"flXHR: checkplayer Call Failed","A call to the 'checkplayer' library failed to complete."); return; }
	}

	function finishConstructor(loadStatus) {
		// maybe set an SWF_INIT triggered timeout here, in case somehow flXHR.swf fails to load/initialize?
		if (loadStatus.status !== _chkplyr.SWF_EI_READY) { return; }

		clearIntervals();
		swfObj = flensed.getObjectById(swfId);
		swfObj.setId(swfId);

		if (loadPolicyURL !== EMPTY) { swfObj.loadPolicy(loadPolicyURL); }
		swfObj.returnBinaryResponseBody(binaryResponseBody);
		swfObj.doOnReadyStateChange = doOnReadyStateChange;
		swfObj.doOnError = doError;
		swfObj.sendProcessed = sendProcessed;
		swfObj.chunkResponse = chunkResponse;
	
		readyState = 0;
		updateFromPublicAPI();
		updatePublicAPI();
		if (typeof onreadystatechange === JSFUNC) {
			try { onreadystatechange(publicAPI); } 
			catch (err) { doError(_flxhr.HANDLER_ERROR,"flXHR::onreadystatechange(): Error","An error occurred in the handler function. ("+err2.message+")"); return; }
		}

		executeQueue();
	}

	function destructor() {
		try { win.detachEvent("onunload",destructor); } catch (err) { }
		try {
			for (var k=0; k<_flxhr._inp.length; k++) {
				if (_flxhr._inp[k] === publicAPI) { _flxhr._inp[k] = JSFALSE; }
			}
		} catch (err2) { }
		try {
			for (var j in publicAPI) {
				if (publicAPI[j] !== Object.prototype[j]) {
					try { publicAPI[j] = null; } catch (err3) { }
				}
			}
		}
		catch (err4) { }
		publicAPI = null;

 		clearIntervals();
		if ((typeof swfObj !== UNDEF) && (swfObj !== null)) {
			try { swfObj.abort(); } catch(err5) { }

			try { swfObj.doOnReadyStateChange = null; doOnReadyStateChange = null; } catch(err6) { }
			try { swfObj.doOnError = null; doOnError = null; } catch(err7) { }
			try { swfObj.sendProcessed = null; sendProcess = null; } catch (err8) { }
			try { swfObj.chunkResponse = null; chunkResponse = null; } catch (err9) { }
			swfObj = null;
		
			try { swfobject.removeSWF(swfId); } catch(err10) { }
		}
		emptyQueue();

		_flxhr = null;
		win = null;
		doc = null;
		onreadystatechange = null;
		onerror  = null;
		ontimeout = null;
		readyState = 0;
		status = null;
		statusText = null;
		responseText = null;
		responseXML = null;
		responseBody = null;
		rawresponse = null;
		appendTo = null;
	}
	
	function chunkResponse() {
		if (binaryResponseBody && typeof arguments[0] !== UNDEF) {	// most likely an array of byte parameters for binary response
			rawresponse = ((rawresponse !== null)?rawresponse:[]);
			rawresponse = rawresponse.concat(arguments[0]);
		}
		else if (typeof arguments[0] === JSSTR) {	// a single string parameter
			rawresponse = ((rawresponse !== null)?rawresponse:EMPTY);
			rawresponse += arguments[0];
		}
	}

	function doOnReadyStateChange() {
		if (typeof arguments[0] !== UNDEF) { readyState = arguments[0]; }
		if (readyState === 4) {
			clearIntervals();
			if (binaryResponseBody && rawresponse !== null) {
				try { 
					responseText = flensed.binaryToString(rawresponse,JSTRUE);
					try { responseBody = flXHR_vb_StringToBinary(responseText); } catch (err4) { responseBody = rawresponse; }
				} catch (err) { }
			} 
			else {
				responseText = rawresponse;
			}
			rawresponse = null;
			if (responseText !== EMPTY) {
				if (xmlResponseText) { 
					try { responseXML = flensed.parseXMLString(responseText); }
					catch (err2) { responseXML = {}; }
				}
			}
		}
		if (typeof arguments[1] !== UNDEF) { status = arguments[1]; }
		if (typeof arguments[2] !== UNDEF) { statusText = arguments[2]; }

		updateFromPublicAPI();
		updatePublicAPI();

		if (typeof onreadystatechange === JSFUNC) {
			try { onreadystatechange(publicAPI); } 
			catch (err3) { doError(_flxhr.HANDLER_ERROR,"flXHR::onreadystatechange(): Error","An error occurred in the handler function. ("+err3.message+")"); return; }
		}
	}

	function doError() {
		clearIntervals();
		emptyQueue();
		var errorObj;
		try {
			errorObj = new flensed.error(arguments[0],arguments[1],arguments[2],publicAPI);
		}
		catch (err) {
			function ErrorObjTemplate() { this.number=0;this.name="flXHR Error: Unknown";this.description="Unknown error from 'flXHR' library.";this.message=this.description;this.srcElement=publicAPI;var a=this.number,b=this.name,c=this.description;function toString() { return a+", "+b+", "+c; } this.toString=toString; }
			errorObj = new ErrorObjTemplate();
		}
		var handled = JSFALSE;
		try { 
			if (typeof onerror === JSFUNC) { onerror(errorObj); handled = JSTRUE; }
		}
		catch (err2) { 
			var prevError = errorObj.toString();
			function ErrorObjTemplate2() { this.number=_flxhr.HANDLER_ERROR;this.name="flXHR::onerror(): Error";this.description="An error occured in the handler function. ("+err2.message+")\nPrevious:["+prevError+"]";this.message=this.description;this.srcElement=publicAPI;var a=this.number,b=this.name,c=this.description;function toString() { return a+", "+b+", "+c; } this.toString=toString; }
			errorObj = new ErrorObjTemplate2();
		}

		if (!handled) {
			setTimeout(function() { flensed.throwUnhandledError(errorObj.toString()); },1);
		}
	}

	function doTimeout() {
		abort();	// calls clearIntervals()
		if (typeof ontimeout === JSFUNC) {
			try { ontimeout(publicAPI); }
			catch (err) {
				doError(_flxhr.HANDLER_ERROR,"flXHR::ontimeout(): Error","An error occurred in the handler function. ("+err.message+")");
				return;
			}
		}
		else { doError(_flxhr.TIMEOUT_ERROR,"flXHR: Operation Timed out","The requested operation timed out."); }
	}

	function clearIntervals() {
		clearTimeout(constructInterval);
		constructInterval = null;
		clearTimeout(timeoutInterval);
		timeoutInterval = null;
		clearTimeout(notReadyInterval);
		notReadyInterval = null;
	}

	function addToQueue(func,funcName,args) {
		constructQueue[constructQueue.length] = { func:func, funcName:funcName, args:args };
		queue_empty = false;
	}
		
	function emptyQueue() {
		if (!queue_empty) {
			queue_empty = true;
			var queuelength = constructQueue.length;
			for (var m=0; m<queuelength; m++) {
				try { constructQueue[m] = JSFALSE; }
				catch (err) { }
			}
			constructQueue = [];
		}
	}

	function executeQueue() {
		if (readyState < 0) {
			notReadyInterval = setTimeout(executeQueue,25);
			return;
		}
		if (!queue_empty) {
			for (var j=0; j<constructQueue.length; j++) {
				try {
					if (constructQueue[j] !== JSFALSE) {
						constructQueue[j].func.apply(publicAPI,constructQueue[j].args);
						constructQueue[j] = JSFALSE;
					}
				}
				catch (err) {
					doError(_flxhr.HANDLER_ERROR,"flXHR::"+constructQueue[j].funcName+"(): Error","An error occurred in the "+constructQueue[j].funcName+"() function."); 
					return;
				}
			}
			queue_empty = true;
		}
	}

	function updatePublicAPI() {
		try {
			publicAPI.instanceId = instanceId;
			publicAPI.readyState = readyState;
			publicAPI.status = status;
			publicAPI.statusText = statusText;
			publicAPI.responseText = responseText;
			publicAPI.responseXML = responseXML;
			publicAPI.responseBody = responseBody;
			publicAPI.onreadystatechange = onreadystatechange;
			publicAPI.onerror = onerror;
			publicAPI.ontimeout = ontimeout;
			publicAPI.loadPolicyURL = loadPolicyURL;
			publicAPI.binaryResponseBody = binaryResponseBody;
			publicAPI.xmlResponseText = xmlResponseText;
		}
		catch (err) { }
	}

	function updateFromPublicAPI() {
		try {
			instanceId = publicAPI.instanceId;
			sendTimeout = publicAPI.timeout;
			onreadystatechange = publicAPI.onreadystatechange;
			onerror = publicAPI.onerror;
			ontimeout = publicAPI.ontimeout;
			if (publicAPI.loadPolicyURL !== null) { 
				if ((publicAPI.loadPolicyURL !== loadPolicyURL) && (readyState >= 0)) { swfObj.loadPolicy(publicAPI.loadPolicyURL); }
				loadPolicyURL = publicAPI.loadPolicyURL;
			}
			if ((publicAPI.binaryResponseBody !== binaryResponseBody) && (readyState >= 0)) { swfObj.returnBinaryResponseBody(publicAPI.binaryResponseBody); }
			binaryResponseBody = publicAPI.binaryResponseBody;
			xmlResponseText = publicAPI.xmlResponseText;
		}
		catch (err) { }
	}

	function reset() {
		abort();
		try { swfObj.reset(); } catch (err) { }
		readyState = 0;
		status = null;
		statusText = null;
		responseText = null;
		responseXML = null;
		responseBody = null;
		rawresponse = null;
		updatePublicAPI();
	}

	function checkCallback(checkObj) {
		if (checkObj.checkPassed) {
			stillContinueConstructor();
		}
		else if (!autoUpdatePlayer) {
			doError(_flxhr.PLAYER_VERSION_ERROR,"flXHR: Insufficient Flash Player Version","The Flash Player was either not detected, or the detected version ("+checkObj.playerVersionDetected+") was not at least the minimum version ("+_flxhr.MIN_PLAYER_VERSION+") needed by the 'flXHR' library.");
		}
		else {
			_flxhr._cpobj.UpdatePlayer();
		}
	}

	function updateCallback(checkObj) {
		if (checkObj.updateStatus === _chkplyr.UPDATE_SUCCESSFUL) {
			try {
				win.open(EMPTY,"_self",EMPTY);	// tricky IE syntax to force a self-close of window
				win.close();
				self.opener = win;
				self.close();
			}
			catch (err) { }
		}
		else if (checkObj.updateStatus === _chkplyr.UPDATE_CANCELED) {
			doError(_flxhr.PLAYER_VERSION_ERROR,"flXHR: Flash Player Update Canceled","The Flash Player was not updated.");
		}
		else if (checkObj.updateStatus === _chkplyr.UPDATE_FAILED) {
			doError(_flxhr.PLAYER_VERSION_ERROR,"flXHR: Flash Player Update Failed","The Flash Player was either not detected or could not be updated.");
		}
	}
	
	function sendProcessed() {
		if (sendTimeout > 0) { timeoutInterval = setTimeout(doTimeout,sendTimeout); }
	}

	// Private Methods (XHR API functions)
	function abort() {
		clearIntervals();
		emptyQueue();
		updateFromPublicAPI();
		try { swfObj.abort(); } 
		catch (err) { doError(_flxhr.CALL_ERROR,"flXHR::abort(): Failed","The abort() call failed to complete."); }
	}

	function open() {
		updateFromPublicAPI();
		if (readyState > 0) { reset(); loadPolicyURL = ""; updateFromPublicAPI(); }	// non-standard to API, a convenience perhaps?
		try { swfObj.open(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]); } 
		catch (err) { doError(_flxhr.CALL_ERROR,"flXHR::open(): Failed","The open() call failed to complete."); }
	}

	function send() {
		updateFromPublicAPI();
		if (readyState <= 1) {	// the SWF will actually catch if the open() hasn't yet been called (ie, readyState = 0)
			try { swfObj.send(arguments[0]); } // flXHR.swf will call sendProcessed() once it begins processing
			catch (err) { doError(_flxhr.CALL_ERROR,"flXHR::send(): Failed","The send() call failed to complete."); }
		}
		else {
			doError(_flxhr.CALL_ERROR,"flXHR::send(): Failed","The send() call cannot be made at this time.");
		}
	}

	function setRequestHeader() {
		updateFromPublicAPI();
		if (readyState === 1) {	// per the native XHR API specs, setRequestHeader can only be set after open (but before send)
			try { swfObj.setRequestHeader(arguments[0],arguments[1]); } 
			catch (err) { doError(_flxhr.CALL_ERROR,"flXHR::setRequestHeader(): Failed","The setRequestHeader() call failed to complete."); }
		}
		else {
			doError(_flxhr.CALL_ERROR,"flXHR::setRequestHeader(): Failed","The setRequestHeader() call cannot be made at this time.");
		}
	}

	function getResponseHeader() { updateFromPublicAPI(); return EMPTY; }

	function getAllResponseHeaders() { updateFromPublicAPI(); return []; }	

	// Public API
	publicAPI = {
		// XHR API
		readyState:readyState,
		responseBody:responseBody,
		responseText:responseText,
		responseXML:responseXML,
		status:status,
		statusText:statusText,
		timeout:sendTimeout,
		open:function() {
			updateFromPublicAPI();
			if (!queue_empty || readyState < 0) {
				addToQueue(open,"open",arguments);
				return;
			}
			open.apply({},arguments);
		},
		send:function() {
			updateFromPublicAPI();
			if (!queue_empty || readyState < 0) {
				addToQueue(send,"send",arguments);
				return;
			}
			send.apply({},arguments);
		},
		abort:abort,
		setRequestHeader:function() {
			updateFromPublicAPI();
			if (!queue_empty || readyState < 0) {
				addToQueue(setRequestHeader,"setRequestHeader",arguments);
				return;
			}
			setRequestHeader.apply({},arguments);
		},
		getResponseHeader:getResponseHeader,
		getAllResponseHeaders:getAllResponseHeaders,
		onreadystatechange:onreadystatechange,
		ontimeout:ontimeout,

		// extended API
		instanceId:instanceId,
		loadPolicyURL:loadPolicyURL,
		binaryResponseBody:binaryResponseBody,
		xmlResponseText:xmlResponseText,
		onerror:onerror,
		Configure:function(configObj) {
			if (typeof configObj !== UNDEF && configObj !== null && configObj === OBJECT) {
				if ((typeof configObj.instanceId !== UNDEF) && (configObj.instanceId !== null) && (configObj.instanceId !== EMPTY)) { instanceId = configObj.instanceId; }
				if (typeof configObject.binaryResponseBody !== UNDEF) { 
					binaryResponseBody = !(!configObject.binaryResponseBody); 
					if (readyState >= 0) { swfObj.returnBinaryResponseBody(binaryResponseBody); }
				}
				if (typeof configObject.xmlResponseText !== UNDEF) { xmlResponseText = !(!configObject.xmlResponseText); }
				if ((typeof configObj.onreadystatechange !== UNDEF) && (configObj.onreadystatechange !== null)) { onreadystatechange = configObj.onreadystatechange; }
				if ((typeof configObj.onerror !== UNDEF) && (configObj.onerror !== null)) { onerror = configObj.onerror; }
				if ((typeof configObj.ontimeout !== UNDEF) && (configObj.ontimeout !== null)) { ontimeout = configObj.ontimeout; }
				if ((typeof configObj.sendTimeout !== UNDEF) && (parseInt(configObj.sendTimeout,10) > 0)) { sendTimeout = parseInt(configObj.sendTimeout,10); }
				if ((typeof configObj.loadPolicyURL !== UNDEF) && (configObj.loadPolicyURL !== null) && (configObj.loadPolicyURL !== EMPTY) && (configObj.loadPolicyURL !== loadPolicyURL)) {
					loadPolicyURL = configObj.loadPolicyURL;
					if (readyState >= 0) { swfObj.loadPolicy(loadPolicyURL); }
				}
				updatePublicAPI();
			}
		},
		Reset:reset,
		Destroy:destructor
	};
	if (instancePooling) { _flxhr._inp[_flxhr._inp.length] = publicAPI; }
	return publicAPI;
};

// Static Properties
flensed.flXHR.HANDLER_ERROR = 10;
flensed.flXHR.CALL_ERROR = 11;
flensed.flXHR.TIMEOUT_ERROR = 12;
flensed.flXHR.DEPENDENCY_ERROR = 13;
flensed.flXHR.PLAYER_VERSION_ERROR = 14;
flensed.flXHR.SECURITY_ERROR = 15;
flensed.flXHR.COMMUNICATION_ERROR = 16;
flensed.flXHR.MIN_PLAYER_VERSION = "9.0.124";
flensed.flXHR.module_ready = function() {};
