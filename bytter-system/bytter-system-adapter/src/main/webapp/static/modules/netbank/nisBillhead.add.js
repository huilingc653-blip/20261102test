bt.use(['form', 'layedit', 'dateType', 'corp', 'selectTable', 'bank', 'rateType',
    'bankInfo', 'moneyType', 'region', 'externalCorpBankAcc', 'accebillInfoPay', 'accebillInfoReceive',
    'accebillIntellect','accebillInfo',
    'accebillView', 'btUpload', 'dicts', 'selectType', 'inputExternalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dicts = bt.dicts.init();
    bt.rateType.init();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);

    /** 计划明细控制项 zuoy 2018-11-19**/
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }
    form.on('checkbox(isCommon)', function (data) {
        var checked = data.elem.checked;
        if(checked){
            $("#isCommonDiv").show();
        }else{
            $("#isCommonDiv").hide();
        }
        form.render("checkbox");
    });

    /**
     * 收款账户值改变后回调
     */
    $("#payeeAccName").change(function () {
        if (dtlColSels["payeeAccName"]) {
            renderBudget();
        }
    });

    /**
     * 收款账户值改变后回调
     */
    $("#payeeAccNameTemp").change(function () {
        if (dtlColSels["payeeAccName"]) {
            renderBudget();
        }
    });

    /**
     * 用途回调
     */
    purposeCallBack = function () {
        if (dtlColSels["purpose"]) {
            renderBudget();
        }
    }

    /**
     * 供应商查询
     * @param value
     */
    seachExternalCorpBankAcc = function (value) {
        value = encodeURI(value);
        var url = basePath + "/sys/externalCorp/listByInputExternalCorp?corpId=" + corp.get("corpId").val() + "&seach=" + value;
        inputExternalCorpBankAcc.get("payeeAccName").setUrl({url: url});
    }

    /**
     * 供应商查询选择回调
     * @param data
     * @param dom
     */
    inputExternalCorpCallback = function (value) {
        if (value) {
            var data = value.data;
            $("#payeeAcc").val(data.externalAcc);
            $("#payeeAccName").val(data.name);
            //value.elem.val(data.name);
            $("#payeeBankCode").val(data.bankSourceCode);
            $("#payeeBank").val(data.bank);
            $("#bankTypeId").val(data.bankType);
            //设置省市组的值
            region.get('payeeAddr').set([{
                name: 'prov',
                defaultValue: data.accProvince
            }, {
                name: 'city',
                defaultValue: data.accCity
            }]);
            $('#payeeProv').val(data.accProvince);
            $('#payeeCity').val(data.accCity);
            //判断跨行标志和地域标志
            difBankAndAreaSign();
            if (dtlColSels["payeeAccName"] || (data.bank && dtlColSels["payeeBank"])) {
                renderBudget();
            }
        }
    }
    var inputExternalCorpBankAcc = bt.inputExternalCorpBankAcc.init();

    /**
     * 业务类别回调
     * @param data
     */
    busTypeCallback = function (data) {
        $("#defItemCode").val(data.itemcode);

        // 重新设置共享业务类型
      //  var shareBusTypeUrl = basePath + "/netbank/nissharebustype/listValidShareBusTypeByBusType?busTypeId=" + data.busid;
      //  bt.selectType.get("shareBusTypeId").attr("data-url", shareBusTypeUrl);
        renderBudget();
    }

    var selectType = bt.selectType.init();
    /**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.bank.init({elem: '#payeeAcc'});
    bt.bankInfo.init();
    var region = bt.region.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });



    //showIsPayed 显示已付款标识
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        $("#isPayedTitle").show();
        $("#isPayed").show();
    } else {
        $("#isPayedTitle").hide();
        $("#isPayed").hide();
    }
    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查
        //if ($("#sysVoucherCode").val() == "49" || $("#sysVoucherCode").val() == "50" || $("#sysVoucherCode").val() == "34") {
            externalCorpBankAcc.set({
                "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
                type: "2"
            });
        /*} else {
            externalCorpBankAcc.set({
                "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
                type: "0"
            });
        }*/
        return true;
    }

    bt.accebillInfoParams = function(){
    	return true
    }


    var accebillInfoPay = bt.accebillInfoPay.init();
    var accebillInfoReceive = bt.accebillInfoReceive.init();
    var accebillIntellect = bt.accebillIntellect.init();
    var accebillView = bt.accebillView.init();
    var accebillInfo = bt.accebillInfo.init();

    // 应付票据
    bt.accebillInfoPayParams = function () {
        var showIsPayed = $("#showIsPayed").val();
        if(showIsPayed == "1"){
            accebillInfoPay.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": 1,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "isPayed":bt.dicts.get("isPayed").val(),//是否已付款
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "oldIds": $("#billId").val()
            });
        }else{
            accebillInfoPay.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": 1,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "oldIds": $("#billId").val()
            });
        }
        return true;
    }

    // 应收票据
    bt.accebillInfoReceiveParams = function () {
        var showIsPayed = $("#showIsPayed").val();
        if(showIsPayed == "1"){
            accebillInfoReceive.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": 0,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "isPayed":bt.dicts.get("isPayed").val(),//是否已付款
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "billBalance": $("#billBalance").val(),
                "ppFlg":"1",
                "oldIds": ""//新增的时候不传oldid,避免和暂存数据混淆（若此票已经在另一个暂存的付款单中被部分占用，新增的时候多次选同一张票，挂接付款余额会不一样）
            });
        }else{
            accebillInfoReceive.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": 0,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "billBalance": $("#billBalance").val(),
                "ppFlg":"1",
                "oldIds": ""//新增的时候不传oldid,避免和暂存数据混淆（若此票已经在另一个暂存的付款单中被部分占用，新增的时候多次选同一张票，挂接付款余额会不一样）
            });
        }
        return true;
    }

    bt.accebillIntellectParams = function () {
        var isEmiBill = 0;
        if ($("#sysVoucherCode").val() == "49") {
            isEmiBill = 1;
        }
        var showIsPayed = $("#showIsPayed").val();
        if(showIsPayed == "1"){
            accebillIntellect.set({
                "resource":'netbank:netBank:nisBillHead:add',
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": isEmiBill,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "isPayed":bt.dicts.get("isPayed").val(),//是否已付款
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "billBalance": $("#billBalance").val(),
                "oldIds": $("#billId").val(),
                "pledgeeName": $("#payerAccName").val(),
                "pledgeeAcc": $("#payerAcc").val(),
                "pledgeeBankAcc": $("#payerBankCode").val(),
                "pledgeeBankName": $("#payerBank").val(),
                "billMoney":$("#payMoney").val()
            });
        }else{
            accebillIntellect.set({
                "resource":'netbank:netBank:nisBillHead:add',
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": isEmiBill,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "payee":$("#payeeAccName").val(),//收款人名称(应付票据对应-收票人)
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "billBalance": $("#billBalance").val(),
                "oldIds": $("#billId").val(),
                "pledgeeName": $("#payerAccName").val(),
                "pledgeeAcc": $("#payerAcc").val(),
                "pledgeeBankAcc": $("#payerBankCode").val(),
                "pledgeeBankName": $("#payerBank").val()
                ,"billMoney":$("#payMoney").val()
            });
        }
        return true;
    }
    bt.accebillViewParams = function () {
        var isEmiBill = 0;
        if ($("#sysVoucherCode").val() == "49") {
            isEmiBill = 1;
        }
        accebillView.set({"id": $("#billId").val()
            ,"billBalance": $("#billBalance").val()
            , "isEmitBill": isEmiBill});
        return true;
    }
    bt.accebillInfoCallback = function (datas, rom) {
        var billIds = "";
        var billbalances = "";
        var vvIds = "";
        var billPoolIds = "";
        $.each(datas.data, function (index, data) {
            if (billIds == "") {
                billIds += data.id;
                billbalances += data.balance;
                vvIds += data.vvid;
            } else {
                billIds += "," + data.id;
                billbalances += "," + data.balance;
                vvIds += "," + data.vvid;
            }
            if(data.poolStatus==1){
                if (billPoolIds == "") {
                    billPoolIds += data.id;
                } else {
                    billPoolIds += "," + data.id;
                }
            }
        });
        $("#vvid").val(vvIds);
        $("#billId").val(billIds);
        $("#billBalance").val(billbalances);
        $("#billPoolIds").val(billPoolIds);
        // 是否选择了新系统出票并且允许分包，如是付款金额设置为只读
        if (datas.isSelectNewBill === "1") {
            $('input[data-name="payMoney"]').attr("readonly", "readonly");
        } else {
            var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
            if(allowDifferentBillMoney === "1"){
                $('input[data-name="payMoney"]').removeAttr("readonly");
            }else{
                $('input[data-name="payMoney"]').attr("readonly", "readonly");
            }
        }
        bt.moneyType.get("payMoney").val(datas.sumMoney);
        bt.moneyType.get("billMoney").val(datas.sumMoney);
    }

    bt.accebillIntellectCallback = function (datas, rom) {
        var billIds = "";
        var vvIds = "";
        var billPoolIds = "";
        var billbalances = "";
        $.each(datas.data, function (index, data) {
            if (billIds == "") {
                billIds += data.id;
                billbalances += data.balance;
                vvIds += data.vvid;
            } else {
                billIds += "," + data.id;
                billbalances += "," + data.balance;
                vvIds += "," + data.vvid;
            }
            if(data.poolStatus==1){
                if (billPoolIds == "") {
                    billPoolIds += data.id;
                } else {
                    billPoolIds += "," + data.id;
                }
            }
        });
        $("#vvid").val(vvIds);
        $("#billId").val(billIds);
        $("#billBalance").val(billbalances);
        $("#billPoolIds").val(billPoolIds);
        // 是否选择了新系统出票并且允许分包，如是付款金额设置为只读
        if (datas.isSelectNewBill === "1") {
            $('input[data-name="payMoney"]').attr("readonly", "readonly");
        } else {
            var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
            if(allowDifferentBillMoney === "1"){
                $('input[data-name="payMoney"]').removeAttr("readonly");
            }else{
                $('input[data-name="payMoney"]').attr("readonly", "readonly");
            }
        }
        bt.moneyType.get("payMoney").val(datas.sumMoney);
        bt.moneyType.get("billMoney").val(datas.sumMoney);
    }

    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",},
        {data: "isOnlineStr",}
    ];
    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAcc",
        urldata: {corpId: $("input[name='corpId']").val(), voucherTypeId: $("select[name='voucherTypeId']").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccName').val(data.accName);
                $('#payerBank').val(data.bankName);
                $('#accId').val(data.id);
                $("#curDiv").text(data.curName);
                $('#curId').val(data.curId);
                $('#billCorpName').val(data.corpName);
                $('#accCorpId').val(data.corpId);
                $('#payerBankTypeId').val(data.bankTypeId);
                //获取账户余额
                var isAccount = $("#isAccount").val();//是否记账：0-否 1-是
                if(isAccount == "0"){
                    //获取单位信息
                    $.ajax({
                        url: basePath + "/bankacc/bisaccbal?getBal",
                        method: 'POST',
                        data: {bankAcc: data.bankAcc,curId: data.curId},
                        dataType: 'json',
                        success: function (data) {
                            moneyType.get("balance").val(data);
                            $("#balanceDiv").text(moneyFun.format(data));
                            moneyType.get("availBal").val(data);
                            $("#availBalDiv").text(moneyFun.format(data));
                            form.render();
                        }
                    });
                } else {
                    //内部账号
                    moneyType.get("balance").val(data.balance);
                    $("#balanceDiv").text(moneyFun.format(data.balance));
                }

                //获取账户余额包含在途金额
                // $.ajax({
                //     url: basePath + "/netbank/nisbillhead?getAccountBal",
                //     method: 'POST',
                //     data: {account: data.bankAcc,curId: data.curId,isAccount:isAccount},
                //     dataType: 'json',
                //     success: function (data) {
                //         moneyType.get("availBal").val(data);
                //         $("#availBalDiv").text(moneyFun.format(data));
                //         form.render();
                //     }
                // });
                $('#bifCode').val(data.bifCode);
                $('#payerBankCode').val(data.bankCode);
                $('#bankNamePrefix').val(data.bankNamePrefix);
                $('#payerProv').val(data.prov);
                $('#payerCity').val(data.city);
                //判断跨行标志和地域标志
                difBankAndAreaSign();
                //转付，重新加载资金下拨账号
                if ($("#sysVoucherCode").val() == "02") {
                    $("#downAcc").val("");
                    downAccInit.get("downAcc").reload({
                        corpId: $("input[name='corpId']").val(),
                        curId: $('#curId').val(),
                        account: $("#payerAcc").val()
                    });
                }
                //代付，重新加载资金下拨账号
                if ($("#sysVoucherCode").val() == "33") {
                    $("#realPayerAcc").val("");
                    realPayerAccInit.get("realPayerAcc").reload({
                        corpId: $("input[name='corpId']").val(),
                        voucherTypeId: $("select[name='voucherTypeId']").val(),
                        payerAcc: $("#payerAcc").val()
                    });
                }
                //重新渲染资金计划
                renderBudget();
            } else {
                $('input.text').val('');
            }
        }
    });

    //收款账号
    var payeeAccInit = bt.selectTable.init({
        elem: "#payeeAccTable",
        url: basePath + "/netbank/nisbillhead/getPayeeAcc",
        urldata: {
            corpId: $("input[name='corpId']").val(),
            curId: $('#curId').val(),
            voucherTypeId: $("select[name='voucherTypeId']").val()
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payeeAccTemp",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payeeAcc').val(data.bankAcc);
                $('#payeeAccTemp').val(data.bankAcc);
                $('#payeeAccNameTemp').val(data.accName);
                $('#payeeBank').val(data.bankName);
                //判断跨行标志和地域标志
                difBankAndAreaSign();
                if (dtlColSels["payeeAcc"] || (data.accName && dtlColSels["payeeAccName"]) || (data.bankName && dtlColSels["payeeBank"])) {
                    renderBudget();
                }
            } else {
                $('input.text').val('');
            }
        }
    });

    //资金下拨账号
    var downAccInit = bt.selectTable.init({
        elem: "#downAccTable",
        url: basePath + "/netbank/nisbillhead/getDownAcc",
        urldata: {corpId: $("input[name='corpId']").val(), curId: $('#curId').val(), account: $("#payerAcc").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#downAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#downAccId').val(data.id);
                $('#downAcc').val(data.bankAcc);
                $('#downAccName').val(data.accName);
                $('#bankNamePrefix').val(data.bankNamePrefix);
                //转付以下拨账号的接口代码为准
                $('#bifCode').val(data.bifCode);
            } else {
                $('input.text').val('');
            }
        }
    });

    //结算中心实际付款账号
    var realPayerAccInit = bt.selectTable.init({
        elem: "#realPayerAccTable",
        url: basePath + "/netbank/nisbillhead/getRealPayerAcc",
        urldata: {corpId: $("input[name='corpId']").val(), voucherTypeId: $("select[name='voucherTypeId']").val(), payerAcc: $("#payerAcc").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#realPayerAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#realPayerAccId').val(data.id);
                $('#realPayerAcc').val(data.bankAcc);
                $.ajax({
                    url: basePath + "/bankacc/bisaccbal?getBal",
                    method: 'POST',
                    data: {bankAcc: data.bankAcc,curId: data.curId},
                    dataType: 'json',
                    success: function (data) {
                        $("#realPayerAccBalDiv").text(moneyFun.format(data));
                        form.render();
                    }
                });
            } else {
                $('input.text').val('');
            }
        }
    });

    /**
     * 跨行标志，地域标志判断
     */
    function difBankAndAreaSign() {
        if ($("#payerBankCode").val() == "" || $("#payeeBankCode").val() == "") {
            dicts.get("difBank").val(0);
        } else if ($("#payerBankCode").val().substr(0, 3) == $("#payeeBankCode").val().substr(0, 3)) {
            //商业银行根据开户行名称前缀匹配
            if($("#payerBankCode").val().substr(0, 3) == "313" || $("#payerBankCode").val().substr(0, 3) == "402"
                || $("#payerBankCode").val().substr(0, 3) == "314"){
                if($("#bankNamePrefix").val()!="" && $("#payeeBank").val().indexOf($("#bankNamePrefix").val()) != -1){
                    dicts.get("difBank").val(0);
                }else{
                    dicts.get("difBank").val(1);
                }
            }else {
                dicts.get("difBank").val(0);
            }
        } else {
            dicts.get("difBank").val(1);
        }
        if ($("#payerProv").val() == "" || $("#payeeProv").val() == "") {
            dicts.get("areaSign").val(0);
        } else if ($("#payerProv").val() == $("#payeeProv").val() && $("#payerCity").val() == $("#payeeCity").val()) {
            dicts.get("areaSign").val(0);
        } else {
            dicts.get("areaSign").val(1);
        }
        form.render('radio');
    }

    /**
     * 校验科目余额是否需要控制
     * @returns {Array}
     */
    checkCtl = function () {
        var attr = [];
        $("#budgetProcessDataTable").find("div[row]").each(function () {
            if ($(this).attr("data-gt-balance")) {
                attr.push($(this).attr("data-gt-balance"));
            }
        });
        return attr;
    }

    /**
     * 校验账户余额
     * @param data
     * @returns {boolean}
     */
    checkBalance = function (data) {
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        //账户余额校验
        var isAccount = $("#isAccount").val();//是否记账：0-否 1-是
        var accountOver = $("#accountOver").val();//内部账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var balControlType = $("#balControlType").val();//付款余额控制类型：0-账户余额 1-可付款余额
        var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
        var isPayed = bt.dicts.get("isPayed").val();//是否已付款
        var sysVoucherCode = $("#sysVoucherCode").val();
        //sysVoucherCode=49 应付票据
        //sysVoucherCode=50 应收票据
        if(balControlType == "1"){
            balance = moneyFun.toNumber(moneyType.get("availBal").val());//可用余额
        }
        //付款金额大于账户余额
        var flag = false;
        if (payMoney > balance) {
            if (isAccount == "0") {
                if (bankaccOver == "1") {
                    flag = true;
                } else if (bankaccOver == "2" && sysVoucherCode !="49"
                    && sysVoucherCode !="50" && isPayed != '1') {
                    bt.loading.hide();
                    layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                    return false;
                }
            } else {
                if (accountOver == "1") {
                    flag = true;
                } else if (accountOver == "2") {
                    bt.loading.hide();
                    layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
                    return false;
                }
            }
        }
        if (flag && sysVoucherCode !="49"
            && sysVoucherCode !="50" && isPayed != '1') {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function (index) {
                bt.loading.show();
                rePay(data);
            });
        } else {
            rePay(data);
        }
    }

    var signDataMsg;
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        var value= $(this).attr("value");
        var isOpenBill = bt.dicts.get("isOpenBill").val();
        var openBillInfo=  $("#openBillInfo").val();
        var sysVoucherCode=  $("#sysVoucherCode").val();

        if(!openBillInfo && sysVoucherCode == "49" && value != '0'&isOpenBill!='1'){
            bt.loading.hide();
            layer.alert("请先完善票据信息！", {icon: 7});
            return false;
        }
        //校验白名单
        corpCheck(data);
        bt.loading.hide();
        return false;
    });

    //校验黑白名单
    function corpCheck(data){
        //对黑白名单进行验证
        var reData=externalCorpCheck({"name":data.field.payeeAccName,"corpId":data.field.corpId});
        if(reData.reCode=='3'){
            bt.loading.hide();
            parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
            return;
        }else if(reData.reCode=='2'){
            bt.loading.hide();
            parent.layer.confirm(reData.msg,function(){
                if($("#isOpenLimit").val()=="1") {
                    bankAccLimit(data);
                }else{
                    submit(data);
                }
            });
        }else{
            if($("#isOpenLimit").val()=="1") {
                bankAccLimit(data);
            }else{
                submit(data);
            }
        }
    }

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
                rate = bt.money.mul(data.field.payMoney,da,2)
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
            data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:1,wishPayDate:data.field.wishPayday},
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
        $("#attachmentId").val(up.getGroupId());
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        //判断应收、应付票据是否选择票据
        var billId = $("#billId").val();
        var isOpenBill = bt.dicts.get("isOpenBill").val();
        var sysVoucherCode = $("#sysVoucherCode").val();
        var billValidConfirm = false; //票据确认提示
        if (sysVoucherCode == "50" || (sysVoucherCode == "49" && isOpenBill == "1")) {
            if(!billId || billId == "") {
                bt.loading.hide();
                layer.alert(bt.lang("nis.netbank.message.billid.null"), {icon: 2});
                return false;
            }
            var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
            var billMoney = moneyFun.toNumber(moneyType.get("billMoney").val());
            if(billMoney != payMoney) {
                if (allowDifferentBillMoney != "1") {
                    bt.loading.hide();
                    layer.alert(bt.lang("nis.netbank.message.billmoney.mismatch"), {icon: 2});
                    return false;
                } else {
                    billValidConfirm = true;
                }
            }
        }
        //证书签名
         signDataMsg = signCaData(function toSubmit() {
            bt.loading.show();
            //判断期望支付日期（根据支付结算参数是否控制期望支付日期）
            var curDate = new Date();
            var wishPayday = dateType.get("wishPayday").val();//期望支付日期
            if (dateFun.compare(wishPayday, curDate) > 0) {
                bt.loading.hide();
                layer.confirm(bt.lang("nis.netbank.message.curDate.lt.wishPayday"), {icon:3},function () {
                    submitTemp(data);
                });
            }else{
                if(billValidConfirm){
                    layer.confirm(bt.lang("nis.netbank.message.billmoney.mismatch.confirm"), {icon:3},function () {
                        bt.loading.hide();
                        submitTemp(data);
                    });
                    bt.loading.hide();
                    return false;
                }else {
                    submitTemp(data);
                }
            }
        });
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }

        //判断期望支付日期（根据支付结算参数是否控制期望支付日期）
        //var curDate = new Date();
        //var wishPayday = dateType.get("wishPayday").val();//期望支付日期
        //if (dateFun.compare(wishPayday, curDate) > 0) {
        //    bt.loading.hide();
        //    layer.confirm(bt.lang("nis.netbank.message.curDate.lt.wishPayday"), {icon:3},function () {
        //        submitTemp(data);
        //    });
        //}else{
        //    if(billValidConfirm){
        //        layer.confirm(bt.lang("nis.netbank.message.billmoney.mismatch.confirm"), {icon:3},function () {
        //            bt.loading.hide();
        //            submitTemp(data);
        //        });
        //        bt.loading.hide();
        //        return false;
        //    }else {
        //        submitTemp(data);
        //    }
       // }
    }

    //封装
    function submitTemp(data){
        var itemCodeCtl = checkCtl();
        if (itemCodeCtl.length) {
            bt.loading.hide();
            if (!dtlColSels.length) {
                parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.detailbalance.notctl', [itemCodeCtl]),
                    {
                        btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                        icon: 0,
                    }, function () {
                        bt.loading.show();
                        checkBalance(data);
                    });
            } else {
                var fbsItemBalance = $("#fbsItemBalance").val();
                if (fbsItemBalance == 0) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.itembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                } else if (fbsItemBalance == 1) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.topitembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                } else if (fbsItemBalance == 2) {
                    parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.superitembalance.notctl', [itemCodeCtl]),
                        {
                            btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                            icon: 0,
                        }, function () {
                            bt.loading.show();
                            checkBalance(data);
                        });
                }
            }
        } else {
            checkBalance(data);
        }
    }

    function submitForm(data) {
        bt.loading.hide();
        var submitmeg = "您确认提交吗？";
        var status = $("#status").val();
        if(status == '0'){
            submitmeg = "您确认暂存吗？";
        }
        parent.layer.confirm(submitmeg, function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            var action = "/netbank/nisbillhead/save";
            //控制重复提交
            if($(data.elem).parent().hasClass('isSubmiting')){
                bt.loading.hide();
                return false
            }
            //增加已提交的标志
            $(data.elem).parent().addClass('isSubmiting');
            var isReserve = $("#isReserve").is(":checked")
            var payMoney = moneyType.get("payMoney").val();
            $.post(basePath + action, JSON.stringify($('#inputForm').serialize()), function (result) {
                bt.loading.hide();
                parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result), function () {
                    parent.layer.closeAll();
                    //解除已提交的标志
                    $(data.elem).parent().removeClass('isSubmiting');
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParentTable();
                            //判断是否保留付款方信息，不刷新
                            if(!isReserve) {
                                bt.navtab.refreshCurrent();
                            } else {
                                //可用金额扣减
                                var availBal = moneyType.get("availBal").val();
                                availBal = moneyFun.sub(availBal, payMoney);
                                moneyType.get("availBal").val(availBal);
                                $("#availBalDiv").text(moneyFun.format(availBal));
                            }
                            resetInit(data);
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
        });
    }

    //重复付款校验
    function rePay(data) {
        var result = false;
        $.ajax({
            url: basePath + "/netbank/nisbillhead?rePay",
            method: 'POST',
            async: false,
            data: {corpId: data.field.corpId, payMoney: data.field.payMoney, payeeAcc: data.field.payeeAcc, curId: data.field.curId, id: ''},
            dataType: 'json',
            success: function (data) {
                result = data.obj;
            }
        });
        if (result) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.repay'), function (index) {
                submitForm(data);
            });
        } else {
            submitForm(data);
        }
        return false;
    }

    //证书签名
    function signCaData(callBack) {
        //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
        var sourceData = $('#payerAcc').val();//付款账号
        var random = String(parseInt(Math.random() * 10000000000));//随机数
        while (random.length < 10) {
            random = random + '0';
        }
        $("#random").val(random);
        var payMoney = moneyFun.format(moneyFun.toNumber(moneyType.get("payMoney").val()), {separator: ''});//金额
        while (payMoney.length < 14) {
            payMoney = '0' + payMoney;
        }
        var wishPayday = dateFun.format(dateType.get("wishPayday").val(), 'yyyyMMdd');//期望支付日期
        sourceData += random + payMoney + wishPayday + $('#payeeAccName').val() + $('#payeeAcc').val();
        return signData(sourceData,"2",callBack);
    }

    //付款方式选择监听
    form.on('select(voucherTypeId)', function (data) {
        //获取付款方式信息
        voucerTypeCallback(data.value);
    });

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#payeeAcc").val(data.externalAcc);
            inputExternalCorpBankAcc.get('payeeAccName').val(data.name);
            $("#payeeBankCode").val(data.bankSourceCode);
            $("#payeeBank").val(data.bank);
            $("#bankTypeId").val(data.bankType);
            //设置省市组的值
            region.get('payeeAddr').set([{
                name: 'prov',
                defaultValue: data.accProvince
            }, {
                name: 'city',
                defaultValue: data.accCity
            }]);
            $('#payeeProv').val(data.accProvince);
            $('#payeeCity').val(data.accCity);
            //判断跨行标志和地域标志
            difBankAndAreaSign();
            if (dtlColSels["payeeAcc"] || (data.name && dtlColSels["payeeAccName"]) || (data.bank && dtlColSels["payeeBank"])) {
                renderBudget();
            }
        } else {  // 选择网点或者成员单位信息
            $("#payeeAcc").val(data.bankAcc);
            $("#payeeAccName").val(data.accName);
            $("#payeeBankCode").val(data.bankCode);
            $("#payeeBank").val(data.bankName);
            $("#bankTypeId").val(data.bankTypeId);
            $("#blRcvpymtpsUnnIdNo").val(data.socCode);
            //设置省市组的值
            region.get('payeeAddr').set([{
                name: 'prov',
                defaultValue: data.prov
            }, {
                name: 'city',
                defaultValue: data.city
            }]);
            $('#payeeProv').val(data.prov);
            $('#payeeCity').val(data.city);
            //判断跨行标志和地域标志
            difBankAndAreaSign();
            if (dtlColSels["payeeAcc"] || (data.corpName && dtlColSels["payeeAccName"]) || (data.bankName && dtlColSels["payeeBank"])) {
                renderBudget();
            }
        }
        $("#blRcvpymtpsUnnIdNo").val((data.socCode == null || data.socCode == undefined) ? "" : data.socCode);
    };

    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#payeeBankCode").val(data.bankCode);
        $("#payeeBank").val(data.bankName);
        $("#bankTypeId").val(data.id);
        //设置省市组的值
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: data.prov
        }, {
            name: 'city',
            defaultValue: data.city
        }]);
        $('#payeeProv').val(data.prov);
        $('#payeeCity').val(data.city);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
        if (dtlColSels["payeeBank"]) {
            renderBudget();
        }
    };

    //省回调函数
    bt.provCallback = function (value, othis, options) {
        $('#payeeProv').val(value);
        $('#payeeCity').val("");
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };
    //市回调函数
    bt.cityCallback = function (value, othis, options) {
        $('#payeeCity').val(value);
        //判断跨行标志和地域标志
        difBankAndAreaSign();
    };

    //单位回调函数
    bt.corpCallback = function (id) {
        // zuoy 2017-10-19 选择单位后重置票据
        $("#billId").val("");
        if(id){
            //获取单位信息
            $.ajax({
                url: basePath + "/sys/corp/getSysCorp",
                method: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        if (data.obj.id) {
                            var accCorpId = $("#accCorpId");
                            accCorpId.val(data.obj.id)
                            if (data.obj.userAccountCode) {
                                accCorpId.val(accCorpId.val() + ',' + data.obj.userAccountCode);
                            }
                        }
                    }
                    form.render();
                }
            });

            //获取单位分配的付款方式
            $.ajax({
                url: basePath + "/netbank/btassignpayway?getAssignPayWayAll",
                method: 'POST',
                data: {corpId: id},
                dataType: 'json',
                success: function (data) {
                    $("#voucherTypeId").empty();
                    if (data && data.obj && data.obj.length > 0) {
                        $.each(data.obj, function (index, item) {
                            $("#voucherTypeId").append("<option value=" + item.voucherCode + ">" + item.voucherName + "</option>");
                            if(item.defVoucherCode){
                                $("select[name='voucherTypeId']").val(item.defVoucherCode);
                            }
                        });
                        //获取付款方式信息
                        var voucherTypeId = $("select[name='voucherTypeId']").val();
                        voucerTypeCallback(voucherTypeId);
                    } else {
                        $("#voucherTypeId").append("<option value=''></option>");
                        //重新加载付款账号
                        payerAccInit.get("payerAcc").rebuild({
                            urldata: {corpId: id, voucherTypeId: ""},
                            columns: payerAccCol
                        });
                        //重新加载收款账号
                        payeeAccInit.get("payeeAccTemp").reload({
                            corpId: id,
                            curId: $('#curId').val(),
                            voucherTypeId: ""
                        });
                        //转付，重新加载资金下拨账号
                        if ($("#sysVoucherCode").val() == "02") {
                            $("#downAcc").val("");
                            downAccInit.get("downAcc").reload({
                                corpId: id,
                                curId: $('#curId').val(),
                                account: $("#payerAcc").val()
                            });
                        }
                        //代付，重新加载资金下拨账号
                        if ($("#sysVoucherCode").val() == "33") {
                            $("#realPayerAcc").val("");
                            realPayerAccInit.get("realPayerAcc").reload({
                                corpId: id,
                                voucherTypeId: "",
                                payerAcc: $("#payerAcc").val()
                            });
                        }
                        renderBudget();
                    }
                    form.render();
                }
            });
            // 重新设置用途
            var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + id;
            bt.selectType.get("busType").attr("data-url", busTypeUrl);

            // 重新设置用途
            var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=1&corpId=" + id;
            bt.selectType.get("purpose").attr("data-url", purposeUrl);
        }

    };

    //初始化单位
    var corp = bt.corp.init();


    //付款方式选择事件
    function voucerTypeCallback(voucherTypeId) {
        if (voucherTypeId != "") {
            $.ajax({
                url: basePath + "/netbank/fcvouchertype?getFcVoucherType",
                method: 'POST',
                data: {id: voucherTypeId},
                dataType: 'json',
                async: true,
                success: function (data) {
                    if (data.obj) {
                        // 付款办理当付款方式为“网银付款”时，是否已付款设置成“是”且不允许修改
                        // T24090014835 付款办理-付款方式优化 付款方式=网银转账，能选到银企和非银企的账户，并且线下付款（不产生指令）默认为【是】且不允许修改。
                        if(data.obj.voucherType == "64"){
                            bt.dicts.get("isPayed").attr({
                                'data-disabledValue': '0',
                                'value': '1',
                            });
                            isPayedCallBack();
                        }else{
                            bt.dicts.get("isPayed").attr({
                                'data-disabledValue': '',
                                'value': '0',
                            });
                        }
                        $("#sysVoucherCode").val(data.obj.sysVoucherCode);
                        $("#isAccount").val(data.obj.isAccount);
                        $("#isBudget").val(data.obj.isBudget);
                        $("#settlType").val(data.obj.settlType);
                        //重新加载付款账号
                        if(data.obj.isAccount && data.obj.isAccount == "1") {
                            payerAccCol = [
                                {data: "bankAcc",},
                                {data: "accName",},
                                {data: "bankName",},
                                {data: "curName",},
                                {data: "isOnlineStr",visible: false}
                            ];
                        }else{
                            payerAccCol = [
                                {data: "bankAcc",},
                                {data: "accName",},
                                {data: "bankName",},
                                {data: "curName",},
                                {data: "isOnlineStr",visible: true}
                            ];
                        }
                        payerAccInit.get("payerAcc").rebuild({
                            urldata: {corpId: $("input[name='corpId']").val(), voucherTypeId: $("select[name='voucherTypeId']").val()},
                            columns: payerAccCol
                        });
                        //清空所有付款信息
                        $('#payerAcc').val("");
                        $('#payerAccName').val("");
                        $('#payerBank').val("");
                        $('#payerBankCode').val("");
                        $('#accId').val("");
                        $("#curDiv").text("");
                        $('#curId').val(data.curId);
                        $('#billCorpName').val("");
                        moneyType.get("balance").val(0);
                        moneyType.get("availBal").val(0);
                        $("#balanceDiv").text(moneyFun.format(0));
                        $("#availBalDiv").text(moneyFun.format(0));
                        $('#bifCode').val("");
                        $('#payerBankCode').val("");
                        $('#payerProv').val("");
                        $('#payerCity').val("");
                        $("#downAccId").val("");
                        //清空票据相关
                        $("#billId").val("");
                        moneyType.get("billMoney").val(0);
                        // //重新加载收款账号
                        payeeAccInit.get("payeeAccTemp").reload({
                            corpId: $("input[name='corpId']").val(),
                            curId: $('#curId').val(),
                            voucherTypeId: $("select[name='voucherTypeId']").val()
                        });
                        // //转付，重新加载资金下拨账号
                        if ($("#sysVoucherCode").val() == "02") {
                            $("#downAcc").val("");
                            downAccInit.get("downAcc").reload({
                                corpId: $("input[name='corpId']").val(),
                                curId: $('#curId').val(),
                                account: $("#payerAcc").val()
                            });
                        }
                        //代付，重新加载资金下拨账号
                        if ($("#sysVoucherCode").val() == "33") {
                            $("#realPayerAcc").val("");
                            realPayerAccInit.get("realPayerAcc").reload({
                                corpId: $("input[name='corpId']").val(),
                                voucherTypeId: $("select[name='voucherTypeId']").val(),
                                payerAcc: $("#payerAcc").val()
                            });
                        }
                        if ($("#sysVoucherCode").val() == "49") {
                            //是否已开发票参数为否时，票据类型和流通介质字段需为必填项
                            var isOpenBill = dicts.get("isOpenBill").val();
                            if(isOpenBill == 0){
                                dicts.get("billType").setRequired(true);
                                dicts.get("billFlag").setRequired(true);
                            }else{
                                dicts.get("billType").setRequired(false);
                                dicts.get("billFlag").setRequired(false);
                            }
                        }else{
                            dicts.get("billType").setRequired(false);
                            dicts.get("billFlag").setRequired(false);
                        }
                        if($("#sysVoucherCode").val() == "49" || $("#sysVoucherCode").val() == "50"){
                            $("#billDiv").show();
                        }else{
                            $("#billDiv").hide();
                        }
                        //判断跨行标志和地域标志
                        difBankAndAreaSign();
                        //资金计划
                        renderBudget();
                        show();
                    }
                }
            });
        }
    }

    function show() {
        var $ = bt.jquery, form = bt.form();
        var isAccount = $("#isAccount").val();//是否记账 0，否 1，是
        if (isAccount == "1") {
            $("#difBankDiv").hide();//跨行标志和地域标志隐藏
            $("#directPayFlag").val("n");
        } else {
            $("#difBankDiv").show();//跨行标志和地域标志显示
            $("#directPayFlag").val("y");
        }
        var sysVoucherCode = $("#sysVoucherCode").val();//系统结算方式
        //02-转付，且记账情况下
        if (sysVoucherCode == "02" && isAccount == "1") {
            $("#downAccDiv").show();//下拨账号显示
            $("#downAcc").attr("lay-verify", "required");
        } else {
            $("#downAccDiv").hide();//下拨账号隐藏
            $("#downAcc").removeAttr("lay-verify");
        }
        //33-代付
        if (sysVoucherCode == "33") {
            $("#realPayerAccIdDiv").show();//结算中心实际付款账号显示
            $("#realPayerAcc").attr("lay-verify", "required");
        } else {
            $("#realPayerAccIdDiv").hide();//结算中心实际付款账号隐藏
            $("#realPayerAcc").removeAttr("lay-verify");
        }
        //05-内转
        if (sysVoucherCode == "05") {
            $("#selPayeeBankBut").hide();//开户行选择按钮隐藏
            $("#payeeBankCodeDiv").hide();//开户行行号隐藏
            $("#payeeAddr").hide();//省市隐藏
            $("#payeeAddrDiv").hide();
            $("#payeeBankCode").removeAttr("lay-verify");
            $("#payeeBank").removeAttr("lay-verify");
            $("#payeeAccDiv").hide();
            $("#payeeAccButDiv").hide();
            $("#payeeAccTempDiv").show();
            $("#payeeAccTemp").attr("lay-verify", "required|tableData");
            $("#payeeAcc").val("");
            $("#payeeAccName").val("");
            $("#payeeBankCode").val("");
            $("#payeeBank").val("");
            region.get('payeeAddr').set([{'required': false}, {'required': false}]);

            $("#payeeAccNameDiv").hide();
            $("#payeeAccNameTempDiv").show();
            $("#payeeAccNameTemp").attr("lay-verify", "required");
            $("#payeeAccNameTemp").attr("name", "payeeAccName");
            $("#payeeAccName").removeAttr("name");
            $("#payeeAccName").removeAttr("lay-verify");
            $("#payeeAccName").val("");
        } else {
            $("#selPayeeBankBut").show();//开户行选择按钮显示
            $("#payeeBankCodeDiv").show();//开户行行号显示
            $("#payeeAddr").show();//省市显示
            $("#payeeAddrDiv").show();
            $("#payeeBankCode").attr("lay-verify", "required");
            $("#payeeBank").attr("lay-verify", "required");
            $("#payeeAccDiv").show();
            $("#payeeAccButDiv").show();
            $("#payeeAccTempDiv").hide();
            $("#payeeAccTemp").removeAttr("lay-verify");
            $("#payeeAccTemp").val("");
            region.get('payeeAddr').set([{'required': true}, {'required': true}]);

            $("#payeeAccNameDiv").show();
            $("#payeeAccNameTempDiv").hide();
            $("#payeeAccName").attr("lay-verify", "required");
            $("#payeeAccName").attr("name", "payeeAccName");
            $("#payeeAccNameTemp").removeAttr("name");
            $("#payeeAccNameTemp").removeAttr("lay-verify");
            $("#payeeAccNameTemp").val("");
        }
        if (sysVoucherCode == "50" || sysVoucherCode == "49") {
            $("#payerAcc").removeAttr("lay-verify");
            $("#payerAccName").removeAttr("lay-verify");
            $("#payerBank").removeAttr("lay-verify");
            $('#curId').val($("#natualCurId").val());
            $("#curDiv").text($("#natualCurName").val());
            $("#billTd2").show();//票据类型,票据流通介质显示
            //50-应收票据
            if (sysVoucherCode == "50") {
                $("#billTd").show();//选票显示
            } else {
                $("#billTd").hide();//选票隐藏
            }
            //49-应付票据
            if (sysVoucherCode == "49") {
                $("#billTd1").show();//选票显示
            } else {
                $("#billTd1").hide();//选票隐藏
            }
            var isOpenBill = bt.dicts.get("isOpenBill").val();
            if(sysVoucherCode == "50" || (sysVoucherCode == "49" && isOpenBill == "1")){
                $("#billMoneyTr").show();
                var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
                if(allowDifferentBillMoney == "1"){
                    $('input[data-name="payMoney"]').removeAttr("readonly");
                }else{
                    $('input[data-name="payMoney"]').attr("readonly", "readonly");
                }
                //bt.corp.get("busCorpId").val("");
                $("busPersonnel").val("");
                $("#billBusTr").hide();
            }else{
                $("#billMoneyTr").hide();
                $('input[data-name="payMoney"]').removeAttr("readonly");
                $("#billBusTr").show();
                var corpId = bt.corp.get("corpId").val()
                //bt.corp.get("busCorpId").val(corpId);
            }
        } else {
            $("#payerAcc").attr("lay-verify", "required|tableData");
            $("#payerAccName").attr("lay-verify", "required");
            $("#payerBank").attr("lay-verify", "required");
            $('#curId').val("");
            $("#curDiv").text("");

            $('input[data-name="payMoney"]').removeAttr("readonly");
            $("#billTd").hide();//选票隐藏
            $("#billTd1").hide();//选票隐藏
            $("#billTd2").hide();//票据类型,票据流通介质隐藏
            $("#billMoneyTr").hide();
            $("#billBusTr").hide();
            //清除票据类型和票据流通介质的值
            dicts.get("billType").val("");
            dicts.get("billFlag").val("");
        }
        form.renderIcon();
    };

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $('input[name="corpId"]').val();
        var itemCode = $('input[name="payItem"]').val();
        var defItemCode = $('input[name="defItemCode"]').val();
        var processDate = dateType.get("wishPayday").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            defItemCode: defItemCode,
            processDate: processDate,
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            settlType: $("#settlType").val(),
            purpose: selectType.get("purpose").val(),
            wishPayday: processDate,
            payerAcc: $("#payerAcc").val(),
            payeeAccName: $("input[name=payeeAccName]").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeBank").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
            }
        });
    };



    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断支付结算是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断付款方式是否挂资金计划
            var isBudget = $("#isBudget").val();
            if (isBudget == "1") {
                $genOtherHtml();
            } else {
                $("#tagItemDetail").html("");
            }
            moneyType.get("payMoney").val(0);
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        renderBudget();
    }

    /**
     * 暂存提交后重新初始化数据
     */
    resetInit = function (data) {
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
        //重新加载收款账号
        payeeAccInit.get("payeeAccTemp").reload({
            corpId: $("input[name='corpId']").val(),
            curId: $('#curId').val(),
            voucherTypeId: ""
        });
        $('#payeeAcc').val("");
        $('#payeeAccTemp').val("");
        inputExternalCorpBankAcc.get('payeeAccName').val("");
        $('#payeeAccNameTemp').val("");
        $('#payeeBankCode').val("");
        $('#payeeBank').val("");

        // 初始化收款省市组的值
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: ""
        }, {
            name: 'city',
            defaultValue: ""
        }]);
        moneyType.get("payMoney").val(0);
        dicts.get("isForindividual").val(0);
        dicts.get("difBank").val(0);
        dicts.get("areaSign").val(0);
        dicts.get("isQuick").val(0);
        dicts.get("isPayed").val(0);
        dateType.get("wishPayday").val(bt.date.format(new Date()));

        $('#bursarBillNo').val("");
        $('#addWord').val("");

        // 重新业务类型
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + data.field.corpId;
        bt.selectType.get("busType").attr("data-url", busTypeUrl);
        // 重新设置共享业务类型
      //  var shareBusTypeUrl = basePath + "/netbank/nissharebustype/listValidShareBusTypeByBusType?busTypeId=";
      //  bt.selectType.get("shareBusTypeId").attr("data-url", shareBusTypeUrl);

        selectType.get("purpose").val(data.field.purpose);
        //转付，重新加载资金下拨账号
        if ($("#sysVoucherCode").val() == "02") {
            $("#downAcc").val("");
            downAccInit.get("downAcc").reload({
                corpId: data.field.corpId,
                curId: data.field.curId,
                account: data.field.payerAcc
            });
        }
        //代付，重新加载资金下拨账号
        if ($("#sysVoucherCode").val() == "33") {
            $("#realPayerAcc").val("");
            realPayerAccInit.get("realPayerAcc").reload({
                corpId: data.field.corpId,
                voucherTypeId: "",
                payerAcc: $("#payerAcc").val()
            });
        }
        renderBudget()
    }

});

