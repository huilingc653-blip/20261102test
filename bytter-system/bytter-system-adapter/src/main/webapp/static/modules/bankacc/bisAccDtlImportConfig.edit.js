bt.use(['form','layedit', 'selectType','dicts','moneyType'], function(){
	var $ = bt.jquery,form = bt.form();
    bt.selectType.init();
    bt.moneyType.init();

    //文件类型
    bt.fileTypeCallback = function(data){
        var fileType = bt.dicts.get("fileType").val();
        if(fileType == "txt"){
            $("#dtlStartLineDiv").text(bt.lang('ras.break.read.position'));
            $("#itemTitle").text(bt.lang('ras.break.read.position'));
            $("#itemDesc").text(bt.lang('ras.break.read.position.confrm.msg'));
            $("#readEndContentTitleDiv").text(bt.lang('ras.break.read.position'));
            $("#readEndContentTitleDiv").text(bt.lang('table.ras.received.import.separator'));
            $('#readEndContentDiv').hide();
            $('#dtlSeparatorDiv').show();
            $('#dtlSeparator').attr('lay-verify', 'required');
        }else{
            $("#dtlStartLineDiv").text(bt.lang('table.rasImportconf.begin.row'));
            $("#itemTitle").text(bt.lang('ras.currer.row'));
            $("#itemDesc").text(bt.lang('ras.reven.confrm.msg'));
            $("#readEndContentTitleDiv").text(bt.lang('table.rasImportconf.begin.row'));
            $("#readEndContentTitleDiv").text(bt.lang('table.bisAccDtlImportConfig.readEndContent'));
            $('#readEndContentDiv').show();
            $('#dtlSeparatorDiv').hide();
            $('#dtlSeparator').removeAttr("lay-verify");
        }
        form.renderIcon();
    }
    //收支标志
    bt.cdSignCallback = function(data){
        var cdSign = bt.dicts.get("cdSign").val();
        if(cdSign == "1"){
            //显示收支方向、交易金额；隐藏支出金额、收入金额
            $("#cdSignStrTh").show();
            $("#cdSignStrTd").show();
            $("#transMoneyTh").show();
            $("#transMoneyTd").show();
            $("#payMoneyTh").hide();
            $("#payMoneyTd").hide();
            $("#incomeMoneyTh").hide();
            $("#incomeMoneyTd").hide();
            $('#cdSignStr').attr('lay-verify', 'required');
            $('#transMoney').attr('lay-verify', 'required');
            $('#payMoney').removeAttr("lay-verify");
            $('#incomeMoney').removeAttr("lay-verify");
            $('#payMoney').val('');
            $('#incomeMoney').val('');
        } else if(cdSign == "2"){
            //显示交易金额；隐藏收支方向、支出金额、收入金额
            $("#cdSignStrTh").hide();
            $("#cdSignStrTd").hide();
            $("#transMoneyTh").show();
            $("#transMoneyTd").show();
            $("#payMoneyTh").hide();
            $("#payMoneyTd").hide();
            $("#incomeMoneyTh").hide();
            $("#incomeMoneyTd").hide();
            $('#cdSignStr').removeAttr("lay-verify");
            $('#transMoney').attr('lay-verify', 'required');
            $('#payMoney').removeAttr("lay-verify");
            $('#incomeMoney').removeAttr("lay-verify");
            $('#payMoney').val('');
            $('#incomeMoney').val('');
        } else {
            //隐藏收支方向、交易金额；显示支出金额、收入金额
            $("#cdSignStrTh").hide();
            $("#cdSignStrTd").hide();
            $("#transMoneyTh").hide();
            $("#transMoneyTd").hide();
            $("#payMoneyTh").show();
            $("#payMoneyTd").show();
            $("#incomeMoneyTh").show();
            $("#incomeMoneyTd").show();
            $('#cdSignStr').removeAttr("lay-verify");
            $('#transMoney').removeAttr("lay-verify");
            $('#payMoney').attr('lay-verify', 'required');
            $('#incomeMoney').attr('lay-verify', 'required');
            $('#cdSignStr').val('');
            $('#transMoney').val('');
        }
        form.renderIcon();
    }
    //日期所在列
    bt.dateRowCallback = function(data) {
        var dateRow = bt.dicts.get("dateRow").val();
        if(dateRow == "1"){
            //隐藏交易日期
            $("#transDateTh").hide();
            $("#transDateTd").hide();
            $('#transDate').val('');
            $('#transDate').removeAttr("lay-verify");
        }else{
            $('#transDate').attr('lay-verify', 'required');
            //显示交易日期
            $("#transDateTh").show();
            $("#transDateTd").show();
        }
    }
    bt.dicts.init();
	//监听提交
    form.on('submit(submit)', function(data){
        var action = "/payment/bisaccdtlimportconfig/modify";
        $.post(basePath + action,data.field,function(result){
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

});