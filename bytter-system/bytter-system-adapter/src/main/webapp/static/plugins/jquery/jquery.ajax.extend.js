(function ($) {
    //首先备份下jquery的ajax方法
    var _ajax = $.ajax;
    //重写jquery的ajax方法
    $.ajax = function (opt) {
        //备份opt中error和success方法
        var fn = {error: function (XMLHttpRequest, textStatus, errorThrown) {}, success: function (data, textStatus) {},complete: function (XHR, TS){}, beforeSend: function (XHR) {}};
        if (opt.error) {fn.error = opt.error;}
        if (opt.success) {fn.success = opt.success;}
        if (opt.beforeSend) {fn.beforeSend = opt.beforeSend;}
        if (opt.complete) {fn.complete = opt.complete;}
        if( opt.type && opt.type.toLowerCase() == 'get'){
            if(!opt.data){
                opt.data = {};
            }
            opt.data['_btv'] = new Date().getTime();
        }

        // var token = store.get('Access-Token') !='' ? store.get('Access-Token') : '';
        var token = store.get('Access-Token') || "";
        var tokenUTF8 = eval('\''+encodeURI(token).replace(/%/gm,'\\x')+'\'');
        opt.headers = {
            "Authorization":token
        }
        //扩展增强处理
        var _opt = $.extend(opt, {
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if(XMLHttpRequest.status === 406){
                    var responseJSON = XMLHttpRequest.responseJSON;
                    var msg = "用户未登录/登录状态已超时，请重新登录";
                    var targetUrl = basePath + "/login";
                    if (responseJSON) {
                        if (responseJSON.msg) {
                            msg = responseJSON.msg;
                        }
                        if (responseJSON.targetUrl) {
                            targetUrl = responseJSON.targetUrl;
                        }
                    }
                    parent.layer.alert(msg, {icon: 2}, function () {
                        //清除缓存的内容
                        document.cookie = "Access-Token="+store.set('Access-Token')+"; expires=" + new Date(0).toUTCString()
                        store.clearAll();
                        window.top.location.href = targetUrl;
                    });
                    return;
                } else if(XMLHttpRequest.status === 412){
                    var responseJSON = XMLHttpRequest.responseJSON;
                    var msg = "锁屏功能异常，请关闭页面或者退出重新登陆";
                    parent.layer.alert(msg,{icon:2});
                    return;
                } else if(XMLHttpRequest.status === 403){
                    var responseJSON = XMLHttpRequest.responseJSON;
                    var msg = responseJSON?.msg ?? "当前用户尚未分配该菜单资源权限，请联系管理员授权!";
                    // parent.layer.alert(msg,{icon:2});
                    parent.layer.confirm(msg,{icon:2,btn:['确定'],closeBtn:0},function(){
                        window.location.href = basePath + "/403";
                    });
                    return;
                }else {
                   var responseText = XMLHttpRequest.responseText;
                   if(responseText && responseText.indexOf("password") !== -1){
                       var msg = "用户未登录/登录状态已超时，请重新登录";
                       var targetUrl = basePath + "/login";
                       parent.layer.alert(msg, {icon: 2}, function () {
                           //清除缓存的内容
                           document.cookie = "Access-Token="+store.set('Access-Token')+"; expires=" + new Date(0).toUTCString()
                           store.clearAll();
                           window.top.location.href = targetUrl;
                       });
                       return;
                   }
                }
                //错误方法增强处理
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success: function (data, textStatus) {
            	   if (typeof (data) != "undefined" && data != null && data != "" && data.code=='CM4001' && data.msg!="" && data.msg!=null) {
                	 	parent.layer.alert(data.msg, {icon: 2}, function () {
                            //清除缓存的内容
                            document.cookie = "Access-Token="+store.set('Access-Token')+"; expires=" + new Date(0).toUTCString()
                            store.clearAll();
                	    var targetUrl = basePath + "/login";
                        window.top.location.href = targetUrl;
                    });
                }
//                        if(!data.success){
//                            parent.layer.alert(data.msg,{icon:2});
//                        }
                //成功回调方法增强处理
                fn.success(data, textStatus);
            },
            beforeSend: function (XHR) {
                fn.beforeSend(XHR);
            },
            complete: function (XHR, TS) {
                fn.complete(XHR,TS);
            }
        });
        return _ajax(_opt);
    };
})(jQuery);
