bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','ratioType','element', 'selectTable','dateType','corp', 'btUpload','checkboxs','allBankInfo'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.rateType.init();
    bt.ratioType.init();
    form.renderIcon();
    bt.allBankInfo.init();

    //汇款类型回调函数
    bt.remitTypeCallback = function(data){
        //汇款类型=2-境外账户汇款时，“其它信息”非必填，并默认为折叠状态
        var remitType = bt.dicts.get("remitType").val();
        if (remitType == "2") {
            $("#reportInfoDiv").removeClass("layui-show");
            $("#reportInfoDiv").addClass("ignore-verify");
            $("#reportInfoItemDiv").hide();
        } else {
            $("#reportInfoDiv").addClass("layui-show");
            $("#reportInfoDiv").removeClass("className");
            $("#reportInfoItemDiv").show();
        }

        var countryId = "CN";
        if ($('#corpIsDomesticBank').val() == "0") {
            // 境外账户
            countryId = $('#corpCountryId').val();
        }
        var bankTypeId = $('#corpBank').val();
        var bifCode = $('#corpBifCode').val();
        setBankTypeCustom(bankTypeId, bifCode, countryId, '', remitType, "other");
        form.renderIcon();
    }

    //保证金方式回调函数
    bt.boodTypeCallback = function (data, $elem){
        var boodType = bt.dicts.get('boodType').val();
        if(boodType == "3"){
            $('#boodRmk').attr('lay-verify', 'required');
        }else{
            $('#boodRmk').removeAttr("lay-verify");
        }
        if(boodType == "4"){
            moneyType.get('payMoney').attr('lay-verify', 'required|number2');
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            selectType.get('bondCurId').attr('data-required', false);
        }else{
            moneyType.get('payMoney').attr('lay-verify', 'number2');
            var deliveryDeadline = $('#deliveryDeadline').val();
            if(deliveryDeadline>0 && boodType != "3") {
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
            $('#purposeDtl').attr('lay-verify', 'required');
        }else{
            $('#purposeDtl').removeAttr("lay-verify");
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

    bt.dicts.init();

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    //交易需求过来的数据或者外汇期权过来大数据，进入页面自动匹配主体账号（交易需求自动带出即可）
    if(($("#tranAppDtlId").val() && $("#corpAccId").val()=="") || ($("#tranAppDtlId").val() && $("#tranAppDtlNo").val()=="")){

        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('#corpIdDef').val(), $('#buyCurIdDef').val(),$('#proIdDef').val(),"1","0", oppId);
        renderBankAcc("1", oppId, $('#buyCurIdDef').val(),$('#proIdDef').val(),"1","1", $('#corpIdDef').val());
        renderBankAcc("0", $('#corpIdDef').val(), $('#sellCurIdDef').val(),$('#proIdDef').val(),"1","1", oppId);
        renderBankAcc("1", oppId, $('#sellCurIdDef').val(),$('#proIdDef').val(),"1","0", $('#corpIdDef').val());
        // renderBankAcc("0", $('#corpIdDef').val(), $('#buyCurIdDef').val(),$('#proIdDef').val(),"1","0", $('#cotpIdDef').val());
        // renderBankAcc("1", $('#cotpIdDef').val(), $('#buyCurIdDef').val(),$('#proIdDef').val(),"1","1", $('#corpIdDef').val());
        // renderBankAcc("0", $('#corpIdDef').val(), $('#sellCurIdDef').val(),$('#proIdDef').val(),"1","1", $('#cotpIdDef').val());
        // renderBankAcc("1", $('#cotpIdDef').val(), $('#sellCurIdDef').val(),$('#proIdDef').val(),"1","0", $('#corpIdDef').val());
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
                        $('#corpContact').val("");
                        $('#corpContactTel').val("");
                        //重新加载联系人
                        corpContactInit.get("corpContact").reload({corpId: id});
                        //加载资金计划
                        renderBudget();
                    }
                }
                form.render();
            }
        });
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: bt.selectType.get('sellCurId').val(), corpId: id});
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: bt.selectType.get('buyCurId').val(), corpId: id});
        //重新加载保证金账户信息
        boodAccInit.get("boodAcc").reload({curId: bt.selectType.get('bondCurId').val(), corpId: id});

        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }

        //加载主体单位和交易对手账户信息
        renderBankAcc("0", id, selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","0", oppId);
        renderBankAcc("0", id, selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","1", oppId);
        // renderBankAcc("0", id, selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","0", selectType.get("cotpId").val());
        // renderBankAcc("0", id, selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","1", selectType.get("cotpId").val());
    };
    //初始化单位
    var corp = bt.corp.init();



    //自动判断跨行标志
    function setDifBank(){
        //自动校验跨行标志
        var corpBank = $('#corpBank').val();
        var srcBank = $('#srcBank').val();
        if(corpBank == srcBank){
            bt.dicts.get('difBank').val('0');
        } else {
            bt.dicts.get('difBank').val('1');
        }
    }

    //付款账户信息
    var srcBankAccInit = bt.selectTable.init({
        elem: "#srcBankAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#sellCurIdDef").val(), corpId: $("#corpIdDef").val()},
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
                selectType.get('sellCurId').val(data.curId);
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
                //自动判断跨行标志
                setDifBank();
                //现金流信息
                $("#targetAccNameTd").text(data.accName);
                $("#targetBankAccTd").text(data.bankAcc);
                $("#targetBankTd").text(data.bankTypeName);
                $("#targetCurTd").text(data.curEnglishCode);
            }else{
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
     * @param remitType 汇款类型
     * @param initCustomType 初始化标识 默认-初始化全部 payeeType-只初始化收款人类型 other-只初始化其他信息
     */
    function setBankTypeCustom(bankType, bifCode, countryId, bankPayType, remitType, initCustomType) {
        // 重新支付类型
        var bankPayTypeUrl = basePath + "/basic/business/btbanktype/listBankTypeCustom?bankType=" + bankType + "&bifCode=" + bifCode + "&countryId=" + countryId + "&customType=bankPayType";
        //获取银行类型信息
        $.ajax({
            url: basePath + "/basic/business/btbanktype/getBankType",
            method: 'POST',
            data: {bankTypeId: bankType},
            dataType: 'json',
            success: function (data) {
                if (data) {
                    if (initCustomType != "other") {
                        // 是否配置支付类型 为 1-是；支付类型应用范围 包含 1-外汇买卖交易
                        if (data.isSetBankPayType == "1" && data.applayScopeBankPayType.indexOf("1") != -1) {
                            if (data.requiredBankPayType == "1") {
                                selectType.get('bankPayType').attr({
                                    "data-url": bankPayTypeUrl,
                                    "data-required": true
                                });
                            } else {
                                selectType.get('bankPayType').attr({
                                    "data-url": bankPayTypeUrl,
                                    "data-required": false
                                });
                            }
                            $("#bankPayTypeTr").show();
                        } else {
                            selectType.get('bankPayType').attr({
                                "data-url": bankPayTypeUrl,
                                "data-required": false
                            });
                            $("#bankPayTypeTr").hide();
                        }
                    }

                    // 是否配置其他信息模块  为 1-是；适用页面 包含 2-资金调拨交易
                    if (data.isSetOtherCustom == "1") {
                        $.ajax({
                            url: basePath + "/basic/business/btbanktype/getBankTypeOtherCustom",
                            method: 'POST',
                            data: {
                                bankType: bankType,
                                bifCode: bifCode,
                                countryId: countryId,
                                remitType: remitType,
                                applayScope: "1"
                            },
                            dataType: 'json',
                            success: function (data) {
                                if (data) {
                                    $("#otherInfoDiv").addClass("layui-show");
                                    $("#otherInfoDiv").removeClass("ignore-verify");
                                    $("#otherInfoItemDiv").show();
                                    //所需字段包含“fundsSource-资金来源”“fexProp-结售汇属性”“rmk-备注”“addWord-附言”
                                    if (data.needField && data.needField.indexOf("fundsSource") != -1) {
                                        $("#fundsSourceTd").html(bt.lang('table.fexTranReq.fundsSource'));
                                        $("#fundsSourceDiv").show();
                                        if (data.requiredField && data.requiredField.indexOf("fundsSource") != -1) {
                                            bt.dicts.get('fundsSource').attr({'data-required': true});
                                        } else {
                                            bt.dicts.get('fundsSource').attr({'data-required': false});
                                        }
                                    } else {
                                        $("#fundsSourceTd").html('');
                                        $("#fundsSourceDiv").hide();
                                        bt.dicts.get('fundsSource').attr({'data-required': false});
                                    }
                                    if (data.needField && data.needField.indexOf("fexProp") != -1) {
                                        $("#fexPropTd").html(bt.lang('table.fexTranReq.fexProp'));
                                        $("#fexPropDiv").show();
                                        if (data.requiredField && data.requiredField.indexOf("fundsSource") != -1) {
                                            bt.dicts.get('fexProp').attr({'data-required': true});
                                        } else {
                                            bt.dicts.get('fexProp').attr({'data-required': false});
                                        }
                                    } else {
                                        $("#fexPropTd").html('');
                                        $("#fexPropDiv").hide();
                                        bt.dicts.get('fexProp').attr({'data-required': false});
                                    }
                                    if (data.needField && data.needField.indexOf("addWord") != -1) {
                                        $("#addWordTr").show();
                                        if (data.requiredField && data.requiredField.indexOf("addWord") != -1) {
                                            $("#addWord").attr("lay-verify", "required");
                                        } else {
                                            $("#addWord").removeAttr("lay-verify");
                                        }
                                    } else {
                                        $("#addWordTr").hide();
                                        $("#addWord").removeAttr("lay-verify");
                                    }
                                    if (data.needField && data.needField.indexOf("rmk") != -1) {
                                        $("#rmkTr").show();
                                        if (data.requiredField && data.requiredField.indexOf("rmk") != -1) {
                                            $("#rmk").attr("lay-verify", "required");
                                        } else {
                                            $("#rmk").removeAttr("lay-verify");
                                        }
                                    } else {
                                        $("#rmkTr").hide();
                                        $("#rmk").removeAttr("lay-verify");
                                    }
                                    if (data.needField && data.needField.indexOf("rmkLocal") != -1) {
                                        $("#rmkLocalTr").show();
                                        if (data.requiredField && data.requiredField.indexOf("rmkLocal") != -1) {
                                            $("#rmkLocal").attr("lay-verify", "required");
                                        } else {
                                            $("#rmkLocal").removeAttr("lay-verify");
                                        }
                                    } else {
                                        $("#rmkLocalTr").hide();
                                        $("#rmkLocal").removeAttr("lay-verify");
                                    }
                                    form.renderIcon();
                                } else {
                                    $("#otherInfoDiv").removeClass("layui-show");
                                    $("#otherInfoDiv").addClass("ignore-verify");
                                    $("#otherInfoItemDiv").hide();
                                }
                            }
                        });
                    } else {
                        $("#otherInfoDiv").removeClass("layui-show");
                        $("#otherInfoDiv").addClass("ignore-verify");
                        $("#otherInfoItemDiv").hide();
                    }

                } else {
                    if (initCustomType != "other") {
                        selectType.get('bankPayType').attr({
                            "data-url": bankPayTypeUrl,
                            "data-required": false
                        });
                        $("#bankPayTypeTr").hide();
                    }

                    $("#otherInfoDiv").removeClass("layui-show");
                    $("#otherInfoDiv").addClass("ignore-verify");
                    $("#otherInfoItemDiv").hide();
                }
                form.renderIcon();
            }
        });
        form.render();
    }

    //收款账户信息
    var corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: $("#buyCurIdDef").val(), corpId: $("#corpIdDef").val()},
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
                selectType.get('buyCurId').val(data.curId);
                $('#corpBank').val(data.bankTypeId);
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccName').val(data.accName);
                $('#corpIsDomesticBank').val(data.isDomesticBank);
                $('#corpCountryId').val(data.prov);
                $('#corpBifCode').val(data.bifCode);
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
                //银行支付类型
                var countryId = "CN";
                if (data.isDomesticBank == "0") {
                    // 境外账户
                    countryId = data.prov;
                }
                //汇款类型
                var remitType = bt.dicts.get("remitType").val();
                setBankTypeCustom(data.bankTypeId, data.bifCode, countryId, '', remitType, '');
                //自动判断跨行标志
                setDifBank();
                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                $("#srcCurTd").text(data.curEnglishCode);
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
        urldata: {corpId: $('input[name="corpId"]').val()},
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
        //外汇交易类型 0-即期、1-远期
        if(fexTranType=="0"){
            //“即期汇率”改为非必填
            bt.moneyType.get('spotRate').currentObject.removeAttr('lay-verify');
            //固定金额 1-买入金额 2-卖出金额
            var fixedMoney =  (bt.dicts.get('fixedMoney').val()).toString();
            if(fixedMoney == ""){
                bt.moneyType.get('spotRate').currentObject.removeAttr('lay-verify');
                bt.moneyType.get('buyMoney').currentObject.removeAttr('lay-verify');
                bt.moneyType.get('sellMoney').currentObject.removeAttr('lay-verify');
                bt.moneyType.get('contractRate').currentObject.removeAttr('lay-verify');
                $("#srcTr").hide();
                $("#targetTr").hide();
            }else {
                if (fixedMoney.indexOf("1") != -1) {
                    bt.moneyType.get('buyMoney').currentObject.attr('lay-verify', 'required');
                    $("#srcTr").show();
                } else {
                    bt.moneyType.get('buyMoney').currentObject.removeAttr('lay-verify');
                    $("#srcTr").hide();
                }
                if (fixedMoney.indexOf("2") != -1) {
                    bt.moneyType.get('sellMoney').currentObject.attr('lay-verify', 'required');
                    $("#targetTr").show();
                } else {
                    bt.moneyType.get('sellMoney').currentObject.removeAttr('lay-verify');
                    $("#targetTr").hide();
                }
                if (fixedMoney.indexOf("1") != -1 && fixedMoney.indexOf("2") != -1) {
                    bt.moneyType.get('contractRate').currentObject.attr('lay-verify', 'required');
                } else {
                    bt.moneyType.get('contractRate').currentObject.removeAttr('lay-verify');
                }
            }
        }else{
            bt.moneyType.get('spotRate').currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('buyMoney').currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('sellMoney').currentObject.attr('lay-verify', 'required');
            bt.moneyType.get('contractRate').currentObject.attr('lay-verify', 'required');
            $("#srcTr").show();
            $("#targetTr").show();
        }
        form.renderIcon();
    }

    function resetFixedMoney(isRadio,elem){
        var type = isRadio? 'radio':'checkbox';
        elem.data('type',type).html('').removeClass('init');
        bt.dicts.init();
    }

    //产品回调函数
    bt.proCallback = function (data) {
        $("#fexBusType").val(data.fexbustype);
        //所选产品为结汇时，默认自动计算买入金额；所选产品为购汇时，默认自动计算卖出金额
        if($("#fexBusType").val() == "2"){//购汇
            bt.dicts.get("autoCal").val("2");
            //结售汇属性设置为购汇的字典
            bt.dicts.get("fexProp")['fex.tranApp.fexProp'] = null;
            bt.dicts.get("fexProp").attr({'data-group': 'fex.tranApp.fexProp2'});
        } else {
            bt.dicts.get("autoCal").val("1");
            //结售汇属性设置为结汇的字典
            bt.dicts.get("fexProp")['fex.tranApp.fexProp2'] = null;
            bt.dicts.get("fexProp").attr({'data-group': 'fex.tranApp.fexProp'});
        }
        $("#fexTranType").val(data.fextrantype);
        $("#deliveryType").val(data.deliverytype);
        $("#fexBusTypeDiv").text(bt.dict("fexProduct.fexBusType", data.fexbustype));
        $("#fexTranTypeDiv").text(bt.dict("fexProduct.fexTranType", data.fextrantype));
        $("#deliveryTypeDiv").text(bt.dict("fexProduct.deliveryType", data.deliverytype));

        if(data.buycurid){
            selectType.get('buyCurId').val(data.buycurid);
            //现金流信息
            $("#srcCurTd").text(data.buyenglishcode);
        }else{
            selectType.get('buyCurId').val('');
            //现金流信息
            $("#srcCurTd").text('');
        }
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: bt.selectType.get('buyCurId').val(), corpId: $('input[name="corpId"]').val()});
        if(data.sellcurid) {
            selectType.get('sellCurId').val(data.sellcurid);
            //现金流信息
            $("#targetCurTd").text(data.sellenglishcode);
        }else{
            selectType.get('sellCurId').val('');
            //现金流信息
            $("#targetCurTd").text('');
        }
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: selectType.get('sellCurId').val(), corpId: $('input[name="corpId"]').val()});

        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }

        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId").val(),data.id,"1","0", oppId);
        renderBankAcc("1", oppId, selectType.get("buyCurId").val(),data.id,"1","1", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId").val(),data.id,"1","1", oppId);
        renderBankAcc("1", oppId, selectType.get("sellCurId").val(),data.id,"1","0", $('input[name="corpId"]').val());
        if (data.deliverytype == "1") {
            $("#deliveryCurTdNull").hide();
            $("#deliveryCurTd").show();
            $("#deliveryCurDiv").show();
            selectType.get('deliveryCurId').attr({'data-required':true,'data-value':data.deliverycurid});
        } else {
            $("#deliveryCurTdNull").show();
            $("#deliveryCurTd").hide();
            $("#deliveryCurDiv").hide();
            selectType.get('deliveryCurId').attr({'data-required':false,'data-value':data.deliverycurid});
        }
        //结汇
        if(data.fexbustype=="1"){
            $("#settleBusTypeTdNull").hide();
            $("#settleBusTypeTd").show();
            $("#settleBusTypeDiv").show();
            bt.dicts.get('settleBusType').attr({'data-required': true});
            $("#purposeTr").show();
        }else{
            $("#settleBusTypeTdNull").show();
            $("#settleBusTypeTd").hide();
            $("#settleBusTypeDiv").hide();
            bt.dicts.get('settleBusType').attr({'data-required': false});
            $("#purposeTr").hide();
            bt.dicts.get('purpose').val('');
            $('#purposeDtl').removeAttr("lay-verify");
            $('#purposeDtl').val('');
        }
        //远期，固定金额只能单选
        resetFixedMoney(data.fextrantype=="1", $('#fixedMoney'));
        //设置买入/卖出金额
        setMoneyInfo();
        form.renderIcon();
        form.render();
    };

    bt.cotpBankCallback = function (data, dom) {
        if (!isEmpty(data.type)) {
            if (data.type === "innerBank") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.bankName);
                $("#cotpBicCode").val(data.swiftCode);
                if(data.swiftCode){
                    $("#cotpBicCodeDiv").text(data.swiftCode);
                }else{
                    $("#cotpBicCodeDiv").text('');
                }
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);
                $("#targetCotpTd").text(data.bankName);
                $("#targetCotpBankTd").text(data.bankName);
            } else if (data.type === "outerBank") {
                $("#oppId").val(data.id);
                $("#cotpName").val(data.bankName);
                $("#cotpBicCode").val(data.swiftCode);
                if(data.swiftCode){
                    $("#cotpBicCodeDiv").text(data.swiftCode);
                }else{
                    $("#cotpBicCodeDiv").text('');
                }
                $("#srcCotpTd").text(data.bankName);
                $("#srcCotpBankTd").text(data.bankName);
                $("#targetCotpTd").text(data.bankName);
                $("#targetCotpBankTd").text(data.bankName);
            }
        } else {
            $("#oppId").val("");
            $("#cotpName").val("");
            $("#cotpBicCode").val("");
            $("#srcCotpTd").text("");
            $("#srcCotpBankTd").text("");
            $("#targetCotpTd").text("");
            $("#targetCotpBankTd").text("");
        }
        renderBankAcc("1", data.id, selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
        renderBankAcc("1", data.id, selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","0", data.id);
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","1", data.id);
    }

    //交易对手银行账户回调函数
    // bt.cotpCallback = function (data) {
    //     $("#srcCotpTd").text(data.cotpname);
    //     $("#targetCotpTd").text(data.cotpname);
    //     $("#cotpBicCode").val(data.biccode);
    //     if(data.biccode){
    //         $("#cotpBicCodeDiv").text(data.biccode);
    //     }else{
    //         $("#cotpBicCodeDiv").text('');
    //     }
    //     //加载主体单位和交易对手账户信息
    //     renderBankAcc("1", data.id, selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
    //     renderBankAcc("1", data.id, selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
    // };

    /**
     * 合同汇率计算
     */
    function calContractRate() {
        //外汇买卖交易的交易类型为“即期”且固定金额全选时，才自动计算买入金额、卖出金额、合同汇率
        /*var fexTranType = $("#fexTranType").val();
        var fixedMoney =  bt.dicts.get('fixedMoney').val()+"";
        if(fexTranType == "0" && (fixedMoney == "" || fixedMoney.indexOf("1") == -1 || fixedMoney.indexOf("2") == -1)){
            return;
        }*/
        var buyCurId = selectType.get("buyCurId").val();
        var buyMoney = moneyType.get("buyMoney").val();
        var sellCurId = selectType.get("sellCurId").val();
        var sellMoney = moneyType.get("sellMoney").val();
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
                            moneyType.get("contractRate").val("");
                        } else {
                            setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data.obj);
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
    function setContractRate(buyCurId, sellCurId, buyMoney, sellMoney, data) {
        //var autoCal = $("input[type = 'checkbox'][name = 'autoCal"+"']:checked");
        var autoCal = bt.dicts.get("autoCal").val();
        var contractRate = moneyType.get("contractRate").val();
        if (isNotEmpty(contractRate)) {
            // 合同汇率存在值
            if (autoCal == "1") {//计算买入
                if (isNotEmpty(sellMoney)) {
                    if (data.baseCurId == buyCurId) {//买入币种为标准
                        moneyType.get("buyMoney").val(bt.money.div(sellMoney, contractRate, 2));
                    } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                        moneyType.get("buyMoney").val(bt.money.mul(sellMoney, contractRate, 2));
                    }
                }
            } else {//计算卖出
                if (isNotEmpty(buyMoney)) {
                    if (data.baseCurId == buyCurId) {//买入币种为标准
                        moneyType.get("sellMoney").val(bt.money.mul(buyMoney, contractRate, 2));
                    } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                        moneyType.get("sellMoney").val(bt.money.div(buyMoney, contractRate, 2));
                    }
                }
            }
        } else {
            if (isNotEmpty(buyMoney) && isNotEmpty(sellMoney)) {
                // 合同汇率不存在值
                if (data.baseCurId == buyCurId) {//买入币种为标准
                    moneyType.get("contractRate").val(bt.money.div(sellMoney, buyMoney, 6));
                } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                    moneyType.get("contractRate").val(bt.money.div(buyMoney, sellMoney, 6));
                }
            }
        }
        if (isNotEmpty(buyMoney) && isNotEmpty(sellMoney)) {
            $("#srcPayMoneyTd").text(moneyFun.format(moneyType.get('buyMoney').val()));
            $("#targetPayMoneyTd").text('-' + moneyFun.format(moneyType.get('sellMoney').val()));
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
    bt.contractRateCallback = function (data) {
        calContractRate();
    };
    //币别回调函数
    bt.buyCurCallback = function (data) {
        calContractRate();
        //加载资金计划
        renderBudget();
        //现金流信息
        $("#srcCurTd").text(data.englishcode);
        //重新加载收款账户信息
        corpBankAccInit.get("corpBankAcc").reload({curId: bt.selectType.get('buyCurId').val(), corpId: $('input[name="corpId"]').val()});
        form.render();
    };
    bt.sellCurCallback = function (data) {
        calContractRate();
        //现金流信息
        $("#targetCurTd").text(data.englishcode);
        //重新加载付款账户信息
        srcBankAccInit.get("srcBankAcc").reload({curId: selectType.get('sellCurId').val(), corpId: $('input[name="corpId"]').val()});
        //加载资金计划
        renderBudget();
        form.render();
    };

    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }
    var selectType = bt.selectType.init();

    //结算日期回调函数
    settleDateCallback = function (data, $elem){
        var settleDate = dateType.get('settleDate').val();
        var tranDate = dateType.get('tranDate').val();
        var deliveryDeadline = dateFun.getBetweenDay(tranDate, settleDate);
        $("#deliveryDeadline").val(deliveryDeadline);
        $("#deliveryDeadlineDiv").text('T+'+deliveryDeadline);
        renderBudget();
        //保证金相关信息设置
        setBoodInfo();
        //现金流
        $("#srcSettleDateTd").text(settleDate);
        $("#targetSettleDateTd").text(settleDate);
    };

    //根据交割期限设置保证金相关信息
    function setBoodInfo(){
        var deliveryDeadline = $('#deliveryDeadline').val();
        var boodType = bt.dicts.get('boodType').val();
        //非T+0且保证金方式不为授信额度时：保证方式、保证金账号、保证比例、保证金钞汇标识、保证金币种都为必填
        if(deliveryDeadline>0 && boodType != "3" && boodType!="4") {
            bt.dicts.get('boodType').setRequired(true);
            $('#boodAcc').attr('lay-verify', 'required|tableData');
            bt.ratioType.get('boodRatio').currentObject.attr('lay-verify', 'required');
            bt.dicts.get('boodBankNote').attr({'data-required': true});
            selectType.get('bondCurId').attr('data-required', true);
        }else{
            if(deliveryDeadline>0) {
                bt.ratioType.get('boodRatio').currentObject.attr('lay-verify', 'required');
                bt.dicts.get('boodType').setRequired(true);
            }else{
                bt.dicts.get('boodType').setRequired(false);
                bt.ratioType.get('boodRatio').currentObject.removeAttr('lay-verify');
            }
            $('#boodAcc').attr('lay-verify', 'tableData');
            bt.dicts.get('boodBankNote').attr({'data-required': false});
            selectType.get('bondCurId').attr('data-required', false);
        }
        form.renderIcon();
    }

    //金额回调函数
    bt.buyMoneyCallback = function(data, obj, preValue){
        //交易从交易需求过来需校验金额
        var moneyOld = moneyFun.format(preValue);//输入前的值
        var moneyNew = moneyFun.format(moneyType.get('buyMoney').val());//输入后的值
        if($("#fixedMoneyApp").val()=="1"){
            if (moneyOld != moneyNew) {
                layer.confirm(bt.lang('fex.tran.req.message.buymoney.update'), function () {
                    $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('buyMoney').val()));
                },function () {
                    moneyType.get('buyMoney').val(preValue);
                    $("#srcPayMoneyTd").text('-' + moneyFun.format(preValue));
                });
            }
        }else{
            $("#srcPayMoneyTd").text('-' + moneyFun.format(moneyType.get('buyMoney').val()));
        }
        calContractRate();

        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }

        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","0", oppId);
        renderBankAcc("1", oppId, selectType.get("buyCurId").val(),selectType.get("proId").val(),"1","1", $('input[name="corpId"]').val());
    };
    bt.sellMoneyCallback = function(data, obj, preValue){
        //交易从交易需求过来需校验金额
        var moneyOld = moneyFun.format(preValue);//输入前的值
        var moneyNew = moneyFun.format(moneyType.get('sellMoney').val());//输入后的值
        if($("#fixedMoneyApp").val()=="2"){
            if (moneyOld != moneyNew) {
                layer.confirm(bt.lang('fex.tran.req.message.sellmoney.update'), function () {
                    $("#targetPayMoneyTd").text('-' + moneyFun.format(moneyType.get('sellMoney').val()));
                },function () {
                    moneyType.get('sellMoney').val(preValue);
                    $("#targetPayMoneyTd").text('-' + moneyFun.format(preValue));
                });
            }
        }else{
            $("#targetPayMoneyTd").text('-' + moneyFun.format(moneyType.get('sellMoney').val()));
        }
        calContractRate();
        var oppId = $("#oppId").val();
        if (isEmpty(oppId)) {
            oppId = null;
        }
        //加载主体单位和交易对手账户信息
        renderBankAcc("0", $('input[name="corpId"]').val(), selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","1", oppId);
        renderBankAcc("1", oppId, selectType.get("sellCurId").val(),selectType.get("proId").val(),"1","0", $('input[name="corpId"]').val());
    };
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    /**
     * 获取账户信息
     * @param busType 业务类型：0主体单位，1交易对手
     * @param corpId 单位/交易对手
     * @param curId 币种
     * @param proId 产品
     * @param swapType 掉期交易 1-近端 2-远端
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
                                        $("#targetCotpBankNearTd").text(data.obj.bankTypeName);
                                    }/* else {
                                        $("#targetCotpAccNameNearTd").text("");
                                        $("#targetCotpBankAccNearTd").text("");
                                        $("#targetCotpBankNearTd").text("");
                                    }*/
                                } else {
                                    if (data.obj.bankAcc) {
                                        $("#targetCotpAccNameRemoteTd").text(data.obj.accName);
                                        $("#targetCotpBankAccRemoteTd").text(data.obj.bankAcc);
                                        $("#targetCotpBankRemoteTd").text(data.obj.bankTypeName);
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
                                        $("#srcCotpBankNearTd").text(data.obj.bankTypeName);
                                        $("#srcCotpAccNameNearTd").text(data.obj.accName);
                                        $("#srcCotpBankAccNearTd").text(data.obj.bankAcc);
                                    } /*else {
                                        $("#srcCotpBankNearTd").text("");
                                        $("#srcCotpAccNameNearTd").text("");
                                        $("#srcCotpBankAccNearTd").text("");
                                    }*/
                                } else {
                                    if (data.obj.bankAcc) {
                                        $("#srcCotpBankRemoteTd").text(data.obj.bankTypeName);
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
        //增加已提交的标志
        $(data.elem).parent().addClass('isSubmiting');


        $("#status").val($(this).attr("value"));
        data.field.status = $(this).attr("value");
        $("#attachmentId").val(up.getGroupId());
        data.field.attachmentId = up.getGroupId();
        var fexTranType = $("#fexTranType").val();
        //外汇交易类型 0-即期、1-远期
        if(fexTranType=="0") {
            if (bt.dicts.get('fixedMoney').val()) {
                data.field.fixedMoney = (bt.dicts.get('fixedMoney').val()).toString();
            } else {
                data.field.fixedMoney = "";
            }
        }
        //判断是否挂接资金计划
        var payMoney = moneyFun.toNumber(moneyType.get("sellMoney").val());
        if ($("input[name='sumMoney']").length) {
            var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
            // 校验金额与资金计划总金额一致
            if (sumMoney != payMoney) {
                bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"), {icon: 2});
                return false;
            }
        }
        var corpAccId = $("#corpAccId").val();
        var corpBankAcc = $("#corpBankAcc").val();
        if(!corpAccId || !corpBankAcc){
            bt.loading.hide();
            layer.alert(bt.lang("fex.cash.flow.message.bankacc.match"), {icon: 2});
            return false;
        }
        //证书签名
        //证书签名
        var encrypt = $("#encrypt").val(); // cfca证书
        if(encrypt == 2 || encrypt == 1){ // 1 天威证书
            bt.loading.hide();
            signData("外汇交易提交", 11,function () {
                bt.loading.show();
                var action = "/foreign/fextranreq/modify";
                $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                    bt.loading.hide();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                        if (result.success) {
                            if (bt.navtab) {
                                parent.layer.closeAll();
                                bt.navtab.closeCurrent();
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        }
                    })
                }, 'JSON');
            })
        }else {
            var signDataMsg = signData("外汇交易提交", 11);
            if (signDataMsg != "") {
                bt.loading.hide();
                layer.alert(bt.lang(signDataMsg), {icon: 2});
                return false;
            }
            $.post(basePath + "/foreign/fextranreq/modify", $('#inputForm').serialize(), function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    if (result.success) {
                        if (bt.navtab) {
                            parent.layer.closeAll();
                            bt.navtab.closeCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }

                })
            }, 'JSON');
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
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var corpId = $("#corpIdDef").val();
        if ($('input[name="corpId"]').val()) {
            corpId = $('input[name="corpId"]').val();
        }
        var curId = $("#sellCurIdDef").val();
        if (selectType.get("sellCurId").val()) {
            curId = selectType.get("sellCurId").val();
        }
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
    renderBudget();

    /**
     * 资金计划挂接
     */
    function renderBudget() {
        //判断外币汇款是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        //外汇交易类型 0-即期、1-远期
        var fexTranType = $("#fexTranType").val();
        var flag = true;
        if(fexTranType == "0") {
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
    //固定金额控制必填
    setMoneyInfo();
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("sellMoney").val(data);
}
