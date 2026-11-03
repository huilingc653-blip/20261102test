bt.use(['form', 'layedit', 'select', 'dateType', 'corp', 'selectTable', 'dateForm', 'bank',
    'bankInfo', 'moneyType', 'region', 'externalCorpBankAcc', 'accebillInfo', 'accebillView',
    'btUpload', 'dicts', 'selectType','inputExternalCorpBankAcc', 'rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dicts = bt.dicts.init();
    bt.rateType.init();

    /** 计划明细控制项 zuoy 2018-11-19**/
    var dtlColSels = [];
    if ($("#dtlColSel").val()) {
        bt.each($("#dtlColSel").val().split(","), function (i, col) {
            dtlColSels[col] = col;
        });
    }
    /**
     * 收款账户值改变后回调
     */
    $("#payeeAccName").change(function () {
        if(dtlColSels["payeeAccName"]){
            renderBudget();
        }
    });

    /**
     * 收款账户值改变后回调
     */
    $("#payeeAccNameTemp").change(function () {
        if(dtlColSels["payeeAccName"]){
            renderBudget();
        }
    });

    /**
     * 用途回调
     */
    purposeCallBack = function(){
        if(dtlColSels["purpose"]){
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
        if(value){
            var data = value.data;
            $("#payeeAcc").val(data.externalAcc);
            value.elem.val(data.name);
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
            if(dtlColSels["payeeAccName"] ||  (data.bank && dtlColSels["payeeBank"])){
                renderBudget();
            }
        }
    }
    var inputExternalCorpBankAcc = bt.inputExternalCorpBankAcc.init();

    //业务类别选择事件
    busTypeCallback = function (data) {
        $("#defItemCode").val(data.itemcode);
        renderBudget();
    }

    var selectType = bt.selectType.init();

    $.ajax({
        url: basePath + "/netbank/nisbustype?getNisBusType",
        method: 'POST',
        data: {id: $("#busTypeDef").val()},
        dataType: 'json',
        async: true,
        success: function (result) {
            if (result.obj) {
                $("#defItemCode").val(result.obj.itemCode);
            }
        }
    });

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
        gid: $("#attachmentId").val()
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
        //if ($("#sysVoucherCode").val() == "49" || $("#sysVoucherCode").val() == "50" || $("#sysVoucherCode").val() == "34") {
            externalCorpBankAcc.set({
                "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : $("#corpIdDef").val(),
                type: "2"
            });
        /*} else {
            externalCorpBankAcc.set({
                "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : $("#corpIdDef").val(),
                type: "0"
            });
        }*/
        return true;
    }

    var accebillInfo = bt.accebillInfo.init();
    var accebillView = bt.accebillView.init();
    bt.accebillInfoParams = function () {
        var isEmiBill=0;
        if ($("#sysVoucherCode").val() == "49"){
            isEmiBill=1;
        }
        var showIsPayed = $("#showIsPayed").val();
        if(showIsPayed == "1"){
            accebillInfo.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": isEmiBill,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "isPayed":bt.dicts.get("isPayed").val(),//是否已付款
                "payee":$("#payeeAccName").val(),//收款人名称
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "oldIds": $("#oldBillId").val()
            });
        }else{
            accebillInfo.set({
                "corpId": $("#accCorpId").val(),
                "billDate": dateType.get("wishPayday").val(),
                "isEmitBill": isEmiBill,
                "billType":bt.dicts.get("billType").val(),
                "billFlag":bt.dicts.get("billFlag").val(),
                "payee":$("#payeeAccName").val(),//收款人名称
                "endorse":$("#payeeAccName").val(),//收款人名称(应收票据对应-后手)
                "ids": $("#billId").val(),
                "oldIds": $("#oldBillId").val()
            });
        }
        return true;
    }
    bt.accebillViewParams = function () {
        accebillView.set({"id": $("#billId").val()});
        return true;
    }
    bt.accebillInfoCallback = function (datas, dom) {
        var billIds = "";
        $.each(datas.data, function (index, data) {
            if (billIds == "") {
                billIds += data.id;
            } else {
                billIds += "," + data.id;
            }
        })
        $("#billId").val(billIds);
        bt.moneyType.get("billMoney").val(datas.sumMoney);
        //付款金额允许修改或者选票金额等于当前付款金额时才回传
        if($("#editPayMoney").val() == "1"){
            bt.moneyType.get("payMoney").val(datas.sumMoney);
        } else {
            var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
            if(allowDifferentBillMoney != "1" && datas.sumMoney != bt.moneyType.get("payMoney").val()){
                $("#billId").val("");
                bt.moneyType.get("billMoney").val(0);
                layer.alert(bt.lang("nis.netbank.message.billmoney.mismatch"), {icon: 2});
            }
        }
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
        url: basePath + "/netbank/nisbillheadPool/getPayerAcc",
        urldata: {corpId: $("#corpIdDef").val(), voucherTypeId: $("select[name='voucherTypeId']").val() ? $("select[name='voucherTypeId']").val() : $("#voucherTypeIdDef").val()},
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
                            form.render();
                        }
                    });
                } else {
                    //内部账号
                    moneyType.get("balance").val(data.balance);
                    $("#balanceDiv").text(moneyFun.format(data.balance));
                }

                //获取账户余额包含在途金额
                $.ajax({
                    url: basePath + "/netbank/nisbillhead?getAccountBal",
                    method: 'POST',
                    data: {account: data.bankAcc,curId: data.curId,isAccount:isAccount},
                    dataType: 'json',
                    success: function (data) {
                        moneyType.get("availBal").val(data);
                        $("#availBalDiv").text(moneyFun.format(data));
                        form.render();
                    }
                });
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
                        corpId: $("#corpIdDef").val(),
                        curId: $('#curId').val(),
                        account: $("#payerAcc").val()
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
        url: basePath + "/netbank/nisbillheadPool/getPayeeAcc",
        urldata: {
            corpId: $('#corpIdDef').val(),
            curId: $('#curId').val(),
            voucherTypeId: $('#voucherTypeIdDef').val()
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
                if (dtlColSels["payeeAcc"]) {
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
        url: basePath + "/netbank/nisbillheadPool/getDownAcc",
        urldata: {corpId: $("#corpIdDef").val(), curId: $('#curId').val(), account: $("#payerAcc").val()},
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


    /**
     * 跨行标志，地域标志判断
     */
    function difBankAndAreaSign() {
        if($("#payerBankCode").val() == "" || $("#payeeBankCode").val() == ""){
            dicts.get("difBank").val(0);
        }else if ($("#payerBankCode").val().substr(0, 3) == $("#payeeBankCode").val().substr(0, 3)) {
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
        if($("#payerProv").val() == "" || $("#payeeProv").val() == ""){
            dicts.get("areaSign").val(0);
        }else if ($("#payerProv").val() == $("#payeeProv").val() && $("#payerCity").val() == $("#payeeCity").val()) {
            dicts.get("areaSign").val(0);
        } else {
            dicts.get("areaSign").val(1);
        }
        form.render('radio');
    }

    //付款方式选择监听
    form.on('select(voucherTypeId)', function (data) {
        //获取付款方式信息
        voucerTypeCallback(data.value);
    });

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
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        //账户余额校验
        var isAccount = $("#isAccount").val();//是否记账：0-否 1-是
        var accountOver = $("#accountOver").val();//内部账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        var balControlType = $("#balControlType").val();//付款余额控制类型：0-账户余额 1-可付款余额
        var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
        if(balControlType == "1"){
            balance = moneyFun.toNumber(moneyType.get("availBal").val());//可用余额
        }
        //付款金额大于账户余额
        var flag = false;
        if (payMoney > balance) {
            if (isAccount == "0") {
                if (bankaccOver == "1") {
                    flag = true;
                } else if (bankaccOver == "2") {
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
        if (flag) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function (index) {
                bt.loading.show();
                rePay(data);
            });
        } else {
            rePay(data);
        }
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        //校验白名单
        corpCheck(data);
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
            url: basePath + "/netbank/nisbillheadPool/standardRate",
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
            url: basePath + "/netbank/nisbillheadPool/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:0,id:data.field.id,wishPayDate:data.field.wishPayday},
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
            url: basePath + "/netbank/nisbillheadPool/limit",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:0,id:data.field.id,wishPayDate:data.field.wishPayday},
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
        //只有erp数据金额不允许更改时，才判断票据金额
        var editPayMoney = $("#editPayMoney").val();
        if(editPayMoney == "0"){
            var payMoneyShowVal = moneyFun.toNumber($("#payMoneyShow").val());
            //判断金额与票据金额是否相等
            if ((sysVoucherCode == "49" && isOpenBill=="1") || sysVoucherCode == "50") {
                bt.loading.hide();
                if(payMoney != payMoneyShowVal){
                    layer.alert(bt.lang("nis.netbank.message.billmoney.mismatch"), {icon: 2});
                    return false;
                }
            }
        }
        //证书签名
        var signDataMsg = signCaData();
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }

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
                bt.loading.hide();
                layer.confirm(bt.lang("nis.netbank.message.billmoney.mismatch.confirm"), {icon:3},function () {
                    submitTemp(data);
                });
            }else {
                submitTemp(data);
            }
        }
    }

    //封装
    function submitTemp(data) {
        var itemCodeCtl = checkCtl();
        if (itemCodeCtl.length) {
            bt.loading.hide();
            if(!dtlColSels.length){
                parent.layer.confirm(bt.lang('fbs.message.detailmoney.gt.detailbalance.notctl', [itemCodeCtl]),
                    {
                        btn: [bt.lang('common.sc_yn.yes'), bt.lang('common.sc_yn.no')],
                        icon: 0,
                    }, function () {
                        bt.loading.show();
                        checkBalance(data);
                    });
            }else {
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

    function submitForm() {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            if($("#isCFCACheck").val() != ""){
                bt.loading.hide();
                layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                return false;
            }
            bt.loading.show();
            //如果付款方式控制不可编辑，并且原始付款方式不为空，则付款方式控制不变（白云机场）
            if($("#editVoucherTypeId").val() == "0" && $("#voucherTypeIdDef").val() != ""){
                $("select[name='voucherTypeId']").val($("#voucherTypeIdDef").val());
            }
            var action = "/netbank/nisbillheadPool/modify";
            $.post(basePath + action, $('#inputForm').serialize(), function (result) {
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
    }

    //重复付款校验
    function rePay(data) {
        var result = false;
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool?rePay",
            method: 'POST',
            async: false,
            data: {
                corpId: data.field.corpId,
                payMoney: data.field.payMoney,
                payeeAcc: data.field.payeeAcc,
                curId: data.field.curId,
                id: data.field.id
            },
            dataType: 'json',
            success: function (data) {
                result = data.obj;
            }
        });
        if (result) {
            bt.loading.hide();
            parent.layer.confirm(bt.lang('netbank.message.confirm.repay'), function (index) {
                submitForm();
            });
        } else {
            submitForm();
        }
        return false;
    }

    //证书签名
    function signCaData() {
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
        return signData(sourceData, "2");
    }

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") {
            // 选择客商
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
            if(dtlColSels["payeeAcc"] || (data.name && dtlColSels["payeeAccName"]) || (data.bank && dtlColSels["payeeBank"])){
                renderBudget();
            }
        } else {
            // 选择单位
            $("#payeeAcc").val(data.bankAcc);
            $("#payeeAccName").val(data.accName);
            $("#payeeBankCode").val(data.bankCode);
            $("#payeeBank").val(data.bankName);
            $("#bankTypeId").val(data.bankTypeId);
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
            if(dtlColSels["payeeAcc"] || (data.corpName && dtlColSels["payeeAccName"]) || (data.bankName && dtlColSels["payeeBank"])){
                renderBudget();
            }
        }
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
        if(dtlColSels["payeeBank"]){
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
        //获取单位信息
        $.ajax({
            url: basePath + "/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    if (data.obj.userAccountCode) {
                        $("#accCorpId").val(data.obj.userAccountCode);
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
                        $("select[name='voucherTypeId']").val(item.defVoucherCode);
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
                    renderBudget();
                }
                form.render();
            }
        });
        // 重新设置用途
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + id;
        bt.selectType.get("busType").attr("data-url", busTypeUrl);

        // 重新设置用途
        var purposeUrl = basePath + "/basic/business/btpurpose?getByList&sysBusType=1&corpId=" + id;
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    };
    //初始化单位
    var corp = bt.corp.init();

    //付款方式选择事件
    function voucerTypeCallback(voucherTypeId, isInit) {
        if (voucherTypeId != "") {
            $.ajax({
                url: basePath + "/netbank/fcvouchertype?getFcVoucherType",
                method: 'POST',
                data: {id: voucherTypeId},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        $("#sysVoucherCode").val(data.obj.sysVoucherCode);
                        $("#isAccount").val(data.obj.isAccount);
                        $("#isBudget").val(data.obj.isBudget);
                        $("#settlType").val(data.obj.settlType);
                        show();
                        if(!isInit){
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
                                urldata: {corpId: $("#corpIdDef").val(), voucherTypeId: $("select[name='voucherTypeId']").val()},
                                columns: payerAccCol
                            });
                            //重新加载收款账号
                            payeeAccInit.get("payeeAccTemp").reload({
                                corpId: $("input[name='corpId']").val(),
                                curId: $('#curId').val(),
                                voucherTypeId: $("select[name='voucherTypeId']").val()
                            });
                            //转付，重新加载资金下拨账号
                            if ($("#sysVoucherCode").val() == "02") {
                                $("#downAcc").val("");
                                downAccInit.get("downAcc").reload({
                                    corpId: $("#corpIdDef").val(),
                                    curId: $('#curId').val(),
                                    account: $("#payerAcc").val()
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
                        }
                        //判断跨行标志和地域标志
                        difBankAndAreaSign();
                        //资金计划
                        renderBudget();
                    }
                }
            });
        }
    };

    function show() {
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
            //$("#payeeAcc").val("");
            //$("#payeeBankCode").val("");
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
                //是否已开发票参数为否时，票据类型和流通介质字段需为必填项
                var isOpenBill = dicts.get("isOpenBill").val();
                if(isOpenBill == 0){
                    dicts.get("billType").setRequired(true);
                    dicts.get("billFlag").setRequired(true);
                }else{
                    dicts.get("billType").setRequired(false);
                    dicts.get("billFlag").setRequired(false);
                }
            } else {
                $("#billTd1").hide();//选票隐藏
            }
            var isOpenBill = bt.dicts.get("isOpenBill").val();
            if(sysVoucherCode == "50" || (sysVoucherCode == "49" && isOpenBill == "1")){
                $("#billMoneyTr").show();
                if ($("#editPayMoney").val() == "0") {
                    $('input[data-name="payMoney"]').attr("readonly", "readonly");
                } else {
                    var allowDifferentBillMoney = $("#allowDifferentBillMoney").val();
                    if(allowDifferentBillMoney == "1"){
                        $('input[data-name="payMoney"]').removeAttr("readonly");
                    }else{
                        $('input[data-name="payMoney"]').attr("readonly", "readonly");
                    }
                }
            }else{
                $("#billMoneyTr").hide();
                if ($("#editPayMoney").val() == "0") {
                    $('input[data-name="payMoney"]').attr("readonly", "readonly");
                } else {
                    $('input[data-name="payMoney"]').removeAttr("readonly");
                }
            }
        } else {
            $("#payerAcc").attr("lay-verify", "required|tableData");
            $("#payerAccName").attr("lay-verify", "required");
            $("#payerBank").attr("lay-verify", "required");
            $('input[data-name="payMoney"]').removeAttr("readonly");
            $("#billTd").hide();//选票隐藏
            $("#billTd1").hide();//选票隐藏
            $("#billTd2").hide();//票据类型,票据流通介质隐藏
            //清除票据类型和票据流通介质的值
            dicts.get("billType").val("");
            dicts.get("billFlag").val("");
            $("#billMoneyTr").hide();
        }
        form.renderIcon();

    };

    //初始化页面
    var id = $("#corpIdDef").val();
    //获取单位分配的付款方式
    $.ajax({
        url: basePath + "/netbank/btassignpayway?getAssignPayWayAll",
        method: 'POST',
        data: {corpId: id},
        dataType: 'json',
        async: true,
        success: function (data) {
            $("#voucherTypeId").empty();
            if (data && data.obj && data.obj.length > 0) {
                $.each(data.obj, function (index, item) {
                    $("#voucherTypeId").append("<option value=" + item.voucherCode + ">" + item.voucherName + "</option>");
                    $("select[name='voucherTypeId']").val($("#voucherTypeIdDef").val());
                });
                //获取付款方式信息
                var voucherTypeId = $("select[name='voucherTypeId']").val();
                voucerTypeCallback(voucherTypeId, true);
                $("#payeeAccTemp").val($("#payeeAcc").val());
            } else {
                $("#voucherTypeId").append("<option value=''></option>");
            }
            form.render();
        }
    });

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function (busType) {
        var corpId = $("#corpIdDef").val();
        if ($('input[name="corpId"]').val()) {
            corpId = $('input[name="corpId"]').val();
        }
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode: $("#defItemCode").val(),
            processDate: dateType.get("wishPayday").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curId").val(),
            settlType: $("#settlType").val(),
            purpose: selectType.get("purpose").val(),
            wishPayday: dateType.get("wishPayday").val(),
            payerAcc: $("#payerAcc").val(),
            payeeAccName: $("input[name=payeeAccName]").val(),
            payeeAcc: $("#payeeAcc").val(),
            payeeBank: $("#payeeBank").val(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url,
            method: 'POST',
            data: param,
            async: true,
            success: function (json) {
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
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }

    /**已开票初始化*/
    var isOpenBill = $("#isOpenBillDef").val();
    if (isOpenBill == "1") {
        $("#accebillInfoTitle").show();
        $("#accebillInfoButDiv").show();
        $("#billMoneyTr").show();
        $('input[data-name="payMoney"]').attr("readonly", "readonly");
    } else {
        $("#accebillInfoTitle").hide();
        $("#accebillInfoButDiv").hide();
        $("#billMoneyTr").hide();
        $('input[data-name="payMoney"]').removeAttr("readonly");
    }
    /**已开票end*/
});

/**
 * 是否已开票
 */
isOpenBillCallBack = function (data) {
    var isOpenBill = bt.dicts.get("isOpenBill").val();
    var showIsPayed = $("#showIsPayed").val();
    if (isOpenBill == "1") {
        $("#accebillInfoTitle").show();
        $("#accebillInfoButDiv").show();
        $("#billMoneyTr").show();
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
    } else {
        $("#accebillInfoTitle").hide();
        $("#accebillInfoButDiv").hide();
        $("#billMoneyTr").hide();
        $('input[data-name="payMoney"]').removeAttr("readonly");
        bt.dicts.get("billType").setRequired(true);
        bt.dicts.get("billFlag").setRequired(true);
        if(showIsPayed == "1"){
            bt.dicts.get("isPayed").val("0");
        }
    }
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
        bt.dateType.get('wishPayday').attr('data-min','1900-01-01');
    } else {
        var sysDate = bt.dateType.get('sysDate').val();
        bt.dateType.get('wishPayday').attr('data-min', sysDate);
        var days = dateFun.getBetweenDay(sysDate, preWishPayday);
        if(days<0){
            preWishPayday=sysDate;
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
    if ($("#editPayMoney").val() == "1") {
        bt.moneyType.get("payMoney").val(data);
    }
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}