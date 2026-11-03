var moneyFun;
var moneyType;
bt.use(['form', 'dateType', 'dicts', 'date','moneyType','bttable'], function () {
    var $ = bt.jquery,form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    var rowMR = 1;
    bt.bttable.init({
        elem: "#dataTable",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                rowMR++;
                var html = '<tr row=' + rowMR + '>';
                html += '<td>' + $("#transTimeStr").val() + '</td>';
                html += '<td>' + $("#corpName").val() + '</td>';
                html += '<td>' + $("#bankAcc").val() + '</td>';
                html += '<td>' + $("#accName").val() + '</td>';
                html += '<td>' + $("#oppAccNo").val() + '</td>';
                html += '<td>' + $("#oppAccName").val() + '</td>';
                html += '<td>' + $("#curName").val() + '</td>';
                html += '<td>' + $("#cdSign").val() + '</td>';
                html += '<td align="center"><div class="layui-input-inline"><input type="text" data-name="amt" data-biz-name="amt' + rowMR + '" name="amt" id="amt' + rowMR + '" class="layui-input bt-biz-moneyType" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00"/><div></td>';
                html += '<td>' + $("#remark").val() + '</td>';
                html += '<td>' + $("#fbsStatus").val() + '</td>';
                html += '<td><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a></td>';
                html += '</tr>';
                $("#dataTable").find("tbody").find("#sumTr").before(html);
                bt.moneyType.init();
                form.render();
            }
        }],
        columns: [{
            data: "transTimeStr",
            render: function (data, row) {
                return $("#transTimeStr").val();
            }
        }, {
            data: "corpName",
            render: function (data, row) {
                return $("#corpName").val();
            }
        }, {
            data: "bankAcc",
            render: function (data, row) {
                return $("#bankAcc").val();
            }
        }, {
            data: "accName",
            render: function (data, row) {
                return $("#accName").val();
            }
        }, {
            data: "oppAccNo",
            render: function (data, row) {
                return $("#oppAccNo").val();
            }
        },{
            data: "oppAccName",
            render: function (data, row) {
                return $("#oppAccName").val();
            }
        },{
            data: "curName",
            render: function (data, row) {
                return $("#curName").val();
            }
        },{
            data: "cdSign",
            render: function (data, row) {
                return $("#cdSign").val();
            }
        }, {
            data: "curName",
            render: function (data, row) {
                return '<input type="text" data-name="amt" data-biz-name="amt' + rowMR + '" name="amt" id="amt' + rowMR + '" class="layui-input" data-type="number" autocomplete="off"  lay-verify="required|number2" data-callback="calItemMoney" value="0.00"/>';
            }
        }, {
            data: "remark",
            render: function (data, row) {
                return $("#remark").val();
            }
        }, {
            data: "fbsStatus",
            render: function (data, row) {
                return $("#fbsStatus").val();
            }
        }, {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
            }
        }],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        var amts = [];
        $('form').find('input[name="amt"]').each(function () {
            amts.push($(this).val());
        });
        if (amts) {
            data.field.amts = amts;
        }
        var oldAmt = moneyFun.toNumber($("#oldAmt").val());
        var newAmt = moneyFun.toNumber($("#newAmt").val());
        if(oldAmt != newAmt){
            layer.alert(bt.lang('nis.pay.message.split.valid'), {icon: 2});
            return false;
        }
        var action = "/bankacc/bisaccdtlbudgetrule/splitSave";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});

//合计金额
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var moneyTotal = 0;
    $('form').find('input[data-name="amt"]').each(function () {
        moneyTotal = moneyFun.add(moneyTotal, moneyFun.toNumber($(this).val()));
    });
    $("#newAmt").val(moneyTotal);
    $("#sumMoney").text(moneyFun.format(moneyTotal));
}


