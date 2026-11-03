var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','selectType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    selectTypes = bt.selectType.init();// 下拉框
    bt.moneyType.init();
    bt.dateType.init();

    // 生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/foreign/fexcotpbankacc/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('foreign:fex:cotp:bankacc:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"cotpName",className:'center',bSortable:true},
            //{data:"corpName",className:'center',bSortable:false},
            {data:"bankAcc",className:'center',bSortable:false},
            {data:"accName",className:'center',bSortable:false},
            {data:"curName",className:'center',bSortable:false},
            {data:"bankName",className:'center',bSortable:false},
            {data:"status",className:'center',bSortable:false,
                render: function ( data, type, full, meta ) {
                    return bt.dict("fex.status",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    if(bt.isResource('foreign:fex:cotp:bankacc:apply') && full.status!=11 && full.status!=95 && full.status!=-3){//更新和删除权限
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('foreign:fex:cotp:bankacc:apply') && full.status==95){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="makeInvalid(this)">'+bt.lang("table.fexForeign.invalid")+'</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("netbank.button.change")+'</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('foreign:fex:cotp:bankacc:apply') && full.status==-3){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="makeEnable(this)">'+bt.lang("table.fexForeign.enable")+'</a><span class="text-explode"></span>';
                    }
                    if(bt.isResource('foreign:fex:cotp:bankacc:query',full.corpId)){//查看
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

/**
 * 新增
 */
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/foreign/fexcotpbankacc/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcotpbankacc/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

/**
 * 编辑
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcotpbankacc/edit/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexcotpbankacc/edit/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

/**
 * 删除行
 * @param el
 */
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/foreign/fexcotpbankacc/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 作废
 * @param el
 */
function makeInvalid(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('fex.message.confirm.invalid'),function(){
        $.post(basePath + "/foreign/fexcotpbankacc/makeInvalid/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 启用
 * @param el
 */
function makeEnable(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('fex.message.confirm.enable'),function(){
        $.post(basePath + "/foreign/fexcotpbankacc/makeEnable/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**
 * 查看
 * @param el
 */
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/foreign/fexcotpbankacc/view/" +data.id
    });
}