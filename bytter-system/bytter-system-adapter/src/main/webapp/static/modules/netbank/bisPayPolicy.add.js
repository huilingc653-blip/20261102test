var $, form;
var noPayeeParams = {};// 不支付收款人参数
bt.use(['form', 'layedit', 'dicts', 'moneyType', 'selectType', 'corp', 'element', 'mulselect', 'externalCorpInfo', 'externalCorpView', 'bankAccInfo', 'bankAccView'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    var selectType = bt.selectType.init();
    var moneyType = bt.moneyType.init();

    //判断字符串是否为空
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //供应商选择
    var externalCorpInfo = bt.externalCorpInfo.init();
    var externalCorpView = bt.externalCorpView.init();

    bt.externalCorpInfoParams = function () {
        var corpId = $('input[name="corpId"]').val();
        if (!corpId) {
            corpId = "-1";
        }
        externalCorpInfo.set({
            "corpId": corpId,
            "id": $("#payeeId").val()
        });
        return true;
    }

    // 支付收款人查看
    bt.externalCorpViewParams = function () {
        externalCorpView.set({"id": $("#payeeId").val()});
        return true;
    }

    // 支付收款人选择回调
    bt.externalCorpInfoCallback = function (datas, rom) {
        var payeeId = "";
        $.each(datas.data, function (index, data) {
            if (payeeId === "") {
                payeeId += data.id;
            } else {
                payeeId += "," + data.id;
            }
        });
        $("#payeeId").val(payeeId);
    }

    //支付账号选择
    var bankAccInfo = bt.bankAccInfo.init();
    var bankAccView = bt.bankAccView.init();
    bt.bankAccInfoParams = function () {
        var corpId = $('input[name="corpId"]').val();
        var curId = selectType.get("curId").val();
        if (!corpId) {
            corpId = "-1";
        }
        if (!curId) {
            curId = "-1";
        }
        bankAccInfo.set({
            "corpId": corpId,
            "curId": curId,
            "id": $("#payerAccId").val()
        });
        return true;
    }
    bt.bankAccViewParams = function () {
        bankAccView.set({
            "id": $("#payerAccId").val(),
            "curId": selectType.get("curId").val()
        });
        return true;
    }
    bt.bankAccInfoCallback = function (datas, rom) {
        var payerAccId = "";
        var i = 0;
        var autoPayerAccNum = $("#autoPayerAccNum").val();
        $.each(datas.data, function (index, data) {
            if (payerAccId === "") {
                payerAccId += data.id;
            } else {
                payerAccId += "," + data.id;
            }
            i = i + 1;
        });
        if (i > autoPayerAccNum) {
            layer.alert(bt.lang("netbank.message.autoPayerAccNum.limit", [autoPayerAccNum]), {icon: 2});
        } else {
            $("#payerAccId").val(payerAccId);
        }
    }

    //单位回调函数
    bt.corpCallback = function (id) {
        // 切换单位，清空选择的支付收款方，不支付收款人，支付账号，不支付用途，共享业务类型
        $("#payerAccId").val('');// 支付账号
        $("#payeeId").val('');// 支付收款人
        $("#noPayPurpose").val('');// 不支付用途
   /*     bt.mulselect.get("shareBusTypeId").attr({"data-url": basePath + "/netbank/nissharebustype/listValidShareBusTypeByBusType?busTypeId="});// 共享业务类型
        bt.mulselect.get("shareBusTypeId").val("");*/

        if (bt.empty(id)) {
            // 业务类别置空
            bt.mulselect.get("busTypeId").attr({"data-url": basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=-1"});
            return false;
        }
        // 重新设置业务类别
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=1&corpId=" + id;
        bt.mulselect.get("busTypeId").attr({"data-url": busTypeUrl});
        bt.mulselect.get("busTypeId").val("");

    }

    // 业务类别回调函数
    bt.busTypeCallback = function (data) {
        // 共享业务类型置空
     /*   bt.mulselect.get("shareBusTypeId").val("");*/
        var busTypeIds = '';
        if (!bt.empty(data) && data.length > 0) {
            for (var a = 0; a < data.length; a++) {
                if (bt.empty(busTypeIds)) {
                    busTypeIds += data[a].busId;
                } else {
                    busTypeIds += ',' + data[a].busId;
                }
            }
        }
       // bt.mulselect.get("shareBusTypeId").attr({"data-url": basePath + "/netbank/nissharebustype/listValidShareBusTypeByBusType?busTypeId=" + busTypeIds});// 共享业务类型
    }

    bt.corp.init();
    bt.mulselect.init();

    //币别回调函数
    bt.curCallback = function (data) {
        //切换币种，清空选择的支付收款方，不支付收款人，支付账号
        $("#payerAccId").val('');// 支付账号
        $("#payeeId").val('');// 支付收款人
    }

    form.on('checkbox(policyType)', function (data) {
        if (data.elem.checked) {
            $("#sortDiv" + data.value).show();
            moneyType.get('sort' + data.value).attr('lay-verify', 'required|number2');
        } else {
            $("#sortDiv" + data.value).hide();
            moneyType.get('sort' + data.value).attr('lay-verify', 'number2');
        }
        moneyType.get('sort' + data.value).val('');
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var sort = "";
        var policyType = "";
        $('form').find('input[name="policyType"]').each(function () {
            if ($(this).is(':checked')) {
                policyType = policyType + $(this).val() + ",";
                sort = sort + moneyType.get('sort' + $(this).val()).val() + ",";
            }
        });
        data.field.sort = sort;
        data.field.policyType = policyType;

        var params = data.field
        // 合并不支付收款人参数
        Object.assign(params, noPayeeParams)

        bt.loading.show();//显示加载中
        var action = "/netbank/bispaypolicy/save";
        $.post(basePath + action, params, function (result) {
            bt.loading.hide(); //关闭加载中
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2})
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshParentTable()
                    bt.navtab.refreshCurrent()
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
                // 清空不支付收款人参数
                noPayeeParams = {};
            }
        }, 'JSON');
        return false;
    });

});

