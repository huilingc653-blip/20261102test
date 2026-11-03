var form, element, corp, dateType, selectType, moneyType, rateType, dicts, corpBankAccInit,allExternalBankAcc;
var isSeleTemplate,isBankPayType = false;
bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType", "dicts", "selectTableType",
    "selectTable", "btUpload","allExternalBankAcc"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    var corpBank = $('[data-name="corpBank"]').data('value')
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.init();
    dicts = bt.dicts.init();
    var gid = $("#gid").val();
    upload = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });
     allExternalBankAcc=bt.allExternalBankAcc.init();
    bt.externalBankAccBeforeClick = function () {
        /*allExternalBankAcc.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc",
            'isCustom':'1'
        });*/
        if(!isSeleTemplate && !isBankPayType){
            allExternalBankAcc.set({
                "type": "innerBankAcc,outerBankAcc,corpBankAcc",
                'isCustom':'1'
            });
        }
        return true;
    }

    //监听提交
    form.on("submit(submit)", function (data) {

        var selectedData = bt.selectType.get('payTypeId').getSelectedData();
        var action = "/foreign/fexTranApp/saveDtlTmp";
        data.field.attachmentId = upload.getGroupId();
        data.field.bankPayTypeId=data.field.payTypeId;
        data.field.bankPayType=selectedData.customcode || '';
        data.field.bankPayTypeName= selectedData.customname || '';
        if (!data.field.templateId) {
            data.field.templateId = parent.$("#templateId").val() || "";
        }
        if (!data.field.templateName) {
            data.field.templateName = parent.$("#templateName").val() || "";
        }
        bt.loading.show();
        $.ajax({
            url: basePath + action,
            type: "post",
            data: data.field,
            dataType: "json",
            async: false,
            success: function (result) {
                bt.loading.hide();
                if (result != null) {
                    parent.closeTab(result);
                }
            }
        });
        return false;
    });

    //主体账户信息
    corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath + "/foreign/fexTranApp/getCorpBankAcc",
        urldata: {
            curId: selectType.get('curId').val(),
            corpBank: selectType.get('corpBank').val(),
            bifCode:$("#bifCode").val(),
            countryId:$("#countryId").val(),
            "corpId": $("#corpId").val()
        },
        scrollX: true,
        scrollY: "200px",
        searchInput: "#corpBankAcc",
        paging: true,
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ],
        clickCallback: function (data) {
            if (data) {
                selectType.get("corpBank").val(data.bankTypeId);
                $("#corpBankAcc").val(data.bankAcc);
                $("#corpAccId").val(data.id);
                $("#corpAccName").html(data.accName);
                $("#corpAccCurId").val(data.curId);
                $("#corpAccCurNameDiv").text(data.curEnglishCode);


                var payTypeId = bt.selectType.get('payTypeId').getSelectedData().id;
                if(payTypeId){
                    $.ajax({
                        url: basePath + '/foreign/fexTranConfigTemplate/getTemplateByBankAcc',
                        type: 'POST',
                        data: {
                            bankAcc: data.bankAcc,
                            payTypeId: payTypeId
                        },
                        success: function(response) {
                            if (response) { // 根据实际返回码调整
                                var jsonObj;
                                if(typeof response  == 'object'){
                                    jsonObj = response;
                                }else{
                                    jsonObj = JSON.parse(response);
                                }
                                if(jsonObj.success){
                                    if(jsonObj.obj){
                                        var templateId = jsonObj.obj.id;
                                        var templateName = jsonObj.obj.templateName;
                                        parent.$("#templateName").val(templateName);
                                        parent.$("#templateId").val(templateId);
                                        $("#templateName").val(templateName);
                                        $("#templateId").val(templateId);
                                    }else {
                                        parent.$("#templateName").val("");
                                        parent.$("#templateId").val("");
                                        $("#templateName").val("");
                                        $("#templateId").val("");
                                        selectType.get("payTypeId").attr({
                                            "data-value": "",
                                            "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType="+data.bankTypeId+"&bifCode="+data.bifCode+"&countryId="+"&bankCode="+data.bankCode+"&isDomesticBank="+data.isDomesticBank
                                        });
                                    }
                                }
                            }
                        }
                    });
                }else {
                    selectType.get("payTypeId").attr({
                        "data-value": "",
                        "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType="+data.bankTypeId+"&bifCode="+data.bifCode+"&countryId="+"&bankCode="+data.bankCode+"&isDomesticBank="+data.isDomesticBank
                    });
                }
            } else {
                $("input.text").val("");
            }
        }, handleDeleteCallback: function () {
            $("#corpAccId").val("");
            $("#corpBankAcc").val("");
            $("#corpAccName").html("")
            $("#corpAccCurNameDiv").html("");

            corpBankAccInit.get("corpBankAcc").rebuild({
                urldata: {
                    curId: selectType.get('curId').val(),
                    corpBank: selectType.get('corpBank').val(),
                    bifCode:$("#bifCode").val(),
                  //  countryId:$("#countryId").val(),
                    "corpId": $("#corpId").val()
                },
                columns: [
                    {data:"bankAcc"},
                    {data:"accName"},
                    {data:"bankName"},
                    {data:"curEnglishCode"}
                ]
            });
        }
    });



    //交易编码
    var tranCodeInit1 = bt.selectTableType.init({
        elem: ".tranName1",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "tranCode", title: bt.lang("table.fexTranInfo.tranCode")},
            {data: "tranName", title: bt.lang("table.fexTranInfo.tranName")}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName1').val(data.tranName);
                $('#tranCode1').val(data.tranCode);
                //获取交易附言
                $.ajax({
                    url: basePath + "/foreign/fexAddWord/getFexAddWord",
                    method: 'POST',
                    data: {tranCode: data.tranCode, cdSign: dicts.get("cdSign").val()},
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
            }
        }
    });

    //交易编码
    var tranCodeInit2 = bt.selectTableType.init({
        elem: ".tranName2",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "tranCode", title: bt.lang("table.fexTranInfo.tranCode")},
            {data: "tranName", title: bt.lang("table.fexTranInfo.tranName")}
        ],
        clickCallback: function (data) {
            if (data) {
                if (data.tranName == $("#tranName1").val() && data.tranCode == $("#tranCode1").val()) {
                    parent.parent.layer.alert(bt.lang("fex.foreign.message.trancode.same"), {icon: 2});
                } else {
                    $("#tranName2").val(data.tranName);
                    $("#tranCode2").val(data.tranCode);
                    var curId = selectType.get("curId").val();
                    if (isEmpty(curId)) {
                        parent.parent.layer.alert(bt.lang("common.select.currency"), {icon: 2});
                        return false;
                    }
                    moneyType.get("tranAmt2").attr("lay-verify", "required");
                    selectType.get("tranCurId2").attr({
                        "data-value": curId, "data-required": true,
                        "data-disabled": true
                    });
                    $("#tranAddWord2").attr("lay-verify", "required");

                    //获取交易附言
                    $.ajax({
                        url: basePath + "/foreign/fexAddWord/getFexAddWord",
                        method: 'POST',
                        data: {tranCode: data.tranCode, cdSign: dicts.get("cdSign").val()},
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
                }
                form.renderIcon();
            }
        }
    });
    //这里填写新的js代码，请参考前端文档
    var bankPayType = bt.selectType.get('payTypeId').getSelectedData().customcode;
    resetExternalAccCorp(bankPayType);
    // 加载默认主体单位银行账户
    renderCorpBankAcc();
});

