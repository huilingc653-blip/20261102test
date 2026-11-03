var moneyFun;
bt.use(['form', 'layedit', 'corp', 'selectType', 'dateType', 'moneyType', 'accountTable', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.accountTable.init({
        showCallback:function () {
            // 重新根据单位ID加载账户表格
            bt.accountTable.get("payerAcc").setCorp($('[name="corpId"]').val());
        }
    });
    bt.dicts.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    onSave = function (data) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            data.caCoding = $("#caCoding").val();
            var action = "/allocation/nisallocationreq/save";
            //控制重复提交
            if ($(data.elem).parent().hasClass('isSubmiting')) {
                bt.loading.hide();
                return false
            }
            //增加已提交的标志
            $(data.elem).parent().addClass('isSubmiting');
            $.post(basePath + action, data, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    //解除已提交的标志
                    $(data.elem).parent().removeClass('isSubmiting');
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParentTable();
                            bt.navtab.refreshCurrent();
                            var timeStamp = Date.parse(new Date());
                            $('#timeStamp').val(timeStamp);
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
        });
    };

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        data.field.status = $(this).attr("value");
        if($("#isOpenLimit").val()=="1") {
            bankAccLimit(data);
        }else{
            submit(data);
        }
        return false;
    });

    //单位限额
    function corpLimit(data,msg){
        //获取单位本位币换算汇率
        var rate = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, corpId: data.field.corpId},
            dataType: 'json',
            success: function (da) {
                rate = bt.money.mul(data.field.money,da,2)
            }
        });
        rate = parseFloat(rate);
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:1,wishPayDate:data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            msg += '<br>'+bt.lang('alert.over.daylimit.corp')//日累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        } else if(result === 2){
            msg += '<br>'+bt.lang('alert.over.monthlimit.corp')//月累计支付限额
            msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
        }

        if(msg.length>0){
            bt.loading.hide();
            parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info')},function (index) {
                submit(data);
            });
        }else{
            submit(data);
        }
    }

    //账号限额
    function bankAccLimit(data){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:1,wishPayDate:data.field.wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        var msg='';
        if (result=== 1) {
            msg = bt.lang('alert.over.paylimit')//单笔支付限额
        } else if(result === 2){
            msg = bt.lang('alert.over.daylimit')//日累计支付限额
        }else if(result === 3){
            msg = bt.lang('alert.over.monthlimit')//月累计支付限额
        }

        corpLimit(data,msg);
    }

    //提交
    function submit(data){
        var bankaccFlag = checkBankAccBal($("#balance").val(), data.field.money, "money");
        if (!bankaccFlag && $("#bankaccOver").val() == 2) {
            bt.loading.hide();
            return false;
        }
        var accountFlag = true;
        //上缴不需要考虑内部账户余额
        /*if($("#isAccount").val() == 1){
            accountFlag = checkFcAccBal($("#fcAccBalance").val(), data.field.money, "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }
        }*/
        //证书签名
        var signDataMsg = signCaData(data);
        if(signDataMsg != ""){
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg),{icon:2});
            return false;
        }
        if (bankaccFlag && accountFlag) {
            onSave(data.field);
        } else if (!bankaccFlag && accountFlag) {  // 银行账户余额不足
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'),function(index){
                onSave(data.field);
            });
        } else if (bankaccFlag && !accountFlag) {  // 内部账户余额不足
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'),function(index){
                onSave(data.field);
            });
        } else {     // 银行账户余额与内部账户余额不足
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'),function(index){
                onSave(data.field);
            });
        }
    }

    //证书签名
    function signCaData(data){
    	//数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
    	var sourceData = $('#payerAcc').val();//付款账号
    	var random = String(parseInt(Math.random()*10000000000));//随机数
    	while(random.length < 10) {
    		random = random + '0';
    	}
    	$("#random").val(random);
    	var payMoney = moneyFun.format(moneyFun.toNumber(moneyType.get("money").val()),{separator:''});//金额
    	while(payMoney.length < 14) {
    		payMoney = '0'+payMoney;
    	}
    	var wishPayday = dateFun.format(dateType.get("wishPayday").val(),'yyyyMMdd');//期望支付日期
    	sourceData += random + payMoney + wishPayday + $('#payeeAccName').val()+$('#payeeAcc').val();
    	var msg = signData(sourceData, "3");
    	data.field.random = random;
    	data.field.encrySource = sourceData;
    	data.field.caCoding = $("#caCoding").val();
    	return msg;
    }
});

/**
 * 选择付款单位回调
 * @param id
 */
function corpCallback(id) {
    clearPayeeAcc();
    // 重新根据单位ID加载账户表格
  //  bt.accountTable.get("payerAcc").setCorp(id);
    // 重新设置用途
    var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=2&corpId=" + id;
    bt.selectType.get("purpose").attr("data-url", purposeUrl)
}

/**
 * 选择账户表格回调
 * @param data
 */
function accountCallBack(data) {
    var $ = bt.jquery;
    clearPayeeAcc();
    $("#payerAccId").val(data.bankAccId);
    $("#payerAcc").val(data.bankAcc);
    $("#payerAccName").val(data.accName);
    $("#payerBankName").val(data.bankName);
    $("#payerBankCode").val(data.bankCode);
    $("#payerBankTypeId").val(data.bankTypeId);
    $("#curId").val(data.curId);
    $("#curName").val(data.curName);
    initFcAccBalance(data.bankAcc, data.curId,"fcAccBalance");
    initProvAndCity(data.bankAccId, data.curId, "payerProvCity");
    if (null != data.parentAccount) {
        $("#payeeCorpId").val(data.parentAccount.corpName);
        $("#payeeAccId").val(data.parentAccount.bankAccId);
        $("#payeeAcc").val(data.parentAccount.bankAcc);
        $("#payeeAccName").val(data.parentAccount.accName);
        $("#payeeBankName").val(data.parentAccount.bankName);
        $("#payeeBankCode").val(data.parentAccount.bankCode);
        $("#payeeBankTypeId").val(data.parentAccount.bankTypeId);
        initProvAndCity(data.parentAccount.bankAccId, data.parentAccount.curId, "payeeProvCity");
    }
    initBank();
    //获取单位信息
    $.ajax({
        url: basePath + "/bankacc/bisaccbal?getBal",
        method: 'POST',
        data: {bankAcc: data.bankAcc,curId: data.curId},
        dataType: 'json',
        success: function (data) {
            $("#balDiv").text(moneyFun.format(data));
            $("#balance").val(data);
        }
    });
}

purposeBefore = function(data){
    if($.isArray(data)){
        data.unshift({
            purpose:'',
        })
    }
    return data
}
