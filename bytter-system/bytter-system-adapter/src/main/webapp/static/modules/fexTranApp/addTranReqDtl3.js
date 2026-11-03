var form, element, corp, dateType, selectType, moneyType, rateType, dicts, corpBankAccInit,allExternalBankAcc;
bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType", "dicts", "selectTableType",
    "selectTable", "btUpload","allExternalBankAcc"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
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
        allExternalBankAcc.set({
            "type": "innerBankAcc,outerBankAcc,corpBankAcc",
            'isCustom':'1'
        });
        return true;
    }

    //监听提交
    form.on("submit(submit)", function (data) {
        var action = "/foreign/fexTranApp/saveDtlTmp";
        data.field.attachmentId = upload.getGroupId();
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
            curId: selectType.get('curId').val(), corpBank: selectType.get('corpBank').val(),
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
            } else {
                $("input.text").val("");
            }
        }
    });

    //交易对手账户信息
    // cotpBankAccInit = bt.selectTable.init({
    //     elem: "#cotpBankAccTable",
    //     url: basePath+"/foreign/fexcotpbankacc/selectJoinCur",
    //     urldata: {cotpId:bt.selectType.get("cotpId").val(), bankId: bt.selectType.get("cotpBank").val()},
    //     scrollX: true,
    //     scrollY:"200px",
    //     searchInput:"#cotpBankAcc",
    //     paging:true,
    //     columns: [
    //         {data:"bankAcc",},
    //         {data:"accName",},
    //         {data:"bankName",},
    //         {data:"curName",}
    //     ],
    //     clickCallback:function(data){
    //         if(data){
    //             $("#cotpbankAddr").html(data.bankAddr);//交易对手银行地址
    //             $("#swiftCode").html(data.bicCode);//swiftCode
    //             $("#agentBank").html(data.tranBankName);//交易对手代理行
    //             $("#agentBankAddr").html(data.tranBankAddr);//交易对手代理行地址
    //             $("#agentBankAcc").html(data.tranBankAcc);//交易对手代理行账户
    //             $("#cotpAccId").val(data.id);
    //             selectType.get("cotpId").val(data.cotpId);
    //             selectType.get("cotpBank").val(data.bankId);
    //             $("#cotpBankAcc").val(data.bankAcc);
    //             $("#cotpAccCurId").val(data.curId);
    //             $("#cotpAccCurNameDiv").text(data.curName);
    //         }else{
    //             $('input.text').val('');
    //         }
    //     }
    // });

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
function curCallBack(data, elem, beforValue, wrapObj) {
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    selectType.get("tranCurId1").val(data.id);
    selectType.get("tranCurId2").val(data.id);
    var curId = selectType.get('curId').val();
    var corpBank = selectType.get('corpBank').val();
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {curId: curId, corpBank: corpBank},
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
 *主体单位银行回调
 * @param data
 * @param elem
 * @param beforValue
 * @param wrapObj
 */
function corpBankCallBack(data, elem, beforValue, wrapObj) {
    $("#corpBankAcc").val("");
    $("#corpAccId").val("");
    var curId = selectType.get('curId').val();
    var corpBank = selectType.get('corpBank').val();
    var corpId = $("#corpId").val();
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {curId: curId, corpBank: corpBank, "corpId": corpId},
        columns: [
            {data:"bankAcc"},
            {data:"accName"},
            {data:"bankName"},
            {data:"curEnglishCode"}
        ]
    });
    form.render();
}

/**
 * 付款类型回调
 */
function payTypeCallBack() {
    var payType = dicts.get("payType").val();
    if ("1" == payType) {
        dateType.get("shipDate").attr("lay-verify", "required");
    } else {
        dateType.get("shipDate").attr("lay-verify", "");
    }
    form.renderIcon();
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
}
