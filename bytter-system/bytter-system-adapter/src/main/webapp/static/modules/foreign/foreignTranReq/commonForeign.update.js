var selectType,dicts,moneyType,moneyFun,dateType,dateFun,form;

/** 设置必填时保留 data-maxlength 对应的 strlength */
function setLayVerifyWithStrlength($el, isRequired, baseVerify) {
    if (!$el || !$el.length) return;
    var hasMaxLen = $el.attr('data-maxlength') || $el.data('maxlength');
    if (isRequired) {
        var verify = baseVerify || 'required';
        if (hasMaxLen && (verify + '').indexOf('strlength') === -1) {
            verify = verify + '|strlength';
        }
        $el.attr('lay-verify', verify);
    } else if (hasMaxLen) {
        $el.attr('lay-verify', 'strlength');
    } else {
        $el.removeAttr('lay-verify');
    }
}

bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','ratioType','element', 'selectTable','dateType','corp',
    'btUpload','checkboxs','allBankInfo'], function(){
    var $ = bt.jquery;
    form = bt.form();

    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    bt.rateType.init();
    bt.ratioType.init();

    // 交易货币对回调
    bt.currencyPairCallback = function(data){
        setCur();
        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }

    bt.profitLossCurCallback = function(data){
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var tranRateType = bt.dicts.get('tranRateType').val();
        var corpBaseCur = $("#corpBaseCur").val();
        var pro = selectType.get("proId").getData().obj;

        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == pro.isAutoDelivery && corpBaseCur != data.id) {
            $(".baseConvertTr").show();
            $("#isShowConvert").val("Y");
            $("input[data-id='convertRate']").attr("lay-verify", "required|number2");
            $("input[data-id='convertMoney']").attr("lay-verify", "required|number2");
        } else {
            $(".baseConvertTr").hide();
            $("#isShowConvert").val("N");
            $("input[data-id='convertRate']").removeAttr("lay-verify");
            $("input[data-id='convertMoney']").removeAttr("lay-verify");

            $("#convertCurType").val("");
            moneyType.get('convertRate').val("");
            moneyType.get('convertMoney').val("");
        }
        getBaseCuQuotationMethod();
        // 计算损益
        calculateFexProfitLoss();
    }

    // 损益回调
    bt.profitLossMoneyCallback = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney();
    }

    // 折本位币汇率回调
    bt.convertRateCallback = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney();
    }

    // 设置损益表单
    function setProfitLoss(){
        if ("spot" != $("#type").val()) {
            return;
        }
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType').val();
        var corpBaseCur = $("#corpBaseCur").val();
        var profitLossCur = selectType.get("profitLossCur").getData().obj;
        if (!tranRateType) {
            return;
        }
        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery) {
            $("#spotTr").show();
            /*$("input[data-id='profitLossMoney']").attr("lay-verify", "required");
            selectType.get("profitLossCur").attr({"data-required":"true"});*/
        } else {
            $("#spotTr").hide();
            $("input[data-id='profitLossMoney']").removeAttr("lay-verify");
            selectType.get("profitLossCur").setAttr({"data-required":false});
        }
        if (!profitLossCur) {
            return;
        }
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery){
            $("#profitLossTr").show();
        } else {
            $("#profitLossTr").hide();
        }
        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery && corpBaseCur != profitLossCur.id) {
            $(".baseConvertTr").show();
            $("input[data-id='convertRate']").attr("lay-verify", "required|number2");
            $("input[data-id='convertMoney']").attr("lay-verify", "required|number2");
        } else {
            $(".baseConvertTr").hide();
            $("input[data-id='convertRate']").removeAttr("lay-verify");
            $("input[data-id='convertMoney']").removeAttr("lay-verify");
        }

        getBaseCuQuotationMethod();

        // 计算损益
        calculateFexProfitLoss();
        form.renderIcon();
    }

    bt.busDirectionCallback = function(){
        var data = selectType.get("tranCurPairId").getData().obj;
        var busDirection = dicts.get("busDirection").val();
        if(data){
            var curId1 = data.baseCurId;
            var curName1 = data.baseEnglishCode;
            var curId2 = data.curId2;
            var curName2 = data.englishCode2;
            if(curId1==curId2){
                curId2 = data.curId1;
                curName2 = data.englishCode1;
            }
            if(busDirection=='1'){
                $("#buyCurId").val(curId1);
                $("#buyCurName").val(curName1);
                $("#sellCurId").val(curId2);
                $("#sellCurName").val(curName2);
            }else{
                $("#buyCurId").val(curId2);
                $("#buyCurName").val(curName2);
                $("#sellCurId").val(curId1);
                $("#sellCurName").val(curName1);
            }
        }

        var proId = selectType.get("proId").getData().value;
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: $("#buyCurId").val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#corpBank').val()});
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: $("#sellCurId").val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#corpBank').val()});
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId").val(),proId,"1","0", $("#cotpId").val());
        renderBankAcc("1", $("#cotpId").val(), $("#buyCurId").val(),proId,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId").val(),proId,"1","1", $("#cotpId").val());
        renderBankAcc("1", $("#cotpId").val(), $("#sellCurId").val(),proId,"1","0", $('input[name="corpId"]').val());
        calContractRate();
        fillForwardSpotRate(true);
        form.render();
    }

    //保证金方式回调函数
    bt.boodTypeCallback = function (data, $elem){
        var boodType = bt.dicts.get('boodType').val().trim();

        if ("5" == boodType || !boodType) {
            $(".boodDiv").hide();
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
            selectType.get('bondCurId').attr('data-required', false);
            moneyType.get('payMoney').removeAttr('lay-verify');
            setLayVerifyWithStrlength($('#boodRmk'), false);
            return;
        } else {
            $(".boodDiv").show();
        }


        if(boodType == "3"){
            setLayVerifyWithStrlength($('#boodRmk'), true, 'required');
        }else{
            setLayVerifyWithStrlength($('#boodRmk'), false);
        }
        if(boodType == "4"){
            moneyType.get('payMoney').attr('lay-verify', 'required|number2');
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            selectType.get('bondCurId').attr('data-required', false);
        }else{
            moneyType.get('payMoney').attr('lay-verify', 'number2');
            var deliveryDeadline = $('#deliveryDeadline').val();
            if(boodType=="9"){
                bt.dicts.get('boodType').setRequired(false);
                bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
            }else if(deliveryDeadline>0){
                bt.dicts.get('boodType').setRequired(true);
                bt.ratioType.get('boodRatio').currentObject.attr('lay-verify', 'required');
            }
            if(deliveryDeadline>0 && boodType != "3" && boodType !="9") {
                $('#boodAcc').attr('lay-verify', 'required|tableData');
                bt.dicts.get('boodBankNote').attr({'data-required': true});
                selectType.get('bondCurId').attr('data-required', true);
            }else{
                $('#boodAcc').attr('lay-verify', 'tableData');
                bt.dicts.get('boodBankNote').attr({'data-required': false});
                selectType.get('bondCurId').attr('data-required', false);
            }
        }
        form.renderIcon();
    };
    //结汇资金用途回调函数
    bt.purposeCallback = function (data, $elem){
        var purpose = bt.dicts.get('purpose').val();
        if(purpose == "005" || purpose == "006" || purpose == "099"){
            setLayVerifyWithStrlength($('#purposeDtl'), true, 'required');
        }else{
            setLayVerifyWithStrlength($('#purposeDtl'), false);
        }
        form.renderIcon();
    };

    //固定金额回调函数
    bt.fixMoneyCallback = function(data){
        //设置买入/卖出金额
        setMoneyInfo();
        //加载资金计划
        renderBudget();
        calContractRate();
    };

    // 钞汇标识回调
    bt.bankNoteCallback = function (data){
        if ("1" == data.value) {
            bt.dicts.get('isPayed').attr({'data-disabled': true});
            bt.dicts.get('isPayed').val("1");
        } else {
            bt.dicts.get('isPayed').attr({'data-disabled': false});
        }
    }

    bt.transExchangeRateType = function(data){
        var tranRateType = bt.dicts.get('tranRateType').val();
        if ("2" == tranRateType) {
            moneyType.get("contractRate").attr("lay-verify", "required");
            moneyType.get("spotRate").attr("lay-verify", "required");
            getSpotRate();
        } else {
            moneyType.get("contractRate").removeAttr("lay-verify");
            moneyType.get("spotRate").removeAttr("lay-verify");

            moneyType.get("sellMoney").removeAttr("readonly");
            moneyType.get("buyMoney").removeAttr("readonly");
        }
        setMoneyInfo();
        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }



    dicts = bt.dicts.init();
    if ($("#originStatus").val() == "-1" && dicts.get("busDirection")) {
        dicts.get("busDirection").attr({'data-disabled': true});
    }
    // 与新增页选货币对后一致：结汇/购汇且单位本位币在货币对一侧时锁定买卖方向（复制页已带回货币对也会走这里）
    lockBusDirectionByCurrencyPair();

    /**
     * 与远期新增页选货币对后一致的买卖方向锁定规则
     */
    function lockBusDirectionByCurrencyPair() {
        // 需求明细/打回页已用隐藏域提交买卖方向，没有可编辑的 busDirection 控件
        if (!$('[data-biz-name="busDirection"]').length) {
            return;
        }
        if ($("#originStatus").val() == "-1") {
            dicts.get("busDirection").attr({'data-disabled': true});
            syncBusDirectionHidden(true);
            return;
        }
        var tranCurPairBaseId = $("#tranCurPairBaseId").val();
        var tranCurPairReferId = $("#tranCurPairReferId").val();
        var corpBaseCur = $("#corpBaseCur").val();
        var fexBusType = $("#fexBusType").val();
        if ((tranCurPairBaseId == corpBaseCur || tranCurPairReferId == corpBaseCur)
            && ("1" == fexBusType || "2" == fexBusType)) {
            dicts.get("busDirection").attr({'data-disabled': true});
            syncBusDirectionHidden(true);
        } else {
            dicts.get("busDirection").attr({'data-disabled': false});
            syncBusDirectionHidden(false);
        }
    }

    /** disabled 的 radio 不会提交，锁定时用隐藏域带出买卖方向 */
    function syncBusDirectionHidden(locked) {
        var $hidden = $("#busDirectionSubmit");
        // 模板里已有 name=busDirection 的隐藏域时，不再追加同名字段
        var $pageHidden = $("#busDirection").filter('input[type=hidden][name=busDirection]');
        if ($pageHidden.length) {
            if ($hidden.length) {
                $hidden.remove();
            }
            return;
        }
        if (locked) {
            var val = $('input[type="radio"][name="busDirection"]:checked').val() || "";
            if ($('[data-biz-name="busDirection"]').length) {
                val = val || dicts.get("busDirection").val() || "";
            }
            if (!val) {
                val = $("#busDirection").val() || "";
            }
            if (!$hidden.length) {
                $('<input type="hidden" id="busDirectionSubmit" name="busDirection">').appendTo("#inputForm");
                $hidden = $("#busDirectionSubmit");
            }
            $hidden.val(val);
        } else if ($hidden.length) {
            $hidden.remove();
        }
    }

    var boodType = bt.dicts.get('boodType').val().trim();
    if (!boodType) {
        bt.dicts.get('boodType').val("5");
        $(".boodDiv").hide();
    } else if("5" == boodType) {
        $(".boodDiv").hide();
    } else {
        $(".boodDiv").show();
    }

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

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
                        //清空联系人信息
                        $('#corpContact').val("");
                        $('#corpContactTel').val("");
                        //重新加载联系人
                        corpContactInit.get("corpContact").reload({corpId: id});
                        //加载资金计划
                        renderBudget();
                    }
                    $("#corpBaseCur").val(data.obj.curId);
                    lockBusDirectionByCurrencyPair();
                }
                form.render();
            }
        });
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: $("#sellCurId").val(), corpId: id,bankTypeId: $('#corpBank').val()});
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: $("#buyCurId").val(), corpId: id,bankTypeId: $('#corpBank').val()});
        //重新加载保证金账户信息
        boodAccInit.get("boodAcc").reload({curId: bt.selectType.get('bondCurId').val(), corpId: id});
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", id, $("#buyCurId").val(),selectType.get("proId").val(),"1","0", $("#cotpId").val());
        renderBankAcc("0", id, $("#sellCurId").val(),selectType.get("proId").val(),"1","1", $("#cotpId").val());
    };
    //初始化单位
    var corp = bt.corp.init();


    //交易对手银行账户回调函数
    bt.cotpBankCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#oppId").val(data.id);
                $("#cotpBank").val(data.id);
                $("#cotpName").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode").val(data.swiftCode);
                    $("#cotpBicCodeDiv").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv").text('');
                }
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);
                $("#targetCotpTd").text(data.bankName);
                $("#targetCotpBankTd").text(data.bankName);
                dicts.get("fixedMoney").attr({"data-disabledValue": ""});
            } else if (data.type === "outerBank") {
                $("#oppId").val(data.id);
                $("#cotpBank").val(data.id);
                $("#cotpName").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode").val(data.swiftCode);
                    $("#cotpBicCodeDiv").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv").text('');
                }
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);
                $("#targetCotpTd").text(data.bankName);
                $("#targetCotpBankTd").text(data.bankName);
                //越南工行需求：2.外汇即期交易，校验【交易对手银行】字段，若对应的【银行类型】为中国工商银行/工商银行且【国家/地区】中文名称为越南时，固定金额字段默认卖出金额不能修改
                if ($('#type').val() == 'spot' && data.bankTypeName && data.bankTypeName.indexOf('工商银行') != -1
                    && data.nameZh && data.nameZh.indexOf('越南') != -1) {
                    dicts.get("fixedMoney").attr({"data-disabledValue": "1","data-defaultvalue": "2"});
                } else {
                    dicts.get("fixedMoney").attr({"data-disabledValue": ""});
                }
            }
        } else {
            $("#oppId").val("");
            $("#cotpBank").val("");
            $("#cotpName").val("");
            $("#cotpBicCode").val("");
            $("#cotpBicCodeDiv").text("");
            $("#srcCotpTd").text("");
            $("#srcCotpBankTd").text("");
            $("#targetCotpTd").text("");
            $("#targetCotpBankTd").text("");
        }
        //新交易对手ID
        var oppId = data.id;
        if (isEmpty(oppId)) {
            oppId = null;
        }
        var corpId = $('[name="corpId"]').val()
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: $("#sellCurId").val(), corpId: corpId,bankTypeId: data.bankTypeId});
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: $("#buyCurId").val(), corpId: corpId,bankTypeId: data.bankTypeId});


        //加载主体单位和交易对手账户信息
        renderBankAcc("1", oppId, $("#buyCurId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        renderBankAcc("1", oppId, $("#sellCurId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId").val(),selectType.get("proId").val(),"1","0", oppId);
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId").val(),selectType.get("proId").val(),"1","1", oppId);
    }

    bt.allBankInfo.init();
    //付款账户信息
    var srcBankAccInit = bt.selectTable.init({
        elem: "#srcBankAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#sellCurId").val(), corpId: $("#corpIdDef").val(),bankTypeName:$("#cotpnameDef").val(),bankTypeId: $('#corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#srcBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data, row, inputElem){
            if(data){
                inputElem.val(data.bankAcc);
                $('#srcAccId').val(data.id);
                $('#srcBank').val(data.bankTypeId);
                $('#srcBankAcc').val(data.bankAcc);
                $('#srcAccName').val(data.accName);
                if (data.bankAcc) {
                    $("#srcBankNameDiv").text(data.bankName);
                }else{
                    $("#srcBankNameDiv").text("");
                }
                if (data.swiftCode) {
                    $("#srcBankAccSwiftCodeDiv").text(data.swiftCode);
                } else {
                    $("#srcBankAccSwiftCodeDiv").text("");
                }
                //现金流信息
                $("#targetAccNameTd").text(data.accName);
                $("#targetBankAccTd").text(data.bankAcc);
                $("#targetBankTd").text(data.bankTypeName);
                // $("#targetCurTd").text(data.curEnglishCode);
            }else{
                $('input.text').val('');
            }
        }
    });

    /**
     * 银行自定义配置已下线，不再根据银行类型显隐支付类型/其他信息
     */
    function setBankTypeCustom(bankType, bifCode, countryId, bankPayType, remitType, initCustomType) {
    }

    //收款账户信息
    var corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#buyCurId").val(), corpId: $("#corpIdDef").val(),bankTypeName:$("#cotpnameDef").val(),bankTypeId: $('#corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#corpBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data, row, inputElem){
            if(data){
                inputElem.val(data.bankAcc);
                $('#corpAccId').val(data.id);
                $('#corpBank').val(data.bankTypeId);
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccName').val(data.accName);
                if (data.bankAcc) {
                    $("#corpBankNameDiv").text(data.bankTypeName);
                }else{
                    $("#corpBankNameDiv").text("");
                }
                if (data.swiftCode) {
                    $("#corpBankAccSwiftCodeDiv").text(data.swiftCode);
                } else {
                    $("#corpBankAccSwiftCodeDiv").text("");
                }
                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                // $("#srcCurTd").text(data.curEnglishCode);
            }else{
                $('input.text').val('');
            }
        }
    });

    //保证金账户信息
    var boodAccInit = bt.selectTable.init({
        elem: "#boodAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $('#bondCurIdDef').val(), corpId:$('#corpIdDef').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#boodAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#boodAcc').val(data.bankAcc);
                $('#boodAccId').val(data.id);
                if (data.curId && selectType && selectType.get('bondCurId')) {
                    selectType.get('bondCurId').val(data.curId);
                }
            }else{
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
            } else {
                $('input.text').val('');
            }
        }
    });

    //联系人
    var corpContactInit = bt.selectTable.init({
        elem: "#corpContactTable",
        url: basePath + "/foreign/fexlinkmaninfo/listByCorpId",
        urldata: {corpId: $('#corpIdDef').val()},
        searchInput: "#corpContact",
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "payerName",},
            {data: "payerTel",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#corpContact').val(data.payerName);
                $('#corpContactTel').val(data.payerTel);
            } else {
                $('input.text').val('');
            }
        }
    });

    //交易地点下拉表格
    var tranAddrInit = bt.selectTable.init({
        elem: "#tranAddrTable",
        url: basePath + "/basic/business/syscountrycust/listAllSysCountryArea",
        searchInput: "#tranAddr",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("#tranAddrId").val(data.id);
            $("input[name='tranAddr']").val(data.nameZh);
        },
    });

    //买入/卖出金额等设置
    function setMoneyInfo(){
        var fexTranType = $("#fexTranType").val();
        var type = $("#type").val();
        var fixedMoney = bt.dicts.get('fixedMoney').val();
        var tranRateType = bt.dicts.get('tranRateType').val();
        if ("2" == tranRateType) {
            moneyType.get("buyMoney").attr('lay-verify', 'required');
            moneyType.get("sellMoney").attr('lay-verify', 'required');

            moneyType.get("contractRate").attr("lay-verify", "required");
            moneyType.get("spotRate").attr("lay-verify", "required");
        } else if ("1" == fixedMoney) { // 即期 && 买入
            moneyType.get("buyMoney").attr('lay-verify', 'required');
            moneyType.get("sellMoney").removeAttr("lay-verify");
            moneyType.get("buyMoney" ).removeAttr("readonly");
            moneyType.get("sellMoney" ).removeAttr("readonly");

            moneyType.get("contractRate").removeAttr("lay-verify");
            moneyType.get("spotRate").removeAttr("lay-verify");
        } else if ("2" == fixedMoney) { // 即期 && 卖出
            moneyType.get("buyMoney").removeAttr("lay-verify");
            moneyType.get("sellMoney").attr('lay-verify', 'required');
            moneyType.get("buyMoney" ).removeAttr("readonly");
            moneyType.get("sellMoney" ).removeAttr("readonly");

            moneyType.get("contractRate").removeAttr("lay-verify");
            moneyType.get("spotRate").removeAttr("lay-verify");
        } else {
            moneyType.get("buyMoney" ).removeAttr("lay-verify");
            moneyType.get("sellMoney" ).removeAttr("lay-verify");
            moneyType.get("buyMoney" ).removeAttr("readonly");
            moneyType.get("sellMoney" ).removeAttr("readonly");

            moneyType.get("contractRate").removeAttr("lay-verify");
            moneyType.get("spotRate").removeAttr("lay-verify");
        }


        $("#srcTr").hide();
        $("#targetTr").hide();
        if(fixedMoney == "1") {
            $("#srcTr").show();
            if ("2" == tranRateType) {
                $("#targetTr").show();
            }
        } else if(fixedMoney == "2") {
            $("#targetTr").show();
            if ("2" == tranRateType) {
                $("#srcTr").show();
            }
        }
        form.renderIcon();
    }


    //产品回调函数
    bt.proCallback = function (data) {
        var swapType = $("#swapType").val();
        $("#fexBusType").val(data.fexbustype);
        // 即期是否自动交割
        $("#isSpotAutoDelivery").val(data.isautodelivery);
        setProfitLoss();
        //所选产品为结汇时，默认自动计算买入金额；所选产品为购汇时，默认自动计算卖出金额
        if($("#fexBusType").val() == "2"){//购汇
            //结售汇属性设置为购汇的字典
            bt.dicts.get("fexProp")['fex.tranApp.fexProp'] = null;
            bt.dicts.get("fexProp").attr({'data-group': 'fex.tranApp.fexProp2'});
        } else {
            //结售汇属性设置为结汇的字典
            bt.dicts.get("fexProp")['fex.tranApp.fexProp2'] = null;
            bt.dicts.get("fexProp").attr({'data-group': 'fex.tranApp.fexProp'});
        }
        $("#fexTranType").val(data.fextrantype);
        if(swapType=="2"){
            $("#fexTranType").val(data.fextrantyperemote);
            if(data.fexbustype=="1"){
                $("#fexBusType").val("2");
            }else if(data.fexbustype=="2"){
                $("#fexBusType").val("1");
            }
        }
        $("#deliveryType").val(data.deliverytype);
        $("#fexBusTypeDiv").text(bt.dict("fexProduct.fexBusType", $("#fexBusType").val()));
        $(".fexTranTypeDiv").text(bt.dict("fexProduct.fexTranType", $("#fexTranType").val()));
        $("#deliveryTypeDiv").text(bt.dict("fexProduct.deliveryType", data.deliverytype));
        if($("#fexTranType").val()=="1"){
            $("#exposureBusType").val("2");
        }else{
            $("#exposureBusType").val("1");
        }

        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull").hide();
            $("#deliveryCurTd").show();
            $("#deliveryCurDiv").show();
        } else {
            $("#deliveryCurTdNull").show();
            $("#deliveryCurTd").hide();
            $("#deliveryCurDiv").hide();
        }
        //结汇
        if($("#fexBusType").val()=="1"){
            $("#settleBusTypeTd").show();
            $("#settleBusTypeDiv").show();
            $("#purposeTr").show();
        }else{
            $("#settleBusTypeTd").hide();
            $("#settleBusTypeDiv").hide();
            $("#purposeTr").hide();
            bt.dicts.get('purpose').val('');
            setLayVerifyWithStrlength($('#purposeDtl'), false);
            $('#purposeDtl').val('');
        }
        //设置买入/卖出金额
        setMoneyInfo();

        if (data.buycurid && data.sellcurid) {
            getCurrencyPair(data.buycurid, data.sellcurid);
        }

        form.renderIcon();
        form.render();
    };

    //交易对手银行账户回调函数
    bt.cotpCallback = function (data) {
        $("#srcCotpTd").text(data.cotpname);
        $("#targetCotpTd").text(data.cotpname);
        $("#cotpBicCode").val(data.biccode);
        if(data.biccode){
            $("#cotpBicCodeDiv").text(data.biccode);
        }else{
            $("#cotpBicCodeDiv").text('');
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("1", data.id, $("#buyCurId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        renderBankAcc("1", data.id, $("#sellCurId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());

        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: $('#buyCurId').val(), corpId: $('input[name="corpId"]').val(),bankTypeName:data.cotpname,bankTypeId: $('#corpBank').val()});
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: $('#sellCurId').val(), corpId: $('input[name="corpId"]').val(),bankTypeName:data.cotpname,bankTypeId: $('#corpBank').val()});
    };

    /**
     * 合同汇率计算
     */
    function calContractRate() {
        var sellCurId = $("#sellCurId").val(); // 卖出币种
        var tranCur = $("#tranCurPairBaseId").val();
        var currentMoney = moneyType.get("buyMoney").val()
        if (tranCur == sellCurId) {
            currentMoney = moneyType.get("sellMoney").val()
        }
        moneyType.get("positionMoney").val(currentMoney);

        var buyMoney = moneyType.get("buyMoney").val();
        var sellMoney = moneyType.get("sellMoney").val();
        if (isNotEmpty(buyMoney) && isNotEmpty(sellMoney)) {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('buyMoney').val()));
            $("#targetPayMoneyTd").text('-' + moneyFun.format(moneyType.get('sellMoney').val()));
        }
    }

    //合同汇率回调函数
    bt.contractRateCallback = function (data) {
        var isPaymentAmount = bt.dicts.get('isPaymentAmount').val(); // 是否自动计算
        var tranCurPairReferId = $("#tranCurPairReferId").val();
        var tranAppDtlId = $("#tranAppDtlId").val();
        var fixedMoney = bt.dicts.get('fixedMoney').val();
        var buyMoney = moneyType.get("buyMoney").val();
        var sellMoney = moneyType.get("sellMoney").val();
        var calculateBuyMoney = "2" == fixedMoney;
        // 需求申请生成的掉期远端单可能只带入卖出金额，此时需按合同汇率反算买入金额
        if (tranAppDtlId && "2" == $("#swapType").val() && !buyMoney && sellMoney) {
            calculateBuyMoney = true;
        }
        var money  = buyMoney; // 货币对左侧金额
        var curId = $("#buyCurId").val();
        if (calculateBuyMoney) { // 买入币种（远端）=货币对的右侧 更新买入金额
            money = sellMoney;
            curId = $("#sellCurId").val();
        }
        if ("1" != isPaymentAmount || !money) {
            // 计算损益
            calculateFexProfitLoss();
            return;
        }
        var contractRate = moneyType.get("contractRate").val();
        var quotationCur = $("#quotationCurId").val(); // 报价方式的左侧
        if (curId == quotationCur) {
            money = moneyFun.mul(money,contractRate,2);
        } else {
            money = moneyFun.div(money,contractRate,2);
        }
        if (calculateBuyMoney) {
            moneyType.get("buyMoney").val(money);
        } else {
            moneyType.get("sellMoney").val(money);
        }
        calContractRate();
        // 计算损益
        calculateFexProfitLoss();
    };

    // 即期汇率回调
    bt.spotRateCallback= function(data){
        // 计算损益
        calculateFexProfitLoss();
    }

    // 买入金额回调
    bt.buyMoneyCallback = function(){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount').val(); // 是否自动计算
        var contractRate = moneyType.get("contractRate").val();
        var sellMoney = moneyType.get("sellMoney").val();
        var tranAppDtlId = $("#tranAppDtlId").val();
        if ("1" != isPaymentAmount || !contractRate || (sellMoney && tranAppDtlId)) {
            return;
        }
        var buyMoney  = moneyType.get("buyMoney").val();
        var buyCurId = $("#buyCurId").val(); // 卖出币种
        var quotationCur = $("#quotationCurId").val(); // 报价方式的左侧
        if (buyCurId == quotationCur && "1" == isPaymentAmount) {
            moneyType.get("sellMoney").val(moneyFun.mul(buyMoney,contractRate,2));
        } else if ("1" == isPaymentAmount){
            moneyType.get("sellMoney").val(moneyFun.div(buyMoney,contractRate,2));
        }
        calContractRate();

        // 计算损益
        calculateFexProfitLoss();
    }

    // 卖出金额回调
    bt.sellMoneyCallback = function(){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount').val(); // 是否自动计算
        var contractRate = moneyType.get("contractRate").val();
        var buyMoney = moneyType.get("buyMoney").val();
        var tranAppDtlId = $("#tranAppDtlId").val();
        if ("1" != isPaymentAmount || !contractRate || (buyMoney && tranAppDtlId)) {
            return;
        }
        var sellMoney  = moneyType.get("sellMoney").val();
        var sellCurId = $("#sellCurId").val(); // 卖出币种
        var quotationCur = $("#quotationCurId").val(); // 报价方式的左侧
        if (sellCurId == quotationCur) {
            moneyType.get("buyMoney").val(moneyFun.mul(sellMoney,contractRate,2));
        } else {
            moneyType.get("buyMoney").val(moneyFun.div(sellMoney,contractRate,2));
        }
        calContractRate();

        // 计算损益
        calculateFexProfitLoss();
    }



    /**
     * 为空判断
     * @param stringData
     * @returns {boolean}
     */
    function isEmpty(stringData) {
        if (null == stringData || stringData == "" || stringData == undefined || stringData == " ") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 非空判断
     * @param stringData
     * @returns {boolean}
     */
    function isNotEmpty(stringData) {
        return !isEmpty(stringData)
    }

    bt.moneyCallback = function(){
        calContractRate();
    }


    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }
    selectType = bt.selectType.init();

    // 折本位币报价方式
    function getBaseCuQuotationMethod(profitLossCurId) {
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = selectType.get("proId").getData().obj.isAutoDelivery;
        var tranRateType = bt.dicts.get('tranRateType').val();
        var corpBaseCur = $("#corpBaseCur").val();
        var profitLossCur = profitLossCurId || selectType.get("profitLossCur").val();

        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery && corpBaseCur != profitLossCur) {
            $(".baseConvertTr").show();
            $("#isShowConvert").val("Y");
            $("input[data-id='convertRate']").attr("lay-verify", "required");
            $("input[data-id='convertMoney']").attr("lay-verify", "required");
        } else {
            $(".baseConvertTr").hide();
            $("#isShowConvert").val("N");
            $("input[data-id='convertRate']").removeAttr("lay-verify");
            $("input[data-id='convertMoney']").removeAttr("lay-verify");
        }
        if ("Y" != $("#isShowConvert").val()) {
            return
        }
        $.ajax({
            url: basePath + "/foreign/fexrateinfo/getBaseCurFexRate",
            method: 'POST',
            data: {curId: profitLossCur,referCurId:$("#corpBaseCur").val()},
            dataType: 'json',
            success: function (result) {
                if (result.obj) {
                    $("#convertCurType").val(result.obj.curEnCode+"/"+result.obj.referCurEnCode);
                    $("#convertCur").val(result.obj.cur);
                    $("#convertCur").val(result.obj.referCur);
                    if (profitLossCur == result.obj.cur) {
                        moneyType.get('convertRate').val(result.obj.purchaseRate);
                    } else {
                        moneyType.get('convertRate').val(result.obj.sellRate);
                    }
                }
            }
        });
    }

    // 远期/掉期/即期：按交易货币对取最新即期汇率；有值不覆盖（除非 force）
    function fillForwardSpotRate(force) {
        var type = $("#type").val();
        if ("forward" == type || "swap" == type || "spot" == type) {
            getSpotRate(force);
        }
    }

    // 查询即期汇率：默认外汇牌价类型 + 最新发布时间的即期牌价
    // 买入币种=报价方式左侧取买入价，买入币种=报价方式右侧取卖出价
    function getSpotRate(force) {
        var pairBaseId = $("#tranCurPairBaseId").val();
        var pairReferId = $("#tranCurPairReferId").val();
        var buyCurId = $("#buyCurId").val();
        if (!pairBaseId || !pairReferId || !buyCurId) {
            return;
        }
        var spotRateField = moneyType.get("spotRate");
        if (!spotRateField) {
            return;
        }
        if (!force && spotRateField.val()) {
            return;
        }
        $.ajax({
            type:'POST',
            url:basePath+"/foreign/fexrateinfo/getSpotRate",
            data:{'curId': pairBaseId, 'referCurId': pairReferId},
            async:false,
            dataType:'json',
            success:function(result){
                if(result.success && result.obj){
                    if(buyCurId == result.obj.cur){
                        spotRateField.val(result.obj.purchaseRate);
                    } else if (buyCurId == result.obj.referCur) {
                        spotRateField.val(result.obj.sellRate);
                    }
                }
            }
        })
    }

    // 计算折本位币生命周期损益
    function calculateFexLifeCycleConvertLossMoney(){
        if ("Y" != $("#isShowConvert").val()) {
            return;
        }
        var calculateForm = {
            profitLossCur: selectType.get("profitLossCur").val(),
            convertCurType: $("#convertCurType").val(),
            profitLossMoney: moneyType.get('profitLossMoney').val(),
            convertRate: moneyType.get('convertRate').val(),
            corpId: $('input[name="corpId"]').val()
        }
        $.ajax({
            "url": basePath + "/foreign/fexProfit/lossManage/calculateFexLifeCycleConvertLossMoney",
            data: JSON.stringify(calculateForm),
            type:"post",
            dataType:"json",
            contentType: "application/json",
            async: false,
            success:function (result) {
                if (result.success) {
                    moneyType.get('convertMoney').val(result.obj);
                }
            }
        });
    }

    //结算日期回调函数
    settleDateCallback = function (data, $elem){
        checkDate();
        var settleDate = dateType.get('settleDate').val();
        var tranDate = dateType.get('tranDate').val();
        var deliveryDeadline = dateFun.getBetweenDay(tranDate, settleDate);

        var begin = dateFun.get(dateFun.format(dateFun.get(tranDate),'yyyy-MM-dd'));//开始日期
        var end = dateFun.get(dateFun.format(dateFun.get(settleDate),'yyyy-MM-dd'));
        var weekDays=0;
        while (begin <= end) {
            if (begin.getDay() === 6) { // 6 代表周六
                weekDays++;
            } else if (begin.getDay() === 0) { // 0 代表周日
                weekDays++;
            }
            begin = dateFun.addDay(begin,1); // 增加一天
        }
        deliveryDeadline = deliveryDeadline-weekDays;
        $("#deliveryDeadline").val(deliveryDeadline);
        $("#deliveryDeadlineDiv").text('T+'+deliveryDeadline);
        renderBudget();
        //保证金相关信息设置
        setBoodInfo();
        //现金流
        $("#srcSettleDateTd").text(settleDate);
        $("#targetSettleDateTd").text(settleDate);
        form.render();
    };

    //根据交割期限设置保证金相关信息
    function setBoodInfo(){
        var deliveryDeadline = $('#deliveryDeadline').val();
        var boodType = bt.dicts.get('boodType').val().trim();
        //非T+0且保证金方式不为授信额度时：保证方式、保证金账号、保证比例、保证金钞汇标识、保证金币种都为必填
        if ("5" == boodType || !boodType) {
            bt.dicts.get('boodType').setRequired(false);
            bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            selectType.get('bondCurId').attr('data-required', false);
        } else if(deliveryDeadline>0 && boodType != "3" && boodType!="4" && boodType !='9') {
            bt.dicts.get('boodType').setRequired(true);
            $('#boodAcc').attr('lay-verify', 'required|tableData');
            bt.ratioType.get('boodRatio').currentObject.attr('lay-verify', 'required');
            bt.dicts.get('boodBankNote').attr({'data-required': true});
            selectType.get('bondCurId').attr('data-required', true);
        }else{
            if(boodType=='9'){
                bt.dicts.get('boodType').setRequired(false);
                bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
            }else{
                if(deliveryDeadline>0) {
                    bt.ratioType.get('boodRatio').currentObject.attr('lay-verify', 'required');
                    bt.dicts.get('boodType').setRequired(true);
                }else{
                    bt.dicts.get('boodType').setRequired(false);
                    bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
                }
            }
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            selectType.get('bondCurId').attr('data-required', false);
        }
        form.renderIcon();
    }

    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    fillForwardSpotRate();

    /**
     * 获取账户信息
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     * @param swapType 掉期交易 1-近端 2-远端
     */
    function renderBankAcc(busType, corpId, curId,proId,swaptype,cdSign,oppCorpId){
        $.ajax({
            url: basePath + "/foreign/fexcorpbankacc?getBankAcc",
            method: 'POST',
            data: {busType: busType, tranType: "1", corpId: corpId, curId: curId, proId: proId, cashFlowType: "0", cdSign: cdSign, oppCorpId: oppCorpId},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    //收款
                    if (cdSign == "0") {
                        if (busType == "0") {
                            $('#corpBank').val(data.obj.bankType);
                            $('#corpAccId').val(data.obj.accId);
                            $('#corpBankAcc').val(data.obj.bankAcc);
                            $('#corpAccName').val(data.obj.accName);
                            if (data.obj.bankAcc) {
                                $("#srcBankTd").text(data.obj.bankTypeName);
                                $("#srcAccNameTd").text(data.obj.accName);
                                $("#srcBankAccTd").text(data.obj.bankAcc);
                                if(data.obj.voucherName) {
                                    $("#srcPayTypeTd").text(data.obj.voucherName);
                                }else{
                                    $("#srcPayTypeTd").text("");
                                }
                                $("#corpBankNameDiv").text(data.obj.bankTypeName);
                            } else {
                                $("#srcBankTd").text("");
                                $("#srcAccNameTd").text("");
                                $("#srcBankAccTd").text("");
                                $("#srcPayTypeTd").text("");
                                $("#corpBankNameDiv").text("");
                            }
                        } else {
                            $('#targetBank').val(data.obj.bankType);
                            $('#targetAccId').val(data.obj.accId);
                            $('#targetBankAcc').val(data.obj.bankAcc);
                            $('#targetAccName').val(data.obj.accName);
                        }
                    }else{
                        if (busType == "0") {
                            $('#srcBank').val(data.obj.bankType);
                            $('#srcAccId').val(data.obj.accId);
                            $('#srcBankAcc').val(data.obj.bankAcc);
                            $('#srcAccName').val(data.obj.accName);
                            if (data.obj.bankAcc) {
                                $("#targetAccNameTd").text(data.obj.accName);
                                $("#targetBankAccTd").text(data.obj.bankAcc);
                                if(data.obj.voucherName) {
                                    $("#targetPayTypeTd").text(data.obj.voucherName);
                                }else{
                                    $("#targetPayTypeTd").text("");
                                }
                                $("#targetBankTd").text(data.obj.bankTypeName);
                                $("#srcBankNameDiv").text(data.obj.bankTypeName);
                            } else {
                                $("#targetAccNameTd").text("");
                                $("#targetBankAccTd").text("");
                                $("#targetPayTypeTd").text("");
                                $("#targetBankTd").text("");
                                $("#srcBankNameDiv").text("");
                            }
                        } else {
                            $('#cotpBank').val(data.obj.bankType);
                            $('#cotpAccId').val(data.obj.accId);
                            $('#cotpBankAcc').val(data.obj.bankAcc);
                            $('#cotpAccName').val(data.obj.accName);
                        }
                    }
                }
            }
        });
    }

    //监听提交
    form.on('submit(submit)', function(data){
        var $busDirectionRadios = $('input[type="radio"][name="busDirection"]');
        //提交前按原生控件状态补齐隐藏域，避免 disabled 的买卖方向被 serialize() 忽略
        syncBusDirectionHidden($busDirectionRadios.length > 0
            && $busDirectionRadios.filter(':disabled').length === $busDirectionRadios.length);
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");
        $("#attachmentId").val(up.getGroupId());
        data.field.attachmentId = up.getGroupId();
        var fexTranType = $("#fexTranType").val();
        //外汇交易类型 0-即期、1-远期
        if(data.field.type=="1"&&fexTranType=="0") {
            if (bt.dicts.get('fixedMoney').val()) {
                data.field.fixedMoney = (bt.dicts.get('fixedMoney').val()).toString();
            } else {
                data.field.fixedMoney = "";
            }
        }

        if ("Y" != $("#isShowConvert").val()) {
            delete data.field.convertRate;
            delete data.field.convertMoney;
            delete data.field.convertCurType;
        }


        var tranDate = dateType.get("tranDate").val();
        var settleDate = dateFun.addDay(dateType.get("settleDate").val(),1);
        /*if(dateFun.compare(tranDate, expiresDate) > 0||dateFun.compare(settleDate, expiresDate) <= 0){
            bt.loading.hide();
            layer.alert("到期日不能小于交易日或者不能大于交割日", {icon: 2});
            return false;
        }*/
        //证书签名
        var signDataMsg = signData("外汇交易提交", 11);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        if(data.field.type=="3"&&data.field.status=='11'){
            bt.loading.hide();
            parent.layer.confirm('近端远端将同时提交，请确认已维护另外一端的数据', function () {
                bt.loading.show();
                var action = "/foreign/fextranreq/modify";
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
                return false;
            });
        }else{
            var action = "/foreign/fextranreq/modify";
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
            return false;
        }
        return false;
    });

    /**
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreq/updateCustInfo/" + $('#id').val() + "?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#custMsg").html("");
                $("#custMsg").html(json);
                bt.dateType.init();
                form.renderIcon();
                form.render();
            }
        });
    }
    $genCustHtml();


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
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $("#corpIdDef").val();
        if ($('input[name="corpId"]').val()) {
            corpId = $('input[name="corpId"]').val();
        }
        var curId = $("#cur").val();
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            corpId: corpId,
            itemCode: $("#payItem").val() ? $("#payItem").val() : "400",
            billNo: $("#billNo").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate: dateType.get("settleDate").val(),
            type: 'select',
            detailControl: true,
            curId: curId,
            wishPayday: dateType.get("settleDate").val(),
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


    // 计算损益
    var tranRateType = $('[data-biz-name="tranRateType"]').data('defaultvalue') || "2";
    var billFrom = $("#billFrom").val();
    var showProfitLoss = $("#showProfitLoss").val();
    var profitLossMoney =  moneyType.get('profitLossMoney').val();
    var defProfitLossCur = $('[data-biz-name="profitLossCur"]').data('defaultvalue');
    var defDusDirection = $('[data-biz-name="busDirection"]').data('defaultvalue')
        || $('[data-biz-name="busDirectionDisplay"]').data('defaultvalue')
        || $("#busDirection").val();
    var defCorpId = $('[data-biz-name="corpId"]').data('defaultvalue');
    if ($("#tranAppDtlId").val()) {
        defCorpId = $("#corpIdDef").val();
    }
    if ("2" == tranRateType && "1" == showProfitLoss && "1" == billFrom && !profitLossMoney) {
        calculateFexProfitLoss(defProfitLossCur, defDusDirection, defCorpId);
    }


    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        //外汇交易类型 0-即期、1-远期
        var fexTranType = $("#fexTranType").val();
        var type = $("#type").val();
        var flag = true;
        if(type=="1"&&fexTranType == "0") {
            //固定金额 1-买入金额 2-卖出金额
            var fixedMoney = (bt.dicts.get('fixedMoney').val()).toString();
            if (fixedMoney.indexOf("2") == -1) {
                flag = false;
            }
        }
        if (budgetSel == "1" && flag) {
            $genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
    renderBudget();
    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }

    // 获取货币对
    function getCurrencyPair(curId1,curId2) {
        $.ajax({
            "url": basePath + "/basic/business/btcurrencypair/getByCurId",
            data:{
                curId1: curId1,
                curId2: curId2
            },
            type:"post",
            dataType:"json",
            async: false,
            success:function (result) {
                if (result.success) {
                    selectType.get("tranCurPairId").val(result.obj.id);
                    setCur(result.obj);
                    if ("spot" == $("#type").val()) {
                        setProfitLoss();
                    }
                }
            }
        });
    }

    function setCur(tranCurPairIdData){
        var data = selectType.get("tranCurPairId").getData().obj;
        var pro = selectType.get("proId").getData().obj;
        var proId = selectType.get("proId").getData().value;
        var busDirection = dicts.get("busDirection").val();
        var defProfitAndLossCur = $("#defProfitAndLossCur").val();
        if(data){
            var curId1 = data.baseCurId;
            var curName1 = data.baseEnglishCode;
            var curId2 = data.curId2;
            var curName2 = data.englishCode2;
            if(curId1==curId2){
                curId2 = data.curId1;
                curName2 = data.englishCode1;
            }
            $("#tranCurPairBaseId").val(curId1);
            $("#tranCurPairReferId").val(curId2);
            $("#cur").val(curId1);
            $("#curEn").val(curName1);
            // 设置损益币种
            var profitLossCurId = null;
            if(defProfitAndLossCur=='1'){
                selectType.get("profitLossCur").attr({"data-displayData":curId1+","+curId2, "data-value":curId1});
                profitLossCurId = curId1;
            }else if(defProfitAndLossCur=='2'){
                selectType.get("profitLossCur").attr({"data-displayData":curId1+","+curId2, "data-value":curId2});
                profitLossCurId = curId2;
            }
            // 买卖方向设置（打回状态不允许改买卖方向）
            if ($("#originStatus").val() != "-1") {
                var tranCurPairBaseId = $("#tranCurPairBaseId").val();
                var tranCurPairReferId = $("#tranCurPairReferId").val();
                var corpBaseCur = $("#corpBaseCur").val();
                var fexBusType = $("#fexBusType").val();
                // 外汇业务类型=结汇 && 单位本位币=交易货币对的左侧 -> 买卖方向=卖出
                if ("1" == fexBusType && tranCurPairBaseId == corpBaseCur) {
                    dicts.get("busDirection").val("1");
                } else if ("2" == fexBusType && tranCurPairBaseId == corpBaseCur) { // 外汇业务类型=购汇 && 单位本位币=交易货币对的右侧 -> 买卖方向=买入
                    dicts.get("busDirection").val("2");
                } else if ("1" == fexBusType && tranCurPairReferId == corpBaseCur) {
                    dicts.get("busDirection").val("2");
                } else if ("2" == fexBusType && tranCurPairReferId == corpBaseCur) {
                    dicts.get("busDirection").val("1");
                }  else if(pro && pro.sellCurId == tranCurPairBaseId && pro.buyCurId == tranCurPairReferId){
                    dicts.get("busDirection").val("2");
                }else {
                    dicts.get("busDirection").val("1");
                }
                // 与新增页选货币对后一致：满足条件则锁定买卖方向
                lockBusDirectionByCurrencyPair();
            }
            busDirection = dicts.get("busDirection").val();
            if(busDirection=="1"){
                $("#buyCurId").val(curId1);
                $("#buyCurName").val(curName1);
                $("#sellCurId").val(curId2);
                $("#sellCurName").val(curName2);
                moneyType.get("buyMoney").attr("readonly",true);
                moneyType.get("sellMoney").removeAttr("readonly");
            }else{
                $("#buyCurId").val(curId2);
                $("#buyCurName").val(curName2);
                $("#sellCurId").val(curId1);
                $("#sellCurName").val(curName1);
                moneyType.get("sellMoney").attr("readonly",true);
                moneyType.get("buyMoney").removeAttr("readonly");
            }
            setQuotationMethod(curId1,curId2);
            // 折本位币
            getBaseCuQuotationMethod(profitLossCurId);
            fillForwardSpotRate(true);
            if ("Y" == $("#isShowConvert").val()) {
                // 计算损益
                calculateFexProfitLoss();
            }
            //现金流信息
            $("#srcCurTd").text($("#buyCurName").val());
            $("#targetCurTd").text($("#sellCurName").val());
        }else{
            $("#tranCurPairBaseId").val('');
            $("#tranCurPairReferId").val('');
            $("#cur").val('');
            $("#curEn").val('');
            $("#buyCurId").val('');
            $("#buyCurName").val('');
            $("#sellCurId").val('');
            $("#sellCurName").val('');
            //现金流信息
            $("#srcCurTd").text('');
            $("#targetCurTd").text('');
        }

        calContractRate();
        //加载资金计划
        renderBudget();
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: $("#buyCurId").val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#corpBank').val()});
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: $("#sellCurId").val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#corpBank').val()});
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId").val(),proId,"1","0", $("#cotpId").val());
        renderBankAcc("1", $("#cotpId").val(), $("#buyCurId").val(),proId,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId").val(),proId,"1","1", $("#cotpId").val());
        renderBankAcc("1", $("#cotpId").val(), $("#sellCurId").val(),proId,"1","0", $('input[name="corpId"]').val());
        form.render();

    }

    setTimeout(function(){
        var tranAppDtlId = $("#tranAppDtlId").val();
        if(tranAppDtlId){
            var buyMoney = moneyType.get("buyMoney").val();
            var sellMoney = moneyType.get("sellMoney").val();
            if (isNull(buyMoney) && !isNull(sellMoney)) {
                bt.sellMoneyCallback();
            } else if (!isNull(buyMoney) && isNull(sellMoney)) {
                bt.buyMoneyCallback();
            } else if (isNull(buyMoney) || isNull(sellMoney)) {
                calContractRate();
            }
            var deliveryDeadline = $("#deliveryDeadline").val();
            if(isNull(deliveryDeadline)){
                settleDateCallback();
            }
        }
    },800);

});

function isNull(data) {
    return (data == "" || data == undefined || data == null) ? true : false;
}



/**
 * 计算损益
 * @param data
 */
