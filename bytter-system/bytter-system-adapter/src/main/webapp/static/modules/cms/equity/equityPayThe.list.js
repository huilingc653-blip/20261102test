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
            'url': basePath + "/cms/equity/payThe/datatablePass"
        },
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
            {data: "equityTypeName", bSortable: true},
            {data:"corpName",bSortable:true},
            {data:"financingDate",bSortable:true},
            {data:"financingUse",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"money",className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.money);
                }},
            {data:"rmk",bSortable:true},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisExpenseDetail.status", full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                        if(bt.isResource('cms:equity:pay:op',full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("table.cmsEquity.payOrMove") + '</a><span class="text-explode">|</span>';
                        }
                        if(bt.isResource('cms:equity:pay:view')) {
                            html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                        }

                    return html;
                }}
        ]
    });
});

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('table.cmsEquity.payOrMove'),basePath + "/cms/equity/payThe/equityDetails/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/payThe/equityDetails/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/equity/info/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/info/view/" + data.id
    });
}