//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 交易对手西选择回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function setCotp(data, elem, beforValue, wrapObj) {
    $("#postAddr").html(data.postaddr);
    $("#country").html(data.countryname);
    setCotpBankAcc();
}


/**
 * 设置显示交易对手信息
 * @param data
 * @param elem
 * @param beforevalue
 * @param wrapObj
 */
/*
function cotpBankAccCallBack(data, elem, beforevalue, wrapObj) {
    $("#cotpbankAddr").html(data.bankaddr);//交易对手银行地址
    $("#swiftCode").html(data.biccode);//swiftCode
    $("#agentBank").html(data.tranbankname);//交易对手代理行
    $("#agentBankAddr").html(data.tranbankaddr);//交易对手代理行地址
    $("#agentBankAcc").html(data.tranbankacc);//交易对手代理行账户
    $("#cotpAccId").val(data.id);
    selectType.get("cotpId").val(data.cotpid);
    selectType.get("cotpBank").val(data.bankid);
}
*/

/**
 * 校验被代付单位是否与主单位相同
 * @param id
 */
function payCorpIdCallBack(id) {
    var corpId = $("#corpId").val();
    if (id == corpId) {
        parent.parent.layer.alert(bt.lang("不能与主体单位相同"), {icon: 2});
        corp.get("payCorpId").val("");
    }
}

