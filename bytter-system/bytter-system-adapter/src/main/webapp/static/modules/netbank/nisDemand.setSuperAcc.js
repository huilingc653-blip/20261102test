var moneyType;
bt.use(['form', 'layedit', 'select','selectTable', 'dateForm', 'selectType','moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    //从当前页面获取表格数据并设置值
    getCurrentSelectedData();
    var corpId = $("#corpId").val();
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",},
        {data: "isOnlineStr",}
    ];

    //获取父级单位
    $.ajax({
        url: basePath + "/netbank/nisdemand?getSuperCorp",
        method: 'POST',
        data: {corpId: corpId},
        dataType: 'json',
        success: function (data) {
            $('#superCorpId').val(data.id);
            $('#superCorpIdDiv').html(data.name);
        }
    });

    bt.selectTableType.init({
        elem: "#superBankAcc",
        data:[],
        scrollX: true,
        SelectTable:true,
        tableElem:$('#superBankAccTable'),
        scrollY: "200px",
        url: basePath + "/netbank/nisdemand/getBankAcc",
        urldata: {corpId:corpId},
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {

                $('#superBankAcc').val(data.bankAcc);
                $('#superBankAccId').val(data.id);

                $('#superAccName').val(data.accName);
                $('#superAccNameDiv').html(data.accName);

                $('#superBankName').val(data.bankName);
                $('#superBankNameDiv').html(data.bankName);

                $('#superCorpId').val(data.corpId);
                $('#superCorpIdDiv').html(data.corpName);

                //获取账户余额
                $.ajax({
                    url: basePath + "/netbank/nisdemand?getBal",
                    method: 'POST',
                    data: {bankAcc: data.bankAcc,curId: data.curId},
                    dataType: 'json',
                    success: function (data) {
                        $("#superBalDiv").html(moneyFun.format(data.bal));
                        $("#superAvailBalDiv").html(moneyFun.format(data.availBal));
                    }
                });

            } else {
                $('input.text').val('');
                clearAll();
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/netbank/nisdemand/batchSetSuperAcc";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrentTable();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        bt.navtab.refreshCurrentTable();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    /**
     * 从当前页面获取表格数据并设置值
     */
    function getCurrentSelectedData() {
        dataSelect = bt.navtab.getCurrentDom().bt.datatable.getSelectData('#appTable');
        var ids ='';
        if(dataSelect){
            bt.each(dataSelect,function (index,item) {
                ids += item.id +',';
            })
        }
        ids = ids.slice(0,ids.length-1);
        $('#id').val(ids);
    }
});

function clearAll() {
    $('#superCorpId').val("");
    $('#superCorpIdDiv').html("");

    $('#superBankAcc').val("");
    $('#superBankAccId').val("");
    $('#superBankAccIdDiv').html("");

    $('#superAccName').val("");
    $('#superAccNameDiv').html("");

    $('#superBankName').val("");
    $('#superBankNameDiv').html("");

    $("#superBalDiv").html("");
    $("#superAvailBalDiv").html("");
}
