var bankType;
var productType;
var selectType;
var corp;
var dict;
var bankAccInit;
var moneyType;
var moneyFun;
var dateType;
var dateFun;
var rate;
var up;
var selectTableObj = null;
var form;
bt.use(['form','selectType','dicts','corp','selectTable','choProduct','moneyType','dateType','rateType','btUpload','bankInfo'],function () {
    var $ = bt.jquery;
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    selectType=bt.selectType.init();
    form = bt.form();
    dict = bt.dicts.init();
    bt.choProduct.init();
    rate = bt.rateType.init();
    up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    var bankInfo = bt.bankInfo.init();
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
            "bankTypeId": selectType.get("bankTypeId").val(),
        });
        return true;
    }

    //付款账号
    bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath+"/chowmatistic/choproductreq/getBankAcc",
        urldata: {corpId: $("input[name='corpIdHidden']").val(), bankCode: $("#bankInfoId").val(),curId:$("#curId").val(),bankType:$("#bankTypeDef").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#bankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#bankAcc').val(data.bankAcc);
                $('#bankAccId').val(data.id);
                $("#bankName").val(data.bankName);
                $("#bankInfoId").val(data.bankCode);
            }else{
                $('input.text').val('');
            }
        }
    });

    selectTableObj = bt.selectTableType.init({
        elem:".productCode",
        url:basePath+"/chowmatistic/choproduct/json",
        valueFromTable:false,
        columns:[
            {"data":"productCode",title:bt.lang("table.choProduct.productCode")},
            {"data":"bankTypeName",title:bt.lang("table.choProduct.bankTypeId")},
            {"data":"productName",title:bt.lang("table.choProduct.productName")},
            {"data":"productTypeName",title:bt.lang("table.choProduct.productType")}
        ],
        clickCallback:function(data,$tr,$elem,full){
            //data 为当前选中行数据；
            //$tr 为当前选中行jq对象；
            //$elem 为当前输入框jq对象;
            //full 为当前下拉表格所有数据;
            $elem.val(data.productCode)   //给当前输入框赋name值;
            callbackProduct(data)
        },
        tableInitCallback:function(full,$elem){
            //full 为当前选中行数据；
            //$elem 为当前输入框jq对象;
        },
    })

    form.on('submit(submit)',function (data) {
        bt.loading.show();
        var action = '/chowmatistic/choproductMaintain/modify';
        data.field.status = 95
        data.field.attachmentId = up.getGroupId();
        $.post(basePath + action,data.field, function (result) {
            parent.layer.alert(result.msg,{icon:result.success?1:2},function () {
                parent.layer.closeAll();
                if (result.success){
                    if (bt.navtab){
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                        // bt.navtab.refreshCurrent();
                    } else{
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
                bt.loading.hide();
            })
        },'JSON');
        return false;
    })
    financialTypeChange({value:dict.get('financialType').val()})
    form.renderIcon();

    bt.close = function(){
        if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
            bt.navtab.closeCurrent();
        }
    }
});