/**
 * 国内外费用承担方回调
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function costSignCallBack() {
    var cdSign = dicts.get("cdSign").val();
    var costSign = dicts.get("costSign").val();
    // 收款时,收款方承担费用,则费用账号必填,付款时,付款方承担费用,则费用账号必填
    if ((costSign == 1 && cdSign == 1)||(costSign == 2 && cdSign == 0)) {
        $("#costAcc").attr("lay-verify", "required");
    }else {
        $("#costAcc").attr("lay-verify", "");
    }
    // 收付方向变化时重新带出主体单位默认银行
    renderCorpBankAcc();
    form.renderIcon();
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


/**
 * 清空交易编码2时设置相应币种2,金额2,附言2非必填
 * @param obj
 */
function setTran2(obj) {
    if (isEmpty(obj.value)) {
        $("#tranCode2").val("");
        moneyType.get("tranAmt2").attr("lay-verify", "");
        selectType.get("tranCurId2").attr({"data-value": "", "data-required": false,
            "data-disabled": false});
        $("#tranAddWord2").attr("lay-verify", "");
        form.renderIcon();
    }
}

/**
 * 币种回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
/**
 * 带出主体单位默认银行（银行类型 + 银行账户）
 */
function renderCorpBankAcc(curId) {
    var corpId = $("#corpId").val();
    curId = curId || selectType.get('curId').val();
    var proId = $("#proId").val();
    if (isEmpty(corpId)) {
        return;
    }
    var oppId = $("#oppId").val();
    if (isEmpty(oppId)) {
        oppId = null;
    }
    $.ajax({
        url: basePath + "/foreign/fexcorpbankacc/getNewBankAcc",
        method: 'POST',
        data: {
            busType: "0",
            tranType: "4",
            corpId: corpId,
            curId: curId,
            proId: proId,
            cashFlowType: "0",
            cdSign: dicts.get('cdSign').val(),
            oppCorpId: oppId
        },
        dataType: 'json',
        success: function (data) {
            if (data.obj && data.obj.bankAcc) {
                selectType.get('corpBank').val(data.obj.bankType);
                $("#corpAccId").val(data.obj.accId);
                $("#corpBankAcc").val(data.obj.bankAcc);
                $("#corpAccName").html(data.obj.accName);
                $("#corpAccCurId").val(data.obj.curId);
                $("#corpAccCurNameDiv").text(data.obj.curName);
                form.render();
            }
        }
    });
}

/**
 * 币种回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function curCallBack(data, elem, beforValue, wrapObj) {
    // 币种变化时重新带出主体单位默认银行
    renderCorpBankAcc(data.id);
    // 刷新主体单位银行账户下拉列表
    /*corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {
            curId: selectType.get('curId').val(),
            corpBank: selectType.get('corpBank').val(),
            bifCode: $("#bifCode").val(),
            countryId: $("#countryId").val(),
            "corpId": $("#corpId").val()
        },
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ]
    });
    form.render();*/
}

/**
 *主体单位银行回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function corpBankCallBack(data, elem, beforValue, wrapObj) {
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    $("#bifCode").val("");
    $("#countryId").val("");
    var curId = selectType.get('curId').val();
    var corpBank = selectType.get('corpBank').val();
    var corpId = $("#corpId").val();
    var bifCode = $("#bifCode").val();
    var countryId = $("#countryId").val();
    parent.$("#templateName").val("");
    parent.$("#templateId").val("");
    $("#templateId").val("");
    $("#templatePayType").val("");


    $("#corpAccName").html("");
    $("#corpAccCurNameDiv").text("");
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {curId: curId, corpBank: corpBank, "corpId": corpId,"bifCode":bifCode,"countryId":countryId},
        columns: [
            {data:"bankAcc"},
            {data:"accName"},
            {data:"bankName"},
            {data:"curEnglishCode"}
        ]
    });

    selectType.get("payTypeId").attr({
        "data-value": "",
        "data-url": ""
    });
    resetExternalAccCorp("");
    form.render();
}

/**
 * 支付类型初始化回调
 */
function payTypeInitCallback() {
    var selectedData = bt.selectType.get('payTypeId').getSelectedData();
    resetExternalAccCorp(selectedData.customcode || "");
}

/**
 * 支付类型回调
 */
