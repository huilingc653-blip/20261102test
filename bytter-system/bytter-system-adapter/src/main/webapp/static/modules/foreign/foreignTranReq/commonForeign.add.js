var selectType,dicts,moneyType,moneyFun,dateType,form,sellMoneyCallback;
var syncingBudgetAndSellMoney = false;

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

bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType','corp',
    'btUpload','ratioType','allBankInfo'], function(){
    var $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    var dateFun = bt.dateType.getFun();
    bt.allBankInfo.init();

    //结售汇属性回调函数
    bt.tranGroupCallback1 = function (data, $elem){
        var tranGroup = bt.dicts.get('tranGroup1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('tranGroup2').val(tranGroup);
        }
    };
    bt.tranGroupCallback2 = function (data, $elem){
        var tranGroup = bt.dicts.get('tranGroup2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('tranGroup1').val(tranGroup);
        }
    };

    // 外汇交易类型回调
    bt.swapFexTranTypeCallback1 = function (data){
          $("#fexTranType1").val(data.value);
          setForwardContractRate();
    }
    bt.swapFexTranTypeCallback2 = function (data){
        $("#fexTranType2").val(data.value);
        setForwardContractRate();
    }


    bt.busDirectionCallback1 = function(){
        var data = selectType.get("tranCurPairId1").getData().obj;
        var busDirection = dicts.get("busDirection1").val();
        var isSwapTran = $("#isSwapTran").val();
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
                $("#buyCurId1").val(curId1);
                $("#buyCurName1").val(curName1);
                $("#sellCurId1").val(curId2);
                $("#sellCurName1").val(curName2);
            }else{
                $("#buyCurId1").val(curId2);
                $("#buyCurName1").val(curName2);
                $("#sellCurId1").val(curId1);
                $("#sellCurName1").val(curName1);
            }
            if(isSwapTran=='1'){
                if(busDirection=="1"){
                    dicts.get("busDirection2").val("2");
                }else{
                    dicts.get("busDirection2").val("1");
                }
                $("#buyCurId2").val($("#sellCurId1").val());
                $("#buyCurName2").val($("#sellCurName1").val());
                $("#sellCurId2").val($("#buyCurId1").val());
                $("#sellCurName2").val($("#buyCurName1").val());
            }
            fillForwardSpotRate("1", true);
            if(isSwapTran=='1'){
                fillForwardSpotRate("2", true);
            }
        }

        var proId = selectType.get("proId").getData().value;
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: $('#buyCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: $('#sellCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId1").val(),proId,"1","0", $("#cotpId1").val());
        renderBankAcc("1", $("#cotpId1").val(), $("#buyCurId1").val(),proId,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId1").val(),proId,"1","1", $("#cotpId1").val());
        renderBankAcc("1", $("#cotpId1").val(), $("#sellCurId1").val(),proId,"1","0", $('input[name="corpId"]').val());
        //calContractRate(1);
        if (isSwapTran == "1") {
            calContractRate(2);
            //重新加载收款账户信息
            reloadSelectTableSafe("corpBankAccTable2", "corpBankAcc2", {curId: $('#buyCurId2').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
            //重新加载付款账户信息
            reloadSelectTableSafe("srcBankAccTable2", "srcBankAcc2", {curId: $('#sellCurId2').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId2").val(),proId,"2","0", $("#cotpId2").val());
            renderBankAcc("1", $("#cotpId2").val(), $("#buyCurId2").val(),proId,"2","1", $('input[name="corpId"]').val());
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId2").val(),proId,"2","1", $("#cotpId2").val());
            renderBankAcc("1", $("#cotpId2").val(), $("#sellCurId2").val(),proId,"2","0", $('input[name="corpId"]').val());
        }
        renderBudget();
        form.render();
    }
    //保证金币种回调函数
    bt.bondCurIdCallback1 = function (data, $elem){
        var bondCurId = bt.dicts.get('bondCurId1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('bondCurId2').val(bondCurId);
        }
    };
    bt.bondCurIdCallback2 = function (data, $elem){
        var bondCurId = bt.dicts.get('bondCurId2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('bondCurId1').val(bondCurId);
        }
    };

    bt.rateType.init();
    bt.ratioType.init();

    //资金性质回调函数
    bt.accNatureCallback1 = function(data){
        var accNature = bt.dicts.get("accNature1").val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("accNature2").val(accNature);
        }
    }
    bt.accNatureCallback2 = function(data){
        var accNature = bt.dicts.get("accNature2").val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("accNature1").val(accNature);
        }
    }

    function setBoodInfoChange(boodType, swapType){
        if ("5" == boodType || !boodType|| !boodType.trim()) {
            $(".boodDiv").hide();
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
            $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
            bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
            selectType.get('bondCurId'+swapType).attr('data-required', false);
            moneyType.get('payMoney'+swapType).removeAttr('lay-verify');
            setLayVerifyWithStrlength($('#boodRmk'+swapType), false);
            return;
        } else {
            $(".boodDiv").show();
        }
        if(boodType == "3"){
            setLayVerifyWithStrlength($('#boodRmk'+swapType), true, 'required');
        }else{
            setLayVerifyWithStrlength($('#boodRmk'+swapType), false);
        }
        if(boodType == "4"){
            moneyType.get('payMoney'+swapType).attr('lay-verify', 'required|number2');
            $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
            selectType.get('bondCurId'+swapType).attr('data-required', false);
        }else{
            moneyType.get('payMoney'+swapType).attr('lay-verify', 'number2');
            var deliveryDeadline = $('#deliveryDeadline'+swapType).val();
            if(boodType=="9"){
                bt.dicts.get('boodType' + swapType).setRequired(false);
                bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
            }else if(deliveryDeadline>0){
                bt.dicts.get('boodType'+swapType).setRequired(true);
                bt.ratioType.get('boodRatio'+swapType).currentObject.attr('lay-verify', 'required');
            }
            if(deliveryDeadline>0 && boodType != "3" && boodType !="9") {
                $('#boodAcc'+swapType).attr('lay-verify', 'required|tableData');
                bt.dicts.get('boodBankNote'+swapType).attr({'data-required': true});
                selectType.get('bondCurId'+swapType).attr('data-required', true);
            }else{
                $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
                bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
                selectType.get('bondCurId'+swapType).attr('data-required', false);
            }
        }
    }

    //保证金方式回调函数
    bt.boodTypeCallback1 = function (data, $elem){
        var boodType = bt.dicts.get('boodType1').val();
        setBoodInfoChange(boodType, "1");
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodType2').val(boodType);
            setBoodInfoChange(boodType, "2");
        }
        form.renderIcon();
    };
    bt.boodTypeCallback2 = function (data, $elem){
        var boodType = bt.dicts.get('boodType2').val();
        setBoodInfoChange(boodType, "2");
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodType1').val(boodType);
            setBoodInfoChange(boodType, "1");
        }
        form.renderIcon();
    };
    //结汇资金用途回调函数
    bt.purposeCallback1 = function (data, $elem){
        var purpose = bt.dicts.get('purpose1').val();
        if(purpose == "005" || purpose == "006" || purpose == "099"){
            setLayVerifyWithStrlength($('#purposeDtl1'), true, 'required');
        }else{
            setLayVerifyWithStrlength($('#purposeDtl1'), false);
        }
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('purpose2').val(purpose);
            if(purpose == "005" || purpose == "006" || purpose == "099"){
                setLayVerifyWithStrlength($('#purposeDtl2'), true, 'required');
            }else{
                setLayVerifyWithStrlength($('#purposeDtl2'), false);
            }
        }
        form.renderIcon();
    };
    bt.purposeCallback2 = function (data, $elem){
        var purpose = bt.dicts.get('purpose2').val();
        if(purpose == "005" || purpose == "006" || purpose == "099"){
            setLayVerifyWithStrlength($('#purposeDtl2'), true, 'required');
        }else{
            setLayVerifyWithStrlength($('#purposeDtl2'), false);
        }
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('purpose1').val(purpose);
            if(purpose == "005" || purpose == "006" || purpose == "099"){
                setLayVerifyWithStrlength($('#purposeDtl1'), true, 'required');
            }else{
                setLayVerifyWithStrlength($('#purposeDtl1'), false);
            }
        }
        form.renderIcon();
    };

    //结售汇属性回调函数
    bt.fexPropCallback1 = function (data, $elem){
        var fexProp = bt.dicts.get('fexProp1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fexProp2').val(fexProp);
        }
    };
    bt.fexPropCallback2 = function (data, $elem){
        var fexProp = bt.dicts.get('fexProp2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fexProp1').val(fexProp);
        }
    };

    //资金来源回调函数
    bt.fundsSourceCallback1 = function (data, $elem){
        var fundsSource = bt.dicts.get('fundsSource1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fundsSource2').val(fundsSource);
        }
    };
    bt.fundsSourceCallback2 = function (data, $elem){
        var fundsSource = bt.dicts.get('fundsSource2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fundsSource1').val(fundsSource);
        }
    };

    //保证金钞汇标识回调函数
    bt.boodBankNoteCallback1 = function (data, $elem){
        var boodBankNote = bt.dicts.get('boodBankNote1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodBankNote2').val(boodBankNote)
        }
    };
    bt.boodBankNoteCallback2 = function (data, $elem){
        var boodBankNote = bt.dicts.get('boodBankNote2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodBankNote1').val(boodBankNote)
        }
    };
    // 钞汇标识回调
    bt.bankNoteCallback1 = function (data){
        if ("1" == data.value) {
            bt.dicts.get('isPayed1').attr({'data-disabled': true});
            bt.dicts.get('isPayed1').val("1");
        } else {
            bt.dicts.get('isPayed1').attr({'data-disabled': false});
        }
    }

    //固定金额回调函数
    bt.fixMoneyCallback1 = function(data){
        //设置买入/卖出金额
        setMoneyInfo("1");
        renderBudget();
        calContractRate("1");
    };

    dicts = bt.dicts.init();

    function reloadSelectTableSafe(tableKey, inputId, params) {
        try {
            var table = selectTableObj[tableKey];
            if (!table || typeof table.get !== "function") {
                return;
            }
            table.get(inputId).reload(params);
        } catch (e) {}
    }

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
                        $('#corpContact1').val("");
                        $('#corpContactTel1').val("");
                        $('#corpContact2').val("");
                        $('#corpContactTel2').val("");
                        //重新加载联系人
                        selectTableObj["corpContactTable1"].get("corpContact1").reload({corpId: id});
                        selectTableObj["corpContactTable1"].get("corpContact2").reload({corpId: id});
                        moneyType.get('sellMoney1').val('');
                        moneyType.get('sellMoney2').val('');
                        //现金流
                        $("#srcCorpNearTd").text(data.obj.name);
                        $("#srcCorpRemoteTd").text(data.obj.name);
                        $("#targetCorpNearTd").text(data.obj.name);
                        $("#targetCorpRemoteTd").text(data.obj.name);
                    }
                    $("#corpBaseCur").val(data.obj.curId);
                    //加载资金计划
                    renderBudget();
                }
                form.render();
            }
        });
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: $('#sellCurId1').val(), corpId: id});
        reloadSelectTableSafe("srcBankAccTable2", "srcBankAcc2", {curId: $('#sellCurId2').val(), corpId: id});
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: $('#buyCurId1').val(), corpId: id});
        reloadSelectTableSafe("corpBankAccTable2", "corpBankAcc2", {curId: $('#buyCurId2').val(), corpId: id});
        //重新加载保证金账户信息
        selectTableObj["boodAccTable1"].get("boodAcc1").reload({curId: bt.selectType.get('bondCurId1').val(), corpId: id});
        reloadSelectTableSafe("boodAccTable2", "boodAcc2", {curId: bt.selectType.get('bondCurId2').val(), corpId: id});
        //加载主体单位和交易对手账户信息
        //买入-收款
        renderBankAcc("0", id, $("#buyCurId1").val(),selectType.get("proId").val(),"1","0", $("#cotpId1").val());
        renderBankAcc("0", id, $("#buyCurId2").val(),selectType.get("proId").val(),"2","0", $("#cotpId2").val());
        //卖出-付款
        renderBankAcc("0", id, $("#sellCurId1").val(),selectType.get("proId").val(),"1","1", $("#cotpId1").val());
        renderBankAcc("0", id, $("#sellCurId2").val(),selectType.get("proId").val(),"2","1", $("#cotpId2").val());
    };
    //初始化单位
    var corp = bt.corp.init();



    //交易对手银行账户回调函数
    bt.cotpBankCallback1 = function (data, dom) {
        if (!isEmpty(data.type)) {
            $("#bankTypeId").val(data.bankTypeId);
            if (data.type === "innerBank") {
                $("#oppId1").val(data.id);
                $("#cotpName1").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode1").val(data.swiftCode);
                    $("#cotpBicCodeDiv1").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv1").text('');
                }
                $("#srcCotpNearTd").text(data.bankName);
                $("#srcCotpBankNearTd").text(data.bankName);
                $("#targetCotpNearTd").text(data.bankName);
                $("#targetCotpBankNearTd").text(data.bankName);
                dicts.get("fixedMoney1").attr({"data-disabledValue": ""});
            } else if (data.type === "outerBank") {
                $("#oppId1").val(data.id);
                $("#cotpName1").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode1").val(data.swiftCode);
                    $("#cotpBicCodeDiv1").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv1").text('');
                }
                $("#srcCotpNearTd").text(data.bankName);
                $("#srcCotpBankNearTd").text(data.bankName);
                $("#targetCotpNearTd").text(data.bankName);
                $("#targetCotpBankNearTd").text(data.bankName);
                //越南工行需求：2.外汇即期交易，校验【交易对手银行】字段，若对应的【银行类型】为中国工商银行/工商银行且【国家/地区】中文名称为越南时，固定金额字段默认卖出金额不能修改
                if ($('#type').val() == 'spot' && data.bankTypeName && data.bankTypeName.indexOf('工商银行') != -1
                    && data.nameZh && data.nameZh.indexOf('越南') != -1) {
                    dicts.get("fixedMoney1").attr({"data-disabledValue": "1","data-defaultvalue": "2"});
                } else {
                    dicts.get("fixedMoney1").attr({"data-disabledValue": ""});
                }
            }
        } else {
            $("#oppId1").val("");
            $("#cotpName1").val("");
            $("#cotpBicCode1").val("");
            $("#cotpBicCodeDiv1").text("");
            $("#srcCotpNearTd").text("");
            $("#srcCotpBankNearTd").text("");
            $("#targetCotpNearTd").text("");
            $("#targetCotpBankNearTd").text("");
        }
        //新交易对手ID
        var oppId = data.id;
        if (isEmpty(oppId)) {
            oppId = null;
        }

        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: $('#buyCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: $('#sellCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});


        //加载主体单位和交易对手账户信息
        renderBankAcc("1", oppId, $("#buyCurId1").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        renderBankAcc("1", oppId, $("#sellCurId1").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId1").val(),selectType.get("proId").val(),"1","0", oppId);
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId1").val(),selectType.get("proId").val(),"1","1", oppId);

        if("1" == $("#isSwapTran").val()){
            //同步写入远端的交易对手
            $("#oppId2").val(data.id);
            $("#cotpName2").val(data.bankName);
            $("#srcCotpRemoteTd").text(data.bankName);
            $("#srcCotpBankRemoteTd").text(data.bankName);
            $("#targetCotpRemoteTd").text(data.bankName);
            $("#targetCotpBankRemoteTd").text(data.bankName);
            if (!isEmpty(data.swiftCode)) {
                $("#cotpBicCode2").val(data.swiftCode);
                $("#cotpBicCodeDiv2").text(data.swiftCode);
            } else {
                $("#cotpBicCodeDiv2").text('');
            }

            //加载主体单位和交易对手账户信息
            renderBankAcc("1", oppId, $("#buyCurId2").val(),selectType.get("proId").val(),"2","1", $('input[name="corpId"]').val());
            renderBankAcc("1", oppId, $("#sellCurId2").val(),selectType.get("proId").val(),"2","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId2").val(),selectType.get("proId").val(),"2","0", oppId);
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId2").val(),selectType.get("proId").val(),"2","1", oppId);
        }
    }

    bt.cotpBankCallback2 = function (data, dom) {
        if (!isEmpty(data.type)) {
            $("#bankTypeId").val(data.bankTypeId);
            if (data.type === "innerBank") {
                $("#oppId2").val(data.id);
                $("#cotpName2").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode2").val(data.swiftCode);
                    $("#cotpBicCodeDiv2").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv2").text('');
                }
                $("#srcCotpRemoteTd").text(data.bankName);
                $("#srcCotpBankRemoteTd").text(data.bankName);
                $("#targetCotpRemoteTd").text(data.bankName);
                $("#targetCotpBankRemoteTd").text(data.bankName);
            } else if (data.type === "outerBank") {
                $("#oppId2").val(data.id);
                $("#cotpName2").val(data.bankName);
                if (!isEmpty(data.swiftCode)) {
                    $("#cotpBicCode2").val(data.swiftCode);
                    $("#cotpBicCodeDiv2").text(data.swiftCode);
                } else {
                    $("#cotpBicCodeDiv2").text('');
                }
                $("#srcCotpRemoteTd").text(data.bankName);
                $("#srcCotpBankRemoteTd").text(data.bankName);
                $("#targetCotpRemoteTd").text(data.bankName);
                $("#targetCotpBankRemoteTd").text(data.bankName);
            }
        } else {
            $("#oppId2").val("");
            $("#cotpName2").val("");
            $("#cotpBicCode2").val("");
            $("#cotpBicCodeDiv2").text("");
            $("#srcCotpRemoteTd").text("");
            $("#srcCotpBankRemoteTd").text("");
            $("#targetCotpRemoteTd").text("");
            $("#targetCotpBankRemoteTd").text("");
        }
        //新交易对手ID
        var oppId = data.id;
        if (isEmpty(oppId)) {
            oppId = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("1", oppId, $("#buyCurId2").val(),selectType.get("proId").val(),"2","1", $('input[name="corpId"]').val());
        renderBankAcc("1", oppId, $("#sellCurId2").val(),selectType.get("proId").val(),"2","0", $('input[name="corpId"]').val());

        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId2").val(),selectType.get("proId").val(),"2","0", oppId);
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId2").val(),selectType.get("proId").val(),"2","1", oppId);

        if("1" == $("#isSwapTran").val()){
            //同步写入近端的交易对手
            $("#oppId1").val(data.id);
            $("#cotpName1").val(data.bankName);
            $("#srcCotpNearTd").text(data.bankName);
            $("#srcCotpBankNearTd").text(data.bankName);
            $("#targetCotpNearTd").text(data.bankName);
            $("#targetCotpBankNearTd").text(data.bankName);
            if (!isEmpty(data.swiftCode)) {
                $("#cotpBicCode1").val(data.swiftCode);
                $("#cotpBicCodeDiv1").text(data.swiftCode);
            } else {
                $("#cotpBicCodeDiv1").text('');
            }

            //加载主体单位和交易对手账户信息
            renderBankAcc("1", oppId, $("#buyCurId1").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
            renderBankAcc("1", oppId, $("#sellCurId1").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId1").val(),selectType.get("proId").val(),"1","0", oppId);
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId1").val(),selectType.get("proId").val(),"1","1", oppId);
        }
    }


    //买入/卖出金额等设置
    function setMoneyInfo(){
        var fixedMoney = bt.dicts.get('fixedMoney1').val();
        var tranRateType = bt.dicts.get('tranRateType1').val();
        if("1" == $("#isSwapTran").val()){
            bt.dicts.get('fixedMoney2').attr({'data-disabled': true});
            if("1" == fixedMoney) {
                bt.dicts.get('fixedMoney2').val("2");
            } else {
                bt.dicts.get('fixedMoney2').val("1");
            }
        }
        if ("2" == tranRateType) {
            moneyType.get("buyMoney1").attr('lay-verify', 'required');
            moneyType.get("sellMoney1").attr('lay-verify', 'required');
            moneyType.get("buyMoney1").removeAttr("readonly");
            moneyType.get("sellMoney1").removeAttr("readonly");
            moneyType.get("contractRate1").attr("lay-verify", "required");
            moneyType.get("spotRate1").attr("lay-verify", "required");
            if("1" == $("#isSwapTran").val()){
                moneyType.get("buyMoney2").attr('lay-verify', 'required');
                moneyType.get("sellMoney2").attr('lay-verify', 'required');
                moneyType.get("buyMoney2").removeAttr("readonly");
                moneyType.get("sellMoney2").removeAttr("readonly");
            }
        } else if ("1" == fixedMoney) { // 即期 && 买入 && 交易汇率类型=合同汇率
            moneyType.get("buyMoney1").attr('lay-verify', 'required');
            moneyType.get("buyMoney1").removeAttr("readonly");
            moneyType.get("sellMoney1").removeAttr('readonly');
            moneyType.get("sellMoney1").removeAttr("lay-verify");
            moneyType.get("contractRate1").removeAttr("lay-verify");
            moneyType.get("spotRate1").removeAttr("lay-verify");
            if("1" == $("#isSwapTran").val()){
                moneyType.get("buyMoney2").attr('readonly', true);
                moneyType.get("buyMoney2").removeAttr("lay-verify");
                moneyType.get("sellMoney2").attr('lay-verify', 'required');
                moneyType.get("sellMoney2").removeAttr("readonly");
            }
        } else if ("2" == fixedMoney) { // 即期 && 卖出
            moneyType.get("buyMoney1").removeAttr('readonly');
            moneyType.get("buyMoney1").removeAttr("lay-verify");
            moneyType.get("sellMoney1").attr('lay-verify', 'required');
            moneyType.get("sellMoney1").removeAttr("readonly");
            moneyType.get("contractRate1").removeAttr("lay-verify");
            moneyType.get("spotRate1").removeAttr("lay-verify");
            if("1" == $("#isSwapTran").val()){
                moneyType.get("buyMoney2").attr('lay-verify', 'required');
                moneyType.get("buyMoney2").removeAttr("readonly");
                moneyType.get("sellMoney2").removeAttr('readonly');
                moneyType.get("sellMoney2").removeAttr("lay-verify");
            }
        } else {
            moneyType.get("buyMoney1").removeAttr("lay-verify");
            moneyType.get("sellMoney1").removeAttr("lay-verify");
            moneyType.get("buyMoney1").removeAttr("readonly");
            moneyType.get("sellMoney1").removeAttr("readonly");

            moneyType.get("buyMoney2").removeAttr("lay-verify");
            moneyType.get("sellMoney2").removeAttr("lay-verify");
            moneyType.get("contractRate1").removeAttr("lay-verify");
            moneyType.get("spotRate1").removeAttr("lay-verify");

            moneyType.get("buyMoney2").removeAttr("readonly");
            moneyType.get("sellMoney2").removeAttr("readonly");
        }

        $("#srcNearTr").hide();
        $("#targetNearTr").hide();
        $("#srcRemoteTr").hide();
        $("#targetRemoteTr").hide();
        if(fixedMoney == "1") {
            $("#srcNearTr").show();
            if ("2" == tranRateType) {
                $("#targetNearTr").show();
            }
            if("1" == $("#isSwapTran").val()){
                fixedMoney = bt.dicts.get('fixedMoney2').val();
                if (fixedMoney == "1") {
                    $("#srcRemoteTr").show();
                    if ("2" == tranRateType) {
                        $("#targetRemoteTr").show();
                    }
                } else {
                    $("#targetRemoteTr").show();
                    if ("2" == tranRateType) {
                        $("#srcRemoteTr").show();
                    }
                }
            }
        } else if(fixedMoney == "2") {
            $("#targetNearTr").show();
            if ("2" == tranRateType) {
                $("#srcNearTr").show();
            }
            if("1" == $("#isSwapTran").val()){
                fixedMoney = bt.dicts.get('fixedMoney2').val();
                if (fixedMoney == "1") {
                    $("#srcRemoteTr").show();
                    if ("2" == tranRateType) {
                        $("#targetRemoteTr").show();
                    }
                } else {
                    $("#targetRemoteTr").show();
                    if ("2" == tranRateType) {
                        $("#srcRemoteTr").show();
                    }
                }
            }
        }
        form.renderIcon();
    }

    /** 掉期远端外汇业务类型/交割类型：必须在 setCur 之前写入，避免账户下拉 destroy 报 clientWidth 后中断 */
    function fillSwapFarProductFields(data, fexTranType) {
        if ("1" != $("#isSwapTran").val() && fexTranType != "2" && data.trantype != "9") {
            return;
        }
        $("#fexBusType2").val(data.fexbustype);
        if (data.fexbustype == "1") {
            $("#fexBusType2").val("2");
        } else if (data.fexbustype == "2") {
            $("#fexBusType2").val("1");
        }
        $("#deliveryType2").val(data.deliverytype);
        $("#fexBusTypeDiv2").text(bt.dict("fexProduct.fexBusType", $("#fexBusType2").val()));
        $("#deliveryTypeDiv2").text(bt.dict("fexProduct.deliveryType", data.deliverytype));
        if ($("#fexBusType2").val() == "2") {
            if (bt.dicts.get("autoCal2")) {
                bt.dicts.get("autoCal2").val("2");
            }
            if (bt.dicts.get("fexProp2")) {
                bt.dicts.get("fexProp2")['fex.tranApp.fexProp'] = null;
                bt.dicts.get("fexProp2").attr({'data-group': 'fex.tranApp.fexProp2'});
            }
        } else {
            if (bt.dicts.get("autoCal2")) {
                bt.dicts.get("autoCal2").val("1");
            }
            if (bt.dicts.get("fexProp2")) {
                bt.dicts.get("fexProp2")['fex.tranApp.fexProp2'] = null;
                bt.dicts.get("fexProp2").attr({'data-group': 'fex.tranApp.fexProp'});
            }
        }
        if (data.fextrantyperemote == "1") {
            $("#exposureBusType2").val("2");
        } else {
            $("#exposureBusType2").val("1");
        }
        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull2").hide();
            $("#deliveryCurTd2").show();
            $("#deliveryCurDiv2").show();
        } else {
            $("#deliveryCurTdNull2").show();
            $("#deliveryCurTd2").hide();
            $("#deliveryCurDiv2").hide();
        }
        if ($("#fexBusType2").val() == "1") {
            $("#settleBusTypeTdNull2").hide();
            $("#settleBusTypeTd2").show();
            $("#settleBusTypeDiv2").show();
            if (bt.dicts.get('settleBusType2')) {
                bt.dicts.get('settleBusType2').attr({'data-required': true});
            }
            $("#purposeTr2").show();
        } else {
            $("#settleBusTypeTdNull2").show();
            $("#settleBusTypeTd2").hide();
            $("#settleBusTypeDiv2").hide();
            if (bt.dicts.get('settleBusType2')) {
                bt.dicts.get('settleBusType2').attr({'data-required': false});
            }
            $("#purposeTr2").hide();
            if (bt.dicts.get('purpose2')) {
                bt.dicts.get('purpose2').val('');
            }
            setLayVerifyWithStrlength($('#purposeDtl2'), false);
            $('#purposeDtl2').val('');
        }
    }

    //产品回调函数
    bt.proCallback = function (data) {//买入/卖出金额等设置
        var fexTranType = data.fextrantype;
        $("#fexBusType1").val(data.fexbustype);
        // 即期是否自动交割
        $("#isSpotAutoDelivery").val(data.isautodelivery);
        fillSwapFarProductFields(data, fexTranType);
        try {
            setCur();
        } catch (e) {}
        setProfitLoss();
        //所选产品为结汇时，默认自动计算买入金额；所选产品为购汇时，默认自动计算卖出金额
        if($("#fexBusType1").val() == "2"){//购汇
            //结售汇属性设置为购汇的字典
            bt.dicts.get("fexProp1")['fex.tranApp.fexProp'] = null;
            bt.dicts.get("fexProp1").attr({'data-group': 'fex.tranApp.fexProp2'});
        } else {
            //结售汇属性设置为结汇的字典
            bt.dicts.get("fexProp1")['fex.tranApp.fexProp2'] = null;
            bt.dicts.get("fexProp1").attr({'data-group': 'fex.tranApp.fexProp'});
        }
        if(fexTranType !="2"){
            $("#fexTranType1").val(data.fextrantype);
            $(".fexTranTypeDiv1").text(bt.dict("fexProduct.fexTranType", data.fextrantype));
        }

        $("#deliveryType1").val(data.deliverytype);
        $("#fexBusTypeDiv1").text(bt.dict("fexProduct.fexBusType", data.fexbustype));
        $("#deliveryTypeDiv1").text(bt.dict("fexProduct.deliveryType", data.deliverytype));
        if(data.fextrantype=="1"){ // 远期
            $("#exposureBusType1").val("2");
        }else{ // 近期
            $("#exposureBusType1").val("1");
        }
        // 交割币种
        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull1").hide();
            $("#deliveryCurTd1").show();
            $("#deliveryCurDiv1").show();
        } else {
            $("#deliveryCurTdNull1").show();
            $("#deliveryCurTd1").hide();
            $("#deliveryCurDiv1").hide();
        }

        //结汇
        if(data.fexbustype=="1"){
            $("#settleBusTypeTdNull1").hide();
            $("#settleBusTypeTd1").show();
            $("#settleBusTypeDiv1").show();
            bt.dicts.get('settleBusType1').attr({'data-required': true});
            $("#purposeTr1").show();
        }else{
            $("#settleBusTypeTdNull1").show();
            $("#settleBusTypeTd1").hide();
            $("#settleBusTypeDiv1").hide();
            bt.dicts.get('settleBusType1').attr({'data-required': false});
            $("#purposeTr1").hide();
            bt.dicts.get('purpose1').val('');
            setLayVerifyWithStrlength($('#purposeDtl1'), false);
            $('#purposeDtl1').val('');
        }
        //设置买入/卖出金额
        setMoneyInfo("1");
        try {
            if (data.buycurid && data.sellcurid) {
                getCurrencyPair(data.buycurid, data.sellcurid);
            }
        } catch (e) {}
        renderBudget();
        form.renderIcon();
        form.render();
    };

    /** 近端合同汇率写入远端：远端为空时才覆盖，避免冲掉已填的远端汇率 */
    function syncSwapContractRateFromNear() {
        if ("1" != $("#isSwapTran").val()) {
            return;
        }
        var nearRate = moneyType.get("contractRate1").val();
        var farRate = moneyType.get("contractRate2").val();
        if (nearRate && !farRate) {
            moneyType.get("contractRate2").val(nearRate);
        }
    }

    function setForwardContractRate(){
        var tranRateType = bt.dicts.get('tranRateType2').val();
        var swapFexTranType = $("#swapFexTranType2").val();
        if ("2" == tranRateType) {
            moneyType.get("contractRate2").attr("lay-verify", "required");
            if ("0" == swapFexTranType) {
                moneyType.get("spotRate2").attr("lay-verify", "required");
            } else {
                moneyType.get("spotRate2").removeAttr("lay-verify");
            }
        } else {
            moneyType.get("contractRate2").removeAttr("lay-verify");
            moneyType.get("spotRate2").removeAttr("lay-verify");

            moneyType.get("sellMoney2").removeAttr("readonly");
            moneyType.get("buyMoney2").removeAttr("readonly");
        }
        form.renderIcon();
    }


    // 交易汇率类型回调
    bt.transExchangeRateType1 = function(data){
        var tranRateType = bt.dicts.get('tranRateType1').val();
        if ("2" == tranRateType) {
            moneyType.get("contractRate1").attr("lay-verify", "required");
            moneyType.get("spotRate1").attr("lay-verify", "required");
            getSpotRate("1");
        } else {
            moneyType.get("contractRate1").removeAttr("lay-verify");
            moneyType.get("spotRate1").removeAttr("lay-verify");

            moneyType.get("sellMoney1").removeAttr("readonly");
            moneyType.get("buyMoney1").removeAttr("readonly");
        }
        if("1" == $("#isSwapTran").val()){
            bt.dicts.get('tranRateType2').val(tranRateType);
            setForwardContractRate();
        }
        setMoneyInfo("1");

        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }

    // 交易汇率类型回调
    bt.transExchangeRateType2 = function(data){
        var tranRateType = bt.dicts.get('tranRateType2').val();
        if ("2" == tranRateType) {
            getSpotRate("2");
        }
        setForwardContractRate();
        if ("spot" == $("#type").val()) {
            setProfitLoss2();
        }
    }

    // 合同汇率回调
    bt.contractRateCallback1 = function(data){
        syncSwapContractRateFromNear();
        var isPaymentAmount = bt.dicts.get('isPaymentAmount1').val(); // 是否自动计算
        var tranCurPairReferId = $("#tranCurPairReferId1").val();
        var fixedMoney = bt.dicts.get('fixedMoney1').val();
        var money  = moneyType.get("buyMoney1").val();
        var curId = $("#buyCurId1").val();
        if ("2" == fixedMoney) { // 固定金额等于卖出需要更新近端买入
            money = moneyType.get("sellMoney1").val();
            curId = $("#sellCurId1").val();
        }
        if ("1" != isPaymentAmount || !money) {
            return;
        }
        var contractRate = moneyType.get("contractRate1").val();
        var quotationCur = $("#quotationCurId1").val(); // 报价方式的左侧
        if (curId == quotationCur) {
            money = moneyFun.mul(money,contractRate,2);
        } else {
            money = moneyFun.div(money,contractRate,2);
        }
        if ("2" == fixedMoney) {
            moneyType.get("buyMoney1").val(money);
        } else {
            moneyType.get("sellMoney1").val(money);
        }
        syncBudgetMoneyFromSellMoney();

        if("1" == $("#isSwapTran").val()){
            // 设置远端买入金额
            setForwartBuyMoney();
            // 设置远端卖出金额
            setForwartSellMoney();
        }
        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }
    bt.contractRateCallback2 = function(data){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount2').val(); // 是否自动计算
        var tranCurPairReferId = $("#tranCurPairReferId2").val();
        var fixedMoney = bt.dicts.get('fixedMoney2').val();
        var money  = moneyType.get("buyMoney2").val();
        var curId = $("#buyCurId2").val();
        if (curId == tranCurPairReferId) { // 买入币种（远端）=货币对的右侧 更新买入金额
            money = moneyType.get("sellMoney2").val();
            curId = $("#sellCurId2").val();
        }
        if ("1" != isPaymentAmount || !money) {
            return;
        }
        var contractRate = moneyType.get("contractRate2").val();
        var quotationCur = $("#quotationCurId2").val(); // 报价方式的左侧
        if (curId == quotationCur) {
            money = moneyFun.mul(money,contractRate,2);
        } else {
            money = moneyFun.div(money,contractRate,2);
        }
        if ($("#buyCurId2").val() == tranCurPairReferId) {
            moneyType.get("buyMoney2").val(money);
        } else {
            moneyType.get("sellMoney2").val(money);
        }
        calContractRate("2");
    }

    // 即期汇率回调
    bt.spotRateCallback1= function(data){
        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }
    bt.spotRateCallback2= function(data){
        if ("spot" == $("#type").val()) {
            setProfitLoss2();
        }
    }

    // 清空货币对时跳过回调，避免 setCur 重入导致账户下拉表异常
    var skipCurrencyPairCallback = false;

    // 交易货币对回调
    bt.currencyPairCallback = function(data){
        if (skipCurrencyPairCallback) {
            return;
        }
        setCur();
        if ("spot" == $("#type").val()) {
            setProfitLoss();
        }
    }
    bt.currencyPairCallback2 = function(data){
        if (skipCurrencyPairCallback) {
            return;
        }
        setCur();
        if ("spot" == $("#type").val()) {
            setProfitLoss2();
        }
    }

    bt.profitLossCurCallback1 = function(data){
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType1').val();
        var corpBaseCur = $("#corpBaseCur").val();

        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery && corpBaseCur != data.id) {
            $(".baseConvertTr").show();
            $("#isShowConvert").val("Y");
            $("input[data-id='convertRate1']").attr("lay-verify", "required");
            $("input[data-id='convertMoney1']").attr("lay-verify", "required");
        } else {
            $(".baseConvertTr").hide();
            $("#isShowConvert").val("N");
            $("input[data-id='convertRate1']").removeAttr("lay-verify");
            $("input[data-id='convertMoney1']").removeAttr("lay-verify");
        }
        getBaseCuQuotationMethod("1");
        // 计算损益
        calculateFexProfitLoss();
    }

    bt.profitLossCurCallback2 = function(data){
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType2').val();
        var corpBaseCur = $("#corpBaseCur").val();

        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery && corpBaseCur != data.id) {
            $(".baseConvertTr").show();
            $("#isShowConvert").val("Y");
            $("input[data-id='convertRate2']").attr("lay-verify", "required");
            $("input[data-id='convertMoney2']").attr("lay-verify", "required");
        } else {
            $(".baseConvertTr").hide();
            $("#isShowConvert").val("N");
            $("input[data-id='convertRate2']").removeAttr("lay-verify");
            $("input[data-id='convertMoney2']").removeAttr("lay-verify");
        }
        getBaseCuQuotationMethod("2");
        // 计算损益
        calculateFexProfitLoss2();
    }
    // 损益回调
    bt.profitLossMoneyCallback1 = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney("1");
    }

    bt.profitLossMoneyCallback2 = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney("2");
    }

    // 折本位币汇率回调
    bt.convertRateCallback1 = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney("1");
    }

    bt.convertRateCallback2 = function(data){
        // 计算损益
        calculateFexLifeCycleConvertLossMoney("2");
    }

    // 设置损益表单
    function setProfitLoss(){
        if ("spot" != $("#type").val()) {
            return;
        }
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType1').val();
        var corpBaseCur = $("#corpBaseCur").val();
        var profitLossCur = selectType.get("profitLossCur1").val();
        if (!tranRateType) {
            return;
        }
        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery) {
            $("#spotTr").show();
            $("#profitLossTr").show();
            $("input[data-id='profitLossMoney1']").attr("lay-verify", "required");
            selectType.get("profitLossCur1").setAttr({"data-required": true});
        } else {
            $("#spotTr").hide();
            $("#profitLossTr").hide();
            $("input[data-id='profitLossMoney1']").removeAttr("lay-verify");
            selectType.get("profitLossCur1").setAttr({"data-required": false});
        }
        if (!profitLossCur) {
            return;
        }
        form.renderIcon();
        form.render();
        getBaseCuQuotationMethod("1");
        // 计算损益
        calculateFexProfitLoss();
    }

    // 设置损益表单
    function setProfitLoss2(){
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType2').val();
        var profitLossCur = selectType.get("profitLossCur2").val();
        if (!tranRateType) {
            return;
        }
        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery) {
            $("#spotTr").show();
            $("input[data-id='profitLossMoney2']").attr("lay-verify", "required");
        } else {
            $("#spotTr").hide();
            $("input[data-id='profitLossMoney2']").removeAttr("lay-verify");
        }
        if (!profitLossCur) {
            return;
        }
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery){
            $("#profitLossTr").show();
        } else {
            $("#profitLossTr").hide();
        }

        // 计算损益
        calculateFexProfitLoss2();
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
                    selectType.get("tranCurPairId1").val(result.obj.id);
                    setCur(result.obj);
                    if ("spot" == $("#type").val()) {
                        setProfitLoss();
                    }
                }
            }
        });
    }

    function setCur(tranCurPairIdData){
        var data = selectType.get("tranCurPairId1").getData().obj;
        var pro = selectType.get("proId").getData().obj;
        var proId = selectType.get("proId").getData().value;
        var busDirection1 = dicts.get("busDirection1").val();
        var isSwapTran = $("#isSwapTran").val();
        var defProfitAndLossCur = $("#defProfitAndLossCur").val();
        if (!pro || !proId) {
            selectType.get("tranCurPairId1").val("");
            layer.alert(bt.lang("fex.tranApp.pro.select"), {icon: 2});
            return;
        }
        if(data){
            var curId1 = data.baseCurId;
            var curName1 = data.baseEnglishCode;
            var curId2 = data.curId2;
            var curName2 = data.englishCode2;
            if(curId1==curId2){
                curId2 = data.curId1;
                curName2 = data.englishCode1;
            }
            // 无汇率时中止后续逻辑，避免清空货币对后仍继续赋值并重复 reload 账户下拉导致无法选择
            if (!setQuotationMethod("1",curId1,curId2)) {
                clearCurPairAndAccounts(isSwapTran);
                return;
            }
            // 选择货币对，设置交易对币种
            $("#tranCurPairBaseId1").val(curId1);
            $("#tranCurPairReferId1").val(curId2);
            $("#cur1").val(curId1);
            $("#curEn1").val(curName1);
            var profitLossCurId = null;
            if ("spot" == $("#type").val()) {
                // 设置损益币种
                if(defProfitAndLossCur=='1'){
                    selectType.get("profitLossCur1").attr({"data-displayData":curId1+","+curId2, "data-value":curId1});
                    profitLossCurId = curId1;
                }else if(defProfitAndLossCur=='2'){
                    selectType.get("profitLossCur1").attr({"data-displayData":curId1+","+curId2, "data-value":curId2});
                    profitLossCurId = curId2;
                }
            }
            // 买卖方向设置
            var tranCurPairBaseId = $("#tranCurPairBaseId1").val();
            var tranCurPairReferId = $("#tranCurPairReferId1").val();
            var corpBaseCur = $("#corpBaseCur").val();

            if ((tranCurPairBaseId == corpBaseCur || tranCurPairReferId == corpBaseCur) && ("1" == $("#fexBusType1").val() || "2" == $("#fexBusType1").val())) {
                dicts.get("busDirection1").attr({'data-disabled': true});
                if (isSwapTran == "1") {
                    dicts.get("busDirection2").attr({'data-disabled': true});
                }
            } else {
                dicts.get("busDirection1").attr({'data-disabled': false});
                if (isSwapTran == "1") {
                    dicts.get("busDirection2").attr({'data-disabled': false});
                }
            }

            // 外汇业务类型=结汇 && 单位本位币=交易货币对的左侧 -> 买卖方向=卖出 1-结汇、2-售汇、3-外汇买卖
            if ("1" == $("#fexBusType1").val() && tranCurPairBaseId == corpBaseCur) {
                dicts.get("busDirection1").val("1");
            } else if ("2" == $("#fexBusType1").val() && tranCurPairBaseId == corpBaseCur) { // 外汇业务类型=购汇 && 单位本位币=交易货币对的右侧 -> 买卖方向=买入
                dicts.get("busDirection1").val("2");
            } else if ("1" == $("#fexBusType1").val() && tranCurPairReferId == corpBaseCur) {
                dicts.get("busDirection1").val("2");
            } else if ("2" == $("#fexBusType1").val() && tranCurPairReferId == corpBaseCur) {
                dicts.get("busDirection1").val("1");
            }  else if(pro.sellCurId == tranCurPairBaseId && pro.buyCurId == tranCurPairReferId){
                dicts.get("busDirection1").val("2");
            }else {
                dicts.get("busDirection1").val("1");
            }
            busDirection1 = dicts.get("busDirection1").val();
            if(busDirection1=='1'){
                $("#buyCurId1").val(curId1);
                $("#buyCurName1").val(curName1);
                $("#sellCurId1").val(curId2);
                $("#sellCurName1").val(curName2);
            }else{
                $("#buyCurId1").val(curId2);
                $("#buyCurName1").val(curName2);
                $("#sellCurId1").val(curId1);
                $("#sellCurName1").val(curName1);
            }
            getBaseCuQuotationMethod("1", profitLossCurId);
            fillForwardSpotRate("1", true);

            //现金流信息
            $("#srcCurNearTd").text($("#buyCurName1").val());
            //现金流信息
            $("#targetCurNearTd").text($("#sellCurName1").val());

            calContractRate("1");

            if (isSwapTran == "1") {
                try {
                    selectType.get("tranCurPairId2").val(data.id);
                } catch (e) {}
                $("#currencyPairName2").val(data.name);
                $("#tranCurPairBaseId2").val(curId1);
                $("#tranCurPairReferId2").val(curId2);
                $("#cur2").val(curId1);
                $("#curEn2").val(curName1);
                if(busDirection1=="1"){
                    dicts.get("busDirection2").val("2");
                    $("#buyCurId2").val(curId1);
                    $("#buyCurName2").val(curName1);
                    $("#sellCurId2").val(curId2);
                    $("#sellCurName2").val(curName2);
                }else{
                    dicts.get("busDirection2").val("1");
                    $("#buyCurId2").val(curId2);
                    $("#buyCurName2").val(curName2);
                    $("#sellCurId2").val(curId1);
                    $("#sellCurName2").val(curName1);
                }
                $("#buyCurId2").val($("#sellCurId1").val());
                $("#buyCurName2").val($("#sellCurName1").val());
                $("#sellCurId2").val($("#buyCurId1").val());
                $("#sellCurName2").val($("#buyCurName1").val());
                setQuotationMethod("2",$("#buyCurId2").val(),$("#sellCurId2").val());
                getBaseCuQuotationMethod("2");
                fillForwardSpotRate("2", true);
                syncSwapContractRateFromNear();
                setForwardContractRate();
                //加载资金计划
                renderBudget();
                //现金流信息
                $("#srcCurRemoteTd").text($("#buyCurName2").val());
                //现金流信息
                $("#targetCurRemoteTd").text($("#sellCurName2").val());
                calContractRate("2");
            }
        }else{
            $("#tranCurPairBaseId1").val('');
            $("#tranCurPairReferId1").val('');
            $("#cur1").val('');
            $("#curEn1").val('');
            //selectType.get("deliveryCurId1").val('');
            $("#buyCurId1").val('');
            $("#buyCurName1").val('');
            $("#sellCurId1").val('');
            $("#sellCurName1").val('');
            if (moneyType.get("spotRate1")) {
                moneyType.get("spotRate1").val("");
            }
            //现金流信息
            $("#srcCurNearTd").text('');
            //现金流信息
            $("#targetCurNearTd").text('');
            if (isSwapTran == "1") {
                $("#tranCurPairBaseId2").val('');
                $("#tranCurPairReferId2").val('');
                $("#cur2").val('');
                $("#curEn2").val('');
                $("#buyCurId2").val('');
                $("#buyCurName2").val('');
                $("#sellCurId2").val('');
                $("#sellCurName2").val('');
                //现金流信息
                $("#srcCurRemoteTd").text('');
                //现金流信息
                $("#targetCurRemoteTd").text('');
            }
        }

        //重新加载收款账户信息（destroy 在隐藏下拉里会报 clientWidth，不能中断选产品）
        try {
            selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: $('#buyCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        } catch (e) {}
        try {
            selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: $('#sellCurId1').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        } catch (e) {}
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId1").val(),proId,"1","0", $("#cotpId1").val());
        renderBankAcc("1", $("#cotpId1").val(), $("#buyCurId1").val(),proId,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId1").val(),proId,"1","1", $("#cotpId1").val());
        renderBankAcc("1", $("#cotpId1").val(), $("#sellCurId1").val(),proId,"1","0", $('input[name="corpId"]').val());
        if (isSwapTran == "1") {
            reloadSelectTableSafe("corpBankAccTable2", "corpBankAcc2", {curId: $('#buyCurId2').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
            reloadSelectTableSafe("srcBankAccTable2", "srcBankAcc2", {curId: $('#sellCurId2').val(), corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#buyCurId2").val(),proId,"2","0", $("#cotpId2").val());
            renderBankAcc("1", $("#cotpId2").val(), $("#buyCurId2").val(),proId,"2","1", $('input[name="corpId"]').val());
            renderBankAcc("0", $('input[name="corpId"]').val(), $("#sellCurId2").val(),proId,"2","1", $("#cotpId2").val());
            renderBankAcc("1", $("#cotpId2").val(), $("#sellCurId2").val(),proId,"2","0", $('input[name="corpId"]').val());
        }
        renderBudget();
        form.render();
    }

    /**
     * 无汇率货币对：清空货币对及相关币种、账户，并只 reload 一次账户下拉
     */
    function clearCurPairAndAccounts(isSwapTran) {
        skipCurrencyPairCallback = true;
        try {
            selectType.get("tranCurPairId1").val("");
            if (isSwapTran == "1") {
                selectType.get("tranCurPairId2").val("");
            }
        } finally {
            skipCurrencyPairCallback = false;
        }
        $("#tranCurPairBaseId1").val('');
        $("#tranCurPairReferId1").val('');
        $("#cur1").val('');
        $("#curEn1").val('');
        $("#buyCurId1").val('');
        $("#buyCurName1").val('');
        $("#sellCurId1").val('');
        $("#sellCurName1").val('');
        $("#quotationMethod1").val("").css("display","none");
        $("#quotationCurId1").val("");
        $("#quotationReferCurId1").val("");
        if (moneyType.get("spotRate1")) {
            moneyType.get("spotRate1").val("");
        }
        $("#srcCurNearTd").text('');
        $("#targetCurNearTd").text('');
        $('#corpBankAcc1').val('');
        $('#corpAccId1').val('');
        $('#corpAccName1').val('');
        $('#corpBank1').val('');
        $('#srcBankAcc1').val('');
        $('#srcAccId1').val('');
        $('#srcAccName1').val('');
        $('#srcBank1').val('');
        $("#corpBankNameDiv1").text("");
        $("#srcBankNameDiv1").text("");
        $("#corpBankAccSwiftCodeDiv1").text("");
        $("#srcBankAccSwiftCodeDiv1").text("");
        if (isSwapTran == "1") {
            $("#tranCurPairBaseId2").val('');
            $("#tranCurPairReferId2").val('');
            $("#cur2").val('');
            $("#curEn2").val('');
            $("#buyCurId2").val('');
            $("#buyCurName2").val('');
            $("#sellCurId2").val('');
            $("#sellCurName2").val('');
            $("#quotationMethod2").val("").css("display","none");
            $("#quotationCurId2").val("");
            $("#quotationReferCurId2").val("");
            $("#srcCurRemoteTd").text('');
            $("#targetCurRemoteTd").text('');
            $('#corpBankAcc2').val('');
            $('#corpAccId2').val('');
            $('#srcBankAcc2').val('');
            $('#srcAccId2').val('');
        }
        if (selectTableObj["corpBankAccTable1"]) {
            selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: '', corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        }
        if (selectTableObj["srcBankAccTable1"]) {
            selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: '', corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        }
        if (isSwapTran == "1") {
            reloadSelectTableSafe("corpBankAccTable2", "corpBankAcc2", {curId: '', corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
            reloadSelectTableSafe("srcBankAccTable2", "srcBankAcc2", {curId: '', corpId: $('input[name="corpId"]').val(),bankTypeId: $('#bankTypeId').val()});
        }
        form.render();
    }

    //结算日期回调函数
    settleDateCallback1 = function (data, $elem){
        var settleDate = dateType.get('settleDate1').val();
        var tranDate = dateType.get('tranDate1').val();
        checkDate("1");
        changeDeliveryDeadline("1");
        //保证金相关信息设置
        setBoodInfo("1");
        //现金流
        $("#srcSettleDateNearTd").text(settleDate);
        $("#targetSettleDateNearTd").text(settleDate);

        if ("1" == $("#isSwapTran").val()) {
            dateType.get('tranDate2').val(tranDate);
        }

        form.render();
    };
    settleDateCallback2 = function (data, $elem){
        checkDate("1");
        var settleDate = dateType.get('settleDate2').val();
        changeDeliveryDeadline("2");
        //保证金相关信息设置
        setBoodInfo("2");
        //现金流
        $("#srcSettleDateRemoteTd").text(settleDate);
        $("#targetSettleDateRemoteTd").text(settleDate);
        form.render();
    };

    bt.tranDateCallback1 = function (data, $elem){
        changeDeliveryDeadline("1");
    };

    bt.tranDateCallback2 = function (data, $elem){
        changeDeliveryDeadline("2");
    };


    /**
     * 计算损益
     * @param data
     */
    function calculateFexProfitLoss(){
        // 货币对
        var tranCurPairBaseId = $("#tranCurPairBaseId1").val();
        var tranCurPairReferId = $("#tranCurPairReferId1").val();
        // 报价方式
        var quotationCurId = $("#quotationCurId1").val();
        var quotationReferCurId = $("#quotationReferCurId1").val();
        // 合同汇率
        var contractRate = $("#contractRate1").val();
        // 即期汇率
        var spotRate =$("#spotRate1").val();
        // 购买币种
        var buyCurId = $("#buyCurId1").val();
        var busDirection = dicts.get("busDirection1").val();
        // 损益币种
        var profitLossCur = selectType.get("profitLossCur1").val();
        if (contractRate && profitLossCur && busDirection && buyCurId) {
            var calculateData = {
                rateTypeId: $("#rateTypeId").val(),corpId: $('input[name="corpId"]').val(),isShowConvert:$("#isShowConvert").val(),
                tranCurPairBaseId: tranCurPairBaseId,tranCurPairReferId:tranCurPairReferId,
                quotationCurId:quotationCurId,quotationReferCurId:quotationReferCurId,busDirection:busDirection,
                contractRate:contractRate,spotRate:spotRate,profitLossCur:profitLossCur,
                buyCurId:buyCurId,buyMoney:moneyType.get("buyMoney1").val(),sellMoney:moneyType.get("sellMoney1").val()
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
                        if ("Y" == $("#isShowConvert").val()) {
                            moneyType.get('convertMoney1').val(result.obj.convertLossMoney);
                            moneyType.get('convertRate1').val(result.obj.convertRate);
                        }
                        moneyType.get('profitLossMoney1').val(result.obj.profitLossMoney);
                    }
                }
            });
        }

    }

    /**
     * 计算损益
     * @param data
     */
    function calculateFexProfitLoss2(){
        // 货币对
        var tranCurPairBaseId = $("#tranCurPairBaseId2").val();
        var tranCurPairReferId = $("#tranCurPairReferId2").val();
        // 报价方式
        var quotationCurId = $("#quotationCurId2").val();
        var quotationReferCurId = $("#quotationReferCurId2").val();
        // 合同汇率
        var contractRate = $("#contractRate2").val();
        // 即期汇率
        var spotRate =$("#spotRate2").val();
        // 购买币种
        var buyCurId = $("#buyCurId2").val();
        var busDirection = dicts.get("busDirection2").val();
        // 损益币种
        var profitLossCur = selectType.get("profitLossCur2").val();
        if (contractRate && profitLossCur && busDirection && buyCurId) {
            var calculateData = {
                rateTypeId:$("#rateTypeId").val(),corpId:$('input[name="corpId"]').val(),isShowConvert:$("#isShowConvert").val(),
                tranCurPairBaseId: tranCurPairBaseId,tranCurPairReferId:tranCurPairReferId,
                quotationCurId:quotationCurId,quotationReferCurId:quotationReferCurId,
                contractRate:contractRate,spotRate:spotRate,profitLossCur:profitLossCur,busDirection:busDirection,
                buyCurId:buyCurId,buyMoney:moneyType.get("buyMoney2").val(),sellMoney:moneyType.get("sellMoney2").val()
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
                        if ("Y" == $("#isShowConvert").val()) {
                            moneyType.get('convertMoney2').val(result.obj.convertLossMoney);
                            moneyType.get('convertRate2').val(result.obj.convertRate);
                        }
                        moneyType.get('profitLossMoney2').val(result.obj.profitLossMoney);
                        moneyType.get("spotRate2").val(data.obj.spotRate);
                    }
                }
            });
        }
    }


    // 折本位币报价方式
    function getBaseCuQuotationMethod(swapType, profitLossCurId) { // $("#isShowConvert").val("Y");

        if ("spot" != $("#type").val()) {
            return;
        }

        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType'+swapType).val();
        var corpBaseCur = $("#corpBaseCur").val();

        var profitLossCur = profitLossCurId || selectType.get("profitLossCur" +swapType).val();
        // 交易汇率类型=合同汇率 && 参数的外汇即期是否计算损益 && 产品的即期是否自动交割 && 单位的本位币不等于损益币种时
        if ("2" == tranRateType && "1" == isProfitAndLoss && "1" == isSpotAutoDelivery && corpBaseCur != profitLossCur) {
            $(".baseConvertTr").show();
            $("#isShowConvert").val("Y");
            if ("1" == swapType) {
                $("input[data-id='convertRate1']").attr("lay-verify", "required");
                $("input[data-id='convertMoney1']").attr("lay-verify", "required");
            } else {
                $("input[data-id='convertRate2']").attr("lay-verify", "required");
                $("input[data-id='convertMoney2']").attr("lay-verify", "required");
            }
        } else {
            $(".baseConvertTr").hide();
            $("#isShowConvert").val("N");
            if ("1" == swapType) {
                $("input[data-id='convertRate1']").removeAttr("lay-verify");
                $("input[data-id='convertMoney1']").removeAttr("lay-verify");
            } else {
                $("input[data-id='convertRate2']").removeAttr("lay-verify");
                $("input[data-id='convertMoney2']").removeAttr("lay-verify");
            }
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
                    $("#convertCurType"+swapType).val(result.obj.curEnCode+"/"+result.obj.referCurEnCode);
                    $("#convertCur"+swapType).val(result.obj.cur);
                    $("#convertCur"+swapType).val(result.obj.referCur);
                    if (profitLossCur == $("#tranCurPairBaseId"+swapType).val()) {
                        moneyType.get('convertRate'+swapType).val(result.obj.purchaseRate);
                    } else {
                        moneyType.get('convertRate'+swapType).val(result.obj.sellRate);
                    }
                }
            }
        });
    }

    // 远期/掉期：按交易货币对取最新即期汇率，随表单保存
    function fillForwardSpotRate(swapType, force) {
        var type = $("#type").val();
        if ("forward" == type || "swap" == type) {
            getSpotRate(swapType, force);
        }
    }

    // 查询即期汇率：默认外汇牌价类型 + 最新发布时间的即期牌价
    // 买入币种=报价方式左侧取买入价，买入币种=报价方式右侧取卖出价
    function getSpotRate(swapType, force) {
        var pairBaseId = $("#tranCurPairBaseId"+swapType).val();
        var pairReferId = $("#tranCurPairReferId"+swapType).val();
        var buyCurId = $("#buyCurId"+swapType).val();
        if (!pairBaseId || !pairReferId || !buyCurId) {
            return;
        }
        var spotRateField = moneyType.get("spotRate"+swapType);
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
    function calculateFexLifeCycleConvertLossMoney(swapType){
        if ("Y" != $("#isShowConvert").val()) {
            return;
        }
        var calculateForm = {
            profitLossCur: selectType.get("profitLossCur" +swapType).val(),
            convertCurType: $("#convertCurType"+swapType).val(),
            profitLossMoney: moneyType.get('profitLossMoney'+swapType).val(),
            convertRate: moneyType.get('convertRate'+swapType).val(),
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
                    moneyType.get('convertMoney'+swapType).val(result.obj);
                }
            }
        });
    }

    /**
     * 修改交割期限
     * @param swapType
     */
    function changeDeliveryDeadline(swapType){
        var settleDate = dateType.get('settleDate'+swapType).val();
        var tranDate = dateType.get('tranDate'+swapType).val();
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
        $("#deliveryDeadline"+swapType).val(deliveryDeadline);
        $("#deliveryDeadlineDiv"+swapType).text('T+'+deliveryDeadline);
        if (swapType == "1") {
            renderBudget();
        }
    }

    //根据交割期限设置保证金相关信息
    function setBoodInfo(swapType){
        var deliveryDeadline = $('#deliveryDeadline'+swapType).val();
        var boodType = bt.dicts.get('boodType'+swapType).val().trim();


        if ("5" == boodType || !boodType) {
            $(".boodDiv").hide();
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
            $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
            bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
            selectType.get('bondCurId'+swapType).attr('data-required', false);
            moneyType.get('payMoney'+swapType).removeAttr('lay-verify');
            setLayVerifyWithStrlength($('#boodRmk'+swapType), false);
            return;
        } else {
            $(".boodDiv").show();
        }


        //非T+0且保证金方式不为授信额度时：保证方式、保证金账号、保证比例、保证金钞汇标识、保证金币种都为必填
        if(deliveryDeadline>0 && boodType!="3" && boodType!="4" && boodType !='9') {
            bt.dicts.get('boodType'+swapType).setRequired(true);
            $('#boodAcc'+swapType).attr('lay-verify', 'required|tableData');
            bt.ratioType.get('boodRatio'+swapType).currentObject.attr('lay-verify', 'required');
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': true});
            selectType.get('bondCurId'+swapType).attr('data-required', true);
        }else{
            if(boodType=='9'){
                bt.dicts.get('boodType' + swapType).setRequired(false);
                bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
            }else{
                if(deliveryDeadline>0) {
                    bt.dicts.get('boodType'+swapType).setRequired(true);
                    bt.ratioType.get('boodRatio'+swapType).currentObject.attr('lay-verify', 'required');
                }else{
                    bt.dicts.get('boodType' + swapType).setRequired(false);
                    bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
                }
            }
            $('#boodAcc'+swapType).removeAttr('lay-verify');
            $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
            selectType.get('bondCurId'+swapType).attr('data-required', false);
        }
        form.renderIcon();
    }

    /**
     * 合同汇率计算
     */
    function calContractRate(swapType) {
        var sellCurId = $("#sellCurId"+swapType).val(); // 卖出币种
        var tranCur = $("#tranCurPairBaseId" +swapType).val();
        var currentMoney = moneyType.get("buyMoney"+swapType).val();
        if (tranCur == sellCurId) {
            currentMoney = moneyType.get("sellMoney"+swapType).val()
        }
        moneyType.get("positionMoney"+swapType).val(currentMoney);

        if ("1" == swapType) {
            setProfitLoss();
        }

        var buyMoney = moneyType.get("buyMoney"+swapType).val();
        var sellMoney = moneyType.get("sellMoney"+swapType).val();
        if (isNotEmpty(buyMoney) && isNotEmpty(sellMoney)) {
            if (swapType == "1") {
                $("#srcPayMoneyNearTd").text(moneyFun.format(moneyType.get("buyMoney" + swapType).val()));
                $("#targetPayMoneyNearTd").text('-' + moneyFun.format(moneyType.get("sellMoney" + swapType).val()));
            } else {
                $("#srcPayMoneyRemoteTd").text(moneyFun.format(moneyType.get("buyMoney" + swapType).val()));
                $("#targetPayMoneyRemoteTd").text('-' + moneyFun.format(moneyType.get("sellMoney" + swapType).val()));
            }
        }
    }

    /**
     * 设置远端卖出金额
     */
    function setForwartSellMoney() {
        var tranCurPairBaseId = $("#tranCurPairBaseId1").val(); // 货币对的左侧
        var sellCurId = $("#sellCurId2").val(); // 卖出币种
        var buyMoney  = moneyType.get("buyMoney1").val();
        var contractRate = moneyType.get("contractRate2").val();
        var isPaymentAmount = bt.dicts.get('isPaymentAmount2').val(); // 是否自动计算
        // 固定金额=卖出金额或交易汇率类型=合同汇率
        if ("1" == $("#isSwapTran").val() && sellCurId == tranCurPairBaseId) {
            moneyType.get("sellMoney2").val(buyMoney);
            moneyType.get("sellMoney2").attr('readonly', true);
            if (contractRate && "1" == isPaymentAmount) {
                var sellCurId = $("#sellCurId2").val(); // 卖出币种
                var quotationCur = $("#quotationCurId2").val(); // 报价方式的左侧
                if (sellCurId == quotationCur ) {
                    moneyType.get("buyMoney2").val(moneyFun.mul(buyMoney,contractRate,2));
                } else {
                    moneyType.get("buyMoney2").val(moneyFun.div(buyMoney,contractRate,2));
                }
                calContractRate("2");
            }
        }
    }

    /**
     * 设置远端买入金额
     */
    function setForwartBuyMoney() {
        var tranCurPairBaseId = $("#tranCurPairBaseId1").val(); // 货币对的左侧
        var buyCurId = $("#buyCurId2").val(); // 买入币种
        var sellMoney  = moneyType.get("sellMoney1").val();
        var contractRate = moneyType.get("contractRate2").val();
        var isPaymentAmount = bt.dicts.get('isPaymentAmount2').val(); // 是否自动计算
        // 固定金额=卖出金额或交易汇率类型=合同汇率
        if("1" == $("#isSwapTran").val() && buyCurId == tranCurPairBaseId){
            moneyType.get("buyMoney2").val(sellMoney);
            moneyType.get("buyMoney2").attr('readonly', true);
            if (contractRate && "1" == isPaymentAmount) {
                var buyCurId = $("#buyCurId2").val(); // 卖出币种
                var quotationCur = $("#quotationCurId2").val(); // 报价方式的左侧
                if (buyCurId == quotationCur) {
                    moneyType.get("sellMoney2").val(moneyFun.mul(sellMoney,contractRate,2));
                } else {
                    moneyType.get("sellMoney2").val(moneyFun.div(sellMoney,contractRate,2));
                }
                calContractRate("2");
            }
        }
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

    // 买入金额回调
    bt.buyMoneyCallback1 = function(){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount1').val(); // 是否自动计算
        var contractRate = moneyType.get("contractRate1").val();
        if ("1" != isPaymentAmount || !contractRate) {
            calContractRate("1");
            return;
        }
        var buyMoney  = moneyType.get("buyMoney1").val();
        var buyCurId = $("#buyCurId1").val(); // 买入币种
        var quotationCur = $("#quotationCurId1").val(); // 报价方式的左侧
        if (buyCurId == quotationCur && "1" == isPaymentAmount) {
            moneyType.get("sellMoney1").val(moneyFun.mul(buyMoney,contractRate,2));
        } else if ("1" == isPaymentAmount){
            moneyType.get("sellMoney1").val(moneyFun.div(buyMoney,contractRate,2));
        }
        calContractRate("1");
        syncBudgetMoneyFromSellMoney();
        if (isSwapTran == "1") {
            syncSwapContractRateFromNear();
            // 设置远端买入金额
            setForwartBuyMoney();
            // 设置远端卖出金额
            setForwartSellMoney();

            calContractRate("2");
        }
    }
    bt.buyMoneyCallback2 = function(){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount2').val(); // 是否自动计算
        var tranCurPairBaseId = $("#tranCurPairBaseId1").val(); // 货币对的左侧
        var sellCurId = $("#sellCurId2").val(); // 卖出币种
        var contractRate = moneyType.get("contractRate2").val();
        if ("1" != isPaymentAmount || !contractRate) { // 由近端买入金额设置远端卖出
            calContractRate("2");
            return;
        } else {
            var buyMoney  = moneyType.get("buyMoney2").val();
            var buyCurId = $("#buyCurId2").val(); // 卖出币种
            var quotationCur = $("#quotationCurId2").val(); // 报价方式的左侧
            if (buyCurId == quotationCur && "1" == isPaymentAmount) {
                moneyType.get("sellMoney2").val(moneyFun.mul(buyMoney,contractRate,2));
            } else if ("1" == isPaymentAmount){
                moneyType.get("sellMoney2").val(moneyFun.div(buyMoney,contractRate,2));
            }
            calContractRate("2");
        }
    }
    // 卖出金额回调
    bt.sellMoneyCallback1 = function(){
        sellMoneyCallback();
        syncBudgetMoneyFromSellMoney();
    }
    sellMoneyCallback = function (money){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount1').val(); // 是否自动计算
        var contractRate = moneyType.get("contractRate1").val();
        if ("1" != isPaymentAmount || !contractRate) {
            calContractRate("1");
            return;
        }
        var sellMoney  = money || moneyType.get("sellMoney1").val();
        var sellCurId = $("#sellCurId1").val(); // 卖出币种
        var quotationCur = $("#quotationCurId1").val(); // 报价方式的左侧
        if (sellCurId == quotationCur) {
            moneyType.get("buyMoney1").val(moneyFun.mul(sellMoney,contractRate,2));
        } else {
            moneyType.get("buyMoney1").val(moneyFun.div(sellMoney,contractRate,2));
        }
        calContractRate("1");
        if (isSwapTran == "1") {
            syncSwapContractRateFromNear();
            // 设置远端买入金额
            setForwartBuyMoney();
            // 设置远端卖出金额
            setForwartSellMoney();

            calContractRate("2");
        }
    }


    bt.sellMoneyCallback2 = function(){
        var isPaymentAmount = bt.dicts.get('isPaymentAmount2').val(); // 是否自动计算
        var tranCurPairBaseId = $("#tranCurPairBaseId1").val(); // 货币对的左侧
        var buyCurId = $("#buyCurId2").val(); // 买入币种
        var contractRate = moneyType.get("contractRate2").val();
        if ("1" != isPaymentAmount || buyCurId == tranCurPairBaseId || !contractRate) { // 由近端卖出金额设置远端买入金额
            calContractRate("2");
            return;
        } else {
            var sellMoney  = moneyType.get("sellMoney2").val();
            var sellCurId = $("#sellCurId2").val(); // 卖出币种
            var quotationCur = $("#quotationCurId2").val(); // 报价方式的左侧
            if (sellCurId == quotationCur) {
                moneyType.get("buyMoney2").val(moneyFun.mul(sellMoney,contractRate,2));
            } else {
                moneyType.get("buyMoney2").val(moneyFun.div(sellMoney,contractRate,2));
            }
            calContractRate("2");
        }
    }

    //保证金比例回调函数
    bt.boodRatioCallback1 = function (data) {
        var boodRatio = bt.ratioType.get('boodRatio1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.ratioType.get('boodRatio2').val(boodRatio);
        }
    }
    bt.boodRatioCallback2 = function (data) {
        var boodRatio = bt.ratioType.get('boodRatio2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            bt.ratioType.get('boodRatio1').val(boodRatio);
        }
    }

    //额度回调函数
    bt.payMoneyCallback1 = function (data) {
        var payMoney = moneyType.get('payMoney1').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('payMoney2').val(payMoney);
        }
    }
    bt.payMoneyCallback2 = function (data) {
        var payMoney = moneyType.get('payMoney2').val();
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('payMoney1').val(payMoney);
        }
    }

    //远期点数回调函数
    bt.forwardPointsCallback1 = function(data){
        var forwardPoints = moneyType.get('forwardPoints1').val();
        if("1" == $("#isSwapTran").val()){
            moneyType.get('forwardPoints2').val(forwardPoints);
        }
    }
    bt.forwardPointsCallback2 = function(data){
        var forwardPoints = moneyType.get('forwardPoints2').val();
        if("1" == $("#isSwapTran").val()){
            moneyType.get('forwardPoints1').val(forwardPoints);
        }
    }


    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

    /**
     * 获取账户信息
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     * @param swapType 掉期交易 1-近端 2-远端
     * @param cdSign 收付方向 0-收款 1-付款
     * @param oppCorpId 对方ID（busType=0时传入交易对手id,否则传入单位id）
     */
    function renderBankAcc(busType, corpId, curId,proId,swaptype,cdSign,oppCorpId){
        if(busType && corpId && proId) {
            var tranType = {spot: "7", forward: "8", swap: "9"}[$("#type").val()];
            $.ajax({
                url: basePath + "/foreign/fexcorpbankacc/getNewBankAcc",
                method: 'POST',
                data: {busType: busType, tranType: tranType, corpId: corpId, curId: curId, proId: proId, cashFlowType: "0", cdSign: cdSign, oppCorpId: oppCorpId},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        //收款
                        if(cdSign=="0") {
                            if (busType == "0") {
                                if(data.obj.accId){
                                    $('#corpBank'+swaptype).val(data.obj.bankType);
                                    $('#corpAccId'+swaptype).val(data.obj.accId);
                                    $('#corpBankAcc'+swaptype).val(data.obj.bankAcc);
                                    $('#corpAccName'+swaptype).val(data.obj.accName);
                                }
                                if (data.obj.bankAcc) {
                                    $("#corpBankNameDiv" + swaptype).text(data.obj.bankTypeName);
                                }/*else{
                                    $("#corpBankNameDiv" + swaptype).text("");
                                }*/
                                //现金流信息
                                if (swaptype == "1") {
                                    //近端
                                    if (data.obj.bankAcc) {
                                        $("#srcBankNearTd").text(data.obj.bankTypeName);
                                        $("#srcAccNameNearTd").text(data.obj.accName);
                                        $("#srcBankAccNearTd").text(data.obj.bankAcc);
                                        if(data.obj.voucherName) {
                                            $("#srcPayTypeNearTd").text(data.obj.voucherName);
                                        }/*else{
                                            $("#srcPayTypeNearTd").text("");
                                        }*/
                                    } /*else {
                                        $("#srcBankNearTd").text("");
                                        $("#srcAccNameNearTd").text("");
                                        $("#srcBankAccNearTd").text("");
                                        $("#srcPayTypeNearTd").text("");
                                    }*/
                                } else {
                                    //远端
                                    if (data.obj.bankAcc) {
                                        if(data.obj.bankAcc){
                                            $("#srcBankRemoteTd").text(data.obj.bankTypeName);
                                            $("#srcAccNameRemoteTd").text(data.obj.accName);
                                            $("#srcBankAccRemoteTd").text(data.obj.bankAcc);
                                        }
                                        if(data.obj.voucherName) {
                                            $("#srcPayTypeRemoteTd").text(data.obj.voucherName);
                                        }/*else{
                                            $("#srcPayTypeRemoteTd").text("");
                                        }*/
                                    } /*else {
                                        $("#srcBankRemoteTd").text("");
                                        $("#srcAccNameRemoteTd").text("");
                                        $("#srcBankAccRemoteTd").text("");
                                        $("#srcPayTypeRemoteTd").text("");
                                    }*/
                                }
                            } else {
                                if (data.obj.accId){
                                    $('#targetBank'+swaptype).val(data.obj.bankType);
                                    $('#targetAccId'+swaptype).val(data.obj.accId);
                                    $('#targetBankAcc'+swaptype).val(data.obj.bankAcc);
                                    $('#targetAccName'+swaptype).val(data.obj.accName);
                                }
                            }
                        }else{
                            //付款
                            if (busType == "0") {
                                if (data.obj.accId){
                                    $('#srcBank'+swaptype).val(data.obj.bankType);
                                    $('#srcAccId'+swaptype).val(data.obj.accId);
                                    $('#srcBankAcc'+swaptype).val(data.obj.bankAcc);
                                    $('#srcAccName'+swaptype).val(data.obj.accName);
                                }
                                if (data.obj.bankAcc) {
                                    $("#srcBankNameDiv" + swaptype).text(data.obj.bankTypeName);
                                }/*else{
                                    $("#srcBankNameDiv" + swaptype).text("");
                                }*/
                                //现金流信息
                                if (swaptype == "1") {
                                    //近端
                                    if (data.obj.bankAcc) {
                                        $("#targetAccNameNearTd").text(data.obj.accName);
                                        $("#targetBankAccNearTd").text(data.obj.bankAcc);
                                        $("#targetBankNearTd").text(data.obj.bankTypeName);
                                        if(data.obj.voucherName) {
                                            $("#targetPayTypeNearTd").text(data.obj.voucherName);
                                        }/*else{
                                            $("#targetPayTypeNearTd").text("");
                                        }*/
                                    } /*else {
                                        $("#targetAccNameNearTd").text("");
                                        $("#targetBankAccNearTd").text("");
                                        $("#targetPayTypeNearTd").text("");
                                        $("#targetBankNearTd").text("");
                                    }*/
                                } else {
                                    //远端
                                    if (data.obj.bankAcc) {
                                        $("#targetAccNameRemoteTd").text(data.obj.accName);
                                        $("#targetBankAccRemoteTd").text(data.obj.bankAcc);
                                        $("#targetBankRemoteTd").text(data.obj.bankTypeName);
                                        if(data.obj.voucherName) {
                                            $("#targetPayTypeRemoteTd").text(data.obj.voucherName);
                                        }/*else{
                                            $("#targetPayTypeRemoteTd").text("");
                                        }*/
                                    } /*else {
                                        $("#targetAccNameRemoteTd").text("");
                                        $("#targetBankAccRemoteTd").text("");
                                        $("#targetPayTypeRemoteTd").text("");
                                        $("#targetBankRemoteTd").text("");
                                    }*/
                                }
                            } else {
                                if (data.obj.accId){
                                    $('#cotpBank'+swaptype).val(data.obj.bankType);
                                    $('#cotpAccId'+swaptype).val(data.obj.accId);
                                    $('#cotpBankAcc'+swaptype).val(data.obj.bankAcc);
                                    $('#cotpAccName'+swaptype).val(data.obj.accName);
                                }
                            }
                        }
                        if (swaptype == "1") {
                            syncSwapFarBankAccFromNear();
                        }
                    }
                }
            });
        }
    }

    //监听提交
    form.on('submit(submit)', function(data){
        var isProfitAndLoss = $("#isProfitAndLoss").val();
        var isSpotAutoDelivery = $("#isSpotAutoDelivery").val();
        var tranRateType = bt.dicts.get('tranRateType1').val();
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId1").val(selectTableObj["up1"].getGroupId());
        data.field.status = $(this).attr("value");
        data.field.attachmentId1 = selectTableObj["up1"].getGroupId();
        var fexTranType1 = $("#fexTranType1").val();
        //外汇交易类型 0-即期、1-远期
        if(data.field.type=="1"&&fexTranType1=="0") {
            if (bt.dicts.get('fixedMoney1').val()) {
                data.field.fixedMoney1 = (bt.dicts.get('fixedMoney1').val()).toString();
            } else {
                data.field.fixedMoney1 = "";
            }
        }
        if ("Y" != $("#isShowConvert").val()) {
            delete data.field.convertRate1;
            delete data.field.convertRate2;
            delete data.field.convertMoney1;
            delete data.field.convertMoney2;
            delete data.field.convertCurType1;
            delete data.field.convertCurType2;
        }
        if ("2" != tranRateType || "1" != isProfitAndLoss || "1" != isSpotAutoDelivery){
            delete data.field.profitLossCur1;
            delete data.field.profitLossCur2;
            delete data.field.profitLossMoney1;
            delete data.field.profitLossMoney2;
        }
        var isSwapTran = $("#isSwapTran").val();
        if (isSwapTran == "1") {
            syncSwapFarBankAccFromNear();
            data.field.corpBankAcc2 = $("#corpBankAcc2").val();
            data.field.corpAccId2 = $("#corpAccId2").val();
            data.field.corpBank2 = $("#corpBank2").val();
            data.field.corpAccName2 = $("#corpAccName2").val();
            data.field.srcBankAcc2 = $("#srcBankAcc2").val();
            data.field.srcAccId2 = $("#srcAccId2").val();
            data.field.srcBank2 = $("#srcBank2").val();
            data.field.srcAccName2 = $("#srcAccName2").val();
            $("#attachmentId2").val(selectTableObj["up2"].getGroupId());
            data.field.attachmentId2 = selectTableObj["up2"].getGroupId();
        }
        //证书签名
        var signDataMsg = signData("外汇交易提交", 11);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        var action = "/foreign/fextranreq/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.refreshCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

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
     * 重置按钮回调
     */
    resetCallBack = function () {
        renderBudget();
    }

    /**
     * 生成自定义信息
     * @return
     */
    $genCustHtml = function () {
        var url = basePath + "/foreign/fextranreq/addCustInfo/" + $('#tranType').val() + "?timestamp=" + new Date().getTime();
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
     * 生成-近端
     * @return
     */
    $genNearHtml = function () {
        var type = $("#type").val();
        var url = basePath + "/foreign/fexTradingReq/addTranReqForeign/1/"+type+"?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#nearMsg").html("");
                $("#nearMsg").html(json);
                form.renderIcon();
                form.render();
            }
        });
    }
    $genNearHtml();

    $genRemoteHtml = function () {
        var type = $("#type").val();
        var url = basePath + "/foreign/fexTradingReq/addTranReqForeign/2/"+type+"?timestamp=" + new Date().getTime();
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#remoteMsg").html("");
                $("#remoteMsg").html(json);
                form.renderIcon();
                form.render();
            }
        });
    }
    $genRemoteHtml();

    /** 下拉表格赋值：同步 beforevalue，避免点击提交时被清空；已赋值则不再走 tableData（远端表未加载会误判） */
    function setSwapSelectTableAcc(inputId, tableKey, value) {
        var $input = $("#" + inputId);
        if (!$input.length) {
            return;
        }
        $input.val(value);
        $input.data("beforevalue", value || "");
        $input.data("value", value ? value : "table");
        if (value) {
            $input.attr("lay-verify", "required");
        }
        try {
            if (selectTableObj[tableKey] && selectTableObj[tableKey].get) {
                selectTableObj[tableKey].get(inputId).val(value);
            }
        } catch (e) {}
    }

    /** 近端付款→远端收款、近端收款→远端付款；远端已有值不覆盖 */
    function syncSwapFarBankAccFromNear() {
        if ("1" != $("#isSwapTran").val()) {
            return;
        }
        var nearPayAcc = $.trim($("#srcBankAcc1").val() || "");
        var farRecvAcc = $.trim($("#corpBankAcc2").val() || "");
        if (nearPayAcc && !farRecvAcc) {
            $("#corpAccId2").val($("#srcAccId1").val());
            $("#corpBank2").val($("#srcBank1").val());
            $("#corpAccName2").val($("#srcAccName1").val());
            setSwapSelectTableAcc("corpBankAcc2", "corpBankAccTable2", nearPayAcc);
            var payBankName = $("#srcBankNameDiv1").text();
            $("#corpBankNameDiv2").text(payBankName);
            $("#corpBankAccSwiftCodeDiv2").text($("#srcBankAccSwiftCodeDiv1").text());
            $("#srcBankRemoteTd").text(payBankName);
            $("#srcAccNameRemoteTd").text($("#srcAccName1").val());
            $("#srcBankAccRemoteTd").text(nearPayAcc);
        } else if (farRecvAcc) {
            $("#corpBankAcc2").attr("lay-verify", "required");
        }
        var nearRecvAcc = $.trim($("#corpBankAcc1").val() || "");
        var farPayAcc = $.trim($("#srcBankAcc2").val() || "");
        if (nearRecvAcc && !farPayAcc) {
            $("#srcAccId2").val($("#corpAccId1").val());
            $("#srcBank2").val($("#corpBank1").val());
            $("#srcAccName2").val($("#corpAccName1").val());
            setSwapSelectTableAcc("srcBankAcc2", "srcBankAccTable2", nearRecvAcc);
            var recvBankName = $("#corpBankNameDiv1").text();
            $("#srcBankNameDiv2").text(recvBankName);
            $("#srcBankAccSwiftCodeDiv2").text($("#corpBankAccSwiftCodeDiv1").text());
            $("#targetBankRemoteTd").text(recvBankName);
            $("#targetAccNameRemoteTd").text($("#corpAccName1").val());
            $("#targetBankAccRemoteTd").text(nearRecvAcc);
        } else if (farPayAcc) {
            $("#srcBankAcc2").attr("lay-verify", "required");
        }
    }

    function showRemoteTranTab() {
        var $li = $("#tranTb li").eq(1);
        if ($li.length && !$li.hasClass("layui-this")) {
            $li.click();
        }
    }

    var isSwapTran = $("#isSwapTran").val();
    if (isSwapTran == "1") {
        $("#tranTb").show();
        $("#remoteMsg").removeClass("ignore-verify");
        form.renderIcon();
        var element = bt.element();
        element.on('tab(docDemoTabBrief)', function (elem) {
            if ($(this).parent().attr("id") == "tranTb" && elem.index == 1) {
                syncSwapFarBankAccFromNear();
            }
        });
        // 捕获阶段先于 layui 点击校验，避免远端下拉 tableData 在隐藏页签误判为空
        document.addEventListener("click", function (e) {
            if ($(e.target).closest("[lay-submit]").length) {
                syncSwapFarBankAccFromNear();
            }
        }, true);
        // 校验失败时会 focus 远端隐藏域，切到远端页签才能看到必填红星和合同汇率
        $("#remoteMsg").on("focus", "input, textarea, select", function () {
            showRemoteTranTab();
        });
    } else {
        $("#tranTb").hide();
        $("#remoteMsg").addClass("ignore-verify");
    }



    function getBudgetContainer(swapType) {
        var container = $("#tagItemDetail" + swapType);
        if (!container.length && swapType == "1") {
            container = $("#tagItemDetail");
        }
        return container;
    }

    function clearBudget() {
        getBudgetContainer("1").html("");
        getBudgetContainer("2").html("").hide();
    }

    function getDictValue(name) {
        var dict = bt.dicts.get(name);
        var value = dict && dict.val ? dict.val() : "";
        return value == null ? "" : value;
    }

    function canRenderBudget() {
        if ($("#budgetSel").val() != "1") {
            return false;
        }
        if (!$('input[name="corpId"]').val() || !getBudgetContainer("1").length) {
            return false;
        }
        var fexTranType = $("#fexTranType1").val();
        var type = $("#type").val();
        if ((type == "1" || type == "spot") && fexTranType == "0") {
            var fixedMoney = getDictValue("fixedMoney1").toString();
            if (fixedMoney.indexOf("2") == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 近端卖出金额变更时，同步更新资金计划执行金额
     */
    function syncBudgetMoneyFromSellMoney() {
        if (syncingBudgetAndSellMoney || !canRenderBudget() || !$("#budgetProcessDataTable").length) {
            return;
        }
        var rows = $("#budgetProcessDataTable").children("div[row]");
        if (rows.length != 1) {
            return;
        }
        var row = rows.eq(0).attr("row");
        var sellMoney = moneyType.get("sellMoney1").val();
        syncingBudgetAndSellMoney = true;
        try {
            console.log(sellMoney);
            //bt.moneyType.get("processMoney" + row).val(sellMoney);
            //var processMoneyInput = $("#processMoney" + row);
            //var exchangeRate = bt.moneyType.get("exchangeRate" + row).val();
            //if (bt.callSumProcessMoney && exchangeRate) {
            //    bt.callSumProcessMoney(sellMoney, processMoneyInput);
            //} else {
            //    bt.moneyType.get("sumMoney").val(sellMoney);
            //}
        } finally {
            syncingBudgetAndSellMoney = false;
        }
    }

    /**
     * 生成近端资金计划
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $('input[name="corpId"]').val();
        var itemCode = $('input[name="payItem"]').val();
        var processDate = dateType.get("settleDate1").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            corpId: corpId,
            itemCode: itemCode ? itemCode : "400",
            processDate: processDate,
            type: 'select',
            detailControl: true,
            curId: $("#sellCurId1").val(),
            settlType: $("#settlType").val(),
            purpose: getDictValue("purpose1"),
            wishPayday: processDate,
            payerAcc: $("#srcBankAcc1").val(),
            payeeAccName: $("#corpAccName1").val(),
            payeeAcc: $("#corpBankAcc1").val(),
            payeeBank: $("#corpBankNameDiv1").text(),
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                var container = getBudgetContainer("1");
                container.show();
                container.html("");
                container.html(json);
                getBudgetContainer("2").html("").hide();
                form.render();
                syncBudgetMoneyFromSellMoney();
            }
        });
    };

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        if (canRenderBudget()) {
            $genOtherHtml();
        } else {
            clearBudget();
        }
    }
    renderBudget();
});

//文本框复制
function 消息类型(swapType, filedName){
    var filedVal = $("#"+filedName+swapType).val();
    var isSwapTran = $("#isSwapTran").val();
    if (isSwapTran == "1") {
        if(swapType == "1") {
            $("#"+filedName+"2").val(filedVal);
        } else {
            $("#"+filedName+"1").val(filedVal);
        }
    }
}

/**
 * 设置报价方式
 * @return {boolean} false 表示货币对无汇率，调用方应中止后续逻辑
 */
function setQuotationMethod(swapType,curId1,curId2){
    var rateTypeId = $("#rateTypeId").val();
    var settleDate = dateType.get("settleDate" + swapType).val();
    var hasRate = true;

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
                var quotationMethodDesc = $("#quotationMethodDesc" + swapType);
                if (quotationMethodDesc) {
                    quotationMethodDesc.hide();
                }

                if (null == result.obj) {
                    $("#quotationMethod"+swapType).val("");
                    $("#quotationCurId"+swapType).val("");
                    $("#quotationReferCurId"+swapType).val("");
                    $("#quotationMethod"+swapType).css("display","none");
                } else {
                    $("#quotationMethod"+swapType).css("display","");
                    $("#quotationMethod"+swapType).val(result.obj.curEnCode+"/"+result.obj.referCurEnCode);
                    $("#quotationCurId"+swapType).val(result.obj.cur);
                    $("#quotationReferCurId"+swapType).val(result.obj.referCur);
                    if ("spot" == $("#type").val()) {
                        var profitLossCur = selectType.get("profitLossCur" + swapType).val();
                        if (profitLossCur == result.obj.cur) {
                            moneyType.get("spotRate"+swapType).val(result.obj.purchaseRate);
                        } else {
                            moneyType.get("spotRate"+swapType).val(result.obj.sellRate);
                        }
                    } else if ("swap" == $("#type").val()) {
                        if ("0" == $("#fexTranType"+swapType).val()) {
                            var defProfitAndLossCur = $("#defProfitAndLossCur").val();
                            if ("1" == defProfitAndLossCur) { // 标准币种
                                moneyType.get("spotRate"+swapType).val(result.obj.purchaseRate);
                            } else { // 报价币种
                                moneyType.get("spotRate"+swapType).val(result.obj.sellRate);
                            }
                        }
                    }
                }
            } else if ("FEX_RATE_INFO_IS_NULL" == result.messageEnum) {
                parent.layer.alert(result.msg, {icon: 3});
                var quotationMethodDesc = $("#quotationMethodDesc" + swapType);
                if (quotationMethodDesc) {
                    quotationMethodDesc.show();
                }
                hasRate = false;
            }
        }
    });
    return hasRate;
}




