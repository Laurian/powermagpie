
/*
 * Watson command
 * based on http://watson.kmi.open.ac.uk/blog/2007/08/17/1187361600000.html
 */
CmdUtils.makeBookmarkletCommand({
    name:   "watson",
    url:    "javascript:(function(){%20%20%20%20q=document.getSelection();%20%20%20%20if(!q)%20q=prompt('Keyword%20to%20search%20in%20Watson:');%20%20%20%20if(q)%20location.href='http://watson.kmi.open.ac.uk/WatsonWUI/index.html#'%20+%20escape(q);%20%20})()"
});

/*
 * MOAW command
 * based on http://watson.kmi.open.ac.uk/MOAW/
 */
CmdUtils.makeBookmarkletCommand({
    name:   "moaw",
    url:    "javascript:%28function%28%29%7Bvar%20ns%3D%27tag%3Akmi.open.ac.uk%2C2008%3AWatson%3A%27%3Bif%20%28window%5Bns%5D%29%7Bwindow%5Bns%5D.init%28%29%7Delse%7Bvar%20s%20%3D%20document.createElement%28%27script%27%29%3Bs.charset%20%3D%20%27UTF-8%27%3Bs.type%20%3D%20%27text/javascript%27%3Bs.src%20%3D%20%27http%3A//watson.kmi.open.ac.uk/MOAW/init.js%3F0.8255482404884231%27%3Bdocument.getElementsByTagName%28%27head%27%29%5B0%5D.appendChild%28s%29%3B%7D%7D%28%29%29%3B"
});

/*
 * Pica (PowerMagpie) command
 */
// This command looks up a keyword in sindice such as
// http://api.sindice.com/v2/search?q=berners-lee&qt=term&format=json

CmdUtils.CreateCommand({
    name: "pica",
    takes: {"a term": noun_arb_text},
    homepage: "http://purl.org/net/powermagpie",
    author: { name: "Laurian Gridinoc", email: "laurian@gridinoc.name"},
    license: "Apache? TBD", //TODO
    description: "Displays PowerMagpie results for a term or a set of discovered terms in the input",
    help: "see http://purl.org/net/powermagpie",

  preview: function(previewBlock, term) {
    previewBlock.innerHTML = "looking up " + term.text + " with PowerMagpie \u2026 ";
    displayMessage(term.text);
        previewBlock.innerHTML = "<iframe src='http://localhost:8080/PowerMagpie/UI/index.html' border='0' width='100%' height='100%' scrolling='yes' style='border: 0; margin: 0;' ></iframe>";

  },

  execute: function(term) {
    displayMessage(" no action here, open Watson? ");
    Utils.openUrlInBrowser("http://api.sindice.com/v2/search?q=" + term.text);
  }
});

/*
 *
 * PowerMagpie interface based on the Mouse-Based Ubiquity
 * http://www.azarask.in/blog/post/can-ubiquity-be-used-only-with-the-mouse/
 *
 */

// UTILITY FUNCTIONS
var LAST_SEL_TEXT = "";

function getUbiquity() {
    var mainWindow = window.QueryInterface(Components.interfaces.nsIInterfaceRequestor)
                        .getInterface(Components.interfaces.nsIWebNavigation)
                        .QueryInterface(Components.interfaces.nsIDocShellTreeItem)
                        .rootTreeItem
                        .QueryInterface(Components.interfaces.nsIInterfaceRequestor)
                        .getInterface(Components.interfaces.nsIDOMWindow);
    return mainWindow.gUbiquity;
}

function getCommandByName(name) {
    var ubiq = getUbiquity();
    return ubiq.__cmdManager.__cmdSource.getCommand( name );
}

