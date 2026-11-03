bt.use(['form', 'layedit', 'datatable', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    var data = {
        bankAcc: $("#bankAcc").val(),
        curId: $("#curId").val(),
        months: $("#months").val()
    };


    //生成列表
    bt.datatable.init({
        elem: "#btBankAccDtlInfo",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        scrollY: 300,
        checkbox: true,
        ajax: {
            'url': basePath + "/bankacc/btbankaccdtl/dataTableAdjust"
        },
        buttons: [],
        buttonsLayout: false,
        columns: [
            {data: "transTime", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return '<input type="hidden" id="tempDtlId" name="tempDtlId" value="' + full.id + '">' + moneyFun.format(full.amt);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.bal);
                }
            },
            {
                data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {
                data: "rbSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.rbSign", full.rbSign);
                }
            },
            {data: "curName", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {
                data: "orderNo", bSortable: true,
                render: function (data, type, full, meta) {
                    return '<input type="text" id="tempOrderNo" name="tempOrderNo" value="' + full.orderNo + '" lay-verify="number2|strlength" data-maxlength="10">';
                }
            }
        ]
    });

    //监听提交
    form.on('submit(adjust)', function (data) {
        var selectData = [];
        var trs = $("#btBankAccDtlInfo").children("tbody").children("tr");
        // 遍历行数据
        trs.each(function (index, tr) {
            var formObj = {};
            var tds = $(tr).find("td");
            tds.each(function (i, td) {
                var $span = $(td).children("span");
                var $input = $(td).find("input");
                var $select = $(td).find("select");
                if ($input.length && !$select.length) {
                    var text = $input.val();
                    var name = $input.attr("name");
                    formObj[name] = text;
                }
                if ($select.length) {
                    var text = $select.val();
                    var name = $select.attr("name");
                    formObj[name] = text;
                }
                if ($span.length) {
                    var text = $span.text();
                    var name = $span.attr("name");
                    formObj[name] = text;
                }
            });
            selectData.push(formObj);
        });
        data.field.tempBtBankAccDtlList = selectData;
        var action = "/bankacc/btbankaccdtl/adjustOrderNo";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            })
        }, 'JSON');
        return false;
    });


    function checkOrderNo(arr) {
        var arrStr = JSON.stringify(arr);
        for (var i = 0; i < arr.length; i++) {
            if ((arrStr.match(new RegExp(arr[i], "g")).length) > 1) {
                return true;
            }
        }
        return false;
    }
});
