bt.use(['form','datatable','btdate','dateType','selectType','corp'], function(){
    var dateType = bt.dateType.init();
    bt.selectType.init()
    bt.corp.init()
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        order: [[1,'asc']],
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/bond/extend/extendRegisterInfo/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        }
        ,columns: [
            {data:"bondTypeName",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"bondCode",bSortable:true},
            {data:"bondFullName",bSortable:true},
            {data:"bondShortName",bSortable:true},
            {data:"bondDeadline",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return data + bt.dict("remainTerm.unit", full.deadlineUnit);
                }
            },
            {data:"bondCurName",bSortable:true},
            {data:"issueCurTotal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issueCurTotal);
                }
            },
            {data:"issueRmbTotal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.issueRmbTotal);
                }
            },
            {data:"purpose",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"issueStatusName",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.issueStatus === 95 ? '审批通过' : data;
                }
            },
            {data:"deferredNum",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.deferredNum === null ? 0 : data;
                }
             },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = ''
                    /*if (full.issueStatus && full.issueStatus === 95) {*/
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a> ';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="query(this)">' + bt.lang("common.button.extendDetal") + '</a> ';
                    /*}*/
                    /** 债劵发行表id */
                    return html;
                }}
        ]
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/bond/extend/list/"+data.id+"/1");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/extend/list/"+data.id+"/1",
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function query(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath +  "/cms/bond/extend/list/"+data.id+"/0");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/extend/list/"+data.id+"/0"
        });
    }
}

