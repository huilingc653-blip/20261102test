/**
 * 自定义密码框
 *
 */
var pwd = ''
var symbol= '•'
var isComposing = false;
var passwordEle = document.getElementById('initPassword');
function CursorPosition (_inputEl) {
    this._inputEl = _inputEl
}
CursorPosition.prototype.getCursorPosition =function(){
    var rangeData = { text: '', start: 0, end: 0 }
    if (this._inputEl.setSelectionRange) {
        // W3C
        rangeData.start = this._inputEl.selectionStart
        rangeData.end = this._inputEl.selectionEnd
        rangeData.text = rangeData.start !== rangeData.end ? this._inputEl.value.substring(rangeData.start, rangeData.end) : ''
    } else if (document.selection) {
        // IE

        var i
        var oS = document.selection.createRange()
        var oR = document.body.createTextRange()
        oR.moveToElementText(this._inputEl)
        rangeData.text = oS.text
        rangeData.bookmark = oS.getBookmark()
        for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart('character', -1) !== 0; i++) {
            if (this._inputEl.value.charAt(i) === '\r') {
                i++
            }
        }
        rangeData.start = i
        rangeData.end = rangeData.text.length + rangeData.start
    }

    return rangeData
}
CursorPosition.prototype.setCursorPosition=function(rangeData){
    var oR
    if (!rangeData) {
        alert('You must get cursor position first.')
    }
    if (this._inputEl.setSelectionRange) {
        // W3C
        this._inputEl.setSelectionRange(rangeData.start, rangeData.end)
    } else if (this._inputEl.createTextRange) {
        // IE
        oR = this._inputEl.createTextRange()
        if (this._inputEl.value.length === rangeData.start) {
            oR.collapse(false)
            oR.select()
        } else {
            oR.moveToBookmark(rangeData.bookmark)
            oR.select()
        }
    }
}

function pwdSetData (positionIndex, value) {
    // 写入原始数据
    var _pwd = value.split(symbol).join('')
    if (_pwd) {
        var index = pwd.length - (value.length - positionIndex.end)
        pwd = pwd.slice(0, positionIndex.end - _pwd.length) + _pwd + pwd.slice(index)
    } else {
        pwd = pwd.slice(0, positionIndex.end) + pwd.slice(positionIndex.end + pwd.length - value.length)
    }
    pwd = changePassword(pwd)
}
function changePassword (pwd) {
    if (!pwd) return ''
    var reg = /^[\u4E00-\u9FA5]+$/
    var newPwd = ''
    for (var i = 0; i < pwd.length; i++) {
        if (!reg.test(pwd[i])) {
            newPwd += pwd[i]
        }
    }
    return newPwd
}

passwordEle.addEventListener('compositionstart', function () {
    isComposing = true
});
passwordEle.addEventListener('compositionend', function (event) {
    if (isComposing) {
        isComposing = false
        // handleCompositionEnd比handleInput后执行，避免isComposing还为true时handleInput无法执行正确逻辑
        passwordInput(event.target)
    }
});

var _theCount=0
function passwordInput(ele){
    if (!!window.ActiveXObject || 'ActiveXObject' in window) {
        _theCount++
        if (_theCount < 1) {
            return
        }
    }
    if (isComposing) return
    var cursorPosition = new CursorPosition(ele)
    var positionIndex = cursorPosition.getCursorPosition()
    pwdSetData(positionIndex, ele.value)
    if(!ele.value)return
    var data = ''
    var reg = /^[\u4E00-\u9FA5]+$/
    for (var i = 0; i < ele.value.length; i++) {
        if (!reg.test(ele.value[i])) {
            data += symbol
        }
    }
    ele.value = data
    cursorPosition.setCursorPosition(positionIndex, ele)
}

