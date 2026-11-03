var form, element, corp, dateType, selectType, moneyType, boodAccInit, upload, ratioType,dicts;
// 存贮校验失败的信息
var verifyFailElemInfo = {
    elem:null,
    msg:'',
    className:'',
    item:null
}


bt.use(["jquery", "form", "corp", "dateType", "selectType", "moneyType", "rateType",
    "selectTableType", "dicts", "selectTable","btUpload","ratioType"], function () {
    $ = bt.jquery;
    form = bt.form();
    element = layui.element;
    corp = bt.corp.init();//单位
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateType = bt.rateType.init();
    dicts = bt.dicts.init();
    ratioType = bt.ratioType.init();
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
        bt.loading.show();
        $.ajax({
            url: basePath + action,
            type: "post",
            data: data.field,
            dataType: "json",
            async: false,
            success: function (result) {
                bt.loading.hide();
                parent.closeTab(result);

            }
        });
        return false;
    });

    //交易编码
    var tranCodeInit = bt.selectTableType.init({
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
            }
        }
    });

//保证金账户
    boodAccInit = bt.selectTable.init({
        elem: "#boodAccTable",
        url: basePath + "/foreign/fexTranApp/getBoodBankAcc",
        scrollX: true,
        scrollY: "200px",
        searchInput: "#boodAcc",
        paging: true,
        columns: [
            {data: "bankAcc"},
            {data: "accName"},
            {data: "bankName"},
            {data: "curName"}
        ],
        clickCallback: function (data) {
            if (data) {
                $("#boodAcc").val(data.bankAcc);
                $("#boodAccId").val(data.id);
                selectType.get("bondCurId").val(data.curId);
            } else {
                $("input.text").val("");
            }
        }
    });

    form.on("checkbox(autoCal)", function(data){
        calWishRate();
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
 * 计算交割期限为T+N
 * @param data
 * @param elem
 */
function calDeliveryDeadline() {
    var tranDate = dateType.get("tranDate").val();
    var settleDate = dateType.get("settleDate").val();
    if (isEmpty(tranDate) || isEmpty(settleDate)) {
        return false;
    }
    var num = bt.date.getBetweenDay(tranDate, settleDate);
    // 设置交割期限
    $("#deliveryDeadlineShow").html("T+" + num);
    $("#deliveryDeadline").val(num);
    if (0 != num) {
        // 保证方式
        dicts.get("boodType").attr({"data-value": "", "data-required": true});

        // // 保证比例
        ratioType.get("boodRatio").attr({"data-value": "", "lay-verify": "required"});
        // // 保证金币种
        // selectType.get("bondCurId").attr({"data-value": "", "data-required": true});
        // dicts.get("boodBankNote").attr({"data-value":"1", "data-required": true});
    } else {
        dicts.get("boodType").attr({"data-value": "", "data-required": false});
        ratioType.get("boodRatio").attr({"data-value": "", "lay-verify": ""});
        // selectType.get("bondCurId").attr({"data-value": "", "data-required": false});
        // dicts.get("boodBankNote").attr({"data-value":"1", "data-required": false});
        // moneyType.get("payMoney").attr("lay-verify", "");
    }
    form.renderIcon();
}

/**
 * 授信方式选择授信额度时则额度必填设置
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function boodTypecallBack(data,text,selectedData,beforeValue) {
    if (selectedData.value == 4) {
        // 授信额度时必填
        moneyType.get("payMoney").attr("lay-verify", "required");
        $("#boodRmk").attr("lay-verify", "");
        selectType.get("bondCurId").attr({"data-value": "", "data-required": false});
        $("#boodAcc").attr("lay-verify", "");
    } else if (selectedData.value == 3) {
        // 其他保证方式说明
        moneyType.get("payMoney").attr("lay-verify", "");
        $("#boodRmk").attr("lay-verify", "required");
        selectType.get("bondCurId").attr({"data-value": "", "data-required": false});
        $("#boodAcc").attr("lay-verify", "");
    }else if ((1 == selectedData.value || 2 == selectedData.value) && Number($("#deliveryDeadline").val()) > 0) {
        // 保证金账户
        moneyType.get("payMoney").attr("lay-verify", "");
        $("#boodRmk").attr("lay-verify", "");
        selectType.get("bondCurId").attr({"data-value": "", "data-required": true});
        $("#boodAcc").attr("lay-verify", "required");
    }
    form.renderIcon();
}

/**
 * 当结汇用途为005、 006、099时资金详细必输
 * @param data
 * @param text
 * @param selectedData
 * @param beforeValue
 */
function purposeCallBack(data,text,selectedData,beforeValue) {
    if ("005" == selectedData.value || "006" == selectedData.value || "099" == selectedData.value) {
        $("#purposeDtl").attr("lay-verify", "required");
    } else {
        $("#purposeDtl").attr("lay-verify", "");
    }
    form.renderIcon();
}

/**
 * 提交数据
 * @param data
 */
function getData() {
    if (!checkForm()) {
        return false;
    }
    $("#gid").val(upload.getGroupId());
    return $("#myform").serialize();
}

/**
 * 期望汇率计算
 */
function calWishRate() {
    var buyCurId = selectType.get("buyCurId").val();
    var buyMoney = moneyType.get("buyMoney").val();
    var sellCurId = selectType.get("sellCurId").val();
    var sellMoney = moneyType.get("sellMoney").val();
    if (isNotEmpty(buyCurId) && isNotEmpty(buyMoney) && isNotEmpty(sellCurId) && isNotEmpty(sellMoney)) {
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
                        moneyType.get("wishRate").val("");
                    } else {
                        setWishRate(buyCurId, sellCurId, buyMoney, sellMoney, data.obj);
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
function setWishRate(buyCurId, sellCurId, buyMoney, sellMoney, data) {
    var autoCal = $("input[type = 'checkbox'][name = 'autoCal']:checked");
    var wishRate = moneyType.get("wishRate").val();
    if (isNotEmpty(wishRate)) {
        // 期望汇率存在值
        if (autoCal.length > 0) {//计算买入
            if (data.baseCurId == buyCurId) {//买入币种为标准
                moneyType.get("buyMoney").val(bt.money.div(sellMoney, wishRate,2));
            } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                moneyType.get("buyMoney").val(bt.money.mul(sellMoney, wishRate,2));
            }
        } else {//计算卖出
            if (data.baseCurId == buyCurId) {//买入币种为标准
                moneyType.get("sellMoney").val(bt.money.mul(buyMoney, wishRate,2));
            } else if (data.baseCurId == sellCurId) {//卖出币种为标准
                moneyType.get("sellMoney").val(bt.money.div(buyMoney, wishRate,2));
            }
        }
    } else {
        // 期望汇率不存在值
        if (data.baseCurId == buyCurId) {//买入币种为标准
            moneyType.get("wishRate").val(bt.money.div(sellMoney, buyMoney,2));
        } else if (data.baseCurId == sellCurId) {//卖出币种为标准
            moneyType.get("wishRate").val(bt.money.div(buyMoney, sellMoney,2));
        }
    }

}

/**
 * 改变保证金币种时清空账户信息
 */
function bondCurIdCallBack() {
    $("#boodAcc").val("");
    $("#boodAccId").val("");
    var bondCurId = selectType.get("bondCurId").val();
    var corpId = $("#corpId").val();
    boodAccInit.get("boodAcc").setUrl({"curId": bondCurId, "corpId": corpId});
}

/**
 * 手动表单必填校验
 * @returns {boolean}
 */
function checkForm() {
    var  verify = form.config.verify, stop = null
        ,DANGER = 'layui-form-danger'
        ,verifyElem = $("#myform").find('*[lay-verify]') //获取需要校验的元素
    // 开始校验
    layui.each(verifyElem, function(_, item){
        // if($(item)[0].tagName =='SELECT' || $(item).css('display') != 'none' ){
        if(!($(item).parents(".ignore-required-hide").length > 0 && $(item).parents(".ignore-required-hide").css('display') == 'none')){
            //刘勇军，如果验证的标签display 为none 且不是select，将不对其进行验证
            var othis = $(this), ver = othis.attr('lay-verify').split('|');
            var tips = '', value = othis.val();
            if ( othis.attr('multiple')){
                value = othis.siblings('input').val();
            }
            value = $.trim(value);//添加去除空格
            othis.removeClass(DANGER);
            //新增加复选框的校验 liuyj
            if(othis[0].type ==='checkbox' || othis[0].type ==='radio' ){
                var name = othis.attr('name');
                var checkboxParent = othis.parent();
                if( checkboxParent.find('input[name="'+name+'"]:checked').length === 0){
                    // layer.msg(tips || verify['required'][1], {
                    //     icon: 5
                    //     ,shift: 6
                    // });
                    //非移动设备自动定位焦点
                    // item.focus();
                    verifyFailElemInfo.msg = tips || verify['required'][1];
                    verifyFailElemInfo.item = item
                    verifyFailElemInfo.className = ''
                    verifyFailElemInfo.elem = null

                    return stop = true;
                }
            }
            layui.each(ver, function(_, thisVer){
                var isFn = typeof verify[thisVer] === 'function';
                //刘勇军 对有千分位的数字进行处理验证
                // if(thisVer =='number'){
                //     if(value.indexOf(',') !==-1){
                //         value = value.replace(/\,/g,'');
                //     }
                // }
                if(othis.hasClass('bt-biz-moneyType')){
                    var separator = othis.data('separator');
                    separator = separator ? separator:',';
                    if(value.indexOf(separator) !==-1){
                        value = value.replace(eval('/\\'+separator+'/g'),'');
                    }
                }
                if(verify[thisVer] && (isFn ? tips = verify[thisVer](value, item) : !verify[thisVer][0].test(value))){
                    // parent.layer.msg(tips || verify[thisVer][1], {
                    //     icon: 5
                    //     ,shift: 6
                    // });
                    verifyFailElemInfo.msg = tips || verify[thisVer][1];
                    verifyFailElemInfo.elem = othis
                    verifyFailElemInfo.className = DANGER
                    verifyFailElemInfo.item = item
                    // item.focus();
                    // othis.addClass(DANGER);
                    return stop = true;
                }
            });
        }
        if(stop) return stop;
    });
    bt.loading.hide();
    if (stop) {
        return false;
    } else {
        return true;
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

/**
 * 设置数据值
 * @param data
 */

function setFormData(data) {
    var fexBusType = $("#fexBusType").val();
    $("#tranCode1").val(data.tranCode1);//交易编码
    $("#tranName1").val(data.tranName1);//交易编码名
    dicts.get("bankNote").val(data.bankNote);//钞汇标识
    dateType.get("tranDate").val(data.tranDate);//交易日期
    dicts.get("boodType").val(data.boodType);//保证方式
    $("#boodAccId").val(data.boodAccId);//保证金账号id
    $("#boodAcc").val(data.boodAcc);//保证金账号
    ratioType.get("boodRatio").val(data.boodRatio);//保证金比例
    dicts.get("boodBankNote").val(data.boodBankNote);//保证金钞汇标识
    selectType.get("bondCurId").val(data.bondCurId);//保证金币种
    $("#contractNo").val(data.contractNo);//合同号
    moneyType.get("payMoney").val(data.payMoney);//额度
    $("#busNo").val(data.busNo);//外汇局批件号
    dicts.get("purpose").val(data.purpose);//结汇资金用途
    $("#purposeDtl").val(data.purposeDtl);//结汇资金详细用途
    dicts.get("fundsSource").val(data.fundsSource);//资金来源
    $("#invoiceNo").val(data.invoiceNo);//发票号
    dicts.get("accNature").val(data.accNature);//账号资金性质
    if (fexBusType != 3) {
         dicts.get("fexProp").val(data.fexProp);
    }
    $("#corpContacts").val(data.corpContacts);//联系人
    $("#corpContactsTel").val(data.corpContactsTel);//联系电话
    $("#boodRmk").val(data.boodRmk);//其他保证方式说明
    $("#rmk").val(data.rmk)//备注
    dicts.get("remitType").val(data.remitType);//汇款类型
    if (fexBusType == "1") {
        dicts.get("settleBusType").val(data.settleBusType);//结汇业务类型
    }

}

/**
 * 获取表单数据
 * @returns {*|jQuery}
 */
function getCopyData() {
    var fexBusType = $("#fexBusType").val();
    var data = {};
    data.tranCode1 = $("#tranCode1").val();//交易编码
    data.tranName1 = $("#tranName1").val();//交易编码名
    data.bankNote = dicts.get("bankNote").val();//钞汇标识
    data.tranDate = dateType.get("tranDate").val();//交易日期
    data.boodType = dicts.get("boodType").val();//保证方式
    data.boodAccId = $("#boodAccId").val();//保证金账号id
    data.boodAcc = $("#boodAcc").val();//保证金账号
    data.boodRatio = ratioType.get("boodRatio").val();//保证金比例
    data.boodBankNote = dicts.get("boodBankNote").val();//保证金钞汇标识
    data.bondCurId = selectType.get("bondCurId").val();//保证金币种
    data.contractNo = $("#contractNo").val();//合同号
    data.payMoney = moneyType.get("payMoney").val();//额度
    data.busNo = $("#busNo").val();//外汇局批件号
    data.purpose = dicts.get("purpose").val();//结汇资金用途
    data.purposeDtl = $("#purposeDtl").val();//结汇资金详细用途
    data.fundsSource = dicts.get("fundsSource").val();//资金来源
    data.invoiceNo = $("#invoiceNo").val();//发票号
    data.accNature = dicts.get("accNature").val();//账号资金性质
    if (fexBusType != 3) {
        data.fexProp = dicts.get("fexProp").val();
    }
    data.corpContacts = $("#corpContacts").val();//联系人
    data.corpContactsTel = $("#corpContactsTel").val();//联系电话
    data.boodRmk = $("#boodRmk").val();//保证金其他说明
    data.rmk = $("#rmk").val()//备注
    data.remitType = dicts.get("remitType").val();//汇款类型
    if (fexBusType == "1") {
        data.settleBusType = dicts.get("settleBusType").val();//结汇业务类型
    }
    return data;
}

/**
 * 币种加载完时，加载数据
 */
function setbondCurId() {
    /**
     * 加载近端交易数据到远端
     */
    /*if (1 == $("#isSwaptTran").val() && $("#swapType").val() == 2) {
        if (isNotEmpty($("#id").val())) {
            var settleDate = dateType.get("settleDate").val();
            if (isEmpty(settleDate)) {
                setFormData(parent.getCopyData());
            }
        } else {
            setFormData(parent.getCopyData());
        }
    }*/
}

/**
 * 显示校验失败的信息
 */
function showVerifyMsg() {

    // 处理校验失败的相关逻辑
    verifyFailElemInfo.msg && parent.layer.msg(verifyFailElemInfo.msg, {
        icon: 5
        ,shift: 6
    });
    verifyFailElemInfo.item && verifyFailElemInfo.item.focus();
    if(verifyFailElemInfo.className && verifyFailElemInfo.elem){
        verifyFailElemInfo.elem.addClass(verifyFailElemInfo.className);
    }
    // 重置相关信息
    verifyFailElemInfo.msg = '';
    verifyFailElemInfo.elem = null
    verifyFailElemInfo.item = null
    verifyFailElemInfo.className = ''

}