function payTypeCallback(data, elem, beforValue, wrapObj) {
    var payTypeId = selectType.get('payTypeId').val();
    var corpBankAcc = $("#corpBankAcc").val();
    var bankPayType = data && data.customcode ? data.customcode : "";
    // 重置客商账号组件
    resetExternalAccCorp(bankPayType);
    // 补充接口请求返回值给templateId，templateName
    $.ajax({
        url: basePath + '/foreign/fexTranConfigTemplate/getTemplateByBankAcc',
        type: 'POST',  // 或者 'POST'，根据实际情况调整
        data: {
            bankAcc: corpBankAcc,
            payTypeId: payTypeId
        },
        success: function(response) {
            if (response) { // 根据实际返回码调整
                var jsonObj;
                if(typeof response  == 'object'){
                    jsonObj = response;
                }else{
                    jsonObj = JSON.parse(response);
                }
                if(jsonObj.success === true){
                    if(jsonObj.obj){
                        var templateId = jsonObj.obj.id;
                        var templateName = jsonObj.obj.templateName;
                        var templatePayType=jsonObj.obj.payTypeId;
                        // 设置父页面的值
                        parent.$("#templateName").val(templateName);
                        parent.$("#templateId").val(templateId);
                        $("#templateId").val(templateId);
                        $("#templateName").val(templateName);
                        $("#templatePayType").val(payTypeId);
                        setRemitType(templateId)
                    }else {
                        parent.$("#templateName").val("");
                        parent.$("#templateId").val("");
                        $("#templateId").val("");
                        $("#templatePayType").val("");
                    }
                }else{
                    // 接口返回失败时的处理
                    console.error('获取模板配置失败:', jsonObj.message);
                    parent.$("#templateName").val("");
                    parent.$("#templateId").val("");
                    $("#templateId").val("");
                    $("#templatePayType").val("");
                }
            } else {
                // 接口返回失败时的处理
                console.error('获取模板配置失败');
                parent.$("#templateName").val("");
                parent.$("#templateId").val("");
                $("#templateId").val("");
                $("#templatePayType").val("");
            }
        },
        error: function(xhr, status, error) {
            // 请求失败时的处理
            console.error('接口请求失败:', error);
        }
    });
}

/**
 * 交易对手账户回调
 * @param data
 */
function externalBankAccCallback(data) {
    if(data.type!="corpBankAcc") {
        $("#cotpBankAcc").val(data.externalAcc);//账号
        $("#cotpAccName").val(data.externalAccName);//账号
        $("#cotpNameEn").val(data.ecNameEn);//名称英文
        $("#oppId").val(data.externalCorpId);//对手原id
        $("#cotpName").val(data.ecName);//对手名称
        $("#cotpBankName").val(data.bank);//银行
        $("#cotpAccCurId").val(data.curId);//币种
        $("#cotpAccCurNameDiv").html(data.englishCode);//币种名称
        $("#cotpPostAddr").val(data.address)//交易对手地址
        $("#postAddrSpan").html(data.address);
        $("#cotpCountry").val(data.countryId)//交易对手国家地区
        if (isNotEmpty(data.country)) {
            $("#countrySpan").html(data.country);
        }
        $("#bicCode").val(data.swiftCode)//交易对手银行swiftcode
        $("#swiftCode").html(data.swiftCode);
        $("#bankAddr").val(data.bankAddress)//交易对手银行地址
        $("#cotpbankAddr").html(data.bankAddress);
        $("#tranBank").val(data.intermediaryBank)//交易对手代理行
        $("#tranBankAddr").val(data.intermediaryBankAddress)//交易对手代理行地址
        $("#tranBankAcc").val(data.intermediaryBankAcc)

        if (isNotEmpty(data.localClearNo)) {
            $("#localClearNo").val(data.localClearNo);
            $("#localClearNoDiv").text(data.localClearNo);//本地清算号
        }else {
            $("#localClearNo").val("");
            $("#localClearNoDiv").text("");//本地清算号
        }
    }else {
        $("#cotpBankAcc").val(data.bankAcc);//账号
        $("#cotpAccName").val(data.accName);//账号
        $("#cotpNameEn").val(data.corpNameEn);//名称英文
        $("#oppId").val(data.corpId);//对手原id
        $("#cotpName").val(data.corpName);//对手名称
        $("#cotpBankName").val(data.bankName);//银行
        $("#cotpAccCurId").val(data.curId);//币种
        $("#cotpAccCurNameDiv").html(data.englishCode);//币种名称
        $("#cotpPostAddr").val(data.addressEn)//交易对手地址
        $("#postAddrSpan").html(data.addressEn);
        $("#cotpCountry").val(data.bankCountryId)//交易对手国家地区
        if (isNotEmpty(data.bankCountryName)) {
            $("#countrySpan").html(data.bankCountryName);
        }
        $("#bicCode").val(data.swiftCode)//交易对手银行swiftcode
        $("#swiftCode").html(data.swiftCode);
        $("#bankAddr").val(data.bankAddress)//交易对手银行地址
        $("#cotpbankAddr").html(data.bankAddress);
        $("#tranBank").val("")//交易对手代理行
        $("#agentBank").html("");
        $("#tranBankAddr").val("")//交易对手代理行地址
        $("#agentBankAddr").html("");
    }
    // 与收付交易保持一致，交易对手变化后按最新条件重新匹配主体单位默认银行
    renderCorpBankAcc();
}



