/*
 * jQuery URIs @VERSION
 * 
 * Copyright (c) 2008 Jeni Tennison
 * Licensed under the MIT (MIT-LICENSE.txt)
 *
 */
/*global jQuery */
(function ($) {

	var
		mem = {},
		schemeRegex = /^([a-z][\-a-z0-9+\.]*):(.+)$/i,
		authorityRegex = /^\/\/([^\/?#]+)(.*)$/,
		pathRegex = /^([^?#]*)(.*)$/,
		queryRegex = /^\?([^#]*)(.*)$/,
		fragmentRegex = /^#(.*)$/,

		scheme = function (u) {
			var m = [];
			if (!schemeRegex.test(u)) {
				throw {
					name: 'MalformedURI',
					message: 'Bad scheme in "' + u + '"'
				};
			}
			m = schemeRegex.exec(u);
			return {
				scheme: m[1].toLowerCase(),
				rest: m[2]
			};
		},

		authority = function (u) {
			var m = [];
			if (authorityRegex.test(u)) {
				m = u.match(authorityRegex);
				return {
					authority: m[1],
					rest: m[2]
				};
			} else {
				return {
					rest: u
				};
			}
		},

		path = function (u) {
		  if (typeof u === 'string') {
  			var m = u.match(pathRegex);
  			return {
  				path: m[1],
  				rest: m[2]
  			};
		  } else {
		    throw {
		      name: 'MalformedURI',
		      message: 'Could not parse "' + '"'
		    };
		  }
		},

		query = function (u) {
			var m = [];
			if (queryRegex.test(u)) {
				m = u.match(queryRegex);
				return {
					query: m[1],
					rest: m[2]
				};
			} else {
				return {
					rest: u
				};
			}
		},

		fragment = function (u) {
			var m = [];
			if (fragmentRegex.test(u)) {
				m = u.match(fragmentRegex);
				return m[1];
			} else {
				return undefined;
			}
		},

		removeDotSegments = function (u) {
			var r = '', m = [];
			while (u !== undefined && u !== '') {
				if (u === '.' || u === '..') {
					u = '';
				} else if (/^\.\.\//.test(u)) { // starts with ../
					u = u.substring(3);
				} else if (/^\.\//.test(u)) { // starts with ./
					u = u.substring(2);
				} else if (/^\/\.(\/|$)/.test(u)) { // starts with /./ or consists of /.
					u = '/' + u.substring(3);
				} else if (/^\/\.\.(\/|$)/.test(u)) { // starts with /../ or consists of /..
					u = '/' + u.substring(4);
					r = r.replace(/\/?[^\/]+$/, '');
				} else {
					m = u.match(/^(\/?[^\/]*)(\/.*)?$/);
					u = m[2];
					r = r + m[1];
				}
			}
			return r;
		},

		merge = function (b, r) {
			if (b.authority !== '' && (b.path === undefined || b.path === '')) {
				return '/' + r;
			} else {
				return b.path.replace(/[^\/]+$/, '') + r;
			}
		};

	$.uri = function (relative, base) {
		relative = relative || '';
		if (mem[relative]) {
			return mem[relative];
		}
		base = base || $.uri.base();
		if (typeof base === 'string') {
			base = $.uri.absolute(base);
		}
		uri = new $.uri.fn.init(relative, base);
		if (mem[uri]) {
			return mem[uri];
		} else {
			mem[uri] = uri;
			return uri;
		}
	};

	$.uri.fn = $.uri.prototype = {
		init: function (relative, base) {
			var r = {};
			base = base || {};
			try {
				r = scheme(relative);
				this.scheme = r.scheme;
				r = authority(r.rest);
				this.authority = r.authority;
				r = path(r.rest);
				this.path = removeDotSegments(r.path);
				r = query(r.rest);
				this.query = r.query;
				r = fragment(r.rest);
				this.fragment = r;
			} catch (e) {
				if (e.name === 'MalformedURI') { // this is what we get when the URI has no scheme
					this.scheme = base.scheme;
					r = authority(relative);
					if (r.authority !== undefined) {
						this.authority = r.authority;
						r = path(r.rest);
						this.path = removeDotSegments(r.path);
						r = query(r.rest);
						this.query = r.query;
						r = fragment(r.rest);
						this.fragment = r;
					} else {
						this.authority = base.authority;
						r = path(r.rest);
						if (r.path === '') {
							this.path = base.path;
							r = query(r.rest);
							if (r.query !== undefined) {
								this.query = r.query;
							} else {
								this.query = base.query;
							}
						} else {
							if (/^\//.test(r.path)) {
								this.path = removeDotSegments(r.path);
							} else {
								this.path = merge(base, r.path);
								this.path = removeDotSegments(this.path);
							}
							r = query(r.rest);
							this.query = r.query;
						}
						r = fragment(r.rest);
						this.fragment = r;
					}
				} else {
					throw e;
				}
			}
			if (this.scheme === undefined) {
				throw {
					name: "MalformedURI",
					message: "URI is not an absolute URI and no base supplied: " + uri
				};
			}
			return this;
		},
	
		resolve: function (relative) {
			return $.uri(relative, this);
		},
	
		toString: function () {
			var result = '';
			result = this.scheme === undefined ? result : result + this.scheme + ':';
			result = this.authority === undefined ? result : result + '//' + this.authority;
			result = result + this.path;
			result = this.query === undefined ? result : result + '?' + this.query;
			result = this.fragment === undefined ? result : result + '#' + this.fragment;
			return result;
		}
	
	};

	$.uri.fn.init.prototype = $.uri.fn;

	$.uri.absolute = function (uri) {
		return $.uri(uri, {});
	};

	$.uri.resolve = function (relative, base) {
		return $.uri(relative, base);
	};
	
	$.uri.base = function () {
		var base = $('head > base').attr('href');
		return base === undefined ? $.uri.absolute(document.URL) : $.uri(base, document.URL);
	}

})(jQuery);
