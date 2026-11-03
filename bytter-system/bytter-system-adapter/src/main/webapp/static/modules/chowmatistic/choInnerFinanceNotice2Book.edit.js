bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts', 'selectType','choInnerFinance'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();
    var rateType = bt.rateType.init();

    bt.choInnerFinance.init();

    //存款信息选择
    bt.depositCallback = function(data,dom){
        $('#relatedBillId').val(data.id);
        $('#relatedBillNo').val(data.billNo);
        $('#curId').val(data.curId);
        $('#payerAccId').val(data.payeeAccId);
        $('#payerAcc').val(data.payeeAcc);
        $('#payeeAccId').val(data.payerAccId);
        $('#payeeAcc').val(data.payerAcc);
        $('#capitalAccId').val(data.payerAccId);
        $('#capitalAcc').val(data.payerAcc);
        $('#capitalAccName').val(data.payerAccName);
        $('#interestAccId').val(data.payerAccId);
        $('#interestAcc').val(data.payerAcc);
        $('#interestAccName').val(data.payerAccName);
        $('#dayLimit').val(data.dayLimit);
        $('#dateUnit').val(data.dateUnit);
        $('#depositRateType').val(data.depositRateType);
        dicts.get('noticeTypeShow').val(data.noticeType);
        //dicts.get('certificateWayShow').val(data.certificateWay);
        $('#noticeType').val(data.noticeType);
        //$('#certificateWay').val(data.certificateWay);
        moneyType.get("depositMoney").val(data.payMoney);
        rateType.get('interestRate').val(data.interestRate);
        rateType.get('accInterestRate').val(data.accInterestRate);
        var drawBal = bt.money.format(moneyFun.sub(data.payMoney, data.drawMoney));
        moneyType.get("drawMoney").val(data.drawMoney);
        moneyType.get("drawBal").val(drawBal);
        var allowBookMoney = bt.money.format(moneyFun.sub(drawBal, data.bookMoney));
        moneyType.get("allowBookMoney").val(allowBookMoney);
        moneyType.get("payMoney").val(allowBookMoney);
        dateType.get("interestStartDate").val(dateFun.format(data.interestStartDate));
        dateType.get("deadlineDate").val(dateFun.format(data.deadlineDate));
        $('#depositPurpose').val(data.purpose);
        //预计利息计算
        setExpectInterest();
    }
    var selectType = bt.selectType.init();

    //到期日期计算
    function setExpectInterest(){
        //预计利息=预约金额*存款利率*（预约日期-起息日期）/360
        var payMoney = moneyType.get("payMoney").val();
        var interestRate = rateType.get('interestRate').val();
        var payDate = dateType.get('payDate').val();
        var interestStartDate = dateType.get("interestStartDate").val();
        //通知类型
        var noticeType = $('#noticeType').val();
        var expectInterest = 0;
        var dayLimit = dateFun.getBetweenDay(interestStartDate,payDate);
        if(noticeType == "1"){
            //1天通知
            expectInterest = moneyFun.div(moneyFun.mul(moneyFun.mul(payMoney,interestRate),dayLimit),360,2);
        }else{
            //7天通知
            expectInterest = moneyFun.div(moneyFun.mul(moneyFun.mul(payMoney,interestRate),parseInt(dayLimit/7)*7),360,2);
            //活期利率
            var accInterestRate = rateType.get('accInterestRate').val();
            expectInterest = moneyFun.add(expectInterest,moneyFun.div(moneyFun.mul(moneyFun.mul(payMoney,accInterestRate),(dayLimit%7)),360,2));
        }
        moneyType.get("expectInterest").val(expectInterest);
    }

    //金额回调函数
    bt.payMoneyCallback = function(data){
        setExpectInterest();
    }

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //初始化页面
    var corpId = $("#corpIdDef").val();

    //本金存入账号
    var capitalAccInit = bt.selectTable.init({
        elem: "#capitalAccTable",
        url: basePath+"/chowmatistic/choinnerfinance/getBankAcc",
        urldata: {corpId: corpId, curId: $("#curId").val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#capitalAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#capitalAcc').val(data.bankAcc);
                $('#capitalAccName').val(data.accName);
                $('#capitalAccId').val(data.id);
            }else{
                $('input.text').val('');
            }
        }
    });
    //利息存入账号
    var interestAccInit = bt.selectTable.init({
      	 elem: "#interestAccTable",
      	 url: basePath+"/chowmatistic/choinnerfinance/getBankAcc",
      	 urldata: {corpId: corpId, busType: $("#busType").val()},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#interestAcc",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
                 $('#interestAcc').val(data.bankAcc);
      			 $('#interestAccName').val(data.accName);
      			 $('#interestAccId').val(data.id);
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });

    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        data.field.status=$(this).attr("value");
        var action = "/chowmatistic/choinnerfinance/modify";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });

    //单位回调函数
    bt.corpCallback = function(id){
        //清空选择的单号
        $('#relatedBillNo').val('');
        //设置通知账号
        bt.choInnerFinance.set({corpId:id,busType:"12"});
    	//重新加载本金存入账号
        capitalAccInit.get("capitalAcc").reload({corpId: id, curId: $("#curId").val()});
        //重新加载利息存入账号
        interestAccInit.get("interestAcc").reload({corpId: id, curId: $("#curId").val()});
        // 重新设置用途
        var purposeUrl = basePath + "/basic/business/btpurpose?getByList&sysBusType=4&corpId=" + id;
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    }
    //初始化单位
    bt.corp.init();
    //设置通知账号
    bt.choInnerFinance.set({corpId:"",busType:"12"});
});


