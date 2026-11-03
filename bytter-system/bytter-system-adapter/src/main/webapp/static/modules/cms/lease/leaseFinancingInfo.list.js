bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp","dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间
    bt.moneyType.init();
    corp = bt.corp.init();//单位

    /**
     * 生成列表
     */
     datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData:"false"
        },
        ajax: {
            'url': basePath + "/cms/lease/financing/info/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('cms:lease:financing:apply:add'),
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
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data: "leaseNo", bSortable: true},
            {data:"leaseType", bSortable:true,className:'center',
                render: function ( data, type, full, meta ){
                    return bt.dict("cms.financing.lease.info.leaseType",full.leaseType);
                }},
            {data:"leaseCode",bSortable:true},
            {data:"payCode",bSortable:true},
            {data: "corpName", bSortable: true},
            {data: "lessor", bSortable: true},
            {data: "supplier", bSortable: true},
            {data:"oriCurName",bSortable:true},
            {data:"oriMoney",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.oriMoney);
                }},
            {data:"rmbMoney",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.rmbMoney);
                }},
            {data:"beginDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("lease.status", full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                        if(full.status==0||full.status==-1) {
                            if(bt.isResource('cms:lease:financing:apply:update')) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        }
                            if(bt.isResource('cms:lease:financing:apply:delete')) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                            }
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    });
});


function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/lease/financing/info/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/lease/financing/info/add",
        end:function(){
            location.reload();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/lease/financing/info/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/lease/financing/info/update/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/cms/lease/financing/info/delete/" + data.id, function(result){
            bt.loading.hide();
            bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/lease/financing/info/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/lease/financing/info/view/" + data.id
    });
}

