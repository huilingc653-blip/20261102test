var CreatedOKLodop7766=null;
var CreatedOKLodopObject, CLodopIsLocal, CLodopJsState;
//====判断是否需要安装CLodop云打印服务器:====
function needCLodop(){
    try{
	var ua=navigator.userAgent;
	if (ua.match(/Windows\sPhone/i) !=null) return true;
	if (ua.match(/iPhone|iPod/i) != null) return true;
	if (ua.match(/Android/i) != null) return true;
	if (ua.match(/Edge\D?\d+/i) != null) return true;
	
	var verTrident=ua.match(/Trident\D?\d+/i);
	var verIE=ua.match(/MSIE\D?\d+/i);
	var verOPR=ua.match(/OPR\D?\d+/i);
	var verFF=ua.match(/Firefox\D?\d+/i);
	var x64=ua.match(/x64/i);
	if ((verTrident==null)&&(verIE==null)&&(x64!==null)) 
		return true; else
	if ( verFF !== null) {
		verFF = verFF[0].match(/\d+/);
		if ((verFF[0]>= 42)||(x64!==null)) return true;
	} else 
	if ( verOPR !== null) {
		verOPR = verOPR[0].match(/\d+/);
		if ( verOPR[0] >= 32 ) return true;
	} else 
	if ((verTrident==null)&&(verIE==null)) {
		var verChrome=ua.match(/Chrome\D?\d+/i);		
		if ( verChrome !== null ) {
			verChrome = verChrome[0].match(/\d+/);
			if (verChrome[0]>=42) return true;
		};
	};
        return false;
    } catch(err) {return true;};
};
//====页面引用CLodop云打印必须的JS文件：====
//====页面引用CLodop云打印必须的JS文件：====
function loadCLodop() {
    if (CLodopJsState == "loading" || CLodopJsState == "complete") return;
    CLodopJsState = "loading";
    var isHttps = 'https:' === document.location.protocol;
    var head = document.head || document.getElementsByTagName("head")[0] || document.documentElement;
    var oscript1 = document.createElement("script");
    if (isHttps) {
        oscript1.src = "https://localhost.lodop.net:8443/CLodopfuncs.js";
    } else {
        oscript1.src = "http://127.0.0.1:8000/CLodopfuncs.js?priority=1";
    }
    oscript1.onload  = oscript1.onload  = function(){CLodopJsState = "complete";}
    oscript1.onerror = oscript1.onerror = function(evt) {CLodopJsState = "complete";}
    head.insertBefore( oscript1,head.firstChild );

    //引用双端口(8000和18000）避免其中某个被占用：
    var oscript2 = document.createElement("script");
    if (isHttps) {
        oscript2.src = "https://localhost.lodop.net:8444/CLodopfuncs.js";
    } else {
        oscript2.src = "http://127.0.0.1:18000/CLodopfuncs.js?priority=0";
    }
    head.insertBefore( oscript2,head.firstChild );
    CLodopIsLocal = !!((oscript1.src + oscript2.src).match(/\/\/localho|\/\/127.0.0./i));
}
if (needCLodop()){loadCLodop();}//加载
//==获取LODOP对象主过程,判断是否安装、需否升级:==
function getLodop(oOBJECT, oEMBED) {
    // var strHtmInstall = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    // var strHtmUpdate = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    // var strHtm64_Install = "<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
    // var strHtm64_Update = "<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
    // var strHtmFireFox = "<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    // var strHtmChrome = "<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";
    // var strCLodopInstall_1 = "<br><font color='#FF00FF'>Web打印服务CLodop未安装启动，点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>下载执行安装</a>";
    // var strCLodopInstall_2 = "<br>（若此前已安装过，可<a href='CLodop.protocol:setup' target='_self'>点这里直接再次启动</a>）";
    // var strCLodopInstall_3 = "，成功后请刷新本页面。</font>";
    //var strCLodopUpdate = "<br><font color='#FF00FF'>Web打印服务CLodop需升级!点击这里<a href='CLodop_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";

    var strHtmInstall="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNotInstalled')+"!"+bt.lang('common.print.clickHere')+"<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L01' target='_self'>"+bt.lang('common.print.doInstalling')+"</a>,"+bt.lang('common.print.refreshAfterInstalled')+"。</font>"; ///*打印控件未安装*/!点击这里 执行安装 安装后请刷新页面或重新进入
    var strHtmUpdate ="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.widgetNeedUpdate')+"&nbsp;&nbsp;<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L01' target='_self' style='color: red' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doUpdating')+"</a> &nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"

    var strHtm64_Install="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNotInstalled')+"!"+bt.lang('common.print.clickHere')+"<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L02' target='_self'>"+bt.lang('common.print.doInstalling')+"</a>,"+bt.lang('common.print.refreshAfterInstalled')+"。</font>";
    var strHtm64_Update="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNeedUpdate')+","+bt.lang('common.print.clickHere')+"<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L02' target='_self'>"+bt.lang('common.print.doUpdating')+"</a>,"+bt.lang('common.print.loginAfterUpdated')+"。</font>";
    var strHtmFireFox="<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    var strHtmChrome="<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";

    var strCLodopUpdate="<br><font color='#FF00FF'>"+bt.lang('bt.lang(common.print.widgetNeedUpdate)')+"!"+bt.lang('common.print.clickHere')+"&nbsp;&nbsp;<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L00' target='_self' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doUpdating')+"</a>,&nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</font>";//升级后请刷新页面。
    var strCLodopInstall ="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.clickHere')+"&nbsp;&nbsp;<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L00' target='_self' style='color: red' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doInstalling')+"</a> &nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"

    var strCLodopInstall_1 = "<br><font color='#FF00FF'>Web打印服务CLodop未安装启动，点击这里<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L00' target='_self'>下载执行安装</a>";
    var strCLodopInstall_2 = "<br>（若此前已安装过，启动打印程序</a>）";
    var strCLodopInstall_3 = "，成功后请刷新本页面。</font>";

    var LODOP;
    try {
        var ua = navigator.userAgent;
        var isIE = !!(ua.match(/MSIE/i)) || !!(ua.match(/Trident/i));
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch (err) {}
            if (!LODOP && CLodopJsState !== "complete") {
                if (CLodopJsState == "loading") alert("网页还没下载完毕，请稍等一下再操作."); else alert("没有加载CLodop的主js，请先调用loadCLodop过程.");
                return;
            }
            if (!LODOP) {
                document.body.innerHTML = strCLodopInstall_1 + (CLodopIsLocal ? strCLodopInstall_2 : "") + strCLodopInstall_3 + document.body.innerHTML;
                return;
            } else {
                if (CLODOP.CVERSION < "4.1.0.1") {
                    document.body.innerHTML = strCLodopUpdate + document.body.innerHTML;
                }
                if (oEMBED && oEMBED.parentNode)
                    oEMBED.parentNode.removeChild(oEMBED); //清理旧版无效元素
                if (oOBJECT && oOBJECT.parentNode)
                    oOBJECT.parentNode.removeChild(oOBJECT);
            }
        } else {
            var is64IE = isIE && !!(ua.match(/x64/i));
            //==如果页面有Lodop就直接使用,否则新建:==
            if (oOBJECT || oEMBED) {
                if (isIE)
                    LODOP = oOBJECT;
                else
                    LODOP = oEMBED;
            } else if (!CreatedOKLodopObject) {
                LODOP = document.createElement("object");
                LODOP.setAttribute("width", 0);
                LODOP.setAttribute("height", 0);
                LODOP.setAttribute("style", "position:absolute;left:0px;top:-100px;width:0px;height:0px;");
                if (isIE)
                    LODOP.setAttribute("classid", "clsid:2105C259-1E0C-4534-8141-A753534CB4CA");
                else
                    LODOP.setAttribute("type", "application/x-print-lodop");
                document.documentElement.appendChild(LODOP);
                CreatedOKLodopObject = LODOP;

            } else
                LODOP = CreatedOKLodopObject;

            //==Lodop插件未安装时提示下载地址:==
            if ((!LODOP) || (!LODOP.VERSION)) {
                if (ua.indexOf('Chrome') >= 0)
                    document.body.innerHTML = strHtmChrome + document.body.innerHTML;
                if (ua.indexOf('Firefox') >= 0)
                    document.body.innerHTML = strHtmFireFox + document.body.innerHTML;
                document.body.innerHTML = (is64IE ? strHtm64_Install : strHtmInstall) + document.body.innerHTML;
                return LODOP;
            }
        }
        if (LODOP.VERSION < "6.2.2.6") {
            if (!needCLodop())
                document.body.innerHTML = (is64IE ? strHtm64_Update : strHtmUpdate) + document.body.innerHTML;
        }
        //===如下空白位置适合调用统一功能(如注册语句、语言选择等):==


        //=======================================================
        return LODOP;
    } catch (err) {
        alert("getLodop出错:" + err);
    }
}

