importClass(Packages.org.ten60.netkernel.layer1.representation.StringAspect);

var foo = context.sinkAspect('powermagpie:/+@term=tester', StringAspect).getString();

response = context.createResponseFrom(new StringAspect(foo));

response.setMimeType("text/plain");