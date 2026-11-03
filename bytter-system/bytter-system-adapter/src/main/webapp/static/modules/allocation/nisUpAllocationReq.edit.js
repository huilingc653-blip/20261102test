bt.use(['form', 'layedit', 'selectType', 'dateType', 'moneyType', 'dicts', 'selectTable', 'corp', 'selectTree'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();

    /**
     * 加载账户信息
     */
    initAccInfo = function () {
        $.ajax({
            url: basePath + "/bankacc/btbankacc?getBankAccInfoById",
            method: 'POST',
            data: {id: $("#payerAccId").val(), curId: $("#curId").val()},
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    var data = result.obj;
                    // 初始化付款人账户信息
                    $("#payerBankName").val(data.bankName);
                    // $("#balance").val(data.balance);
                    initFcAccBalance(data.bankAcc, data.curId, "fcAccBalance");
                    initBankAccBalance(data.bankAcc, data.curId, "balance");
                    // 加载付款账号
                    var selectPayeeTable = payeeAccInit(data.corpId, $("#payeeAccId").val());

                    // 初始化收款人账户信息
                    if ("undefined" != data.parentBankAcc && null != data.parentBankAcc) {
                        $("#payeeCorpId").val(data.parentBankAcc.corpName);
                        $("#payeeBankName").val(data.parentBankAcc.bankName);
                        $("#payeeBankCode").val(data.parentBankAcc.bankCode);

                        // 加载收款单位
                        bt.selectTree.init({
                            url: basePath + "/allocation/nisallocationreq/treeByNetId?prentAccId=" + $("#payeeAccId").val(),
                            elem: ".demo-selectTree-normal",
                            name: 'payeeCorpId',
                            id: 'id',
                            pId: 'pId',
                            required: false,
                            selected: data.corpId,
                            clickCallBack: function (id) {
                                $("#payerAccId").val("");
                                $("#payerAcc").val("");
                                $("#payerAccName").val("");
                                $("#payerBankName").val("");
                                $("#balance").val(0);
                                $("#fcAccBalance").val(0);

                                // 重新加载付款账号
                                selectPayeeTable.get("payerAcc").reload({
                                    corpId: id,
                                    prentAccId: $("#payeeAccId").val(),
                                    curId: $("#curId").val(),
                                });
                                // 重新设置用途
                                var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=2&corpId=" + id;
                                bt.selectType.get("purpose").attr("data-url", purposeUrl);
                            }
                        });
                    }
                    form.render();
                }
            }
        });
    };
    initAccInfo();

    /**
     * 重置按钮回调
     */
    resetCallback = function () {
        $("#payerAccId").val($("#payerAccId").data("defaultvalue"));
        $("#curId").val($("#curId").data("defaultvalue"));
        initAccInfo();
    }

    onSave = function (data) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            data.field.caCoding = $("#caCoding").val();
            var action = "/allocation/nisallocationreq/modify";
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.closeCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
        })
    };

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
            data: {curId: data.field.curId, corpId: data.field.payerAccId},
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
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.payerAccId,temp:0,id:data.field.id,wishPayDate:data.field.wishPayday},
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
            data: {curId: data.field.curId, payMoney: data.field.money, payerAcc: data.field.payerAcc,temp:0,id:data.field.id,wishPayDate:data.field.wishPayday},
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
        //上划不需要考虑内部账户余额
        /*if ($("#isAccount").val() == 1) {
            accountFlag = checkFcAccBal($("#fcAccBalance").val(), data.field.money, "money");
            if (!accountFlag && $("#accountOver").val() == 2) {
                bt.loading.hide();
                return false;
            }
        }*/
        //证书签名
        var signDataMsg = signCaData(data);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        if (bankaccFlag && accountFlag) {
            onSave(data);
        } else if (!bankaccFlag && accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.balance'), function (index) {
                onSave(data);
            });
        } else if (bankaccFlag && !accountFlag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.fcbalance'), function (index) {
                onSave(data);
            });
        } else {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('nis.allocation.message.money.gt.bal'), function (index) {
                onSave(data);
            });
        }
    }

    //证书签名
    function signCaData(data) {
        //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
        var sourceData = $('#payerAcc').val();//付款账号
        var random = String(parseInt(Math.random() * 10000000000));//随机数
        while (random.length < 10) {
            random = random + '0';
        }
        $("#random").val(random);
        var payMoney = moneyFun.format(moneyFun.toNumber(moneyType.get("money").val()), {separator: ''});//金额
        while (payMoney.length < 14) {
            payMoney = '0' + payMoney;
        }
        var wishPayday = dateFun.format(dateType.get("wishPayday").val(), 'yyyyMMdd');//期望支付日期
        sourceData += random + payMoney + wishPayday + $('#payeeAccName').val() + $('#payeeAcc').val();
        var msg = signData(sourceData, "3");
        data.field.random = random;
        data.field.encrySource = sourceData;
        data.field.caCoding = $("#caCoding").val();
        return msg;
    }

    /**
     * 收款账号
     * @param corpId
     * @param prentAccId
     */
    payeeAccInit = function (corpId, prentAccId) {
        var selectTable = bt.selectTable.init({
            elem: "#payerAccTable",
            url: basePath + "/allocation/nisallocationreq/getPayeeAcc",
            urldata: {corpId: corpId, prentAccId: prentAccId, curId: $("#curId").val()},
            scrollX: true,
            scrollY: "200px",
            searchInput: "#payerAcc",
            paging: true,
            columns: [
                {data: "bankAcc",},
                {data: "accName",},
                {data: "bankName",},
                {data: "curName",}
            ],
            clickCallback: function (data) {
                $("#payerAccId").val(data.id);
                $("#payerAcc").val(data.bankAcc);
                $("#payerAccName").val(data.accName);
                $("#payerBankName").val(data.bankName);
                $("#payerBankCode").val(data.bankCode);
                // $("#balance").val(data.balance);
                initFcAccBalance(data.bankAcc, data.curId, "fcAccBalance");
                initBankAccBalance(data.bankAcc, data.curId, "balance");
                initBank();
            }
        });
        return selectTable;
    }
});
