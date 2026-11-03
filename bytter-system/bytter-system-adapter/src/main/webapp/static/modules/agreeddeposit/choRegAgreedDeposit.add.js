var $,form,moneyType,rateType,dateType,corp,select;
bt.use(['form', 'layedit', 'dicts', 'rateType','selectType','moneyType','dateType','corp'], function () {
    $ = bt.jquery, form = bt.form();
    select = bt.selectType.init();
    dateType = bt.dateType.init();
    removeVerifyIcon();
    //监听提交
    form.on('submit(submit)', function (data) {
        data.field.status = $(this).attr("value");
        var action = "/agreeddeposit/choinneragreeddeposit/save";
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
     * 单位回调
     * @param data
     */
    bt.corpCallback = function (corpId) {
        $("#blnctrAcc").val('');
        $('#accName').html('');
        $("#curId").val('');
        $('#curName').html('');
        dateType.get("beginDate").val('');
        $('#agrAcc').val('');
        if(corpId){
            var accUrl = basePath + "/agreeddeposit/choinneragreeddeposit?getBlnctrAccByCorpId&&corpId=" + corpId +"&busType=0";
            select.get("accId").attr({"data-disabled": false,"data-url":accUrl});
            getDate(corpId);
        }
    }

    /**
     * 是否存入协定账号回调
     */
    bt.isAgreedDepositAcc = function (data) {
        if(data.value === "1"){
            rateType.get("agrInterest").attr("lay-verify","required");
            moneyType.get("retainedBal").attr("lay-verify","required");
            $("#agrAcc").attr("lay-verify","required");
            $(".isAcc").show();
            $("#dataTable").html('');
        }else{
            rateType.get("agrInterest").val('');
            $("#agrAcc").val('');
            moneyType.get("retainedBal").val('');
            rateType.get("agrInterest").attr("lay-verify","");
            moneyType.get("retainedBal").attr("lay-verify","");
            $("#agrAcc").attr("lay-verify","");
            $(".isAcc").hide();

            var htmlText = "<thead>";
            htmlText += "<tr>";
            htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.fc_charge_type_dtl.min_range_money")+'（>）</th>';
            htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.fc_charge_type_dtl.max_range_money")+'（<=）</th>';
            htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.btBankAcc.agrInterest")+'</th>';
            htmlText +='<th style="text-align: center;">'+bt.lang("table.createRepayPlan.operation")+'</th>';
            htmlText +='</tr>';
            htmlText +='</thead>';
            htmlText +='<tbody>';
            htmlText += '<tr data-row=0>';
            htmlText += '<td>';
            htmlText += '<input type="text" id="minMoney0" name="detailList[0].minMoney" style="text-align:right" lay-verify="required" ' +
                'data-name="detailList[0].minMoney" data-biz-name="minMoney0" autocomplete="off" class="layui-input bt-biz-moneyType min" ' +
                'data-callback="changeMinMoney">';
            htmlText += '</td>';
            htmlText += '<td>';
            htmlText += '<input type="text" id="maxMoney0" name="detailList[0].maxMoney" style="text-align:right" data-name="detailList[0].maxMoney"' +
                ' data-biz-name="maxMoney0" autocomplete="off" class="layui-input bt-biz-moneyType max" data-callback="changeMaxMoney">';
            htmlText += '</td>';
            htmlText += '<td>';
            htmlText += '<input type="text" id="agreeRate0" name="detailList[0].agreeRate" data-name="detailList[0].agreeRate" ' +
                'class="layui-input bt-biz-rateType" data-biz-name="agreeRate0" lay-verify="required">';
            htmlText += '</td>';
            htmlText += '<td>';
            htmlText += '<a href="#" title="#springMessage(\'common.addTr\')" onClick="addTr()"><i class="layui-icon">&#xe654;</i></a>';
            htmlText += '</td>';
            htmlText += '</tr>';
            htmlText += '</tbody>';
            $("#dataTable").html(htmlText);
            moneyType.render();
            rateType.render();
        }
         form.renderIcon();
    }

    corp = bt.corp.init();
    bt.dicts.init();
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();
    /**
     * 账号回调
     * @param data
     */
    accCallBack = function (data) {
        // debugger
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

    trHtml = function (tab,minValue) {
        var rowsCount = tab.rows.length;
        var row = rowsCount - 1;
        var trHtml = '<tr data-row="'+row+'">';
        trHtml += '<td><input type="text" id="minMoney'+row+'" name="detailList['+row+'].minMoney" style="text-align:right" lay-verify="required"  ' +
                'data-name="detailList['+row+'].minMoney" data-biz-name="minMoney'+row+'" autocomplete="off" class="layui-input bt-biz-moneyType min" ' +
            'value="'+minValue+'" data-callback="changeMinMoney"></td>';
        trHtml += '<td><input type="text" id="maxMoney'+row+'" name="detailList['+row+'].maxMoney" style="text-align:right" ' +
                'data-name="detailList['+row+'].maxMoney" data-biz-name="maxMoney'+row+'" autocomplete="off" class="layui-input bt-biz-moneyType max" data-callback="changeMaxMoney"></td>';
        trHtml += '<td><input type="text" id="agreedRate'+row+'" name="detailList['+row+'].agreeRate" data-name="detailList['+row+'].agreeRate" class="layui-input bt-biz-rateType" data-biz-name="agreedRate" lay-verify="required"></td>';
        trHtml += '<td><a href="#" title="#springMessage("common.delTr")" class="del_tr"><i class="layui-icon">&#xe640;</i></a></td>';
        trHtml += '</tr>';
        $("#dataTable tbody").append(trHtml);

    }
});

//账号回调
var getAccCallBack = function (data) {
    // console.log(data,111)
    accCallBack(data);
}

function addTr() {
    var tab = document.getElementById("dataTable");
    var minValue = $("#dataTable tbody").find("tr:last .max").val();
    if(minValue){
        $("#dataTable .del_tr").remove()
        trHtml(tab,minValue);
        var lastRow = $("#dataTable tbody").find("tr:last").data('row');
        moneyType.get('maxMoney'+(lastRow-1)).attr("lay-verify","required");
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
            moneyType.get('maxMoney'+beforeRowIndex).val('')
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

    //没有下一行数据,当前值不为空校验
    if( !nextRowIndex ){
        if(value){
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
        }
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


//校验必填
function removeVerifyIcon () {
    $("#dataTable").find('tbody td span.pl').remove()
}

function selectAcc(){
    if (bt.navtab) {
        var corpId = corp.get("corpId").val();
        var curId = $("#curId").val();
        var beginDate = dateType.get("beginDate").val();
        var accId= select.get("accId").val();
        var agrInterest = rateType.get("agrInterest").val();
        if(!corpId.trim()){
            parent.layer.alert(bt.lang('fbs.budget.message.corp.null'), {icon: 2});
            return false;
        }
        if(!accId.trim()){
            parent.layer.alert(bt.lang('sys.login.message.account.null'), {icon: 2});
            return false;
        }
        if(!curId.trim()){
            parent.layer.alert(bt.lang('nis.netbank.message.curname.null'), {icon: 2});
            return false;
        }
        if(!beginDate.trim()){
            parent.layer.alert(bt.lang('cms.precative.startdate.not.null'), {icon: 2});
            return false;
        }
        if(!agrInterest.trim()){
            parent.layer.alert(bt.lang('nis.netbank.message.agrinterest.null'), {icon: 2});
            return false;
        }
        bt.navtab.addNavtab(bt.lang('table.fcAgreedDeposit.agrAcc'), basePath + "/agreeddeposit/choinneragreeddeposit/addAgreedAcc?corpId="+corpId+"&curId="+curId+"&beginDate="+beginDate+"&accId="+accId+"&agrInterest="+agrInterest);
        return;
    }
}

//获取日结日期
function getDate(data) {
    $.ajax({
        url: basePath + "/agreeddeposit/choinneragreeddeposit?getBeginDate",
        method: 'POST',
        data: {corpId: data},
        dataType: 'json',
        async: true,
        success: function (dataTemp) {
            if (dataTemp.obj) {
                dateType.get("beginDate").attr("data-min",dataTemp.obj);
            }
        }
    });
}
