var moneyType;
bt.use(['form', 'layedit', 'dateType', 'corp', 'selectTableType','selectTable', 'moneyType','dicts','selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();

    bt.corpCallback = function (id) {
        //加载需求账号
        bankAccInit.get("bankAcc").rebuild({
            urldata: {corpId: id},
            columns: bankAccCol
        });
        //获取父级单位
        $.ajax({
            url: basePath + "/netbank/nisdemand?getSuperCorp",
            method: 'POST',
            data: {corpId: id},
            dataType: 'json',
            success: function (data) {
                $('#superCorpId').val(data.id);
                $('#superCorpIdDiv').html(data.name);
                renderSuperBankAccSelectTable((data && data.id) || '')
            }
        });
    };
    bt.corp.init();

    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",},
        {data: "isOnlineStr",}
    ];
    demandMoneyCallBack = function (data) {
        moneyType.get("demandAllocationMoney").val(data);
    };
    moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();

    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/netbank/nisdemand/getBankAcc",
        urldata: {corpId:$("input[name='corpId']").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
                $('#bankAccId').val(data.id);

                $('#accName').val(data.accName);
                $('#accNameDiv').html(data.accName);

                $('#bankName').val(data.bankName);
                $('#bankNameDiv').html(data.bankName);

                //获取账户余额
                $.ajax({
                    url: basePath + "/netbank/nisdemand?getBal",
                    method: 'POST',
                    data: {bankAcc: data.bankAcc,curId: data.curId},
                    dataType: 'json',
                    success: function (data) {
                        $("#balDiv").html(moneyFun.format(data.bal));
                        $("#availBalDiv").html(moneyFun.format(data.availBal));
                    }
                });

            } else {
                $('input.text').val('');
                clearAll();
            }
        }
    });

    renderSuperBankAccSelectTable('init');


    function renderSuperBankAccSelectTable(corpId){
        if(corpId === 'init'){
            bt.selectTableType.init({
                elem: "#superBankAcc",
                data:[],
                scrollX: true,
                SelectTable:true,
                tableElem:$('#superBankAccTable'),
                scrollY: "200px",
                url: basePath + "/netbank/nisdemand/getBankAcc",
                urldata: {corpId:''},
               // searchInput: "#bankAcc",
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
        }else{
            if(!corpId){
                bt.selectTableType.get('superBankAcc').renderByData([])
            }else{
                // url: ,
                //   ,
                $.ajax({
                    type: 'POST',
                    url: basePath + "/netbank/nisdemand/getBankAcc",
                    data: {corpId:corpId},
                    dataType: 'json',
                    success: function (result) {
                        bt.selectTableType.get('superBankAcc').renderByData(result||[])
                    },
                });
            }
        }

    }
    //监听提交
    form.on('submit(submit)', function (data) {
        $("#status").val($(this).attr("value"));
        parent.layer.confirm(bt.lang('common.message.confirm.submit'), function (index) {
            var action = "/netbank/nisdemand/save";
            bt.loading.show();
            $.post(basePath + action, $('#inputForm').serialize(), function (result) {
                bt.loading.hide();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer.closeAll();
                    if (result.success) {
                        if (bt.navtab) {
                            bt.navtab.refreshParentTable();
                            location.reload();//重刷页面
                        } else {
                            parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        }
                    }
                })
            }, 'JSON');
        });
        return false;
    });
})

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

    $('#bankAcc').val("");
    $('#bankAccId').val("");

    $('#accName').val("");
    $('#accNameDiv').html("");

    $('#bankName').val("");
    $('#bankNameDiv').html("");
    $("#balDiv").html("");
    $("#availBalDiv").html("");
    $("#superBalDiv").html("");
    $("#superAvailBalDiv").html("");
}