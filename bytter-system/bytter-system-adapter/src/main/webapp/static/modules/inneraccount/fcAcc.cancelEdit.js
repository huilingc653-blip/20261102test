bt.use(['form','layedit','moneynum','btdate','select','corp'], function(){
    var $ = bt.jquery,form = bt.form();

    $("#submit").on("click", function () {
        save("R");
    });
    $("#temp").on("click", function () {
        save("U");
    });

    function save(type) {
        bt.loading.show();
        // 验证class =“verify”区域
        if (!form.verifyFun(".verify")) {
            bt.loading.hide();
            return;
        }

        // if($("select[name='voucherType']").val()==" "){
        //     bt.loading.hide();
        //     layer.msg(bt.lang('请选择凭证种类！'));
        //     return ;
        // }
        var jsonArr = {};
        var isFL = $("#isFL").val();
        if("cancelEditFL" == isFL) {
            jsonArr = getFormData();
            jsonArr.isNeedBusRule = "1";
        }
        jsonArr.id = $("#id").val();
        jsonArr.blnctrAcc=$("#blnctrAcc").val();

        $.ajax({
            url: basePath + "/inneraccount/fcacccancel/edit/" + type,
            method: 'POST',
            cache: false,
            data: JSON.stringify(jsonArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (bt.navtab) {
                    bt.navtab.closeCurrent();
                }
            },
            error:function (a,b,c) {
                bt.loading.hide();
                parent.layer.alert(b, {icon: 2});
            }
        });
    }

    function getFormData() {
        var obj = new Object();
        var count = $("#count").val();
        var account = [],debitMoney =[],creditMoney=[],summaryContent=[];
        for(var i = 0 ; i < count ; i++){
            if(i == count-1 && $("#accountSpanShow")){
                account[i] = $("#accountSpanShow").text();
            }else{
                account[i] = $("#account"+i).val();
            }
            debitMoney[i] = $("#debitMoney"+i).val();
            creditMoney[i] = $("#creditMoney"+i).val();
            summaryContent[i] = $("#summaryContent"+i).val();
        }
        obj.account=account;
        obj.debitMoney=debitMoney;
        obj.creditMoney=creditMoney;
        obj.summaryContent=summaryContent;
        obj.transferAcc = $("select[name='transferAcc']").val();
        obj.dayOver=$("#tallyDate").val();
        obj.voucherType=$("select[name='voucherType']").val();
        obj.busGlobalId=$("#busGlobalId").val();
        obj.startDate=$("#startDate").val();
        return obj;
    }

    /**
     * 记账分录界面
     * @return
     */
    $genAccountHtml=function(){
        var isFL = $("#isFL").val();
        if("cancelEdit" == isFL){
            return ;
        }
        var account = $("#blnctrAcc").val();
        var url=basePath+"/inneraccount/fcacccancel/editFcCancelAccount";
        var param={
            account:account,
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:true,
            success: function (json) {
                $("#tagFcCancelAccount").html("");
                $("#tagFcCancelAccount").html(json);
                form.render();
            }
        });
    };


    $(document).ready(function(){

        if($('#status').val() == 'N' || $('#status').val() == "U" ){
            $('#endTrCheckBackReason').hide();
        }else{
            $('#endTrCheckBackReason').show();
        }
        $genAccountHtml();
        //form.render('select');
    });
});

