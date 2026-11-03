var dateTypes,moneyTypes,selectTypes,dicts,rateTypes,parameters,form,ratioType;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','btUpload','rateType','ratioType'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    moneyFun=bt.moneyType.getFun();
    ratioType= bt.ratioType.init();

    var gid = $("#attachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == "null") {
            return true;
        }
        return false;
    }

    //自定义验证规则
    form.verify({
        code:function(value){
            var pattern = /^[a-zA-Z0-9|-]*$/;
            if(!pattern.test(value)){
                // 债券代码只能填写英文数字
                return '债券代码只能填写英文/数字';
            }
        }
    });

    //监听提交
    form.on('submit', function(data){
        var action = "/cms/bond/register/save"
        data.field.attachmentId = up.getGroupId();
        data.field.status = $(this).val();
        bt.loading.show();
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    if(data.field.id == ""){
                        bt.navtab.refreshCurrent();
                    }else{
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    }

                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });

});

function corpCallback(data) {
    $("#corpName").val(data.corpName)
}

function resetCallback() {
    window.location.reload();
}

function typeCallBack(data) {
    if (data.bondname === '可转债' || data.bondname === '可交债') {
        $("#convertStartDate").attr("lay-verify", "required")
        $("#convertEndDate").attr("lay-verify", "required")
        $("#convertRate").attr("lay-verify", "required")
    } else {
        $("#convertStartDate").removeAttr("lay-verify")
        $("#convertEndDate").removeAttr("lay-verify")
        $("#convertRate").removeAttr("lay-verify")
    }
    form.renderIcon()
}


/**
 * 折合人民币
 */
function conver() {
    var curId = selectTypes.get("bondCur").val();
    var moneysum = $("#issueCurTotal").val();
    var curPreTime = getTime(new Date());
    var rate = "";
    if (!bt.empty(curId) && !bt.empty(moneysum) && !bt.empty(curPreTime)) {
        bt.jquery.ajax({
            url: basePath + "/cms/lease/financing/info/conver/" + curId + "/" + curPreTime,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                rate = t;
                bt.moneyType.init().get("issueRmbTotal").val(moneyFun.mul(rate, moneysum, 2));
            },
        });
    }
}

function getTime(date) {
    let y = date.getFullYear() //年
    let m = date.getMonth() + 1  //月，月是从0开始的所以+1
    let d = date.getDate() //日
    m = m < 10 ? "0" + m : m //小于10补0
    d = d < 10 ? "0" + d : d //小于10补0
    return y + "-" + m + "-" + d; //返回时间形式yyyy-mm-dd
}