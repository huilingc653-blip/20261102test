var selectType,moneyType,dict;
bt.use(['form','dicts','selectType','bttable','moneyType','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    moneyType = bt.moneyType.init();
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curEnglishCode",}
    ];

    var selectOption = function (elemName,row) {
        var option = {
            elem: "#" + elemName+row,
            url: basePath + "/sys/accountGrant/getListBy",
            urldata: {accountType: "2,3", permission: "t", corpId: $("#corpId").val()},
            scrollX: true,
            scrollY: "200px",
            searchInput: "#bankAcc"+row,
            paging: true,
            columns: bankAccCol,
            clickCallback: function (data) {
                if (data) {
                    $("#accId"+row).val(data.bankAccId);
                    $("#bankAcc"+row).val(data.bankAcc);
                    $("#corpId"+row).val(data.corpId);
                    $("#showCorp"+row).html(data.corpName);
                    $("#curId"+row).val(data.curId);
                    $("#showCur"+row).html(data.curEnglishCode);
                } else {
                    $('input.text').val('');
                }
            }
        }
        return option;
    };
    /**
     * 是否限额管控
     * @param data
     */
    isLimitCallBack = function (data) {
        var limitVal = data.value;
        moneyType.get("limitMoney1").val("");
        moneyType.get("limitMoney2").val("");
        moneyType.get("limitMoney3").val("");
        if("1" == limitVal){
            selectType.get("curId").attr("data-required",true);
            selectType.get("rateType").attr("data-required",true);
            dict.get("limitTypes").attr({'data-required': true});
        }else{
            selectType.get("curId").attr("data-required", false);
            selectType.get("rateType").attr("data-required", false);
            dict.get("limitTypes").attr({'data-required': false});
            moneyType.get("limitMoney1").removeAttr("lay-verify");
            moneyType.get("limitMoney2").removeAttr("lay-verify");
            moneyType.get("limitMoney3").removeAttr("lay-verify");
        }
        form.renderIcon();
    }

    dict = bt.dicts.init();
    selectType = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var accIds = [];
        var corpIds = [];
        var curIds = [];
        var accNatures = [];
        $('form').find('input[name="accIds"]').each(function () {
            accIds.push($(this).val());
        });
        $('form').find('input[name="corpIds"]').each(function () {
            corpIds.push($(this).val());
        });
        $('form').find('input[name="curIds"]').each(function () {
            curIds.push($(this).val());
        });
        $('select[name="accNatures"]').each(function () {
            accNatures.push($(this).val());
        });
        var accIdTemp = "";
        for(i=0;i<accIds.length;i++){
            if(i == 0){
                accIdTemp=accIds[i]
            }else{
                accIdTemp=accIdTemp+","+accIds[i]
            }
        }
        var corpIdTemp = "";
        for(i=0;i<corpIds.length;i++){
            if(i == 0){
                corpIdTemp=corpIds[i]
            }else{
                corpIdTemp=corpIdTemp+","+corpIds[i]
            }
        }
        var curIdTemp = "";
        for(i=0;i<curIds.length;i++){
            if(i == 0){
                curIdTemp=curIds[i]
            }else{
                curIdTemp=curIdTemp+","+curIds[i]
            }
        }
        var accNaturesTemp = "";
        for(i=0;i<accNatures.length;i++){
            if(i == 0){
                accNaturesTemp=accNatures[i]
            }else{
                accNaturesTemp=accNaturesTemp+","+accNatures[i]
            }
        }
        var limitTypeTemp= "";
        for(i=0;i<data.field.limitTypes.length;i++){
            if(i == 0){
                limitTypeTemp=data.field.limitTypes[i]
            }else{
                limitTypeTemp=limitTypeTemp+","+data.field.limitTypes[i]
            }
        }

        if(!bt.empty(accIdTemp)){
            data.field.accIds = accIdTemp;
        }
        if(!bt.empty(corpIdTemp)){
            data.field.corpIds = corpIdTemp;
        }
        if(!bt.empty(curIdTemp)){
            data.field.curIds = curIdTemp;
        }
        if(!bt.empty(accNaturesTemp)){
            data.field.accNatures = accNaturesTemp;
        }
        if(!bt.empty(limitTypeTemp)){
            data.field.limitTypes = limitTypeTemp;
        }
        var action = "/foreign/fexcorppool/savePool";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.closeAll();
            }
            if (result.success) {
                if (bt.navtab) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    bt.navtab.getParentDom().bt.send();
                    bt.navtab.closeCurrent();
                }
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

    //生成列表
    var rowMR = 0;
    bt.bttable.init({
        elem: "#dataTable",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                var corpTemp = $("#corpId").val();
                if (bt.empty(corpTemp)) {
                    layer.alert(bt.lang("fin.paramconfig.message.poolid.notnull"), {icon: 7});
                    return false;
                }
                var html = '<tr row=' + rowMR + '>';
                var accTd = '<input type="text" id="bankAcc'+rowMR+'" name="bankAcc'+rowMR+'" data-name="bankAcc'+rowMR+'" data-biz-name="bankAcc'+rowMR+'" autocomplete="off" class="layui-input text">';
                accTd += '<table id="bankAccTable'+rowMR+'" class="hide"><thead><tr>';
                accTd += '<th> '+bt.lang("table.accountDTO.bankAcc")+'</th>';
                accTd += '<th> '+bt.lang("table.accountDTO.accName")+'</th>';
                accTd += '<th> '+bt.lang("table.accountDTO.bankName")+'</th>';
                accTd += '<th> '+bt.lang("table.accountDTO.curName")+'</th>';
                accTd += '</tr></thead></table>';
                html += '<td align="center"><div class="layui-input-inline"><input type="hidden" id="accId'+rowMR+'" name="accIds">'+accTd+'<div></td>';
                html += '<td align="center"><input type="hidden" id="corpId'+rowMR+'" name="corpIds"><div id="showCorp'+rowMR+'"></div></td>';
                html += '<td align="center"><input type="hidden" id="curId'+rowMR+'" name="curIds"><div id="showCur'+rowMR+'"></div></td>';
                html += '<td align="center"><div class="layui-input-inline"><div class="bt-biz-dict" data-type="select" data-biz-name="accNature' + rowMR + '" data-defaultvalue="1" data-group="fexCorpPoolBank.accNature" data-name="accNatures" data-required="true"></div></div></td>';
                html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">'+bt.lang("common.button.delete")+'</a><a class="" href="#"  onclick="viewPoolAcc('+rowMR+')" >'+ bt.lang("common.button.view")+'</a></td>';
                html += '</tr>';
                $("#dataTable").children("tbody").append(html);
                bt.dicts.init();
                bt.selectTable.init(selectOption("bankAccTable",rowMR));
                rowMR++;
            }
        }],
        tableInitCallback:function(){
            bt.dicts.init();
            form.render('select');
        },
        columns: [
        ],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });

});