function passwordcont(){
    pwd=$('input[name="sys.initPassword"]').attr("data")
    var symbolNum=''
    for(var index = 0;index<pwd.length;index++){
        symbolNum+='•'
    }
    $('input[name="sys.initPassword"]').val(symbolNum);
}
passwordcont()

document.getElementById('reset').addEventListener('click',function(){
    pwd='123456'
})

var form;
bt.use(['form', 'dicts', 'element'], function () {
    form = bt.form(), $ = bt.jquery;
    bt.element();
    var dict = bt.dicts.init();
    //监听提交
    form.on('submit(configSubmit)', function (data) {
        data.field['sys.initPassword']=pwd
        var uiStyleOld = $("#uiStyleOld").val();
        var uiStyleNew = $("input[name='sys.uiStyle']:checked").val();
        if (uiStyleOld != uiStyleNew) {
            $.post(basePath + "/sys/config/save", data.field, function (result) {
                layer.confirm(bt.lang('sys.config.uiStyle.msg'), function (index) {
                    var pathUrl = basePath + '/oauth/logout';
                    $.get(pathUrl, function (json) {
                        //清除缓存的内容
                        clearCookies()
                        store.clearAll();
                        if (json.success) {
                            // 清除cookie token
                            // document.cookie = "Access-Token="+store.set('Access-Token')+"; expires=" + new Date(0).toUTCString();
                            top.location.href=basePath+"/login";
                            layer.close(index);
                        }else{
                            top.location.href=basePath+"/login";
                            //抛异常?
                        }
                    }, 'JSON');
                });
            }, "JSON");
        } else {
            $.post(basePath + "/sys/config/save", data.field, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, "JSON");
        }
        return false;
    });
    form.on('radio(sys.encrypt)', function (data) {
        encryptOpFun();
    });
    // 说明显示隐藏
    showDesc();
});

function encryptOpFun() {
    var dataValue = $('input:radio[name="sys.encrypt"]:checked').val();
    if (dataValue == 0 && sysEncryptVal != 0) {
        $("input:radio[name='sys.loginType'][value='0']").prop('checked', 'true');
        $('input:text[name="sys.issuer"]').each(function (i, item) {
            $(item).val("");
            $(item).attr("disabled", true).addClass("layui-disabled");
        });
        $('input:radio[name="sys.loginType"]').each(function (i, item) {
            $(item).attr("disabled", true).addClass("layui-disabled");
        });
        $('input:checkbox[name="sys.licenceApp"]').each(function (i, item) {
            $(item).attr("checked", false);
            $(item).attr("disabled", true).addClass("layui-disabled");
        });
        form.render('radio');
        form.render('checkbox');
    } else if (sysEncryptVal == 0 && dataValue != 0) {
        $('input:text[name="sys.issuer"]').each(function (i, item) {
            $(item).attr("disabled", false).removeClass("layui-disabled");
        });
        $('input:radio[name="sys.loginType"]').each(function (i, item) {
            $(item).attr("disabled", false).removeClass("layui-disabled");
        });
        $('input:checkbox[name="sys.licenceApp"]').each(function (i, item) {
            $(item).attr("disabled", false).removeClass("layui-disabled");
        });
        form.render('radio');
        form.render('checkbox');
    }
    sysEncryptVal = dataValue;
}


