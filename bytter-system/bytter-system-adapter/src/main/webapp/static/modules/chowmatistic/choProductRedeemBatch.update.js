var corp,dateType,dict,selectType,index,moneyType,count;
bt.use(['form','choProduct','corp','dateType','rateType','moneyType','dicts','selectType','bisAccDtl'],function () {
    form = bt.form();
    bt.choProduct.init();
    corp = bt.corp.init();
    dateType = bt.dateType.init();
    dict = bt.dicts.init();
    selectType=bt.selectType.init();
    var dateFun = dateType.getFun();

    var bisAccDtl = bt.bisAccDtl.init();
    bt.bisAccDtlBeforeClick = function (data) {
        var id = layui.jquery(data).data("biz-name");
        index = id.substr(id.lastIndexOf("_") + 1);
        var bankAcc = $("#bankAcc").text();
        var corpId = $("#corpId").val();
        var redeemMoney = moneyType.get("redeemMoney_"+index).val();
        var redeemDate = dateType.get("redeemDate_"+index).val();
        bisAccDtl.set({
            "bankAcc": bankAcc,
            "corpId": corpId,
            "redeemMoney": redeemMoney,
            "redeemDate": redeemDate
        });
        return true;
    }

    $("#batchRedeemHead").on('click','.icon-1',function () {
        $('.table-tips-box').css('display','block');
    })
    $("#batchRedeemHead").on('click','.table-tips-box button',function () {
        $('.table-tips-box').css('display','none');
        var type = $(this).attr('type');
        if(type === 'button'){
            $("#reset").attr('class', 'layui-btn layui-btn-primary');
            $("#button").attr('class', 'layui-btn');
            var incomeMoney = $("input[name='incomeMoney']");
            var length = incomeMoney.length;
            for(var i=0;i<length;i++){
                moneyType.get('incomeMoney_'+i).attr('data-callback', 'incomeMoneyCallback');
                rateType.get('incomeRate_'+i).attr('data-callback', 'bt.incomeRateCallback');
            }
        }else{
            $("#reset").attr('class', 'layui-btn');
            $("#button").attr('class', 'layui-btn layui-btn-primary');
            var incomeMoney = $("input[name='incomeMoney']");
            var length = incomeMoney.length;
            for(var i=0;i<length;i++){
                moneyType.get('incomeMoney_'+i).attr('data-callback', '');
                rateType.get('incomeRate_'+i).attr('data-callback', '');
            }
        }
        return false
    })

    var timerId = null;
    $("#batchRedeemHead").hover(function () {
        if(timerId){
            clearTimeout(timerId);
        }
    },function () {
        timerId = setTimeout(function () {
            $('.table-tips-box').css('display','none');
        },1000)
    })

    callbackProduct = function (data,dom) {
        initViewData(data);
    };
    //赎回日期回调方法
    redeemDateCallback = function(data,elem){
        var id = elem.data("biz-name");
        var i = id.substr(id.lastIndexOf("_") + 1);
        // 非保本浮动收益型
        if ($('#financialType').val() === '2') {
            var params = {
                "reqId":$('#reqId_'+i).val(),
                "netValDate":data
            }
            queryRedeemBal(params,i);
        } else {
            calIncomeMoney(i);
        }
        dateType.get("transferDate_"+i).attr("data-min", data);
        dateType.get("transferDate_"+i).val(data);
    };
    //赎回金额回调
    redeemMoneyCallback = function(data,elem){
        if ($('#financialType').val() !== '2') {
            var id = elem.data("biz-name");
            var i = id.substr(id.lastIndexOf("_") + 1);
            calIncomeMoney(i);
        }
        //统计合计
        var redeemMoneySum = 0;
        var redeemMoney = $("input[name='redeemMoney']");
        var length = redeemMoney.length;
        for(var j=0;j<length;j++){
            var redeemMoney = Number(moneyType.get('redeemMoney_'+j).val());
            redeemMoneySum = redeemMoneySum+redeemMoney;
        }
        $('#redeemMoneySum').text(moneyFun.format(redeemMoneySum));
    };
    //实际收益率回调方法
    bt.incomeRateCallback = function (data,elem) {
        var id = elem.data("biz-name");
        var i = id.substr(id.lastIndexOf("_") + 1);
        calIncomeMoney(i);
    };
    rateType = bt.rateType.init();
    rate = bt.rateType.getFun();

    incomeMoneyCallback = function (data,elem) {
        if ($('#financialType').val() !== '2') {
            var id = elem.data("biz-name");
            var i = id.substr(id.lastIndexOf("_") + 1);
            calIncomeRate(i);
        }
        //统计合计
        var incomeMoneySum = 0;
        var incomeMoney = $("input[name='incomeMoney']");
        var length = incomeMoney.length;
        for(var j=0;j<length;j++){
            var incomeMoney = Number(moneyType.get('incomeMoney_'+j).val());
            incomeMoneySum = incomeMoneySum+incomeMoney;
        }
        $('#incomeMoneySum').text(moneyFun.format(incomeMoneySum));
    };

    //赎回申购费 回调
    subFeeMoneyCallback = function(){
        //统计合计
        var subFeeMoneySum = 0;
        var subFeeMoney = $("input[name='subFeeMoney']");
        var length = subFeeMoney.length;
        for(var j=0;j<length;j++){
            var subFeeMoney = Number(moneyType.get('subFeeMoney_'+j).val());
            subFeeMoneySum = subFeeMoneySum+subFeeMoney;
        }
        $('#subFeeMoneySum').text(moneyFun.format(subFeeMoneySum));
    };

    //赎回费回调
    redeemChargeMoneyCallback = function(){
        //统计合计
        var redeemChargeMoneySum = 0;
        var redeemChargeMoney = $("input[name='redeemChargeMoney']");
        var length = redeemChargeMoney.length;
        for(var j=0;j<length;j++){
            var redeemChargeMoney = Number(moneyType.get('redeemChargeMoney_'+j).val());
            redeemChargeMoneySum = redeemChargeMoneySum+redeemChargeMoney;
        }
        $('#redeemChargeMoneySum').text(moneyFun.format(redeemChargeMoneySum));
    };

    //赎回其他手续费 回调
    chargeMoneyCallback = function(){
        //统计合计
        var chargeMoneySum = 0;
        var chargeMoney = $("input[name='chargeMoney']");
        var length = chargeMoney.length;
        for(var j=0;j<length;j++){
            var chargeMoney = Number(moneyType.get('chargeMoney_'+j).val());
            chargeMoneySum = chargeMoneySum+chargeMoney;
        }
        $('#chargeMoneySum').text(moneyFun.format(chargeMoneySum));
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
                $('#maxRedeemPortion').val(obj.attributes.maxRedeemPortion)
                form.render();
            }
        });
    }

    function queryRedeemBal(params,i) {
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
                $("#leftMoney_"+i).text(moneyFun.format(leftMoney));
                $("#leftMoneyVal_"+i).val(leftMoney);
                $("#redeemableMoney_"+i).text(moneyFun.format(redeemableMoney));
                $("#redeemableMoneyVal_"+i).val(redeemableMoney);

                // 设置赎回日净值
                moneyType.get("redeemDailyNetVal_"+i).val(json.attributes.netVal)
                moneyType.get("redeemDailyNetVal_"+i).attr("disabled", "disabled")
                moneyType.get("redeemMoney_"+i).attr("data-max", redeemableMoney);
                moneyType.get("redeemMoney_"+i).val('');
                // 设置当日收益
                moneyType.get("incomeMoney_"+i).val(json.attributes.dayIncome);
                // 当日收益最大值
                $("#incomeMoneyMax").val(json.attributes.dayIncome);
                if (isFirst) {
                    var formatDate = dateFun.format(maxRedeemDate, 'yyyy-MM-dd');
                    dateType.get("redeemDate_"+i).attr("data-max", formatDate);
                    dateType.get("transferDate_"+i).attr("data-max", formatDate);
                    dateType.get("redeemDate_"+i).val(formatDate)
                    dateType.get("transferDate_"+i).val(formatDate)
                    moneyType.get("redeemPortion_"+i).attr("data-max", json.attributes.maxPortion)
                }
                dateType.render()
                moneyType.render()
                form.render();
            }
        });
    }

    function initViewData(data) {
        count = data.length;
        //先删除表头和所有的行。
        $("#batchRedeem").find("tr").remove();
        //0:保本收益型,1:保本浮动收益型,2:非保本浮动收益型
        var financialType = "";
        var payWay = "";
        if(data[0]) {
            selectType.get('bankTypeId').val(data[0].bankTypeId);
            dict.get('productType').val(data[0].productType);
            $("#productCode").val(data[0].productCode);
            $("#productName").text(data[0].productName);
            $("#curName").text(data[0].curName);
            $("#bankName").text(data[0].bankName);
            $("#bankNameHid").val(data[0].bankName);
            $("#bankAcc").text(data[0].bankAcc != null ? data[0].bankAcc : '');
            $("#bankAccHid").val(data[0].bankAcc != null ? data[0].bankAcc : '');
            $("#corpId").val(data[0].corpId);
            $("#showName").text(data[0].corpName);
            $("#financialTypeName").text(data[0].financialTypeName);
            $("#financialType").val(data[0].financialType);
            financialType = data[0].financialType;
            payWay = data[0].payWay;
            //是否显示是否续存
            /*if(financialType === "0"){
                $('.fType').attr('style', 'display:true');
                var isStore = data[0].isStore;
                dict.get('isStore').val(isStore);
                if(isStore === "1"){
                    $("#certificateWay").val(data[0].certificateWay);
                }
            }else{
                $('.fType').attr('style', 'display:none');
                dict.get("isStore").attr({
                    'data-value':''
                });
            }*/
        }
        var head = "";
        //保本收益型 表头
        if(financialType === "0"){
            var redeemMoneyRequired = "<span style=\"color: red\">*</span>";
            if(payWay === "2"){
                redeemMoneyRequired = "";
            }
            //显示保本收益型列
            head = '<tr align="center">\n' +
                '                <th><div style="width: 100px;">'+bt.lang("common.table.thead.operation")+'</div></th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.redeemDate")+'</th>\n' +
                '                <th>'+redeemMoneyRequired+bt.lang("table.choProductRedeem.redeemMoney")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.transferDate")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeMoney")+'&nbsp;<span class="icon-1" style="">\n' +
                '                    <span>!</span>\n' +
                '                    <div class="table-tips-box">\n' +
                '                        <i class="layui-icon">&#xe625;</i>\n' +
                '                        <div>\n' +
                '                            <span class="icon-2">!</span>\n' +
                '                            <span class="tips-text">关闭利率计算器</span>\n' +
                '                        </div>\n' +
                '                        <div>\n' +
                '                            <button id="reset" type="reset" class="layui-btn layui-btn-primary">关闭</button>\n' +
                '                            <button id="button" type="button" class="layui-btn">开启</button>\n' +
                '                        </div>\n' +
                '                    </div>\n' +
                '                </span></th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.rmk")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.redeemChargeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.lastRedeemDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.incomeClause")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.interestStartDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.deadlineDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.dayLimit")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.cashDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyMoney")+'</th>\n' +
                '                <th>'+bt.lang("cho.product.redeem.leftMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.referIncomeRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.referIncomeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.isMoveUp")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.isStore")+'</th>\n' +
                '                <th>'+bt.lang("table.cmsProvideLoanInfo.cplInterestPayment")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.incomeType")+'</th>\n' +
                '                <th>'+bt.lang("table.ratUrationApply.contactNo")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.contractRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.capitalSource")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoney")+'</th>\n' +
                '            </tr>';
        }
        //保本浮动收益型 表头
        if(financialType === "1"){
            //显示保本收益型列
            head = '<tr align="center">\n' +
                '                <th><div style="width: 100px;">'+bt.lang("common.table.thead.operation")+'</div></th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.redeemDate")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.redeemMoney")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.transferDate")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeMoney")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.rmk")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.redeemChargeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.lastRedeemDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.incomeClause")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.interestStartDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.deadlineDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.dayLimit")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.cashDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyMoney")+'</th>\n' +
                '                <th>'+bt.lang("cho.product.redeem.leftMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.referIncomeRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.referIncomeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.isMoveUp")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.capitalSource")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoney")+'</th>\n' +
                '            </tr>';
        }
        //非保本浮动收益型 表头
        if(financialType === "2"){
            head = '<tr align="center">\n' +
                '                <th><div style="width: 100px;">'+bt.lang("common.table.thead.operation")+'</div></th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductReq.redeemDailyNetVal")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductReq.redeemPortion")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.redeemDate")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("cho.product.redeem.money")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.transferDate")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeMoney")+'</th>\n' +
                '                <th><span style="color: red">*</span>'+bt.lang("table.choProductRedeem.incomeRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.rmk")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.redeemChargeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoneyRedeem")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductRedeem.lastRedeemDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.incomeClause")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.interestStartDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.deadlineDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.dayLimit")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.cashDate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.buyMoney")+'</th>\n' +
                '                <th>'+bt.lang("cho.product.redeem.leftMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.isMoveUp")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.isStore")+'</th>\n' +
                '                <th>'+bt.lang("table.cmsProvideLoanInfo.cplInterestPayment")+'</th>\n' +
                '                <th>'+bt.lang("table.choProduct.incomeType")+'</th>\n' +
                '                <th>'+bt.lang("table.ratUrationApply.contactNo")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.contractRate")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.capitalSource")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.subFeeMoney")+'</th>\n' +
                '                <th>'+bt.lang("table.choProductReq.chargeMoney")+'</th>\n' +
                '            </tr>';
        }
        $("#batchRedeemHead").append(head);
        var redeemMoneySum =0;
        for(var i=0;i<data.length;i++){
            //根据产品是否提前赎回,设置赎回的最小时间;允许提前赎回时,最小值为起息日期;否则为到期日期
            var isMoveUp = data[i].isMoveUp;
            var minDate;
            if(isMoveUp == 1){
                minDate = data[i].interestStartDate;
            }else{
                minDate = data[i].deadlineDate;
            }
            var maxDate = data[i].deadlineDate;
            var deadlineDate,cashDate,referIncomeRate,referIncomeMoney,dayLimit,leftMoney,contractRate,contractNo,incomeType;
            if (data[i].deadlineDate) {
                deadlineDate = dateFun.format(data[i].deadlineDate, 'yyyy-MM-dd');
            }else{
                deadlineDate= "";
            }
            if (data[i].cashDate) {
                cashDate = dateFun.format(data[i].cashDate, 'yyyy-MM-dd');
            }else{
                cashDate = "";
            }
            if (data[i].referIncomeRate) {
                referIncomeRate= rate.format(data[i].referIncomeRate, 'percent', 6);
            }else{
                referIncomeRate = "";
            }
            if (data[i].referIncomeMoney) {
                referIncomeMoney = data[i].referIncomeMoney;
            }else{
                referIncomeMoney = "";
            }
            if (data[i].dayLimit) {
                dayLimit = data[i].dayLimit;
            }else{
                dayLimit = "";
            }
            contractRate = data[i].endContractRate !== null ? (rate.format(data[i].startContractRate, 'percent', 6) + '~' + rate.format(data[i].endContractRate, 'percent', 6))
                : data[i].startContractRate !== null ? rate.format(data[i].startContractRate, 'percent', 6) : '';
            contractNo = data[i].contractNo === null ? '' : data[i].contractNo;
            incomeType = data[i].incomeType === null ? '' : bt.dict("financial.type",data[i].incomeType);
            var redeemMoney;
            var lastRedeemDate;
            $.ajax({
                url: basePath+'/chowmatistic/choproductredeem/getLeftMoney/'+data[i].id+"?_btv="+new Date(),
                method: 'GET',
                async:false,
                success: function (json) {
                    var obj = JSON.parse(json);
                    redeemMoney = obj.attributes.leftMoney;
                    leftMoney = moneyFun.format(obj.attributes.leftMoney);
                    //付息方式 为2 分段付息
                    lastRedeemDate = "";
                    if (data[i].payWay === '2') {
                        lastRedeemDate = obj.attributes.lastRedeemDate;
                        if (lastRedeemDate !== null
                            && lastRedeemDate !== undefined) {
                            $('#lastRedeemDate_'+i).text(lastRedeemDate)
                            var newDate = dateFun.format(new Date(new Date(lastRedeemDate).setDate(new Date(lastRedeemDate).getDate()+1)), 'yyyy-MM-dd');
                            var formatDate = dateFun.format(newDate, 'yyyy-MM-dd');
                            minDate = formatDate;
                            lastRedeemDate = formatDate;
                        } else {
                            lastRedeemDate = "";
                        }
                        dateType.render()
                    }
                    form.render();
                }
            });
            redeemMoneySum = redeemMoneySum + redeemMoney;
            //保本收益型
            if(financialType === "0"){
                var html = '        <tr id="'+i+'">\n' +
                    '            <td align="center">\n' +
                    '                <a href="#" onclick="deleteDtl(this)">'+bt.lang("common.button.delete")+'</a><input  name="reqId" type="hidden" value="'+data[i].id+'">' +
                    '<input id="dayLimit_'+i+'" name="dayLimit" type="hidden" value="'+data[i].dayLimit+'"> ' +
                    '<input id="incomeClause_'+i+'" name="incomeClause" type="hidden" value="'+data[i].incomeClause+'">' +
                    '<input id="leftMoneyVal_'+i+'" name="incomeClause" type="hidden" value="'+redeemMoney+'">' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="redeemDate_'+i+'" name="redeemDate" data-name="redeemDate" data-biz-name="redeemDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType"  data-min="'+minDate+'" data-max="'+maxDate+'" data-type="date" data-callback="redeemDateCallback">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="redeemMoney_'+i+'" name="redeemMoney" style="text-align:right"  data-name="redeemMoney" data-biz-name="redeemMoney_'+i+'" data-callback="redeemMoneyCallback" \n' +
                    '                       autocomplete="off" class="layui-input bt-biz-moneyType" data-max="'+redeemMoney+'" value="'+redeemMoney+'" placeholder="'+bt.lang("table.choProductRedeem.redeemMoney")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="transferDate_'+i+'" name="transferDate" data-name="transferDate" data-biz-name="transferDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType" data-today="true" data-min="'+minDate+'" data-type="date">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="incomeMoney_'+i+'" name="incomeMoney" style="text-align: right;"  data-name="incomeMoney"\n' +
                    '                       data-decimal="2" data-minus="true"\n' +
                    '                       data-biz-name="incomeMoney_'+i+'" autocomplete="off" class="layui-input bt-biz-moneyType" data-callback="incomeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" data-name="incomeRate"  id="incomeRate_'+i+'" data-biz-name="incomeRate_'+i+'" class="layui-input bt-biz-rateType" data-decimal="6"\n' +
                    '                       placeholder="'+bt.lang("table.choProductRedeem.incomeRate")+'" data-callback="bt.incomeRateCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text"  name="rmk" id="rmk'+i+'" class="layui-input"\n' +
                    '                       placeholder="'+bt.lang("table.choProduct.rmk")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="subFeeMoney" id="subFeeMoney_'+i+'" data-biz-name="subFeeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="subFeeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="redeemChargeMoney" id="redeemChargeMoney_'+i+'" data-biz-name="redeemChargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="redeemChargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="chargeMoney"  id="chargeMoney_'+i+'" data-biz-name="chargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="chargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="lastRedeemDate_'+i+'" style="color: rgb(184,184,184)">'+lastRedeemDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="incomeClauseShow_'+i+'">'+bt.dict('choProduct.incomeClause',data[i].incomeClause)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyDate_'+i+'">'+dateFun.format(data[i].buyDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="interestStartDate_'+i+'">'+dateFun.format(data[i].interestStartDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="deadlineDate_'+i+'">'+deadlineDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="dayLimitShow_'+i+'">'+dayLimit+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="cashDate_'+i+'">'+cashDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyMoney_'+i+'">'+moneyFun.format(data[i].buyMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="leftMoney_'+i+'">'+leftMoney+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="referIncomeRate_'+i+'">'+referIncomeRate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="referIncomeMoney_'+i+'">'+referIncomeMoney+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="isMoveUp_'+i+'">'+bt.dict("sf_yn",data[i].isMoveUp)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="isStore_'+i+'">'+bt.dict("sf_yn",data[i].isStore)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="payWay_'+i+'">'+bt.dict("financial.payWay",data[i].payWay)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="incomeType_'+i+'">'+incomeType+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="contractNo_'+i+'">'+contractNo+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="contractRate_'+i+'"></label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="capitalSource_'+i+'">'+bt.dict("choProduct.capitalSource",data[i].capitalSource)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqSubFeeMoney_'+i+'">'+moneyFun.format(data[i].subFeeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqChargeMoney_'+i+'">'+moneyFun.format(data[i].chargeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '        </tr>';
                $("#batchRedeemBody").append(html);
            }
            //保本浮动收益型
            if(financialType === "1"){
                var html = '        <tr id="'+i+'">\n' +
                    '            <td align="center">\n' +
                    '                <a href="#" onclick="deleteDtl(this)">'+bt.lang("common.button.delete")+'</a><input  name="reqId" type="hidden" value="'+data[i].id+'">' +
                    '<input id="dayLimit_'+i+'" name="dayLimit" type="hidden" value="'+data[i].dayLimit+'"> ' +
                    '<input id="incomeClause_'+i+'" name="incomeClause" type="hidden" value="'+data[i].incomeClause+'">' +
                    '<input id="leftMoneyVal_'+i+'" name="incomeClause" type="hidden" value="'+leftMoney+'">' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="redeemDate_'+i+'" name="redeemDate" data-name="redeemDate" data-biz-name="redeemDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType"  data-min="'+minDate+'" data-max="'+maxDate+'" data-type="date" data-callback="redeemDateCallback">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="redeemMoney_'+i+'" name="redeemMoney" style="text-align:right"  data-name="redeemMoney" data-biz-name="redeemMoney_'+i+'" data-callback="redeemMoneyCallback" \n' +
                    '                       autocomplete="off" class="layui-input bt-biz-moneyType" data-max="'+redeemMoney+'" value="'+redeemMoney+'" placeholder="'+bt.lang("table.choProductRedeem.redeemMoney")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="transferDate_'+i+'" name="transferDate" data-name="transferDate" data-biz-name="transferDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType" data-today="true" data-min="'+minDate+'" data-type="date">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="incomeMoney_'+i+'" name="incomeMoney" style="text-align: right;"  data-name="incomeMoney"\n' +
                    '                       data-decimal="2" data-minus="true"\n' +
                    '                       data-biz-name="incomeMoney_'+i+'" autocomplete="off" class="layui-input bt-biz-moneyType" data-callback="incomeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" data-name="incomeRate"  id="incomeRate_'+i+'" data-biz-name="incomeRate_'+i+'" class="layui-input bt-biz-rateType" data-decimal="6"\n' +
                    '                       placeholder="'+bt.lang("table.choProductRedeem.incomeRate")+'" data-callback="bt.incomeRateCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text"  name="rmk" id="rmk'+i+'" class="layui-input"\n' +
                    '                       placeholder="'+bt.lang("table.choProduct.rmk")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="subFeeMoney" id="subFeeMoney_'+i+'" data-biz-name="subFeeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="subFeeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="redeemChargeMoney" id="redeemChargeMoney_'+i+'" data-biz-name="redeemChargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="redeemChargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="chargeMoney"  id="chargeMoney_'+i+'" data-biz-name="chargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="chargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="lastRedeemDate_'+i+'" style="color: rgb(184,184,184)">'+lastRedeemDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="incomeClauseShow_'+i+'">'+bt.dict('choProduct.incomeClause',data[i].incomeClause)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyDate_'+i+'">'+dateFun.format(data[i].buyDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="interestStartDate_'+i+'">'+dateFun.format(data[i].interestStartDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="deadlineDate_'+i+'">'+deadlineDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="dayLimitShow_'+i+'">'+dayLimit+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="cashDate_'+i+'">'+cashDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyMoney_'+i+'">'+moneyFun.format(data[i].buyMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="leftMoney_'+i+'">'+leftMoney+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="referIncomeRate_'+i+'">'+referIncomeRate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="referIncomeMoney_'+i+'">'+referIncomeMoney+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="isMoveUp_'+i+'">'+bt.dict("sf_yn",data[i].isMoveUp)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="capitalSource_'+i+'">'+bt.dict("choProduct.capitalSource",data[i].capitalSource)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqSubFeeMoney_'+i+'">'+moneyFun.format(data[i].subFeeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqChargeMoney_'+i+'">'+moneyFun.format(data[i].chargeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '        </tr>';
                $("#batchRedeemBody").append(html);
            }
            //非保本浮动收益型
            if(financialType === "2"){
                var html = '        <tr id="'+i+'">\n' +
                    '            <td align="center">\n' +
                    '                <a href="#" onclick="deleteDtl(this)">'+bt.lang("common.button.delete")+'</a><input  name="reqId" type="hidden" value="'+data[i].id+'">' +
                    '<input id="dayLimit_'+i+'" name="dayLimit" type="hidden" value="'+data[i].dayLimit+'"> <input id="incomeClause_'+i+'" name="incomeClause" type="hidden" value="'+data[i].incomeClause+'">' +
                    '<input id="isMaintainVal_'+i+'" name="isMaintainVal" type="hidden" value="'+data[i].isMaintainVal+'">' +
                    '<input id="leftMoneyVal_'+i+'" name="incomeClause" type="hidden" value="'+leftMoney+'">'+
                    '            </td>\n' +
                    '<td>' +
                    ' <input type="text" id="redeemDailyNetVal_'+i+'" name="redeemDailyNetVal"  data-name="redeemDailyNetVal" data-biz-name="redeemDailyNetVal_'+i+'" class="layui-input bt-biz-moneyType" data-decimal="6"\n' +
                    '                        data-callback="redeemDailyNetValCallback">' +
                    '</td>'+
                    '<td>' +
                    '<input type="text" id="redeemPortion_'+i+'" name="redeemPortion" style="text-align:right" data-name="redeemPortion" data-biz-name="redeemPortion_'+i+'" data-decimal="6"\n' +
                    '                       autocomplete="off" class="layui-input bt-biz-moneyType"  data-callback="redeemPortionCallback">' +
                    '</td>'+
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="redeemDate_'+i+'" name="redeemDate" data-name="redeemDate" data-biz-name="redeemDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType" data-min="'+minDate+'" data-max="'+maxDate+'" data-type="date" data-callback="redeemDateCallback">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="redeemMoney_'+i+'" name="redeemMoney" style="text-align:right"  data-name="redeemMoney" data-biz-name="redeemMoney_'+i+'" data-callback="redeemMoneyCallback" \n' +
                    '                       autocomplete="off" class="layui-input bt-biz-moneyType" data-max="'+redeemMoney+'" value="'+redeemMoney+'" placeholder="'+bt.lang("table.choProductRedeem.redeemMoney")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <div class="layui-input-inline">\n' +
                    '                    <input type="text" id="transferDate_'+i+'" name="transferDate" data-name="transferDate" data-biz-name="transferDate_'+i+'"\n' +
                    '                           class="layui-input bt-biz-dateType" data-today="true" data-min="'+minDate+'" data-type="date">\n' +
                    '                </div>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" id="incomeMoney_'+i+'" name="incomeMoney" style="text-align: right;"  data-name="incomeMoney"\n' +
                    '                       data-decimal="2" data-minus="true"\n' +
                    '                       data-biz-name="incomeMoney_'+i+'" autocomplete="off" class="layui-input bt-biz-moneyType" data-callback="incomeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text" data-name="incomeRate"  id="incomeRate_'+i+'" data-biz-name="incomeRate_'+i+'" class="layui-input bt-biz-rateType" data-decimal="6"\n' +
                    '                       placeholder="'+bt.lang("table.choProductRedeem.incomeRate")+'" data-callback="bt.incomeRateCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input type="text"  name="rmk" id="rmk'+i+'" class="layui-input"\n' +
                    '                       placeholder="'+bt.lang("table.choProduct.rmk")+'" >\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="subFeeMoney" id="subFeeMoney_'+i+'" data-biz-name="subFeeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="subFeeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="redeemChargeMoney" id="redeemChargeMoney_'+i+'" data-biz-name="redeemChargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="redeemChargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <input name="chargeMoney"  id="chargeMoney_'+i+'" data-biz-name="chargeMoney_'+i+'" class="layui-input bt-biz-moneyType" data-callback="chargeMoneyCallback">\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="lastRedeemDate_'+i+'" style="color: rgb(184,184,184)">'+lastRedeemDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="incomeClauseShow_'+i+'">'+bt.dict('choProduct.incomeClause',data[i].incomeClause)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyDate_'+i+'">'+dateFun.format(data[i].buyDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="interestStartDate_'+i+'">'+dateFun.format(data[i].interestStartDate, 'yyyy-MM-dd')+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="deadlineDate_'+i+'">'+deadlineDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="dayLimitShow_'+i+'">'+dayLimit+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="cashDate_'+i+'">'+cashDate+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="buyMoney_'+i+'">'+moneyFun.format(data[i].buyMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="leftMoney_'+i+'">'+leftMoney+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="isMoveUp_'+i+'">'+bt.dict("sf_yn",data[i].isMoveUp)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="isStore_'+i+'">'+bt.dict("sf_yn",data[i].isStore)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="payWay_'+i+'">'+bt.dict("financial.payWay",data[i].payWay)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="incomeType_'+i+'">'+incomeType+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="contractNo_'+i+'">'+contractNo+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="contractRate_'+i+'"></label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="capitalSource_'+i+'">'+bt.dict("choProduct.capitalSource",data[i].capitalSource)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqSubFeeMoney_'+i+'">'+moneyFun.format(data[i].subFeeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '            <td>\n' +
                    '                <label id="reqChargeMoney_'+i+'">'+moneyFun.format(data[i].chargeMoney)+'</label>\n' +
                    '            </td>\n' +
                    '        </tr>';
                $("#batchRedeemBody").append(html);
            }
        }
        //保本收益型//保本浮动收益型
        if(financialType === "0" || financialType === "1") {
            var hj = '<tr><td align="right">'+bt.lang("table.adjustDetail.moneySum")+'</td><td></td><td align="right"> <label id="redeemMoneySum">'+moneyFun.format(redeemMoneySum)+'</label></td>' +
                '<td></td><td align="right"><label id="incomeMoneySum"></label></td><td></td><td></td><td align="right"><label id="subFeeMoneySum"></label></td>' +
                '<td align="right"><label id="redeemChargeMoneySum"></label></td><td align="right"><label id="chargeMoneySum"></label></td></tr>';
            $("#batchRedeemBody").append(hj);
        }
        //保本收益型//保本浮动收益型
        if(financialType === "2") {
            var hj = '<tr><td></td><td></td><td align="right">'+bt.lang("table.adjustDetail.moneySum")+'</td><td></td><td align="right"> <label id="redeemMoneySum">'+moneyFun.format(redeemMoneySum)+'</label></td>' +
                '<td></td><td align="right"><label id="incomeMoneySum"></label></td><td></td><td></td><td align="right"><label id="subFeeMoneySum"></label></td>' +
                '<td align="right"><label id="redeemChargeMoneySum"></label></td><td align="right"><label id="chargeMoneySum"></label></td></tr>';
            $("#batchRedeemBody").append(hj);
        }
        dateType.render();
        rateType.render();
        moneyType.render();
    }

    form.on('submit(submit)',function (data) {
        var action = '/chowmatistic/choproductredeem/modify';
        $("#status").val($(this).attr("value"));
        var reqIds = $("input[name='reqId']");
        if(checkDate(reqIds.length)) {
            bt.loading.show();
            $.post(basePath + action, $("#inputForm").serialize(), function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParent();
                            bt.navtab.closeCurrent();
                            // bt.navtab.refreshCurrent();
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                    bt.loading.hide();
                })
            }, 'JSON');
        }
        return false;
    })
    form.renderIcon();

    //根据赎回日期计算
    function calIncomeMoney(i) {
        var _redeemMoney = moneyType.get("redeemMoney_"+i).val();
        var _incomeRate = bt.rateType.get("incomeRate_"+i).val() / 100;
        var _incomeClause = $("#incomeClause_"+i).val();
        var incomeClause = _incomeClause;
        if(incomeClause == 0){
            incomeClause = 360;
        }
        var _dayLimit = getBetweenDay($("#interestStartDate_"+i).text(),dateType.get("redeemDate_"+i).val(),_incomeClause);//赎回日期-起息日期
        var _incomeMoney = moneyFun.format(_redeemMoney*_incomeRate*_dayLimit/incomeClause);
        if ($('#payWay_'+i).text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate_'+i).text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate_"+i).val(),_incomeClause);
            }
            var leftMoneyVal =$("#leftMoneyVal_"+i).val();
            _incomeMoney = moneyFun.format(leftMoneyVal * _incomeRate * _dayLimit / incomeClause)
        }
        if (_incomeMoney === 0 || _incomeMoney === '0.00') {
            bt.rateType.get("incomeRate_"+i).val(0);
        }
        moneyType.get("incomeMoney_"+i).val(_incomeMoney);
    }

    //根据收益金额计算收益利率
    function calIncomeRate(i) {
        var _redeemMoney = moneyType.get("redeemMoney_"+i).val();
        var _incomeMoney = bt.moneyType.get("incomeMoney_"+i).val();
        // var _dayLimit = $("#dayLimit").val();
        var _incomeClause = $("#incomeClause_"+i).val();
        var incomeClause = _incomeClause;
        if(incomeClause == 0){
            incomeClause = 360;
        }
        var _dayLimit = getBetweenDay($("#interestStartDate_"+i).text(),dateType.get("redeemDate_"+i).val(),_incomeClause);//赎回日期-起息日期
        var number = _redeemMoney*_dayLimit;
        var _incomeRate = number === 0 ? 0 : _incomeMoney/(number/incomeClause);
        if ($('#payWay_'+i).text() === '分段付息') {
            var lastRedeemDate = $('#lastRedeemDate_'+i).text();
            if (lastRedeemDate !== '') {
                //本次赎回日期-上次赎回日期
                _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate_"+i).val(),_incomeClause);
            }
            var leftMoneyVal =$("#leftMoneyVal_"+i).val();
            _incomeRate = _dayLimit === 0 ? 0 : (_incomeMoney / leftMoneyVal) / (_dayLimit / incomeClause)
        }
        if (_incomeRate === 0) {
            moneyType.get("incomeMoney_"+i).val(0);
        }
        _incomeRate=_incomeRate.toFixed(8);
        bt.rateType.get("incomeRate_"+i).val(_incomeRate);
    }

    /**
     * 计算两个日期之间的间隔
     * @param startDate
     * @param endDate
     * @returns {number}
     */
    function getBetweenDay(startDate,endDate,incomeClause){
        if (null == startDate || null == endDate || '' == startDate || '' == endDate){
            return;
        }
        var date1 = bt.date.get(startDate);
        var date2 = bt.date.get(endDate);
        //判断收益条款，为0（30/360）时，需要对年对月对日取期限
        var btt;
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

    /**
     * 删除
     * @param _this
     * @param id
     */
    deleteDtl = function (_this) {
        if(count === 1){
            $(_this).parents("tr").next("tr").remove();
        }
        $(_this).parents("tr").remove();
        count = count - 1;
    }

    /**
     * 验证必填数据
     */
    function checkDate(length) {
        var financialType = $("#financialType").val();
        if(length <= 0){
            parent.layer.alert("没有赎回数据，请重新选择！");
            return false;
        }
        for(var i=0;i<length;i++){
            if(financialType === "2") {
                if (moneyType.get("redeemDailyNetVal_" + i)) {
                    var redeemDailyNetVal = moneyType.get("redeemDailyNetVal_" + i).val();
                    if (redeemDailyNetVal == null || redeemDailyNetVal == "" || redeemDailyNetVal == undefined) {
                        parent.layer.alert(bt.lang("common.message.choProductRedeem.redeemDailyNetVal.isnotnull"));
                        return false;
                    }
                }
                if (moneyType.get("redeemPortion_" + i)) {
                    var redeemPortion = moneyType.get("redeemPortion_" + i).val();
                    if (redeemPortion == null || redeemPortion == "" || redeemPortion == undefined) {
                        parent.layer.alert(bt.lang("common.message.choProductRedeem.redeemPortion.isnotnull"));
                        return false;
                    }
                }
            }
            //赎回日期 必填
            var redeemDate =  dateType.get("redeemDate_"+i).val();
            if(redeemDate == null || redeemDate == "" || redeemDate == undefined){
                parent.layer.alert(bt.lang("common.message.choProductRedeem.redeemDate.isnotnull"));
                return false;
            }
            //赎回金额 必填
            var redeemMoney =  moneyType.get("redeemMoney_"+i).val();
            //分段付息时不验证赎回金额
            if ($('#payWay_'+i).text() === '分段付息') {
                if(redeemMoney == null || redeemMoney == "" || redeemMoney == undefined || redeemMoney < 0){
                    parent.layer.alert(bt.lang("common.message.choProductRedeem.redeemMoney.isnotnull"));
                    return false;
                }
            }else{
                if(redeemMoney == null || redeemMoney == "" || redeemMoney == undefined || redeemMoney <= 0){
                    parent.layer.alert(bt.lang("common.message.choProductRedeem.redeemMoney.isnotnull"));
                    return false;
                }
            }
            //到账日期 必填
            var transferDate =  dateType.get("transferDate_"+i).val();
            if(transferDate == null || transferDate == "" || transferDate == undefined){
                parent.layer.alert(bt.lang("common.message.choProductRedeem.transferDate.isnotnull"));
                return false;
            }
            //收益金额 必填
            var incomeMoney =  moneyType.get("incomeMoney_"+i).val();
            if(incomeMoney == null || incomeMoney == "" || incomeMoney == undefined || incomeMoney <= 0){
                parent.layer.alert(bt.lang("common.message.choProductRedeem.incomeMoney.isnotnull"));
                return false;
            }
            //实际收益率 必填
            var incomeRate =  bt.rateType.get("incomeRate_"+i).val();
            if(incomeRate == null || incomeRate == "" || incomeRate == undefined || incomeRate <= 0){
                parent.layer.alert(bt.lang("common.message.choProductRedeem.incomeRate.isnotnull"));
                return false;
            }
        }
        return true;
    }
});



