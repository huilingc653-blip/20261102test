bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebillClaim/config/ruleBillNo/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankTypeName", bSortable: true},
            {data: "busTypeName", bSortable: true},
            {data: "billNumFieldName", bSortable: true},
            {data: "relationName", bSortable: false},
            {data: "separated",bSortable:true},
            {data: "markName",bSortable:true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    if (bt.isResource('netbank:bankAcc:btBankAccRev:apply',full.corpId)) {
                            html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                            html += '<span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                            html += '<span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('bill.accebill.createRuleNo'), basePath + "/bill/accebillClaim/config/ruleBillNo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebillClaim/config/ruleBillNo/add",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/bill/accebillClaim/config/ruleBillNo/view?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebillClaim/config/ruleBillNo/view?id=" + data.id,
            end:function(){
                appTable && appTable.draw();
            }
        });
    }
}

//跳转到编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bill/accebillClaim/config/ruleBillNo/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebillClaim/config/ruleBillNo/update?id=" + data.id,
            end:function(){
                appTable && appTable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        if(data.mark === "1"){
            layer.alert(bt.lang('accebill.message.error.claimaccount.validsign'), {icon: 7});
            return;
        }else{
            $.post(basePath + "/bill/accebillClaim/config/ruleBillNo/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if(result.success){
                    bt.navtab.refreshParent();
                    bt.navtab.refreshCurrent()
                }
            }, 'JSON');
        }
    });
}
