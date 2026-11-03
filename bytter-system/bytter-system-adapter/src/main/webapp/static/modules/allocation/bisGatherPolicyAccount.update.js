bt.use(['form', 'layedit', 'dateType', 'selectTree', 'moneyType', 'ratioType', 'selectType', 'dicts','selectTable'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.ratioType.init();
    bt.dicts.init();

    //付款账号
    var bankAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var bankAccInit = bt.selectTable.init({
        elem: "#bankAccTable",
        url: basePath + "/allocation/bisgatherpolicy/getPayerAccQuery",
        urldata: {resource:'allocation:bisgatherpolicy:account:query',corpCode:'100'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();//显示加载中
        var action = "/allocation/bisgatherpolicy/modifyAccount";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success){
                parent.layer.closeAll();
            }
        },'JSON');
        return false;
    });


});
