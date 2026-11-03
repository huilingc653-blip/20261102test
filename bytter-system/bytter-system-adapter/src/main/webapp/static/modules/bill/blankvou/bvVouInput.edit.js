var selectType;
bt.use(['form','layedit','moneynum','btdate','corp','select','accountTable','bank','selectType','dateType'], function(){
    var $ = bt.jquery,form = bt.form();
    selectType = bt.selectType.init();
    corpCallback=function (ids) {
        corpCallbackFun(ids);
    }
    bt.corp.init();
    bt.dateType.init();
    bt.accountTable.init({urldata:{accountType:'2,3'}});
    bt.bank.init({
        elem:'.bank'
    });
    noRules($("#ruleNo").val());
    //加载凭证种类下拉列表
    // bt.select.init({
    //     elem:'.selectVouTid',
    //     name:"vouTid",
    //     defaultVal: $("#vouTid").val(),
    //     valueField:"id",
    //     textField:"name",
    //     search: true,
    //     required: true,
    //     disabled: false,
    //     url:basePath+"/bill/blankvou/bvvouchertype/queryVoucherTypeValidByInbox"
    // });
    //加载所属库箱下拉列表
    // bt.select.init({
    //     elem:'.selectSboxId',
    //     name:"sboxId",
    //     defaultVal: $("#sboxId").val(),
    //     valueField:"id",
    //     textField:"name",
    //     search: true,
    //     required: true,
    //     disabled: false,
    //     url:basePath + "/bill/blankvou/bvstoragebox/jsonListByCorpIds/"+$("#corpIdOld").val()
    // });
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false,
            required:true,
            defaultVal: $(item).attr("defaultVal"),
            placeholder:($("[field='inputDate']").attr("placeholder"))
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
    //监听保存
    form.on('submit(save)', function(data){
        if(!data.field.bankAcc){
            data.field.curId = '';
        }
        bt.loading.show();
        var action = "/bill/blankvou/bvvouinput/modify";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
            }else{
                parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.edit.repeat"), {icon: result.success?1:2});
            }
            bt.loading.hide();
        },'JSON');
        return false;
    });
    //监听入库
    form.on('submit(storage)', function(data){
        if(!data.field.bankAcc){
            data.field.curId = '';
        }
        var action = "/bill/blankvou/bvvouinput/storage/edit";
        parent.layer.confirm(bt.lang('common.message.confirm.storage'),function(){
            bt.loading.show();
            $.post(basePath + action,data.field,function(result){
                if(result.success){
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                    bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
                }else{
                    parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.storage.repeat"), {icon: result.success?1:2});
                }
                bt.loading.hide();
            },'JSON');
        });
        return false;
    });
    //表单校验
    form.verify({
        //编码规则正则表达式
        /*pass: [
              /^[A-Za-z*0-9]+$/
              ,'只能由字母、数字或者*号构成'
            ],*/
        //编码规则校验
        ruleNo: function(val){
            if(val=="") return;
            var indexSta = val.indexOf("*");
            var length;
            var indexEnd ;
            if (indexSta != -1) {
                indexEnd = val.lastIndexOf("*");
                if (indexSta == indexEnd) {
                    length = 1;
                } else {
                    var norule = val.substring(indexSta, indexEnd+1);
                    var rule = norule.replace(/\*/g,"");
                    if (rule=='') {
                        length = norule.length;
                    } else {
                        //return "编码规则里*号必须为连续的!";
                        return bt.lang("bill.blankvou.ruleNo.message1");//编码规则里*号必须为连续的!
                    }
                }
            } else {
                //return "编码规则必须有连续的*号!";
                return bt.lang("bill.blankvou.ruleNo.message2");//编码规则必须有连续的*号!
            }
        },
        //起始号校验
        beginNos: function(value){
            var ruleNo = $("#ruleNo").val();
            var staLength = ruleNo.indexOf("*");
            var endLength = ruleNo.lastIndexOf("*");
            var starLength = parseInt(endLength) - parseInt(staLength) + 1;//编码规则的*号个数
            var end = $("#end").val();//截止号
            if(value.length > starLength){
                //return "起始号码不能大于"+starLength+"位！";
                return bt.lang("bill.blankvou.ruleNo.star.message1")+starLength+bt.lang("bill.blankvou.ruleNo.message3");// "起始号码不能大于"+starLength+"位！";
            }
            if(Number(value) > Number($("#end").val()) && Number($("#end").val()) > 0){
                //return "起始号码不能大于截止号码!";
                return bt.lang("bill.blankvou.ruleNo.star.message2");//"起始号码不能大于截止号码!";
            }
        },
        //截止号校验
        endNos: function(value){
            var ruleNo = $("#ruleNo").val();
            var staLength = ruleNo.indexOf("*");
            var endLength = ruleNo.lastIndexOf("*");
            var starLength = parseInt(endLength) - parseInt(staLength) + 1;//编码规则的*号个数
            if(value.length > starLength){
                //return "截止号码不能大于"+starLength+"位！";
                return bt.lang("bill.blankvou.ruleNo.end.message1")+starLength+bt.lang("bill.blankvou.ruleNo.message3");//"截止号码不能大于"+starLength+"位！";
            }
            if(Number(value) < Number($("#beginNo").val()) && Number($("#beginNo").val()) > 0){
                //return "截止号码不能小于起始号码!";
                return bt.lang("bill.blankvou.ruleNo.end.message2");//"截止号码不能小于起始号码!";
            }
        }
    });
});

