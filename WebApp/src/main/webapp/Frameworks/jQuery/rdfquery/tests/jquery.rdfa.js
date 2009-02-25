/*
 * jquery.rdfa.js unit tests
 */
(function($){

var ns = { namespaces: {
	rdf: "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
	xsd: "http://www.w3.org/2001/XMLSchema#",
	dc: "http://purl.org/dc/elements/1.1/",
	foaf: "http://xmlns.com/foaf/0.1/",
	cc: "http://creativecommons.org/ns#"
}};

function setup(rdfa) {
	$('#main').html(rdfa);
};

function testTriples (received, expected) {
	var i;
	equals(received.tripleStore.length, expected.length, 'there should be ' + expected.length + ' triples');
	for (i = 0; i < expected.length; i += 1) {
		equals(received.tripleStore[i], expected[i]);
	}
};

module("RDFa Test Suite");																																																																

test("Test 0001", function() {
	setup('<p>This photo was taken by <span class="author" about="photo1.jpg" property="dc:creator">Mark Birbeck</span>.</p>');
	testTriples($('#main > p > span').rdfa(), 
	            [$.rdf.triple('<photo1.jpg> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > p').remove();
});

test("Test 0006", function() {
	setup('<p>This photo was taken by <a about="photo1.jpg" rel="dc:creator" rev="foaf:img" href="http://www.blogger.com/profile/1109404">Mark Birbeck</a>.</p>');
	testTriples($('#main > p > a').rdfa(), 
	            [$.rdf.triple('<photo1.jpg> dc:creator <http://www.blogger.com/profile/1109404>', ns), 
	             $.rdf.triple('<http://www.blogger.com/profile/1109404> foaf:img <photo1.jpg>', ns)]);
	$('#main > p').remove();
});

test("Test 0007", function() {
	setup('<p>This photo was taken by <a about="photo1.jpg" property="dc:title" content="Portrait of Mark" rel="dc:creator" rev="foaf:img" href="http://www.blogger.com/profile/1109404">Mark Birbeck</a>.</p>');
	testTriples($('#main > p > a').rdfa(), 
	            [$.rdf.triple('<photo1.jpg> dc:title "Portrait of Mark" .', ns),
	             $.rdf.triple('<photo1.jpg> dc:creator <http://www.blogger.com/profile/1109404> .', ns),
	             $.rdf.triple('<http://www.blogger.com/profile/1109404> foaf:img <photo1.jpg> .', ns)]);
	$('#main > p').remove();
});

test("Test 0008", function() {
	setup('<p>This document is licensed under a <a about="" rel="cc:license" href="http://creativecommons.org/licenses/by-nc-nd/2.5/">Creative Commons</a>.</p>');
	testTriples($('#main > p > a').rdfa(), 
	            [$.rdf.triple('<> cc:license <http://creativecommons.org/licenses/by-nc-nd/2.5/> .', ns)]);
	$('#main > p').remove();
});

test("Test 0009", function() {
	$('head').append('<link about="http://example.org/people#Person1" rev="foaf:knows" href="http://example.org/people#Person2" />');
	testTriples($('link[about]').rdfa(), 
	            [$.rdf.triple('<http://example.org/people#Person2> foaf:knows <http://example.org/people#Person1> .', ns)]);
	$('link[about]').remove();
});

test("Test 0010", function() {
	$('head').append('<link about="http://example.org/people#Person1" rel="foaf:knows" rev="foaf:knows" href="http://example.org/people#Person2" />');
	testTriples($('link[about]').rdfa(), 
  	[$.rdf.triple('<http://example.org/people#Person1> foaf:knows <http://example.org/people#Person2> .', ns),
  	 $.rdf.triple('<http://example.org/people#Person2> foaf:knows <http://example.org/people#Person1> .', ns)]);
	$('link[about]').remove();
});

test("Test 0011", function() {
	setup('<div about="">Author: <span property="dc:creator">Albert Einstein</span><h2 property="dc:title">E = mc<sup>2</sup>: The Most Urgent Problem of Our Time</h2></div>');
	testTriples($('#main > div > span').rdfa(),
		[$.rdf.triple('<> dc:creator "Albert Einstein" .', ns)]);
	testTriples($('#main > div > h2').rdfa(), 
		[$.rdf.triple('<> dc:title "E = mc<sup xmlns=\\"http://www.w3.org/1999/xhtml\\">2</sup>: The Most Urgent Problem of Our Time"^^rdf:XMLLiteral .', ns)]);
	testTriples($('#main > div').rdfa(), 
		[$.rdf.triple('<> dc:creator "Albert Einstein" .', ns),
		 $.rdf.triple('<> dc:title "E = mc<sup xmlns=\\"http://www.w3.org/1999/xhtml\\">2</sup>: The Most Urgent Problem of Our Time"^^rdf:XMLLiteral .', ns)]);
	$('#main > div').remove();
});

test("Test 0012", function() {
	$('head').append('<meta about="http://example.org/node" property="ex:property" xml:lang="fr" content="chat" />');
	testTriples($('meta').rdfa(), 
		[$.rdf.triple('<http://example.org/node> <http://example.org/property> "chat"@fr .')]);
	$('meta').remove();
})

/* This test has been amended because replacing the head is difficult in the QUnit test runner. The logic is the same. */
test("Test 0013", function() {
	setup('<div about="" xml:lang="fr"><h1 xml:lang="en">Test 0013</h1><span about="http://example.org/node" property="ex:property" content="chat"></span></div>');
	testTriples($('#main > div > span').rdfa(), 
		[$.rdf.triple('<http://example.org/node> <http://example.org/property> "chat"@fr .')]);
	$('#main > div').remove();
});

test("Test 0014", function() {
	setup('<p><span	about="http://example.org/foo" property="ex:bar" content="10" datatype="xsd:integer">ten</span></p>');
	testTriples($('#main > p').rdfa(), 
		[$.rdf.triple('<http://example.org/foo> <http://example.org/bar> 10 .')]);
	testTriples($('#main > p > span').rdfa(), 
		[$.rdf.triple('<http://example.org/foo> <http://example.org/bar> 10 .')]);
	$('#main > p').remove();
});

test("Test 0015", function() {
	$('head').append('<link rel="dc:source" href="urn:isbn:0140449132" /><meta property="dc:creator" content="Fyodor Dostoevsky" />');
	testTriples($('link[rel=dc:source]').rdfa(), 
		[$.rdf.triple('<> dc:source <urn:isbn:0140449132> .', ns)]);
	testTriples($('meta').rdfa(), 
		[$.rdf.triple('<> dc:creator "Fyodor Dostoevsky"  .', ns)]);
	$('link[rel=dc:source]').remove();
	$('meta').remove();
})

test("Test 0017", function() {
	setup('<p><span about="[_:a]" property="foaf:name">Manu Sporny</span> <span about="[_:a]" rel="foaf:knows" resource="[_:b]">knows</span> <span about="[_:b]" property="foaf:name">Ralph Swick</span>.</p>');
	testTriples($('#main > p > span').eq(0).rdfa(),
		[$.rdf.triple('_:a foaf:name "Manu Sporny" .', ns)]);
	testTriples($('#main > p > span').eq(1).rdfa(),
		[$.rdf.triple('_:a foaf:knows _:b .', ns)]);
	testTriples($('#main > p > span').eq(2).rdfa(),
		[$.rdf.triple('_:b foaf:name "Ralph Swick" .', ns)]);
	testTriples($('#main > p').rdf(),
		[$.rdf.triple('_:a foaf:name "Manu Sporny" .', ns),
		 $.rdf.triple('_:a foaf:knows _:b .', ns),
	   $.rdf.triple('_:b foaf:name "Ralph Swick" .', ns)]);
	$('#main > p').remove();
});

test("Test 0018", function() {
	setup('<p>This photo was taken by <a about="photo1.jpg" rel="dc:creator" href="http://www.blogger.com/profile/1109404">Mark Birbeck</a>.</p>');
	testTriples($('#main > p > a').rdfa(),
		[$.rdf.triple('<photo1.jpg> dc:creator <http://www.blogger.com/profile/1109404> .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<photo1.jpg> dc:creator <http://www.blogger.com/profile/1109404> .', ns)]);
	$('#main > p').remove();
});

test("Test 0019", function() {
	setup('<div about="mailto:manu.sporny@digitalbazaar.com" rel="foaf:knows" href="mailto:michael.hausenblas@joanneum.at"></div>');
	testTriples($('#main > div').rdfa(),
		[$.rdf.triple('<mailto:manu.sporny@digitalbazaar.com> foaf:knows <mailto:michael.hausenblas@joanneum.at> .', ns)]);
	$('#main > div').remove();
});

test("Test 0020", function() {
	setup('<div about="photo1.jpg"><span class="attribution-line">this photo was taken by <span property="dc:creator">Mark Birbeck</span></span></div>');
	testTriples($('#main > div > span > span').rdfa(),
		[$.rdf.triple('<photo1.jpg> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > div > span').rdfa(),
		[$.rdf.triple('<photo1.jpg> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > div').rdfa(),
		[$.rdf.triple('<photo1.jpg> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > div').remove();
});

test("Test 0021", function() {
	setup('<div><span class="attribution-line">this photo was taken by <span property="dc:creator">Mark Birbeck</span></span></div>');
	testTriples($('#main > div > span > span').rdfa(),
		[$.rdf.triple('<> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > div > span').rdfa(),
		[$.rdf.triple('<> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > div').rdfa(),
		[$.rdf.triple('<> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > div').remove();
});

test("Test 0023", function() {
	setup('<div id="photo1">This photo was taken by <span property="dc:creator">Mark Birbeck</span></div>');
	testTriples($('#main > div > span').rdfa(),
		[$.rdf.triple('<> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > div').rdfa(),
		[$.rdf.triple('<> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > div').remove();
});

test("Test 0025", function() {
	setup('<p>This paper was written by <span rel="dc:creator" resource="#me"><span property="foaf:name">Ben Adida</span>.</span></p>');
	testTriples($('#main > p > span > span').rdfa(),
		[$.rdf.triple('<#me> foaf:name "Ben Adida" .', ns)]);
	testTriples($('#main > p > span').rdfa(),
		[$.rdf.triple('<> dc:creator <#me> .', ns),
		 $.rdf.triple('<#me> foaf:name "Ben Adida" .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<> dc:creator <#me> .', ns),
		 $.rdf.triple('<#me> foaf:name "Ben Adida" .', ns)]);
	$('#main > p').remove();
});

test("Test 0026", function() {
	setup('<p><span about="http://internet-apps.blogspot.com/" property="dc:creator" content="Mark Birbeck" /></p>');
	testTriples($('#main > p > span').rdfa(),
		[$.rdf.triple('<http://internet-apps.blogspot.com/> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<http://internet-apps.blogspot.com/> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > p').remove();
});

test("Test 0027", function() {
	setup('<p><span about="http://internet-apps.blogspot.com/" property="dc:creator" content="Mark Birbeck">Mark B.</span></p>');
	testTriples($('#main > p > span').rdfa(),
		[$.rdf.triple('<http://internet-apps.blogspot.com/> dc:creator "Mark Birbeck" .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<http://internet-apps.blogspot.com/> dc:creator "Mark Birbeck" .', ns)]);
	$('#main > p').remove();
});

test("Test 0029", function() {
	setup('<p><span about="http://example.org/foo" property="dc:creator" datatype="xsd:string"><b>M</b>ark <b>B</b>irbeck</span>.</p>');
	testTriples($('#main > p > span > b').rdfa(), []);
	testTriples($('#main > p > span').rdfa(),
		[$.rdf.triple('<http://example.org/foo> dc:creator "Mark Birbeck"^^xsd:string .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<http://example.org/foo> dc:creator "Mark Birbeck"^^xsd:string .', ns)]);
	$('#main > p').remove();
});

test("Test 0030", function() {
	setup('<p>This document is licensed under a <a rel="cc:license" href="http://creativecommons.org/licenses/by-nc-nd/2.5/">Creative Commons License</a>.</p>');
	testTriples($('#main > p > a').rdfa(),
		[$.rdf.triple('<> cc:license <http://creativecommons.org/licenses/by-nc-nd/2.5/> .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<> cc:license <http://creativecommons.org/licenses/by-nc-nd/2.5/> .', ns)]);
	$('#main > p').remove();
});

test("Test 0031", function() {
	setup('<p about="#wtw">The book <b>Weaving the Web</b> (hardcover) has the ISBN <span rel="dc:identifier" resource="urn:ISBN:0752820907">0752820907</span>.</p>');
	testTriples($('#main > p > b').rdfa(), []);
	testTriples($('#main > p > span').rdfa(),
		[$.rdf.triple('<#wtw> dc:identifier <urn:ISBN:0752820907> .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<#wtw> dc:identifier <urn:ISBN:0752820907> .', ns)]);
	$('#main > p').remove();
});

test("Test 0032", function() {
	setup('<p about="#wtw">The book <b>Weaving the Web</b> (hardcover) has the ISBN <a rel="dc:identifier" resource="urn:ISBN:0752820907" href="http://www.amazon.com/Weaving-Web-Tim-Berners-Lee/dp/0752820907">0752820907</a>.</p>');
	testTriples($('#main > p > b').rdfa(), []);
	testTriples($('#main > p > a').rdfa(),
		[$.rdf.triple('<#wtw> dc:identifier <urn:ISBN:0752820907> .', ns)]);
	testTriples($('#main > p').rdfa(),
		[$.rdf.triple('<#wtw> dc:identifier <urn:ISBN:0752820907> .', ns)]);
	$('#main > p').remove();
});

test("Test 0033", function() {
	var rdf, triple;
	setup('<p>This paper was written by <span rel="dc:creator"><span property="foaf:name">Ben Adida</span>.</span></p>');
	rdf = $('#main > p > span > span').rdfa();
	triple = rdf.tripleStore[0];
	ok(triple.subject.blank, "the subject of the foaf:name triple should be blank");
	equals(triple.property, $.rdf.resource('foaf:name', ns));
	equals(triple.object, $.rdf.literal('"Ben Adida"'));
	
	rdf = $('#main > p > span').rdfa();
	equals(rdf.tripleStore.length, 2, 'the span should return two triples');
	triple = rdf.tripleStore[0];
	if (triple !== undefined) {
		equals(triple.subject, $.rdf.resource('<>'));
		equals(triple.property, $.rdf.resource('dc:creator', ns));
		ok(triple.object.blank, "the object of the dc:creator triple should be blank");
	}
	triple = rdf.tripleStore[1];
	if (triple !== undefined) {
		ok(triple.subject.blank, "the subject of the foaf:name triple should be blank");
		equals(triple.property, $.rdf.resource('foaf:name', ns));
		equals(triple.object, $.rdf.literal('"Ben Adida"'));
		ok(rdf.tripleStore[0].object === rdf.tripleStore[1].subject, "the object of the first triple should be the same as the subject of the second triple");
	}
	
	$('#main > p').remove();
});

test("Test 0034", function() {
	setup('<div about="http://sw-app.org/mic.xhtml#i" rel="foaf:img"><img src="http://sw-app.org/img/mic_2007_01.jpg" alt="A photo depicting Michael" /></div>');
	testTriples($('#main > div > img').rdfa(), []);
	testTriples($('#main > div').rdfa(),
		[$.rdf.triple('<http://sw-app.org/mic.xhtml#i> foaf:img <http://sw-app.org/img/mic_2007_01.jpg> .', ns)]);
	$('#main > div').remove();
});

module("Adding RDFa to elements");

test("adding RDFa to an element whose text matches the literal value of the RDFa", function() {
  setup('<p>This document is by <span>Jeni Tennison</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> dc:creator "Jeni Tennison" .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'dc:creator');
  equals(span.attr('content'), undefined);
  equals(span.attr('datatype'), '');
  $('#main > p').remove();
});

test("adding RDFa to an element whose text does not match the literal value of the RDFa", function() {
  setup('<p>This document is by <span>me</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> dc:creator "Jeni Tennison" .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'dc:creator');
  equals(span.attr('content'), 'Jeni Tennison');
  equals(span.attr('datatype'), '');
  $('#main > p').remove();
});

test("adding RDFa to an element whose value has a datatype", function() {
  setup('<p>Last updated <span>today</span></p>');
  var span = $('#main > p > span');
  span.rdfa('<> dc:date "2008-10-19"^^xsd:date .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'dc:date');
  equals(span.attr('content'), '2008-10-19');
  equals(span.attr('datatype'), 'xsd:date');
  $('#main > p').remove();
});

test("adding RDFa without the namespaces already having been declared", function() {
  setup('<p>This document is by <span>Jeni Tennison</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> <http://www.example.org/ns/my#author> "Jeni Tennison" .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'ns1:author');
  equals(span.attr('content'), undefined);
  equals(span.attr('datatype'), '');
  equals(span.attr('xmlns:ns1'), 'http://www.example.org/ns/my#');
  $('#main > p').remove();
});

test("adding RDFa to an element where the literal value has a language", function() {
  setup('<p>This document is about my <span>chat</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> ex:topic "chat"@fr .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'ex:topic');
  equals(span.attr('content'), undefined);
  equals(span.attr('datatype'), undefined);
  equals(span.attr('lang'), 'fr');
  $('#main > p').remove();
});

test("adding RDFa to an element where the literal value has a language different from the context and its contents", function() {
  setup('<p lang="en">This document is about my <span>cat</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> ex:topic "chat"@fr .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'ex:topic');
  equals(span.attr('content'), 'chat');
  equals(span.attr('datatype'), undefined);
  equals(span.attr('lang'), 'fr');
  equals(span.children('span').length, 1, "it should have a nested span added");
  equals(span.children('span').attr('lang'), 'en', "with the context language added");
  $('#main > p').remove();
});

test("adding RDFa where the object is an XMLLiteral", function() {
  setup('<p>This document is by <span>Jeni Tennison</span>.</p>');
  var span = $('#main > p > span');
  span.rdfa('<> dc:creator "Jeni <em>Tennison</em>"^^<http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral> .');
  equals(span.attr('about'), undefined);
  equals(span.attr('property'), 'dc:creator');
  equals(span.attr('content'), undefined);
  equals(span.attr('datatype'), 'rdf:XMLLiteral');
  equals(span.children('em').length, 1, "it should have a nested em added");
  equals(span.children('em').text(), "Tennison");
  $('#main > p').remove();
});

})(jQuery);