function checkDate(swapType){
    var tranAddrId = $("#tranAddrId" + swapType).val();
    var proId = selectType.get("proId").getData().value;
    var settleDate = dateType.get("settleDate" + swapType).val();
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
                        dateType.get("settleDate"+swapType).val('');
                        parent.layer.alert(bt.lang("fex.settle.date.not.work"), {icon: 7});
                    }
                }
            }
        });
    }
}

/**
 * 资金计划金额变更时，同步更新近端卖出金额
 * @param sumMoney 资金计划汇总金额
 */
function syncSellMoneyFromBudget(sumMoney) {
    if (syncingBudgetAndSellMoney) {
        return;
    }
    syncingBudgetAndSellMoney = true;
    try {
        sellMoneyCallback(sumMoney);
    } finally {
        syncingBudgetAndSellMoney = false;
    }
}

/**
 * 资金计划选择/修改后的回调
 * @param sumMoney 资金计划汇总金额
 * @param datas 资金计划明细数据
 * @param rowNum 当前行号
 */
function budgetProcessCallback(sumMoney, datas, rowNum) {
    syncSellMoneyFromBudget(sumMoney);
}

/**
 * 兼容资金计划明细金额汇总后的回调
 * @param data 资金计划汇总金额
 */
function sumMoneyCallBack(data) {
    budgetProcessCallback(data);

}
