/**
 *  查验数据
 *  1 如果top有数据，则把top数据赋值当前window中 top即首页window
 *  2 如果top没有数据，就检查当前window中有没有，如果没有，就去缓存中获取
 *  3 如果缓存store中也没有，就去后台取值
 *
 *  注意：当获取的资源超大，且缓存存贮不下时，会将资源存贮在top中
 *
 * */
function checkData(type, storeName, ajaxData) {
    var winTop = window.top
    if (winTop && winTop.bt[type] &&
        ($.isPlainObject(winTop.bt[type])  // 如果是对象
            || ($.isArray(winTop.bt[type]) && winTop.bt[type].length > 0) // 判断是否时有值的数组
        )) {
        bt[type] = winTop.bt[type]
    } else if (!(bt[type] && ($.isArray(bt[type]) && bt[type].length > 0))) {
        bt[type] = store.get(storeName);
    }
    if (!bt[type]) {
        ajaxData && ajaxData();
    }
}

// 获取国际化资源
if(window.top !== window){
    checkData('bytterMutiLang', 'bytter_lang', getLang)
}

bt.lang = function (code, arr) {
    // code = md5(code);
    var t;
    try {
        t = bt.bytterMutiLang[code];
    } catch (e) {
        getLang();
    }

    t = bt.bytterMutiLang[code];

    if (!t) {
        return code;
    }
    if (arr) {
        for (var i = 0; i < arr.length; i++) {
            t = t.replace('{' + i + '}', arr[i]);
        }
    }
    t=t.replace(/&lt;br&gt;/g,'<br>');
    return t;
};

if (!(bt.excRateConvert && bt.excRateConvert.length > 0)) {
    bt.excRateConvert = store.get('bytter_excRateConvert');
}

if (!(bt.excRateBudget && bt.excRateBudget.length > 0)) {
    bt.excRateBudget = store.get('bytter_excRateBudget');
}

if(window.top !== window){
    checkData('dictContextGroup', 'bytter_dictGroup', getDictContextGroup);
    checkData('dictContext', 'bytter_dict', getDictContext);
    checkData('userResource', 'bytter_userresource', getUserResource)
    checkData('userCorpResource', 'bytter_resource', getUserCorpResource)
}

bt.dict = function (groupCode, code) {
    if(bt.dictContext[groupCode]){
        if(bt.dictContext[groupCode][code]){
            return bt.dictContext[groupCode][code].name
        }
    }
    return code||'';
   //  return bt.dictContext[groupCode] ? bt.dictContext[groupCode][code] ? bt.dictContext[groupCode][code].name : "" : "";
};
bt.dictGroup = function (groupCode) {
    return bt.dictContext[groupCode] ? bt.dictContext[groupCode] : [];
};

bt.isResource = function (code, corpId, index) {
    if (corpId) {
        var corpArr;
        var userCorpResource;
        try {
            // 如果顶层top 没有userCorpResource，将会报错，采用catch方法
            corpArr = window.top.bt.userCorpResource.resources[code] || bt.userCorpResource.resources[code];
            userCorpResource = window.top.bt.userCorpResource.corp || bt.userCorpResource.corp

        } catch (e) {
            corpArr = bt.userCorpResource.resources[code];
            userCorpResource = bt.userCorpResource.corp;
            // return  getResourceCorpIdsByPermission(code, corpId, index);
        }
        if (corpArr) {
            var newCorpIds = corpArr.split(',').map(function (item, index, ary) {
                // return window.top.bt.userCorpResource.corp[item]
                return userCorpResource[item]
            })
            if (newCorpIds) {
                if (newCorpIds.indexOf(corpId) === -1) {
                    if (index) {
                        layer.alert(bt.lang('common.message.dataResourceCheck', [index]), {icon: 7})
                    }
                    return false
                } else {
                    return true
                }
            }
        }
    } else {
        return bt.userResource.indexOf(code) !== -1
    }
};
function getLang() {
    $.ajaxSettings.async = false;
    $.ajax({
        url: basePath + (bt.loginLangOnly ? "/lang/login" : "/lang"), type: 'get', async: false, dataType: 'json',
        success: function (json) {
            if (!json) {
                return;
            }
            var lang = {};
            for (var i = 0; i < json.length; i++) {
                lang[json[i].langKey] = json[i].langContext;
            }
            bt.bytterMutiLang = lang;
        }
    });
    $.ajaxSettings.async = true;
}