/**
 * 是否已开票
 */
isOpenBillCallBack = function (data) {
    var $ = bt.jquery, form = bt.form();
    var isOpenBill = bt.dicts.get("isOpenBill").val();
    var showIsPayed = $("#showIsPayed").val();
    if (isOpenBill == "1") {
        $("#accebillInfoTitle").show();
        $("#accebillInfoButDiv").show();
        $("#poolBpTitle").hide();
        $("#poolBpSelectDiv").hide();
        $("#billMoneyTr").show();
        $("#billBusTr").hide();
        $("#drawBillFlag").hide();//出票标志隐藏
        var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
        if(allowDifferentBillMoney == "1"){
            $('input[data-name="payMoney"]').removeAttr("readonly");
        }else{
            $('input[data-name="payMoney"]').attr("readonly", "readonly");
        }
        bt.dicts.get("billType").setRequired(false);
        bt.dicts.get("billFlag").setRequired(false);
        if(showIsPayed == "1"){
            bt.dicts.get("isPayed").val("1");
        }
        $("#payerAcc").removeAttr("lay-verify");
        $("#payerAccName").removeAttr("lay-verify");
        $("#payerBank").removeAttr("lay-verify");
        var isPayed = bt.dicts.get("isPayed").val();
        /*if (isPayed == "1") {
            $("#accebillIntellectBut1").hide();
        }else{
            $("#accebillIntellectBut1").show();
        }*/
    } else {
        $("#accebillInfoTitle").hide();
        $("#accebillInfoButDiv").hide();
        /*$("#accebillIntellectBut1").hide();*/
        $("#poolBpTitle").show();
        $("#poolBpSelectDiv").show();
        $("#billMoneyTr").hide();
        $("#billBusTr").show();
        $("#drawBillFlag").show();//出票标志隐藏
        $('#billId').val("");
        $('input[data-name="payMoney"]').removeAttr("readonly");
        bt.dicts.get("billType").setRequired(true);
        bt.dicts.get("billFlag").setRequired(true);
        if(showIsPayed == "1"){
            bt.dicts.get("isPayed").val("0");
        }
        var bpId=bt.selectType.get("bpId").val();
        //判断是否选择票据池，选择则付款账号必填
        if(bpId){
            $("#payerAcc").attr("lay-verify", "required|tableData");
            $("#payerAccName").attr("lay-verify", "required");
            $("#payerBank").attr("lay-verify", "required");
        }else{
            $("#payerAcc").removeAttr("lay-verify");
            $("#payerAccName").removeAttr("lay-verify");
            $("#payerBank").removeAttr("lay-verify");
        }
    }
    form.renderIcon();
}