//单位回调方法
var callbackCorp = function (data) {
    bankAccInit.get("bankAcc").reload({corpId: data, bankCode: $("#bankInfoId").val(),curId:selectType.get("curId").val(),bankType: selectType.get("bankTypeId").val()});
};
//购买银行选择回调回调方法
var callbackBuyBank = function (data) {
    bankType = data.id;
    $("#bankName").val(data.bankName);
    $("#bankInfoId").val(data.bankCode);
    bankAccInit.get("bankAcc").reload({corpId: corp.get("corpId").val(), bankCode: data.bankCode,curId:selectType.get("curId").val(),bankType: selectType.get("bankTypeId").val()});
};
//购买日期的选择回调
var buyDateCallback = function (data) {
    dateType.get("interestStartDate").attr("data-min",data);
    dateType.get("interestStartDate").val(data);
    dateType.get("interestStartDate").render();
    dateType.get("deadlineDate").attr("data-min",data);
    dateType.get("deadlineDate").render();
    dateType.get("cashDate").attr("data-min",data);
    dateType.get("cashDate").render();
    calculateInComme();
    var days = getBetweenDay(data,dateType.get("deadlineDate").val());
    $("input[name='dayLimit']").val(days);
    $("#dayLimitLabel").text(days+"天");
};
//起息日期的选择回调
var interestStartDateCallback = function (data) {
    dateType.get("deadlineDate").attr("data-min",data);
    dateType.get("deadlineDate").render();
    dateType.get("cashDate").attr("data-min",data);
    dateType.get("cashDate").render();
    var days = getBetweenDay(data,dateType.get("deadlineDate").val());
    $("#dayLimitLabel").text(days+"天");
    $("input[name='dayLimit']").val(days);
    calculateInComme();
};
//期限手动修改的回调
var dayLimitCallback = function () {
    calculateInComme();
}
//到期日期的选择回调
var deadlineDateCallback = function (data) {
    var interestStartDate = dateType.get("interestStartDate").val();
    if (data) {
        dateType.get("cashDate").attr("data-min", data);
    }else{
        dateType.get("cashDate").attr("data-min", interestStartDate);
    }
    var days = getBetweenDay(interestStartDate,data);
    $("#dayLimitLabel").text(days+"天");
    $("input[name='dayLimit']").val(days);
    calculateInComme();
};
//参考收益率选择回调
var referIncomeRateCallback = function (data) {
    calculateInComme();
};
//结束参考收益率选择回调
var endReferIncomeRateCallback = function (data) {
    calculateEndInCome();
};
//购买金额回调
var callbackBuyMoney = function () {
    calculateInComme();
    calPortion()
    calculateEndInCome()
};
//收益条款选择回调
var incomeClause = function (data) {
    calculateInComme();
    calculateEndInCome()
};
//产品代码的回调监听
var callbackProduct = function (data) {
    $('#productCode').val(data.productCode);
    dict.get("productType").val(data.productType);
    selectType.get("bankTypeId").val(data.bankTypeId);
    $("#productName").val(data.productName);
    selectType.get("curId").val(data.curId);
    bankAccInit.get("bankAcc").reload({corpId: corp.get("corpId").val(), bankCode: $("#bankInfoId").val(),curId:data.curId,bankType: data.bankTypeId});
    if(data.financialType === '2'){
        dateType.get("deadlineDate").attr("lay-verify","");
    }else{
        dateType.get("deadlineDate").attr("lay-verify","required");
    }
    form.renderIcon();
};

/**
 * 计算两个日期之间的间隔
 * @param startDate
 * @param endDate
 * @returns {number}
 */
function getBetweenDay(startDate,endDate){
    if (null == startDate || null == endDate || ''==endDate || '' == startDate){
        return 0;
    }
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);
    var btt = date2 - date1;
    return btt/(1000*60*60*24);
}

/**
 * 计算参考收益
 */
function calculateInComme() {
    var days = getBetweenDay(dateType.get("interestStartDate").val(),dateType.get("deadlineDate").val());
    var buyMoney = moneyType.get("buyMoney").val();
    var referIncomeRate = rate.get("referIncomeRate").val();
    var incomeClause = dict.get("incomeClause").val();
    var value = buyMoney*referIncomeRate*days/incomeClause;
    // if (value != null && value != ""){
        moneyType.get("referIncomeMoney").val(value);
    // }
}

/**
 * 计算结束参考收益
 */
function calculateEndInCome() {
    var days = getBetweenDay(dateType.get("interestStartDate").val(),dateType.get("deadlineDate").val());
    var buyMoney = moneyType.get("buyMoney").val();
    var referIncomeRate = rate.get("endReferIncomeRate").val();
    var incomeClause = dict.get("incomeClause").val();
    var value = buyMoney*referIncomeRate*days/incomeClause;
    moneyType.get("endReferIncomeMoney").val(referIncomeRate === '' ? '' : value);
}

closeTab = function(){
    bt.close();
}

