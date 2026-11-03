/**
 * 自定义密码框
 *
 */
var pwd = ''
var symbol= '•'
var isComposing = false;
var passwordEle = document.getElementById('lockInput');
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

/**
 * 锁屏
 * 本组件依赖jquery
 * liuyj
 */

var Lock = function () {
    this.element = ".lock-btn";
}

Lock.fn = Lock.prototype;

Lock.fn.init = function (options) {
    var that = this;

    $(that.element).on("click",function(){
        layer.confirm(bt.lang('sys.home.widget.whetherToLockScreen'), function(index){
            that.lockScreen();
        });
        setTimeout(function () {
            $('.info-btn').find('.layui-this').removeClass('layui-this')
        },50)
    });

    $("#unlock").on("click",function () {
        that.unlockFunc();
    })

    $(".lock-srk").off("keydown").on("keydown",function (event) {
        if (event.keyCode == "13") {
            //回车执行解锁
            $("#unlock").trigger("click");
            return false;
        }
    })

    that.checkLockScreen();
}
Lock.fn.lockScreen = function () {
    var that = this;
    $.get(basePath+'/sys/user/lockScreen',function(result){
        if(result.success){
            that.showScreen(true);
        }
        // 关闭首页全局加载动画
        $("#homeLoading").addClass('layui-hide');

        $(".lock-screen-container .input-content input").focus();
        screenTimerStart(false);
    },'JSON');
}

Lock.fn.checkSubmit = function (data) {
   this.unlockFunc()
    return false;
}

Lock.fn.unlockFunc = function(){
    var that = this;
    if(pwd.length === 0){
        layer.alert(bt.lang('sys.login.pleaseEnterPassword'), {icon: 2,zIndex:99999999});
        return;
    }
    $.get(basePath+'/randomString',function(rs) {
        var pd = pwd;
        var encrypted = sm2Encrypt(pd, rs.obj, 1);
        $.post(basePath+'/sys/user/unLockScreen',{password:encrypted,pubKey:rs.obj},function(result){
            if(result.success){
                that.showScreen(false);
                $(".lock-srk").val("");
                screenTimerStart(true);
            }else{
                $(".lock-srk").val("");
                layer.alert(bt.lang('sys.login.message.password.error'), {icon: 2,zIndex:99999999});
            }
        },'JSON');
    },'JSON');
}

Lock.fn.checkLockScreen = function () {
    var that = this;
    $.get(basePath+'/sys/user/checkLockScreen',function(result){
        //返回成功且数据为true
        if (result.success && result.obj) {
            that.lockScreen();
        }else{
            // 关闭首页全局加载动画
            $("#homeLoading").addClass('layui-hide');
        }

    },'JSON');
}

Lock.fn.showScreen = function (flag) {
    flag?$(".lock-screen-container").removeClass("hide"):$(".lock-screen-container").addClass("hide");
}
$(function () {
    window.lockScreen = new Lock();
    window.lockScreen.init();
})

