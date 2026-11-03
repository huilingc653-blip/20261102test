bt.use(['form','layedit','btdate','moneynum','money'], function(){
    var $ = bt.jquery,form = bt.form();
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false,
            defaultVal:($(this).attr("value"))
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听提交
    form.on('submit(submit)', function(data){

        bt.loading.show();
        var accState=$(this).attr("value");
        if(accState =='-1' && !transactionBackPre()){
            return false;
        }
        var jsonArr = getFormData(accState);
        var action = "/inneraccount/fcfreeze/check";
        $.ajax({
            url: basePath+action,
            method: 'POST',
            cache:false,
            data: JSON.stringify(jsonArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                if(bt.navtab) {
                    bt.navtab.closeCurrent();
                }
            },
            error:function (a,b,c) {
                bt.loading.hide();
                parent.layer.alert(b, {icon: 2});
            }
        });
        return false;

    });

    function getFormData(accState) {
        var obj = new Object();
        obj.status=accState;
        obj.id = $("#id").val();
        obj.refuseWhy = $("#refuseWhy").val();
        return obj;
    }
    var transactionBackPre = function (obj) {
        if($("#refuseWhy").val().replace(/(^\s*)|(\s*$)/g, "") == ""){
            sysAlert(bt.lang("table.fcAcc.checkBackReason.please"));
            $("#refuseWhy").addClass('layui-form-danger');
            $("#refuseWhy")[0].focus();
            bt.loading.hide();
            return false;
        }
        return true;
    };

    function sysAlert(val) {
        // layer.alert(val, {
        //     skin: 'layui-layer-molv', //样式类名
        //     closeBtn: 0
        // });
        layer.msg(val, {
            icon: 5
            ,shift: 6
        });
    }

});

