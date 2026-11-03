bt.use(['form','btUpload','layedit','dateType','moneyType', 'selectTable'], function(){
	var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#attachmentId").val()
    });

    //初始化表格赋值
    var initdataTableSet = function() {
        var bookDtlSize = $("#bookDtlSize").val();
        for(var i = 0; i < bookDtlSize; i++){
            initSelectAccount(i);
        }
    }
    var initSelectAccount = function (row) {
        var cls = "";
        if (row != undefined) {
            cls = row
        }
        if ($("#example" + cls).length > 0) {
            bt.selectTable.init({
                elem: "#example" + cls,
                url: basePath + "/finacial/accbook/fcaccbook/getTallyAccs",
                scrollX: true,
                paging: true,
                scrollY: "200px",
                searchInput: "#accountInput" + cls,
                columns: [
                    {data: "blnctrAcc",},
                    {data: "accName",},
                    {data: "subjectName",}
                ],
                clickCallback: function (data) {
                    //设置账户名称
                    $("#accountInput" + cls).val(data.blnctrAcc);
                    $("#accNameInput" + cls).text(data.accName);
                }
            });
        }
    }
    initdataTableSet();

  	//监听提交
    form.on('submit(back)', function(data){
    	bt.loading.show();//显示加载中
    	if($("#backReason").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
    		bt.loading.hide(); //关闭加载中
    		parent.layer.alert(bt.lang("table.fcAccBook.checkBackReason.please"));
            $("#backReason")[0].focus();
            return false;
        }
    	
        var action = "/finacial/accbook/fcaccbook/back";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
  	
    //监听提交
    form.on('submit(pass)', function(data){
    	bt.loading.show();//显示加载中
        var checkKey = false;
        //账号&借贷金额
        var bookDtlSize = $("#bookDtlSize").val();
        for(var i = 0; i < bookDtlSize; i++){
            //账号
            var account = $("#account"+i).val();
            var accountInput = $("#accountInput"+i).val();
            if (account != accountInput) {
                checkKey = true;
                break;
            }
            //借方金额
            var debitMoney = moneyFun.toNumber(moneyType.get("debitMoney_"+i).val());
            var debitMoneyInput = moneyFun.toNumber(moneyType.get("debitMoneyInput_"+i).val());
            if (debitMoney != debitMoneyInput) {
                checkKey = true;
                break;
            }
            //贷方金额
            var creditMoney = moneyFun.toNumber(moneyType.get("creditMoney_"+i).val());
            var creditMoneyInput = moneyFun.toNumber(moneyType.get("creditMoneyInput_"+i).val());
            if (creditMoney != creditMoneyInput) {
                checkKey = true;
                break;
            }
        }
        //记账日期
        var tallyDate = bt.dateType.get("tallyDate").val();
        var tallyDateInput = bt.dateType.get("tallyDateInput").val();
        if (tallyDate != tallyDateInput) {
            checkKey = true;
        }
        //起息日期
        var interestStartDate = bt.dateType.get("interestStartDate").val();
        var interestStartDateInput = bt.dateType.get("interestStartDateInput").val();
        if (interestStartDate != interestStartDateInput) {
            checkKey = true;
        }
        if (checkKey) {
            bt.loading.hide();
            layer.alert(bt.lang("table.fcAccBook.checkKey.valid"), {icon: 2});
            return false;
        }
        var action = "/finacial/accbook/fcaccbook/checkPass";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    
});