/**
 * 获取用户资源权限
 * */
function getUserResource() {
    $.ajaxSettings.async = false;
    $.get(basePath + "/userResource", function (json) {
        if (json) {
            bt.userResource = json;
        }
    }, 'JSON');
    $.ajaxSettings.async = true;
}

/**
 * 获取字典方法
 */
function getDictContext() {
    $.ajaxSettings.async = false;
    $.get(basePath + "/dict", function (json) {
        if (!json) {
            return;
        }
        var dict = {};
        for (var i = 0; i < json.length; i++) {
            var temp = json[i];
            var tempGroupCode = temp.groupCode;
            if (!dict[tempGroupCode]) {
                dict[tempGroupCode] = {};
            }
            if (!dict[tempGroupCode][temp.code]) {
                dict[tempGroupCode][temp.code] = {name: temp.name, index: i, code: temp.code};
            }
        }
        bt.dictContext = dict;
    }, 'JSON');
    $.ajaxSettings.async = true;
}

/**
 * 获取字典组
 */
function getDictContextGroup() {
    $.ajaxSettings.async = false;
    $.post(basePath + "/sys/dict/allDictGroup", function (json) {
        if (!json) {
            return;
        }
        var dictGroup = new Array();
        for (var i = 0; i < json.length; i++) {
            var temp = json[i];
            if (temp && temp.groupCode) {
                dictGroup.push({"groupCode": temp.groupCode, "groupName": temp.groupName});
            }
        }
        bt.dictContextGroup = dictGroup;
    }, 'JSON');
    $.ajaxSettings.async = true;
}

/**
 * 获取用户单位资源权限
 */
function getUserCorpResource() {
    $.ajaxSettings.async = false;
    $.get(basePath + "/resource", function (json) {
        if (json) {
            bt.userCorpResource = json;
        }
    }, 'JSON');
    $.ajaxSettings.async = true;
}

function extractMenuId(url){
    if(!url){
        return '';
    }
    var menuIdArray = url.split("menuId=");
    if(menuIdArray.length < 2){
        return '';
    }
    var menuId = menuIdArray[menuIdArray.length-1];
    var endIndex = menuId.indexOf('&');
    if(endIndex > 0){
        menuId = menuId.substring(0,endIndex);
    }
    return menuId;
}

//ie浏览器 Object.assign 兼容写法
if (typeof Object.assign != 'function') {
    Object.assign = function(target) {
        'use strict';
        if (target == null) {
            throw new TypeError('Cannot convert undefined or null to object');
        }

        target = Object(target);
        for (var index = 1; index < arguments.length; index++) {
            var source = arguments[index];
            if (source != null) {
                for (var key in source) {
                    if (Object.prototype.hasOwnProperty.call(source, key)) {
                        target[key] = source[key];
                    }
                }
            }
        }
        return target;
    };
}


/**
 * 获取某个菜单下的资源权限方法
 */
function getUserCorpResourceByMenuId(url) {
    var menuId=extractMenuId(url);
    if(menuId.length == 0){
        return;
    }
    var isAccessed = false;
    if(window.top.bt.accessedMenuIds) {
        isAccessed = menuId in window.top.bt.accessedMenuIds
    }
    if(isAccessed){
        return;
    }
    $.ajaxSettings.async = false;
    $.get(basePath + "/resource/"+menuId, function (json) {
        if (json) {
            if(!window.top.bt.userCorpResource || !window.top.bt.userCorpResource.sign || !window.top.bt.userCorpResource.sign.allCorpIdsSign || json.sign.allCorpIdsSign != window.top.bt.userCorpResource.sign.allCorpIdsSign){
            //if(!window.top.bt.userCorpResource){
                // 如果有值，就合并
                if(window.top.bt.userCorpResource){
                    //只复制资源 资源结构有问题
                    //Object.assign(window.top.bt.userCorpResource.resources,json.resources);
                    //console.log(window.top.bt.userCorpResource)
                }else{
                    window.top.bt.userCorpResource = json;
                }
            }else{
                $.each(json.resources,function (index,value) {
                    window.top.bt.userCorpResource.resources[index]=value;
                });
                if(json.corp){
                    window.top.bt.userCorpResource.corp=json.corp;
                }
            }
            if(!window.top.bt.accessedMenuIds) {
                window.top.bt.accessedMenuIds = {};
            }
            window.top.bt.accessedMenuIds[menuId]=1;
        }
    }, 'JSON');
    $.ajaxSettings.async = true;
}