function showDesc() {
    var tip_index1 = 0;
    $('#licenceOverNoticeDay-desc').mouseenter(function () {
        tip_index1 = layer.tips(bt.lang('sys.config.licenceOverNoticeDay.desc'), '#licenceOverNoticeDay-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index1);
    });

    var tip_index2 = 0;
    $('#dualManager-desc').mouseenter(function () {
        tip_index2 = layer.tips(bt.lang('sys.config.dualManager.desc'), '#dualManager-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index2);
    });

    var tip_index3 = 0;
    $('#uiStyle-desc').mouseenter(function () {
        tip_index3 = layer.tips(bt.lang('sys.config.uiStyle.desc'), '#uiStyle-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index3);
    });

    var tip_index4 = 0;
    $('#initPassword-desc').mouseenter(function () {
        tip_index4 = layer.tips(bt.lang('sys.config.initPassword.desc'), '#initPassword-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index4);
    });

    var tip_index5 = 0;
    $('#password-minLength-desc').mouseenter(function () {
        tip_index5 = layer.tips(bt.lang('sys.config.password.minLength.desc'), '#password-minLength-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index5);
    });

    var tip_index6 = 0;
    $('#password-model-desc').mouseenter(function () {
        tip_index6 = layer.tips(bt.lang('sys.config.password.model.desc'), '#password-model-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index6);
    });

    var tip_index7 = 0;
    $('#password-deadLine-desc').mouseenter(function () {
        tip_index7 = layer.tips(bt.lang('sys.config.password.deadLine.desc'), '#password-deadLine-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index7);
    });

    var tip_index8 = 0;
    $('#passwordCloseBtn-desc').mouseenter(function () {
        tip_index8 = layer.tips(bt.lang('sys.config.passwordCloseBtn.desc'), '#passwordCloseBtn-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index8);
    });

    var tip_index9 = 0;
    $('#loginOpenIpLimit-desc').mouseenter(function () {
        tip_index9 = layer.tips(bt.lang('sys.config.loginOpenIpLimit.desc'), '#loginOpenIpLimit-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index9);
    });

    var tip_index10 = 0;
    $('#ipWhiteList-desc').mouseenter(function () {
        tip_index10 = layer.tips(bt.lang('sys.config.ipRemark'), '#ipWhiteList-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index10);
    });

    var tip_index11 = 0;
    $('#issuer-desc').mouseenter(function () {
        tip_index11 = layer.tips(bt.lang('sys.config.issuer.desc'), '#issuer-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index11);
    });

    var tip_index12 = 0;
    $('#isOpenLimit-desc').mouseenter(function () {
        tip_index12 = layer.tips(bt.lang('sys.config.isOpenLimit.desc'), '#isOpenLimit-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index12);
    });

    // 定时任务启用管理
    var tip_index13 = 0;
    $('#taskManage-desc').mouseenter(function () {
        tip_index13 = layer.tips(bt.lang('sys.config.taskManage.desc'), '#taskManage-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index13);
    });

    var tip_index14 = 0;
    $('#rate-desc').mouseenter(function () {
        tip_index14 = layer.tips(bt.lang('sys.config.rate.desc'), '#rate-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index14);
    });

    var tip_index15 = 0;
    $('#netbankCfcaDateAlertDays-desc').mouseenter(function () {
        tip_index15 = layer.tips(bt.lang('sys.config.netbankCfcaDateAlertDays.desc'), '#netbankCfcaDateAlertDays-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index15);
    });

    var tip_index16 = 0;
    $('#blackListCheck-desc').mouseenter(function () {
        tip_index16 = layer.tips(bt.lang('sys.config.blackListCheck.desc'), '#blackListCheck-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index16);
    });

    var tip_index17 = 0;
    $('#attachmentFormat-desc').mouseenter(function () {
        tip_index17 = layer.tips(bt.lang('sys.config.attachment.formatTip'), '#attachmentFormat-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index17);
    });

    // 客商统一管理
    var tip_index18 = 0;
    $('#externalCorp-desc').mouseenter(function () {
        tip_index18 = layer.tips(bt.lang('sys.config.externalCorp.desc'), '#externalCorp-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index18);
    });

    var tip_index19 = 0;
    $('#accPermission-desc').mouseenter(function () {
        tip_index19 = layer.tips(bt.lang('sys.config.accPermission.desc'), '#accPermission-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index19);
    });

    var tip_index20 = 0;
    $('#appPageSize-desc').mouseenter(function () {
        tip_index20 = layer.tips(bt.lang('sys.config.appPageSize.desc'), '#appPageSize-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index20);
    });
}
