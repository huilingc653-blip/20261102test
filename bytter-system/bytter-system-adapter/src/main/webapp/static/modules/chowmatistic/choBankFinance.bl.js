bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts', 'bank'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bankAcc',
    });
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    bt.blInterestRate = function(value){
        dealAnticInterest(value);
    };
    bt.rateType.init();
    //监听提交
    form.on('submit(submit)', function(data){
    	data.field.status=$(this).attr("value");
        var action = "/chowmatistic/chobankfinance/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	 if(bt.navtab) {
                     bt.navtab.refreshParent();
                     bt.navtab.closeCurrent();
                 }else {
                     parent.layer ? parent.layer.closeAll() : layer.closeAll();
                 }
            }
        },'JSON');
        return false;
    });

    if($("#payeeAcc")) {
        var corpId = $("#corpId").val();
        var bankTypeId = $("#bankTypeId").val();
        var bankName = $("#bankName").val();
        var accType = $("#accType").val();
        //收款账号
        payeeAccInit = bt.selectTable.init({
            elem: "#payeeAccTable",
            url: basePath+"/chowmatistic/chobankfinance/getBankAccByAccType",
            urldata: {corpId: corpId, bankTypeId: bankTypeId, bankName: bankName,accType:accType},
            scrollX: true,
            scrollY:"200px",
            searchInput:"#payeeAcc",
            paging:true,
            columns: [
                {data:"bankAcc",},
                {data:"accName",},
                {data:"bankName",},
                {data:"curName",}
            ],
            clickCallback:function(data){
                if(data){
                    $('#payeeAcc').val(data.bankAcc);
                    $('#payeeAccId').val(data.id);
                }else{
                    $('input.text').val('');
                }
            }
        });
    }

    if($("#interestRateDef")) {
        var interestRateDef = $("#interestRateDef").val();
        dealAnticInterest(interestRateDef)
    }

    function dealAnticInterest(value){
        var payMoney = $("#payMoney").val();
        var interestRate = value;
        var interestStartDate = $("#interestStartDate").val();
        var deadlineDate = $("#deadlineDate").val();
        var calculationRule = $("#calculationRule").val();

        var days;
        var num;
        if(calculationRule === '0'){
            num = 360;
            days = (bt.date.get(deadlineDate).getFullYear()-bt.date.get(interestStartDate).getFullYear())*360 + (bt.date.get(deadlineDate).getMonth()-bt.date.get(interestStartDate).getMonth())*30;
        }else{
            days = dateFun.getBetweenDay(interestStartDate, deadlineDate);
            if(calculationRule === '1'){
                num = 360;
            }else{
                num = 365;
            }
        }
        var anticInterest = payMoney * interestRate * days / num;
        moneyType.get("blInterest").val(anticInterest);
    }
    
});