function calculateFexProfitLoss(defProfitLossCur, defDusDirection, defCorpId){
    // 货币对
    var tranCurPairBaseId = $("#tranCurPairBaseId").val();
    var tranCurPairReferId = $("#tranCurPairReferId").val();
    // 报价方式
    var quotationCurId = $("#quotationCurId").val();
    var quotationReferCurId = $("#quotationReferCurId").val();
    // 合同汇率
    var contractRate = moneyType.get("contractRate").val();
    // 即期汇率
    var spotRate = moneyType.get("spotRate").val();
    // 购买币种
    var buyCurId = $("#buyCurId").val();
    var busDirection = defDusDirection || dicts.get("busDirection").val();
    // 损益币种
    var profitLossCur = defProfitLossCur || selectType.get("profitLossCur").val();
    var corpId = defCorpId || $('input[name="corpId"]').val();
    if (contractRate && profitLossCur && busDirection && buyCurId) {
        var calculateData = {
            rateTypeId: $("#rateTypeId").val(),corpId: corpId,isShowConvert:$("#isShowConvert").val(),
            tranCurPairBaseId: tranCurPairBaseId,tranCurPairReferId:tranCurPairReferId,
            quotationCurId:quotationCurId,quotationReferCurId:quotationReferCurId,busDirection:busDirection,
            contractRate:contractRate,spotRate:spotRate,profitLossCur:profitLossCur,
            buyCurId:buyCurId,buyMoney:moneyType.get("buyMoney").val(),sellMoney:moneyType.get("sellMoney").val()
        }
        $.ajax({
            "url": basePath + "/foreign/fexProfit/lossManage/calculateFexProfitLoss",
            data: JSON.stringify(calculateData),
            type:"post",
            dataType:"json",
            contentType: "application/json",
            async: false,
            success:function (result) {
                if (result.success) {
                    var spotRate = moneyType.get("spotRate").val();
                    if (!spotRate) {
                        moneyType.get("spotRate").val(result.obj.spotRate);
                    }
                    moneyType.get('profitLossMoney').val(result.obj.profitLossMoney);
                    if ("Y" == $("#isShowConvert").val()) {
                        moneyType.get('convertMoney').val(result.obj.convertLossMoney);
                        moneyType.get('convertRate').val(result.obj.convertRate);
                    }
                }
            }
        });
    }
}

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("money").val(data);
}

