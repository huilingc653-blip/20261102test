bt.use(['form','datatable','selectType','dicts'],function () {
    selectType=bt.selectType.init();
    dict = bt.dicts.init();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproduct/datatable"
        },
        buttons:[
            {
                text:bt.lang('common.button.add'),
                action:function (e, dt, node, config) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data:"bankTypeName",bSortable:true,className:'center'},
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"productType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("cho.productType",full.productType);
                }
            },
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            // {data:"validSign",bSortable:true,className:'center',
            //     render: function ( data, type, full, meta ) {
            //         return bt.dict("sf_yn",full.validSign);
            //     }
            // },
            /*{data:"certificateWay",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("choBankFinance.certificateWay",full.certificateWay);
                }
            },*/
            {data:"rmk",bSortable:false,className:'center'},
            {data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
                        <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;
            }}
        ]
    })
});

function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/chowmatistic/choproduct/add');
        return;
    }
    parent.layer.open({
        type:2,
        area:['850px','530px'],
        maxmin:true,
        content:basePath+'/chowmatistic/choproduct/add',
        end:function () {
            table && table.draw();
        }
    });
}

function update(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + '/chowmatistic/choproduct/update?id=' + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: basePath + "/chowmatistic/choproduct/update?id=" + data.id,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function del(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
        $.post(basePath + "/chowmatistic/choproduct/delete/" + data.id,function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg,{icon: result.success?1:2});
        },'JSON');
    })
}

// 理财类型回调函数
function financialTypeCallback(data) {
    var filterData
    if (data.value === '1') {
        filterData = '3,4,5,6'
    } else if (data.value === '0') {
        filterData = '1,2'
    } else if (data.value === '2') {
        filterData = '2,3,4,5,6'
    }
    dict.get("productType").attr({'data-filterData': filterData})

    bt.dicts.render()
}

function resetProductType() {
    dict.get("productType").attr({'data-filterData':''})
}