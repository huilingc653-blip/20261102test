var $,form,moneyType,rateType,dateType;
bt.use(['form', 'layedit', 'dicts', 'selectTree','rateType','selectType','moneyType','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    rateType = bt.rateType.init();//利率
    var select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    removeVerifyIcon();
    //监听提交
    form.on('submit(submit)', function (data) {
        data.field.status = $(this).attr("value");
        var action = "/agreeddeposit/fcagreeddeposit/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent();
                        bt.navtab.refreshParentTable();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 资金池回调
     * @param data
     */
    netIdCallback = function (data) {
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/agreeddeposit/fcagreeddeposit?listUserCorpPoolChilds&poolId=" + data.id+"&opType=0&checkSign=0";
        console.info(corpPoolChildUrl);
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
        setAcc();
        getDate(data);
    }

    /**
     * 单位回调
     * @param data
     */
    corpCallback = function (data) {
        var netId = data.poolid;
        var corpId = data.corpid;
        if(netId && corpId){
            var accUrl = basePath + "/agreeddeposit/fcagreeddeposit?getBlnctrAcc&netId=" + netId +"&corpId=" + corpId+"&busType=0";
            bt.selectType.get("accId").attr({"data-disabled": false,"data-url":accUrl});
        }else{
            setAcc();
        }

    }
    /**
     * 账号回调
     * @param data
     */
    accCallBack = function (data) {
        if(data.blnctracc){
            $("#blnctrAcc").val(data.blnctracc);
            $('#accName').html(data.accname);
            $("#curId").val(data.curid);
            $('#curName').html(data.curname);
        }else{
            $("#blnctrAcc").val('');
            $('#accName').html('');
            $("#curId").val('');
            $('#curName').html('');
        }

    }

    setAcc = function () {
        bt.selectType.get("accId").attr({"data-disabled": true,"data-defaultValue":''});
        $("#blnctrAcc").val('');
        $('#accName').html('');
        $("#curId").val('');
        $('#curName').html('');
    }

    trHtml = function (tab,minValue) {
        var rowsCount = tab.rows.length;
        var row = rowsCount - 1;
        var trHtml = '<tr data-row="'+row+'">';
        trHtml += '<td><input type="text" id="minMoney'+row+'" name="detailList['+row+'].minMoney" style="text-align:right" lay-verify="required|number2"  ' +
                'data-name="detailList['+row+'].minMoney" data-biz-name="minMoney'+row+'" autocomplete="off" class="layui-input bt-biz-moneyType min" ' +
            'value="'+minValue+'" data-callback="changeMinMoney"></td>';
        trHtml += '<td><input type="text" id="maxMoney'+row+'" name="detailList['+row+'].maxMoney" style="text-align:right" ' +
                'data-name="detailList['+row+'].maxMoney" data-biz-name="maxMoney'+row+'" autocomplete="off" class="layui-input bt-biz-moneyType max" data-callback="changeMaxMoney"></td>';
        trHtml += '<td><input type="text" id="agreedRate'+row+'" name="detailList['+row+'].agreeRate" data-name="detailList['+row+'].agreeRate" class="layui-input bt-biz-rateType" data-biz-name="agreedRate" lay-verify="required|number2"></td>';
        trHtml += '<td><a href="#" title="#springMessage("common.delTr")" class="del_tr"><i class="layui-icon">&#xe640;</i></a></td>';
        trHtml += '</tr>';
        $("#dataTable tbody").append(trHtml);

    }
});

//资金池回调
var getNetIdCallback = function (data) {
    netIdCallback(data);
}

//回调
var getCorpCallback = function (data) {
    corpCallback(data);
}

//账号回调
var getAccCallBack = function (data) {
    accCallBack(data);
}

function addTr() {
    var tab = document.getElementById("dataTable");
    var minValue = $("#dataTable tbody").find("tr:last .max").val();
    if(minValue){
        $("#dataTable .del_tr").remove()
        trHtml(tab,minValue);
        var lastRow = $("#dataTable tbody").find("tr:last").data('row');
        moneyType.get('maxMoney'+(lastRow-1)).attr("lay-verify","required|number2");
        rateType = bt.rateType.init();//利率
        moneyType = bt.moneyType.init();
        removeVerifyIcon()
    }else{
        parent.layer.alert(bt.lang('cms.agreed.addMaxNull'), {icon: 2});
    }
}

//监听删除标志
$('body').on('click','.del_tr',function () {
    var tbody =  $(this).parents('tbody');
    var row = $(this).parents('tr').data('row');
    moneyType.get('maxMoney'+(row-1)).removeAttr("lay-verify");
    $(this).parents('tr').remove();
    if(row != 1){
        tbody.find('tr:last-child').find('td:last-child').html('<a href="#" title="#springMessage("common.delTr")" class="del_tr"><i class="layui-icon">&#xe640;</i></a>');
    }

});


/**
 * 下限金额回调
 * @param value
 * @param elem
 */
function changeMinMoney(value , elem) {
    var beforeRowIndex = elem.parents('tr').prev('tr').data('row');

    //当前行
    var currentRowIndex = elem.parents('tr').data('row');
    var currentMaxMoney = bt.moneyType.getFun().toNumber(moneyType.get('maxMoney'+currentRowIndex).val());
    if(bt.moneyType.getFun().sub(currentMaxMoney,value) <= 0  && currentRowIndex ){
        moneyType.get('minMoney'+currentRowIndex).val('');
        if(beforeRowIndex){
            moneyType.get('maxMoney'+beforeRowIndex).val('');
        }
        parent.layer.alert(bt.lang('cms.fcChargeType.compareNumMin'), {icon: 2});
        return;
    }
    //检验上一行数据
    if(beforeRowIndex !== undefined){
        var minMoney = bt.moneyType.getFun().toNumber(moneyType.get('minMoney'+beforeRowIndex).val());
        if(bt.moneyType.getFun().sub(minMoney, value) < 0 ){
            moneyType.get('maxMoney'+beforeRowIndex).val(value)
        }else {
            moneyType.get('minMoney'+currentRowIndex).val('');
            moneyType.get('maxMoney'+beforeRowIndex).val('');
            parent.layer.alert(bt.lang('cms.agreed.compareNumBefore'), {icon: 2});
            return;
        }

    }
}

/**
 * 上限金额回调
 * @param value
 * @param elem
 */
function changeMaxMoney(value , elem) {
    var nextRowIndex = elem.parents('tr').next('tr').data('row');

    var currentRowIndex = elem.parents('tr').data('row');
    var currentMinMoney = bt.moneyType.getFun().toNumber(moneyType.get('minMoney'+currentRowIndex).val());
    if(bt.moneyType.getFun().sub(currentMinMoney,value) >= 0){
        moneyType.get('maxMoney'+currentRowIndex).val('');
        if(nextRowIndex){
            moneyType.get('minMoney'+nextRowIndex).val('');
        }
        parent.layer.alert(bt.lang('cms.fcChargeType.compareNum'), {icon: 2});
        return
    }
    //检验下一行数据
    if( nextRowIndex ){
        var nextMaxMoney = bt.moneyType.getFun().toNumber(moneyType.get('maxMoney'+nextRowIndex).val());
        if(bt.moneyType.getFun().sub(nextMaxMoney,value) > 0 ){
            moneyType.get('minMoney'+nextRowIndex).val(value)
        }else {
            moneyType.get('maxMoney'+currentRowIndex).val('');
            moneyType.get('minMoney'+nextRowIndex).val('');
            parent.layer.alert(bt.lang('cms.agreed.compareNumNext'), {icon: 2});
            return;
        }
    }
}

//关闭
var closeDoc=function(){
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    if(index){
        parent.layer.close(index); //再执行关闭
    }else {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
};

//校验必填
function removeVerifyIcon () {
    $("#dataTable").find('tbody td span.pl').remove()
}

function isExist(accId) {
    var flag = false;
    $.ajax({
        url: basePath + "/agreeddeposit/fcagreeddeposit?isExistBlnctrAcc",
        method: 'POST',
        data: {accId: accId,busType:0},
        dataType: 'json',
        async: true,
        success: function (dataTemp) {
            if (dataTemp.obj) {
                flag = true;
            }
        }
    });
}

//获取日结日期
function getDate(data) {
    $.ajax({
        url: basePath + "/agreeddeposit/fcagreeddeposit?getBeginDate",
        method: 'POST',
        data: {netId: data.id},
        dataType: 'json',
        async: true,
        success: function (dataTemp) {
            if (dataTemp.obj) {
                dateType.get("beginDate").attr("data-min",dataTemp.obj);
            }
        }
    });
}