//编码规则处理
function noRules(val){
    var reg=/^[A-Za-z*0-9]+$/
    var $ = bt.jquery;
    if(val==""){
        $(".staSpan").html("");
        $(".endSpan").html("");
        return;
    }
    if(!reg.test(val)){
        $("#ruleNo").val("");
        parent.layer.alert(bt.lang("bvvouInput.ruleno.config.check"));
        return;
    }
    var indexSta = val.indexOf("*");
    var indexEnd = 0;
    if (indexSta != -1) {
        indexEnd = val.lastIndexOf("*");
    }
    var staStr = val.substring(0,indexSta);//规则前缀
    var endStr = val.substring(indexEnd+1,val.length);//规则后缀
    $(".staSpan").html(staStr);
    $(".endSpan").html(endStr);
}

//计算份数
function getNumSize(){
    var $ = bt.jquery;
    $("#numSize").val("");
    var beginNo = $("#beginNo").val();
    var endNo = $("#endNo").val();
    if(beginNo==""||endNo=="") return;
    if (!(/^\d{1,}$/.test(beginNo))||!(/^\d{1,}$/.test(endNo))) return;
    if(Number(beginNo) > Number(endNo)) return;
    //份数不能超过1000
    var num = endNo-beginNo+1;
    if(num>1000){
        parent.layer.alert(bt.lang("bill.blankvou.bvvouinput.numsize.error"));
        $("#endNo").val("");
        return;
    }
    $("#numSize").val(num);
    $("#numSizeView").text(num);
}

//单位回调获取银行账户、银行类别、库箱
function corpCallbackFun(corpId){
    var $ = bt.jquery,form = bt.form();
    //库箱
    selectType.get("sboxId").attr({
        "data-defaultvalue": "",
        "data-url": basePath + "/bill/blankvou/bvstoragebox/jsonListByCorpIds/" + corpId
    });
    //通过单位id加载出票人银行账号
    bt.accountTable.get("bankAcc").initCorp(corpId);
    //银行类别
    $("#bankId").val("");
    $("#bankName").val("");
}

//账户加载银行
function bankAccCallBack(data){
    //根据银行账号获取银行名称
    var $ = bt.jquery;
    $("#bankAcc").val(data.bankAcc);
    $("#bankId").val(data.bankAccId);
    $("#bankName").val(data.bankName);
    $("#bankName").attr("readonly", "readonly");
    $('#curName').html(data.curName);
    $('#curId').val(data.curId);
}
function changeBankAcc(){
    $("#bankName").val("");
    $("#bankName").removeAttr("readonly");
    $('#curName').html("");
    $('#curId').val("");
}