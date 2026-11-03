var billTable = {};
var idsValArray = [];
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select', 'dateType', 'btImport', 'dicts','mulselect','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    rateType = bt.rateType.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.mulselect.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/billTargetProportion/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide: bt.isnotResource('bill:accebill:abemitbill:billTargetProportion:operate'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                hide: bt.isnotResource('bill:accebill:abemitbill:billTargetProportion:operate'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {
                data: "corpName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return "(" + full.corpCode + ")" + full.corpName;
                }
            },
            {data: "currentYear", bSortable: true ,className: "center"},
            {
                data: "targetProportion", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0" || data == 0 ) {
                        html = '<div name="targetProportion">' + bt.rateType.getFun().format(full.targetProportion, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div name="targetProportion">' + bt.rateType.getFun().format(0, 'percent', 2, ',') + '</div>'
                    }
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('bill:accebill:abemitbill:billTargetProportion:operate')) {
                        html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });

    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "20000007",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head,data,select) {
                bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath + "/bill/accebill/billTargetProportion/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if(result.success){
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }
});

//新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/bill/accebill/billTargetProportion/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/billTargetProportion/add",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

//跳转到编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bill/accebill/billTargetProportion/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/billTargetProportion/update?id=" + data.id,
            end:function(){
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

//删除
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/billTargetProportion/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if(result.success){
                bt.navtab.refreshParent();
                bt.navtab.refreshCurrent()
            }
        }, 'JSON');
    });
}