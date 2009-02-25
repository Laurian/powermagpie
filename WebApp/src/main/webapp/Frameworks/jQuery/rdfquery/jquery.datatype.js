/*
 * jQuery CURIE @VERSION
 * 
 * Copyright (c) 2008 Jeni Tennison
 * Licensed under the MIT (MIT-LICENSE.txt)
 *
 * Depends:
 *	jquery.uri.js
 */
/*global jQuery */
(function ($) {

	var
		leadingTrailingSpace = /^[\u0020\n\r\t]+|[\u0020\n\r\t]+$/,
		internalSpace = /[\u0020\n\r\t]+/;

	$.typedValue = function (value, datatype) {
		return $.typedValue.fn.init(value, datatype);
	};
	
	$.typedValue.fn = $.typedValue.prototype = {
		representation: undefined,
		value: undefined,
		datatype: undefined,
		
		init: function (value, datatype) {
			if ($.typedValue.valid(value, datatype)) {
				this.representation = value;
				this.datatype = datatype;
				this.value = $.typedValue.types[datatype].value(value);
				return this;
			} else {
				throw {
					name: 'InvalidValue',
					message: value + ' is not a valid ' + datatype + ' value'
				};
			}
		}
	};
	
	$.typedValue.fn.init.prototype = $.typedValue.fn;

	$.typedValue.types = {};

	$.typedValue.types['http://www.w3.org/2001/XMLSchema#string'] = {
		regex: /^.*$/,
		strip: false,
		value: function (v) {
			return v;
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#boolean'] = {
		regex: /^(?:true|false|1|0)$/,
		strip: true,
		value: function (v) {
			return v === 'true' || v === '1';
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#decimal'] = {
		regex: /^[\-\+]?(?:[0-9]+\.[0-9]*|\.[0-9]+|[0-9]+)$/,
		strip: true,
		value: function (v) {
			return v;
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#integer'] = {
		regex: /^[\-\+]?[0-9]+$/,
		strip: true,
		value: function (v) {
			return parseInt(v);
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#float'] = {
		regex: /^(?:[\-\+]?(?:[0-9]+\.[0-9]*|\.[0-9]+|[0-9]+)(?:[eE][\-\+]?[0-9]+)?|[\-\+]?INF|NaN)$/,
		strip: true,
		value: function (v) {
			if (v === '-INF') {
				return -1 / 0;
			} else if (v === 'INF' || v === '+INF') {
				return 1 / 0;
			} else {
				return parseFloat(v);
			}
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#double'] = {
		regex: $.typedValue.types['http://www.w3.org/2001/XMLSchema#float'].regex,
		strip: true,
		value: $.typedValue.types['http://www.w3.org/2001/XMLSchema#float'].value
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#duration'] = {
		regex: /^([\-\+])?P(?:([0-9]+)Y)?(?:([0-9]+)M)?(?:([0-9]+)D)?(?:T(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+(?:\.[0-9]+))?S)?)$/,
		validate: function (v) {
			var m = this.regex.exec(v);
			return m[2] || m[3] || m[4] || m[5] || m[6] || m[7];
		},
		strip: true,
		value: function (v) {
			return v;
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#dateTime'] = {
		regex: /^(-?[0-9]{4,})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):(([0-9]{2})(\.([0-9]+))?)((?:[\-\+]([0-9]{2}):([0-9]{2}))|Z)?$/,
		validate: function (v) {
			var 
				m = this.regex.exec(v),
				year = parseInt(m[1]),
				tz = m[10] === undefined || m[10] === 'Z' ? '+0000' : m[10].replace(/:/, ''),
				date, ms;
			if (year === 0 ||
				  parseInt(tz) < -1400 || parseInt(tz) > 1400) {
				return false;
			}
			try {
				year = year < 100 ? Math.abs(year) + 1000 : year;
				date = '' + year + '/' + m[2] + '/' + m[3] + ' ' + m[4] + ':' + m[5] + ':' + m[7] + ' ' + tz;
				date = new Date(date);
				return true;
			} catch (e) {
				return false;
			}
		},
		strip: true,
		value: function (v) {
			return v;
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#date'] = {
		regex: /^(-?[0-9]{4,})-([0-9]{2})-([0-9]{2})((?:[\-\+]([0-9]{2}):([0-9]{2}))|Z)?$/,
		validate: function (v) {
			var 
				m = this.regex.exec(v),
				year = parseInt(m[1]),
				month = parseInt(m[2]),
				day = parseInt(m[3]),
				tz = m[10] === undefined || m[10] === 'Z' ? '+0000' : m[10].replace(/:/, '');
			if (year === 0 ||
				  month > 12 ||
				  day > 31 ||
				  parseInt(tz) < -1400 || parseInt(tz) > 1400) {
				return false;
			} else {
				return true;
			}
		},
		strip: true,
		value: function (v) {
			return v;
		}
	};
	
	$.typedValue.types['http://www.w3.org/2001/XMLSchema#anyURI'] = {
		regex: /^.*$/,
		strip: true,
		value: function (v, options) {
			var opts = $.extend({}, $.xsd.defaults, options);
			return $.uri.resolve(v, opts.base);
		}
	};

	$.typedValue.defaults = {
		base: $.uri.base(),
		namespaces: {}
	};

	$.typedValue.valid = function (value, datatype) {
		var d = $.typedValue.types[datatype];
		if (d === undefined) {
			throw {
				name: "InvalidDatatype",
				message: "The datatype " + datatype + " hasn't been registered"
			}
		} else if (d.regex.test(value)) {
			return d.validate === undefined ? true : d.validate(value);
		} else {
			return false;
		}
	};

})(jQuery);