function setQuotationMethod(curId1,curId2){
    var rateTypeId = $("#rateTypeId").val();
    var settleDate = dateType.get("settleDate").val();

    $.ajax({
        "url": basePath + "/foreign/fexrateinfo/getByCurId",
        data:{
            curId1: curId1,
            curId2: curId2,
            rateTypeId:rateTypeId,
            exchangeDate: bt.date.format(settleDate, 'yyyy-MM-dd')
        },
        type:"post",
        dataType:"json",
        async: false,
        success:function (result) {
            if (result.success) {
                var quotationMethodDesc = $("#quotationMethodDesc");
                if (quotationMethodDesc) {
                    quotationMethodDesc.hide();
                }

                if (null == result.obj) {
                    $("#quotationMethod").val("");
                    $("#quotationCurId").val("");
                    $("#quotationReferCurId").val("");
                    $("#quotationMethod").css("display","none");
                } else {
                    $("#quotationMethod").css("display","");
                    $("#quotationMethod").val(result.obj.curEnCode+"/"+result.obj.referCurEnCode);
                    $("#quotationCurId").val(result.obj.cur);
                    $("#quotationReferCurId").val(result.obj.referCur);
                    if ("spot" == $("#type").val()) {
                        var profitLossCur = selectType.get("profitLossCur").val();
                        if (profitLossCur == result.obj.cur) {
                            moneyType.get("spotRate").val(result.obj.purchaseRate);
                        } else {
                            moneyType.get("spotRate").val(result.obj.sellRate);
                        }
                    }
                }
            } else if ("FEX_RATE_INFO_IS_NULL" == result.messageEnum) {
                parent.layer.alert(result.msg, {icon: 3});
                var quotationMethodDesc = $("#quotationMethodDesc");
                if (quotationMethodDesc) {
                    quotationMethodDesc.show();
                }
                return;
            }
        }
    });
}


function checkDate(){
    var tranAddrId = $("#tranAddrId").val();
    var proId = selectType.get("proId").getData().value;
    var settleDate = dateType.get("settleDate").val();
    if(tranAddrId && proId && settleDate){
        $.ajax({
            "url": basePath + "/foreign/fexTradingLifecycle/checkWorkDate",
            data:{
                tranAddrId:tranAddrId,
                proId:proId,
                settleDate:settleDate
            },
            type:"post",
            dataType:"json",
            async: false,
            success:function (data) {
                if (data.success) {
                    if (false == data.obj) {
                        dateType.get("settleDate").val('');
                        parent.layer.alert(bt.lang("fex.settle.date.not.work"), {icon: 7});
                    }
                }
            }
        });
    }
}