function createFormUI(cmd, selectedText, elem, mods) {
    doc = CmdUtils.getDocument();

    // Check if the form interface exists:
    if( jQuery("#UbiquityForm", doc).length == 0 )
        jQuery("#UbiquityDiv", doc).prepend("<div id='UbiquityForm'></div>");
    else
        jQuery("#UbiquityForm", doc).empty();


    var button = doc.createElement("input");
    jQuery(button)
        .attr("type", "button")
        .attr("value", cmd.name)
        .css({backgroundColor: "#999", border:"1px solid #AAA", marginLeft:"10px"})
        .click(function(event){
            cmd.preview( {}, {text:input.value}, mods || {}, elem );
        });

    var input = doc.createElement("input");
    jQuery(input)
        .attr("type", "text")
        .attr("value", selectedText)
        .blur(function(){ LAST_SEL_TEXT = this.value; })
        .keydown(function(event){
            if( event.which == 13 ){
                button.click();
            }
        })
        .css({width:"330px", opacity:.6});

    var go = doc.createElement("input");
    jQuery(go)
        .attr("type", "button")
        .attr("value", ">")
        .css({backgroundColor: "#999", border:"1px solid #AAA"})
        .click(function(event){
            cmd.execute( {}, {text:input.value}, mods || {})
        });

    jQuery('#UbiquityForm', doc)
        .append(input, button, go);
}

function putCommandPreviewInElement(commandName, elem, mods) {
    var cmd = getCommandByName( commandName );

    var selText = CmdUtils.getWindow().getSelection().toString();
    selText = selText || LAST_SEL_TEXT;
    LAST_SEL_TEXT = selText;
    cmd.preview( {}, {text:selText}, mods || {}, elem );

    createFormUI(cmd, selText, elem, mods);
}

function executeCommand(commandName, mods) {
    var cmd = getCommandByName( commandName );
    var selText = CmdUtils.getWindow().getSelection().toString();
    selText = selText || LAST_SEL_TEXT;
    LAST_SEL_TEXT = selText;

    cmd.execute( {}, {text:selText}, mods || {} );
}


// Scrolling Related  utitlies
function _openScrollPane(){
    var WIDTH = 500;
    var WIDTHPX = WIDTH + "px";

    var win = CmdUtils.getWindow();
    var doc = CmdUtils.getDocumentInsecure();
    winWidth = jQuery("body");
    var div = doc.createElement("div");
    var hidden = doc.createElement("div");

    var iframe = doc.createElement("iframe");
    jQuery(iframe).css({
        width: 450,
        height: 500,
        border: "none"
    });

    jQuery("body", doc).append(div);
    div.appendChild(iframe);
    div.id = "UbiquityDiv";

    iframe.contentDocument.open();
    iframe.contentDocument.write('');
    iframe.contentDocument.close();

    iframe.id = "UbiquityIframe";

    jQuery(iframe.contentDocument.body).css({
        color:"#dadada"
    });

    iframe.contentDocument.linkColor = "white";
    iframe.contentDocument.vlinkColor = "white";



    jQuery("body", doc).css("overflow", "hidden");

    jQuery(hidden).css({position:"absolute", left: win.innerWidth, top:0, width:1, height:1, display:"hidden"})
    jQuery("body", doc).append(hidden);

    jQuery(div).css({
        position:"fixed", top: 0, left:win.innerWidth,
        width:WIDTH, height: win.innerHeight,
        backgroundColor: "#333", color: "white", fontSize: "16px",
        padding: "20px",
        zIndex: 1000
    }).click(function(event){ event.stopPropagation(); });

    Utils.setTimeout(function(){
        jQuery("html,body", doc).animate({"scrollLeft":"500px"});
        jQuery(div, doc).animate({"left":"-=" + WIDTHPX});
        jQuery(hidden, doc).animate({"left":"+=" + WIDTHPX});

        jQuery("#UbiquityBadge", doc).remove();

        jQuery("body", doc).one("click", function(event){

            event.stopPropagation();
            event.preventDefault();

            if( event.originalTarget.parentNode && event.originalTarget.parentNode.id == "UbiquityDiv" ){
                return;
            }

            jQuery(div, doc).animate({"left":"+=" + WIDTHPX});
            jQuery(hidden, doc).animate({"left":"-=" + WIDTHPX}, function(){
                jQuery(hidden,doc).hide();
            });


            jQuery("html,body", doc).animate({"scrollLeft":"0"}, function(){
                jQuery("body", doc).css("overflow", "visible");
                jQuery("#UbiquityDiv",doc).remove();
            });

        });

  }, 200)

  return {pane: div, preview:iframe.contentDocument};
}


