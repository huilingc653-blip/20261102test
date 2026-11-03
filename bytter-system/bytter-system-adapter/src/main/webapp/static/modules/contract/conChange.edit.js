var corp,form,uploader,dicts,moneyType,conType;
bt.use(['form','layedit','btdate','moneyType','corp','dicts','select','conType','jquery','dateType','btUpload'], function() {
    var $ = bt.jquery, date = new Date();
    form = bt.form();
    dicts = bt.dicts.init();
    corp = bt.corp.init();
    bt.dateType.init();
    conType = bt.conType.init();
    moneyType = bt.moneyType.init();
    uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    bt.btUpload.init({
        elem: "#conAttachmen",
        auto: false,
        isExpand: false,
        hasDelete: false,
        hasUpload: false,
        gid: $("#conAttachmentId").val()
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
            //required:true,
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
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                }
                //bt.navtab.refreshParent();//刷新父选项卡
                bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
            }
        }, 'JSON');
        return false;
    }
    //业务信息初始化
    initShowConBusinessInfo($("#mid").val());

});


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