bt.isnotResource = function (code, corpId, index) {
    return !bt.isResource(code, corpId, index)
};

if (parent.bt.navtab) {
    bt.navtab = parent.bt.navtab;
}
bt.use(['layer'], function () {
    var $ = bt.jquery, layer = bt.layer;
    var ajaxQuenen = [];
    bt.loading = {
        loadingIndex: 0,
        show: function (time) {
            if (time) {
                this.loadingIndex = layer.load(1, {shade: [0.3, '#fff'], time: time});
            } else {
                this.loadingIndex = layer.load(1, {shade: [0.3, '#fff']});
            }
        },
        hide: function () {
            layer.close(this.loadingIndex);
        }
    };
});
var closeForms = function () {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if (index) {
        parent.layer.close(index); //再执行关闭
    } else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};

bt.trackFlowByBusinessId = function (businessId) {
    var url = basePath + "/bpm/my/detailPage";
    if (businessId) {
        url += "?businessId=" + businessId
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '580px'],
        content: url
    });
};

//初始化自动锁屏操作对象
function ScreenTimer(settings) {
    this.triggerTtime = settings.time;
    this.start = settings.start || false;
    document.onmousemove = ScreenTimer.prototype.onevent;
    document.onmousedown = ScreenTimer.prototype.onevent;
    document.onkeydown = ScreenTimer.prototype.onevent;
    document.onkeypress = ScreenTimer.prototype.onevent;
    this.signal();
}

//到时间触发回调方法进行锁屏
ScreenTimer.prototype.timeout = function () {
    if (this.start) {
       // if ($('.lock-btn').length === 0) {
        if (window.top && window.top.$('.lock-btn').length === 0) {
            $.get(basePath + "/oauth/logout");
            alert("由于您长时间未操作，为保证您账号安全，请重新登录！");
            window.open("about:blank", "_self").close()
        } else {
            if (window.lockScreen) {
                window.lockScreen.lockScreen();
            }
        }
    }
};

//回调定时任务设置
ScreenTimer.prototype.setTimeout = function () {
    if (this.timerID) {
        window.clearTimeout(this.timerID);
        this.timerID = null;
    }
    if (this.start) {
        var screenTimerObj = this;
        var fun = function () {
            screenTimerObj.timeout();
        };
        this.timerID = window.setTimeout(fun, this.triggerTtime);
    }
};

ScreenTimer.prototype.signal = function () {
    this.setTimeout();
};

ScreenTimer.prototype.onevent = function (e) {
    bt.screenTimer.signal();
};


var screenTimerSignal = function () {
    bt.screenTimer.setTimeout();
};

//自动锁屏定时任务初始化
var initScreenTimer = function (time) {
    if (!time) {
        time = 20 * 60 * 1000;
    } else {
        time = time * 60 * 1000;
    }
    if (!bt.screenTimer && top.bt.screenTimer) {
        bt.screenTimer = top.bt.screenTimer;
    }
    if (!bt.screenTimer) {
        bt.screenTimer = new ScreenTimer({time: time, start: true});
    } else {
        document.onmousemove = screenTimerSignal;
        document.onmousedown = screenTimerSignal;
        document.onkeydown = screenTimerSignal;
        document.onkeypress = screenTimerSignal;
    }
};

//自动锁屏设置是否启用定回调功能 true启用 false禁用
var screenTimerStart = function (start) {
    if (bt.screenTimer) {
        bt.screenTimer.start = start;
    }
};