// 赎回日净值回调方法
function redeemDailyNetValCallback(data,elem) {
    var id = elem.data("biz-name");
    var i = id.substr(id.lastIndexOf("_") + 1);
    calRedeemInfo(i)
}

// 赎回份额回调方法
function redeemPortionCallback(data,elem) {
    var id = elem.data("biz-name");
    var i = id.substr(id.lastIndexOf("_") + 1);
    calRedeemInfo(i)
}

// 计算赎回本金、收益金额
function calRedeemInfo(i) {
    if ($('#isMaintainVal_'+i).val() === '1') {
        return
    }
    var redeemPortion = Number(moneyType.get('redeemPortion_'+i).val());
    // 赎回总金额 = 赎回日净值 * 赎回份额
    var redeemTotalMoney = Number(moneyType.get('redeemDailyNetVal_'+i).val()) * redeemPortion
    // 收益金额 = 赎回日净值 * 赎回份额 - 购买日净值 * 赎回份额
    var incomeMoney = redeemTotalMoney - Number(moneyType.get('dailyNetVal_'+i).val()) * redeemPortion
    moneyType.get('incomeMoney_'+i).val(incomeMoney)
    // 赎回本金 = 赎回总金额 - 收益金额（收益为负数则等于赎回总金额）
    var redeemMoney = incomeMoney <= 0 ? redeemTotalMoney : redeemTotalMoney - incomeMoney
    // 如果将当前份额全部赎回，则赎回本金等于可赎回本金
    if (redeemPortion === Number($('#maxRedeemPortion_'+i).val())) {
        redeemMoney = Number($('#redeemableMoneyVal_'+i).val())
    }
    moneyType.get('redeemMoney_'+i).val(redeemMoney)
}

