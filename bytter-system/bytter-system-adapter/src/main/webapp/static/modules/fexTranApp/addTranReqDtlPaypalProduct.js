var form, element, corp, dateType, selectType, moneyType, rateType, dicts, corpBankAccInit;
bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType", "dicts", "selectTableType",
    "selectTable", "btUpload","region","allExternalBankAcc"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    var allExternalBankAcc=bt.allExternalBankAcc.init();
    bt.externalBankAccBeforeClick = function () {
        allExternalBankAcc.set({
            "type": "payPal",
            'isCustom':'1'
        });
        return true;
    }
    //省回调函数
    bt.provCallback = function (value, othis, options) {
        $('#cotpProv').val(value);
        $('#cotpCity').val("");
    };
    //市回调函数
    bt.cityCallback = function (value, othis, options) {
        $('#cotpCity').val(value);
    };
    bt.cotpCountryCallBack = function (data) {
        setAddress(data.id, data.namezh, '');
    }
    region = bt.region.init();
    //银行自定义配置初始化前回调
    customBeforeInit = function (data,arguments) {
        var show = "("+arguments.customCode+")" + arguments.customName;
        return show;
    }
    //交易对手回调函数
    function setCotp(data, flag) {
        //交易对手所在国家地区
        setAddress(data.countryid, data.countryname,data.address);
        if(data.countryid){
            //$("#nameZhDiv").text(data.countryname);
            selectType.get('cotpCountry').val(data.countryid);
            setTimeout(function () {
                selectType.get('cotpAddress').val(data.address);
            }, 500)
            region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: data.prov
            }, {
                name: 'cotpCity',
                defaultValue: data.city
            }]);
        } else {
            //$("#nameZhDiv").text('');
            selectType.get('cotpCountry').val('');
            setTimeout(function () {
                selectType.get('cotpAddress').val('');
            }, 500)
            region.get('payeeAddr').set([{
                name: 'cotpProv',
                defaultValue: ''
            }, {
                name: 'cotpCity',
                defaultValue: ''
            }]);
        }
        //交易对手地址
        if(data.addressen){
            $("#cotpAdrrDiv").text(data.addressen);
        } else {
            $("#cotpAdrrDiv").text('');
        }
        //现金流信息
        $("#srcCotpTd").text(data.cotpname);
        //email
        if(isNotEmpty(data.email) && isEmpty($("#email").val())){
            var emails = data.email.split(";");
            $("#email").empty();
            for(var i=0;i<emails.length;i++){
                $("#email").append($("<option/>").text(emails[i]).attr("value",emails[i]));
            }
        }
        //isdCode国家代码
        if(isNotEmpty(data.isdcode) && isEmpty($("#isdCode").val())){
            var isdcodes = data.isdcode.split(";");
            $("#isdCode").empty();
            for(var i=0;i<isdcodes.length;i++){
                $("#isdCode").append($("<option/>").text(isdcodes[i]).attr("value",isdcodes[i]));
            }
        }
        //电话号码(MOBN)
        if(isNotEmpty(data.phonecode) && isEmpty($("#phoneCode").val())){
            var phonecodes = data.phonecode.split(";");
            $("#phoneCode").empty();
            for(var i=0;i<phonecodes.length;i++){
                $("#phoneCode").append($("<option/>").text(phonecodes[i]).attr("value",phonecodes[i]));
            }
        }

        //邮箱(EMAL)
        var email = $("#email").val();
        //电话号码(MOBN)
        var isdCode = $("#isdCode").val();
        var phoneCode = $("#phoneCode").val();
        //交易对手银行账号
        var cotpBankAcc = $("#cotpBankAcc").val();
        //交易对手银行账号为空
        if(isEmpty(cotpBankAcc)){
            if(isNotEmpty(email)){
                $("#cotpBankAcc").val(email);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");

            } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
                $("#cotpBankAcc").val(isdCode + phoneCode);
                $("#cotpAccName").val("");
                $("#cotpAccId").val("");

            }
        }
    }
    bt.cotpCallback = function (data) {
        setCotp(data, true);
    };
    /**
     * 设置地点
     * @param data
     * @param elem
     * @param bv
     */
    function setAddress(countryId, countryName,cotpAddress){
        var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + countryId;
        var requiredAddress = false;//设置地点是否必填
        var requiredAddr = false;//设置省市是否必填
        //汇款类型=1-中国大陆账户跨境汇款/2-境外账户汇款
        var remitType = bt.dicts.get("remitType").val();
        var bankTypeName = $("#srcBankTd").text();
        if (countryName == "中国") {
            $('#addressDiv').hide();
            $('#addrDiv').show();
            if (bankTypeName == "中国银行" && remitType == "2") {
                //中国银行 且 境外账户汇款 必填
                requiredAddr = true;
            }
        } else {
            $('#addressDiv').show();
            $('#addrDiv').hide();
            if (bankTypeName == "中国银行" && remitType == "2") {
                //中国银行 且 境外账户汇款 必填
                requiredAddress = true;
            }
        }
        selectType.get("cotpAddress").attr({"data-value": cotpAddress?cotpAddress:'', "data-url": url, "data-required":requiredAddress});
        region.get('payeeAddr').set([{
            name: 'cotpProv',
            defaultValue: '',
            required:requiredAddr
        }, {
            name: 'cotpCity',
            defaultValue: '',
            required:requiredAddr
        }]);
        form.renderIcon();
        form.render();
    }
    //设置交易对手fpsid的值
    setFpsId();
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

    //监听提交
    form.on("submit(submit)", function (data) {
        var action = "/foreign/fexTranApp/saveDtlTmp";
        data.field.attachmentId = upload.getGroupId();
        //邮箱(EMAL)
        var email = $("#email").val();
        //电话号码(MOBN)
        var isdCode = $("#isdCode").val();
        var phoneCode = $("#phoneCode").val();
        //交易对手银行账号
        var cotpBankAcc = $("#cotpBankAcc").val();

        //邮箱/手机号字段不能同时为空
        if(isEmpty(email) && (isEmpty(isdCode) || isEmpty(phoneCode))){
            layer.alert(bt.lang("fex.fexTranReq.message.paypalProduct.email"), {icon: 2});
            return false;
        }

        if(isEmpty(cotpBankAcc)){
            if(isNotEmpty(email)){
                $("#cotpBankAcc").val(email);
                $("#saveFields").val("EMAL");

            } else if(isNotEmpty(isdCode) && isNotEmpty(phoneCode)) {
                $("#cotpBankAcc").val(isdCode + " " +phoneCode);
                $("#saveFields").val("MOBN");

            }
        }else{
            $("#saveFields").val("BBAN");
        }
        data.field.cotpBankAcc =  $("#cotpBankAcc").val();
        data.field.saveFields =  $("#saveFields").val();

        bt.loading.show();
        console.log(data.field);
        console.log(email)
        console.log($("#email").val())
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
        url: basePath+"/foreign/fextranreq/getCorpBankAcc",
        urldata: {corpId: $("#corpId").val(), curId: bt.selectType.get('curId').val(), bankType: bt.selectType.get('corpBank').val()},
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
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccId').val(data.id);
                $('#corpAccName').val(data.accName);
                $('#corpAccCurId').val(data.curId);
                $("#corpAccCurNameDiv").text(data.curEnglishCode);
                $('#corpId').val(data.corpId);
                $("#corpAccNameDiv").text(data.accName);
                $("#corpNameDiv").text(data.corpName);
                bt.selectType.get('corpBank').val(data.bankTypeId);
                //现金流信息
                $("#srcBankTd").text(data.bankTypeName);
                $("#srcAccNameTd").text(data.accName);
                $("#srcBankAccTd").text(data.bankAcc);
                $("#srcCorpTd").text(data.corpName);
                //国家地区信息
                $('#payerCountryId').val(data.prov);
                $('#payerCountryNameZh').val(data.provName);
                $('#payerEnCode').val(data.countryEnCode);

                //账户类别
                $('#accType').val(data.accType);
                $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));
                //付款行Swift Code
                $("#payBankSwiftCodeDiv").text(data.bankCode);
                //主体银行本地清算号
                if(data.localClearNo){
                    $("#payLocalClearNoDiv").text(data.localClearNo);
                }else{
                    $("#payLocalClearNoDiv").text("");
                }

                //主体单位银行地址
                if(data.bankAdd){
                    $("#payBankAdrrDiv").text(data.bankAdd);
                }else{
                    $("#payBankAdrrDiv").text("");
                }
                //主体单位国家地区
                $("#payNameBankDiv").text(data.provName);
                //主体单位国家地区 简码
                $("#payNameBankEnCode").text(data.provName);
                form.renderIcon();
            } else {
                $("input.text").val("");
            }
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
    $("#country").html(data.countryname)
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
    var  corpId = $("#corpId").val();
    corpBankAccInit.get("corpBankAcc").rebuild({
        urldata: {corpId:corpId,curId: curId, bankType: corpBank},
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
        urldata: {corpId:corpId,curId: curId, bankType: corpBank},
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

function setFpsId(){
    var eMailStr = $("#eMails").val();
    var isdCodeStr = $("#isdCodes").val();
    var phoneCodeStr = $("#phoneCodes").val();
    //email
    if(isNotEmpty(eMailStr)){
        var emails = eMailStr.split(";");
        $("#email").empty();
        for(var i=0;i<emails.length;i++){
            $("#email").append($("<option/>").text(emails[i]).attr("value",emails[i]));
        }
        $("#email").val($("#eMailhid").val());
    }
    //isdCode国家代码
    if(isNotEmpty(isdCodeStr)){
        var isdcodes = isdCodeStr.split(";");
        $("#isdCode").empty();
        for(var i=0;i<isdcodes.length;i++){
            $("#isdCode").append($("<option/>").text(isdcodes[i]).attr("value",isdcodes[i]));
        }
        $("#isdCode").val($("#isdCodehid").val());
    }
    //电话号码(MOBN)
    if(isNotEmpty(phoneCodeStr)){
        var phonecodes = phoneCodeStr.split(";");
        $("#phoneCode").empty();
        for(var i=0;i<phonecodes.length;i++){
            $("#phoneCode").append($("<option/>").text(phonecodes[i]).attr("value",phonecodes[i]));
        }
        $("#phoneCode").val($("#phoneCodehid").val());
    }
}

/**
 * 交易对手账户回调
 * @param data
 */
function externalBankAccCallback(data) {
     if(data.type=="payPal"){

        $("#cotpNameEn").val(data.ecNameEn);//名称英文
        $("#oppId").val(data.externalCorpId);//对手原id
        $("#cotpName").val(data.ecName);//对手名称

        $("#cotpAddressEn").val(data.addressEn)//交易对手地址
        $("#cotpAdrrDiv").html(data.addressEn);
        $("#cotpCountry").val(data.countryId)//交易对手国家地区
         if(isNotEmpty(data.countryName)) {
             $("#cotpCountryDiv").html(data.countryName);
         }


        $("#email").val(data.email);
        $("#isdCode").val(data.countryCode);
        $("#phoneCode").val(data.tel);
         $("#phoneCodeDiv").val(data.phone);

    }
}
