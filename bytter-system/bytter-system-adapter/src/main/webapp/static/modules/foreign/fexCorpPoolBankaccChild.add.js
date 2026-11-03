var select;
bt.use(['form','dicts','selectType','bttable','moneyType','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    var dict = bt.dicts.init();
    select = bt.selectType.init();
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
            url: basePath + "/foreign/fexcorppool/getListBy",
            urldata: {poolId: $("#poolId").val()},
            scrollX: true,
            scrollY: "200px",
            searchInput: "#bankAcc"+row,
            paging: true,
            columns: bankAccCol,
            clickCallback: function (data) {
                if (data) {
                    $("#accId"+row).val(data.id);
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
        var action = "/foreign/fexcorppool/savePoolAccChild";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.navtab.getParentDom().bt.send();
                bt.navtab.closeCurrent();
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
                html += '<td align="center"><div class="layui-input-inline"><div class="bt-biz-dict" data-type="select" data-biz-name="accNature' + rowMR + '" data-defaultvalue="1" data-group="fexCorpPoolBank.accNature" data-name="accNatures"></div></div></td>';
                html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a><a class="" href="#"  onclick="viewPoolAcc('+rowMR+')" >'+ bt.lang("common.button.view")+'</a></td>';
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
        columns: [],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });
});

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