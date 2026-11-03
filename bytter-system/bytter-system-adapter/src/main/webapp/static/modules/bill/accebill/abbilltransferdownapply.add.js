var corp;
bt.use(['form','layedit','btdate','corp','select','moneyType','accountTable','bank','btUpload'], function(){
    var $ = bt.jquery,form = bt.form();
    corp = bt.corp.init();
    bt.moneyType.init();
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    bt.bank.init({
        elem:'.bank'
    })
    var uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听保存
    form.on('submit(save)', function(data){
        submit(data,"/bill/accebill/abbilltransferdownapply/save/add");
    });
    //监听提交
    form.on('submit(submit)', function(data){
        submit(data,"/bill/accebill/abbilltransferdownapply/submit/add");
    });
    var submit = function(data,action){
        bt.loading.show();
        $("#attachmentId").val(uploader.getGroupId());
        $.post(basePath + action,$('#inputForm').serialize(),function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                location.reload();//重载当前页面
            }
        },'JSON');
        return false;
    }
});

//单位回调函数
function corpCallback(data){
	var $ = bt.jquery;
    //单位组件获取不到netId，故这里先用ajax获取进行校验
    $.ajax({
        url: basePath + "/bill/accebill/abbilltransferdownapply/getSysCorpById/"+data.id,
        method: 'POST',
        dataType: "json",
        //contentType: 'application/json;charset=utf-8', //设置请求头信息
        success: function (result) {
            //没有上级网点，或者网点是本身则不能下拨申请
            if(!result.netId || result.netId==result.id){
                parent.layer.alert(bt.lang('table.abBillTransfer.corp.message'));
                corp.get('recordCorpId').val("");
                return;
            }
            $("#recordCorpName").val(data.corpName);
            //通过单位id加载出票人银行账号
            //electricBill=1 票据专户
            bt.accountTable.get("recevieBankacc").initCorp(data.id,"1");
        }
    });
}

//申请单位账号回调方法
function recevieBankaccCallBack(data){
	var $ = bt.jquery;
	$("#recevieBankacc").val(data.bankAcc);
}

