var $,form,moneyType,rateType,dateType,select,dateFun,dicts;
bt.use(['form', 'layedit', 'dicts', 'selectTree','rateType','selectType','moneyType','dateType','corp'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    rateType = bt.rateType.init();//利率
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    //监听提交
    form.on('submit(submit)', function (data) {
        data.field.status = $(this).attr("value");
        var action = "/agreeddeposit/choinneragreeddeposit/savePos";
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
        setAcc();
        if(corpId){
            var accUrl = basePath + "/agreeddeposit/choinneragreeddeposit?getBlnctrAccByCorpId&&corpId=" + corpId +"&busType=1";
            bt.selectType.get("accId").attr({"data-disabled": false,"data-url":accUrl});
        }
    }

    bt.corp.init();
    /**
     * 账号回调
     * @param data
     */
    accCallBack = function (data) {
        setAcc();
        if(data.id){
            $.ajax({
                url: basePath + "/agreeddeposit/choinneragreeddeposit?getChoInfo",
                method: 'POST',
                data: {accId: data.id,busType : '1'},
                dataType: 'json',
                async: true,
                success: function (data) {
                    if (data.obj) {
                        $("#blnctrAcc").val(data.obj.blnctrAcc);
                        $("#agreedDepositOriId").val(data.obj.id);
                        select.get("accId").val(data.obj.accId);
                        $('#accName').html(data.obj.accName);
                        $("#curId").val(data.obj.curId);
                        $('#curName').html(data.obj.curName);
                        $("#contractNo").val(data.obj.contractNo);
                        $("#beginDate").val(data.obj.beginDate);
                        $("#oldEndDate").val(data.obj.endDate);
                        dicts.get("certificateWay").val(data.obj.certificateWay);

                        if("0" == data.obj.certificateWay){
                            var days = dateFun.getBetweenDay(data.obj.beginDate,data.obj.endDate);
                            var newEndDate = dateFun.format(dateFun.addDay(data.obj.endDate, days));
                            dateType.get('endDate').val(newEndDate);
                        }

                        dicts.get("isAgreedDepositAcc").val(data.obj.isAgreedDepositAcc);
                        rateType.get("agrInterest").val(data.obj.agrInterest);
                        $("#agrAccId").val(data.obj.agrAccId);
                        $("#agrAcc").val(data.obj.agrAcc);
                        moneyType.get("retainedBal").val(data.obj.retainedBal);

                        if(data.obj.isAgreedDepositAcc === "1"){
                            $(".inter").attr("lay-verify","required");
                            $("#agrAcc").attr("lay-verify","required");
                            $("#retainedBal").attr("lay-verify","required");
                            $(".isAcc").show();
                        }else{
                            $("#agrInterest").attr("lay-verify","");
                            $(".inter").attr("lay-verify","");
                            $("#retainedBal").attr("lay-verify","");
                            $("#agrAcc").attr("lay-verify","");
                            $(".isAcc").hide();
                        }

                        if("0" == data.obj.isAgreedDepositAcc) {
                            var htmlText = "<thead>";
                            htmlText += "<tr >";
                            htmlText += '<th style="text-align: center;"><span class="sign">*</span>' + bt.lang("table.fc_charge_type_dtl.min_range_money") + '（>）</th>';
                            htmlText += '<th style="text-align: center;"><span class="sign">*</span>' + bt.lang("table.fc_charge_type_dtl.max_range_money") + '（<=）</th>';
                            htmlText += '<th style="text-align: center;"><span class="sign">*</span>' + bt.lang("table.btBankAcc.agrInterest") + '</th>';
                            htmlText += '</tr>';
                            htmlText += '</thead>';
                            htmlText += '<tbody>';
                            $.each(data.obj.detailList, function (index, item) {
                                var maxTemp = "";
                                var maxNum = "";
                                if (item.maxMoney != null) {
                                    maxTemp = item.maxMoney;
                                    maxNum = bt.money.format(item.maxMoney);
                                } else {
                                    maxTemp = "";
                                    maxNum = "";
                                }
                                htmlText += '<tr data-row="' + index + '">';
                                htmlText += '<td class="textRight"> ' + bt.money.format(item.minMoney);
                                htmlText += '<input type="hidden" id="minMoney"' + index + '" name="detailList[' + index + '].minMoney" data-name="detailList[' + index + '].minMoney" value="' + item.minMoney + '" class="min" >';
                                htmlText += '</td>';
                                htmlText += '<td class="textRight"> ' + maxNum;
                                htmlText += '<input type="hidden" id="maxMoney"' + index + '" name="detailList[' + index + '].maxMoney" data-name="detailList[' + index + '].maxMoney" value="' + maxTemp + '" class="max">';
                                htmlText += '</td>';
                                htmlText += '<td class="textRight"> ' + bt.rateType.getFun().format(item.agreeRate, 'percent', 4, ',');
                                htmlText += '<input type="hidden" id="agreeRate"+"' + index + '" name="detailList[' + index + '].agreeRate" data-name="detailList[' + index + '].agreeRate" value="' + item.agreeRate + '" class="agreeRate">';
                                htmlText += '</td>';
                                htmlText += '</tr>';
                            });
                            $("#dataTable").html(htmlText);
                        }else{
                            $("#dataTable").html('');
                        }
                        form.renderIcon();
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
        $("#oldEndDate").val('');
        dateType.get("endDate").val('');
        $("#curId").val('');
        $('#curName').html('');
        $("#dataTable").html('');
        $(".min").val('');
        $(".max").val('');
        $(".agreeRate").val('');
        dicts.get("certificateWay").val('0');
        dicts.get("isAgreedDepositAcc").val('0');
        rateType.get("agrInterest").val('');
        $("#agrAccId").val('');
        $("#agrAcc").val('');
        moneyType.get("retainedBal").val('');
        $("#agrInterest").attr("lay-verify","");
        $(".inter").attr("lay-verify","");
        $("#retainedBal").attr("lay-verify","");
        $("#agrAcc").attr("lay-verify","");
        $(".isAcc").hide();
    }

});


//账号回调
var getAccCallBack = function (data) {
    accCallBack(data);
}

//结束日期回调
var endCallBack = function (data) {
    var oldEndDate = $("#oldEndDate").val();
    if(dateFun.compare(oldEndDate,data) > 0){
        parent.layer.alert(bt.lang("cho.inneraccount.agreedDeposit.endDate.compare"), {icon: 2});
        dateType.get('endDate').val('');
        return false;
    }
}

//到期处理方式回调
var wayCallBack = function (data) {
    var endDate = $("#oldEndDate").val();
    if(data.value == "1"){
        dateType.get('endDate').val(endDate);
    }else{
        var beginDate = $("#beginDate").val();
        if(beginDate && endDate){
            var days = dateFun.getBetweenDay(beginDate,endDate);
            var newEndDate = dateFun.format(dateFun.addDay(endDate, days));
            dateType.get('endDate').val(newEndDate);
        }
    }
}