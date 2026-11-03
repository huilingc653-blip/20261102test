bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts', 'selectType'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var rateType = bt.rateType.init();

    //通知类型回调函数
    bt.noticeTypeCallback = function(data){
        if(dicts.get('noticeType').val() == "1"){
            $('#dayLimit').val(1);
        }else{
            $('#dayLimit').val(7);
        }
        //计算到期日期
        deadlineDateShow(dateType.get("interestStartDate").val(), $('#dayLimit').val());
    }
    var dicts = bt.dicts.init();

    //存款类型回调函数
    bt.depositRateTypeCallback = function (data) {
        if(data && data.id) {
            dicts.get('noticeType').val(data.period);
            dicts.get('noticeTypeShow').val(data.period);
            if (data.period == "1") {
                $('#dayLimit').val(1);
            } else {
                $('#dayLimit').val(7);
            }
            //计算到期日期
            var interestStartDate = dateType.get("interestStartDate").val();
            deadlineDateShow(interestStartDate, $('#dayLimit').val());

            //获取利率
            $.ajax({
                url: basePath + "/chowmatistic/choinnerfinance?getInterestRate",
                method: 'POST',
                data: {depositRateType: data.id, interestStartDate: interestStartDate, curId: $('#curId').val()},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        rateType.get('interestRate').val(data.obj.rate);
                    } else {
                        rateType.get('interestRate').val(0);
                    }
                    form.render();
                }
            });
        }
    };
    var selectType = bt.selectType.init();

    //参考利息
    bt.expectInterestCallback = function (data) {
        deadlineDateShow(dateType.get("interestStartDate").val(), $("#dayLimit").val());
    };
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //付款账号
    var payerAccInit = bt.selectTable.init({
      	 elem: "#payerAccTable",
      	 url: basePath+"/chowmatistic/choinnerfinance/getBankAcc",
      	 urldata: {corpId: $("input[name='corpId']").val(), curId: ''},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payerAcc",
         paging:true,
      	 columns: [
             {data:"bankAcc",},
             {data:"accName",},
             {data:"curName",},
             {data:"balance",bSortable:true,className:'textRight',
                 render: function ( data, type, full, meta ) {
                     return bt.money.format(full.balance);
                 }
             }
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payerAcc').val(data.bankAcc);
      			 $('#payerAccId').val(data.id);
      			 $('#curId').val(data.curId);
                 $('#balance').val(data.balance);
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
    	data.field.status=$(this).attr("value");
        var action = "/chowmatistic/choinnerfinance/save";
        var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
        var balance = moneyFun.toNumber($("#balance").val());//账户余额
        var noticeKeep = moneyFun.toNumber($("#noticeKeep").val());//通知账户留存金额
        balance = moneyFun.sub(balance, noticeKeep);
        if(payMoney > balance){
            bt.loading.hide();
            layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
            return false;
        }else {
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParent();
                            bt.navtab.refreshCurrent()
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
            return false;
        }
    });
    
    //单位回调函数
    bt.corpCallback = function(id){
    	//重新加载付款账号
    	payerAccInit.get("payerAcc").reload({corpId: id, curId: ''});

        // 重新设置用途
        var purposeUrl = basePath + "/basic/business/btpurpose?getByList&sysBusType=4&corpId=" + id;
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    }
    //初始化单位
    bt.corp.init();

    //到期日期计算
    function deadlineDateShow(interestStartDate, dayLimit){
        dateType.get("deadlineDate").val(dateFun.format(dateFun.addDay(interestStartDate, dayLimit)));
        setExpectInterest();
	}
    deadlineDateShow(dateType.get("interestStartDate").val(), $("#dayLimit").val());

    //参考利息计算
    function setExpectInterest(){
        //参考利息=存款金额*存款利率*（到期日期-起息日期）/360
        var payMoney = moneyType.get("payMoney").val();
        var interestRate = rateType.get('interestRate').val();
        var deadlineDate = dateType.get('deadlineDate').val();
        var interestStartDate = dateType.get("interestStartDate").val();
        if(deadlineDate && interestStartDate && interestRate && payMoney) {
            var dayLimit = dateFun.getBetweenDay(interestStartDate, deadlineDate);
            var expectInterest = moneyFun.div(moneyFun.mul(moneyFun.mul(payMoney, interestRate), dayLimit), 360, 2);
            moneyType.get("expectInterest").val(expectInterest);
        }else{
            moneyType.get("expectInterest").val(0);
        }
    }
});
