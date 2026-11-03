function setIframeHeight(iframe) {
	if (iframe) {
		var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
		if (iframeWin.document.body) {
			iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
		}
	}
};

window.onload = function () {
	loadIFrame();
};

function loadIFrame(){
	var iframes = document.getElementsByTagName("iframe");
	for (i=0; i<iframes.length; i++) {
		setIframeHeight(iframes[i]);
	}
}

window.setInterval("loadIFrame()", 200);