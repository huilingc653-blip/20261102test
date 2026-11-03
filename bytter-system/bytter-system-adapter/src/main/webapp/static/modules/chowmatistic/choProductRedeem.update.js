bt.use(['form','choProduct','corp','dateType','rateType','moneyType','dicts','selectType'],function () {
    form = bt.form();
    bt.choProduct.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    dict = bt.dicts.init();
    selectType=bt.selectType.init();
    var dateFun = dateType.getFun();

    callbackProduct = function (data,dom) {
        initViewData(data);
    };
    //赎回日期回调方法
    redeemDateCallback = function(data){
        // 非保本浮动收益型
        if ($('#financialType').val() === '2') {
            var params = {
                "reqId":$('#reqId').val(),
                "netValDate":data
            }
            queryRedeemBal(params);
        } else {
            calIncomeMoney();
        }
        dateType.get("transferDate").attr("data-min", data);
        dateType.get("transferDate").val(data);
    };
    //赎回金额回调
    redeemMoneyCallback = function(data){
        if ($('#financialType').val() !== '2') {
            calIncomeMoney();
        }
    };
    //实际收益率回调方法
    bt.incomeRateCallback = function (data) {
        calIncomeMoney();
    };
    rateType = bt.rateType.init();
    rate = bt.rateType.getFun();

    incomeMoneyCallback = function () {
        if ($('#financialType').val() !== '2') {
            calIncomeRate();
        }
    };
    moneyType = bt.moneyType.init();
    moneyFun = moneyType.getFun();

    // 不维护日净值查询可赎回本金
    function queryRedeemInfo(id) {
        dateType.get("redeemDate").attr("data-max", "2099-12-31");
        dateType.get("transferDate").attr("data-max", "2099-12-31");
        $.ajax({
            url: basePath+'/chowmatistic/choproductredeem/queryRedeemInfo/'+id+"?_btv="+new Date(),
            method: 'GET',
            async:false,
            success: function (json) {
                var obj = JSON.parse(json);
                $("#redeemableMoney").text(moneyFun.format(obj.attributes.maxRedeemMoney));
                $("#redeemableMoneyVal").val(obj.attributes.maxRedeemMoney);
                moneyType.get("redeemPortion").attr("data-max",obj.attributes.maxRedeemPortion);
                moneyType.get("redeemMoney").attr("data-max", obj.attributes.maxRedeemMoney);

                form.render();
            }
        });
    }
    function queryRedeemBal(params) {
        var isFirst = params.netValDate == null
        $.ajax({
            url: basePath + '/chowmatistic/choproductNetVal/queryRedeemBal',
            type: 'post',
            async: false,
            contentType: "application/json",
            data: JSON.stringify(params),
            dataType: 'json',
            success: function (json) {
                if (json.attributes.leftMoney === undefined) {
                    return
                }
                var leftMoney = json.attributes.leftMoney
                var maxRedeemDate = json.attributes.maxRedeemDate;
                var redeemableMoney = json.attributes.redeemableMoney;
                moneyType.get("redeemMoney").attr("data-max", leftMoney);
                moneyType.get("redeemMoney").val(leftMoney);
                $("#leftMoney").text(moneyFun.format(leftMoney));
                $("#leftMoneyVal").val(leftMoney);
                $("#redeemableMoney").text(moneyFun.format(redeemableMoney));
                $("#redeemableMoneyVal").val(redeemableMoney);

                // 设置赎回日净值
                moneyType.get("redeemDailyNetVal").val(json.attributes.netVal)
                moneyType.get("redeemDailyNetVal").attr("disabled", "disabled")
                moneyType.get("redeemMoney").attr("data-max", redeemableMoney);
                moneyType.get("redeemMoney").val('');
                // 设置当日收益
                moneyType.get("incomeMoney").val(json.attributes.dayIncome);
                // 当日收益最大值
                $("#incomeMoneyMax").val(json.attributes.dayIncome);
                if (isFirst) {
                    var formatDate = dateFun.format(maxRedeemDate, 'yyyy-MM-dd');
                    dateType.get("redeemDate").attr("data-max", formatDate);
                    dateType.get("transferDate").attr("data-max", formatDate);
                    dateType.get("redeemDate").val(formatDate)
                    dateType.get("transferDate").val(formatDate)
                    moneyType.get("redeemPortion").attr("data-max", json.attributes.maxPortion)
                }
                dateType.render()
                moneyType.render()
                form.render();
            }
        });
    }
    
    function initViewData(data) {
        $("#corpId").val(data.corpId);
        $("#showName").text(data.corpName);
        selectType.get('bankTypeId').val(data.bankTypeId);
        dict.get('productType').val(data.productType);
        $("#productCode").val(data.productCode);
        $("#productName").text(data.productName);
        $("#curName").text(data.curName);
        $("#bankName").text(data.bankName);
        $("#bankAcc").text(data.bankAcc != null ? data.bankAcc : '');
        $("#buyMoney").text(moneyFun.format(data.buyMoney));
        $("#incomeClauseShow").text(bt.dict('choProduct.incomeClause',data.incomeClause));
        $("#buyDate").text(data.buyDate);
        if (data.dayLimit) {
            $("#dayLimitShow").text(data.dayLimit);
        }
        $("#interestStartDate").text(data.interestStartDate);
        if (data.deadlineDate) {
            $("#deadlineDate").text(data.deadlineDate);
        }
        $("#cashDate").text(data.cashDate === null ? '' : data.cashDate);
        if (data.referIncomeRate) {
            $("#referIncomeRate").text(rate.format(data.referIncomeRate, 'percent', 6));
        }
        if (data.referIncomeMoney) {
            $("#referIncomeMoney").text(moneyFun.format(data.referIncomeMoney));
            $("#referIncomeMoneyVal").val(data.referIncomeMoney);
        }
        $("#reqId").val(data.id);
        $("#incomeClause").val(data.incomeClause);
        $("#dayLimit").val(data.dayLimit);
        moneyType.get("portion").val('')
        moneyType.get("dailyNetVal").val('')
        $('.redeemableMoneyTd').attr('style', 'display:none')
        $('.redeemMoneyTd1').html('赎回金额(小写)')
        $('.redeemMoneyTd2').html('赎回金额(大写)')

        $('.showLeftMoney').attr('style', 'display:true')
        $('.showIsMaintainVal').attr('style', 'display:none')
        $('#isMaintainVal').text('')
        moneyType.get('redeemMoney').removeAttr('readonly')
        moneyType.get('incomeMoney').removeAttr('readonly')
        if (data.financialType === '1') {
            $('.fType').attr('style', 'display:none')
            $('.isShowReferIncome').attr('style', 'display:true')
            $('.isShowDaily').attr('style', 'display:none')
            $('.isShowRedeem').attr('style', 'display:none')
            rateType.get("incomeRate").attr('lay-verify', 'required')
            moneyType.get("portion").attr('lay-verify', 'none')
            moneyType.get("dailyNetVal").attr('lay-verify', 'none')
            moneyType.get("redeemDailyNetVal").attr('lay-verify', 'none')
            moneyType.get("redeemPortion").attr('lay-verify', 'none')
        } else if (data.financialType === '0') {
            $('.fType').attr('style', 'display:true')
            $('.isShowReferIncome').attr('style', 'display:true')
            $('.isShowDaily').attr('style', 'display:none')
            $('.isShowRedeem').attr('style', 'display:none')
            rateType.get("incomeRate").attr('lay-verify', 'required')
            moneyType.get("portion").attr('lay-verify', 'none')
            moneyType.get("dailyNetVal").attr('lay-verify', 'none')
            moneyType.get("redeemDailyNetVal").attr('lay-verify', 'none')
            moneyType.get("redeemPortion").attr('lay-verify', 'none')
        } else if (data.financialType === '2') {
            $('.isShowReferIncome').attr('style', 'display:none')
            $('.isShowDaily').attr('style', 'display:true')
            $('.fType').attr('style', 'display:none')
            $('.fType2').attr('style', 'display:true')
            $('.isShowRedeem').attr('style', 'display:true')
            moneyType.get("portion").val(data.portion)
            moneyType.get("dailyNetVal").val(data.dailyNetVal)
            rateType.get("incomeRate").attr('lay-verify', 'none')
            moneyType.get("portion").attr('lay-verify', 'required')
            moneyType.get("dailyNetVal").attr('lay-verify', 'required')
            moneyType.get("redeemDailyNetVal").attr('lay-verify', 'required')
            moneyType.get("redeemPortion").attr('lay-verify', 'required')
            moneyType.get("redeemPortion").attr('data-max', data.portion)

            $('.redeemMoneyTd1').html('赎回本金(小写)')
            $('.redeemMoneyTd2').html('赎回本金(大写)')
            $('.redeemableMoneyTd').attr('style', 'display:true')

            // 是否维护日净值
            $('.showIsMaintainVal').attr('style', 'display:true')
            $('#isMaintainVal').text(data.isMaintainVal === null ? '是' : bt.dict("sf_yn",data.isMaintainVal))
            if (data.isMaintainVal === '0') {
                // 可赎回金额
                $('.showLeftMoney').attr('style', 'display:none')
                moneyType.get("redeemDailyNetVal").removeAttr("disabled")
                moneyType.get("redeemDailyNetVal").val('')
                moneyType.get("redeemMoney").val('')
                moneyType.get('redeemMoney').attr('readonly','readonly')
                moneyType.get('incomeMoney').attr('readonly','readonly')
            }
        }
        $("#financialType").val(data.financialType);
        $("#buyMoneyVal").val(data.buyMoney);
        $("#financialTypeName").text(data.financialTypeName);
        $("#capitalSource").text(bt.dict("choProduct.capitalSource",data.capitalSource));
        $("#reqSubFeeMoney").text(moneyFun.format(data.subFeeMoney));
        $("#reqChargeMoney").text(moneyFun.format(data.chargeMoney));
        $("#isMoveUp").text(bt.dict("sf_yn",data.isMoveUp));
        $("#isStore").text(bt.dict("sf_yn",data.isStore));
        $("#payWay").text(bt.dict("financial.payWay",data.payWay));
        if (data.payWay === '2') {
            moneyType.get("redeemMoney").removeAttr("lay-verify")
        } else {
            moneyType.get("redeemMoney").attr("lay-verify","required")
        }
        moneyType.render()
        $("#incomeType").text(data.incomeType === null ? '' : bt.dict("financial.type",data.incomeType))
        $("#contractNo").text(data.contractNo === null ? '' : data.contractNo)

        $("#contractRate").text(data.endContractRate !== null ? (rate.format(data.startContractRate, 'percent', 6) + '~' + rate.format(data.endContractRate, 'percent', 6))
            : data.startContractRate !== null ? rate.format(data.startContractRate, 'percent', 6) : '');
        //根据产品是否提前赎回,设置赎回的最小时间;允许提前赎回时,最小值为起息日期;否则为到期日期
        if (1 == data.isMoveUp) {
            dateType.get("redeemDate").attr("data-min", data.interestStartDate);
            dateType.get("transferDate").attr("data-min", data.interestStartDate);
        }else{
            dateType.get("redeemDate").attr("data-min", data.deadlineDate);
            dateType.get("transferDate").attr("data-min", data.deadlineDate);
        }
        bt.rateType.get("incomeRate").val('')
        moneyType.get("incomeMoney").val('')
        dateType.render()
        $('#lastRedeemDate').text('')
        $('#lastRedeemDateShow').hide()
        if ($('#financialType').val() === '2') {
            if (data.isMaintainVal === '0') { // 不维护日净值信息
                queryRedeemInfo(data.id)
            } else { // 维护
                var params = {
                    "reqId":data.id
                }
                queryRedeemBal(params);
            }
        } else {
            dateType.get("redeemDate").attr("data-max", "2099-12-31");
            dateType.get("transferDate").attr("data-max", "2099-12-31");
            $.ajax({
                url: basePath+'/chowmatistic/choproductredeem/getLeftMoney/'+data.id+"?_btv="+new Date(),
                method: 'GET',
                async:false,
                success: function (json) {
                    var obj = JSON.parse(json);
                    moneyType.get("redeemMoney").attr("data-max",obj.attributes.leftMoney);
                    moneyType.get("redeemMoney").val(obj.attributes.leftMoney);
                    moneyType.render()
                    $("#leftMoney").text(moneyFun.format(obj.attributes.leftMoney));
                    $("#leftMoneyVal").val(obj.attributes.leftMoney);
                    if ($('#payWay').text() === '分段付息') {
                        var lastRedeemDate = obj.attributes.lastRedeemDate;
                        if (lastRedeemDate !== null
                            && lastRedeemDate !== undefined) {
                            $('#lastRedeemDate').text(lastRedeemDate)
                            var newDate = dateFun.format(new Date(new Date(lastRedeemDate).setDate(new Date(lastRedeemDate).getDate()+1)), 'yyyy-MM-dd');
                            var formatDate = dateFun.format(newDate, 'yyyy-MM-dd');
                            dateType.get("redeemDate").attr("data-min", formatDate);
                            dateType.get("transferDate").attr("data-min", formatDate);
                            dateType.get("redeemDate").val(formatDate)
                            dateType.get("transferDate").val(formatDate)
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
        }
    }

    form.on('submit(submit)',function (data) {
        var incomeMoney = Number(moneyType.get('incomeMoney').val());
        var incomeMoneyMax = Number($('#incomeMoneyMax').val());
        var action = '/chowmatistic/choproductredeem/modify';
        data.field.status = $(this).attr("value");
        if ($('#financialType').val() === '2' && $('#isMaintainVal').text() === '是' && incomeMoney > incomeMoneyMax  ) {
            parent.layer.confirm('你的收益金额超出当前赎回日期的日净值最大收益，若继续提交，系统将自动修改当日的日净值数据', function () {
                bt.loading.show();

                $.post(basePath + action,data.field,function (result) {
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                    if (result.success){
                        if (bt.navtab){
                            bt.navtab.refreshParent();
                            bt.navtab.closeCurrent();
                            // bt.navtab.refreshCurrent();
                        } else{
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                },'JSON');
                bt.loading.hide();
            })
        } else {
            bt.loading.show();
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
        }
        return false;
    })
    form.renderIcon();

    function calIncomeMoney() {
        var _redeemMoney = moneyType.get("redeemMoney").val();
        var _incomeRate = bt.rateType.get("incomeRate").val();
        var _dayLimit = getBetweenDay($("#interestStartDate").text(),dateType.get("redeemDate").val());//赎回日期-起息日期
        var _incomeClause = $("#incomeClause").val();
        var _incomeMoney = moneyFun.format(_redeemMoney*_incomeRate*_dayLimit/_incomeClause);
        if ($('#payWay').text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate').text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate").val());
            }
            _incomeMoney = moneyFun.format($('#leftMoneyVal').val() * _incomeRate * _dayLimit / _incomeClause)
        }
        if (_incomeMoney === 0 || _incomeMoney === '0.00') {
            bt.rateType.get("incomeRate").val(0);
        }
        moneyType.get("incomeMoney").val(_incomeMoney);
    }

    function calIncomeRate() {
        var _redeemMoney = moneyType.get("redeemMoney").val();
        var _incomeMoney = bt.moneyType.get("incomeMoney").val();
        var _dayLimit = getBetweenDay($("#interestStartDate").text(),dateType.get("redeemDate").val());//赎回日期-起息日期
        var _incomeClause = $("#incomeClause").val();
        var number = _redeemMoney*_dayLimit;
        var _incomeRate = number === 0 ? 0 : _incomeMoney/(number/_incomeClause);
        if ($('#payWay').text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate').text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate").val());
            }
            _incomeRate = _dayLimit === 0 ? 0 : (_incomeMoney / $('#leftMoneyVal').val()) / (_dayLimit / _incomeClause)
        }
        if (_incomeRate === 0) {
            moneyType.get("incomeMoney").val(0);
        }
        _incomeRate=_incomeRate.toFixed(8);
        bt.rateType.get("incomeRate").val(_incomeRate);
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

    if ($("#financialType").val() !== '2') {
        $.ajax({
            url: basePath+'/chowmatistic/choproductredeem/getLeftMoney/'+$("#reqId").val(),
            method: 'GET',
            async:false,
            success: function (json) {
                var obj = JSON.parse(json);
                moneyType.get("redeemMoney").attr("data-max",obj.attributes.leftMoney);
                moneyType.render()
                $("#leftMoney").text(moneyFun.format(obj.attributes.leftMoney));
                $("#leftMoneyVal").val(obj.attributes.leftMoney);
                form.render();
            }
        });
    }
});



// 赎回日净值回调方法
function redeemDailyNetValCallback(data) {
    calRedeemInfo()
}

// 赎回份额回调方法
function redeemPortionCallback(data) {
    calRedeemInfo()
}

// 计算赎回本金、收益金额
function calRedeemInfo() {
    if ($('#isMaintainVal').text() === '是') {
        return
    }
    var redeemPortion = Number(moneyType.get('redeemPortion').val());
    // 赎回总金额 = 赎回日净值 * 赎回份额
    var redeemTotalMoney = Number(moneyType.get('redeemDailyNetVal').val()) * redeemPortion
    // 收益金额 = 赎回日净值 * 赎回份额 - 购买日净值 * 赎回份额
    var incomeMoney = redeemTotalMoney - Number(moneyType.get('dailyNetVal').val()) * redeemPortion
    moneyType.get('incomeMoney').val(incomeMoney)
    // 赎回本金 = 赎回总金额 - 收益金额（收益为负数则等于赎回总金额）
    var redeemMoney = incomeMoney <= 0 ? redeemTotalMoney : redeemTotalMoney - incomeMoney
    moneyType.get('redeemMoney').val(redeemMoney)
}