/**
 * 限额折算币种回调
 * @param data
 */
curCallBack = function (data) {
    $("#englishCode").val(data.englishcode);
}

/**
 * 资金池回调
 * @param data
 */
netIdCallback = function (data) {
    $("#poolId").val(data.id);
    $("#corpId").val(data.corpid);
    $("#corpName").val(data.corpname);
    $("#dataTable tbody").html("");
}

/**
 * 限额设置回调
 * @param data
 */
typeCallBack = function(data){
    var isChecked = data.othis.hasClass('layui-form-checked');
    if("all" == data.value){
        moneyType.get("limitMoney1").val("");
        moneyType.get("limitMoney2").val("");
        moneyType.get("limitMoney3").val("");
        if(isChecked){
            moneyType.get("limitMoney1").removeAttr("readonly");
            moneyType.get("limitMoney2").removeAttr("readonly");
            moneyType.get("limitMoney3").removeAttr("readonly");
            moneyType.get("limitMoney1").attr("lay-verify","required");
            moneyType.get("limitMoney2").attr("lay-verify","required");
            moneyType.get("limitMoney3").attr("lay-verify","required");
        }else {
            moneyType.get("limitMoney1").attr("readonly","readonly");
            moneyType.get("limitMoney2").attr("readonly","readonly");
            moneyType.get("limitMoney3").attr("readonly","readonly");
            moneyType.get("limitMoney1").removeAttr("lay-verify");
            moneyType.get("limitMoney2").removeAttr("lay-verify");
            moneyType.get("limitMoney3").removeAttr("lay-verify");
        }
    }else{
        moneyType.get("limitMoney"+data.value).val("");
        if(isChecked){
            moneyType.get("limitMoney"+data.value).removeAttr("readonly");
            moneyType.get("limitMoney"+data.value).attr("lay-verify","required");
        }else {
            moneyType.get("limitMoney"+data.value).attr("readonly","readonly");
            moneyType.get("limitMoney"+data.value).removeAttr("lay-verify");
        }
    }
    form.renderIcon();
}

/**
 * 查看账号
 * @param row
 * @returns {boolean}
 */
function viewPoolAcc(row) {
    var accId = $("#accId"+row).val();
    var curId = $("#curId"+row).val();
    if (bt.empty(accId) || bt.empty(curId)) {
        layer.alert(bt.lang("table.btwarnstrategy.accountselect.errorMessage"), {icon: 7});
        return false;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath +"/bankacc/btbankaccquery/view?id=" + accId + "&curId=" + curId
    });
}


/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}