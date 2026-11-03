bt.use(['form','choProductRenewal','corp','dateType','rateType','moneyType','dicts','selectType'],function () {
    form = bt.form();
    bt.choProductRenewal.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    var dateFun = dateType.getFun();
    dict = bt.dicts.init();
    rate = bt.rateType.getFun();
    rateType = bt.rateType.init();
    selectType=bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun = moneyType.getFun();


    callbackProduct = function (data,dom) {
        initViewData(data);
    };
    //赎回日期回调方法
    redeemDateCallback = function(data){
        calIncomeMoney();
        dateType.get("transferDate").attr("data-min", data);
        dateType.get("transferDate").val(data);
    };
    //赎回金额回调
    redeemMoneyCallback = function(data){
        calIncomeMoney();
    };
    //实际收益率回调方法
    bt.incomeRateCallback = function (data) {
        calIncomeMoney();
    };

    incomeMoneyCallback = function () {
        calIncomeRate();
    };

    function initViewData(data) {
        selectType.get('bankTypeId').val(data.bankTypeId);
        dict.get('productType').val(data.productType);
        $("#moveUpRmk").text(data.moveUpRmk != null ? data.moveUpRmk : '');
        $("#rmk").text(data.rmk != null ? data.rmk : '');
        $("#redeemId").val(data.redeemId);
        $("#productCode").val(data.productCode);
        $("#productName").text(data.productName);
        $("#curName").text(data.curName);
        $("#bankName").text(data.bankName);
        $("#bankAcc").text(data.bankAcc != null ? data.bankAcc : '');
        $("#corpId").val(data.corpId);
        $("#showName").text(data.corpName);
        $("#buyMoney").text(moneyFun.format(data.buyMoney));
        $("#incomeClauseShow").text(bt.dict('choProduct.incomeClause',data.incomeClause));
        $("#buyDate").text(dateFun.format(data.buyDate, 'yyyy-MM-dd'));
        if (data.dayLimit) {
            $("#dayLimitShow").text(data.dayLimit);
            $('#dayLimit').val(data.dayLimit)
        }
        $("#interestStartDate").text(dateFun.format(data.interestStartDate, 'yyyy-MM-dd'));
        dateType.get('interestStartDate').val(data.interestStartDate)
        dateType.get("deadlineDate").attr("data-min", dateFun.format(data.interestStartDate, 'yyyy-MM-dd'));
        dateType.get('deadlineDate').val(data.deadlineDate)
        if (data.cashDate) {
            $("#cashDate").text(dateFun.format(data.cashDate, 'yyyy-MM-dd'));
        }
        $("#referIncomeRate").text(rate.format(data.referIncomeRate, 'percent', 6));
        $('#referIncomeRateVal').val(data.referIncomeRate)
        if (data.referIncomeMoney) {
            $("#referIncomeMoney").text(moneyFun.format(data.referIncomeMoney));
            $("#referIncomeMoneyVal").val(data.referIncomeMoney);
        }
        $("#reqId").val(data.id);
        $("#incomeClause").val(data.incomeClause);
        $("#dayLimit").val(data.dayLimit);
        if (data.financialType === '1') {
            $('.fType').attr('style', 'display:none')
        } else {
            $('.fType').attr('style', 'display:true')
        }
        $("#buyMoneyVal").val(data.buyMoney);
        $("#financialTypeName").text(data.financialTypeName);
        $("#capitalSource").text(bt.dict("choProduct.capitalSource",data.capitalSource));
        // moneyType.get("subFeeMoney").val(moneyFun.format(data.subFeeMoney));
        // moneyType.get("redeemChargeMoney").val(moneyFun.format(data.redeemChargeMoney));
        // moneyType.get("chargeMoney").val(moneyFun.format(data.chargeMoney));
        $("#reqSubFeeMoney").text(moneyFun.format(data.reqSubFeeMoney));
        $("#reqChargeMoney").text(moneyFun.format(data.reqChargeMoney));
        $("#isMoveUp").text(bt.dict("sf_yn",data.isMoveUp));
        $("#isStore").text(bt.dict("sf_yn",data.isStore));
        $("#payWay").text(bt.dict("financial.payWay",data.payWay));

        moneyType.render()
        $("#incomeType").text(data.incomeType === null ? '' : bt.dict("financial.type",data.incomeType))
        $("#contractNo").text(data.contractNo === null ? '' : data.contractNo)

        $("#contractRate").text(data.endContractRate !== null ? (rate.format(data.startContractRate, 'percent', 6) + '~' + rate.format(data.endContractRate, 'percent', 6))
            : data.startContractRate !== null ? rate.format(data.startContractRate, 'percent', 6) : '');
        //根据产品是否提前赎回,设置赎回的最小时间;允许提前赎回时,最小值为起息日期;否则为到期日期
        if (1 == data.isMoveUp) {
            dateType.get("redeemDate").attr("data-min", dateFun.format(data.interestStartDate, 'yyyy-MM-dd'));
            dateType.get("transferDate").attr("data-min", dateFun.format(data.interestStartDate, 'yyyy-MM-dd'));
        }else{
            dateType.get("redeemDate").attr("data-min", dateFun.format(data.deadlineDate, 'yyyy-MM-dd'));
            dateType.get("transferDate").attr("data-min", dateFun.format(data.deadlineDate, 'yyyy-MM-dd'));
        }
        bt.rateType.get("incomeRate").val('')
        moneyType.get("incomeMoney").val('')
        $.ajax({
            url: basePath+'/chowmatistic/choproductredeem/getLeftMoney/'+data.id+"?_btv="+new Date(),
            method: 'GET',
            async:false,
            success: function (json) {
                var obj = JSON.parse(json);
                moneyType.get("redeemMoney").val(obj.attributes.leftMoney);
                moneyType.get("redeemMoney").attr("data-max",obj.attributes.leftMoney);
                $("#leftMoney").text(moneyFun.format(obj.attributes.leftMoney));
                if ($('#payWay').text() === '分段付息') {
                    var lastRedeemDate = obj.attributes.lastRedeemDate;
                    if (lastRedeemDate !== null
                        && lastRedeemDate !== undefined) {
                        $('#lastRedeemDate').text(lastRedeemDate)
                        var newDate = dateFun.format(new Date(new Date(lastRedeemDate).setDate(new Date(lastRedeemDate).getDate()+1)), 'yyyy-MM-dd');
                        dateType.get("redeemDate").val(newDate)
                        dateType.get("transferDate").val(newDate)
                        dateType.get("redeemDate").attr("data-min", dateFun.format(newDate, 'yyyy-MM-dd'));
                        dateType.get("transferDate").attr("data-min", dateFun.format(newDate, 'yyyy-MM-dd'));
                        $('#lastRedeemDateShow').show()
                    } else {
                        $('#lastRedeemDate').text('')
                        $('#lastRedeemDateShow').hide()
                    }
                    dateType.render()
                } else {
                    $('#lastRedeemDateShow').hide()
                }
                form.render();
            }
        });
        dateType.render()
    }

    form.on('submit(submit)',function (data) {
        bt.loading.show();
        var action = '/chowmatistic/choproductRenewal/save';
        data.field.status = $(this).attr("value");
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
    form.renderIcon();

    function calIncomeMoney() {
        var _redeemMoney = moneyType.get("redeemMoney").val();
        var _incomeRate = bt.rateType.get("incomeRate").val();
        // var _dayLimit = $("#dayLimit").val();
        var _dayLimit = getBetweenDay($("#interestStartDate").text(),dateType.get("redeemDate").val());//赎回日期-起息日期
        var _incomeClause = $("#incomeClause").val();
        var _incomeMoney = moneyFun.format(_redeemMoney*_incomeRate*_dayLimit/_incomeClause);
        if ($('#payWay').text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate').text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate").val());
            }
            _incomeMoney = moneyFun.format($('#buyMoneyVal').val() * _incomeRate * _dayLimit / _incomeClause)
        }
        moneyType.get("incomeMoney").val(_incomeMoney);
    }

    function calIncomeRate() {
        var _redeemMoney = moneyType.get("redeemMoney").val();
        var _incomeMoney = bt.moneyType.get("incomeMoney").val();
        // var _dayLimit = $("#dayLimit").val();
        var _dayLimit = getBetweenDay($("#interestStartDate").text(),dateType.get("redeemDate").val());//赎回日期-起息日期
        var _incomeClause = $("#incomeClause").val();
        var _incomeRate = _incomeMoney/(_redeemMoney*_dayLimit/_incomeClause);
        if ($('#payWay').text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate').text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate").val());
            }
            _incomeRate = (_incomeMoney / $('#buyMoneyVal').val()) / (_dayLimit / _incomeClause)
        }
        _incomeRate=_incomeRate.toFixed(8);
        bt.rateType.get("incomeRate").val(_incomeRate);
    }

    closeTab = function(){
        bt.close();
    }

    bt.close = function(){
        if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
            bt.navtab.closeCurrent();
        }
    }
});

