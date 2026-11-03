/*开证查看页面公共方法写在此处，页面特有方法请放在页面内*/

/**
 * 查看授信品种和额度
 */
function viewRationDtl() {
    var ratItemCode = $("#ratItemCode").val();
    var itemMoney = $("#itemMoney").val();
    var param = {
        corpId: $("#corpId").val(),
        curCode: $("#curCode").val(),
        bankCode: $("#bankCode").val(),
        ratItemCode: ratItemCode,
        beginDate: $("#beginDate").val(),
        ratCode: $("#ratCode").val(),
        itemFlag: $("#ratmodel").val(),
        itemMoney: $("#itemMoney").val(),
        isRmbRat: $("#isNativeCur").val(),
        itemCode: ratItemCode,
        useMaxMoney: itemMoney
    };

/*    var applyDtls = [];
    var applyDtl = {};
    applyDtl["itemCode"] = ratItemCode;
    applyDtl["useMaxMoney"] = itemMoney;
    applyDtls.push(applyDtl);
    param["applyDtls"] = applyDtls;*/
    var layerObj = parent.layer ? parent.layer : layer;
    layerObj.open({
        type: 2,
        area: ['80%', '70%'],
        maxmin: true,
        content: basePath + "/cms/rat/raturation/viewRationDtl?" + parseParam(param),
        end: function () {

        }
    });
}

/**
 * json转URL参数
 * @param param
 * @param key
 * @returns {string}
 */
function parseParam(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
}


/*费用计算*/
function compCostCalculate(nextValue,elem,preValue) {
    /*开证手续费*/
    var chargeMoney = moneyType.get("chargeMoney").val();
    /*其他费用*/
    var otherFee = moneyType.get("otherFee").val();
    /*承兑费*/
    var acceptanceFee = moneyType.get("acceptanceFee").val();
    /*贴现息*/
    var discountInterest = moneyType.get("discountInterest").val();
    /*贸易亏损*/
    var tradeDeficit = moneyType.get("tradeDeficit").val();
    var compCost = Number(chargeMoney) + Number(otherFee) + Number(acceptanceFee) + Number(discountInterest) + Number(tradeDeficit);
    moneyType.get("compCost").val(compCost);
    var acceptanceMoney = moneyType.get("acceptanceMoney").val();
    moneyType.get("rebackMoney").val(Number(acceptanceMoney) - Number(compCost));
}


/**
 * 关闭
 */
function closeNavtab() {
    if (bt.navtab != null) {
        bt.navtab.closeCurrent();
    }
}


/**
 *
 * @param param
 */
function setOtherParam(param){
    var viewFlag = $("#viewFlag").val();
    var corpId;//网点
    var curId;//币别
    var startDate;//开始日期
    var endDate;//结束日期
    if(viewFlag=="1"){
        startDate = $("#startDate").val();//开始日期
        endDate = $("#enddate").val() ;//结束日期
        corpId = $("#applicant").val();//网点id
        curId = $("#curCode").val();
    }else{
        corpId = corp.get("applicant").val();//网点id
        startDate = $.trim(dateObj.get("issuingDate").val());//开始日期
        endDate = $.trim(dateObj.get("expiryDate").val());//结束日期
        curId = selectType.get("issuingCurId").val();//币别
    }
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "": endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}