// COMMANDS
CmdUtils.CreateCommand({
    name:"picascroll",
    execute: function(){

        var pointers = _openScrollPane();
        var preview = pointers.preview;
        var pane = pointers.pane;

        putCommandPreviewInElement("pica", preview.body);

        var doc = CmdUtils.getDocumentInsecure();

        var cmds = {
            "pica": {},
            "sindice-term": {},
            "dbpedia-cat": {},
            "headup": {},
            "zemify": {},
            "watson": {}
        }
        jQuery(pane).append("<br/>");

        for( var cmd in cmds ){
            var a = doc.createElement("span");

            jQuery(a)
                .html( cmd+", " )
                // We need to store the command name in the object, otherwise
                // closure scoping will always yield the last one in cmds.
                .attr("name", cmd )
                .css({cursor: "pointer", fontSize: "15px"})
                .click( function(event){
                    var name = jQuery(this).attr("name");
                    putCommandPreviewInElement( name, preview.body, cmds[name] );
                })
                .hover(
                    function(event){
                        var span = doc.createElement("span");
                        var name = jQuery(this).attr("name");
                        jQuery(span).mouseup(function(){
                            executeCommand( name, cmds[name] );
                        }).html("[!]").attr("id","UbiquityGo").css({
                            position:"absolute",
                            marginTop:"-20px",
                            marginLeft: "-30px",
                            opacity: .8,
                            backgroundColor: "#333",
                            padding: "3px",
                            cursor: "pointer"
                        });
                        jQuery(this).append(span);
                    },
                    function(){ jQuery("#UbiquityGo", doc).remove(); }
                );

            jQuery(pane).append(a);
        }
    }
});

CmdUtils.CreateCommand({
    name: "picasetup",
    execute: function(){
        doc = CmdUtils.getDocument();
        win = CmdUtils.getWindow();

        jQuery("body", doc).mousedown(function(event){
            jQuery("#UbiquityBadge", doc).remove();
        });


        jQuery("body", doc).mouseup(function(event){

            var selection = win.getSelection();
            if( !selection.isCollapsed && jQuery("#UbiquityBadge", doc).length == 0 ){
                var badge = doc.createElement("span");
                jQuery(badge)
                    .css({
                        height:31,
                        width:26,
                        backgroundImage: "url(http://azarask.in/verbs/mouse/selection_mark.png)",
                        position:"absolute",
                        "-moz-background-inline-policy":"continuous",
                        cursor:"pointer",
                        "margin-top": "-20px",
                        "margin-left": "-10px",
                        "opacity": .3 })
                    .attr("id", "UbiquityBadge")
                    .mousedown(function(event){
                        cmd_picascroll();
                    });

                var range = selection.getRangeAt(0);
                newRange = doc.createRange();
                newRange.setStart(selection.focusNode, range.endOffset);
                newRange.insertNode(badge);

                jQuery(badge).hover(
                    function(){ jQuery(this).animate({opacity:1},100) },
                    function(){ jQuery(this).animate({opacity:.3},100) }
                );

        /*
        // Experimental method of showing thingy.
        jQuery("body", doc).mousemove(function(event){
          var pos = jQuery(badge).position();
          var dY = pos.top - event.pageY;
          var dX = pos.left - event.pageX;
          var norm = Math.sqrt( dY*dY + dX*dX);

          if( norm > 100 ){ norm = 100; }
          if( norm < 20 ){ norm = 0; }
          var norm = norm/100;

          jQuery(badge).css({opacity:1-norm});
        })
        */

            }
        });
    }
});

function pageLoad_picasetup(){
  cmd_picasetup();
}