/**
 * 模板选择回调方法
 * @param row
 */
function setParentParams(selectedData) {
    var curId = selectType.get('curId').val();
    var corpId = $("#corpId").val();
    $("#templateId").val(selectedData.id);
    $("#templatePayType").val(selectedData.payTypeId);

    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    $("#corpAccName").html("");
    $("#corpAccCurNameDiv").html("");
    isSeleTemplate = true;
    selectType.get("payTypeId").attr({
        "data-value": selectedData.payTypeId,
        "data-url": basePath + "/basic/business/btbanktype/listByCondition?bankType=" + selectedData.bankTypeId + "&bifCode=" + selectedData.bifId + "&countryId=" + selectedData.countryId + "&payTypeId=" + selectedData.payTypeId
    });

    selectType.get("corpBank").attr({
        "data-value": selectedData.bankTypeId,
        "data-url": basePath + "/basic/business/btbanktype/json"
    });

    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {curId: curId, corpBank: selectedData.bankTypeId, "corpId": corpId,"bifCode":selectedData.bifId,"countryId":selectedData.countryId},
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ]
    });

    // 重置客商账号组件
    resetExternalAccCorp(selectedData.customCode || selectedData.customcode || "");

    setRemitType(selectedData.id)
    form.render();
}


function setRemitType(id){
    $.ajax({
        url: basePath + "/foreign/fexTranReqConfig/getTemplateConfigBaseInfoByFieldCode/"+id+"?fieldCode=remitType",
        type: "get",
        async: false,
        success: function (result) {
            if (result) {
                var obj;
                if(typeof result  == 'object'){
                    obj = result;
                }else{
                    obj = JSON.parse(result);
                }
                if (obj.success&&obj.obj) {
                    var remitTypeValue = obj.obj.fieldDefVal;
                    dicts.get('remitType').val(remitTypeValue);
                }

            }
        }
    });
}

function cleanTemplate() {
    var corpId = $("#corpId").val();
    $("#templateId").val("");
    $("#templatePayType").val("");
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    $("#corpAccName").html("");
    $("#corpAccCurNameDiv").html("");
    $("#corpBank").val("");

    $("#bifCode").val("");
    $("#countryId").val("")
    selectType.get("corpBank").attr({
        "data-value": "",
        "data-url": basePath + "/basic/business/btbanktype/json"
    });
    selectType.get("payTypeId").attr({
        "data-value": "",
        "data-url": ""
    });
    resetExternalAccCorp("");


    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {corpId: corpId},
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curEnglishCode"}
        ]
    });
    form.render();
}


/**
 *
 * 重置客商账号组件
 *
 * @param bankPayType
 */
function resetExternalAccCorp(bankPayType){
    isBankPayType = !!bankPayType;
    bankPayType = (bankPayType || "").toUpperCase();
    var isPayPal = "PAYPAL" == bankPayType || "PALPAY" == bankPayType;
    var isFps = "FPS" == bankPayType;
    $("#cotpBankAcc").attr("lay-verify", isPayPal || isFps ? "" : "required");
    form.renderIcon();
    if (isPayPal){
        allExternalBankAcc.set({
            "type": "payPal",
            'isCustom':'1'
        });
    } else if(isFps){
        allExternalBankAcc.set({
            "type": "outerBankAcc,fps",
            'isCustom':'1'
        });
    } else {
        allExternalBankAcc.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc",
            'isCustom':'1'
        });
    }
}
