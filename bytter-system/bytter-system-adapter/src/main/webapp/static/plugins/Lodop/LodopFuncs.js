var CreatedOKLodop7766=null;
var CreatedOKLodopObject, CLodopIsLocal, CLodopJsState;
var layerObj = parent.layer?parent.layer:{};

function needCLodop() {
    try {
        var ua = navigator.userAgent;
        if (ua.match(/Windows\sPhone/i))
            return true;
        if (ua.match(/iPhone|iPod|iPad/i))
            return true;
        if (ua.match(/Android/i))
            return true;
        if (ua.match(/Edge\D?\d+/i))
            return true;

        var verTrident = ua.match(/Trident\D?\d+/i);
        var verIE = ua.match(/MSIE\D?\d+/i);
        var verOPR = ua.match(/OPR\D?\d+/i);
        var verFF = ua.match(/Firefox\D?\d+/i);
        var x64 = ua.match(/x64/i);
        if ((!verTrident) && (!verIE) && (x64))
            return true;
        else if (verFF) {
            verFF = verFF[0].match(/\d+/);
            if ((verFF[0] >= 41) || (x64))
                return true;
        } else if (verOPR) {
            verOPR = verOPR[0].match(/\d+/);
            if (verOPR[0] >= 32)
                return true;
        } else if ((!verTrident) && (!verIE)) {
            var verChrome = ua.match(/Chrome\D?\d+/i);
            if (verChrome) {
                verChrome = verChrome[0].match(/\d+/);
                if (verChrome[0] >= 41)
                    return true;
            }
        }
        return false;
    } catch (err) {
        return true;
    }
}
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
//====获取LODOP对象的主过程：====
function getLodop(oOBJECT,oEMBED,isErp){

    var downloadResource00=basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L00";
    var downloadResource01=basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L01";
    var downloadResource02=basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L02";
    // var downloadResource03=basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L03";
    if(isErp){
        downloadResource00=basePath+"/erp/bankReceipt/download/resource?code=402880455c5dcd90015c5dcdda790L00";
        downloadResource01=basePath+"/erp/bankReceipt/download/resource?code=402880455c5dcd90015c5dcdda790L01";
        downloadResource02=basePath+"/erp/bankReceipt/download/resource?code=402880455c5dcd90015c5dcdda790L02";
    }
    var strHtmInstall="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.widgetNotInstalled')+"!"+bt.lang('common.print.clickHere')+"<a href='"+downloadResource01+"' target='_self' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doInstalling')+"</a>,"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"; ///*打印控件未安装*/!点击这里 执行安装 安装后请刷新页面或重新进入
    // var strHtmUpdate ="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.widgetNeedUpdate')+"&nbsp;&nbsp;<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L01' target='_self' style='color: red' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doUpdating')+"</a> &nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"
    var strHtmUpdate ="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.widgetNeedUpdate')+"&nbsp;&nbsp;<a href='"+downloadResource01+"' target='_self' style='color: red' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doUpdating')+"</a> &nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"
   // var strHtmUpdate="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNeedUpdate')+","+bt.lang('common.print.clickHere')+"<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L01' target='_self'>"+bt.lang('common.print.doUpdating')+"</a>,"+bt.lang('common.print.loginAfterUpdated')+"。</font>";//打印控件需要升级!点击这里 执行升级 升级后请重新进入

    var strHtm64_Install="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNotInstalled')+"!"+bt.lang('common.print.clickHere')+"<a href='"+downloadResource02+"' target='_self'>"+bt.lang('common.print.doInstalling')+"</a>,"+bt.lang('common.print.refreshAfterInstalled')+"。</font>";
    var strHtm64_Update="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNeedUpdate')+","+bt.lang('common.print.clickHere')+"<a href='"+downloadResource02+"' target='_self'>"+bt.lang('common.print.doUpdating')+"</a>,"+bt.lang('common.print.loginAfterUpdated')+"。</font>";
    var strHtmFireFox="<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
    var strHtmChrome="<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";

    //  var strCLodopInstall="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNotInstalled')+"!<br>"+bt.lang('common.print.clickHere')+"<a href='"+basePath+"/download/resource?code=402880455c5dcd90015c5dcdda790L00' target='_self'>"+bt.lang('common.print.doInstalling')+"</a>,<br>"+bt.lang('common.print.refreshAfterInstalled')+"。</font>";
    var strCLodopUpdate="<br><font color='#FF00FF'>"+bt.lang('common.print.widgetNeedUpdate')+"!"+bt.lang('common.print.clickHere')+"&nbsp;&nbsp;<a href='"+downloadResource00+"' target='_self' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doUpdating')+"</a>,&nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</font>";//升级后请刷新页面。
    var strCLodopInstall ="<div style='font-size: 14px;line-height: 24px;padding: 20px;'>"+bt.lang('common.print.clickHere')+"&nbsp;&nbsp;<a href='"+downloadResource00+"' target='_self' style='color: red' class='layui-btn layui-bg-blue'>"+bt.lang('common.print.doInstalling')+"</a> &nbsp;&nbsp;"+bt.lang('common.print.refreshAfterInstalled')+"。</div>"
    var LODOP;
    var isHttps = 'https:' === document.location.protocol;
    try{
        var ua = navigator.userAgent;
        var isIE = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
        //layerObj.msg(bt.lang('common.msg.printing'),{time: 800}); //正在打印中
        if (needCLodop()) {
            try {
                LODOP = getCLodop();
            } catch (err) {
                // loadCLodop();
            };
            if (!LODOP && CLodopJsState !== "complete") {
                return null;
            }
            if (!LODOP) {
                layerObj.confirm(bt.lang('common.print.ifWidgetInstalled')+'？', { //是否已经安装了打印程序
                    btn:[bt.lang('abBillPledgeApply.interiorApply.yes'),bt.lang('abBillPledgeApply.interiorApply.no')]//['是','否'] //按钮
                }, function(){
                    layerObj.alert(bt.lang('common.print.searchLodopAndStart'), { //'请在开始栏搜索"lodop"程序，找到该程序双击启动服务'
                        skin: 'layui-layer-molv' //样式类名
                        ,closeBtn: 0
                    });
                }, function(){
                    layerObj.open({
                        type: 1,
                        title:bt.lang('common.msg.installPrintSoft'),//'打印程序未安装',
                        area: ['280px', '158px'], //宽高
                        content: strCLodopInstall
                    });
                });
                return;
            } else {
                if (CLODOP.CVERSION < "4.1.0.1") {
                    // document.body.innerHTML = strCLodopUpdate + document.body.innerHTML;
                    layer.open({
                        type: 1,
                        title:bt.lang('common.print.widgetNeedUpdate'),////'打印服务需升级',
                        content: strCLodopUpdate
                    });
                    return
                }
                if (oEMBED && oEMBED.parentNode)
                    oEMBED.parentNode.removeChild(oEMBED); //清理旧版无效元素
                if (oOBJECT && oOBJECT.parentNode)
                    oOBJECT.parentNode.removeChild(oOBJECT);
            }
        }else{
            var is64IE = isIE && !!(ua.match(/x64/i));
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
                var str = "";
                if (ua.indexOf('Chrome') >= 0){
                    //document.body.innerHTML = strHtmChrome + document.body.innerHTML;
                    str +=strHtmChrome;
                }

                if (ua.indexOf('Firefox') >= 0){
                    //document.body.innerHTML = strHtmFireFox + document.body.innerHTML;
                    str +=strHtmFireFox;
                }
                str += (is64IE ? strHtm64_Install : strHtmInstall);
                // document.body.innerHTML = (is64IE ? strHtm64_Install : strHtmInstall) + document.body.innerHTML;
                layer.open({
                    type: 1,
                    content: str
                });
                return LODOP;
            }
        }
        //https才检查版本
        if (LODOP.VERSION < "6.2.2.6" && isHttps) {
            // document.body.innerHTML = (is64IE ? strHtm64_Update : strHtmUpdate) + document.body.innerHTML;
            if (!needCLodop()){
                layerObj.open({
                    type: 1,
                    title:bt.lang('common.print.widgetNeedUpdate'),//'打印服务需升级',
                    content: (is64IE ? strHtm64_Update : strHtmUpdate)
                });
            }

            return null
        }
        if (LODOP&&LODOP.SET_LICENSES){
            LODOP.SET_LICENSES("深圳市拜特科技股份有限公司", "459547369717172919278901905623", "", "");
        }
        return LODOP;
    } catch(err) {
        layerObj.alert(bt.lang('common.msg.printError')+err) //'打印过程中出错'
    };
};
if (needCLodop()){loadCLodop();}//加载

layui.define(["layer"], function(exports) {
    layerObj = layer;
})


