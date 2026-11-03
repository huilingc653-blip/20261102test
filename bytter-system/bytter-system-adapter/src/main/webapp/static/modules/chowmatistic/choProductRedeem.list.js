var dict;
bt.use(['form','datatable','selectType','dicts','rateType','corp','dateType','moneyType'],function () {
    var selectType=bt.selectType.init();
    dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
    var rateType = bt.rateType.getFun();
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        checkbox: true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/chowmatistic/choproductredeem/datatable"
        },
        buttons:[
            {
                hide: bt.isnotResource('cho:productRedeem:choProductRedeem:apply'),
                text:bt.lang('common.button.add'),
                action:function (e, dt, node, config) {
                    add();
                }
            },{
                hide: bt.isnotResource('cho:productRedeem:choProductRedeem:apply'),
                text:bt.lang('common.button.batchSubmit'),
                action:function (e, dt, node, config)
                {
                    var data = dt.rows('.selected').data();
                    batchSubmit(data);
                }
            }
            ,{
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data:"corpName",bSortable:true,className:'center'},
            {data:"bankTypeName",bSortable:true,className:'center'},
            {data:"financialTypeName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
            {data:"productCode",bSortable:true,className:'center'},
            {data:"productName",bSortable:true,className:'center'},
            {data:"productType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("cho.productType",full.productType);
                }
            },
            {data:"bankAcc",bSortable:true,className:'center'},
            {data:"curName",bSortable:true,className:'center'},
            {data:"status",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("common.status",full.status);
                }},
            {data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('cho:productRedeem:choProductRedeem:apply',full.corpId)){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    })
});

function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/chowmatistic/choproductredeem/add');
        return;
    }
    parent.layer.open({
        type:2,
        area:['850px','530px'],
        maxmin:true,
        content:basePath+'/chowmatistic/choproductredeem/add',
        end:function () {
            table && table.draw();
        }
    });
}

function update(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + '/chowmatistic/choproductredeem/update?id=' + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: basePath + "/chowmatistic/choproductredeem/update?id=" + data.id,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function del(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
        $.post(basePath + "/chowmatistic/choproductredeem/delete/" + data.id,function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg,{icon: result.success?1:2});
        },'JSON');
    })
}

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/chowmatistic/choproductredeem/view/" + data.id
    })
}

function batchSubmit(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        $.post(basePath + "/chowmatistic/choproductredeem/batchSubmit",{ids:ids}, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
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
    var url = basePath + "/chowmatistic/choproduct/json?financialType=" + data.value;
    bt.selectType.get("eqs_productCode").attr("data-url", url);
    bt.selectType.get("likes_productName").attr("data-url", url);
    bt.dicts.render()
}

function resetProductType() {
    dict.get("productType").attr({'data-filterData':''})
    var url = basePath + "/chowmatistic/choproduct/json";
    bt.selectType.get("likes_productName").attr("data-url", url);
    bt.selectType.get("eqs_productCode").attr("data-url", url);
}

// beforeInitCorp = function (nodes) {
//     $.ajax({
//         url: basePath + "/chowmatistic/choproductreq/filterBySource",
//         method: 'POST',
//         data: {corpVOList: JSON.stringify(nodes)},
//         dataType: 'json',
//         async:false,
//         success: function (data) {
//             nodes = data;
//         }
//     });
//     return nodes;
// }