bt.use(['form', 'layedit', 'moneyType', 'rateType', 'btUpload','datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.rateType.init();
    var rateFun = bt.rateType.getFun();
    // 是否为详情页
    var isView = $('#isView').val()
    var btnColumn = isView ? [] : [
        {
            hide: bt.isnotResource('cho:productNetVal:choProductNetVal:apply'),
            text:bt.lang('common.button.add'),
            action:function (e, dt, node, config) {
                add();
            }
        }
    ]
    var listColumn = [
        {data:"netValDate",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.date.format(data, "yyyy-MM-dd")
            }},
        {data:"buyMoney",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }},
        {data:"buyPortion",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return data;
            }},
        {data:"dailyNetVal",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }},
        {data:"tenThousandIncome",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }},
        {data:"dayIncome",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }},
        {data:"actualIncome",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }},
        {data:"bal",bSortable:true,className:'center',
            render:function (data, type, full, meta) {
                return bt.money.format(data);
            }}
    ]
    if (!isView) {
        listColumn.push({data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                var html = '';
                var firstId = $('#firstId').val();
                var lastId = $('#lastId').val();
                if(bt.isResource('cho:productNetVal:choProductNetVal:apply',full.corpId)){
                    if (firstId !== full.id) {
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                        if (lastId === full.id) {
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                        }
                    }
                }
                return html;
            }})
    }
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        checkbox: false,
        ordering: false,
        ignoreButtons:['output','print'],
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductNetVal/editDataTable"
        },
        buttons:btnColumn,
        columns:listColumn
    })
});

function add() {
    if ($('#lastMoney').val() == '0') {
        parent.layer.alert("该理财产品已全部赎回，无需维护日净值信息");
        return false
    }
    parent.layer.open({
        type: 2,
        area: ['800px', '530px'],
        content: basePath + "/chowmatistic/choproductNetVal/add?reqId=" + $('#reqId').val(),
        end: function () {
        }
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['800px', '530px'],
        content: basePath + "/chowmatistic/choproductNetVal/updateNetVal?id=" + data.id,
        end: function () {
        }
    });
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/chowmatistic/choproductNetVal/delete/" + data.id, function (result) {
            bt.navtab.refreshCurrent()
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
