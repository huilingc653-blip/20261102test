bt.use(['form','datatable','corp','moneyType','btImport','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    var dicts = bt.dicts.init();
    bt.corp.init();//单位
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        checkbox:true,
        order: [3, 'desc'],
        ajax: {
            'url': basePath+"/cms/provide/loanInfoApply/datatableUpdate"
        },
        buttons: [
            {
                hide: bt.isnotResource('cms:loaninfo:apply:edit'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                hide:bt.isnotResource('cms:loaninfo:apply:edit'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName", bSortable: false},
            {data:"billCode", bSortable: true},
            {data:"createDate", bSortable: true},
            {data:"bankName", bSortable: false},
            {data:"curName", bSortable: false},
            {data:"plMoney", bSortable: true, className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.plMoney);
                }},
            {data:"plDate", bSortable: true},
            {data:"repayDate", bSortable: true},
            {data:"repayTypeName", bSortable: false},
            {data:"contractTermName", bSortable: false},
            {data:"plTerm", bSortable: false, render: function ( data, type, full, meta ){
                    var htm = ""
                    if(full.plTerm == null){
                        htm += 0+"";
                    }else{
                        htm += full.plTerm;
                    }
                    return htm + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
                }},
            {data:"status", bSortable: false, render: function ( data, type, full, meta ){
                    return bt.dict('cms.bizStatus', full.bizStatus);
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '';
                    if(full.status != 11) {
                        if (bt.isResource('cms:loaninfo:apply:edit')) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a> ';
                        }
                        if (bt.isResource('cms:loaninfo:apply:edit')) {
                            html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                        }
                    }
                    html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                    // html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.workFlow.trackFlowByBusinessId(\''+ full.id +'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                }
            ]
        }
    });

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "70000009",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/cms/provide/loanInfoApply/saveImport",
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
    };
});

function edit(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/provide/loanInfoApply/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loanInfoApply/update/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/cms/provide/loanInfoApply/delete/" + data.id, function(result){
            bt.loading.hide();
            bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function add(el){

    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/provide/loanInfoApply/addLoanInfo");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loanInfoApply/addLoanInfo",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.billCode + "&show=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.billCode + "&show=1"
    });
}

//日志列表
function listMsg(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);

        }
    }
}
