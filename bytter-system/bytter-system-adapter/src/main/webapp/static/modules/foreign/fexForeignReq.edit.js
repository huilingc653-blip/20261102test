var moneyType;
var moneyFun;
bt.use(['form', 'layedit', 'element', 'dateType', 'corp', 'selectTable', 'bank',
    'swiftInfo', 'moneyType', 'externalCorpBankAccFex', 'btUpload', 'dicts', 'selectType', 'inputExternalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form(), element = bt.element();
    element.on('tab(docDemoTabBrief)', function (elem) {
        initShow(elem.index);
    });

    /**
     * 汇款金额回调
     */
    payMoneyCallBack = function (data, $elem) {
        //离线账号
        if ($('#isOnline').val() == "0") {
            //计算购汇金额
            var payMoney = moneyType.get("payMoney").val();
            var foreignExrate = moneyType.get("foreignExrate").val();
            if (foreignExrate && payMoney) {
                var foreignMoney = moneyFun.mul(moneyFun.toNumber(payMoney), moneyFun.toNumber(foreignExrate));
                moneyType.get("foreignMoney").val(foreignMoney);
            }
        } else {
            moneyType.get("foreignExrate").val(0);
            moneyType.get("foreignMoney").val(0);
        }
    }
    function initPurpose() {
        var corpId = corp.get("corpId").val();
        var curId = selectType.get("curId").val();
        var countryId = $("#payerCountryId").val();
        var bankTypeId = selectType.get("bankTypeId").val();
        var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
            + "&curId=" + curId+ "&countryId=" + countryId+ "&bankTypeId=" + bankTypeId;
        bt.selectType.get("remitUse").attr("data-url", purposeUrl);
    }
    var corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    /**
     * 汇款方式回调
     */
    bt.remitMethodCallBack = function (data, $elem) {
        var remitMethod = dicts.get("remitMethod").val();
        /*var payeeType = "0,1,2,3,7";
        if(remitMethod == "2" || remitMethod == "3" || remitMethod == "4"){
            //当“汇款方式”为“2-实时电汇”、“3-实时特快转账CHATS”、“4-转数快FPS”
            payeeType = payeeType + ",4,5";
        } else if (remitMethod == "5"){
            //当“汇款方式”为“5-实时转账”
            payeeType = payeeType + ",4,5,6";
        }
        dicts.get("payeeType").attr({"data-displayData": payeeType});
        if(remitMethod == "2" || remitMethod == "3" || remitMethod == "5"){
            //当“汇款方式”为“2-实时电汇”、“3-实时特快转账CHATS”、“5-实时转账”
            dicts.get("payeeType").attr({"data-required": true});
        } else {
            // 农业银行、渣打银行印尼时必填
            var bankTypeName = $("#bankTypeName").val();
            var payerEnCode = $('#payerEnCode').val();
            if (bankTypeName == "农业银行" || (bankTypeName == "渣打银行" && payerEnCode == "ID")) {
                dicts.get("payeeType").attr({"data-required": true});
            } else {
                dicts.get("payeeType").attr({"data-required": false});
            }
        }*/
        setPayType();
        form.renderIcon();
        form.render();
    }

    /**
     * 付款类型
     */
    function setPayType() {
        var remitMethod = dicts.get("remitMethod").val();
        var payType = "1,2,3,4";
        var payerEnCode = $('#payerEnCode').val();
        if(payerEnCode == "MY" && (remitMethod == "2" || remitMethod == "5")){
            //当“汇款方式”为“2-实时电汇”、“5-实时转账”
            payType = payType + ",5,6,7,8";
        }
        dicts.get("payType").attr({"data-displayData": payType});
    }
    var dicts = bt.dicts.init();

    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }

    bt.bankPayTypeCallback = function(data) {
        var bankPayType = bt.selectType.get("bankPayType").val();
        var countryId = "CN";
        if ($('#payerIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#payerCountryId').val();
        }
        var bankTypeId = bt.selectType.get('bankTypeId').val();
        var bifCode = $('#payerBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, bankPayType, "payeeType");
        form.renderIcon();
    }
    var selectType = bt.selectType.init();
    bt.swiftInfo.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    initShow($("#remitType").val());

    function initShow(remitType) {
        if (remitType == "1") {
            // $("#payerNameEnTd1").show();
            // $("#payerNameEnTd2").show();
            // $("#payerNameEn").attr("lay-verify", "required");
            // $("#payerAddEnTd1").show();
            // $("#payerAddEnTd2").show();
            /*$("#remitNatureTd1").hide();
            $("#remitNatureTd2").hide();
            $("#remitNatureTd2").find('select').removeAttr('lay-verify');*/
            //境外汇款
            $("#remitType").val(1);
        } else {
            // $("#payerNameEnTd1").hide();
            // $("#payerNameEnTd2").hide();
            // $("#payerNameEn").removeAttr("lay-verify");
            // $("#payerAddEnTd1").hide();
            // $("#payerAddEnTd2").hide();
            /*$("#remitNatureTd1").show();
            $("#remitNatureTd2").show();
            $("#remitNatureTd2").find('select').attr("lay-verify", "required");*/
            //境内汇款
            $("#remitType").val(0);
        }

        //离线账号
        if ($('#isOnline').val() == "0") {
            $("#foreignMoneyTr").show();
        } else {
            $("#foreignMoneyTr").hide();
        }
        //兴业银行个性化
        var bankTypeName = $("#bankTypeName").val();
        setBranchCodeForXybank(bankTypeName);
    }

    /**
     * 计划明细控制项
     */
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
        if (dtlColSels["payeeAccName"]) {
            renderBudget();
        }
    });

    /**
     * 供应商查询
     * @param value
     */
    seachExternalCorpBankAcc = function (value) {
        value = encodeURI(value);
        var url = basePath + "/sys/externalCorp/listByInputExternalCorpFex?corpId=" + corp.get("corpId").val() + "&seach=" + value;
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
            $("#payeeBankCode").val(data.bankSourceCode);
            $("#payeeBank").val(data.bank);
            if (dtlColSels["payeeAccName"] || (data.bank && dtlColSels["payeeBank"])) {
                renderBudget();
            }
        }
    }
    var inputExternalCorpBankAcc = bt.inputExternalCorpBankAcc.init();
    var externalCorpBankAccFex = bt.externalCorpBankAccFex.init();
    bt.externalCorpBankAccFexParams = function () {
        externalCorpBankAccFex.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: "0"
        });
        return true;
    }

    //付款账号
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/foreign/fexforeignreq/getPayerAcc",
        urldata: {corpId: $("#accCorpId").val(), bankTypeId: $("#bankTypeIdDef").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#payerAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data: "curName",},
            {data: "isOnlineStr"}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payerAcc').val(data.bankAcc);
                $('#payerAccName').val(data.accName);
                $('#payerBank').val(data.bankName);
                $('#accId').val(data.id);
                $('#isOnline').val(data.isOnline);
                //离线账号
                if ($('#isOnline').val() == "0") {
                    $("#foreignMoneyTr").show();
                } else {
                    $("#foreignMoneyTr").hide();
                }
                //国家地区信息
                $('#payerCountryId').val(data.prov);
                $('#payerCountryNameZh').val(data.provName);
                $('#payerEnCode').val(data.countryEnCode);
                $('#payerBifCode').val(data.bifCode);
                //境内外账号标志
                $('#payerIsDomesticBank').val(data.isDomesticBank);
                // 重新设置用途
                initPurpose();
                //付款类型
                setPayType();
                //账户类别
                $('#accType').val(data.accType);
                $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));

                var corpId = corp.get("corpId").val();
                var curId = selectType.get("curId").val();
                //var countryId = $("#payerCountryId").val();
                var bankTypeId = selectType.get("bankTypeId").val();
                var purposeUrl = basePath + "/basic/business/btpurpose/getByListForFex?sysBusType=6&corpId=" + corpId
                    + "&curId=" + curId+ "&countryId=" + $("#payerCountryId").val()+ "&bankTypeId=" + bankTypeId;
                //农业银行：收款人类型、汇款去向、汇款用途改为必填字段
                if(data.bifCodeStr == "9009"){
                    bt.dicts.get('remitRoute').attr({'data-required': true});
                    bt.dicts.get('payeeType').attr({'data-required': true});
                    selectType.get('remitUse').attr({"data-url": purposeUrl,'data-required': true});
                } else {
                    bt.dicts.get('remitRoute').attr({'data-required': false});
                    bt.dicts.get('payeeType').attr({'data-required': false});
                    selectType.get('remitUse').attr({"data-url": purposeUrl,'data-required': false});
                }
                var countryId = "CN";
                if (data.isDomesticBank == "0") {
                    // 境外账户
                    countryId = data.prov;
                }
                //银行支付类型
                setBankTypeCustom(data.bankTypeId, data.bifCode, countryId, '', '');
                form.renderIcon();
                $.ajax({
                    url: basePath + "/bankacc/bisaccbal.do?getBal",
                    method: 'POST',
                    data: {bankAcc: data.bankAcc,curId: data.curId},
                    dataType: 'json',
                    success: function (data) {
                        moneyType.get("balance").val(data);
                        $("#balanceDiv").text(moneyFun.format(data));
                        form.render();
                    }
                });
                if (dtlColSels["payerAcc"] || (data.curId && dtlColSels["curId"])) {
                    renderBudget();
                }
                //1-境外汇款
                if ($("#remitType").val() == "1") {
                    //获取汇款人信息
                    $.ajax({
                        url: basePath + "/foreign/fexpayerinfo/getFexPayerInfo",
                        method: 'POST',
                        data: {payerAcc: $('#payerAcc').val()},
                        dataType: 'json',
                        success: function (data) {
                            if (data.obj) {
                                $("#payerNameEn").val(data.obj.payerNameEn);
                                $("#payerAddEn").val(data.obj.payerAddEn);
                            } else {
                                $("#payerNameEn").val("");
                                $("#payerAddEn").val("");
                            }
                            form.render();
                        }
                    });
                }
                //民生银行个性化
                setBranchCodeForMsbank(data.bankTypeName);
                //兴业银行个性化
                setBranchCodeForXybank(data.bankTypeName);
            } else {
                $('input.text').val('');
            }
        }
    });

    /**
     * 设置银行自定义信息
     * @param bankType 银行类型
     * @param bifCode 接口代码
     * @param countryId 国家地区
     * @param bankPayType 支付类型
     * @param initCustomType 初始化标识 默认-初始化全部 payeeType-只初始化收款人类型 other-只初始化其他信息
     */
    function setBankTypeCustom(bankType, bifCode, countryId, bankPayType, initCustomType) {
        // 重新支付类型
        var bankPayTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&customType=bankPayType";
        // 重新设置收款人类型
        var payeeTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&bankPayType=" + bankPayType + "&customType=payeeType";
        //获取银行类型信息
        $.ajax({
            url: basePath + "/basic/business/btbanktype/getBankType",
            method: 'POST',
            data: {bankTypeId: bankType},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (initCustomType != "payeeType") {
                        // 是否配置支付类型 为 1-是；支付类型应用范围 包含 4-外币汇款申请
                        if (data.isSetBankPayType == "1" && data.applayScopeBankPayType.indexOf("4") != -1) {
                            if (data.requiredBankPayType == "1") {
                                selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": true});
                            } else {
                                selectType.get('bankPayType').attr({
                                    "data-url": bankPayTypeUrl,
                                    "data-required": false
                                });
                            }
                            $("#bankPayTypeTr").show();
                        } else {
                            selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": false});
                            $("#bankPayTypeTr").hide();
                        }
                    }

                    // 是否配置收款人类型 为 1-是；收款人类型应用范围 包含 4-外币汇款申请
                    if (data.isSetPayeeType == "1" && data.applayScopePayeeType.indexOf("4") != -1) {
                        if (data.requiredPayeeType == "1") {
                            selectType.get('payeeType').attr({"data-url":payeeTypeUrl,"data-required":true});
                        } else {
                            selectType.get('payeeType').attr({"data-url":payeeTypeUrl,"data-required":false});
                        }
                        $(".payeeTypeDiv").show();
                        $("#payeeTypeTd").html(bt.lang('table.fexForeignReq.payeeType'));
                    } else {
                        selectType.get('payeeType').attr({"data-url":payeeTypeUrl,"data-required":false});
                        $(".payeeTypeDiv").hide();
                        $("#payeeTypeTd").html('');
                    }
                } else {
                    if (initCustomType != "payeeType") {
                        selectType.get('bankPayType').attr({"data-url": bankPayTypeUrl, "data-required": false});
                        $("#bankPayTypeTr").hide();
                    }

                    selectType.get('payeeType').attr({"data-url":payeeTypeUrl,"data-required":false});
                    $(".payeeTypeDiv").hide();
                    $("#payeeTypeTd").html('');
                }
                form.renderIcon();
            }
        });
        form.render();
    }

    //民生银行个性化
    function setBranchCodeForMsbank(bankTypeName) {
        if (bankTypeName == "民生银行") {
            $('#payerBankSpan').html(bt.lang('table.fexForeignReq.branchCode'));
            $('#branchDiv').show();
            $('#payerBankDiv').hide();
        } else {
            $('#payerBankSpan').html(bt.lang('table.fexForeignReq.payerBank'));
            $('#branchDiv').hide();
            $('#payerBankDiv').show();
        }
        form.renderIcon();
    }

    //兴业银行个性化
    function setBranchCodeForXybank(bankTypeName) {
        if (bankTypeName == "兴业银行") {
            $('#xyBankTr').show();
            //$("#declareNo").attr("lay-verify","required");
            bt.dateType.get('arrivalDate').attr({'data-required': true});
            $("#payerAddEn").attr("lay-verify","required");
            //	中国大陆账户跨境汇款
            var remitType = $("#remitType").val();
            if (remitType == "1") {
                $("#addWord").attr("lay-verify", "required|langKey");
                //汇款人名称、收款人名称、收款人开户行名称 限制英文或数字
                $("#payerNameEn").attr("lay-verify", "required|langKey");
                $("#payeeAccName").attr("lay-verify", "required|langKey");
                $("#payeeBank").attr("lay-verify", "required|langKey");
                $("#payerAddEn").attr("lay-verify", "required|langKey");
            } else {
                $("#addWord").removeAttr('lay-verify');
                //汇款人名称、收款人名称、收款人开户行名称 解除英文或数字的限制
                $("#payerNameEn").attr("lay-verify","required");
                $("#payeeAccName").attr("lay-verify","required");
                $("#payeeBank").attr("lay-verify","required");
                $("#payerAddEn").attr("lay-verify", "");
            }
        } else {
            $('#xyBankTr').hide();
            //$("#declareNo").removeAttr('lay-verify');
            bt.dateType.get('arrivalDate').attr({'data-required': false});
            $("#payerAddEn").removeAttr('lay-verify');
            $("#addWord").removeAttr('lay-verify');
            //汇款人名称、收款人名称、收款人开户行名称 解除英文或数字的限制
            $("#payerNameEn").attr("lay-verify","required");
            $("#payeeAccName").attr("lay-verify","required");
            $("#payeeBank").attr("lay-verify","required");
            $("#payerAddEn").attr("lay-verify", "");
        }
        form.renderIcon();
    }

    //办理机构
    var branchCodeInit = bt.selectTable.init({
        elem: "#branchCodeTable",
        url: basePath + "/basic/business/bisBankBranch/listAll",
        searchInput: "#branchCode",
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "branchCode",},
            {data: "branchName",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#branchCode').val(data.branchCode);
                $('#branchName').val(data.branchName);
            } else {
                $('input.text').val('');
            }
        }
    });

    //收款国别
    var payeeCountryCodeInit = bt.selectTable.init({
        elem: "#payeeCountryCodeTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#payeeCountryName",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payeeCountryName').val(data.nameZh);
                $('#payeeCountryCode').val(data.id);
            } else {
                $('input.text').val('');
            }
        }
    });

    //汇款联系人信息
    var payerNameInit = bt.selectTable.init({
        elem: "#payerNameTable",
        url: basePath + "/foreign/fexlinkmaninfo/listByCorpId",
        urldata: {corpId: $("#corpIdDef").val()},
        searchInput: "#payerName",
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "payerName",},
            {data: "payerTel",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#payerName').val(data.payerName);
                $('#payerTel').val(data.payerTel);
            } else {
                $('input.text').val('');
            }
        }
    });

    //交易编码
    var tranCodeInit1 = bt.selectTable.init({
        elem: "#tranCodeTable1",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName1').val(data.tranName);
                $('#tranCode1').val(data.tranCode);
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: '1'},
                    dataType: 'json',
                    success: function (data) {
                        if (data.obj) {
                            $('#tranAddWord1').val(data.obj);
                        } else {
                            $('#tranAddWord1').val('');
                        }
                        form.render();
                    }
                });
            } else {
                $('input.text').val('');
                $('#tranAddWord1').val('');
            }
        }
    });

    var tranCodeInit2 = bt.selectTable.init({
        elem: "#tranCodeTable2",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName2",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName2').val(data.tranName);
                $('#tranCode2').val(data.tranCode);
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: '1'},
                    dataType: 'json',
                    success: function (data) {
                        if (data.obj) {
                            $('#tranAddWord2').val(data.obj);
                        } else {
                            $('#tranAddWord2').val('');
                        }
                        form.render();
                    }
                });
            } else {
                $('input.text').val('');
                $('#tranAddWord2').val('');
            }
        }
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
    };

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        bankAccLimit(data);
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
                rate = bt.money.mul(data.field.payMoney,da,2)
            }
        });
        rate = parseFloat(rate);

        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:0,id:data.field.id,wishPayDate: data.field.payDate},
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
            msg += '<br<br>'+bt.lang('billPool.sign.isContinue')
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
            data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:0,id:data.field.id,wishPayDate: data.field.payDate},
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
        //校验交易编码
        var tranCode1 = $('#tranCode1').val();
        var tranCode2 = $('#tranCode2').val();
        if (tranCode2 != "") {
            //交易编码1和交易编码2不允许相同
            if (tranCode1 == tranCode2) {
                bt.loading.hide();
                layer.alert(bt.lang("fex.foreign.message.trancode.same"), {icon: 2});
                return false;
            }
            //相应金额2不能为空
            var tranAmt1 = moneyType.get("tranAmt1").val();
            if (tranAmt1 == null) {
                bt.loading.hide();
                layer.alert(bt.lang("fex.foreign.message.tranamt2.null"), {icon: 2});
                return false;
            }
            //交易附言2不能为空
            var tranAddWord2 = $('#tranAddWord2').val();
            if (!tranAddWord2) {
                bt.loading.hide();
                layer.alert(bt.lang("fex.foreign.message.tranaddword2.null"), {icon: 2});
                return false;
            }
        }
        //证书签名
        var signDataMsg = signData("外币汇款提交", 11);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        var action = "/foreign/fexforeignreq/modify";
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
    }

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        $("#payeeAcc").val(data.externalAcc);
        inputExternalCorpBankAcc.get('payeeAccName').val(data.name);
        $("#payeeSwiftCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
        $("#payeeAdd").val(data.address);
        $("#payeeBankAdd").val(data.accAddress);
        $("#payeeCountryCode").val(data.accProvinceFex);
        payeeCountryCodeInit.get("payeeCountryName").val(data.accProvinceFexName);
        if (dtlColSels["payeeAcc"] || (data.name && dtlColSels["payeeAccName"]) || (data.bank && dtlColSels["payeeBank"])) {
            renderBudget();
        }
    };

    //swiftcode选择
    bt.swiftInfoCallback = function (data, dom) {
        $("#payeeSwiftCode").val(data.swiftCode);
        $("#payeeBank").val(data.bankName);
        $("#payeeBankAdd").val(data.bankAdd);
        $("#payeeCountryCode").val(data.countryCode);
        payeeCountryCodeInit.get("payeeCountryName").val(data.nameZh);
        if (dtlColSels["payeeBank"]) {
            renderBudget();
        }
    };
    //swiftcode选择
    bt.payeeAgentSwiftInfoCallback = function (data, dom) {
        $("#payeeAgentSwiftCode").val(data.swiftCode);
        $("#payeeAgentBank").val(data.bankName);
    };
    //银行类别回调函数
    bt.bankTypeCallback = function (data) {
        //重新加载汇款账号
        payerAccInit.get("payerAcc").reload({
            corpId: $("#accCorpId").val(),
            bankTypeId: selectType.get("bankTypeId").val()
        });
        //重新加载汇款联系人
        payerNameInit.get("payerName").reload({corpId: corp.get("corpId").val()});
        var remitMethod = "0,1";
        if (data.remitmethod) {
            remitMethod = remitMethod + "," + data.remitmethod;
        }
        dicts.get("remitMethod").attr({"data-displayData": remitMethod});
        form.render();
        // 重新设置用途
        initPurpose();
        //民生银行个性化
        setBranchCodeForMsbank(data.typename);
        //兴业银行个性化
        setBranchCodeForXybank(data.typename);
    };

    //汇款币种回调函数
    bt.curIdCallback = function (data) {
        $("#tranCurId1").val(data.id);
        $("#tranCurId2").val(data.id);
        $("#tranCurName1").val(data.curname);
        $("#tranCurName2").val(data.curname);
        renderBudget();
        form.render();
        // 重新设置用途
        initPurpose();
    };

    var selectType = bt.selectType.init();

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
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
            processDate: dateType.get("payDate").val(),
            type: 'select',
            detailControl: true,
            curId: $("#curIdDef").val(),
            purpose: $("#addWord").val(),
            wishPayday: dateType.get("payDate").val(),
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
    renderBudget();

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            $genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }

    /**
     * 暂存提交后重新初始化数据
     */
    resetInit = function (data) {
        $('#payeeAcc').val("");
        inputExternalCorpBankAcc.get('payeeAccName').val("");
        $('#payeeBankCode').val("");
        $('#payeeBank').val("");
        moneyType.get("payMoney").val(0);
        dateType.get("payDate").val(bt.date.format(new Date()));
        $('#addWord').val("");
        renderBudget();
    }
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    if (data != 0) {
        moneyType.get("payMoney").val(data);
        var payMoney = moneyType.get("payMoney").val();
        var foreignExrate = moneyType.get("foreignExrate").val();
        if (foreignExrate && payMoney) {
            var foreignMoney = moneyFun.mul(moneyFun.toNumber(payMoney), moneyFun.toNumber(foreignExrate));
            moneyType.get("foreignMoney").val(foreignMoney);
        }
    }
}
