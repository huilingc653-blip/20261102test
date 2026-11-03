bt.use(["jquery", "datatable", "form",  "selectType", "dicts","dateType"], function () {
    $ = bt.jquery;
    form = bt.form()
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    bt.dateType.init();//日期时间


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
            'url': basePath + "/finacial/check/fcautocheckconfig/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('finacial:check:fcautocheckconfig:op'),
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
            {data: "autoCheckCode",className:'center', bSortable: true},
            {data: "autoCheckName",className:'center', bSortable: true},
            {
                data: "checkType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAutoCheckConfig.checkType", full.checkType);
                }
            },
            {data:"launchTime",className:"center",render:function(data, type, full, meta){
                    return bt.date.format(full.launchTime, "yyyy-MM-dd hh:mm:ss");
                }},
            {
                data: "checkCycle", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcAutoCheckConfig.checkCycle", full.checkCycle);
                }
            },
            {
                data: "validSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                        if(bt.isResource('finacial:check:fcautocheckconfig:op')) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="withCorp(this)">'+bt.lang("关联单位")+'</a>';
                        }

                    return html;
                }}
        ]
    });
});


function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/finacial/check/fcautocheckconfig/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/fcautocheckconfig/add",
        end:function(){
            location.reload();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/finacial/check/fcautocheckconfig/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/fcautocheckconfig/update/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/finacial/check/fcautocheckconfig/delete/" + data.id, function(result){
            bt.loading.hide();
            bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function withCorp(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('关联单位'),basePath + "/finacial/check/fcautocheckconfig/withCorp/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/fcautocheckconfig/withCorp/" + data.id
    });
}

