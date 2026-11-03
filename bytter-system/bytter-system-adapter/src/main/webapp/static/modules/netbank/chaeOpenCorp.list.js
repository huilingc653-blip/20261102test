var btcorp;
bt.use(['form', 'datatable', 'dicts', 'btImport', 'corp','select'], function () {
    $ = bt.jquery, form = bt.form();
    btcorp = bt.corp.init();
    bt.dicts.init();

    importExcel = function () {
        up = bt.btImport.init({
            code: "3396",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            hideExportBtn:true,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法

                $.ajax({
                    type: "POST",
                    url: basePath + "/netbank/chaeOpenCorp/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});

                    }
                });
            }
        });
    }
    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': bt.isResource('netbank:chaeOpenCorp:list') ? (basePath + "/netbank/chaeOpenCorp/datatable") : ""
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('netbank:chaeOpenCorp:save'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: "批量设置",
                hide:bt.isnotResource('netbank:chaeOpenCorp:save'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchUpdate(data);
                }
            },{
                text: bt.lang('common.button.imports'),
                hide:bt.isnotResource('netbank:chaeOpenCorp:save'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        }
        , columns: [
            {data: "corpCode", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "isChaeOpenCorpName", bSortable: false, className: 'center', render:function (data, type, full, meta){
                    var html = '<input type="text" disabled style="width: 0px;height: 0px" value="'+full.isChaeOpenCorpName+'">'
                    html = html + '<input type="radio" class="tableRadio" lay-filter="isEnable'+full.corpId+'" name="isEnable'+full.corpId+'"'+ (full.isChaeOpenCorp && full.isChaeOpenCorp == 1 ? "checked" : "") +' value="1" title="是">'
                        +'<input type="radio" class="tableRadio" lay-filter="isEnable'+full.corpId+'" name="isEnable'+full.corpId+'" value="0" title="否" '+ (full.isChaeOpenCorp && full.isChaeOpenCorp == 0 ? "checked" : "") +' >';
                    return html;
                }},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    if(bt.isResource('netbank:chaeOpenCorp:delete')) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                        return html;
                    }
                    return '';
                }
            }
        ]
    });
    bt.dicts.render();
    bt.dicts.init();
    //form.render('select');
});
function add() {
    if(!btcorp.get("corpId").val()){
        parent.layer.alert("请先选择单位", {icon: 2});
        return false;
    }
    parent.layer.confirm('是否为此单位启用差额下拨', function () {
        $.post(basePath + "/netbank/chaeOpenCorp/save", {corpId:btcorp.get("corpId").val()}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function batchUpdate(data) {
    if (data.length < 1) {
        layer.msg("请选择数据");
        return;
    }
    var corpIds = "";
    var values = "";
    for(var index = 0; index < data.length; index++){
        if(corpIds == ""){
            corpIds = data[index].corpId;
            values = $("#appTable").find('input[lay-filter="isEnable'+data[index].corpId+'"]:checked').val();
        }else {
            corpIds = corpIds + "," + data[index].corpId;
            values = values + "," + $("#appTable").find('input[lay-filter="isEnable'+data[index].corpId+'"]:checked').val();
        }
    }
    parent.layer.confirm('是否更新数据', function () {
        $.post(basePath + "/netbank/chaeOpenCorp/batchUpdate", {corpIds:corpIds,values:values}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/netbank/chaeOpenCorp/delete/", {id:data.id}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
