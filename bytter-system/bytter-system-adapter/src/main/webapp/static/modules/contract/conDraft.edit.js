var corp,form,uploader,dicts,moneyType,conType;
bt.use(['form','layedit','btdate','moneyType','rateType','corp','dicts','select','conType','jquery','dateType','btUpload'], function() {
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
        submit(data, "/contract/condraft/save");
    });
    //监听新增提交
    form.on('submit(submit)', function (data) {
        submit(data, "/contract/condraft/submit");
    });
    var submit = function (data, action) {
        bt.loading.show();
        $("#attachmentId").val(uploader.getGroupId());
        data.field.attachmentId = uploader.getGroupId();
        if (!data.field.attachmentId){
            parent.layer.alert(bt.lang("contract.message.attachment.isnull"),{icon:7});
            bt.loading.hide();
            return false;
        }
        var selectCorp = corp.get('corpId').data()[0];
        data.field.corpName = selectCorp.corpName
        data.field.corpCode = selectCorp.code
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
    initConType();
    initShowConBusinessInfo();

    //合同类型回调函数
    //显示该合同类型的字段明细
    bt.conTypeCallback = function(data){
        if(bt.empty(data)){

        }else{
            showConBusinessInfo(data.value);
        }
    }
});



//异步获取合同类型字段明细
function showConBusinessInfo(typeId){
    $.post(basePath + "/contract/contractTypeField/getConTypeFieldById", {
        id : typeId
    }, function(data) {
        $("#businessInfoTable tbody").html("");
        if(data && data.length>0){
            var html='';
            for(var i = 0; i < data.length; i++) {
                var htmlString='<td class="textRight" width="15%">' +
                    '<input type="hidden" name="busdtls[' + i + '].name" id="name' + i + '" value="'+data[i].fieldName+'">' +
                    '<input type="hidden" name="busdtls[' + i + '].sortNum" id="sortNum' + i + '" value="'+data[i].sortNum+'">'+
                    '<input type="hidden" name="busdtls[' + i + '].fieldType" id="fieldType' + i + '" value="'+data[i].fieldType+'">'+
                    '<input type="hidden" name="busdtls[' + i + '].requiredSign" id="requiredSign' + i + '" value="'+data[i].requiredSign+'">'+
                    bt.lang(data[i].fieldName)+
                    '</td>';
                htmlString+='<td width="35%"><input type="text"  name="busdtls['+i+'].val"  id="val' +i+'"';
                if(data[i].fieldType=='01'){//01普通字符
                    htmlString+=' class="layui-input"';
                }else if(data[i].fieldType=='02'){//02金额
                    htmlString+=' class="layui-input bt-biz-moneyType"';
                }else if(data[i].fieldType=='03'){//03数字
                    htmlString+=' class="layui-input bt-biz-moneyType"';
                }else if(data[i].fieldType=='04'){//04日期
                    htmlString+='  class="layui-input bt-biz-dateType"';
                }else if(data[i].fieldType=='05'){//05利率
                    htmlString+='  class="layui-input bt-biz-rateType" ';
                }
                if(data[i].requiredSign=="1"){
                    if(data[i].fieldType=='01') {//01普通字符
                        htmlString+=' lay-verify="required|strlength" data-maxlength="500" ';
                    } else {
                        htmlString+=' lay-verify="required" ';
                    }
                }
                htmlString+='  /></td>';
                var d = i+1;
                if (d%2==1) {
                    html+= '<tr>';
                    html+=htmlString;
                    //当只有一个字段
                    if(data.length==1){
                        html+='<td></td><td></td>';
                        html+='</tr>';
                    }
                }else{
                    html+=htmlString;
                    html+='</tr>';
                }
            }
            $("#businessInfoTable tbody").append(html);
            bt.dateType.init();
            bt.moneyType.init();
            bt.rateType.init();
            form.render();
            form.renderIcon();

        }

    }, 'JSON');
}

//初始化合同字段明细
function initShowConBusinessInfo(){
    var mid = $("#mid").val();
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
                var htmlString= '<td class="textRight" width="15%">' +
                    '<input type="hidden" name="busdtls[' + i + '].name" id="name' + i + '" value="'+data[i].name+'">' +
                    '<input type="hidden" name="busdtls[' + i + '].sortNum" id="sortNum' + i + '" value="'+data[i].sortNum+'">'+
                    '<input type="hidden" name="busdtls[' + i + '].fieldType" id="fieldType' + i + '" value="'+data[i].fieldType+'">'+
                    '<input type="hidden" name="busdtls[' + i + '].requiredSign" id="requiredSign' + i + '" value="'+data[i].requiredSign+'">'+
                    bt.lang(data[i].name)+
                    '</td>';
                htmlString+='<td width="35%"><input type="text"  name="busdtls['+i+'].val"  id="val' +i+'" value="'+val+'" ';
                if(data[i].fieldType=='01'){//01普通字符
                    htmlString+=' class="layui-input"';
                }else if(data[i].fieldType=='02'){//02金额
                    htmlString+=' class="layui-input bt-biz-moneyType"';
                }else if(data[i].fieldType=='03'){//03数字
                    htmlString+=' class="layui-input bt-biz-moneyType"';
                }else if(data[i].fieldType=='04'){//04日期
                    htmlString+='  class="layui-input bt-biz-dateType"';
                }else if(data[i].fieldType=='05'){//05利率
                    htmlString+='  class="layui-input bt-biz-rateType" ';
                }
                if(data[i].requiredSign=="1"){
                    if(data[i].fieldType=='01') {//01普通字符
                        htmlString+=' lay-verify="required|strlength" data-maxlength="500" ';
                    } else {
                        htmlString+=' lay-verify="required" ';
                    }
                }
                htmlString+='  /></td>';
                var d = i+1;
                //判断是奇数偶数
                if (d%2==1) {
                    html+= '<tr>';
                    html+=htmlString;
                    //当只有一个字段
                    if(data.length==1){
                        html+='<td></td><td></td>';
                        html+='</tr>';
                    }
                }else{
                    html+=htmlString;
                    html+='</tr>';
                }
            }
            $("#businessInfoTable tbody").append(html);
            bt.dateType.init();
            bt.moneyType.init();
            bt.rateType.init();
            form.render();
            form.renderIcon();

        }

    }, 'JSON');
}

//初始化合同类型
var setTimeNum=0;
function initConType(){
    if(bt.corp.get("corpId").val()==""){
        if(setTimeNum<4){
            setTimeout("initConType()",500);
        }
        setTimeNum++;
    }else{
        bt.conType.get("typeId").init({"corpId":bt.corp.get("corpId").val(),"typeidval":$("#typeIdVal").val()});
    }
}

//合同发起方回调函数
function corpCallback(data){
    if(data){
        if(data instanceof Array){
            var corpId="";
            for(j = 0,len=data.length; j < len; j++) {
                if(corpId){
                    corpId+=",";
                }
                corpId+=data[j].id;
            }
            bt.conType.get("typeId").init({"corpId":corpId});
        }else{
            bt.conType.get("typeId").init({"corpId":data.id});
        }
        $("#businessInfoTable tbody").html("");
    }
}