/**
 * 是否已付款回调
 */
isPayedCallBack = function (data) {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var isPayed = bt.dicts.get("isPayed").val();
    var preWishPayday = bt.dateType.get('wishPayday').val();
    if (isPayed == "1") {
        if ($("#sysVoucherCode").val() == 49) {
            bt.dicts.get("isOpenBill").val(1);
            isOpenBillCallBack();
        }else if ($("#sysVoucherCode").val() == 50){
            $("#accebillIntellectBut").hide();
        }
        bt.dateType.get('wishPayday').attr('data-min','1900-01-01');
    } else {
        var sysDate = bt.dateType.get('sysDate').val();
        bt.dateType.get('wishPayday').attr('data-min', sysDate);
        var days = dateFun.getBetweenDay(sysDate, preWishPayday);
        if(days<0){
            preWishPayday=sysDate;
        }
        if ($("#sysVoucherCode").val() == 49) {
            var isOpenBill = bt.dicts.get("isOpenBill").val();
            if(isOpenBill=="1"){
                //$("#accebillIntellectBut1").show();
            }
            //选择是否已付款为否时联动是否已出票为否
            bt.dicts.get("isOpenBill").val("0");
            isOpenBillCallBack();
        }else if ($("#sysVoucherCode").val() == 50){
            $("#accebillIntellectBut").show();
        }
    }
    //切换是否已付款时，把之前的票据信息清空
    $("#billId").val('');
    form.renderIcon();
    bt.dateType.get('wishPayday').val(preWishPayday);
}

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("payMoney").val(data);
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}

