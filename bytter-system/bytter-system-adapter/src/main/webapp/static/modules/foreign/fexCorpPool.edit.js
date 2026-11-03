var selectType,dict;
bt.use(['form', 'dicts', 'selectType', 'bttable', 'moneyType','selectTable'], function () {
    $ = bt.jquery, form = bt.form();
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
            bt.each(dict.get("limitTypes").val(),function (i,indexVal) {
                moneyType.get("limitMoney"+indexVal).attr("lay-verify","required");
            })
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
        var ids = [];
        $('form').find('input[name="ids"]').each(function () {
            ids.push($(this).val());
        });
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
        var idTemp = "";
        for(i=0;i<ids.length;i++){
            if(i == 0){
                idTemp=ids[i]
            }else{
                idTemp=idTemp+","+ids[i]
            }
        }
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

        if(!bt.empty(idTemp)){
            data.field.ids = idTemp;
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
        var action = "/foreign/fexcorppool/modifyPool";
        $.post(basePath + action, data.field, function (result) {
            if (result.success) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.navtab.getParentDom().bt.send();
                bt.navtab.closeCurrent();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });

    var rowMR = 0;
    bt.bttable.init({
        elem: "#dataTable",
        url: basePath + "/foreign/fexcorppool/bankAccInfo?id="+$("#id").val(),
        checkbox: false,//带checkboxs
        buttons: [//表格上面按钮功能
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
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
                    html += '<td align="center"><input type="hidden" id="bid'+rowMR+'" name="ids"  autocomplete="off" class="layui-input" value="null"><div class="layui-input-inline"><input type="hidden" id="accId' + rowMR + '" name="accIds">' + accTd + '<div></td>';
                    html += '<td align="center"><input type="hidden" id="corpId' + rowMR + '" name="corpIds"><div id="showCorp' + rowMR + '"></div></td>';
                    html += '<td align="center"><input type="hidden" id="curId' + rowMR + '" name="curIds"><div id="showCur' + rowMR + '"></div></td>';
                    html += '<td align="center"><div class="layui-input-inline"><div class="bt-biz-dict" data-type="select" data-biz-name="accNature' + rowMR + '" data-defaultvalue="1" data-group="fexCorpPoolBank.accNature" data-name="accNatures" data-required="true"></div></div></td>';
                    html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a><a class="" href="#"  onclick="viewPoolAcc('+rowMR+')" >'+ bt.lang("common.button.view")+'</a></td>';
                    html += '</tr>';
                    $("#dataTable").children("tbody").append(html);
                    bt.dicts.init();
                    bt.selectTable.init(selectOption("bankAccTable",rowMR));
                    rowMR++;
                }
            }
        ],
        // btInitComplete:function(setting,json){
        tableInitCallback: function () {
            bt.dicts.init();
            for(var i=0;i<rowMR;i++){
                bt.selectTable.init(selectOption("bankAccTable",i));
            }
            form.render('select');
        },
        columns: [
            {
                "data": "bankAcc", className: 'center',
                render: function (data, type, full, meta) {
                    var accTd = '<input type="text" id="bankAcc'+rowMR+'" name="bankAcc'+rowMR+'" data-name="bankAcc'+rowMR+'" data-biz-name="bankAcc'+rowMR+'" autocomplete="off" class="layui-input text" value="'+data.bankAcc+'" lay-verify="required">';
                    accTd += '<table id="bankAccTable'+rowMR+'" class="hide"><thead><tr>';
                    accTd += '<th> '+bt.lang("table.accountDTO.bankAcc")+'</th>';
                    accTd += '<th> '+bt.lang("table.accountDTO.accName")+'</th>';
                    accTd += '<th> '+bt.lang("table.accountDTO.bankName")+'</th>';
                    accTd += '<th> '+bt.lang("table.accountDTO.curName")+'</th>';
                    accTd += '</tr></thead></table>';
                    var html = '<div class="layui-input-inline"><input type="hidden" id="bid'+rowMR+'" name="ids"  autocomplete="off" class="layui-input" value="'+data.id+'"><input type="hidden" id="accId' + rowMR + '" name="accIds" value="' + data.accId + '">' + accTd + '<div>';
                    return html;
                }
            }, {
                "data": "corpId", className: 'center',
                render: function (data, type, full, meta) {
                    var html = "<input type=\"hidden\" id=\"corpId" + rowMR + "\" name=\"corpIds\" value=" + data.corpId + "><div id=\"showCorp" + rowMR + "\">" + data.corpName + "</div>";
                    return html;
                }
            }, {
                "data": "curId", className: 'center',
                render: function (data, type, full, meta) {
                    var html = "<input type=\"hidden\" id=\"curId" + rowMR + "\" name=\"curIds\" value=" + data.curId + "><div id=\"showCur" + rowMR + "\">" + data.englishCode + "</div>";
                    return html
                }
            }, {
                "data": "accNature", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<div class="bt-biz-dict" data-type="select" data-biz-name="accNature' + rowMR + '" data-group="fexCorpPoolBank.accNature" data-name="accNatures" data-defaultvalue="' + data.accNature + '" data-required="true"></div>';
                    return html
                }
            }, {
                "data": "", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a><a class="" href="#"  onclick="viewPoolAcc('+rowMR+')" >'+ bt.lang("common.button.view")+'</a>';
                    rowMR++;
                    return html
                }
            }
        ],
        delRowCallback: function (data, $tr) {
            var id = $("#bid"+$tr.attr("row")).val();
            if(bt.empty(id)){
                $tr.remove();
            }else{
                delData(id);
            }
        }
    });

});


/**
 * 限额折算币种回调
 * @param data
 */
curCallBack = function (data) {
    $("#englishcode").val(data.englishcode);
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
 * 删除数据
 */
function delData(id) {
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fexcorppool/delete/" + id +"/0/1", function (result) {
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function (ms) {
    if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " " || ms == "null") {
        return true;
    }
    return false;
}