//到期日期的选择回调
function deadlineDateCallback (data) {
    var interestStartDate = dateType.get("interestStartDate").val();
    var days = getBetweenDay(interestStartDate,data);
    $("#dayLimit").val(days);
    $("#dayLimitShow").text(days);
    // $("input[name='dayLimit']").val(days);
    calculateInComme();
}

/**
 * 计算参考收益
 */
function calculateInComme() {
    var days = getBetweenDay(dateType.get("interestStartDate").val(),dateType.get("deadlineDate").val());
    var buyMoney = $("#buyMoneyVal").val();
    var referIncomeRate = $("#referIncomeRateVal").val();
    var incomeClause = $("#incomeClause").val();
    var value = buyMoney*referIncomeRate*days/incomeClause;
    if (value != null && value != ""){
       $("#referIncomeMoney").text(moneyFun.format(value));
       $("#referIncomeMoneyVal").val(value);
    }
}

/**
 * 计算两个日期之间的间隔
 * @param startDate
 * @param endDate
 * @returns {number}
 */
function getBetweenDay(startDate,endDate){
    if (null == startDate || null == endDate || '' == startDate || '' == endDate){
        return;
    }
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);
    var date3 = bt.date.get($("#deadlineDate").text());
    //当选择的赎回日期大于产品的到期日期时,按产品的到期日期计算收益
    if (date3) {
        if (date2 > date3) {
            date2 = date3;
        }
    }
    var btt = date2 - date1;
    return btt/(1000*60*60*24);
}