//银行流水选择回调回调方法
var callbackBisAccDtl = function (data) {
    var amt = 0;
    for(var i=0;i<data.length;i++){
        amt = amt + data[i].amt;
    }
    var redeemMoney = moneyType.get('redeemMoney_'+index).val();
    moneyType.get("incomeMoney_"+index).val(amt-redeemMoney);
    calIncomeRate(index);
};

//根据收益金额计算收益利率
var calIncomeRate = function (i) {
    var _redeemMoney = moneyType.get("redeemMoney_"+i).val();
    var _incomeMoney = bt.moneyType.get("incomeMoney_"+i).val();
    // var _dayLimit = $("#dayLimit").val();
    var _incomeClause = $("#incomeClause_"+i).val();
    var incomeClause = _incomeClause;
    if(incomeClause == 0){
        incomeClause = 360;
    }
    var _dayLimit = getBetweenDay($("#interestStartDate_"+i).text(),dateType.get("redeemDate_"+i).val(),_incomeClause);//赎回日期-起息日期
    var number = _redeemMoney*_dayLimit;
    var _incomeRate = number === 0 ? 0 : _incomeMoney/(number/incomeClause);
    if ($('#payWay_'+i).text() === '分段付息') {
        var lastRedeemDate = $('#lastRedeemDate_'+i).text();
        if (lastRedeemDate !== '') {
            //本次赎回日期-上次赎回日期
            _dayLimit = getBetweenDay(lastRedeemDate, dateType.get("redeemDate_"+i).val(),_incomeClause);
        }
        var leftMoneyVal =$("#leftMoneyVal_"+i).val();
        _incomeRate = _dayLimit === 0 ? 0 : (_incomeMoney / leftMoneyVal) / (_dayLimit / incomeClause)
    }
    if (_incomeRate === 0) {
        moneyType.get("incomeMoney_"+i).val(0);
    }
    _incomeRate=_incomeRate.toFixed(8);
    bt.rateType.get("incomeRate_"+i).val(_incomeRate);
};

/**
 * 计算两个日期之间的间隔
 * @param startDate
 * @param endDate
 * @returns {number}
 */
var getBetweenDay = function (startDate,endDate,incomeClause){
    if (null == startDate || null == endDate || '' == startDate || '' == endDate){
        return;
    }
    var date1 = bt.date.get(startDate);
    var date2 = bt.date.get(endDate);
    //判断收益条款，为0（30/360）时，需要对年对月对日取期限
    var btt;
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