// 理财类型回调函数
function financialTypeCallback(data) {
    var value = data.value;
    selectTableObj.get("productCode").setUrl({financialType: value});
    var bankAcc = $('#bankAcc');
    var incomeType = dict.get('incomeType');

    // 隐藏是否维护日净值
    $('.isMaintainVal').attr('style', 'display:none')
    if (value === '1') { // 保本浮动型
        bankAcc.attr('lay-verify', 'required|tableData')
        incomeType.attr({'data-required': false})
        rate.get('startContractRate').attr('lay-verify', 'none')
        $('.fType').attr('style', 'display:none')
        $('.isShowReferIncome').attr('style', 'display:true')
        $('.isShowDaily').attr('style', 'display:none')
        rate.get("referIncomeRate").attr('lay-verify', 'required')
        moneyType.get("dailyNetVal").attr('lay-verify', 'none')
    } else if (value === '0') { // 保本收益型
        bankAcc.removeAttr('lay-verify')
        incomeType.attr({'data-required': true})
        rate.get('startContractRate').attr('lay-verify', 'required')
        $('.fType').attr('style', 'display:true')
        $('.isShowReferIncome').attr('style', 'display:true')
        $('.isShowDaily').attr('style', 'display:none')
        rate.get("referIncomeRate").attr('lay-verify', 'required')
        moneyType.get("dailyNetVal").attr('lay-verify', 'none')
    } else if (value === '2') { // 非保本浮动型
        bankAcc.attr('lay-verify', 'required|tableData')
        incomeType.attr({'data-required': true})
        rate.get('startContractRate').attr('lay-verify', 'none')
        $('.fType').attr('style', 'display:none')
        $('.fType2').attr('style', 'display:true')
        $('.isShowReferIncome').attr('style', 'display:none')
        $('.isShowDaily').attr('style', 'display:true')
        rate.get("referIncomeRate").attr('lay-verify', 'none')
        moneyType.get("dailyNetVal").attr('lay-verify', 'required')

        // 显示是否维护日净值
        $('.isMaintainVal').attr('style', 'display:true')
    }
    moneyType.get("dailyNetVal").val('')
    moneyType.get("portion").val('')
    bankAcc.val('');
    $('#bankAccId').val('');
    $("#bankName").val('');
    $("#bankInfoId").val('');
    selectTableObj.get("productCode").val('')
    callbackProduct({
        bankTypeId: '',
        financialType: value,
        productName: '',
        curId: '',
        bankAcc: ''
    })
    dict.get('isStore').val(0)
    dict.get('payWay').val('')
    incomeType.val('');
    $("#contractNo").val('');
    rate.get('startContractRate').val('')
    rate.get('endContractRate').val('')

    financialTypeChange({value:value})
    form.renderIcon();
}


function dailyNetValCallback() {
    calPortion()
}

/**
 * 计算份额
 */
function calPortion() {
    var buyMoney = moneyType.get("buyMoney").val();
    var dailyNetVal = moneyType.get("dailyNetVal").val();
    if (dailyNetVal !== '') {
        moneyType.get("portion").val(buyMoney / dailyNetVal)
    }
}

// 理财类型回调函数
function financialTypeChange(data) {
    var filterData
    if (data.value === '1') {
        filterData = '3,4,5,6'
    } else if (data.value === '0') {
        filterData = '1,2'
    } else if (data.value === '2') {
        filterData = '2,3,4,5,6'
    }
    dict.get("productType").attr({'data-filterData': filterData})
    bt.dicts.render()
}

// 重新渲染银行类别组件
function renderBankAccInit() {
    bankAccInit.get("bankAcc").reload({corpId: corp.get('corpId').val(), bankCode: $("#bankInfoId").val(),
        curId:selectType.get("curId").val(),bankType: selectType.get("bankTypeId").val()});
}

// 银行类别回调函数
function bankTypeIdCallback(data) {
    renderBankAccInit()
}

// 币别回调函数
function curIdCallback(data) {
    renderBankAccInit()
}

// 自动生成合同号
function createContractNo() {
    $.ajax({
        url: basePath+'/chowmatistic/choproductBuyApply/createContractNo',
        method: 'GET',
        async:false,
        success: function (json) {
            var obj = JSON.parse(json);
            $('#contractNo').val(obj.obj)
            form.render();
        }
    });
}