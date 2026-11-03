bt.use(['form','layedit','dateType','moneyType','corp','moneynum','money','jquery'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.corp.init();
    bt.dateType.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var accState=$(this).attr("value");
        data.field.status=accState;
        if(parseFloat(bt.money.toNumber($("#freezeMoney").val())) > parseFloat(bt.money.toNumber($("#activeMoney").val()))){
            bt.loading.hide();
            layer.msg(bt.lang("innerAccount.freeze.money.compare"));
            return false;
        }

        layer.confirm(validFrom(), {
            btn: [bt.lang("common.button.submit"),bt.lang("common.button.close")] //按钮
        }, function(i){
            var action = "/inneraccount/fcfreeze/save";
            $.post(basePath + action,data.field,function(result){
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            },'JSON');
            layer.close(i);
            bt.loading.hide();
        },function (i) {
            bt.loading.hide();
        });



        return false;
    });

    function validFrom() {
        var ask = bt.lang("innerAccount.freeze.confirm");
        return ask;
    }

    form.on('select(blnctrAcc)',function (data) {
        var selectedObj=$("#blnctrAcc").find("option:selected");
        var curName = selectedObj.attr("data-curname");
        var accName = selectedObj.attr("data-accname");
        var activeMoney = selectedObj.attr("data-activemoney");
        var freezeMoney = selectedObj.attr("data-freezemoney");
        $("#curName").val(selectedObj.data("curname"));
        $("#accName").val(accName);
        $("#activeMoney").val(activeMoney);
        $("#accuFreezeMoney").val(freezeMoney);

        $("#curNameSpan").text(selectedObj.data("curname"));
        $("#accNameSpan").text(accName);
        $("#activeMoneySpan").text(activeMoney);
        $("#accuFreezeMoneySpan").text(freezeMoney);

        bt.moneynum.init({elem:$("#activeMoney"),decimal:2});
        bt.moneynum.init({elem:$("#accuFreezeMoney"),decimal:2});

        getHisAccount(data.value);
    });


    $(document).ready(function(){
        $("select[name='freezeType']").find("option").each(function (){
            if ($(this).val() == $("#freezeTypeHid").val()) {
                $(this).attr("selected", "selected");
            }
        });

        form.render('select');
        getHisAccount($("#blnctrAcc").val());
    });



    function getHisAccount(blnctrAcc) {
        var $ = bt.jquery;
        //动态添加历史冻结记录
        $("#dyncTable tbody").html("");
        $.ajax({
            url: basePath+"/inneraccount/fcfreeze/getHisListByAccount/"+blnctrAcc+"?tk="+(new Date()).getTime(),
            method: 'GET',
            dataType: 'json',
            async:false,
            success: function(data){
                if(data){
                    $.each(data,function(index){
                        $("#dyncTable tbody").append("<tr id='option"+index+"'><td>"+ data[index].freezeNo+"</td>"
                            +"<td>"+ bt.date.format(data[index].beginDate, "yyyy-mm-dd")+"</td>"
                            +"<td>"+ bt.date.format(data[index].endDate, "yyyy-mm-dd")+"</td>"
                            +"<td><span id='"+index+"freezeMoney' data-name='"+index+"freezeMoney'>"+ data[index].freezeMoney+"</span></td>"
                            +"<td>"+ data[index].freezeTypeName+"</td>"
                            +"<td>"+ data[index].freezeReason+"</td></tr>");
                        bt.moneynum.init({elem:$("#"+index+"freezeMoney"),decimal:2});
                    });
                }

            }
        });

    }
});
//网点下拉框回调
function netChange(obj) {
}
//单位下拉框回调
function corpChange(obj) {
    var $ = bt.jquery;
    $("#blnctrAcc").empty();
    getBlnctrAcc(obj.id);
}

function getBlnctrAcc(corpId) {
    var $ = bt.jquery,form = bt.form();
    $.ajax({
        url: basePath+"/inneraccount/fcfreeze/getValidAccountByCorpId/"+corpId+"?tk="+(new Date()).getTime(),
        method: 'GET',
        dataType: 'json',
        async:false,
        success: function(data){
            if(data){
                $("#blnctrAcc").append("<option value=''>"+bt.lang('common.placeholder.select')+"</option>");
                $.each(data,function(index){
                    if($("#blnctrAccHid").val() == data[index].blnctrAcc){
                        $("#blnctrAcc").append("<option value='"+data[index].blnctrAcc+"'"
                            + " data-curName='"+data[index].curName+"'"
                            + " data-accName='"+data[index].accName+"'"
                            + " data-activeMoney='"+data[index].activeMoney+"'"
                            + " data-freezeMoney='"+data[index].freezeMoney+"'"
                            +" selected>"+ data[index].blnctrAcc+"</option>");

                    }else{
                        $("#blnctrAcc").append("<option value='"+data[index].blnctrAcc+"'"
                            + " data-curName='"+data[index].curName+"'"
                            + " data-accName='"+data[index].accName+"'"
                            + " data-activeMoney='"+data[index].activeMoney+"'"
                            + " data-freezeMoney='"+data[index].freezeMoney+"'"
                            +">"+ data[index].blnctrAcc+"</option>");
                    }
                });
            }
            form.render('select');
        }
    });
}

