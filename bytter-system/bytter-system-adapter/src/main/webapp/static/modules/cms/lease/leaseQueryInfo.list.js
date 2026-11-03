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
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData:"false"
        },
        ajax: {
            'url': basePath + "/cms/lease/financing/info/queryDatatable"
        },
        // ignoreButtons: ['print'],
        buttons: [
            {
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
            {data: "status", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("lease.status", data);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if(bt.isResource('cms:lease:financing:apply:query')) {
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="toList(this)">'+bt.lang("cms.lease.LeaseObjectInfo")+'</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }}
        ]
    });
});

function toList(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms:lease:sub:mater:infoList'),basePath + "/cms/lease/UpdateObjectInfo/viewList/"+ data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/lease/UpdateObjectInfo/viewList/"+ data.id,
        end:function(){
            location.reload();
        }
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
