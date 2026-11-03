var calContractRate;
bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType','corp', 'btUpload','ratioType','allBankInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    var timeStamp = Date.parse(new Date());
    $('#timeStamp').val(timeStamp);
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.allBankInfo.init();

    //结售汇属性回调函数
    bt.tranGroupCallback1 = function (data, $elem){
        var tranGroup = bt.dicts.get('tranGroup1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('tranGroup2').val(tranGroup);
        }
    };
    bt.tranGroupCallback2 = function (data, $elem){
        var tranGroup = bt.dicts.get('tranGroup2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('tranGroup1').val(tranGroup);
        }
    };
    //保证金币种回调函数
    bt.bondCurIdCallback1 = function (data, $elem){
        var bondCurId = bt.dicts.get('bondCurId1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('bondCurId2').val(bondCurId);
        }
    };
    bt.bondCurIdCallback2 = function (data, $elem){
        var bondCurId = bt.dicts.get('bondCurId2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('bondCurId1').val(bondCurId);
        }
    };
    var selectType = bt.selectType.init();
    bt.rateType.init();
    bt.ratioType.init();

    //跨行标志回调函数
    bt.difBankCallback1 = function(data){
        var difBank = bt.dicts.get("difBank1").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("difBank2").val(difBank);
        }
    }
    bt.difBankCallback2 = function(data){
        var difBank = bt.dicts.get("difBank2").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("difBank1").val(difBank);
        }
    }

    //远期点数回调函数
    bt.forwardPointsCallback1 = function(data){
        var forwardPoints = moneyType.get('forwardPoints1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('forwardPoints2').val(forwardPoints);
        }
    }
    bt.forwardPointsCallback2 = function(data){
        var forwardPoints = moneyType.get('forwardPoints2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('forwardPoints1').val(forwardPoints);
        }
    }

    //钞汇标识回调函数
    bt.bankNoteCallback1 = function(data){
        var bankNote = bt.dicts.get("bankNote1").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("bankNote2").val(bankNote);
        }
    }
    bt.bankNoteCallback2 = function(data){
        var bankNote = bt.dicts.get("bankNote2").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("bankNote1").val(bankNote);
        }
    }

    //资金性质回调函数
    bt.accNatureCallback1 = function(data){
        var accNature = bt.dicts.get("accNature1").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("accNature2").val(accNature);
        }
    }
    bt.accNatureCallback2 = function(data){
        var accNature = bt.dicts.get("accNature2").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("accNature1").val(accNature);
        }
    }

    //汇款类型回调函数
    bt.remitTypeCallback1 = function(data){
        //汇款类型=2-境外账户汇款时，“其它信息”非必填，并默认为折叠状态
        var remitType = bt.dicts.get("remitType1").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("remitType2").val(remitType);
        }
        if (remitType == "2") {
            $("#reportInfoDiv1").removeClass("layui-show");
            $("#reportInfoDiv1").addClass("ignore-verify");
            $("#reportInfoDiv2").removeClass("layui-show");
            $("#reportInfoDiv2").addClass("ignore-verify");
            $("#reportInfoItemDiv1").hide();
            $("#reportInfoItemDiv2").hide();
        } else {
            $("#reportInfoDiv1").addClass("layui-show");
            $("#reportInfoDiv1").removeClass("className");
            $("#reportInfoDiv2").addClass("layui-show");
            $("#reportInfoDiv2").removeClass("className");
            $("#reportInfoItemDiv1").show();
            $("#reportInfoItemDiv2").show();
        }

        var countryId = "CN";
        if ($('#corpIsDomesticBank1').val() == "0") {
            // 境外账户
            countryId = $('#corpCountryId1').val();
        }
        var bankTypeId = $('#corpBank1').val();
        var bifCode = $('#corpBifCode1').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, '', remitType, "other", "1");
        form.renderIcon();
    }
    bt.remitTypeCallback2 = function(data){
        //汇款类型=2-境外账户汇款时，“其它信息”非必填，并默认为折叠状态
        var remitType = bt.dicts.get("remitType2").val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get("remitType1").val(remitType);
        }
        if (remitType == "2") {
            $("#reportInfoDiv2").removeClass("layui-show");
            $("#reportInfoDiv2").addClass("ignore-verify");
            $("#reportInfoDiv1").removeClass("layui-show");
            $("#reportInfoDiv1").addClass("ignore-verify");
            $("#reportInfoItemDiv1").hide();
            $("#reportInfoItemDiv2").hide();
        } else {
            $("#reportInfoDiv2").addClass("layui-show");
            $("#reportInfoDiv2").removeClass("className");
            $("#reportInfoDiv1").addClass("layui-show");
            $("#reportInfoDiv1").removeClass("className");
            $("#reportInfoItemDiv1").show();
            $("#reportInfoItemDiv2").show();
        }

        var countryId = "CN";
        if ($('#corpIsDomesticBank2').val() == "0") {
            // 境外账户
            countryId = $('#corpCountryId2').val();
        }
        var bankTypeId = $('#corpBank2').val();
        var bifCode = $('#corpBifCode2').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, '', remitType, "other", "2");
        form.renderIcon();
    }

    function setBoodInfoChange(boodType, swapType){
        if(boodType == "3"){
            $('#boodRmk'+swapType).attr('lay-verify', 'required');
        }else{
            $('#boodRmk'+swapType).removeAttr("lay-verify");
        }
        if(boodType == "4"){
            moneyType.get('payMoney'+swapType).attr('lay-verify', 'required|number2');
            $('#boodAcc'+swapType).attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': false});
            selectType.get('bondCurId'+swapType).attr('data-required', false);
        }else{
            moneyType.get('payMoney'+swapType).attr('lay-verify', 'number2');
            var deliveryDeadline = $('#deliveryDeadline'+swapType).val();
            if(deliveryDeadline>0 && boodType != "3") {
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
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodType2').val(boodType);
            setBoodInfoChange(boodType, "2");
        }
        form.renderIcon();
    };
    bt.boodTypeCallback2 = function (data, $elem){
        var boodType = bt.dicts.get('boodType2').val();
        setBoodInfoChange(boodType, "2");
        var isSwapTran = bt.dicts.get("isSwapTran").val();
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
            $('#purposeDtl1').attr('lay-verify', 'required');
        }else{
            $('#purposeDtl1').removeAttr("lay-verify");
        }
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('purpose2').val(purpose);
            if(purpose == "005" || purpose == "006" || purpose == "099"){
                $('#purposeDtl2').attr('lay-verify', 'required');
            }else{
                $('#purposeDtl2').removeAttr("lay-verify");
            }
        }
        form.renderIcon();
    };
    bt.purposeCallback2 = function (data, $elem){
        var purpose = bt.dicts.get('purpose2').val();
        if(purpose == "005" || purpose == "006" || purpose == "099"){
            $('#purposeDtl2').attr('lay-verify', 'required');
        }else{
            $('#purposeDtl2').removeAttr("lay-verify");
        }
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('purpose1').val(purpose);
            if(purpose == "005" || purpose == "006" || purpose == "099"){
                $('#purposeDtl1').attr('lay-verify', 'required');
            }else{
                $('#purposeDtl1').removeAttr("lay-verify");
            }
        }
        form.renderIcon();
    };

    //结售汇属性回调函数
    bt.fexPropCallback1 = function (data, $elem){
        var fexProp = bt.dicts.get('fexProp1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fexProp2').val(fexProp);
        }
    };
    bt.fexPropCallback2 = function (data, $elem){
        var fexProp = bt.dicts.get('fexProp2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fexProp1').val(fexProp);
        }
    };

    //资金来源回调函数
    bt.fundsSourceCallback1 = function (data, $elem){
        var fundsSource = bt.dicts.get('fundsSource1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fundsSource2').val(fundsSource);
        }
    };
    bt.fundsSourceCallback2 = function (data, $elem){
        var fundsSource = bt.dicts.get('fundsSource2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('fundsSource1').val(fundsSource);
        }
    };

    //保证金钞汇标识回调函数
    bt.boodBankNoteCallback1 = function (data, $elem){
        var boodBankNote = bt.dicts.get('boodBankNote1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodBankNote2').val(boodBankNote)
        }
    };
    bt.boodBankNoteCallback2 = function (data, $elem){
        var boodBankNote = bt.dicts.get('boodBankNote2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.dicts.get('boodBankNote1').val(boodBankNote)
        }
    };

    //固定金额回调函数
    bt.fixMoneyCallback1 = function(data){
        //设置买入/卖出金额
        setMoneyInfo("1");
        calContractRate(1);
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //清空资金计划
            $('#budget1').val("");
            moneyType.get('sellMoney1').val('');
        }
    };
    bt.fixMoneyCallback2 = function(data){
        //设置买入/卖出金额
        setMoneyInfo("2");
        calContractRate(2);
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //清空资金计划
            $('#budget2').val("");
            moneyType.get('sellMoney2').val('');
        }
    };

    bt.dicts.init();

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
                        //清空资金计划
                        $('#budget1').val("");
                        $('#budget2').val("");
                        moneyType.get('sellMoney1').val('');
                        moneyType.get('sellMoney2').val('');
                        //现金流
                        $("#srcCorpNearTd").text(data.obj.name);
                        $("#srcCorpRemoteTd").text(data.obj.name);
                        $("#targetCorpNearTd").text(data.obj.name);
                        $("#targetCorpRemoteTd").text(data.obj.name);
                    }
                }
                form.render();
            }
        });
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: bt.selectType.get('sellCurId1').val(), corpId: id});
        selectTableObj["srcBankAccTable2"].get("srcBankAcc2").reload({curId: bt.selectType.get('sellCurId2').val(), corpId: id});
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: bt.selectType.get('buyCurId1').val(), corpId: id});
        selectTableObj["corpBankAccTable2"].get("corpBankAcc2").reload({curId: bt.selectType.get('buyCurId2').val(), corpId: id});
        //重新加载保证金账户信息
        selectTableObj["boodAccTable1"].get("boodAcc1").reload({curId: bt.selectType.get('bondCurId1').val(), corpId: id});
        selectTableObj["boodAccTable2"].get("boodAcc2").reload({curId: bt.selectType.get('bondCurId2').val(), corpId: id});

        var oppId1 = $("#oppId1").val();
        if (isEmpty(oppId1)) {
            oppId1 = null;
        }

        var oppId2 = $("#oppId2").val();
        if (isEmpty(oppId2)) {
            oppId2 = null;
        }

        //加载主体单位和交易对手账户信息
        //买入-收款
        renderBankAcc("0", id, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", oppId1);
        renderBankAcc("0", id, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","0", oppId2);
        // renderBankAcc("0", id, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", selectType.get("cotpId1").val());
        // renderBankAcc("0", id, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","0", selectType.get("cotpId2").val());
        //卖出-付款
        renderBankAcc("0", id, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", oppId1);
        renderBankAcc("0", id, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","1", oppId2);
        // renderBankAcc("0", id, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", selectType.get("cotpId1").val());
        // renderBankAcc("0", id, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","1", selectType.get("cotpId2").val());
    };
    //初始化单位
    var corp = bt.corp.init();

    bt.cotpBankCallback1 = function (data, dom) {
        if (!isEmpty(data.type)) {
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
        //加载主体单位和交易对手账户信息
        renderBankAcc("1", oppId, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
        renderBankAcc("1", oppId, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", oppId);
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", oppId);

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
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
            renderBankAcc("1", oppId, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","1", $('input[name="corpId"]').val());
            renderBankAcc("1", oppId, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","0", oppId);
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","1", oppId);
        }
    }

    bt.cotpBankCallback2 = function (data, dom) {
        if (!isEmpty(data.type)) {
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
        renderBankAcc("1", oppId, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","1", $('input[name="corpId"]').val());
        renderBankAcc("1", oppId, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","0", $('input[name="corpId"]').val());

        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","0", oppId);
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","1", oppId);

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
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
            renderBankAcc("1", oppId, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
            renderBankAcc("1", oppId, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());

            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", oppId);
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", oppId);
        }
    }

    //交易对手银行账户回调函数
    // bt.cotpCallback1 = function (data) {
    //     $("#srcCotpNearTd").text(data.cotpname);
    //     $("#targetCotpNearTd").text(data.cotpname);
    //     $("#cotpBicCode1").val(data.biccode);
    //     if(data.biccode){
    //         $("#cotpBicCodeDiv1").text(data.biccode);
    //     }else{
    //         $("#cotpBicCodeDiv1").text('');
    //     }
    //     //加载主体单位和交易对手账户信息
    //     renderBankAcc("1", data.id, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
    //     renderBankAcc("1", data.id, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
    //
    //     var isSwapTran = bt.dicts.get("isSwapTran").val();
    //     if (isSwapTran == "1") {
    //         //同步写入远端的交易对手
    //         selectType.get("cotpId2").val(data.id);
    //         $("#srcCotpRemoteTd").text(data.cotpname);
    //         $("#targetCotpRemoteTd").text(data.cotpname);
    //         $("#cotpBicCode2").val(data.biccode);
    //         if (data.biccode) {
    //             $("#cotpBicCodeDiv2").text(data.biccode);
    //         } else {
    //             $("#cotpBicCodeDiv2").text('');
    //         }
    //         //加载主体单位和交易对手账户信息
    //         renderBankAcc("1", data.id, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","1", $('input[name="corpId"]').val());
    //         renderBankAcc("1", data.id, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","0", $('input[name="corpId"]').val());
    //     }
    // };
    // bt.cotpCallback2 = function (data) {
    //     $("#srcCotpRemoteTd").text(data.cotpname);
    //     $("#targetCotpRemoteTd").text(data.cotpname);
    //     $("#cotpBicCode2").val(data.biccode);
    //     if(data.biccode) {
    //         $("#cotpBicCodeDiv2").text(data.biccode);
    //     }else{
    //         $("#cotpBicCodeDiv2").text('');
    //     }
    //     //加载主体单位和交易对手账户信息
    //     renderBankAcc("1", data.id, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"2","1", $('input[name="corpId"]').val());
    //     renderBankAcc("1", data.id, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"2","0", $('input[name="corpId"]').val());
    //
    //     var isSwapTran = bt.dicts.get("isSwapTran").val();
    //     if (isSwapTran == "1") {
    //         //同步写入近端的交易对手
    //         selectType.get("cotpId1").val(data.id);
    //         $("#srcCotpNearTd").text(data.cotpname);
    //         $("#targetCotpNearTd").text(data.cotpname);
    //         $("#cotpBicCode1").val(data.biccode);
    //         if (data.biccode) {
    //             $("#cotpBicCodeDiv1").text(data.biccode);
    //         } else {
    //             $("#cotpBicCodeDiv1").text('');
    //         }
    //         //加载主体单位和交易对手账户信息
    //         renderBankAcc("1", data.id, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
    //         renderBankAcc("1", data.id, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
    //     }
    // };

    //买入/卖出金额等设置
    function setMoneyInfo(swapType){
        var fexTranType = $("#fexTranType"+swapType).val();
        //外汇交易类型 0-即期、1-远期
        if(fexTranType=="0"){
            //“即期汇率”改为非必填
            bt.moneyType.get('spotRate'+swapType).currentObject.removeAttr('lay-verify');
            //固定金额 1-买入金额 2-卖出金额
            var fixedMoney = (bt.dicts.get('fixedMoney'+swapType).val()).toString();
            if(fixedMoney == ""){
                bt.moneyType.get('spotRate'+swapType).currentObject.removeAttr('lay-verify');
                bt.moneyType.get('buyMoney'+swapType).currentObject.removeAttr('lay-verify');
                bt.moneyType.get('sellMoney'+swapType).currentObject.removeAttr('lay-verify');
                bt.moneyType.get('contractRate'+swapType).currentObject.removeAttr('lay-verify');
                if(swapType == "1") {
                    $("#srcNearTr").hide();
                    $("#targetNearTr").hide();
                }else{
                    $("#srcRemoteTr").hide();
                    $("#targetRemoteTr").hide();
                }
            }else {
                if (fixedMoney.indexOf("1") != -1) {
                    bt.moneyType.get('buyMoney' + swapType).currentObject.attr('lay-verify', 'required');
                    if(swapType == "1") {
                        $("#srcNearTr").show();
                    }else{
                        $("#srcRemoteTr").show();
                    }
                } else {
                    bt.moneyType.get('buyMoney' + swapType).currentObject.removeAttr('lay-verify');
                    if(swapType == "1") {
                        $("#srcNearTr").hide();
                    }else{
                        $("#srcRemoteTr").hide();
                    }
                }
                if (fixedMoney.indexOf("2") != -1) {
                    bt.moneyType.get('sellMoney' + swapType).currentObject.attr('lay-verify', 'required');
                    if(swapType == "1") {
                        $("#targetNearTr").show();
                    }else{
                        $("#targetRemoteTr").show();
                    }
                } else {
                    bt.moneyType.get('sellMoney' + swapType).currentObject.removeAttr('lay-verify');
                    if(swapType == "1") {
                        $("#targetNearTr").hide();
                    }else{
                        $("#targetRemoteTr").hide();
                    }
                }
                if (fixedMoney.indexOf("1") != -1 && fixedMoney.indexOf("2") != -1) {
                    bt.moneyType.get('contractRate' + swapType).currentObject.attr('lay-verify', 'required');
                } else {
                    bt.moneyType.get('contractRate' + swapType).currentObject.removeAttr('lay-verify');
                }
            }
        }else{
            bt.moneyType.get('spotRate'+swapType).currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('buyMoney'+swapType).currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('sellMoney'+swapType).currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('contractRate'+swapType).currentObject.attr('lay-verify', 'required');
            if(swapType == "1") {
                $("#srcNearTr").show();
                $("#targetNearTr").show();
            }else {
                $("#srcRemoteTr").show();
                $("#targetRemoteTr").show();
            }
        }
        form.renderIcon();
    }

    function resetFixedMoney(isRadio,elem){
        var type = isRadio? 'radio':'checkbox';
        elem.data('type',type).html('').removeClass('init');
        bt.dicts.init();
    }

    //产品回调函数
    bt.proCallback1 = function (data) {
        $("#fexBusType1").val(data.fexbustype);
        //所选产品为结汇时，默认自动计算买入金额；所选产品为购汇时，默认自动计算卖出金额
        if($("#fexBusType1").val() == "2"){//购汇
            bt.dicts.get("autoCal1").val("2");
            //结售汇属性设置为购汇的字典
            bt.dicts.get("fexProp1")['fex.tranApp.fexProp'] = null;
            bt.dicts.get("fexProp1").attr({'data-group': 'fex.tranApp.fexProp2'});
        } else {
            bt.dicts.get("autoCal1").val("1");
            //结售汇属性设置为结汇的字典
            bt.dicts.get("fexProp1")['fex.tranApp.fexProp2'] = null;
            bt.dicts.get("fexProp1").attr({'data-group': 'fex.tranApp.fexProp'});
        }
        $("#fexTranType1").val(data.fextrantype);
        $("#deliveryType1").val(data.deliverytype);
        $("#fexBusTypeDiv1").text(bt.dict("fexProduct.fexBusType", data.fexbustype));
        $("#fexTranTypeDiv1").text(bt.dict("fexProduct.fexTranType", data.fextrantype));
        $("#deliveryTypeDiv1").text(bt.dict("fexProduct.deliveryType", data.deliverytype));

        if(data.buycurid){
            selectType.get('buyCurId1').val(data.buycurid);
            //现金流信息
            $("#srcCurNearTd").text(data.buyenglishcode);
        }else{
            selectType.get('buyCurId1').val('');
            //现金流信息
            $("#srcCurNearTd").text('');
        }
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: selectType.get('buyCurId1').val(), corpId: $('input[name="corpId"]').val()});
        if(data.sellcurid){
            selectType.get('sellCurId1').val(data.sellcurid);
            //现金流信息
            $("#targetCurNearTd").text(data.sellenglishcode);
        }else{
            selectType.get('sellCurId1').val('');
            //现金流信息
            $("#targetCurNearTd").text('');
        }
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: selectType.get('sellCurId1').val(), corpId: $('input[name="corpId"]').val()});

        var oppId1 = $("#oppId1").val();
        if (isEmpty(oppId1)) {
            oppId1 = null;
        }

        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),data.id,"1","0", oppId1);
        renderBankAcc("1", oppId1, selectType.get("buyCurId1").val(),data.id,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),data.id,"1","1", oppId1);
        renderBankAcc("1", oppId1, selectType.get("sellCurId1").val(),data.id,"1","0", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),data.id,"1","0", selectType.get("cotpId1").val());
        // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("buyCurId1").val(),data.id,"1","1", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),data.id,"1","1", selectType.get("cotpId1").val());
        // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("sellCurId1").val(),data.id,"1","0", $('input[name="corpId"]').val());
        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull1").hide();
            $("#deliveryCurTd1").show();
            $("#deliveryCurDiv1").show();
            selectType.get('deliveryCurId1').attr({'data-required':true,'data-value':data.deliverycurid});
        } else {
            $("#deliveryCurTdNull1").show();
            $("#deliveryCurTd1").hide();
            $("#deliveryCurDiv1").hide();
            selectType.get('deliveryCurId1').attr({'data-required':false,'data-value':data.deliverycurid});
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
            $('#purposeDtl1').removeAttr("lay-verify");
            $('#purposeDtl1').val('');
        }
        //远期，固定金额只能单选
        resetFixedMoney(data.fextrantype=="1", $('#fixedMoney1'));
        //设置买入/卖出金额
        setMoneyInfo("1");
        form.renderIcon();
        form.render();
    };
    bt.proCallback2 = function (data) {
        $("#fexBusType2").val(data.fexbustype);
        //所选产品为结汇时，默认自动计算买入金额；所选产品为购汇时，默认自动计算卖出金额
        if($("#fexBusType2").val() == "2"){//购汇
            bt.dicts.get("autoCal2").val("2");
            //结售汇属性设置为购汇的字典
            bt.dicts.get("fexProp2")['fex.tranApp.fexProp'] = null;
            bt.dicts.get("fexProp2").attr({'data-group': 'fex.tranApp.fexProp2'});
        } else {
            bt.dicts.get("autoCal2").val("1");
            //结售汇属性设置为结汇的字典
            bt.dicts.get("fexProp2")['fex.tranApp.fexProp2'] = null;
            bt.dicts.get("fexProp2").attr({'data-group': 'fex.tranApp.fexProp'});
        }
        $("#fexTranType2").val(data.fextrantype);
        $("#deliveryType2").val(data.deliverytype);
        $("#fexBusTypeDiv2").text(bt.dict("fexProduct.fexBusType", data.fexbustype));
        $("#fexTranTypeDiv2").text(bt.dict("fexProduct.fexTranType", data.fextrantype));
        $("#deliveryTypeDiv2").text(bt.dict("fexProduct.deliveryType", data.deliverytype));

        if(data.buycurid){
            selectType.get('buyCurId2').val(data.buycurid);
            //现金流信息
            $("#srcCurRemoteTd").text(data.buyenglishcode);
        }else{
            selectType.get('buyCurId2').val('');
            //现金流信息
            $("#srcCurRemoteTd").text('');
        }
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable2"].get("corpBankAcc2").reload({curId: selectType.get('buyCurId2').val(), corpId: $('input[name="corpId"]').val()});
        if(data.sellcurid){
            selectType.get('sellCurId2').val(data.sellcurid);
            //现金流信息
            $("#targetCurRemoteTd").text(data.sellenglishcode);
        }else{
            selectType.get('sellCurId2').val('');
            //现金流信息
            $("#targetCurRemoteTd").text('');
        }
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable2"].get("srcBankAcc2").reload({curId: selectType.get('sellCurId2').val(), corpId: $('input[name="corpId"]').val()});
        var oppId2 = $("#oppId2").val();
        if (isEmpty(oppId2)) {
            oppId2 = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),data.id,"2","0", oppId2);
        renderBankAcc("1", oppId2, selectType.get("buyCurId2").val(),data.id,"2","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),data.id,"2","1", oppId2);
        renderBankAcc("1", oppId2, selectType.get("sellCurId2").val(),data.id,"2","0", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),data.id,"2","0", selectType.get("cotpId2").val());
        // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("buyCurId2").val(),data.id,"2","1", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),data.id,"2","1", selectType.get("cotpId2").val());
        // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("sellCurId2").val(),data.id,"2","0", $('input[name="corpId"]').val());

        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull2").hide();
            $("#deliveryCurTd2").show();
            $("#deliveryCurDiv2").show();
            selectType.get('deliveryCurId2').attr({'data-required':true,'data-value':data.deliverycurid});
        } else {
            $("#deliveryCurTdNull2").show();
            $("#deliveryCurTd2").hide();
            $("#deliveryCurDiv2").hide();
            selectType.get('deliveryCurId2').attr({'data-required':false,'data-value':data.deliverycurid});
        }
        //结汇
        if(data.fexbustype=="1"){
            $("#settleBusTypeTdNull2").hide();
            $("#settleBusTypeTd2").show();
            $("#settleBusTypeDiv2").show();
            bt.dicts.get('settleBusType2').attr({'data-required': true});
            $("#purposeTr2").show();
        }else{
            $("#settleBusTypeTdNull2").show();
            $("#settleBusTypeTd2").hide();
            $("#settleBusTypeDiv2").hide();
            bt.dicts.get('settleBusType2').attr({'data-required': false});
            $("#purposeTr2").hide();
            bt.dicts.get('purpose2').val('');
            $('#purposeDtl2').removeAttr("lay-verify");
            $('#purposeDtl2').val('');
        }
        //远期，固定金额只能单选
        resetFixedMoney(data.fextrantype=="1", $('#fixedMoney1'));
        //设置买入/卖出金额
        setMoneyInfo("2");
        form.renderIcon();
        form.render();
    };

    //结算日期回调函数
    settleDateCallback1 = function (data, $elem){
        var settleDate = dateType.get('settleDate1').val();
        var tranDate = dateType.get('tranDate1').val();
        var deliveryDeadline = dateFun.getBetweenDay(tranDate, settleDate);
        $("#deliveryDeadline1").val(deliveryDeadline);
        $("#deliveryDeadlineDiv1").text('T+'+deliveryDeadline);
        //保证金相关信息设置
        setBoodInfo("1");
        //现金流
        $("#srcSettleDateNearTd").text(settleDate);
        $("#targetSettleDateNearTd").text(settleDate);
    };
    settleDateCallback2 = function (data, $elem){
        var settleDate = dateType.get('settleDate2').val();
        var tranDate = dateType.get('tranDate2').val();
        var deliveryDeadline = dateFun.getBetweenDay(tranDate, settleDate);
        $("#deliveryDeadline2").val(deliveryDeadline);
        $("#deliveryDeadlineDiv2").text('T+'+deliveryDeadline);
        //保证金相关信息设置
        setBoodInfo("2");
        //现金流
        $("#srcSettleDateRemoteTd").text(settleDate);
        $("#targetSettleDateRemoteTd").text(settleDate);
    };

    //根据交割期限设置保证金相关信息
    function setBoodInfo(swapType){
        var deliveryDeadline = $('#deliveryDeadline'+swapType).val();
        var boodType = bt.dicts.get('boodType'+swapType).val();
        //非T+0且保证金方式不为授信额度时：保证方式、保证金账号、保证比例、保证金钞汇标识、保证金币种都为必填
        if(deliveryDeadline>0 && boodType!="3" && boodType!="4") {
            bt.dicts.get('boodType'+swapType).setRequired(true);
            $('#boodAcc'+swapType).attr('lay-verify', 'required|tableData');
            bt.ratioType.get('boodRatio'+swapType).currentObject.attr('lay-verify', 'required');
            bt.dicts.get('boodBankNote'+swapType).attr({'data-required': true});
            selectType.get('bondCurId'+swapType).attr('data-required', true);
        }else{
            if(deliveryDeadline>0) {
                bt.dicts.get('boodType'+swapType).setRequired(true);
                bt.ratioType.get('boodRatio'+swapType).currentObject.attr('lay-verify', 'required');
            }else{
                bt.dicts.get('boodType' + swapType).setRequired(false);
                bt.ratioType.get('boodRatio'+swapType).currentObject.removeAttr('lay-verify');
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
    calContractRate = function(swapType) {
        //外汇买卖交易的交易类型为“即期”且固定金额全选时，才自动计算买入金额、卖出金额、合同汇率
        /*var fexTranType = $("#fexTranType"+swapType).val();
        var fixedMoney =  bt.dicts.get('fixedMoney' + swapType).val()+"";
        if(fexTranType == "0" && (fixedMoney == "" || fixedMoney.indexOf("1") == -1 || fixedMoney.indexOf("2") == -1)){
            return;
        }*/
        var buyCurId = selectType.get("buyCurId"+swapType).val();
        var buyMoney = moneyType.get("buyMoney"+swapType).val();
        var sellCurId = selectType.get("sellCurId"+swapType).val();
        var sellMoney = moneyType.get("sellMoney"+swapType).val();
        if (isNotEmpty(buyCurId) && isNotEmpty(sellCurId)) {
            $.ajax({
                "url": basePath + "/basic/business/btcurrencypair/getByCurId",
                data:{
                    curId1: buyCurId,
                    curId2: sellCurId
                },
                type:"post",
                dataType:"json",
                async: false,
                success:function (data) {
                    if (data.success) {
                        if (null == data.obj) {
                            parent.layer.alert(bt.lang("fex.tranApp.currencyPair"), {icon: 2});
                            moneyType.get("contractRate"+swapType).val("");
                        } else {
                            setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data.obj,swapType);
                        }
                    }
                }
            });
        }
    }

    /**
     * 汇率计算
     * @param buyCurId
     * @param sellCurId
     * @param buyMoney
     * @param sellMoney
     * @param data
     */
    function setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data,swapType) {
        //var autoCal = $("input[type = 'checkbox'][name = 'autoCal"+swapType+"']:checked");
        var autoCal = bt.dicts.get("autoCal"+swapType).val();
        var contractRate = moneyType.get("contractRate"+swapType).val();
        if (isNotEmpty(contractRate)) {
            // 合同汇率存在值
            if (autoCal == "1") {//计算买入
                if (isNotEmpty(sellMoney)) {
                    if (data.baseCurId == buyCurId) {//买入币种为标准
                        moneyType.get("buyMoney" + swapType).val(bt.money.div(sellMoney, contractRate, 2));
                    } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                        moneyType.get("buyMoney" + swapType).val(bt.money.mul(sellMoney, contractRate, 2));
                    }
                }
            } else {//计算卖出
                if (isNotEmpty(buyMoney)) {
                    if (data.baseCurId == buyCurId) {//买入币种为标准
                        moneyType.get("sellMoney" + swapType).val(bt.money.mul(buyMoney, contractRate, 2));
                    } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                        moneyType.get("sellMoney" + swapType).val(bt.money.div(buyMoney, contractRate, 2));
                    }
                }
            }
        } else {
            if (isNotEmpty(buyMoney) && isNotEmpty(sellMoney)) {
                // 合同汇率不存在值
                if (data.baseCurId == buyCurId) {//买入币种为标准
                    moneyType.get("contractRate" + swapType).val(bt.money.div(sellMoney, buyMoney, 6));
                } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                    moneyType.get("contractRate" + swapType).val(bt.money.div(buyMoney, sellMoney, 6));
                }
            }
        }
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

    //合同汇率回调函数
    bt.contractRateCallback1 = function (data) {
        calContractRate(1);
    };
    bt.contractRateCallback2 = function (data) {
        calContractRate(2);
    };
    //买入币别回调函数
    bt.buyCurCallback1 = function (data) {
        calContractRate(1);
        //现金流信息
        $("#srcCurNearTd").text(data.englishcode);

        var oppId1 = $("#oppId1").val();
        if (isEmpty(oppId1)) {
            oppId1 = null;
        }

        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", oppId1);
        renderBankAcc("1", oppId1, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", selectType.get("cotpId1").val());
        // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: selectType.get('buyCurId1').val(), corpId: $('input[name="corpId"]').val()});

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //近端买入币种写入远端卖出币种
            selectType.get("sellCurId2").val(data.id);
            calContractRate(2);
            //现金流信息
            $("#targetCurRemoteTd").text(data.englishcode);
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                //清空资金计划
                $('#budget2').val("");
                moneyType.get('sellMoney2').val('');
            }
            var oppId2 = $("#oppId2").val();
            if (isEmpty(oppId2)) {
                oppId2 = null;
            }
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","1", oppId2);
            renderBankAcc("1", oppId2, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","0", $('input[name="corpId"]').val());
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","1", selectType.get("cotpId2").val());
            // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","0", $('input[name="corpId"]').val());
            //重新加载付款账户信息
            selectTableObj["srcBankAccTable2"].get("srcBankAcc2").reload({curId: selectType.get('sellCurId2').val(), corpId: $('input[name="corpId"]').val()});

        }
        form.render();
    };
    bt.buyCurCallback2 = function (data) {
        calContractRate(2);
        //现金流信息
        $("#srcCurRemoteTd").text(data.englishcode);
        var oppId2 = $("#oppId2").val();
        if (isEmpty(oppId2)) {
            oppId2 = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","0", oppId2);
        renderBankAcc("1", oppId2, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","1", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","0", selectType.get("cotpId2").val());
        // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","1", $('input[name="corpId"]').val());
        //重新加载收款账户信息
        selectTableObj["corpBankAccTable2"].get("corpBankAcc2").reload({curId: selectType.get('buyCurId2').val(), corpId: $('input[name="corpId"]').val()});

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //远端买入币种写入近端卖出币种
            selectType.get("sellCurId1").val(data.id);
            calContractRate(1);
            //现金流信息
            $("#targetCurNearTd").text(data.englishcode);
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                //清空资金计划
                $('#budget1').val("");
                moneyType.get('sellMoney1').val('');
            }
            var oppId1 = $("#oppId1").val();
            if (isEmpty(oppId1)) {
                oppId1 = null;
            }
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", oppId1);
            renderBankAcc("1", oppId1, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", selectType.get("cotpId1").val());
            // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
            //重新加载付款账户信息
            selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: selectType.get('sellCurId1').val(), corpId: $('input[name="corpId"]').val()});
        }
        form.render();
    };
    //卖出币别回调函数
    bt.sellCurCallback1 = function (data) {
        calContractRate(1);
        //现金流信息
        $("#targetCurNearTd").text(data.englishcode);
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //清空资金计划
            $('#budget1').val("");
            moneyType.get('sellMoney1').val('');
        }
        var oppId1 = $("#oppId1").val();
        if (isEmpty(oppId1)) {
            oppId1 = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", oppId1);
        renderBankAcc("1", oppId1, selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","1", selectType.get("cotpId1").val());
        // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("sellCurId1").val(),selectType.get("proId1").val(),"1","0", $('input[name="corpId"]').val());
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable1"].get("srcBankAcc1").reload({curId: selectType.get('sellCurId1').val(), corpId: $('input[name="corpId"]').val()});

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //近端卖出币种写入远端买入币种
            selectType.get("buyCurId2").val(data.id);
            calContractRate(2);
            //现金流信息
            $("#srcCurRemoteTd").text(data.englishcode);
            var oppId2 = $("#oppId2").val();
            if (isEmpty(oppId2)) {
                oppId2 = null;
            }
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","0", oppId2);
            renderBankAcc("1", oppId2, selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","1", $('input[name="corpId"]').val());
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","0", selectType.get("cotpId2").val());
            // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("buyCurId2").val(),selectType.get("proId2").val(),"1","1", $('input[name="corpId"]').val());
            //重新加载收款账户信息
            selectTableObj["corpBankAccTable2"].get("corpBankAcc2").reload({curId: selectType.get('buyCurId2').val(), corpId: $('input[name="corpId"]').val()});

        }
        form.render();
    };
    bt.sellCurCallback2 = function (data) {
        calContractRate(2);
        //现金流信息
        $("#targetCurRemoteTd").text(data.englishcode);
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //清空资金计划
            $('#budget2').val("");
            moneyType.get('sellMoney2').val('');
        }
        var oppId2 = $("#oppId2").val();
        if (isEmpty(oppId2)) {
            oppId2 = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","1", oppId2);
        renderBankAcc("1", oppId2, selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","0", $('input[name="corpId"]').val());
        // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","1", selectType.get("cotpId2").val());
        // renderBankAcc("1", selectType.get("cotpId2").val(), selectType.get("sellCurId2").val(),selectType.get("proId2").val(),"1","0", $('input[name="corpId"]').val());
        //重新加载付款账户信息
        selectTableObj["srcBankAccTable2"].get("srcBankAcc2").reload({curId: selectType.get('sellCurId2').val(), corpId: $('input[name="corpId"]').val()});

        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //远端卖出币种写入近端买入币种
            selectType.get("buyCurId1").val(data.id);
            calContractRate(1);
            //现金流信息
            $("#srcCurNearTd").text(data.englishcode);
            var oppId1 = $("#oppId1").val();
            if (isEmpty(oppId1)) {
                oppId1 = null;
            }
            //加载主体单位和交易对手账户信息
            renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", oppId1);
            renderBankAcc("1", oppId1, selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
            // renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","0", selectType.get("cotpId1").val());
            // renderBankAcc("1", selectType.get("cotpId1").val(), selectType.get("buyCurId1").val(),selectType.get("proId1").val(),"1","1", $('input[name="corpId"]').val());
            //重新加载收款账户信息
            selectTableObj["corpBankAccTable1"].get("corpBankAcc1").reload({curId: selectType.get('buyCurId1').val(), corpId: $('input[name="corpId"]').val()});

        }
        form.render();
    };

    //金额回调函数
    bt.buyMoneyCallback1 = function (data) {
        var buyMoney = moneyType.get('buyMoney1').val();
        calContractRate(1);
        $("#srcPayMoneyNearTd").text(moneyFun.format(buyMoney));
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //近端买入金额写入远端卖出金额
            moneyType.get('sellMoney2').val(buyMoney);
            calContractRate(2);
            $("#targetPayMoneyRemoteTd").text('-' + moneyFun.format(moneyType.get('sellMoney2').val()));
        }
        form.render();
    };
    bt.buyMoneyCallback2 = function (data) {
        var buyMoney = moneyType.get('buyMoney2').val();
        calContractRate(2);
        $("#srcPayMoneyRemoteTd").text(moneyFun.format(buyMoney));
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //远端买入金额写入近端卖出金额
            moneyType.get('sellMoney1').val(buyMoney);
            calContractRate(1);
            $("#targetPayMoneyNearTd").text('-' + moneyFun.format(moneyType.get('sellMoney1').val()));
        }
        form.render();
    };
    bt.sellMoneyCallback1 = function (data) {
        var sellMoney = moneyType.get('sellMoney1').val();
        calContractRate(1);
        $("#targetPayMoneyNearTd").text('-' + moneyFun.format(sellMoney));
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //近端卖出金额写入远端买入金额
            moneyType.get('buyMoney2').val(sellMoney);
            calContractRate(2);
            $("#srcPayMoneyRemoteTd").text(moneyFun.format(moneyType.get('buyMoney2').val()));
        }
        form.render();
    };
    bt.sellMoneyCallback2 = function (data) {
        var sellMoney = moneyType.get('sellMoney2').val();
        calContractRate(2);
        $("#targetPayMoneyRemoteTd").text('-' + moneyFun.format(sellMoney));
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            //远端卖出金额写入近端买入金额
            moneyType.get('buyMoney1').val(sellMoney);
            calContractRate(1);
            $("#srcPayMoneyNearTd").text(moneyFun.format(moneyType.get('buyMoney1').val()));
        }
        form.render();
    };

    //保证金比例回调函数
    bt.boodRatioCallback1 = function (data) {
        var boodRatio = bt.ratioType.get('boodRatio1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.ratioType.get('boodRatio2').val(boodRatio);
        }
    }
    bt.boodRatioCallback2 = function (data) {
        var boodRatio = bt.ratioType.get('boodRatio2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            bt.ratioType.get('boodRatio1').val(boodRatio);
        }
    }

    //额度回调函数
    bt.payMoneyCallback1 = function (data) {
        var payMoney = moneyType.get('payMoney1').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('payMoney2').val(payMoney);
        }
    }
    bt.payMoneyCallback2 = function (data) {
        var payMoney = moneyType.get('payMoney2').val();
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            moneyType.get('payMoney1').val(payMoney);
        }
    }
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

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
            $.ajax({
                url: basePath + "/foreign/fexcorpbankacc/getNewBankAcc",
                method: 'POST',
                data: {busType: busType, tranType: "1", corpId: corpId, curId: curId, proId: proId, cashFlowType: "0", cdSign: cdSign, oppCorpId: oppCorpId},
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
                                //现金流信息
                                if (swaptype == "1") {
                                    //近端
                                    if (data.obj.bankAcc) {
                                        $("#targetCotpAccNameNearTd").text(data.obj.accName);
                                        $("#targetCotpBankAccNearTd").text(data.obj.bankAcc);
                                        // $("#targetCotpBankNearTd").text(data.obj.bankTypeName);
                                    }/* else {
                                        $("#targetCotpAccNameNearTd").text("");
                                        $("#targetCotpBankAccNearTd").text("");
                                        $("#targetCotpBankNearTd").text("");
                                    }*/
                                } else {
                                    if (data.obj.bankAcc) {
                                        $("#targetCotpAccNameRemoteTd").text(data.obj.accName);
                                        $("#targetCotpBankAccRemoteTd").text(data.obj.bankAcc);
                                        // $("#targetCotpBankRemoteTd").text(data.obj.bankTypeName);
                                    }/* else {
                                        $("#targetCotpAccNameRemoteTd").text("");
                                        $("#targetCotpBankAccRemoteTd").text("");
                                        $("#targetCotpBankRemoteTd").text("");
                                    }*/
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
                                //现金流信息
                                if (swaptype == "1") {
                                    //近端
                                    if (data.obj.bankAcc) {
                                        // $("#srcCotpBankNearTd").text(data.obj.bankTypeName);
                                        $("#srcCotpAccNameNearTd").text(data.obj.accName);
                                        $("#srcCotpBankAccNearTd").text(data.obj.bankAcc);
                                    } /*else {
                                        $("#srcCotpBankNearTd").text("");
                                        $("#srcCotpAccNameNearTd").text("");
                                        $("#srcCotpBankAccNearTd").text("");
                                    }*/
                                } else {
                                    if (data.obj.bankAcc) {
                                        // $("#srcCotpBankRemoteTd").text(data.obj.bankTypeName);
                                        $("#srcCotpAccNameRemoteTd").text(data.obj.accName);
                                        $("#srcCotpBankAccRemoteTd").text(data.obj.bankAcc);
                                    } /*else {
                                        $("#srcCotpBankRemoteTd").text("");
                                        $("#srcCotpAccNameRemoteTd").text("");
                                        $("#srcCotpBankAccRemoteTd").text("");
                                    }*/
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    //监听提交
    form.on('submit(submit)', function(data){
        //控制重复提交
        if($(data.elem).parent().hasClass('isSubmiting')){
            bt.loading.hide();
            return false;
        }
        bt.loading.show();
        if (data.field.fixedMoney2) {
            data.field.fixedMoney2 = data.field.fixedMoney2 ?.join(',') || '';
        }
        //增加已提交的标志
        $(data.elem).parent().addClass('isSubmiting');
        $("#status").val($(this).attr("value"));
        $("#attachmentId1").val(selectTableObj["up1"].getGroupId());
        data.field.status = $(this).attr("value");
        data.field.attachmentId1 = selectTableObj["up1"].getGroupId();
        var fexTranType1 = $("#fexTranType1").val();
        //外汇交易类型 0-即期、1-远期
        if(fexTranType1=="0") {
            if (bt.dicts.get('fixedMoney1').val()) {
                data.field.fixedMoney1 = (bt.dicts.get('fixedMoney1').val()).toString();
            } else {
                data.field.fixedMoney1 = "";
            }
        }
        var corpAccId1 = $("#corpAccId1").val();
        var corpBankAcc1 = $("#corpBankAcc1").val();
        if(!corpAccId1 || !corpBankAcc1){
            bt.loading.hide();
            layer.alert(bt.lang("fex.cash.flow.message.bankacc.match"), {icon: 2});
            return false;
        }
        var isSwapTran = bt.dicts.get("isSwapTran").val();
        if (isSwapTran == "1") {
            $("#attachmentId2").val(selectTableObj["up2"].getGroupId());
            data.field.attachmentId2 = selectTableObj["up2"].getGroupId();
            var fexTranType2 = $("#fexTranType2").val();
            //外汇交易类型 0-即期、1-远期
            if(fexTranType2=="0") {
                if (bt.dicts.get('fixedMoney2').val()) {
                    data.field.fixedMoney2 = (bt.dicts.get('fixedMoney2').val()).toString();
                } else {
                    data.field.fixedMoney2 = "";
                }
            }
            var corpAccId2 = $("#corpAccId2").val();
            var corpBankAcc2 = $("#corpBankAcc2").val();
            if(!corpAccId2 || !corpBankAcc2){
                bt.loading.hide();
                layer.alert(bt.lang("fex.cash.flow.message.bankacc.match"), {icon: 2});
                return false;
            }
        }
        //证书签名
        var encrypt = $("#encrypt").val(); // cfca证书
        if(encrypt == 2 || encrypt == 1){ // 1 天威证书
            bt.loading.hide();
            var signDataMsg = signData("外汇交易提交", 11,function () {
                bt.loading.show();
                $.post(basePath + "/foreign/fextranreq/save",data.field,function(result){
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                        if(result.success){
                            if(bt.navtab) {
                                bt.navtab.refreshParentTable();
                                bt.navtab.refreshCurrent();
                            }else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                        parent.layer.closeAll();
                    })
                },'JSON');
            });
        }else{
            var signDataMsg = signData("外汇交易提交", 11);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            bt.loading.show();
            var action = "/foreign/fextranreq/save";
            $.post(basePath + action,data.field,function(result){
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                    if(result.success){
                        if(bt.navtab) {
                            bt.navtab.refreshParentTable();
                            bt.navtab.refreshCurrent();
                        }else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                    parent.layer.closeAll();
                })
            },'JSON');
        }

        setTimeout(function () {
            //解除已提交的标志
            $(data.elem).parent().removeClass('isSubmiting');
        },300)
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
        //重新设置时间戳
        var timeStamp = Date.parse(new Date());
        $('#timeStamp').val(timeStamp);
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
        var url = basePath + "/foreign/fextranreq/addTranReqForeign/1?timestamp=" + new Date().getTime();
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
        var url = basePath + "/foreign/fextranreq/addTranReqForeign/2?timestamp=" + new Date().getTime();
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

});

/**
 * 是否掉期交易
 */
isSwapTranCallBack = function (data) {
    var isSwapTran = bt.dicts.get("isSwapTran").val();
    if (isSwapTran == "1") {
        $("#tranTb").show();
        $("#remoteMsg").removeClass("className");
    } else {
        $("#tranTb").hide();
        $("#remoteMsg").addClass("ignore-verify");
    }
}

//文本框复制
function cloneSwapTran(swapType, filedName){
    var filedVal = $("#"+filedName+swapType).val();
    var isSwapTran = bt.dicts.get("isSwapTran").val();
    if (isSwapTran == "1") {
        if(swapType == "1") {
            $("#"+filedName+"2").val(filedVal);
        } else {
            $("#"+filedName+"1").val(filedVal);
        }
    }
}
