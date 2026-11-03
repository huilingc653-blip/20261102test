var dicts,cell;
bt.use(['form','jquery','selectTree','corp','region','moneyType','dicts','selectType','cell'], function(){
    var form = bt.form(),layer = bt.layer,$  = bt.jquery;
    bt.corp.init();
    var region  = bt.region.init();
    bt.moneyType.init();
    bt.selectType.init();
    //自定义验证规则
    form.verify({
        name: function(value){
            if(value.length < 3){
                // 组织名称至少得3个字符！
                return bt.lang('sys.sysCorp.minLength');
            }
            if(value.length>200){
                // 组织名称不能大于50个字符！
                return bt.lang('sys.sysCorp.maxLength');
            }
        },
        sortVerify:function(value){
            var pattern = /^-?\d{0,5}$/;
            if(value && !pattern.test(value)){
                // 排序值为正整数，并且不能大于5位数！
                return bt.lang('sys.sysCorp.sortVerify.maxLength');
            }
        },
        code:function(value){
            if(value.length>30){
                // 机构代码不能大于30个字符！
                return bt.lang('sys.sysCorp.code.maxLength');
            }
            var pattern = /^[a-zA-Z0-9|-]*$/;
            if(!pattern.test(value)){
                // 机构代码只能填写英文数字及'-'符号
                return bt.lang('sys.sysCorp.code.pattern');
            }
        },
        accountLicence:function(value){
            if(value && value.length > 30){
                // 开户许可证不能大于30个字符！
                return bt.lang('sys.sysCorp.accountLicence.maxLength');
            }
        },
        headPerson:function(value){
            if(value && value.length > 30){
                // 单位负责人不能大于30个字符！
                return bt.lang('sys.sysCorp.headPerson.maxLength');
            }
        },
        address:function(value){
            if(value && value.length > 200){
                // 单位地址不能大于200个字符！
                return bt.lang('sys.sysCorp.address.maxLength');
            }
        }
    });

    //监听提交
    form.on('submit(corpSubmit)', function(data){
        if(typeof(data.field.status) == "undefined"){
            data.field.status = 0;
        }else{
            data.field.status = 1;
        }

        if(typeof(data.field.type) == "undefined"){
            data.field.type = 1;
        }else if(data.field.type === "on"){
            data.field.type = 0;
        }else{
            data.field.type = 1;
        }

        if(typeof(data.field.isLimitQuota) == "undefined"){
            data.field.isLimitQuota = 0;
        }else if(data.field.isLimitQuota === "on"){
            data.field.isLimitQuota = 1;
        }else{
            data.field.isLimitQuota = 0;
        }

        if(typeof(data.field.listedCompany) == "undefined"){
            data.field.listedCompany = 0;
        }else{
            data.field.listedCompany = 1;
        }

        // var corpUserFlag;
        // var corpChildFlag;
        // $.ajaxSettings.async = false;
        // $.get(basePath + "/sys/corp/judgeCorp?id=" + data.field.id, function(resultMap){
        //     corpUserFlag = resultMap.hasUser;
        //     corpChildFlag = resultMap.hasChildCorp;
        // },'JSON');
        // $.ajaxSettings.async = true;

        // var corpParentIdOld = $("#corpParentId").val();
        // var corpParentIdNew = data.field.parentId;

        // if ((corpChildFlag && data.field.status == 0) || ((corpParentIdNew!=corpParentIdOld)&&corpChildFlag)) {
        //     parent.layer.alert(bt.lang('sys.corp.has.child.delete'), {icon: 2});
        // }else {
        //     if (corpUserFlag && data.field.status == 0) {
        //         parent.layer.alert(bt.lang('sys.corp.has.user.delete'), {icon: 2});
        //         return false;
        //     }
            var action = $('input[name="id"]').val()?"update":"save";
            $.post(basePath+'/sys/corp/'+action,data.field,function(result){
                if(result.success){
                    parent.layer.closeAll();
                }
                if(result.obj){
                    var tableHtml = "<div>";
                    for (var i = 0; i < result.obj.length; i++){
                        var corpInfo=result.obj[i];
                        tableHtml += "<br>"+bt.lang("table.sysCorp.name")+" : ("+corpInfo.corpCode+")"+corpInfo.corpName+"<br> <table class='layui-table' width='95%'>";
                        for (var j = 0; j < corpInfo.sysList.length; j++) {
                            var sysInfo=corpInfo.sysList[j];
                            tableHtml += "<tr>";
                            tableHtml += "<td rowspan='" +sysInfo.size+ "' width='20%'>"+sysInfo.sysName+"</td>";
                            for(var k = 0; k < sysInfo.busList.length; k++){
                                var busInfo=sysInfo.busList[k];
                                if(k>0){
                                    tableHtml += "<tr>";
                                }
                                tableHtml += "<td rowspan='" +busInfo.size+ "' width='20%'>"+busInfo.busName+"</td>";
                                for(var l = 0; l < busInfo.errorMsgs.length; l++){
                                    if(l>0){
                                        tableHtml += "<tr>";
                                    }
                                    tableHtml += "<td width='60%'>"+busInfo.errorMsgs[l]+"</td>";
                                    if(l>0){
                                        tableHtml += "</tr>";
                                    }
                                }
                                if(k>0){
                                    tableHtml += "</tr>";
                                }
                            }
                            tableHtml += "</tr>";
                        }
                        tableHtml += "</table>";
                    }
                    tableHtml += "</div>";
                    parent.layer.open({
                        type: 1,
                        title: "<font color=\"#FF0000\">"+result.msg+"</font>",
                        maxmin: true,
                        area : [ '500px', '300px' ],
                        btn: [bt.lang("common.button.close")],
                        content: tableHtml
                    });
                }else{
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                }
            },'JSON');
        // }
        return false;
    });

    dicts=bt.dicts.init();
    cells=bt.cell.init();

});
function parentCorpFun(data){
	if(!cells.get("unitCmBiz").val()){
        cells.get("unitCmBiz").val(data.unitCm);
    }
}
function unitAttributeFun(data,text,selectedData,beforeValue){
	if(selectedData&&selectedData.value == '02'){
		$('[data-biz-name="unitCmBiz"]').attr("lay-verify", "required");
	}else{
		$('[data-biz-name="unitCmBiz"]').attr("lay-verify", "");
	}
	bt.form().renderIcon();
}


function divclick() {
    if ($("#otherInfo").is(":hidden")) {
        $("#otherInfo").show();    //如果元素为隐藏,则将它显现
        $("#otherInfoIcon").html("&#xe61a;")
    } else {
        $("#otherInfo").hide();     //如果元素为显现,则将其隐藏
        $("#otherInfoIcon").html("&#xe602;")
    }
}