/**
 * 当选择票据池时，票据类型只能为银票，票据流通介质为电票
 */
function setAbPool(){
    form = bt.form();
    bt.dicts.get("billType").val("10");
    bt.dicts.get("billFlag").val("11");
    var bpId=bt.selectType.get("bpId").val();
    //判断是否选择票据池，选择则付款账号必填
    if(bpId){
        $("#payerAcc").attr("lay-verify", "required|tableData");
        $("#payerAccName").attr("lay-verify", "required");
        $("#payerBank").attr("lay-verify", "required");
    }else{
        $("#payerAcc").removeAttr("lay-verify");
        $("#payerAccName").removeAttr("lay-verify");
        $("#payerBank").removeAttr("lay-verify");
    }
    form.renderIcon();
}

function transmitParams(){
    var map = new Map();
    map.set("payeracc",$("#payerAcc").val());
    map.set("payer",$("#payerAccName").val());
    map.set("payerbankno",$("#payerBankCode").val());
    map.set("payerBankName",$("#payerBank").val());
    map.set("payeeBankName",$("#payeeBank").val());
    map.set("payeeAccName",$("#payeeAccName").val());
    return map;
}


function openBillPage() {
    var corpId = bt.corp.get("corpId").val();
    var payMoney = bt.moneyType.get("payMoney").val();
    var payeeacc = $("#payeeAcc").val();
    var payeeAccName = $("#payeeAccName").val();
    var payeebankno = $("#payeeBankCode").val();
    var payeeBankName = $("#payeeBank").val();
    var blRcvpymtpsUnnIdNo = $("#blRmtrUnnIdNo").val();
    var payeracc = $("#payerAcc").val();
    var payerbankno = $("#payerBankCode").val();
    var voucherTypeId = $("#voucherTypeId").val();
    var purpose = bt.selectType.get("purpose").val()
    if(!corpId || !payeeacc || !payeeAccName || !payeebankno || !payeeBankName){
        parent.layer.alert("请先填写收款人等信息", {icon: 2});
        return false;
    }
    if(!payMoney || payMoney<=0){
        parent.layer.alert("请先填写付款金额！", {icon: 2});
        return false;
    }
    parent.layer.open({
        type: 2,
        area: ['1200px','600px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/billList?corpId="+corpId+"&billMoney="+payMoney+"&payeracc="+payeracc+"&payeeacc="+payeeacc+"&payeebankno="+payeebankno
            +"&blRcvpymtpsUnnIdNo="+blRcvpymtpsUnnIdNo+"&payerbankno="+payerbankno+"&voucherTypeId="+voucherTypeId+"&rmk="+purpose,
    });
}

/**
 * 开票页面弹窗回调方法
 * @param id
 */
function openBillResult(openBillInfo){
    $("#openBillInfo").val(JSON.stringify(openBillInfo));
    bt.moneyType.get("payMoney").val(openBillInfo.billMoney);
}
