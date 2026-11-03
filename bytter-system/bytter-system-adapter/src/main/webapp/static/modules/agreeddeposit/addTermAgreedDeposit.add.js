var $,form,moneyType,rateType,dateType,select,dateFun;
bt.use(['form', 'layedit', 'dicts', 'selectTree','rateType','selectType','moneyType','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    rateType = bt.rateType.init();//利率
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    //监听提交
    form.on('submit(submit)', function (data) {
        data.field.status = $(this).attr("value");
        var action = "/agreeddeposit/fcagreeddeposit/saveTerm";
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
        var corpPoolChildUrl = basePath + "/agreeddeposit/fcagreeddeposit?listUserCorpPoolChilds&poolId=" + data.id+"&opType=2&checkSign=0";
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
        setAcc();
    }

    /**
     * 单位回调
     * @param data
     */
    corpCallback = function (data) {
        var netId = data.poolid;
        var corpId = data.corpid;
        setAcc();
        if(netId && corpId){
            var accUrl = basePath + "/agreeddeposit/fcagreeddeposit?getBlnctrAcc&netId=" + netId +"&corpId=" + corpId+"&busType=2";
            bt.selectType.get("accId").attr({"data-disabled": false,"data-url":accUrl});
        }else{
            bt.selectType.get("accId").attr("data-disabled", true);
        }

    }
    /**
     * 账号回调
     * @param data
     */
    accCallBack = function (data) {
        setAcc();
        if(data.id){
            $.ajax({
                url: basePath + "/agreeddeposit/fcagreeddeposit?getInfo",
                method: 'POST',
                data: {accId: data.id,busType : '2'},
                dataType: 'json',
                async: true,
                success: function (data) {
                    if (data.obj) {
                        $("#blnctrAcc").val(data.obj.blnctrAcc);
                        $("#agreedDepositOriId").val(data.obj.id);
                        select.get("accId").val(data.obj.accId);
                        $('#accName').html(data.obj.accName);
                        $("#contractNo").val(data.obj.contractNo);
                        $("#beginDate").val(data.obj.beginDate);
                        $("#endDate").val(data.obj.endDate);
                        $("#curId").val(data.obj.curId);
                        $('#curName').html(data.obj.curName);
                        var htmlText = "<thead>";
                        htmlText += "<tr >";
                        htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.fc_charge_type_dtl.min_range_money")+'（>）</th>';
                        htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.fc_charge_type_dtl.max_range_money")+'（<=）</th>';
                        htmlText +='<th style="text-align: center;"><span class="sign">*</span>'+bt.lang("table.btBankAcc.agrInterest")+'</th>';
                        htmlText +='</tr>';
                        htmlText +='</thead>';
                        htmlText +='<tbody>';
                        $.each(data.obj.detailList,function(index,item){
                            var maxTemp = "";
                            var maxNum = "";
                            if(item.maxMoney != null){
                                maxTemp = item.maxMoney;
                                maxNum = bt.money.format(item.maxMoney);
                            }else{
                                maxTemp = "";
                                maxNum = "";
                            }
                            htmlText += '<tr data-row="'+index+'">';
                            htmlText += '<td class="textRight"> '+bt.money.format(item.minMoney);
                            htmlText += '<input type="hidden" id="minMoney"'+index +'" name="detailList['+index+'].minMoney" data-name="detailList['+index+'].minMoney" value="'+item.minMoney+'" class="min" >';
                            htmlText += '</td>';
                            htmlText += '<td class="textRight"> '+ maxNum
                            htmlText += '<input type="hidden" id="maxMoney"'+index+'" name="detailList['+index+'].maxMoney" data-name="detailList['+index+'].maxMoney" value="'+maxTemp+'" class="max">';
                            htmlText += '</td>';
                            htmlText += '<td class="textRight"> '+bt.rateType.getFun().format(item.agreeRate,'percent',4,',');
                            htmlText += '<input type="hidden" id="agreeRate"+"'+index+'" name="detailList['+index+'].agreeRate" data-name="detailList['+index+'].agreeRate" value="'+item.agreeRate+'" class="agreeRate">';
                            htmlText += '</td>';
                            htmlText += '</tr>';
                        });
                        $("#dataTable").html(htmlText);
                    }
                }
            });
        }
    }

    setAcc = function () {
        select.get("accId").val('');
        $("#agreedDepositOriId").val('');
        $("#blnctrAcc").val('');
        $('#accName').html('');
        $("#contractNo").val('');
        $("#beginDate").val('');
        $("#endDate").val('');
        $("#curId").val('');
        $('#curName').html('');
        $("#dataTable").html('');
        $(".min").val('');
        $(".max").val('');
        $(".agreeRate").val('');
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

//终止日期回调
var stopCallBack = function (data) {
    var beginDate = $("#beginDate").val();
    var endDate = $("#endDate").val();
    if(dateFun.compare(beginDate,data) > 0){
        parent.layer.alert(bt.lang("cho.fcagreeddeposit.stopDate.greater.beginDate"), {icon: 2});
        dateType.get('stopDate').val('');
        return false;
    }
    if(dateFun.compare(data,endDate) > 0){
        parent.layer.alert(bt.lang("cho.fcagreeddeposit.stopDate.less.endDate"), {icon: 2});
        dateType.get('stopDate').val('');
        return false;
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
