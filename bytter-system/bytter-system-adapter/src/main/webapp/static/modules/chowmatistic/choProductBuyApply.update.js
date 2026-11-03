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
bt.use(['form','selectType','dicts','corp','selectTable','choProduct','moneyType','dateType','rateType','btUpload','bankInfo','choProductType'],function () {
    var $ = bt.jquery;
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    selectType=bt.selectType.init();
    var choProductType = bt.choProductType.init();
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

    //参考收益和参考收益率显示优化
    $('.isShowReferIncome').on('click','.layui-icon',function (){
        var key = $(this).data('key');
        if(key ==='add'){
            $('#addBox').show();
            $('#addBox2').show();
            $(this).hide();
            $('[data-key="remove"]').show();
        }else if(key === 'remove'){
            $('#addBox').hide();
            $('#addBox2').hide();
            $(this).hide();
            $('[data-key="add"]').show();
            rate.get("endReferIncomeRate").val(null);
            moneyType.get("endReferIncomeMoney").val('');
        }
    })

    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
            "bankTypeId": selectType.get("bankTypeId").val(),
        });
        return true;
    }

    bt.choProductTypeBeforeClick = function () {
        choProductType.set({
            "financialType": dict.get("financialType").val(),
            "productType": dict.get("productType").val(),
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

    // selectTableObj = bt.selectTableType.init({
    //     elem:".productCode",
    //     url:basePath+"/chowmatistic/choproduct/json",
    //     valueFromTable:false,
    //     columns:[
    //         {"data":"productCode",title:bt.lang("table.choProduct.productCode")},
    //         {"data":"bankTypeName",title:bt.lang("table.choProduct.bankTypeId")},
    //         {"data":"productName",title:bt.lang("table.choProduct.productName")},
    //         {"data":"productTypeName",title:bt.lang("table.choProduct.productType")}
    //     ],
    //     clickCallback:function(data,$tr,$elem,full){
    //         //data 为当前选中行数据；
    //         //$tr 为当前选中行jq对象；
    //         //$elem 为当前输入框jq对象;
    //         //full 为当前下拉表格所有数据;
    //         $elem.val(data.productCode)   //给当前输入框赋name值;
    //         callbackProduct(data)
    //     },
    //     tableInitCallback:function(full,$elem){
    //         //full 为当前选中行数据；
    //         //$elem 为当前输入框jq对象;
    //     },
    // })

    bt.choProductTypeApplyButCallback = function (data, dom) {
        $('#productCode').val(data.productCode);
        $("#productId").val(data.id);
        dict.get("productType").val(data.productType);
        selectType.get("bankTypeId").val(data.bankTypeId);
        $("#productName").val(data.productName);
        selectType.get("curId").val(data.curId);
        //如果是靠档计息产品是否续存那里改为【否】，且无法改为【是】
        if(data.productType === '7'){
            dict.get("isStore").val('0');
            dict.get("isStore").attr({'data-disabled':true});
            //清空靠档计息时保存的利率明细id
            $("#rateDetailIds").val("");
        }
        bankAccInit.get("bankAcc").reload({corpId: corp.get("corpId").val(), bankCode: $("#bankInfoId").val(),curId:data.curId,bankType: data.bankTypeId});
        if(data.financialType === '2'){ // 非保本
            dateType.get("deadlineDate").attr("lay-verify","");
        }else{
            dateType.get("deadlineDate").attr("lay-verify","required");
        }
        if(dict.get("productType").val() == '7'){
            //去掉参考收益率必填
            rate.get("referIncomeRate").attr('lay-verify','none')
            $('.referIncomeRate').addClass('layui-hide');
            $('.interestRateBasedOnTiers').removeClass('layui-hide');
            //到期日期为非必填
            dateType.get("deadlineDate").attr("lay-verify","");
        }else{
            if(dict.get("financialType").val()!='2'){
                rate.get("referIncomeRate").attr('lay-verify','required')
                $('.referIncomeRate').removeClass('layui-hide');
            }
            $('.interestRateBasedOnTiers').addClass('layui-hide');
            //到期日期为非必填
            dateType.get("deadlineDate").attr("lay-verify","required");
        }
        dict.render()
        form.renderIcon();

    }


    form.on('submit(submit)',function (data) {
        bt.loading.show();
        var action = '/chowmatistic/choproductBuyApply/modify';
        data.field.buyApplyStatus = $(this).attr("value");
        data.field.attachmentId = up.getGroupId();
        if(dict.get("productType").val() == '7'){
            var rateDetailIds = $("#rateDetailIds").val()
            if (rateDetailIds){
                data.field.rateDetailIds = rateDetailIds;
            }else{
                parent.layer ? parent.layer.alert(bt.lang("cho.product.message.rateDetail.canNotNull"), {icon: 2}) : layer.alert(bt.lang("cho.product.message.rateDetail.canNotNull"), {icon: 2});
                bt.loading.hide();
                return false;
            }
        }
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
    var days = getBetweenDay(data,dateType.get("deadlineDate").val());
    $("input[name='dayLimit']").val(days);
    $("#dayLimitLabel").text(days+"天");
    calculateInComme();
    calculateEndInCome()
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
    calculateEndInCome()
    //清空靠档计息时保存的利率明细id
    $("#rateDetailIds").val("");
};
//期限手动修改的回调
var dayLimitCallback = function () {
    calculateInComme();
    calculateEndInCome()
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
    calculateEndInCome()
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
    var days = getBetweenDay(dateType.get("interestStartDate").val(),dateType.get("deadlineDate").val());
    $("#dayLimit").val(days);
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
    if(dict.get("productType").val() == '7'){
        //去掉参考收益率必填
        rate.get("referIncomeRate").attr('lay-verify','none')
        $('.referIncomeRate').addClass('layui-hide');
        $('.interestRateBasedOnTiers').removeClass('layui-hide');
        //到期日期为非必填
        dateType.get("deadlineDate").attr("lay-verify","");
    }else{
        if(dict.get("financialType").val()!='2'){
            rate.get("referIncomeRate").attr('lay-verify','required')
            $('.referIncomeRate').removeClass('layui-hide');
        }
        $('.interestRateBasedOnTiers').addClass('layui-hide');
        //到期日期为非必填
        dateType.get("deadlineDate").attr("lay-verify","required");
    }
    dict.render()
    form.renderIcon();
};

/**
 * 计算两个日期之间的间隔
 * @param startDate
 * @param endDate
 * @returns {number}
 */
function getBetweenDay(startDate,endDate){
    if (null == startDate || null == endDate || '' == startDate || '' == endDate){
        return 0;
    }
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);

    //判断收益条款，为0（30/360）时，需要对年对月对日取期限
    var btt;
    var incomeClause = dict.get("incomeClause").val();
    if(incomeClause == 0){
        var startDateDay = date1.getDate();
        var endDateDay = date2.getDate();
        var startDateMonth = date1.getMonth();
        var endDateMonth = date2.getMonth();
        var startDateYear = date1.getFullYear();
        var endDateYear = date2.getFullYear();
        //起息日的天数D1    到期日的天数D2     判断日期天数大小
        if(startDateDay <= endDateDay){
            //D1<=D2   N=360*(y2-y1)+30*(m2-m1)+(d2-d1)
            btt = 360*(endDateYear-startDateYear) + 30*(endDateMonth-startDateMonth) + (endDateDay-startDateDay);
        }else{
            //D1>D2
            //判断D2是不是当月的最后一天
            var maxDay = new Date(endDateYear, endDateMonth + 1, 0).getDate();
            if(endDateDay === maxDay){
                //d2是m2最后一天时，N=360*(y2-y1)+30*(m2-m1)
                btt = 360*(endDateYear-startDateYear) + 30*(endDateMonth-startDateMonth);
            }else{
                //当月没有那个日期，特殊处理
                var lastDay = new Date(startDateYear, endDateMonth, 0).getDate();
                if(startDateDay>lastDay){
                    startDateDay = lastDay;
                }
                var newDate = new Date(endDateYear, endDateMonth-1, startDateDay);

                btt = 360*(endDateYear-startDateYear) + 30*(endDateMonth-startDateMonth-1) +
                    bt.date.getBetweenDay(newDate,date2);
            }
        }
    }else{
        btt = (date2 - date1)/(1000*60*60*24);
    }
    return btt;
}

/**
 * 计算参考收益
 */
function calculateInComme() {
    var days = getBetweenDay(dateType.get("interestStartDate").val(),dateType.get("deadlineDate").val());
    var buyMoney = moneyType.get("buyMoney").val();
    var referIncomeRate = rate.get("referIncomeRate").val();
    var incomeClause = dict.get("incomeClause").val();
    if(incomeClause == 0){
        incomeClause = 360;
    }
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
    if(incomeClause == 0){
        incomeClause = 360;
    }
    var value = buyMoney*referIncomeRate*days/incomeClause;
    moneyType.get("endReferIncomeMoney").val(referIncomeRate === '' ? '' : value);
}

closeTab = function(){
    bt.close();
}

// 理财类型回调函数
function financialTypeCallback(data) {
    var value = data.value;
    // selectTableObj.get("productCode").setUrl({financialType: value});
    var bankAcc = $('#bankAcc');
    var incomeType = dict.get('incomeType');

    // 隐藏是否维护日净值
    $('.isMaintainVal').attr('style', 'display:none')
    if (value === '1') { // 保本浮动型
        incomeType.attr({'data-required': false})
        rate.get('startContractRate').attr('lay-verify', 'none')
        $('.fType').attr('style', 'display:none')
        $('.isShowReferIncome').attr('style', 'display:true')
        $('.isShowDaily').attr('style', 'display:none')
        rate.get("referIncomeRate").attr('lay-verify', 'required')
        moneyType.get("dailyNetVal").attr('lay-verify', 'none')
    } else if (value === '0') { // 保本收益型
        incomeType.attr({'data-required': false})
        rate.get('startContractRate').attr('lay-verify', 'none')
        $('.fType').attr('style', 'display:true')
        $('.isShowReferIncome').attr('style', 'display:true')
        $('.isShowDaily').attr('style', 'display:none')
        rate.get("referIncomeRate").attr('lay-verify', 'required')
        moneyType.get("dailyNetVal").attr('lay-verify', 'none')
        $(".interestRateBasedOnTiers").addClass("layui-hide")
    } else if (value === '2') {  // 非保本浮动型
        incomeType.attr({'data-required': false})
        rate.get('startContractRate').attr('lay-verify', 'none')
        $('.fType').attr('style', 'display:none')
        $('.fType2').attr('style', 'display:true')
        $('.isShowReferIncome').attr('style', 'display:none')
        $('.isShowDaily').attr('style', 'display:true')
        rate.get("referIncomeRate").attr('lay-verify', 'none')
        moneyType.get("dailyNetVal").attr('lay-verify', 'required')
        dateType.get("deadlineDate").attr("lay-verify","");
        $(".interestRateBasedOnTiers").addClass("layui-hide")
        // 显示是否维护日净值
        $('.isMaintainVal').attr('style', 'display:true')
    }
    moneyType.get("dailyNetVal").val('')
    moneyType.get("portion").val('')
    bankAcc.val('');
    moneyType.get("referIncomeMoney").val(0)
    moneyType.get("endReferIncomeMoney").val(0);
    $('#bankAccId').val('');
    $("#bankName").val('');
    $("#bankInfoId").val('');
    // selectTableObj.get("productCode").val('')
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

function confirmRateInterest(){
    var startInterestDate = dateType.get("interestStartDate").val();
    if (!startInterestDate || startInterestDate === '') {
        parent.layer ? parent.layer.alert(bt.lang("fin.fcinterestdtl.intereststartdate.notnull"), {icon: 2}) : layer.alert(bt.lang("fin.fcinterestdtl.intereststartdate.notnull"), {icon: 2});
        return;
    }
    console.log($("#productId").val())
    parent.layer.open({
        type: 2,
        area: ['1050px', '530px'],
        maxmin: true,
        title: '利率明细', // 直接在这里设置标题
        content: basePath + "/chowmatistic/choProductRateDetail/rateDetail?productId="+$("#productId").val()+"&interestStartDate="+dateType.get("interestStartDate").val()+"&rateDetailIds="+$("#rateDetailIds").val(),
    });
}