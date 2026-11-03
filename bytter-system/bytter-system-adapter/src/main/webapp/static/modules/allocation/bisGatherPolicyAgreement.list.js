var datatable;
bt.use(['form','datatable','dicts','dateType','select','corp','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.corp.init();

    importExcel = function () {
        up = bt.btImport.init({
            code: "10000088",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/allocation/bisgatherpolicy/saveAgreementImport",
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
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/allocation/bisgatherpolicy/agreementDatatable"
        },
        ignoreButtons:['colvis'],
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }/*,{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }*/
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:false},
            {data:"isAgreement",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isAgreement);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;
                }}
        ]
    });

});
function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/bisgatherpolicy/addAgreement",
        end:function(){
            bt.navtab.refreshCurrentTable();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisgatherpolicy/updateAgreement?id=" + data.id,
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/allocation/bisgatherpolicy/deleteAgreement/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
