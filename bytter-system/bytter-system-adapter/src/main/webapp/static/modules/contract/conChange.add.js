var form,uploader,moneyType,dateType,conSelect;
bt.use(['form','layedit','btdate','moneyType','jquery','dateType','btUpload','conSelect','conView'], function() {
    var $ = bt.jquery, date = new Date();
    form = bt.form();
    conSelect = bt.conSelect.init();
    bt.conView.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });


    //监听新增保存
    form.on('submit(save)', function (data) {
        submit(data, "/contract/conchange/save");
    });
    //监听新增提交
    form.on('submit(submit)', function (data) {
        submit(data, "/contract/conchange/submit");
    });
    var submit = function (data, action) {
        bt.loading.show();
        $("#attachmentId").val(uploader.getGroupId());
        data.field.attachmentId = uploader.getGroupId();
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
                bt.navtab.refreshParentTable();//刷新父页面表格
                location.reload();//重载当前页面
            }
        }, 'JSON');
        return false;
    }

    //合同选择参数
    bt.conSelectParams=function(){
        var opResource="con:conchange:resource:op";
        conSelect.set({"opResource":opResource});
        return true;
    }

    //合同选择回调函数
    bt.conSelectCallback = function(datas,dom){
        if(datas){
            $("#mid").val(datas.id);//合同ID
            $("#conName").html(datas.conName);//合同名称
            $("#conNo").html(datas.conNo);//合同代码
            if(datas.corpCode){
                $("#corpName").html('('+datas.corpCode+')'+datas.corpName);//合同发起人
            }
            $("#signType").html(datas.signTypeName);//签署方式
            $("#typeName").html(datas.typeName);//合同类型
            $("#beginDate").html(datas.beginDate);//合同生效日期
            $("#endDate").html(datas.endDate);//合同失效日期
            //附件初始化
            initAttachmenId(datas.attachmentId);
            //业务信息初始化
            initShowConBusinessInfo(datas.id);
        }
    }
});

function initAttachmenId(attachmenId){
    $("#conAttachmen").html("");
    if(attachmenId){
        bt.btUpload.init({
            elem: "#conAttachmen",
            auto: false,
            isExpand: false,
            hasDelete: false,
            hasUpload: false,
            gid:attachmenId,
            success:function(data){
            }
        });
    }
}


//初始化合同字段明细
function initShowConBusinessInfo(mid){
    if(mid){
        $.post(basePath + "/contract//conInfoBusdtl/getConInfoBusdtlByMid", {
            mid : mid
        }, function(data) {
            if(data && data.length>0){
                $("#businessInfoTable tbody").html("");
                var html='';
                for(var i = 0; i < data.length; i++) {
                    var val='';
                    if(!bt.empty(data[i].val)){
                        val = data[i].val
                    }
                    var content='<td class="textRight" width="15%">'+bt.lang(data[i].name)+'</td>';
                    content+='<td width="35%">';
                    if(data[i].fieldType=='02'){//02金额
                        content+= bt.moneyType.getFun().format(val,{separator: ",",decimal: 2});
                    }else if(data[i].fieldType=='05'){//05利率
                        content+=bt.moneyType.getFun().mul(val, 100,4)+"%";
                    }else{
                        content+=val;
                    }
                    content+='</td>';

                    var d = i+1;
                    //判断是奇数偶数
                    if (d%2==1) {
                        html+= '<tr>';
                        html+=content;
                        //当只有一个字段
                        if(data.length==1){
                            html+='<td></td><td></td>';
                            html+='</tr>';
                        }
                    }else{
                        html+=content;
                        html+='</tr>';
                    }
                }
                $("#businessInfoTable tbody").append(html);
                form.render();
                form.renderIcon();
            }
        }, 'JSON');
    }
}






