/*	CheckPlayer 0.7 alpha4 <http://www.checkplayer.com/>
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
	var this_script = "checkplayer.js",		// DO NOT rename this file or change this line.
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

	try { swfobject.getObjectById("a"); } catch (err2) { load_script("swfobject.js","text/javascript"); }
	try { flensed.ua.pv.join("."); } catch (err3) { load_script("flensed.js","text/javascript"); }

	var dependencyTimeout = setTimeout(function(){
		try { win.detachEvent("onunload",clearDependencyTimeout); } catch (err) { }
		try { swfobject.getObjectById("a"); flensed.ua.pv.join("."); } catch (err2) { throw new Error("CheckPlayer dependencies failed to load."); }
	},10000);	// only wait 10 secs max for swfobject and flensedCore to load
	function clearDependencyTimeout() { clearTimeout(dependencyTimeout); try { win.detachEvent("onunload",clearDependencyTimeout); } catch (err) { } }
	try { win.attachEvent("onunload",clearDependencyTimeout); } catch (err4) { }	// only IEwin would leak memory this way
},0);

flensed.checkplayer = function(playerVersionCheck,checkCB,autoUpdate,updateCB) {
	var _chkplyr = flensed.checkplayer;	// for compression optimization
	if (typeof _chkplyr._instance !== "undefined") { return _chkplyr._instance; }

	// Private Properties
	var	MIN_XI_VERSION = "6.0.65",
	 	UNDEF = "undefined",
		EMPTY = "",
		OBJECT = "object",
		JSFUNC = "function",
		JSSTR = "string",
		JSTRUE = true,
		JSFALSE = false,
		win = window,
		doc = document,
		updateInterval = [],
		notReadyInterval = null,
		updateCalled = JSFALSE,
		constructInterval = null,
		updateSWFId = null,
		updateContainerId = EMPTY,
		ready = JSFALSE,
		updateObj = null,
		swfIdArr = {},
		swfIntArr = {},
		swfQueue = [],

	// Configurable Properties (via new() constructor)
		versionToCheck = null,
		checkCallback = null,
		updateCallback = null,
		updateFlash = JSFALSE,

	// Properties Exposed (indirectly, read-only) in Public API
		flashVersionDetected = null,
		versionCheckPassed = JSFALSE,
		updateable = JSFALSE,
		updateStatus = JSFALSE,
		holder = null;

	// Private Methods
	var constructor = function() {
		if ((typeof playerVersionCheck !== UNDEF) && (playerVersionCheck !== null) && (playerVersionCheck !== JSFALSE)) { versionToCheck = playerVersionCheck + EMPTY; }	// convert to string
		else { versionToCheck = "0.0.0"; }
		if (typeof checkCB === JSFUNC) { checkCallback = checkCB; }
		if (typeof autoUpdate !== UNDEF) { updateFlash = !(!autoUpdate); }	// convert to boolean
		if (typeof updateCB === JSFUNC) { updateCallback = updateCB; }

		function clearConstructInterval() { clearTimeout(constructInterval); try { win.detachEvent("onunload",clearConstructInterval); } catch (err) { } }
		try { win.attachEvent("onunload",clearConstructInterval); } catch (err) { }	// only IEwin would leak memory this way
		(function waitForCore() {
			try { flensed.bindEvent(win,"onunload",destructor); } catch (err) { constructInterval = setTimeout(waitForCore,25); return; }
			try { win.detachEvent("onunload",clearConstructInterval); } catch (err2) { }
			flashVersionDetected = flensed.ua.pv.join(".");
			constructInterval = setTimeout(continueConstructor,25);
		})();
	}();

	function continueConstructor() {
		var appendTo = doc.getElementsByTagName("body")[0];

		if ((typeof appendTo === UNDEF) || (appendTo === null)) {
			constructInterval = setTimeout(continueConstructor,25);
			return;
		}
		try { swfobject.getObjectById("a"); } catch (swfobject_err) {
			constructInterval = setTimeout(continueConstructor,25);
			return;
		}

		updateable = swfobject.hasFlashPlayerVersion(MIN_XI_VERSION);
		versionCheckPassed = swfobject.hasFlashPlayerVersion(versionToCheck);
		updatePublicAPI();

		ready = JSTRUE;
		if (typeof checkCallback === JSFUNC) { checkCallback(publicAPI); }

		if (versionCheckPassed) { executeQueue(); }
		else if (updateFlash) { updateFlashPlayer(); }
	}

	function destructor() {
		if (typeof win.detachEvent !== UNDEF) { win.detachEvent("onunload",destructor); }
		_chkplyr._instance = null;
		if ((typeof updateObj !== UNDEF) && (updateObj !== null)) {
			try { updateObj.updateSWFCallback = null; updateSWFCallback = null; } catch(err) { }
			updateObj = null;
		}
		try {
			for (var k in publicAPI) {
				if (publicAPI[k] !== Object.prototype[k]) {
					try { publicAPI[k] = null; } catch (err2) { }
				}
			}
		}
		catch (err3) { }
		publicAPI = null;

		win = null;
		doc = null;
		clearIntervals();
		swfQueue = null;
		checkCallback = null;
		updateCallback = null;

		try {
			for (var n in _chkplyr) {
				if (_chkplyr[n] !== Object.prototype[n]) {
					try { _chkplyr[n] = null; } catch (err4) { }
				}
			}
		}
		catch (err5) { }
		flensed.checkplayer = null;
	}

	function addToQueue(func,funcName,args) {
		swfQueue[swfQueue.length] = { func:func, funcName:funcName, args:args };
	}

	function executeQueue() {
		if (!ready) {
			notReadyInterval = setTimeout(executeQueue,25);
			return;
		}
		var swfQueueLength = 0;
		try { swfQueueLength = swfQueue.length; } catch (err) { }
		for (var j=0; j<swfQueueLength; j++) {
			try {
				swfQueue[j].func.apply(publicAPI,swfQueue[j].args);
				swfQueue[j] = JSFALSE;
			}
			catch (err2) {
				versionCheckPassed = JSFALSE;
				updatePublicAPI();

				if (typeof checkCallback === JSFUNC) { checkCallback(publicAPI); }
				else { throw new Error("checkplayer::"+swfQueue[j].funcName+"() call failed."); }
			}
		}
		swfQueue = null;
	}

	function clearIntervals() {
		clearTimeout(constructInterval);
		constructInterval = null;
		clearTimeout(notReadyInterval);
		notReadyInterval = null;
		for (var j in swfIntArr) {
			if (swfIntArr[j] !== Object.prototype[j]) {
				clearInterval(swfIntArr[j]);
				swfIntArr[j] = 0;
			}
		}
		for (var k in updateInterval) {
			if (updateInterval[k] !== Object.prototype[k]) {
				clearTimeout(updateInterval[k]);
				updateInterval[k] = 0;
			}
		}
	}

	function updatePublicAPI() {
		try {
			publicAPI.playerVersionDetected = flashVersionDetected;
			publicAPI.checkPassed = versionCheckPassed;
			publicAPI.updateable = updateable;
			publicAPI.updateStatus = updateStatus;
			publicAPI.updateControlsContainer = holder;
		}
		catch (err) { }
	}

	function setVisibility(id, isVisible) {
		var v = isVisible ? "visible" : "hidden";
		var obj = flensed.getObjectById(id);
		try {
			if (obj !== null && (typeof obj.style !== UNDEF) && (obj.style !== null)) { obj.style.visibility = v; }
			else { 
				try { swfobject.createCSS("#" + id, "visibility:" + v); } catch (err) { }
			}
		}
		catch (err2) { 
			try { swfobject.createCSS("#" + id, "visibility:" + v); } catch (err3) { }
		}
	}

	function updateFlashPlayer() {
		var appendTo = doc.getElementsByTagName("body")[0];

		if ((typeof appendTo === UNDEF) || (appendTo === null)) {
			updateInterval[updateInterval.length] = setTimeout(updateFlashPlayer,25);
			return;
		}
		try { swfobject.getObjectById("a"); } catch (swfobject_err) {
			updateInterval[updateInterval.length] = setTimeout(updateFlashPlayer,25);
			return;
		}
		
		if (!updateCalled) {
			updateCalled = JSTRUE;
			clearIntervals();
			if (updateable) {
				updateContainerId = "CheckPlayerUpdate";
				updateSWFId = updateContainerId + "SWF";

				try {
					swfobject.createCSS("#"+updateContainerId,"width:315px;height:139px;position:absolute;left:5px;top:5px;border:1px solid #000000;background-color:#ffffff;text-align:center;display:block;padding-top:2px;");
					swfobject.createCSS("#"+updateSWFId,"display:inline");
				}
				catch (err) { updateFailed("A call to the 'swfobject' library failed."); return; }

				holder=doc.createElement("div");
				holder.id = updateContainerId;
				appendTo.appendChild(holder);
				setVisibility(holder.id,JSFALSE);

				updatePublicAPI();

				var flashvars = { swfId:updateSWFId, MMredirectURL:win.location.toString().replace(/&/g,"%26"), MMplayerType:(flensed.ua.ie && flensed.ua.win ? "ActiveX" : "PlugIn"), MMdoctitle:doc.title.slice(0, 47) + " - Flash Player Installation" };
				var params = { allowScriptAccess:"always" };
				var attributes = { id:updateSWFId, name:updateSWFId };

				try {
					doSWF(flensed.base_path+"updateplayer.swf", {appendToId:updateContainerId}, "310", "137", flashvars, params, attributes, {swfTimeout:3000,swfCB:continueUpdate}, JSTRUE);
				}
				catch (err2) { updateFailed(); return; }
			}
			else { updateFailed(); }
		}
	}

	function updateFailed(errMsg) {
		if (typeof errMsg === UNDEF) { errMsg = "Flash Player not detected or not updateable."; }
		updateStatus = _chkplyr.UPDATE_FAILED;
		updatePublicAPI();
		if (typeof updateCallback === JSFUNC) { updateCallback(publicAPI); }
		else {
			throw new Error("checkplayer::UpdatePlayer(): "+errMsg);
		}
	}

	function continueUpdate(loadStatus) {
		if (loadStatus.status === _chkplyr.SWF_LOADED) {
			clearTimeout(swfIntArr["continueUpdate::"+updateSWFId]);
			updateObj = loadStatus.srcElem;
			updateObj.updateSWFCallback = updateSWFCallback;

			updateStatus = _chkplyr.UPDATE_INIT;
			updatePublicAPI();
			if (typeof updateCallback === JSFUNC) { updateCallback(publicAPI); }
			setVisibility(holder.id,JSTRUE);
		}
		else if (loadStatus.status === _chkplyr.SWF_FAILED || loadStatus.status === _chkplyr.SWF_TIMEOUT) {
			updateFailed();
		}
	}

	function updateSWFCallback(statusCode) {
		try {
			if (statusCode === 0) {			// update successful
				updateStatus = _chkplyr.UPDATE_SUCCESSFUL;
				holder.style.display = "none";
			}
			else if (statusCode === 1) {	// user canceled
				updateStatus = _chkplyr.UPDATE_CANCELED;
				holder.style.display = "none";
			}
			else if (statusCode === 2) {	// update failed
				holder.style.display = "none";
				updateFailed("The Flash Player update failed.");
				return;
			}
			else if (statusCode === 3) {	// update timeout
				holder.style.display = "none";
				updateFailed("The Flash Player update timed out.");
				return;
			}
		}
		catch (err) { }

		updatePublicAPI();

		if (typeof updateCallback === JSFUNC) { updateCallback(publicAPI); }
	}

	function doSWF(swfUrlStr, targetElem, widthStr, heightStr, flashvarsObj, parObj, attObj, optObj, ignoreVersionCheck) {
		if (targetElem !== null && (typeof targetElem === JSSTR || typeof targetElem.replaceId === JSSTR)) { setVisibility(((typeof targetElem === JSSTR)?targetElem:targetElem.replaceId),JSFALSE); }
		if (!ready) {
			addToQueue(doSWF,"DoSWF",arguments);
			return;
		}
		
		if (versionCheckPassed || ignoreVersionCheck) {
			widthStr += EMPTY; // Auto-convert to string to make it idiot proof
			heightStr += EMPTY;

			var att = (typeof attObj === OBJECT) ? attObj : {};
			att.data = swfUrlStr;
			att.width = widthStr;
			att.height = heightStr;
			var par = (typeof parObj === OBJECT) ? parObj : {};
			if (typeof flashvarsObj === OBJECT) {
				for (var i in flashvarsObj) {
					if (flashvarsObj[i] !== Object.prototype[i]) { // Filter out prototype additions from other potential libraries
						if (typeof par.flashvars !== UNDEF) {
							par.flashvars += "&" + i + "=" + flashvarsObj[i];
						}
						else {
							par.flashvars = i + "=" + flashvarsObj[i];
						}
					}
				}
			}

			var swfId = null;
			if (typeof attObj.id !== UNDEF) { swfId = attObj.id; }
			else if (targetElem !== null && (typeof targetElem === JSSTR || typeof targetElem.replaceId === JSSTR)) { swfId = ((typeof targetElem === JSSTR)?targetElem:targetElem.replaceId); }
			else { swfId = "swf_"+swfIdArr.length; }
			
			var replaceId = null;
			if (targetElem === null || targetElem === JSFALSE || typeof targetElem.appendToId === JSSTR) {
				var appendTo = null;
				if (targetElem !== null && targetElem !== JSFALSE && typeof targetElem.appendToId === JSSTR) { appendTo = flensed.getObjectById(targetElem.appendToId); }
				else { appendTo = doc.getElementsByTagName("body")[0]; }
				var targetObj = doc.createElement("div");
				replaceId = (targetObj.id = swfId);
				appendTo.appendChild(targetObj);
			}
			else { replaceId = ((typeof targetElem.replaceId === JSSTR) ? targetElem.replaceId : targetElem); }
			
			var swfCB = function(){}, swfTimeout = 0, swfEICheck = EMPTY, srcelem = null;
			if (typeof optObj !== UNDEF && optObj !== null) {
				if (typeof optObj === OBJECT) {
					if (typeof optObj.swfCB !== UNDEF && optObj.swfCB !== null) { swfCB = optObj.swfCB; }
					if (typeof optObj.swfTimeout !== UNDEF && (parseInt(optObj.swfTimeout,10) > 0)) { swfTimeout = optObj.swfTimeout; }
					if (typeof optObj.swfEICheck !== UNDEF && optObj.swfEICheck !== null && optObj.swfEICheck !== EMPTY) { swfEICheck = optObj.swfEICheck; }
				}
				else if (typeof optObj === JSFUNC) { swfCB = optObj; }
			}

			try { srcelem = swfobject.createSWF(att, par, replaceId); }
			catch (err) { srcelem = null; }
			
			if (srcelem !== null) {
				swfIdArr[swfIdArr.length] = swfId;
				if (typeof swfCB === JSFUNC) {
					swfCB({status:_chkplyr.SWF_INIT,srcId:swfId,srcElem:srcelem});
					swfIntArr[swfId] = setInterval(function() {
						var theObj = flensed.getObjectById(swfId);
						if ((typeof theObj !== UNDEF) && (theObj !== null) && (theObj.nodeName === "OBJECT" || theObj.nodeName === "EMBED")) {
							var perloaded = 0;
							try { perloaded = theObj.PercentLoaded(); } catch (err) { }
							if (perloaded > 0) {
								if (swfTimeout > 0) { clearTimeout(swfIntArr["DoSWFtimeout::"+swfId]); }
								if (perloaded < 100) {
									// prevent swfCB from blocking this interval call if the user-defined function is long running
									setTimeout(function(){swfCB({status:_chkplyr.SWF_LOADING,srcId:swfId,srcElem:theObj});},1);
								}
								else {
									clearInterval(swfIntArr[swfId]);
									// force redraw of SWF (some weird browser rendering issues)
									setVisibility(swfId,JSFALSE);
									setVisibility(swfId,JSTRUE);
									// prevent swfCB from blocking this interval call if the user-defined function is long running
									setTimeout(function(){swfCB({status:_chkplyr.SWF_LOADED,srcId:swfId,srcElem:theObj});},1);
									
									if (swfEICheck !== EMPTY) {
										swfIntArr[swfId] = setInterval(function() {
											if (typeof theObj[swfEICheck] === JSFUNC) {
												var checkdone = JSFALSE;
												try { theObj[swfEICheck](); checkdone = JSTRUE; } catch (err) { }
												if (checkdone) {
													clearInterval(swfIntArr[swfId]);
													swfCB({status:_chkplyr.SWF_EI_READY,srcId:swfId,srcElem:theObj});
												}
											}
										},100);
									}
								}
							}
						}
					},100);
					if (swfTimeout > 0) {
						swfIntArr["DoSWFtimeout::"+swfId] = setTimeout(function() {
							var theObj = flensed.getObjectById(swfId);
							if ((typeof theObj !== UNDEF) && (theObj !== null) && (theObj.nodeName === "OBJECT" || theObj.nodeName === "EMBED")) {
								var perloaded = 0;
								try { perloaded = theObj.PercentLoaded(); } catch (err) { }
								if (perloaded <= 0) {
									clearInterval(swfIntArr[swfId]);
									if ((flensed.ua.ie && flensed.ua.win) && (theObj.readyState !== 4)) {
										theObj.id = "removeSWF::"+theObj.id;
										theObj.style.display = "none";
										swfIntArr[theObj.id] = setInterval(function() {
											if (theObj.readyState === 4) {
												clearInterval(swfIntArr[theObj.id]);
												swfobject.removeSWF(theObj.id);
											}
										},100);
									}
									else { swfobject.removeSWF(theObj.id); }
									swfIntArr[swfId] = null;
									swfIntArr["DoSWFtimeout::"+swfId] = null;
									swfCB({status:_chkplyr.SWF_TIMEOUT,srcId:swfId,srcElem:theObj});
								}
							}
						},swfTimeout);
					}
				}
				if (swfId === replaceId) { setVisibility(swfId,JSTRUE); }
			}
			else {
				if (typeof swfCB === JSFUNC) { swfCB({status:_chkplyr.SWF_FAILED,srcId:swfId,srcElem:null}); }
				else { throw new Error("checkplayer::DoSWF(): SWF could not be loaded."); }
			}
		}
		else {
			if (typeof swfCB === JSFUNC) { swfCB({status:_chkplyr.SWF_FAILED,srcId:swfId,srcElem:null}); }
			else { throw new Error("checkplayer::DoSWF(): Minimum Flash Version not detected."); }
		}
	}

	// Public API
	var publicAPI = {
		playerVersionDetected:flashVersionDetected,
		versionChecked:versionToCheck,
		checkPassed:versionCheckPassed,

		UpdatePlayer:updateFlashPlayer,
		DoSWF:function(swfUrlStr, targetElem, widthStr, heightStr, flashvarsObj, parObj, attObj, optObj) {
			doSWF(swfUrlStr,targetElem,widthStr,heightStr,flashvarsObj,parObj,attObj,optObj,JSFALSE);
		},

		updateable:updateable,
		updateStatus:updateStatus,
		updateControlsContainer:holder
	};
	_chkplyr._instance = publicAPI;
	return publicAPI;
};

flensed.checkplayer.UPDATE_INIT = 1;
flensed.checkplayer.UPDATE_SUCCESSFUL = 2;
flensed.checkplayer.UPDATE_CANCELED = 3;
flensed.checkplayer.UPDATE_FAILED = 4;
flensed.checkplayer.SWF_INIT = 5;
flensed.checkplayer.SWF_LOADING = 6;
flensed.checkplayer.SWF_LOADED = 7;
flensed.checkplayer.SWF_FAILED = 8;
flensed.checkplayer.SWF_TIMEOUT = 9;
flensed.checkplayer.SWF_EI_READY = 10;
flensed.checkplayer.module_ready = function() {};
