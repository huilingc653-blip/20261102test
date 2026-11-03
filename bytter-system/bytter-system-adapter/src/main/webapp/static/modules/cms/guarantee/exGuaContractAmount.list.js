bt.use(['form','datatable','corp','moneyType','dicts','rateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.rateType.init();
    bt.selectType.init();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/cms/guarantee/guaExGuaranteeAmount/datatable"
        },
        buttons: [
            {
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
            {data:"corpName",bSortable:false,render:function (data, type, full, meta) {
                    if(!full.corpName){
                        return "";
                    }
                    var corps = full.corpName.split(",");
                    if (corps.length > 2){
                        return corps[0]+","+corps[1]+" ...";
                    }else{
                        return full.corpName;
                    }

                }},
            {data:"assets",className:'textRight',bSortable:true,
                render:function(data, type, full, meta){
                    return moneyFun.format(full.assets);
                }
            },
            {data: "curName", bSortable: true},
            {data:"proportion",className:'textRight',render: function ( data, type, full, meta ){
                    return bt.rateType.getFun().format(full.proportion);
                }},
            {data:"assets",className:'textRight',bSortable:true,
                render:function(data, type, full, meta){
                    return moneyFun.format(moneyFun.mul(full.assets,full.proportion));
                }
            },
            {data: "rmk", bSortable: true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>\
                   <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
                   <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;
                }}
        ]
    });
});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/cms/guarantee/guaExGuaranteeAmount/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaExGuaranteeAmount/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaExGuaranteeAmount/view/" + data.id
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/cms/guarantee/guaExGuaranteeAmount/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaExGuaranteeAmount/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/guarantee/guaExGuaranteeAmount/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
