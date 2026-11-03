bt.use(['form', 'layedit', 'select','selectTable', 'datatable', 'dateForm', 'selectType', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //从当前页面获取表格数据并设置值
    getCurrentSelectedData();

    //主体单位银行回调函数
    bt.corpBankCallback = function (data) {
        //重新加载源账户信息
        corpBankAccInit.get("corpBankAcc").reload({corpId: $("input[name='corpId']").val(), curId: '', bankType: bt.selectType.get('corpBank').val()});
        form.render();
    };
    bt.selectType.init();

    //主体账户信息
    var corpBankAccInit = bt.selectTable.init({
        elem: "#corpBankAccTable",
        url: basePath+"/foreign/fextranreq/getCorpBankAcc",
        urldata: {corpId: $("input[name='corpId']").val(), curId: '', bankType: bt.selectType.get('corpBank').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#corpBankAcc",
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#corpBankAcc').val(data.bankAcc);
                $('#corpAccId').val(data.id);
                $('#corpAccName').val(data.accName);
                $('#corpAccCurId').val(data.curId);
                $("#corpAccCurNameDiv").text(data.curEnglishCode);
                $('#corpId').val(data.corpId);
                $("#corpAccNameDiv").text(data.accName);
                $("#corpNameDiv").text(data.corpName);
                bt.selectType.get('corpBank').val(data.bankTypeId);
                //账户类别
                $('#accType').val(data.accType);
                $("#accTypeDiv").text(bt.dict("btBankAcc.accType", data.accType));

                //获取账户余额信息
                $.ajax({
                    url: basePath + "/bankacc/bisaccbal/getBal",
                    method: 'POST',
                    data: {bankAcc: data.bankAcc,curId: data.curId},
                    dataType: 'json',
                    success: function (data) {
                        moneyType.get("balance").val(data);
                        $("#balanceDiv").text(moneyFun.format(data));
                        form.render();
                    }
                });
            }else{
                $('input.text').val('');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var tranType = $("#tranType").val();
        var action = "/foreign/fextranreq/batchSetPayerAcc";
        if ("4" == tranType){ // 收付交易需要修改模板
            action = "/foreign/fextranreqCust/batchSetPayerAcc";
        }
        bt.loading.show();
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrentTable();
                    } else {
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