//业务种类初始化前回调
busBefore = function (data) {
    if (Array.isArray(data)) {
        bt.each(data, function (i, item) {
            item.busTypeName = "(" + item.busType + ")" + item.busTypeName;
        })
    }
    return data;
}

// 不支付收款人新增
function addNoPayee() {
    var params = "";
    if (Object.keys(noPayeeParams).length !== 0) {
        // 将对象转成字符串。如 {a:'aaa',b:'bbb'}转成 "a='aaa'&b='bbb'"
        params = Object.keys(noPayeeParams).map(function (key) {
            return encodeURIComponent(key) + '=' + encodeURIComponent(noPayeeParams[key]);
        }).join('&')
    }
    var url;
    if (params === "") {
        url = basePath + "/netbank/bispaypolicy/addNoPayee?id=" + $("#id").val();
    } else {
        url = basePath + "/netbank/bispaypolicy/addNoPayee?id=" + $("#id").val() + "&" + params;
    }
    parent.layer.open({
        type: 2,
        title: "不支付收款人",
        area: ['1000px', '750px'],
        maxmin: true,
        content: url,
        end: function () {
        }
    });
}

// 不支付收款人查看
function viewNoPayee() {
    var params = "";
    if (Object.keys(noPayeeParams).length !== 0) {
        // 将对象转成字符串。如 {a:'aaa',b:'bbb'}转成 "a='aaa'&b='bbb'"
        params = Object.keys(noPayeeParams).map(function (key) {
            return encodeURIComponent(key) + '=' + encodeURIComponent(noPayeeParams[key]);
        }).join('&')
    }
    var url;
    if (params === "") {
        url = basePath + "/netbank/bispaypolicy/viewNoPayee?id=" + $("#id").val();
    } else {
        url = basePath + "/netbank/bispaypolicy/viewNoPayee?id=" + $("#id").val() + "&" + params;
    }
    parent.layer.open({
        type: 2,
        title: "不支付收款人",
        area: ['1000px', '750px'],
        maxmin: true,
        content: url,
        end: function () {
        }
    });
}

// 获取不支付收款人数据
function setChildNoPayeeParam(params) {
    noPayeeParams = params;
}