var openExportExcel = function (exportUrl, data, code) {
    if (!exportUrl) {
        exportUrl = basePath + "/export/excel/exportAll";
    }
    var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "dataTablesInputParam").attr("value", encodeURIComponent(JSON.stringify(data.dataTablesInput))));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "exportExcelHeadListParam").attr("value", encodeURIComponent(JSON.stringify(data.columns))));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "totalParam").attr("value", encodeURIComponent(data.total)));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "fileNameParam").attr("value", encodeURIComponent(data.title)));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "codeParam").attr("value", encodeURIComponent(code)));
        form.appendTo('body');
        form.submit();
    } catch (e) {
        console.log(e);
    } finally {
        form.remove();
    }
}
/*
* 票据打印公共样式;
* 解决部分浏览器打印样式无效的问题
* */
var printStyle = '<head><link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all"><link rel="stylesheet" href="' + basePath + 'static/css/common.css" media="all">';
printStyle += '<style>.print-list *{font-family:\'宋体\'}.print-header{text-align:center;margin-bottom:20px}.print-header .print-header-name{font-size:24px}.print-header .print-header-title{letter-spacing:8px;font-size:18px;height:40px;line-height:40px}.print-header .print-header-title span{letter-spacing:normal;margin-left:2px}.print-body-header{margin-left:10px;margin-right:10px}.print-body-header tr td,.print-footer>table tr td{padding:2px}.print-body .layui-table{margin-top:2px}.print-body .layui-table thead tr{background-color:#fff}.print-body .layui-table>thead>tr>th,.print-body .layui-table>tbody>tr>td,.print-body .layui-table{border:1px solid #000!important}.print-body .layui-table>thead th,.print-body .layui-table>tbody td{padding:0 5px}.print-body thead th {font-weight: 400;height:30px;}</style></head>';

// 点击页面其它区域，关闭弹框
window.addEventListener('click',function () {
    window.top.$('#childMenu').removeClass('is-click-open').hide()
    window.top.$('#menuLayer').removeClass('child-open').removeClass('is-click-open').hide()
    // window.top.$('#sideMenuNav').find('.parent-active').removeClass('parent-active')
  //  window.top.$('.layui-side-mini').find('.mini-parent-active').removeClass('mini-parent-active')
    if(window.top.updateTopParentMenuCss){
        window.top.updateTopParentMenuCss()
    }
    window.top.$('#searchResult').hide()
    window.top.$('#tabMoreContainer').hide()
})

// 获取皮肤参数
var webSkin = store.get('web-skin')
if(webSkin === 'dark-skin'){
    $('body').addClass('dark-skin')
}else {
    $('body').removeClass('dark-skin')
}

bt.getSysSkin = function () {
    var webSkin = store.get('web-skin')
    if(!webSkin){
        webSkin = 'light-skin'
    }
    return webSkin
}

// html 转意方法
function htmlEscape(s){
    return s.replace(/&lt;/g,'<')
        .replace(/&gt;/g,">" )
}

// JSON 转对象
function parseJSON(str) {
    try{
        return  JSON.parse(str)
    }catch (e) {

    }
    return str
}

/**判断字符串是否为空*/
function isEmpty(ms) {
    if (typeof (ms) === "undefined" || ms === null || ms === "") {
        return true;
    }
    return false;
}

// 去除金额格式化的方法
function moneyUnformat(str) {
    // 处理金额去格式化
    if(str && str.indexOf(',') !== -1){
        var str1 = str.replace( /,/g, '' );
        var result = str1 - str1;
        if(isNaN(result)){
            return str
        }
        if(!(str1 - str1)){
            return str1
        }
    }
    return str
}

// 获取当前所有的 cookie
function clearCookies() {
    // var cookies = document.cookie.split(';');

    // 遍历所有 cookie，将过期时间设置为过去，强制使其失效
    // for (var  i = 0; i < cookies.length; i++) {
    //     var  cookie = cookies[i];
    //     var eqPos = cookie.indexOf('=');
    //     var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
    //     document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT';
    // }
    document.cookie = "Access-Token="+store.set('Access-Token')+"; expires=" + new Date(0).toUTCString()
}
